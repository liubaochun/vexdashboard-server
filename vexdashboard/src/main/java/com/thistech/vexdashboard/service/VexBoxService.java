package com.thistech.vexdashboard.service;

import com.thistech.common.util.Page;
import com.thistech.vexdashboard.common.model.ApplicationType;
import com.thistech.vexdashboard.common.model.VexBox;
import com.thistech.vexdashboard.repository.VexboxRepository;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@Service
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Path("/vexboxs")
public class VexBoxService extends AbstractCrudService<VexBox>{
    private static final Logger log = LoggerFactory.getLogger(VexBoxService.class);

    @Resource
    private VexboxRepository vexboxRepository;

    @Override
    protected VexboxRepository getRepository() {
        return vexboxRepository;
    }

    /**
     * This operation retrieves a page of VexBox.
     * @param organizationId The organization's ID or DMR ID.
     * @param currentApplicationType The ApplicationType.
     * @param ipAddress The IP address regex.
     * @param sortProperty The sort property (e.g., 'name').
     * @param isDesc If true and sort property is not blank, will sort in descending order.
     * @param pageIndex The page index.
     * @param pageSize The page size.
     * @return This operation returns a page of Networks.
     */
    @GET
    //@Authorize(AccessCondition.ACS_NETWORK_READ)
    @StatusCodes({ @ResponseCode(code = 200, condition = "OK."), @ResponseCode(code = 403, condition = "Identity is not authorized to view the Networks.") })
    public Page<VexBox> find(@QueryParam("o") final String organizationId,
                             @QueryParam("currentApplicationType") final String currentApplicationType,
                             @QueryParam("ip") final String ipAddress,
                             @QueryParam("sortBy") @DefaultValue("applicationType") final String sortProperty,
                             @QueryParam("d") @DefaultValue("false") final boolean isDesc,
                             @QueryParam("pageIndex") @DefaultValue("0") final int pageIndex,
                             @QueryParam("pageSize") @DefaultValue("20") final int pageSize) {
        Page<VexBox> page = null;
        page = vexboxRepository.findByApptypeAndIp(currentApplicationType, ipAddress, sortProperty, isDesc, pageIndex, pageSize);
        return page;
    }

    public Page<VexBox> find(String organizationId, String query, String sortProperty, boolean isDesc, int pageIndex, int pageSize) {
        return null;
    }


    /**
     * This operation retrieves a VexBox by Id and Organization Id
     * @param id The Id of VexBox
     * @return This Operation returns a VexBox.
     */
    @GET
    @Path("{id}")
    //@Authorize(AccessCondition.METAMORE_NETWORK_FEED_READ)
    @StatusCodes({ @ResponseCode(code = 200, condition = "OK."), @ResponseCode(code = 404, condition = "The NetworkFeed does not exist or is not visible to the organization provided.")})
    public VexBox findOne(@PathParam("id") String id) {
        //Organization organization = getOrganization(organizationId);
        VexBox vexBox = vexboxRepository.findOne(id);
        //if (vexBox != null && StringUtils.equals(organization.getId(), vexBox.getOrganizationId())) {
        if (vexBox != null) {
            return vexBox;
        } else {
            return null;
        }
    }


    @GET
    @Path("/application/{applicationType}")
    //@Authorize(AccessCondition.METAMORE_NETWORK_FEED_READ)
    @StatusCodes({@ResponseCode(code = 200, condition = "OK."), @ResponseCode(code = 401, condition = "Identity is not authorized to view this AcquisitionSystemProfile."), @ResponseCode(code = 404, condition = "No AcquisitionSystemProfile was found for the given id.")})
    public Page<VexBox> find(@PathParam("applicationType") ApplicationType applicationType) {
        return null;
    }

    /**
     * This operation creates a VexBox.
     * @param vexBox This is the VexBox to save.
     * @return This operation returns the created VexBox.
     */
    @Override
    @POST
    //@Authorize(AccessCondition.ACS_NETWORK_MANAGE)
    @StatusCodes({ @ResponseCode(code = 200, condition = "OK."), @ResponseCode(code = 400, condition = "Bad Request"), @ResponseCode(code = 403, condition = "Identity is not authorized to save a Network"), @ResponseCode(code = 409, condition = "A unique constraint was violated.")})
    public VexBox save(final VexBox vexBox) {
        VexBox v =  super.save(vexBox);
        return v;
    }


}
