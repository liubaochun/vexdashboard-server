package com.thistech.vexdashboard.common.model;

import com.thistech.common.mongo.model.MongoPersistent;

/**
 * Created by brent on 5/28/15.
 */
public interface VexdashboardPersistent <T extends MongoPersistent> extends MongoPersistent<T> {
    String getOrganizationId();
    T setOrganizationId(String value);

    PublishStatus getPublishStatus();
    T setPublishStatus(PublishStatus value);
}
