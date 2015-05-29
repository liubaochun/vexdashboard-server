package com.thistech.vexdashboard.repository;

import com.thistech.vexdashboard.common.model.VexdashboardPersistent;
import com.thistech.vexdashboard.common.model.PublishStatus;
import com.thistech.common.exception.ApplicationException;
import com.thistech.common.exception.DuplicateEntityException;
import com.thistech.common.mongo.model.BaseMongoPersistent;
import com.thistech.common.mongo.repository.AbstractMongoRepository;
import com.thistech.common.util.Page;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.ws.rs.core.Context;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public abstract class AbstractVexdashboardRepository<T extends VexdashboardPersistent> extends AbstractMongoRepository<T> {

    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private Validator validator;
    @Context
    private ApplicationContext context;

    private static final Logger log = LoggerFactory.getLogger(AbstractVexdashboardRepository.class);

    public AbstractVexdashboardRepository(Class<T> clazz) {
        super(clazz);
    }

    public Class<T> getEntityClass() {
        return getClazz();
    }

    @Override
    public void delete(String id) {
        // make sure we dont call this anywhere
        throw new ApplicationException(ApplicationException.Type.GENERAL, "Not Supported");
    }

    @Override
    public void delete(List<T> deletes) {
        // make sure we dont call this anywhere
        throw new ApplicationException(ApplicationException.Type.GENERAL, "Not Supported");
    }
    @Override
    public void delete(T entity) {
        if (entity != null) {
            entity.setPublishStatus(PublishStatus.DELETED);
            if(isAuditingOn()) {
                saveAuditInfo(entity, false);
            }
            getMongoTemplate().save(entity);
        }
    }

    @Override
    protected void saveAuditInfo(T entity, boolean isDeleting) {
        if(StringUtils.isBlank(entity.getLastUpdateUser()) || "UNKNOWN".equals(entity.getLastUpdateUser()))
        {
            entity.setLastUpdateUser("SYSTEM");
        }
        super.saveAuditInfo(entity, isDeleting);
    }
    @Override
    protected void saveAuditInfo(T entity, boolean isDeleting, String creatingEntityId) {
        if(StringUtils.isBlank(entity.getLastUpdateUser()) || "UNKNOWN".equals(entity.getLastUpdateUser()))
        {
            entity.setLastUpdateUser("SYSTEM");
        }
        super.saveAuditInfo(entity, isDeleting,creatingEntityId);
    }
    @Override
    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    @Override
    public Validator getValidator() {
        return validator;
    }

    protected Criteria createCriteria() {
        return where("publishStatus").ne(PublishStatus.DELETED);
    }

    public T findByName(final String value) {
        final Criteria criteria = createCriteria();
        criteria.andOperator(where("name").is(value));
        return getMongoTemplate().findOne(query(criteria), getEntityClass());
    }

    /**
     * Updates or creates an persistent entity.
     *
     * @param entity A non-null entity.
     * @param name The entity's optional name.
     * @return The given entity.
     * @throws ConstraintViolationException If unable to save the entity's state.
     * @throws DuplicateEntityException If an entity with the same name as the given entity already exists.
     */
    public T update(final T entity, final String name) throws ConstraintViolationException, DuplicateEntityException {
        if (StringUtils.isEmpty(entity.getId())) {
            final T existing = findOne(name);
            if ((existing != null) && (entity instanceof BaseMongoPersistent)) {
                ((BaseMongoPersistent) entity).setId(existing.getId());
            }
        }
        final Criteria criteria = createCriteria().and("_id").ne(entity.getId()).andOperator(where("name").is(name));
        if (getMongoTemplate().exists(query(criteria), entity.getClass())) {
            throw new DuplicateEntityException("name", "There is already an entity of type " + entity.getClass().getName() + " with the given name.");
        }
        return super.save(entity);
    }

    protected Page<T> findPage(Criteria criteria, String sortProperty, Boolean isDesc, int pageIndex, int pageSize) {
        Page<T> page = super.findPage(criteria, sortProperty, isDesc, pageIndex, pageSize);
        if (pageIndex > 0 && pageIndex <= page.getTotalPages()) {
            page = super.findPage(criteria, sortProperty, isDesc, page.getTotalPages() - 1, pageSize);
        }
        return page;
    }

    @Override
    public boolean isAuditingOn(){
        return true;
    }
}

