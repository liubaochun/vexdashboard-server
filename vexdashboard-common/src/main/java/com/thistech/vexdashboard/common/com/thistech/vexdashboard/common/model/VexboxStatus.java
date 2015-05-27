package com.thistech.vexdashboard.common.com.thistech.vexdashboard.common.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import com.thistech.common.mongo.model.BaseMongoPersistent;
import com.thistech.vexdashboard.common.util.VexDashboardUtil;
import org.codehaus.enunciate.json.JsonName;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VexboxStatus", namespace = VexDashboardUtil.VEXDASHBOARD_NAMESPACE)
@XmlRootElement(name = "VexboxStatus", namespace = VexDashboardUtil.VEXDASHBOARD_NAMESPACE)
@Document(collection = "VexboxStatus")
public class VexboxStatus extends BaseMongoPersistent<VexBox> implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String CACHE_NAME = "VexboxStatus";
    @Indexed
    @NotNull
    @XmlAttribute
    @JsonProperty
    private String ipaddress;

    @Indexed
    @NotNull
    @XmlAttribute
    private Date timestamp;

    @XmlElement(name = "metrics", namespace = VexDashboardUtil.VEXDASHBOARD_NAMESPACE)
    @JsonProperty("metrics")
    @JsonName("metrics")
    private List<Metric> metrics;

    public String getIpaddress() {
        return ipaddress;
    }

    public VexboxStatus setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
        return this;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public VexboxStatus setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public VexboxStatus setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
        return this;
    }
}
