package com.stackstech.honeybee.server.quality.entity;

public enum JobType {

    BATCH("batch"), //
    STREAMING("streaming"), //
    VIRTUAL("virtual");

    private String name;

    private JobType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
