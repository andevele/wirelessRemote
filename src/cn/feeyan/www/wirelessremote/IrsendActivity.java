package cn.feeyan.www.wirelessremote;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class IrsendActivity extends Activity {
	private static final String TAG = "IrsendActivity";
	
	private ImageView menuKey;
	private ImageView exitkey;
	private ImageView homeKey;
	private ImageView muteKey;
	private ImageView powerKey;
	private ImageView upKey;
	private ImageView downKey;
	private ImageView leftKey;
	private ImageView rightKey;
	private ImageView okKey;

	private int recivebuffersize = 30;

	private Boolean startrunflag = true;
	private String tmpBufferString;
	private SeekBar voiceSeekBar;
	boolean flag = true;
	private long mExitTime;
	public static final String PREFIX = "client";
	private static final String VOLUME = "volumeset";
	Vibrator vibrator = null;
	private String connectionValue;
	private ImageView tokeyBoard;
	private SendUdpDataPacket mSendUdpDataPacket;
	private Handler mHandler;
	private ImageView connectingTvKey;
	private SharedPreferences sp;
	private Handler voiceSeekBarHandler;
	private ImageView tvSource;
	private ImageView circleLabel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		sp = getSharedPreferences(Constants.SP_FILE_NAME, Context.MODE_PRIVATE);
		initViews();
		mSendUdpDataPacket = new SendUdpDataPacket();
		mHandler = new updateUIHandler();
		/*
		 * Intent mIntent = getIntent(); if (mIntent != null) { connectionValue
		 * = mIntent.getStringExtra(Constants.CONNECTION_KEY); }
		 */
		String value = sp.getString(Constants.CONNECTION_KEY_SHARED, "");
		if (value != null && value.length() == 6 && !value.equals("")) {
			connectionValue = value;
		}
	}

	private void initViews() {
		muteKey = (ImageView) findViewById(R.id.label_mute);
		muteKey.setOnClickListener(mKeyOnClickListener);

		tvSource = (ImageView) findViewById(R.id.label_tv_source);
		tvSource.setOnClickListener(mKeyOnClickListener);

		powerKey = (ImageView) findViewById(R.id.label_power);
		powerKey.setOnClickListener(mKeyOnClickListener);

		menuKey = (ImageView) findViewById(R.id.label_menu);
		menuKey.setOnClickListener(mKeyOnClickListener);

		exitkey = (ImageView) findViewById(R.id.label_back);
		exitkey.setOnClickListener(mKeyOnClickListener);

		homeKey = (ImageView) findViewById(R.id.label_home);
		homeKey.setOnClickListener(mKeyOnClickListener);

		upKey = (ImageView) findViewById(R.id.label_up);
		upKey.setOnClickListener(mKeyOnClickListener);
		ViewTreeObserver keyTreeObserver = upKey.getViewTreeObserver();
		keyTreeObserver.addOnGlobalLayoutListener(keyTreeObserverListener);

		downKey = (ImageView) findViewById(R.id.label_down);
		downKey.setOnClickListener(mKeyOnClickListener);

		leftKey = (ImageView) findViewById(R.id.label_left);
		leftKey.setOnClickListener(mKeyOnClickListener);

		rightKey = (ImageView) findViewById(R.id.label_right);
		rightKey.setOnClickListener(mKeyOnClickListener);

		okKey = (ImageView) findViewById(R.id.label_ok);
		okKey.setOnClickListener(mKeyOnClickListener);

		voiceSeekBar = (SeekBar) findViewById(R.id.voice_seekbar);
		voiceSeekBar.setOnSeekBarChangeListener(mVoiceSeekBarChangeListener);
		voiceSeekBarHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				Bundle bundle = msg.getData();
				String value = bundle.getString("voiceKey");
				// Log.d("irsend", "value = " + value);
				if (value != null && !value.equals("") && voiceSeekBar != null) {
					int currentProgress = Integer.parseInt(value);
					if (currentProgress <= 5) {
						currentProgress = 0;
					} else if (currentProgress > 5 && currentProgress <= 15) {
						currentProgress = 10;
					} else if (currentProgress > 15 && currentProgress <= 25) {
						currentProgress = 20;
					} else if (currentProgress > 25 && currentProgress <= 35) {
						currentProgress = 30;
					} else if (currentProgress > 35 && currentProgress <= 45) {
						currentProgress = 40;
					} else if (currentProgress > 45 && currentProgress <= 55) {
						currentProgress = 50;
					} else if (currentProgress > 55 && currentProgress <= 65) {
						currentProgress = 60;
					} else if (currentProgress > 65 && currentProgress <= 75) {
						currentProgress = 70;
					} else if (currentProgress > 75 && currentProgress <= 85) {
						currentProgress = 80;
					} else if (currentProgress > 85 && currentProgress <= 95) {
						currentProgress = 90;
					} else if (currentProgress > 95) {
						currentProgress = 100;
					}

					voiceSeekBar.setProgress(currentProgress);
				}
			}
		};

		tokeyBoard = (ImageView) findViewById(R.id.label_keyboard);
		tokeyBoard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(IrsendActivity.this, KeyBoardActivity.class);
				intent.putExtra(Constants.STORE_CONNECTION_KEY, connectionValue);
				startActivity(intent);
			}
		});

		connectingTvKey = (ImageView) findViewById(R.id.label_connecting_tv);
		connectingTvKey.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(IrsendActivity.this, ConnectionActivity.class);
				startActivity(intent);
				finish();
			}

		});

		circleLabel = (ImageView) findViewById(R.id.circle_label);
	}

	View.OnClickListener mKeyOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// Log.d("irsend", "the on press");
			vibrator.vibrate(new long[] { 0, 100 }, -1);

			switch (v.getId()) {
			case R.id.label_mute:
				setkeyName("mute");
				break;
			case R.id.label_tv_source:
				setkeyName("source");
				break;
			case R.id.label_power:
				setkeyName("power");
				break;
			case R.id.label_menu:
				setkeyName("menu");
				break;
			case R.id.label_back:
				setkeyName("exit");
				break;
			case R.id.label_home:
				setkeyName("home");
				break;
			case R.id.label_up:
				setkeyName("up");
				break;
			case R.id.label_down:
				setkeyName("down");
				break;
			case R.id.label_left:
				setkeyName("left");
				break;
			case R.id.label_right:
				setkeyName("right");
				break;
			case R.id.label_ok:
				setkeyName("ok");
				break;

			default:
				tmpBufferString = "";
				break;
			}

			new Thread() {

				public void run() {
					if (tmpBufferString != null) {
						boolean isSend = mSendUdpDataPacket.send(HexToReverse.getHexByte(tmpBufferString));
						if (!isSend) {
							mHandler.sendEmptyMessageDelayed(Constants.SEND_FAILURE, 200);
						}
					}
				}
			}.start();

			return;

		}
	};

	// 动态设置左、右、上、下四个按键的layout_margin，这样就不会采用布局中的dp来设置，影响自适应多屏
	OnGlobalLayoutListener keyTreeObserverListener = new OnGlobalLayoutListener() {

		@Override
		public void onGlobalLayout() {
			// TODO Auto-generated method stub
			int vircleHeight = circleLabel.getHeight();
			int vircleWeight = circleLabel.getWidth();
			LayoutParams upKeyParams = (LayoutParams) upKey.getLayoutParams();
			LayoutParams leftKeyParams = (LayoutParams) leftKey.getLayoutParams();
			int marginBottom = vircleHeight * 1 / 2;
			int marginRight = vircleWeight * 1 / 2;
			upKeyParams.setMargins(0, 0, 0, marginBottom);
			leftKeyParams.setMargins(0, 0, marginRight, 0);
			upKey.setLayoutParams(upKeyParams);
			leftKey.setLayoutParams(leftKeyParams);
		}

	};

	class updateUIHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == Constants.SEND_FAILURE)
				Toast.makeText(getApplicationContext(), R.string.check_network_connection, Toast.LENGTH_LONG).show();
		}
	}

	private void setkeyName(String keyName) {
		tmpBufferString = PREFIX + keyName;
	}

	private OnSeekBarChangeListener mVoiceSeekBarChangeListener = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			// vibrator.vibrate(new long[] { 0, 100 }, -1);
			new Thread() {
				public void run() {
					String currentProgress = String.valueOf(voiceSeekBar.getProgress());
					String sendProgress = PREFIX + connectionValue + VOLUME + currentProgress.trim();
					mSendUdpDataPacket.send(HexToReverse.getReversedHex(sendProgress));
				}
			}.start();
			vibrator.vibrate(new long[] { 0, 100 }, -1);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			// 振动效果
			// vibrator.vibrate(new long[] { 0, 100 }, -1);
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			// TODO Auto-generated method stub
			Log.i("progress is: ", "" + progress);
			// vibrator.vibrate(new long[] { 0, 100 }, -1);
		}
	};

	View.OnLongClickListener mLongClickListener = new View.OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub

			new Thread() {

				public void run() {

					// send("siviton");

				}
			}.start();

			return false;
		}
	};

	protected void onPause() {

		startrunflag = false;
		super.onPause();
	};

	protected void onResume() {

		startrunflag = true;
		/*
		 * 该线程用于接收来自服务端的数据,比如,音量大小等; 或者接收当前客户端发送的数据，用于自测调试
		 * 
		 */
		new Thread() {

			public void run() {

				byte[] inBuff = new byte[recivebuffersize];

				DatagramSocket receivesocket = null;

				DatagramPacket inPacket = new DatagramPacket(inBuff, inBuff.length);
				if (receivesocket == null) {
					try {
						receivesocket = new DatagramSocket(Constants.SERVER_PORT);
					} catch (SocketException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				if (receivesocket != null) {
					while (startrunflag) {
						try {
							receivesocket.receive(inPacket);
							byte[] byteData = inPacket.getData();
							String hex = HexToReverse.printHexString(byteData);
							String receivedHex = hex.substring(0, hex.indexOf('0'));
							Log.d(TAG, "-----------receivedHex = " + receivedHex);
							String receivedStringFromServer = HexToReverse.toStringHex(receivedHex);
							Log.d(TAG, "-----------receivedStringFromServer = " + receivedStringFromServer);

						} catch (IOException e) {

							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					receivesocket.close();
				}
			}
		}.start();

		super.onResume();
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				String exitApp = getString(R.string.exit_app);
				Toast.makeText(this, exitApp, Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();

			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		muteKey = null;
		powerKey = null;
		menuKey = null;
		exitkey = null;
		homeKey = null;
		upKey = null;
		downKey = null;
		leftKey = null;
		rightKey = null;
		okKey = null;
		voiceSeekBar = null;
		tokeyBoard = null;
	}
}
