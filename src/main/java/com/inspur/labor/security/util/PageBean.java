package com.inspur.labor.security.util;

import com.github.pagehelper.Page;

import java.io.Serializable;
import java.util.List;

/**
 * 自定义分页封装
 *
 * @Title: PageBean
 * @Author: gengpeng
 * @CreateDate: 2020/9/14 15:14
 * @Version: 1.0
 */
public class PageBean<T> implements Serializable {

    private static final long serialVersionUID = -9202109574544652243L;
    /**
     * 总记录数
     */
    private long total;
    /**
     * 结果集
     */
    private List<T> list;
    /**
     * 第几页
     */
    private int pageNum;
    /**
     * 每页记录数
     */
    private int pageSize;
    /**
     * 总页数
     */
    private int pages;
    /**
     * 当前页的数量 <= pageSize，该属性来自ArrayList的size属性
     */
    private int size;

    /**
     * 包装Page对象，因为直接返回Page对象，在JSON处理以及其他情况下会被当成List来处理，
     * 而出现一些问题。
     *
     * @param list page结果
     * @param
     */
    public PageBean(List<T> list) {
        if (list instanceof Page) {
            Page<T> page = (Page<T>) list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.list = page;
            this.size = page.size();
        }
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
