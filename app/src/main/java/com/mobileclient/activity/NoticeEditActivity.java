package com.mobileclient.activity;

import com.mobileclient.domain.Notice;
import com.mobileclient.service.NoticeService;
import com.mobileclient.util.Utils;
import com.sun.jna.platform.win32.WinNT;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoticeEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明公告idTextView
	private TextView TV_noticeId;
	// 声明标题输入框
	private EditText ET_title;
	// 声明公告内容输入框
	private EditText ET_content;
	// 声明发布时间输入框
	private EditText ET_publishDate;
	protected String carmera_path;
	/*要保存的新闻公告信息*/
	Notice notice = new Notice();
	/*新闻公告管理业务逻辑层*/
	private NoticeService noticeService = new NoticeService();

	private int noticeId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		//getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.notice_edit);
		Utils.setStatusBar(this, false, false);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑新闻公告信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		//TV_noticeId = (TextView) findViewById(R.id.TV_noticeId);
		ET_title = (EditText) findViewById(R.id.ET_title);
		ET_content = (EditText) findViewById(R.id.ET_content);
		//ET_publishDate = (EditText) findViewById(R.id.ET_publishDate);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		noticeId = extras.getInt("noticeId");
		ET_title.setText(extras.getString("noticeTitle"));
		ET_content.setText(extras.getString("noticeContent"));
		ET_publishDate.setText(extras.getString("publishDate"));
		/*单击修改新闻公告按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					/*验证获取标题*/ 
					if(ET_title.getText().toString().equals("")) {
						Toast.makeText(NoticeEditActivity.this, "标题输入不能为空!", Toast.LENGTH_LONG).show();
						ET_title.setFocusable(true);
						ET_title.requestFocus();
						return;	
					}
					notice.setNoticeTitle(ET_title.getText().toString());
					/*验证获取公告内容*/ 
					if(ET_content.getText().toString().equals("")) {
						Toast.makeText(NoticeEditActivity.this, "公告内容输入不能为空!", Toast.LENGTH_LONG).show();
						ET_content.setFocusable(true);
						ET_content.requestFocus();
						return;	
					}
					notice.setNoticeContent(ET_content.getText().toString());
					/*验证获取发布时间*/ 
//					if(ET_publishDate.getText().toString().equals("")) {
//						Toast.makeText(NoticeEditActivity.this, "发布时间输入不能为空!", Toast.LENGTH_LONG).show();
//						ET_publishDate.setFocusable(true);
//						ET_publishDate.requestFocus();
//						return;
//					}
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
					//添加发布时间

					notice.setPublishDate(df.format(new Date()));
					/*调用业务逻辑层上传新闻公告信息*/
					NoticeEditActivity.this.setTitle("正在更新公告信息，稍等...");
					final Handler handler=new Handler()
					{
						@Override
						public void handleMessage(Message msg) {
							super.handleMessage(msg);
							Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
							Intent intent = getIntent();
							setResult(RESULT_OK,intent);
							finish();
						}
					};
					new Thread(new Runnable() {
						@Override
						public void run() {
							noticeService.UpdateNotice(notice);
							Message msg=new Message();
							handler.sendMessage(msg);
						}
					}).start();

				} catch (Exception e) {}
			}
		});
		initViewData();
	}

	/* 初始化显示编辑界面的数据 */
	private void initViewData() {
	    notice = noticeService.GetNotice(noticeId);
		this.TV_noticeId.setText(noticeId+"");
		this.ET_title.setText(notice.getNoticeTitle());
		this.ET_content.setText(notice.getNoticeContent());
		this.ET_publishDate.setText(notice.getPublishDate());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
