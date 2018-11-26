package com.dreamwin.download.services.thread;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dreamwin.cclib.crypto.Hashlib;
import com.dreamwin.download.conf.Config;
import com.dreamwin.download.orm.Task;
import com.dreamwin.download.util.StringUtil;

public class DownloadThread extends Thread {
	
	private static final Logger LOGGER = LogManager.getLogger(DownloadThread.class);
	
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
		int id = task.getId();
		String src = task.getSrc();
		String dst = task.getDst();
		
		// 使用WgetWrapper下载，需要设置操作系统环境变量，否则可能不能识别wget命令。
		// WgetWrapper wget = new WgetWrapper(src, dst);
		// wget.download();
		//
		// if (!wget.isSuccessDownload()) {
		// LOGGER.error(String.format("Wget download fail, taskid: %d, code: %d.", id, wget.getReturnCode()));
		// // 下载命令执行失败
		// return;
		// }
		// 自己实现wget下载
		if (!wget(src, dst)) {
			LOGGER.error(String.format("Wget download fail, taskid: %d.", id));
			return;
		}
		
		File dstFile = new File(dst);
		if (!dstFile.isFile()) {
			LOGGER.error(String.format("Dst file invalid, taskid: %d.", id));
			// 目标文件不存在
			return;
		}
		
		String md5 = task.getMd5();
		String dstMd5 = Hashlib.md5(dstFile);
		if (dstMd5 == null || !dstMd5.equalsIgnoreCase(md5)) {
			// 无法计算md5，或md5不正确。
			LOGGER.error(String.format("Md5 wrong, taskid: %d.", id));
			return;
		}
		
		// 下载成功。
		success = true;
	}
	
	private boolean wget(String src, String dst) {
		if (StringUtil.isBlank(src) || StringUtil.isBlank(dst)) {
			return false;
		}
		
		File dstFile = new File(dst);
		if (dstFile.exists() && !dstFile.delete()) {
			LOGGER.error("Dst file exists but can not deleted.");
			return false;
		}
		
		File parent = dstFile.getParentFile();
		if (!parent.exists() && !parent.mkdirs()) {
			LOGGER.error("Dst parent file not exists but can not created.");
			return false;
		}
		
		String command = String.format("%s -c '%s' -O '%s'", Config.wget_path, src, dst);
		String[] realCommand = new String[] {
		    Config.bash_path, "-c", command
		};
		ProcessBuilder pb = new ProcessBuilder(realCommand);
		pb.directory(parent);
		pb.redirectErrorStream(true);
		
		Process process = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		try {
			process = pb.start();
			
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			
			StringBuilder sb = new StringBuilder();
			String line = null;
			while (true) {
				line = br.readLine();
				if (line == null) {
					break;
				}
				
				if (line.trim().isEmpty()) {
					continue;
				}
				
				sb.append(line.trim()).append("\r\n");
			}
			int returnCode = process.waitFor();
			
			LOGGER.info(sb.substring(0, sb.length() - "\r\n".length()));
			return returnCode == 0;
		} catch (IOException e) {
			LOGGER.error("", e);
		} catch (InterruptedException e) {
			LOGGER.error("", e);
		} finally {
			closeStream(br);
			closeStream(isr);
			closeStream(is);
			process.destroy();
		}
		
		return false;
	}
	
	private void closeStream(Closeable handle) {
		if (handle == null) {
			return;
		}
		
		try {
			handle.close();
		} catch (IOException e) {
			handle = null;
		}
	}
	
}
