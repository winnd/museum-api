package com.museum.api.common.vo;



import com.github.pagehelper.PageInfo;

public class PageModel {

	// 当前页
	private int pageNum;
	// 每页的数量
	private int pageSize;

	// 总页数
	private int pages;

	private long totalNum;

	public PageModel(PageInfo<?> pageInfo) {
		setPageNum(pageInfo.getPageNum());
		setPages(pageInfo.getPages());
		setPageSize(pageInfo.getPageSize());
		setTotalNum(pageInfo.getTotal());
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(long totalNum) {
		this.totalNum = totalNum;
	}

}
