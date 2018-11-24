package com.dreamwin.download.services.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dreamwin.download.orm.Task;
import com.dreamwin.download.services.thread.DownloadThread;

public class DownloadManager extends Thread {

	private static final Logger LOGGER = LogManager.getLogger(DownloadManager.class);

	private static Queue<Task> queue = new ConcurrentLinkedQueue<Task>();
	private static List<DownloadThread> pool = new ArrayList<DownloadThread>();

	@Override
	public void run() {
		
	}

}
