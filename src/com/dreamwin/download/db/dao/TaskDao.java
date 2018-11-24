package com.dreamwin.download.db.dao;

import com.dreamwin.cclib.db.sober.Sober;
import com.dreamwin.cclib.db.sober.SoberException;
import com.dreamwin.download.db.DB;
import com.dreamwin.download.db.SoberFactory;

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

}
