package com.dreamwin.download.services.thread;

import java.io.File;

import com.dreamwin.cclib.crypto.Hashlib;
import com.dreamwin.cclib.httplib.wget.WgetWrapper;
import com.dreamwin.download.orm.Task;

public class DownloadThread extends Thread {

	private Task task;

	private boolean success = false;

	public DownloadThread(Task task) {
		this.task = task;
	}

	public Task getTask() {
		return task;
	}

	public boolean isSuccess() {
		return success;
	}

	@Override
	public void run() {
		String src = task.getSrc();
		String dst = task.getDst();

		// 下载
		WgetWrapper wget = new WgetWrapper(src, dst);
		wget.download();

		if (!wget.isSuccessDownload()) {
			// 下载命令执行失败
			return;
		}

		File dstFile = new File(dst);
		if (!dstFile.isFile()) {
			// 目标文件不存在
			return;
		}

		String md5 = task.getMd5();
		String dstMd5 = Hashlib.md5(dstFile);
		if (dstMd5 == null || !dstMd5.equalsIgnoreCase(md5)) {
			// 无法计算md5，或md5不正确。
			return;
		}

		// 下载成功。
		success = true;
	}

}
