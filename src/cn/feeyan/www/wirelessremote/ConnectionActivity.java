package cn.feeyan.www.wirelessremote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.feeyan.www.wirelessremote.Constants;

public class ConnectionActivity extends Activity {
	private EditText connectValue;
	private Button connectButton;
	private TextView infomationText;
	private SharedPreferences sp;
	private Button backToMainActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		sp = getSharedPreferences(Constants.SP_FILE_NAME, Context.MODE_PRIVATE);
		connectValue = (EditText) findViewById(R.id.connect_value);
		connectValue.addTextChangedListener(new ConnectTextWatcher());
		connectButton = (Button) findViewById(R.id.login_button);
		connectButton.setOnClickListener(mButtonOnClickListener);
		infomationText = (TextView) findViewById(R.id.infomation_label);
		backToMainActivity = (Button) findViewById(R.id.back_to_mainactivity);
		backToMainActivity.setOnClickListener(mButtonOnClickListener);

	}

	OnClickListener mButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.login_button:
				String value = connectValue.getText().toString().trim();
				if (value != null && !value.equals("")) {
					if (value.length() == 6) {
						Intent mIntent = new Intent(ConnectionActivity.this,
								IrsendActivity.class);
						// mIntent.putExtra(Constants.CONNECTION_KEY, value);
						sp.edit()
								.putString(Constants.CONNECTION_KEY_SHARED,
										value).commit();
						startActivity(mIntent);
						finish();
					} else {
						displayInformation(R.string.wrongvalue);
					}
				} else {
					displayInformation(R.string.nullvalue);
				}
				break;

			case R.id.back_to_mainactivity:
				Intent mIntent = new Intent(ConnectionActivity.this,
						IrsendActivity.class);
				startActivity(mIntent);
				finish();
				break;

			}

		}

	};

	class ConnectTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			infomationText.setVisibility(View.INVISIBLE);
		}

	}

	private void displayInformation(int id) {
		connectValue.setText("");
		infomationText.setVisibility(View.VISIBLE);
		infomationText.setText(getResources().getText(id));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			System.exit(0);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
