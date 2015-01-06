package com.example.classtable;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditContactActivity extends Activity {

	private EditText mNameText;
	private EditText mRomText;
	private EditText mDayText;
	private EditText mStartText;
	private EditText mEndText;

	private Button mAddConfirmBtn;

	private MyDatabaseHelper mDatabaseHelper;
	private Cursor mDataCursor;

	 private void setAddContactResult() {
	 Intent i = new Intent();
	 setResult(RESULT_OK, i);
	 }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);

		mNameText = (EditText) findViewById(R.id.editText1);
		mRomText = (EditText) findViewById(R.id.editText2);
		mDayText = (EditText) findViewById(R.id.editText3);
		mStartText = (EditText) findViewById(R.id.editText4);
		mEndText = (EditText) findViewById(R.id.editText5);

		mAddConfirmBtn = (Button) findViewById(R.id.add_confirm);
		mAddConfirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					String name = mNameText.getText().toString();
					String rom = mRomText.getText().toString();
					String dayString = mDayText.getText().toString();
					String start_point_string = mStartText.getText().toString();
					String end_point_string = mEndText.getText().toString();

					if (dayString.equals("") || start_point_string.equals("")
							|| end_point_string.equals("")) {
						Toast.makeText(EditContactActivity.this, "输入有误！", Toast.LENGTH_SHORT).show();
					} else {
						int day = Integer.parseInt(dayString);
						int start_point = Integer.parseInt(start_point_string);
						int end_point = Integer.parseInt(end_point_string);
						mDatabaseHelper.insert(name, rom, day, start_point,
								end_point);
						//Toast.makeText(EditContactActivity.this, "成功添加~", Toast.LENGTH_SHORT).show();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}

				setAddContactResult(); // TODO 1

				finish();
			}
		});

		mDatabaseHelper = new MyDatabaseHelper(getApplicationContext());

		// Log.d(TAG, "1");

		mDataCursor = mDatabaseHelper.query();

	}

}
