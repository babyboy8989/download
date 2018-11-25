package com.dreamwin.download.db.dao;

import java.util.Date;
import java.util.List;

import com.dreamwin.cclib.db.sober.Sober;
import com.dreamwin.cclib.db.sober.SoberException;
import com.dreamwin.download.db.DB;
import com.dreamwin.download.db.SoberFactory;
import com.dreamwin.download.orm.Task;

public class TaskDao {

	private static Sober sober = SoberFactory.getSober(DB.DOWNLOAD);

	/**
	 * 重置任务状态
	 * 
	 * @param original
	 * @param target
	 * @throws SoberException
	 */
	public static void resetStatus(int original, int target) throws SoberException {
		String sql = "UPDATE task SET status=? WHERE status=?";

		sober.write(sql, target, original);
	}

	/**
	 * 存储任务
	 * 
	 * @param task
	 * @throws SoberException
	 */
	public static void save(Task task) throws SoberException {
		sober.insert(task);
	}

	/**
	 * 查询待处理任务
	 * 
	 * @return
	 * @throws SoberException
	 */
	public static List<Task> getReady() throws SoberException {
		String sql = "SELECT * FROM task WHERE status=? ORDER BY retry_count, id";

		return sober.read(sql, Task.STATUS_READY).toObjects(Task.class);
	}

	/**
	 * 设置下载开始
	 * 
	 * @param id
	 * @return
	 * @throws SoberException
	 */
	public static boolean setDownloadStart(int id) throws SoberException {
		String sql = "UPDATE task SET status=? WHERE id=? AND status=?";

		return sober.write(sql, Task.STATUS_WORKING, id, Task.STATUS_READY) == 1;
	}

	/**
	 * 设置下载完成
	 * 
	 * @param id
	 * @return
	 * @throws SoberException
	 */
	public static boolean setDownloadFinish(int id) throws SoberException {
		String sql = "UPDATE task SET status=?, finish_time=? WHERE id=? AND status=?";

		return sober.write(sql, Task.STATUS_DONE, new Date(), id, Task.STATUS_WORKING) == 1;
	}

	/**
	 * 设置下载重试
	 * 
	 * @param id
	 * @return
	 * @throws SoberException
	 */
	public static boolean setDownloadRetry(int id) throws SoberException {
		String sql = "UPDATE task SET status=?, retry_count=retry_count+1 WHERE id=? AND status=?";

		return sober.write(sql, Task.STATUS_READY, id, Task.STATUS_WORKING) == 1;
	}

}
