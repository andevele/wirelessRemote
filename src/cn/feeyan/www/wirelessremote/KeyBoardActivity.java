package cn.feeyan.www.wirelessremote;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.feeyan.www.wirelessremote.Constants;

public class KeyBoardActivity extends Activity {

	private boolean isLetterLower = true;

	// 一个对象数组，保存所有数字,大小写字母,5个特殊字符按键对应的对象
	private ImageView[] imgsView = new ImageView[41];

	// 所有数字,大小写字母,5个特殊字符按键的布局id
	private int[] btnsNumber = new int[] { R.id.id_number_one, R.id.id_number_two, R.id.id_number_three,
			R.id.id_number_four, R.id.id_number_five, R.id.id_number_six, R.id.id_number_seven, R.id.id_number_eight,
			R.id.id_number_nine, R.id.id_number_zero,

			R.id.id_letter_q_lower, R.id.id_letter_w_lower, R.id.id_letter_e_lower, R.id.id_letter_r_lower,
			R.id.id_letter_t_lower, R.id.id_letter_y_lower, R.id.id_letter_u_lower, R.id.id_letter_i_lower,
			R.id.id_letter_o_lower, R.id.id_letter_p_lower, R.id.id_letter_a_lower, R.id.id_letter_s_lower,
			R.id.id_letter_d_lower, R.id.id_letter_f_lower, R.id.id_letter_g_lower, R.id.id_letter_h_lower,
			R.id.id_letter_j_lower, R.id.id_letter_k_lower, R.id.id_letter_l_lower, R.id.id_letter_z_lower,
			R.id.id_letter_x_lower, R.id.id_letter_c_lower, R.id.id_letter_v_lower, R.id.id_letter_b_lower,
			R.id.id_letter_n_lower, R.id.id_letter_m_lower,

			R.id.id_symbol_backspace, R.id.id_symbol_at, R.id.id_symbol_dot, R.id.id_symbol_space,
			R.id.id_symbol_enter };
	// 所有大写字母图片
	private int[] btnUpperLetters = new int[] { R.drawable.bg_letter_q_upper, R.drawable.bg_letter_w_upper,
			R.drawable.bg_letter_e_upper, R.drawable.bg_letter_r_upper, R.drawable.bg_letter_t_upper,
			R.drawable.bg_letter_y_upper, R.drawable.bg_letter_u_upper, R.drawable.bg_letter_i_upper,
			R.drawable.bg_letter_o_upper, R.drawable.bg_letter_p_upper, R.drawable.bg_letter_a_upper,
			R.drawable.bg_letter_s_upper, R.drawable.bg_letter_d_upper, R.drawable.bg_letter_f_upper,
			R.drawable.bg_letter_g_upper, R.drawable.bg_letter_h_upper, R.drawable.bg_letter_j_upper,
			R.drawable.bg_letter_k_upper, R.drawable.bg_letter_l_upper, R.drawable.bg_letter_z_upper,
			R.drawable.bg_letter_x_upper, R.drawable.bg_letter_c_upper, R.drawable.bg_letter_v_upper,
			R.drawable.bg_letter_b_upper, R.drawable.bg_letter_n_upper, R.drawable.bg_letter_m_upper };

	// 所有小写字母图片
	private int[] btnLowerLetters = new int[] { R.drawable.bg_letter_q_lower, R.drawable.bg_letter_w_lower,
			R.drawable.bg_letter_e_lower, R.drawable.bg_letter_r_lower, R.drawable.bg_letter_t_lower,
			R.drawable.bg_letter_y_lower, R.drawable.bg_letter_u_lower, R.drawable.bg_letter_i_lower,
			R.drawable.bg_letter_o_lower, R.drawable.bg_letter_p_lower, R.drawable.bg_letter_a_lower,
			R.drawable.bg_letter_s_lower, R.drawable.bg_letter_d_lower, R.drawable.bg_letter_f_lower,
			R.drawable.bg_letter_g_lower, R.drawable.bg_letter_h_lower, R.drawable.bg_letter_j_lower,
			R.drawable.bg_letter_k_lower, R.drawable.bg_letter_l_lower, R.drawable.bg_letter_z_lower,
			R.drawable.bg_letter_x_lower, R.drawable.bg_letter_c_lower, R.drawable.bg_letter_v_lower,
			R.drawable.bg_letter_b_lower, R.drawable.bg_letter_n_lower, R.drawable.bg_letter_m_lower };
	// 所有特殊字符
	private int[] btnSpectials = new int[] { R.id.id_symbol_equal, R.id.id_symbol_comma, R.id.id_symbol_colon,
			R.id.id_symbol_single_quote, R.id.id_symbol_semicolon, R.id.id_symbol_quote, R.id.id_symbol_tilde,
			R.id.id_symbol_vertical_line, R.id.id_symbol_backslash, R.id.id_symbol_underline,

			R.id.id_symbol_pound_sign, R.id.id_symbol_question_mark, R.id.id_symbol_percent, R.id.id_symbol_plus,
			R.id.id_symbol_minus, R.id.id_symbol_left_parenthese, R.id.id_symbol_right_parenthese,
			R.id.id_symbol_left_bracket, R.id.id_symbol_right_bracket, R.id.id_symbol_exclamatory_mark,

			R.id.id_symbol_left_brace, R.id.id_symbol_right_brace, R.id.id_symbol_left_guillemets,
			R.id.id_symbol_right_guillemets };

	private Vibrator vibrator;
	private String tmpBufferString;

	private String connectionValue;

	private SendUdpDataPacket mSendUdpDataPacket;

	private Handler mHandler;
	private ImageView switchUpperLowerImageView;
	private ImageView specialImageView;
	private LinearLayout mMainLinearlayout;
	private FrameLayout mContainer;
	private ImageView specialImageViewReverse;
	private LinearLayout mSpecialLetterlayout;
	private ImageView[] imgsSpecialView = new ImageView[24];

	private ImageView backToHomePage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.default_layout);
		mContainer = (FrameLayout) findViewById(R.id.main_container);

		mMainLinearlayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.keyboard_layout, null);
		mContainer.addView(mMainLinearlayout);

		vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

		Intent intent = getIntent();
		if (intent != null) {
			connectionValue = intent.getStringExtra(Constants.STORE_CONNECTION_KEY);
		}

		mSendUdpDataPacket = new SendUdpDataPacket();

		for (int i = 0; i < btnsNumber.length; i++) {
			imgsView[i] = (ImageView) mMainLinearlayout.findViewById(btnsNumber[i]);
			imgsView[i].setOnClickListener(imgsViewClickListener);
		}
		mHandler = new updateUIHandler();

		switchUpperLowerImageView = (ImageView) mMainLinearlayout.findViewById(R.id.id_symbol_switch);
		specialImageView = (ImageView) mMainLinearlayout.findViewById(R.id.id_symbol_special);
		switchUpperLowerImageView.setOnClickListener(switchClickListener);
		specialImageView.setOnClickListener(switchClickListener);

		backToHomePage = (ImageView) mMainLinearlayout.findViewById(R.id.id_back_to_homepage);
		backToHomePage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				// System.exit(0);
			}
		});

	}

	private Handler mSwitchToSpecialHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == Constants.SEND_SPECIAL) {
				mSpecialLetterlayout = (LinearLayout) LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.keyboard_special_letter_layout, null);
				mContainer.removeAllViews();
				mContainer.addView(mSpecialLetterlayout);
				if (mSpecialLetterlayout != null)
					for (int i = 0; i < btnSpectials.length; i++) {
						imgsSpecialView[i] = (ImageView) mSpecialLetterlayout.findViewById(btnSpectials[i]);
						imgsSpecialView[i].setOnClickListener(imgsSpecialViewClickListener);
					}
				specialImageViewReverse = (ImageView) mSpecialLetterlayout.findViewById(R.id.id_symbol_special_2);
				specialImageViewReverse.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						mContainer.removeAllViews();
						mContainer.addView(mMainLinearlayout);
					}
				});

			}
		}

	};

	private OnClickListener switchClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.id_symbol_switch:
				switchUpperLower();
				break;
			case R.id.id_symbol_special:
				new Thread() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mSwitchToSpecialHandler.sendEmptyMessage(Constants.SEND_SPECIAL);
					}

				}.start();

				break;
			default:
				break;
			}
		}

	};

	private OnClickListener imgsViewClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub

			vibrator.vibrate(new long[] { 0, 100 }, -1);

			switch (view.getId()) {
			case R.id.id_number_one:
				setkeyName("one");
				break;
			case R.id.id_number_two:
				setkeyName("two");
				break;
			case R.id.id_number_three:
				setkeyName("three");
				break;
			case R.id.id_number_four:
				setkeyName("four");
				break;
			case R.id.id_number_five:
				setkeyName("five");
				break;
			case R.id.id_number_six:
				setkeyName("six");
				break;
			case R.id.id_number_seven:
				setkeyName("seven");
				break;
			case R.id.id_number_eight:
				setkeyName("eight");
				break;
			case R.id.id_number_nine:
				setkeyName("nine");
				break;
			case R.id.id_number_zero:
				setkeyName("zero");
				break;
			case R.id.id_letter_a_lower:
				setLetterkeyName("la");
				break;
			case R.id.id_letter_b_lower:
				setLetterkeyName("lb");
				break;
			case R.id.id_letter_c_lower:
				setLetterkeyName("lc");
				break;
			case R.id.id_letter_d_lower:
				setLetterkeyName("ld");
				break;
			case R.id.id_letter_e_lower:
				setLetterkeyName("le");
				break;
			case R.id.id_letter_f_lower:
				setLetterkeyName("lf");
				break;
			case R.id.id_letter_g_lower:
				setLetterkeyName("lg");
				break;
			case R.id.id_letter_h_lower:
				setLetterkeyName("lh");
				break;
			case R.id.id_letter_i_lower:
				setLetterkeyName("li");
				break;
			case R.id.id_letter_j_lower:
				setLetterkeyName("lj");
				break;
			case R.id.id_letter_k_lower:
				setLetterkeyName("lk");
				break;
			case R.id.id_letter_l_lower:
				setLetterkeyName("ll");
				break;
			case R.id.id_letter_m_lower:
				setLetterkeyName("lm");
				break;
			case R.id.id_letter_n_lower:
				setLetterkeyName("ln");
				break;
			case R.id.id_letter_o_lower:
				setLetterkeyName("lo");
				break;
			case R.id.id_letter_p_lower:
				setLetterkeyName("lp");
				break;
			case R.id.id_letter_q_lower:
				setLetterkeyName("lq");
				break;
			case R.id.id_letter_r_lower:
				setLetterkeyName("lr");
				break;
			case R.id.id_letter_s_lower:
				setLetterkeyName("ls");
				break;
			case R.id.id_letter_t_lower:
				setLetterkeyName("lt");
				break;
			case R.id.id_letter_u_lower:
				setLetterkeyName("lu");
				break;
			case R.id.id_letter_v_lower:
				setLetterkeyName("lv");
				break;
			case R.id.id_letter_w_lower:
				setLetterkeyName("lw");
				break;
			case R.id.id_letter_x_lower:
				setLetterkeyName("lx");
				break;
			case R.id.id_letter_y_lower:
				setLetterkeyName("ly");
				break;
			case R.id.id_letter_z_lower:
				setLetterkeyName("lz");
				break;
			case R.id.id_symbol_at:
				setkeyName("at");
				break;
			case R.id.id_symbol_dot:
				setkeyName("dot");
				break;
			case R.id.id_symbol_space:
				setkeyName("space");
				break;
			case R.id.id_symbol_enter:
				setkeyName("enter");
				break;
			case R.id.id_symbol_backspace:
				setkeyName("backspace");
				break;
			default:
				tmpBufferString = "";
				break;
			}

			new Thread(new SendInformationThread()).start();
		}

	};

	OnClickListener imgsSpecialViewClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			vibrator.vibrate(new long[] { 0, 100 }, -1);

			switch (view.getId()) {
			case R.id.id_symbol_equal:
				setkeyName("equal");
				break;
			case R.id.id_symbol_comma:
				setkeyName("comma");
				break;
			case R.id.id_symbol_colon:
				setkeyName("colon");
				break;
			case R.id.id_symbol_single_quote:
				setkeyName("singlequote");
				break;
			case R.id.id_symbol_semicolon:
				setkeyName("semicolon");
				break;
			case R.id.id_symbol_quote:
				setkeyName("quote");
				break;
			case R.id.id_symbol_tilde:
				setkeyName("tilde");
				break;
			case R.id.id_symbol_vertical_line:
				setkeyName("verticalline");
				break;
			case R.id.id_symbol_backslash:
				setkeyName("backslash");
				break;
			case R.id.id_symbol_underline:
				setkeyName("underline");
				break;
			case R.id.id_symbol_pound_sign:
				setkeyName("poundsign");
				break;
			case R.id.id_symbol_question_mark:
				setkeyName("questionmark");
				break;
			case R.id.id_symbol_percent:
				setkeyName("percent");
				break;
			case R.id.id_symbol_plus:
				setkeyName("plus");
				break;
			case R.id.id_symbol_minus:
				setkeyName("minus");
				break;
			case R.id.id_symbol_left_parenthese:
				setkeyName("leftparenthese");
				break;
			case R.id.id_symbol_right_parenthese:
				setkeyName("rightparenthese");
				break;
			case R.id.id_symbol_left_bracket:
				setkeyName("leftbracket");
				break;
			case R.id.id_symbol_right_bracket:
				setkeyName("rightbracket");
				break;
			case R.id.id_symbol_exclamatory_mark:
				setkeyName("exclamatory");
				break;
			case R.id.id_symbol_left_brace:
				setkeyName("leftbrace");
				break;
			case R.id.id_symbol_right_brace:
				setkeyName("rightbrace");
				break;
			case R.id.id_symbol_left_guillemets:
				setkeyName("leftguillemets");
				break;
			case R.id.id_symbol_right_guillemets:
				setkeyName("rightguillemets");
				break;

			default:
				break;
			}

			new Thread(new SendInformationThread()).start();
		}

	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	// 所有字幕大写和小写相互切换
	private void switchUpperLower() {
		if (isLetterLower) {
			switchUpperLowerImageView.setImageResource(R.drawable.symbol_switch_pressed);
			for (int i = 10; i < btnsNumber.length - 5; i++) {
				imgsView[i] = (ImageView) mMainLinearlayout.findViewById(btnsNumber[i]);
				imgsView[i].setImageResource(btnUpperLetters[i - 10]);
			}
			isLetterLower = false;
		} else {
			switchUpperLowerImageView.setImageResource(R.drawable.symbol_switch_normal);
			switchUpperLowerImageView.setFocusable(false);
			switchUpperLowerImageView.setFocusableInTouchMode(false);
			for (int i = 10; i < btnsNumber.length - 5; i++) {
				imgsView[i] = (ImageView) mMainLinearlayout.findViewById(btnsNumber[i]);
				imgsView[i].setImageResource(btnLowerLetters[i - 10]);
			}
			isLetterLower = true;
		}
	}

	private void setkeyName(String keyName) {
		tmpBufferString = IrsendActivity.PREFIX + keyName;
	}

	private void setLetterkeyName(String keyName) {
		if (isLetterLower) {
			tmpBufferString = IrsendActivity.PREFIX + keyName;
		} else {
			String upperLetter = new StringBuilder("u").append(keyName.charAt(1)).toString();
			tmpBufferString = IrsendActivity.PREFIX + upperLetter;
		}
	}

	private class SendInformationThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (tmpBufferString != null) {
				boolean isSend = mSendUdpDataPacket.send(HexToReverse.getHexByte(tmpBufferString));
				if (!isSend) {
					mHandler.sendEmptyMessageDelayed(Constants.SEND_FAILURE, 200);
				}
			}
		}

	}

	class updateUIHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == Constants.SEND_FAILURE)
				Toast.makeText(getApplicationContext(), R.string.check_network_connection, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		for (int i = 0; i < btnsNumber.length; i++) {
			imgsView[i] = null;
		}

		for (int i = 0; i < btnSpectials.length; i++) {
			imgsSpecialView[i] = null;
		}

		mMainLinearlayout = null;
		mSpecialLetterlayout = null;
		specialImageViewReverse = null;
		switchUpperLowerImageView = null;
	}
}
