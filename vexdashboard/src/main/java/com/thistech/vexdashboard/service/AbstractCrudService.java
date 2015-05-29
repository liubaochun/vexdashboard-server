package com.thistech.vexdashboard.service;

import com.thistech.vexdashboard.common.model.VexdashboardPersistent;
import com.thistech.vexdashboard.repository.AbstractVexdashboardRepository;
import com.thistech.vinz.common.model.Identity;
import com.thistech.common.util.Page;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * AbstractCrudService
 * @author <a href="mailto:jeffb@thistech.com">Jeff Bailey</a>
 */
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public abstract class AbstractCrudService<T extends VexdashboardPersistent> extends AbstractVinzContextService {
    private static final Logger log = LoggerFactory.getLogger(AbstractCrudService.class);
    public Response delete(String id) {
        Identity current = currentIdentity();
        log.info("{} is attempting to delete {} with id {}.", current, getEntityClassName(), id);
        T entity = findOne(id);
        if (entity != null) {
            checkOrganization(entity.getOrganizationId());
            entity.setLastUpdateUser(current.getName());
            getRepository().delete(entity);
            log.info("{} deleted {} successfully.", current, entity);
            return Response.ok().build();
        }
        throw new WebApplicationException(Response.Status.NO_CONTENT);
    }

    /**
     * Find an entity, ensuring the user is authorized to view the returned object.
     * @param id The entity's id
     * @return The entity
     * @throws WebApplicationException with {@see Response.Status.UNAUTHORIZED} if the user was not authorized to view the entity.
     */
    public T findOne(String id) {
        Identity current = currentIdentity();
        log.info("{} is attempting to retrieve {} with id {}.", current, getEntityClassName(), id);
        T entity = getRepository().findOne(id);
        if (entity != null) {
            checkOrganization(entity.getOrganizationId());
            log.info("{} successfully retrieved {}.", current, entity);
            return entity;
        }
        return null;
    }

    public abstract Page<T> find(String organizationId, String query, String sortProperty, boolean isDesc, int pageIndex, int pageSize);

    /**
     * Check that the current Identity is authorized to update the entity.
     * @param entity the entity to check
     * @throws WebApplicationException with a status of Response.Status.FORBIDDEN if not authorized
     */
    protected void checkAuthorization(T entity) throws WebApplicationException {
        entity.setOrganizationId(normalizeOrganizationId(entity.getOrganizationId()));
    }

    /**
     * Save the entity, ensuring the user is authorized to create or update the object.
     * @param entity The entity to save
     * @return The saved entity
     * @throws WebApplicationException with {@see Response.Status.FORBIDDEN} if the user was not authorized to create or update {@param entity}.
     */
    public T save(T entity) {
        Identity current = currentIdentity();
        entity.setLastUpdateUser(entity.getId());
        log.info("{} is aAttempting to save {}.", current, entity);
        checkAuthorization(entity);
        if (StringUtils.isNotBlank(entity.getId())) {
            T existing = getRepository().findOne(entity.getId());
            if (existing != null && !StringUtils.equals(entity.getOrganizationId(), existing.getOrganizationId())) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }
        }
        entity.setLastUpdateUser(current.getName());
        getRepository().save(entity);
        log.info("{} saved {} successfully.", current, entity);
        return entity;
    }

    protected String getEntityClassName() {
        return getRepository().getEntityClass().getSimpleName();
    }
    protected abstract AbstractVexdashboardRepository<T> getRepository();
    protected T update(final T entity, final String name) {
        final Identity currentIdentity = currentIdentity();
        log.info("{} is attempting to save {}.", currentIdentity, entity);
        final String organizationId = entity.getOrganizationId();
        if (StringUtils.isBlank(entity.getId())) {
            entity.setOrganizationId(normalizeOrganizationId(organizationId));
        } else if (organizationId != null && !currentIdentity.authorizedFor(organizationId)) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        entity.setLastUpdateUser(currentIdentity.getName());
        getRepository().update(entity, name);
        log.info("{} successfully updated.", currentIdentity, entity);
        return entity;
    }
}
