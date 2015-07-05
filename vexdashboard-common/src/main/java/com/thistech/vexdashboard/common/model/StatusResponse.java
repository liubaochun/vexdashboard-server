package com.thistech.vexdashboard.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thistech.vexdashboard.common.util.VexDashboardUtil;
import org.codehaus.enunciate.json.JsonName;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StatusResponse", namespace = VexDashboardUtil.VEXDASHBOARD_NAMESPACE)
@XmlRootElement(name = "StatusResponse", namespace = VexDashboardUtil.VEXDASHBOARD_NAMESPACE)
public class StatusResponse {

    @XmlElement(name = "status", namespace = VexDashboardUtil.VEXDASHBOARD_NAMESPACE)
    @JsonProperty("status")
    @JsonName("status")
    private List<VexboxStatus> status;

    public List<VexboxStatus> getStatus() { return this.status; }
    public StatusResponse setStatus(List<VexboxStatus> status) {
        this.status = status;
        return this;
    }
}
