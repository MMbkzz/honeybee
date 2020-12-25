package com.stackstech.honeybee.core.log;

public class ServerLogger<T> {

    private String thread;

    private String rowkey;

    private long timenano;

    private String tag;

    private T message;

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public long getTimenano() {
        return timenano;
    }

    public void setTimenano(long timenano) {
        this.timenano = timenano;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }
}
