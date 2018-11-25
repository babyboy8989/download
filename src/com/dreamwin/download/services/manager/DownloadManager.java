package com.dreamwin.download.services.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dreamwin.cclib.db.sober.SoberException;
import com.dreamwin.cclib.util.TimeUtil;
import com.dreamwin.download.conf.Config;
import com.dreamwin.download.db.dao.TaskDao;
import com.dreamwin.download.orm.Task;
import com.dreamwin.download.services.thread.DownloadThread;

public class DownloadManager extends Thread {

	private static final Logger LOGGER = LogManager.getLogger(DownloadManager.class);

	private static List<DownloadThread> pool = new ArrayList<DownloadThread>();

	@Override
	public void run() {

		while (true) {
			// 回收资源，清理pool。
			recycle();
			// 分配资源，填充pool。
			allocate();
			// 执行间隔
			TimeUtil.sleep(Config.scan_interval);
		}

	}

	/**
	 * 读取Task，分配线程处理。
	 */
	private void allocate() {
		if (pool.size() >= Config.download_concurrent_number) {
			return;
		}

		List<Task> list;
		try {
			list = TaskDao.getReady();
		} catch (SoberException e) {
			LOGGER.error("Get ready task list fail.", e);
			return;
		}
		if (list.isEmpty()) {
			return;
		}

		for (Task task : list) {
			if (pool.size() >= Config.download_concurrent_number) {
				break;
			}

			int id = task.getId();

			// 设置任务下载开始
			try {
				if (!TaskDao.setDownloadStart(id)) {
					LOGGER.warn(String.format("Set download start fail, taskid: %d.", id));
					continue;
				}
			} catch (SoberException e) {
				LOGGER.error(String.format("Database error when set download start, taskid: %d.", id), e);
				continue;
			}

			// 创建线程
			DownloadThread thread = new DownloadThread(task);
			pool.add(thread);
			thread.start();

			LOGGER.info(String.format("Add a new thread, start downloading, taskid: %d.", id));
		}
	}

	/**
	 * 回收已完成线程资源。
	 */
	private void recycle() {
		List<DownloadThread> finished = new ArrayList<DownloadThread>();
		for (DownloadThread thread : pool) {
			// 仍活跃
			if (thread.isAlive()) {
				continue;
			}

			int id = thread.getTask().getId();
			try {
				if (thread.isSuccess()) {
					if (!TaskDao.setDownloadFinish(id)) {
						continue;
					}

					LOGGER.info(String.format("Finish a thread, download success, taskid: %d.", id));
				} else {
					if (!TaskDao.setDownloadRetry(id)) {
						continue;
					}

					LOGGER.warn(String.format("Finish a thread, download fail, retry later, taskid: %d.", id));
				}
			} catch (SoberException e) {
				LOGGER.error(String.format("Database error when set download finish, taskid: %d.", id), e);
				continue;
			}

			finished.add(thread);
		}

		pool.removeAll(finished);
	}

}
