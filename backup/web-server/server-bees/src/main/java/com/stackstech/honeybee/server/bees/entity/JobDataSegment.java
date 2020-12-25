package com.stackstech.honeybee.server.bees.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author william
 */
@Entity
public class JobDataSegment extends AbstractAuditableEntity {

    private static final long serialVersionUID = -9056531122243340484L;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(JobDataSegment.class);

    @NotNull
    private String dataConnectorName;

    private boolean asTsBaseline = false;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,
            CascadeType.REMOVE, CascadeType.MERGE})
    @JoinColumn(name = "segment_range_id")
    private SegmentRange segmentRange = new SegmentRange();

    @JsonProperty("as.baseline")
    public boolean isAsTsBaseline() {
        return asTsBaseline;
    }

    public void setAsTsBaseline(boolean asTsBaseline) {
        this.asTsBaseline = asTsBaseline;
    }

    @JsonProperty("segment.range")
    public SegmentRange getSegmentRange() {
        return segmentRange;
    }

    public void setSegmentRange(SegmentRange segmentRange) {
        this.segmentRange = segmentRange;
    }

    @JsonProperty("data.connector.name")
    public String getDataConnectorName() {
        return dataConnectorName;
    }

    public void setDataConnectorName(String dataConnectorName) {
        if (StringUtils.isEmpty(dataConnectorName)) {
            LOGGER.warn(" Data connector name is invalid. " +
                    "Please check your connector name.");
            throw new NullPointerException();
        }
        this.dataConnectorName = dataConnectorName;
    }

    public JobDataSegment() {
    }

    public JobDataSegment(String dataConnectorName, boolean baseline) {
        this.dataConnectorName = dataConnectorName;
        this.asTsBaseline = baseline;
    }

    public JobDataSegment(String dataConnectorName, boolean baseline,
                          SegmentRange segmentRange) {
        this.dataConnectorName = dataConnectorName;
        this.asTsBaseline = baseline;
        this.segmentRange = segmentRange;
    }
}
