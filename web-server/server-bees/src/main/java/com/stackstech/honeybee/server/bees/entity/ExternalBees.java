package com.stackstech.honeybee.server.bees.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

/**
 * Measures to publish metrics that processed externally
 */
@Entity
public class ExternalBees extends Bees {
    private static final long serialVersionUID = -7551493544224747244L;

    private String metricName;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private VirtualJob virtualJob;

    public ExternalBees() {
        super();
    }

    public ExternalBees(String name, String description, String organization,
                        String owner, String metricName, VirtualJob vj) {
        super(name, description, organization, owner);
        this.metricName = metricName;
        this.virtualJob = vj;
    }

    @JsonProperty("metric.name")
    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public VirtualJob getVirtualJob() {
        return virtualJob;
    }

    public void setVirtualJob(VirtualJob virtualJob) {
        this.virtualJob = virtualJob;
    }

    @Override
    public String getType() {
        return "external";
    }
}
