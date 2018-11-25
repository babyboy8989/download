package com.dreamwin.download.form;

import com.dreamwin.cclib.arch.Form;

@Form
public class DownloadForm {

	private String src;
	private String dst;
	private String md5;

	public boolean isValid() {
		// 实现参数校验，如url格式、目标地址权限、md5格式、参数长度等。
		return true;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getDst() {
		return dst;
	}

	public void setDst(String dst) {
		this.dst = dst;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DownloadForm [src=");
		builder.append(src);
		builder.append(", dst=");
		builder.append(dst);
		builder.append(", md5=");
		builder.append(md5);
		builder.append("]");
		return builder.toString();
	}

}
