package com.thistech.vexdashboard.common.com.thistech.vexdashboard.common.model;

import com.sun.istack.NotNull;
import com.thistech.vexdashboard.common.util.VexDashboardUtil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "Metric", namespace = VexDashboardUtil.VEXDASHBOARD_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class Metric implements Serializable{
    private static final long serialVersionUID = 1L;
    public static final String CACHE_NAME = "Metric";

    @NotNull
    @XmlAttribute(required = true)
    private MetricType type;

    @NotNull
    @XmlAttribute(required = true)
    private float value;

    public MetricType getType() {
        return type;
    }

    public Metric setType(MetricType type) {
        this.type = type;
        return this;
    }

    public float getValue() {
        return value;
    }

    public Metric setValue(float value) {
        this.value = value;
        return this;
    }
}
