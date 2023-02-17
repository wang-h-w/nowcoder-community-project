package com.nowcoder.community.utils;

public class Page {
    // 当前页码
    private int current = 1;
    // 每页显示上限
    private int limit = 10;
    // 页码显示区域宽度
    private int width = 2;
    // 总记录数
    private int rows;
    // 查询路径
    private String path;

    /**
     * 获取当前页的起始行
     * @return Offset of current page
     */
    public int getOffset() {
        return (current - 1) * limit;
    }

    /**
     * 获取总页数
     * @return Total number of pages
     */
    public int getTotal() {
        if (rows % limit == 0) return rows / limit;
        else return rows / limit + 1;
    }

    /**
     * 获得页码显示区域的起始页码
     * @return Start page
     */
    public int getFrom() {
        int from = Math.max(1, current - width);
        int fromIfMeetEnd = getTotal() - 2 * width;
        if (fromIfMeetEnd >= 1 && fromIfMeetEnd < from) return fromIfMeetEnd;
        return from;
    }

    /**
     * 获得页码显示区域的终止页码
     * @return End page
     */
    public int getTo() {
        int to = Math.min(getTotal(), current + width);
        int toIfMeetStart = 1 + 2 * width;
        if (toIfMeetStart <= getTotal() && toIfMeetStart > to) return toIfMeetStart;
        return to;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1)  this.current = current;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (1 <= limit && limit <= 100) this.limit = limit;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) this.rows = rows;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Page{" +
                "current=" + current +
                ", limit=" + limit +
                ", width=" + width +
                ", rows=" + rows +
                ", path='" + path + '\'' +
                '}';
    }
}
