package com.example.classtable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private int colors[] = { Color.rgb(0xee, 0xff, 0xff),
			Color.rgb(0xf0, 0x96, 0x09), Color.rgb(0x8c, 0xbf, 0x26),
			Color.rgb(0x00, 0xab, 0xa9), Color.rgb(0x99, 0x6c, 0x33),
			Color.rgb(0x3b, 0x92, 0xbc), Color.rgb(0xd5, 0x4d, 0x34),
			Color.rgb(0xcc, 0xcc, 0xcc) };
	/* 添加课程按钮 */
	private Button addClassBtn;

	/* 数据库 */
	private MyDatabaseHelper mDatabaseHelper;

	/* 数据库指针 */
	private Cursor mClassCursor;

	private String classInfo = "";

	/* 课程数组 */
	private ArrayList<Lesson> mClassArray;		// Lesson类包括从数据库中获取的课程名称， 教室， 星期， 上课时间
	private ArrayList<Lesson> mClassArray1;
	private ArrayList<Lesson> mClassArray2;
	private ArrayList<Lesson> mClassArray3;
	private ArrayList<Lesson> mClassArray4;
	private ArrayList<Lesson> mClassArray5;
	private ArrayList<Lesson> mClassArray6;
	private ArrayList<Lesson> mClassArray7;

	/*	课表列布局（天）	*/
	private LinearLayout ll1;	// 星期一
	private LinearLayout ll2;	// 星期二
	private LinearLayout ll3;
	private LinearLayout ll4;
	private LinearLayout ll5;
	private LinearLayout ll6;
	private LinearLayout ll7;
	
	/**
	 * 侧滑布局对象，用于通过手指滑动将左侧的菜单布局进行显示或隐藏。
	 */
	private SlidingLayout slidingLayout;

	/**
	 * menu按钮，点击按钮展示左侧布局，再点击一次隐藏左侧布局。
	 */
	private Button menuButton;

	/**
	 * 放在content布局中的ListView。
	 */
	private ListView contentListView;

	/**
	 * 作用于contentListView的适配器。
	 */
	ArrayList<Map<String, Object>> mDataList = new ArrayList<Map<String, Object>>();
	private ArrayAdapter<String> contentListAdapter;

	/**
	 * 用于填充contentListAdapter的数据源。
	 */
	private String[] contentItems = { " " };

	
	
	/* 对课程数组进行排序 */
	// 主要的排序依据： 根据课程开始时间来排序
	class SortByStart implements Comparator {
		public int compare(Object o1, Object o2) {
			Lesson s1 = (Lesson) o1;
			Lesson s2 = (Lesson) o2;
			if (s1.getStart() > s2.getStart())
				return 1;
			else if (s1.getStart() < s2.getStart())
				return -1;
			else
				return 0;
		}
	}

	
	
	/* 从数据库指针获取课程并保存在课程数组	*/
	private void getLessonArrayFromDB() {
		mClassCursor = mDatabaseHelper.query();
		if (mClassCursor == null || mClassCursor.moveToFirst() == false)
			return;

		mClassArray = new ArrayList<Lesson>();		// 超过星期的保存在这里
		mClassArray1 = new ArrayList<Lesson>();
		mClassArray2 = new ArrayList<Lesson>();
		mClassArray3 = new ArrayList<Lesson>();
		mClassArray4 = new ArrayList<Lesson>();
		mClassArray5 = new ArrayList<Lesson>();
		mClassArray6 = new ArrayList<Lesson>();
		mClassArray7 = new ArrayList<Lesson>();

		while (mClassCursor.moveToNext()) {
			String className = mClassCursor.getString(mClassCursor
					.getColumnIndex("name"));
			String classRom = mClassCursor.getString(mClassCursor
					.getColumnIndex("rom"));

			int classDay = mClassCursor.getInt(mClassCursor
					.getColumnIndex("day"));
			int classStart = mClassCursor.getInt(mClassCursor
					.getColumnIndex("start_point"));
			int classEnd = mClassCursor.getInt(mClassCursor
					.getColumnIndex("end_point"));

			Lesson a = new Lesson(className, classRom, classDay, classStart,
					classEnd);
			switch (classDay) {
			case 1:
				mClassArray1.add(a);
				break;
			case 2:
				mClassArray2.add(a);
				break;
			case 3:
				mClassArray3.add(a);
				break;
			case 4:
				mClassArray4.add(a);
				break;
			case 5:
				mClassArray5.add(a);
				break;
			case 6:
				mClassArray6.add(a);
				break;
			case 7:
				mClassArray7.add(a);
				break;
			default:
				mClassArray.add(a);
			}

		}

		Collections.sort(mClassArray, new SortByStart());
		Collections.sort(mClassArray1, new SortByStart());
		Collections.sort(mClassArray2, new SortByStart());
		Collections.sort(mClassArray3, new SortByStart());
		Collections.sort(mClassArray4, new SortByStart());
		Collections.sort(mClassArray5, new SortByStart());
		Collections.sort(mClassArray6, new SortByStart());
		Collections.sort(mClassArray7, new SortByStart());

	}

	/*	实现每天的课程显示		*/
	private void setLessonView() {
		setClassTableView(mClassArray1, ll1);
		setClassTableView(mClassArray2, ll2);
		setClassTableView(mClassArray3, ll3);
		setClassTableView(mClassArray4, ll4);
		setClassTableView(mClassArray5, ll5);
		setClassTableView(mClassArray6, ll6);
		setClassTableView(mClassArray7, ll7);
	}
	
	/* 根据课程数据来设置页面显示 */
	private void setClassTableView(ArrayList<Lesson> t, LinearLayout l) {
		if (t == null || t.isEmpty())
			return;
		
		Random random = new Random();
		int color = random.nextInt(6)+1;		// 随机获取颜色
				
		
		l.removeViews(1, l.getChildCount() - 1);
		
		int blank_start = 1;
		int blank_end = 14;
		
		int lesson_start = t.get(0).getStart();
		
		
		if(lesson_start > blank_start)
			setNoClass(l, lesson_start - blank_start, 0);
		
		int lesson_size = t.size();
		
		for(int i = 0; i < lesson_size; i++) {
			int first_lesson_start = t.get(i).getStart();
			int first_lesson_end = t.get(i).getEnd();
			
			String className = t.get(i).getName();
			String classRom = t.get(i).getRom();
			int classDay = t.get(i).getDay();
			
			setClass(l, className, classRom, "", "", first_lesson_end - first_lesson_start + 1, ++color);
			color %= 6;
			
			if(i < lesson_size - 1) {
				int second_lesson_start = t.get(i + 1).getStart();
				//int second_lesson_end = t.get(i + 1).getEnd();
				
				if(second_lesson_start > first_lesson_end + 1)
					setNoClass(l, second_lesson_start - first_lesson_end - 1, 0);		
			}
		}
		
		int lesson_end = t.get(lesson_size - 1).getEnd();
		
		if(blank_end > lesson_end)
			setNoClass(l, blank_end - lesson_end, 0);
		
	}
	
	 

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (data == null)
			return;
		
		else if (requestCode == 1) {
		
			if (resultCode == RESULT_OK) {
				getLessonArrayFromDB();
				setLessonView();
				Toast.makeText(MainActivity.this, "已添加课程", Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		slidingLayout = (SlidingLayout) findViewById(R.id.slidingLayout);
		menuButton = (Button) findViewById(R.id.add);

		contentListView = (ListView) findViewById(R.id.list1);
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("name", "我的课程表");
		map.put("image", R.drawable.classicon2);
		mDataList.add(map);

		map = new HashMap<String, Object>();
		map.put("name", "考试信息");
		map.put("image", R.drawable.examicon3);
		mDataList.add(map);

		map = new HashMap<String, Object>();
		map.put("name", "我的笔记");
		map.put("image", R.drawable.noteicon2);
		mDataList.add(map);

		SimpleAdapter contentListAdapter = new SimpleAdapter(this, mDataList,
				R.layout.item, new String[] { "name", "image" }, new int[] {
						R.id.name, R.id.image });
		// contentListAdapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1,
		// contentItems);
		contentListView.setAdapter(contentListAdapter);

		// 将监听滑动事件绑定在contentListView上
		slidingLayout.setScrollEvent(contentListView);

		menuButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 实现点击一下menu展示左侧布局，再点击一下隐藏左侧布局的功能
				if (slidingLayout.isLeftLayoutVisible()) {
					slidingLayout.scrollToRightLayout();
				} else {
					slidingLayout.scrollToLeftLayout();
				}
			}
		});

		addClassBtn = (Button) findViewById(R.id.set);
		addClassBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// getLessonArrayFromDB();				
				
				Intent i = new Intent(MainActivity.this,
						EditContactActivity.class);
				startActivityForResult(i, 1);

			}
		});

		mDatabaseHelper = new MyDatabaseHelper(getApplicationContext());


		// TODO ADD CLASS
				// 分别表示周一到周日
				 ll1 = (LinearLayout) findViewById(R.id.ll1);
				 ll2 = (LinearLayout) findViewById(R.id.ll2);
				 ll3 = (LinearLayout) findViewById(R.id.ll3);
				 ll4 = (LinearLayout) findViewById(R.id.ll4);
				 ll5 = (LinearLayout) findViewById(R.id.ll5);
				 ll6 = (LinearLayout) findViewById(R.id.ll6);
				 ll7 = (LinearLayout) findViewById(R.id.ll7);
				 
		getLessonArrayFromDB();
		setLessonView();
		
		 

	}

	void setClass(LinearLayout ll, String title, String place, String last,
			String time, int classes, int color) {
		View view = LayoutInflater.from(this)
				.inflate(R.layout.item_class, null);
		view.setMinimumHeight(dip2px(this, classes * 48));
		view.setBackgroundColor(colors[color]);
		((TextView) view.findViewById(R.id.title)).setText(title);
		((TextView) view.findViewById(R.id.place)).setText(place);
		((TextView) view.findViewById(R.id.last)).setText(last);
		((TextView) view.findViewById(R.id.time)).setText(time);
		// 为课程View设置点击的监听器
		view.setOnClickListener(new OnClickClassListener());
		TextView blank1 = new TextView(this);
		TextView blank2 = new TextView(this);
		blank1.setHeight(dip2px(this, classes));
		blank2.setHeight(dip2px(this, classes));
		ll.addView(blank1);
		ll.addView(view);
		ll.addView(blank2);
	}

	/**
	 * 设置无课（空百）
	 * 
	 * @param ll
	 * @param classes
	 *            无课的节数（长度）
	 * @param color
	 */
	void setNoClass(LinearLayout ll, int classes, int color) {
		TextView blank = new TextView(this);
		if (color == 0)
			blank.setMinHeight(dip2px(this, classes * 50));
		blank.setBackgroundColor(colors[color]);
		ll.addView(blank);
	}

	// 点击课程的监听器
	class OnClickClassListener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			String className;
			String classRom;
			className = (String) ((TextView) v.findViewById(R.id.title)).getText();
			classRom = (String) ((TextView) v.findViewById(R.id.place)).getText();

			
			LayoutInflater layoutInflater = getLayoutInflater();
			final View mDialogClass = layoutInflater.inflate(
					R.layout.dialog_class_edit,
					(ViewGroup) findViewById(R.id.dialog_class));
			
			final EditText mClassNameEt = (EditText) mDialogClass.findViewById(R.id.dialog_name_edit);
			final EditText mClassRomEt = (EditText) mDialogClass.findViewById(R.id.dialog_rom_edit);
			final EditText mClassDayEt = (EditText) mDialogClass.findViewById(R.id.dialog_day_edit);
			final EditText mClassStartEt = (EditText) mDialogClass.findViewById(R.id.dialog_start_edit);
			final EditText mClassEndEt = (EditText) mDialogClass.findViewById(R.id.dialog_end_edit);
			
			
			mClassNameEt.setText(className);
			mClassRomEt.setText(classRom);
			
			new AlertDialog.Builder(MainActivity.this)
				.setTitle("编辑课程")
				.setView(mDialogClass)
				.setPositiveButton("更新", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Toast.makeText(MainActivity.this, "更新中...", Toast.LENGTH_SHORT).show();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_SHORT).show();
					}
				}).create().show();
			
			
			
			
		}
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/** * 根据手机的分辨率从 px(像素) 的单位 转成为 dp */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
