package com.dreamwin.download.conf;

public class Config {
	
	// 执行间隔，单位秒。
	public static int scan_interval = 30;
	// 下载并发
	public static int download_concurrent_number = 5;
	
	public static String wget_path = "/usr/local/bin/wget";
	public static String bash_path = "/bin/bash";
	
}
