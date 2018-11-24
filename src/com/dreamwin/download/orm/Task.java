package com.dreamwin.download.orm;

import java.util.Date;

import com.dreamwin.cclib.db.sober.annotation.Column;
import com.dreamwin.cclib.db.sober.annotation.Entity;
import com.dreamwin.cclib.db.sober.annotation.Table;

@Entity
@Table(name = "task", engine = "InnoDB", charset = "utf8")
public class Task {

	public static final int STATUS_READY = 0;
	public static final int STATUS_WORKING = 5;
	public static final int STATUS_DONE = 10;

	private Integer id;
	private String src;
	private String dst;
	private String md5;
	private int status;
	@Column(name = "create_time")
	private Date createTime;
	@Column(name = "finish_time")
	private Date finishTime;
	@Column(name = "retry_count")
	private int retryCount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

}
