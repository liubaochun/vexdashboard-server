package com.thistech.vexdashboard.common.com.thistech.vexdashboard.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thistech.vexdashboard.common.util.VexDashboardUtil;
import com.thistech.common.mongo.model.BaseMongoPersistent;
import org.codehaus.enunciate.json.JsonName;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.XmlElement;

import java.io.Serializable;
import java.util.List;

@Document(collection = "VexBox")
@XmlType(name="VexBox", namespace = VexDashboardUtil.VEXDASHBOARD_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class VexBox extends BaseMongoPersistent<VexBox> implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String CACHE_NAME = "VexBox";

    @NotNull
    @XmlElement(name = "ipaddress", namespace = VexDashboardUtil.VEXDASHBOARD_NAMESPACE)
    private String ipAddress;

    @NotNull
    @XmlElement(name = "cpu", namespace = VexDashboardUtil .VEXDASHBOARD_NAMESPACE )
    private String cpu;

    @NotNull
    @XmlElement(name = "memory", namespace = VexDashboardUtil.VEXDASHBOARD_NAMESPACE)
    private String memory;

    @XmlElement
    private String diskSize;

    @NotNull
    @XmlElement
    private String javaVersion;

    @XmlElement
    private ApplicationType applicationType;

    @XmlElement
    private String applicatonVersion;

    @XmlElement(name = "thresholds", nillable = false, namespace = VexDashboardUtil.VEXDASHBOARD_NAMESPACE)
    @XmlElementWrapper(name = "thresholds", nillable = true, namespace = VexDashboardUtil.VEXDASHBOARD_NAMESPACE)
    @JsonName("thresholds")
    @JsonProperty("thresholds")
    private List<Threshold> thresholds;

    public VexBox() {

    }

    public String getIpAddress() {
        return ipAddress;
    }

    public VexBox setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public String getCpu() {
        return cpu;
    }

    public VexBox setCpu(String cpu) {
        this.cpu = cpu;
        return this;
    }

    public String getMemory() {
        return memory;
    }

    public VexBox setMemory(String memory) {
        this.memory = memory;
        return this;
    }

    public String getDiskSize() {
        return diskSize;
    }

    public VexBox setDiskSize(String diskSize) {
        this.diskSize = diskSize;
        return this;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public VexBox setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
        return this;
    }

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public VexBox setApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;
        return this;
    }

    public String getApplicatonVersion() {
        return applicatonVersion;
    }

    public VexBox setApplicatonVersion(String applicatonVersion) {
        this.applicatonVersion = applicatonVersion;
        return this;
    }

    public List<Threshold> getThresholds() {
        return thresholds;
    }

    public VexBox setThreholds(List<Threshold> threholds) {
        this.thresholds = threholds;
        return this;
    }
}
