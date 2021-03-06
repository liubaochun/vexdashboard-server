package com.thistech.vexdashboard.service;

import com.thistech.common.util.Page;
import com.thistech.vexdashboard.common.model.*;
import com.thistech.vexdashboard.repository.VexboxRepository;
import com.thistech.vexdashboard.repository.VexboxStatusRepository;
import com.thistech.vinz.common.model.*;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;


@Service
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Path("/boxstatus")
public class BoxStatusService extends AbstractCrudService<VexboxStatus>{
    private static final Logger log = LoggerFactory.getLogger(BoxStatusService.class);

    @Resource
    private VexboxStatusRepository vexboxStatusRepository;

    @Override
    protected VexboxStatusRepository getRepository() {
        return vexboxStatusRepository;
    }

    @Resource
    private VexBoxService vexBoxService;

    @Override
    public Page<VexboxStatus> find(String organizationId, String query, String sortProperty, boolean isDesc, int pageIndex, int pageSize) {
        return null;
    }

    /**
     * This operation creates a VexboxStatus.
     * @param vexboxStatus This is the VexboxStatus to save.
     * @return This operation returns the created VexboxStatus.
     */
    @Override
    @POST
    //@Authorize(AccessCondition.ACS_NETWORK_MANAGE)
    @StatusCodes({ @ResponseCode(code = 200, condition = "OK."), @ResponseCode(code = 400, condition = "Bad Request"), @ResponseCode(code = 403, condition = "Identity is not authorized to save a Network"), @ResponseCode(code = 409, condition = "A unique constraint was violated.")})
    public VexboxStatus save(final VexboxStatus vexboxStatus) {
        VexboxStatus v =  super.save(vexboxStatus);
        return v;
    }

    @GET
    @StatusCodes({ @ResponseCode(code = 200, condition = "OK."), @ResponseCode(code = 400, condition = "Bad Request"), @ResponseCode(code = 403, condition = "Identity is not authorized to save a Network"), @ResponseCode(code = 409, condition = "A unique constraint was violated.")})
    public StatusResponse getStatus(@QueryParam("ipaddresses")String ipaddresses, @QueryParam("startTime")Long startTime, @QueryParam("endTime") Long endTime) {
        Date startDate = new Date(startTime);
        Date endDate = new Date(endTime);

        String[] ipArray = StringUtils.split(ipaddresses, ';');
        List<String> ipList = Arrays.asList(ipArray);

        List<VexboxStatus> statusList = vexboxStatusRepository.findStatusByIpList(ipList, startDate, endDate);
        StatusResponse response = new StatusResponse().setStatus(statusList);
        return response;
    }

    @GET
    @Path("/ip/{boxId}")
    @StatusCodes({ @ResponseCode(code = 200, condition = "OK."), @ResponseCode(code = 400, condition = "Bad Request"), @ResponseCode(code = 403, condition = "Identity is not authorized to save a Network"), @ResponseCode(code = 409, condition = "A unique constraint was violated.")})
    public BoxProfile getBoxProfile(@PathParam("boxId")String boxId) {
        VexBox vexBox = vexBoxService.findOne(boxId);
        if (vexBox == null) {
            return null;
        }
        BoxProfile profile = new BoxProfile();
        profile.setVexBox(vexBox);

        Date endDate = new Date();
        long milliseconds = endDate.getTime();
        Date startDate = new Date(milliseconds - 60 * 60 * 1000);
        List<VexboxStatus> statusList = vexboxStatusRepository.findStatusByIpaddress(vexBox.getIpAddress(), startDate, endDate);
        int statusCount = statusList.size();

        if (statusCount > 0) {
            Date[] timestamps = new Date[statusCount];
            float[] cpuValues = new float[statusCount];
            float[] memValues = new float[statusCount];

            int count = 0;
            for (VexboxStatus status : statusList) {
                timestamps[count] = status.getTimestamp();
                List<Metric> metrics = status.getMetrics();
                for (Metric metric: metrics) {
                    if (metric.getType().equals(MetricType.CPU)) {
                        cpuValues[count] = metric.getValue();
                    } else if (metric.getType().equals(MetricType.MEMORY)) {
                        memValues[count] = metric.getValue();
                    }
                }
                count++;
            }

            profile.setCpuValues(cpuValues).setMemValues(memValues).setTimestamps(timestamps);
        }
        return profile;
    }

}
