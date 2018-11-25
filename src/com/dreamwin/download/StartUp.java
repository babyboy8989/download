package com.dreamwin.download;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dreamwin.cclib.arch.exception.ArchException;
import com.dreamwin.cclib.arch.parse.Arch;
import com.dreamwin.cclib.db.sober.SoberException;
import com.dreamwin.cclib.util.ConfigUtil;
import com.dreamwin.download.conf.Config;
import com.dreamwin.download.db.DB;
import com.dreamwin.download.db.SoberFactory;
import com.dreamwin.download.db.dao.TaskDao;
import com.dreamwin.download.orm.Task;
import com.dreamwin.download.services.manager.DownloadManager;

/**
 * The system start interface.
 */
public class StartUp extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LogManager.getLogger(StartUp.class);

	public void init(ServletConfig config) throws ServletException {
		// 1.加载配置文件。
		ConfigUtil.setShowLog(false);
		if (!ConfigUtil.readFromFile(Config.class, "config.properties")) {
			LOGGER.error("Read config from config.properties fail, Exit!");
			System.exit(1);
		}
		ConfigUtil.setShowLog(true);
		ConfigUtil.print(Config.class);

		// 2.初始化Sober
		if (!SoberFactory.initialize("com.dreamwin.download.orm", "druid.properties", false, DB.values())) {
			LOGGER.error("Initialize sober fail, Exit!");
			System.exit(1);
		}

		// 3.初始Arch
		try {
			Arch.scanForm("com.dreamwin.download.form");
		} catch (ArchException e) {
			LOGGER.error("Arch scan form fail, Exit!");
			System.exit(1);
		}

		// 4.重置任务务，容错
		try {
			TaskDao.resetStatus(Task.STATUS_WORKING, Task.STATUS_READY);
		} catch (SoberException e) {
			LOGGER.error("Reset task status fail, Exit!", e);
			System.exit(1);
		}

		// 5.启动下载处理队列
		DownloadManager manager = new DownloadManager();
		manager.start();

		// 6.可以启动其他功能，如配置动态刷新。
	}

}
