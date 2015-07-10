package com.thistech.vexdashboard.common.model;

import com.thistech.vexdashboard.common.util.VexDashboardUtil;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

@Document(collection = "BoxProfile")
@XmlType(name="BoxProfile", namespace = VexDashboardUtil.VEXDASHBOARD_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class BoxProfile {
    @NotNull
    @XmlElement(name = "vexBox", namespace = VexDashboardUtil.VEXDASHBOARD_NAMESPACE)
    VexBox vexBox;

    @NotNull
    @XmlElement(name = "timestamps", namespace = VexDashboardUtil.VEXDASHBOARD_NAMESPACE)
    Date[] timestamps;

    @NotNull
    @XmlElement(name = "cpuValues", namespace = VexDashboardUtil.VEXDASHBOARD_NAMESPACE)
    float[] cpuValues;

    @NotNull
    @XmlElement(name = "memValues", namespace = VexDashboardUtil.VEXDASHBOARD_NAMESPACE)
    float[] memValues;

    public BoxProfile() {

    }

    public VexBox getVexBox() {
        return this.vexBox;
    }
    public BoxProfile setVexBox(VexBox vexBox) {
        this.vexBox = vexBox;
        return this;
    }

    public Date[] getTimestamps() {
        return this.timestamps;
    }
    public BoxProfile setTimestamps(Date[] timestamps) {
        this.timestamps = timestamps;
        return this;
    }

    public float[] getCpuValues() {
        return this.cpuValues;
    }
    public BoxProfile setCpuValues(float[] cpuValues) {
        this.cpuValues = cpuValues;
        return this;
    }

    public float[] getMemValues() {
        return this.memValues;
    }
    public BoxProfile setMemValues(float[] memValues) {
        this.memValues = memValues;
        return this;
    }

}
