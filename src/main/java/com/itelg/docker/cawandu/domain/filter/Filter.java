package com.itelg.docker.cawandu.domain.filter;

public interface Filter
{
	public String getOrderBy();
	public Boolean isAscending();
	public void setOrderBy(String orderBy, boolean ascending);

	public Integer getPage();
	public Integer getPageSize();
	public void setPageSize(int page, int pageSize);
	public Integer getOffset();
}