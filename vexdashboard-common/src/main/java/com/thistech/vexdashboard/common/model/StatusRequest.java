package com.thistech.vexdashboard.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import com.thistech.vexdashboard.common.util.VexDashboardUtil;
import org.codehaus.enunciate.json.JsonName;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StatusRequest", namespace = VexDashboardUtil.VEXDASHBOARD_NAMESPACE)
@XmlRootElement(name = "StatusRequest", namespace = VexDashboardUtil.VEXDASHBOARD_NAMESPACE)
public class StatusRequest implements Serializable {
    @XmlElement(name = "ipaddresses", namespace = VexDashboardUtil.VEXDASHBOARD_NAMESPACE)
    @JsonProperty("ipaddresses")
    @JsonName("ipaddresses")
    private List<String> ipaddresses;

    @NotNull
    @XmlAttribute
    private Date startDate;

    @NotNull
    @XmlAttribute
    private Date endDate;

    public List<String> getIpaddresses() {
        return ipaddresses;
    }

    public StatusRequest setIpaddresses(List<String> ipaddresses) {
        this.ipaddresses = ipaddresses;
        return this;
    }

    public Date getStartDate() { return startDate; }

    public StatusRequest setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public Date getEndDate() { return endDate; }
    public StatusRequest setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

}
