package com.dreamwin.download.util;

public class StringUtil {
	
	public static boolean isBlank(String content) {
		if (content == null) {
			return true;
		}
		
		return content.trim().isEmpty();
	}
	
}
