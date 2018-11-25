package com.dreamwin.download.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dreamwin.cclib.arch.exception.ArchException;
import com.dreamwin.cclib.arch.parse.Arch;
import com.dreamwin.cclib.db.sober.SoberException;
import com.dreamwin.cclib.naga.annotation.Mapping;
import com.dreamwin.cclib.naga.view.Naga;
import com.dreamwin.cclib.naga.view.View;
import com.dreamwin.download.db.dao.TaskDao;
import com.dreamwin.download.form.DownloadForm;
import com.dreamwin.download.orm.Task;

public class TaskController {

	private static final Logger LOGGER = LogManager.getLogger(TaskController.class);

	@Mapping(uri = "/servlet/download")
	public View addTask(HttpServletRequest request) {

		// 解析参数，构建form。
		DownloadForm form;
		try {
			form = Arch.parse(request, DownloadForm.class);
		} catch (ArchException e) {
			LOGGER.error("Arch parse download form fail.", e);
			return Naga.createStringView("Invalid request.");
		}

		// 校验
		if (!form.isValid()) {
			LOGGER.error("Validate download form fail, form: " + form);
			return Naga.createStringView("Invalid params.");
		}

		// 生成task
		Task task = new Task();
		task.setSrc(form.getSrc());
		task.setDst(form.getDst());
		task.setMd5(form.getMd5());
		task.setCreateTime(new Date());
		task.setStatus(Task.STATUS_READY);

		// 存储task
		try {
			TaskDao.save(task);
		} catch (SoberException e) {
			LOGGER.error("Save task fail, form: " + form, e);
			return Naga.createStringView("Save task fail.");
		}

		// 返回成功
		return Naga.createStringView("Save task success.");
	}

}
