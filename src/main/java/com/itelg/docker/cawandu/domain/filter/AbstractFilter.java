package com.itelg.docker.cawandu.domain.filter;

public abstract class AbstractFilter implements Filter
{
    private String orderBy;
    private Boolean ascending;
    private Integer page;
    private Integer pageSize;

    @Override
    public String getOrderBy()
    {
        return orderBy;
    }

    @Override
    public Boolean isAscending()
    {
        return ascending;
    }

    @Override
    public void setOrderBy(String orderBy, boolean ascending)
    {
        this.orderBy = orderBy;
        this.ascending = Boolean.valueOf(ascending);
    }

    @Override
    public Integer getPage()
    {
        return page;
    }

    private void setPage(Integer page)
    {
        this.page = page;
    }

    @Override
    public Integer getPageSize()
    {
        return pageSize;
    }

    private void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }

    @Override
    public void setPageSize(int page, int pageSize)
    {
        setPage(Integer.valueOf(page));
        setPageSize(Integer.valueOf(pageSize));
    }

    @Override
    public Integer getOffset()
    {
        return Integer.valueOf(getPageSize().intValue() * (getPage().intValue() - 1));
    }
}