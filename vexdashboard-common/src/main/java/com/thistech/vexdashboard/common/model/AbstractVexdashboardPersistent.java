package com.thistech.vexdashboard.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thistech.common.mongo.model.BaseMongoPersistent;
import com.thistech.vinz.common.model.Organization;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.HashMap;
import java.util.Map;

@XmlType(name = "AcsPersistentType")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("unchecked")

public abstract class AbstractVexdashboardPersistent<T extends AbstractVexdashboardPersistent> extends BaseMongoPersistent<T> implements VexdashboardPersistent<T> {

    /** The organization id */
    @Indexed
    @NotNull
    @XmlAttribute
    @JsonProperty
    private String organizationId;
    @Indexed
    @NotNull
    @XmlAttribute
    private PublishStatus publishStatus = PublishStatus.PUBLISHED;

    public AbstractVexdashboardPersistent() {}

    public AbstractVexdashboardPersistent(String organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String getOrganizationId() { return organizationId; }
    public T setOrganizationId(String value) { this.organizationId = value; return (T) this; }

    public PublishStatus getPublishStatus() { return publishStatus; }
    public T setPublishStatus(PublishStatus value) { this.publishStatus = value; return (T) this; }

    /**
     *
     * @return the related entity map. The key is the id of the entity and value is the class of that entity.
     * Sub-classes are expected to also call super method to include the entities in the base class
     */
    public Map<String,Class> createRelatedEntityMap(){
        Map<String,Class> map = new HashMap<>();
        if(getOrganizationId() != null){
            map.put(getOrganizationId(), Organization.class);
        }
        return map;
    }
}