package com.stackstech.honeybee.server.bees.entity;


import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class SegmentRange extends AbstractAuditableEntity {

    private static final long serialVersionUID = -8929713841303669564L;

    @Column(name = "data_begin")
    private String begin = "-1h";

    private String length = "1h";

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public SegmentRange(String begin, String length) {
        this.begin = begin;
        this.length = length;
    }

    SegmentRange() {
    }

}
