package com.huangyuanlove.liaoba.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.huangyuanlove.liaoba.R;

public class UpdateManager {
	private Context mContext;

	private String saveFileName;

	/* 进度条与通知ui刷新的handler和msg常量 */
	private ProgressBar mProgress;

	private String downLoadUrl;

	private String changeLog;
	private static final int DOWN_UPDATE = 1;

	private static final int DOWN_OVER = 2;

	private int progress;

	private Thread downLoadThread;

	private boolean interceptFlag = false;

	private boolean runBackground = false;

	private AlertDialog alertDialog;
	private AlertDialog progressDialog;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress + 1);
				break;
			case DOWN_OVER:
				if (runBackground) {
					Toast.makeText(mContext,"下载完成",Toast.LENGTH_SHORT).show();
				}
				progressDialog.dismiss();
				installApk();

				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context) {
		this.mContext = context;

	}

	// 外部接口让主Activity调用
	public void checkUpdateInfo(String version, String url, String log) {
		if (!"".equals(url)) {
			changeLog = log;
			showNoticeDialog();
			saveFileName = Environment
					.getExternalStorageDirectory() + "/LiaoBa/LiaoBa"+version + ".apk";
			downLoadUrl = url;

		}

	}

	private void showNoticeDialog() {

		alertDialog = new AlertDialog.Builder(mContext).create();
		alertDialog.show();
		alertDialog.setCanceledOnTouchOutside(false);
		Window dialogWindow = alertDialog.getWindow();
		dialogWindow.setContentView(R.layout.dialog_version_update);

		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = (int) (mContext.getResources().getDisplayMetrics().density * 288);
		dialogWindow.setAttributes(lp);

		TextView reNameTitle = (TextView) dialogWindow
				.findViewById(R.id.alert_dialog_title_tv);

		reNameTitle.setText("软件版本更新");

		TextView messageTv = (TextView) dialogWindow
				.findViewById(R.id.alert_dialog_message_tv);
		messageTv.setVisibility(View.VISIBLE);

		messageTv.setText(changeLog.replace(";", "\n"));

		Button confirmBt = (Button) dialogWindow
				.findViewById(R.id.alert_dialog_confirm_bt);
		Button cancelBt = (Button) dialogWindow
				.findViewById(R.id.alert_dialog_cancel_bt);

		confirmBt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.cancel();
				showDownloadDialog();

			}
		});

		cancelBt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				alertDialog.cancel();

			}
		});

	}

	private void showDownloadDialog() {
		progressDialog = new AlertDialog.Builder(mContext).create();
		progressDialog.show();
		progressDialog.setCanceledOnTouchOutside(false);
		Window dialogWindow = progressDialog.getWindow();
		dialogWindow.setContentView(R.layout.dialog_progress);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = (int) (mContext.getResources().getDisplayMetrics().density * 288);

		dialogWindow.setAttributes(lp);
		TextView title = (TextView) dialogWindow
				.findViewById(R.id.progress_title);
		title.setText("软件版本更新");
		mProgress = (ProgressBar) dialogWindow.findViewById(R.id.progress);

		Button background = (Button) dialogWindow
				.findViewById(R.id.progress_background);
		Button cancel = (Button) dialogWindow
				.findViewById(R.id.progress_cancel);

		background.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// alertDialog.cancel();
				progressDialog.dismiss();
				runBackground = true;
				// // interceptFlag = true;

			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				interceptFlag = true;
				progressDialog.cancel();

			}
		});

		downloadApk();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(downLoadUrl);

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(Environment
						.getExternalStorageDirectory() + "/LiaoBa/");
				if (!file.exists()) {
					file.mkdir();
				}
				String apkFileName = saveFileName;
				File apkFile = new File(apkFileName);
				FileOutputStream fos = new FileOutputStream(apkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				// 点击取消就停止下载.
				while (!interceptFlag) {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				}
				;

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * 下载apk
	 */
	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
	 * 
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}


	public String getVersion() {

		String version = "1.0";
		PackageManager packageManager = mContext.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					mContext.getPackageName(), 0);
			version = packageInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		return version;
	}



}
