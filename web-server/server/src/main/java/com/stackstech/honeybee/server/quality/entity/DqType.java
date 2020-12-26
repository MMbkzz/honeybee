package com.stackstech.honeybee.server.quality.entity;

public enum DqType {
    /**
     * Currently we support six dimensions of measure.
     */
    ACCURACY,
    PROFILING,
    TIMELINESS,
    UNIQUENESS,
    COMPLETENESS,
    CONSISTENCY
}
