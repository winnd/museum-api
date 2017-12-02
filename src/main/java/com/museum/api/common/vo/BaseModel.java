package com.museum.api.common.vo;

import com.github.pagehelper.PageInfo;
import com.museum.api.common.constant.Constants;

/**
 * <p>
 * model基类。
 * </p>
 * 
 * @version 1.0
 * @author 甘 <br />
 *         履历 <br />
 *         2016/01/27 : 甘: 新建<br />
 * @param <T>
 */
public class BaseModel<T> {
	private String status = Constants.SUCCESS;

	private String message = "操作成功";

	private T data;

	private PageModel page;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public PageModel getPage() {
		return page;
	}

	public void setPage(PageInfo<?> pageInfo) {
		this.page = new PageModel(pageInfo);
	}

}
