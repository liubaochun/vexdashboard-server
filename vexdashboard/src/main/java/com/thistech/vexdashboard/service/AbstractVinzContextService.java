package com.thistech.vexdashboard.service;

import com.thistech.vinz.common.cxf.VinzContext;
import com.thistech.vinz.common.model.AccessCondition;
import com.thistech.vinz.common.model.Identity;
import com.thistech.vinz.common.model.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * AbstractVinzContextService
 * @author <a href="mailto:jeffb@thistech.com">Jeff Bailey</a>
 */
public class AbstractVinzContextService {

    private static final Logger log = LoggerFactory.getLogger(AbstractVinzContextService.class);
    private VinzContext vinzContext;

    @Context
    public void setVinzContext(VinzContext value) {
        this.vinzContext = value;
    }

    protected VinzContext getVinzContext() { return vinzContext; }

    /**
     * Retrieve the current Identity
     * @return The currently authenticated Identity
     */
    protected Identity currentIdentity() {
        return getVinzContext().currentIdentity();

    }

    /**
     * Check whether the current Identity has one of the Access Conditions listed
     * @param accessConditions The Access Conditions
     * @throws WebApplicationException if the current Identity does not have any of the Access Conditions listed.
     */
    protected void checkAccessCondition(AccessCondition ... accessConditions) {
        if (!getVinzContext().hasAccessCondition(accessConditions)) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

    /**
     * Check that an Identity is authorized for the Organization or DMR ID provided
     * @param id The Organization or DMR ID.
     * @throws WebApplicationException if the current Identity is not authorized for the Organization or DMR ID provided.
     */
    protected void checkOrganization(String id) {
        getOrganization(id);
    }

    /**
     * Get an Organization for ID or DMR ID.
     * @param id The Organization or DMR ID.
     * @return The Organization
     * @throws WebApplicationException if the id does not match an Organization associated to the the authenticated Identity.
     */
    protected Organization getOrganization(String id) {
        Organization organization = null;
        boolean hasGlobalOrgAccess = getVinzContext().currentIdentity().hasAccessCondition(AccessCondition.VINZ_GLOBAL_ORGANIZATION_READ, AccessCondition.ACS_OPERATIONAL_SETTINGS_MANAGE);
        if (hasGlobalOrgAccess) {
            organization = new Organization(id, null, null);

        } else {
            organization = getVinzContext().getOrganization(id);
            if (organization == null) {
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            }
        }
        return organization;
    }

    /**
     * Normalize an Organization or DMR ID to the immutable Organization ID.
     * @param id The Organization or DMR ID.
     * @return The Organization ID.
     * @throws WebApplicationException if the current Identity is not authorized for the Organization or DMR ID provided.
     */
    protected String normalizeOrganizationId(String id) {
        if (id == null) { return null; }
        return getOrganization(id).getId();
    }

}
