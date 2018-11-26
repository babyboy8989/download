package com.dreamwin.download.form;

import com.dreamwin.cclib.arch.Form;
import com.dreamwin.download.util.StringUtil;

@Form
public class DownloadForm {

	private String src;
	private String dst;
	private String md5;

	public boolean isValid() {
		if (StringUtil.isBlank(src) || StringUtil.isBlank(dst) || StringUtil.isBlank(md5)) {
			return false;
		}
		
		// 实现参数校验
		String tmp = src.toLowerCase();
		// 如：限制url为http请求
		if (!tmp.startsWith("http://")) {
			return false;
		}
		// 如：限制src长度
		if (tmp.length() > 500) {
			return false;
		}
		// 如：md5长度校验
		if (md5.length() != 32) {
			return false;
		}

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
