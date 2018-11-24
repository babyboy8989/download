package com.dreamwin.download.db;

public enum DB {

	DOWNLOAD("download");

	private String alias;

	private DB(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return alias;
	}

}
