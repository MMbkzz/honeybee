package com.stackstech.dcp.core.util;

/**
 * Title: 分页类
 *
 * @version 1.0
 */
public class PageInfo {

    // 分页查询开始记录位置
    private int begin;
    // 分页查看下结束位置
    private int end;
    // 每页显示记录数
    private int length;
    // 查询结果总记录数
    private int count;
    // 当前页码
    private int current;
    // 总共页数
    private int total;

    public PageInfo() {
        this(0, 10);
    }

    public PageInfo(int begin, int length) {
        this.begin = begin;
        this.length = length;
        this.end = this.begin + this.length;
        this.current = (int) Math.floor((this.begin * 1.0d) / this.length) + 1;
    }

    public PageInfo(int begin, int length, int count) {
        this(begin, length);
        this.count = count;
    }

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setBegin(int begin) {
        this.begin = begin;
        if (this.length != 0) {
            this.current = (int) Math.floor((this.begin * 1.0d) / this.length) + 1;
        }
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
        if (this.begin != 0) {
            this.current = (int) Math.floor((this.begin * 1.0d) / this.length) + 1;
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        this.total = (int) Math.floor((this.count * 1.0d) / this.length);
        if (this.count % this.length != 0) {
            this.total++;
        }
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (this.length != 0) {
            this.begin = (current - 1) * length;
            this.end = current * length;
        }
        this.current = current;
    }

    public int getTotal() {
        if (total == 0) {
            return 1;
        }
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
