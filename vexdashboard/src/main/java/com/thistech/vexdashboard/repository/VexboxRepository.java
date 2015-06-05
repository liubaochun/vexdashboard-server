package com.thistech.vexdashboard.repository;

import com.thistech.common.util.Page;
import com.thistech.vexdashboard.common.model.VexBox;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.Set;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by brent on 5/28/15.
 */
@Repository
public class VexboxRepository extends AbstractVexdashboardRepository<VexBox>{

    public VexboxRepository() {
        super(VexBox.class);
    }

    //@Cacheable(value = VexBox.CACHE_NAME, key = "{'VexBox',#root.methodName,#searchString,#sortProperty,#isDesc,#pageIndex,#pageSize}")
    public Page<VexBox> find(String searchString, final String sortProperty, final boolean isDesc, final int pageIndex, final int pageSize) {
        final Criteria criteria = createCriteria();
        Criteria[] searchCriteria = null;
        if (StringUtils.isNotBlank(searchString)) {
            String regex = toRegex(searchString);
            searchCriteria = new Criteria[] {where("ipAddress").regex(regex, "i"), where("applicationType").regex(regex, "i")};
        }

        if (searchCriteria != null) {
            criteria.orOperator(searchCriteria);
        }

        return findPage(criteria, StringUtils.defaultIfEmpty(sortProperty, "applicationType"), isDesc, pageIndex, pageSize);
    }

}
