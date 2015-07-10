package com.thistech.vexdashboard.repository;

import com.thistech.vexdashboard.common.model.VexboxStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class VexboxStatusRepository extends AbstractVexdashboardRepository<VexboxStatus>{

    public VexboxStatusRepository() {
        super(VexboxStatus.class);
    }

    public List<VexboxStatus> findStatusByIpList(final List<String> ipList, Date startDate, Date endDate) {

        if (ipList.isEmpty() || startDate == null) {
            return new ArrayList<>();
        }

        if (endDate == null) {
            endDate = new Date();
        }
        final Criteria criteria = createCriteria();

        if (ipList.size() == 1) {
            criteria.andOperator(Criteria.where("ipaddress").is(ipList.get(0)), Criteria.where("timestamp").gte(startDate), Criteria.where("timestamp").lte(endDate));
        } else {
            criteria.andOperator(Criteria.where("timestamp").gte(startDate), Criteria.where("timestamp").lte(endDate), Criteria.where("ipaddress").in(ipList));
        }

        return getMongoTemplate().find(Query.query(criteria), getEntityClass());
    }

    public List<VexboxStatus> findStatusByIpaddress(final String ipaddress, Date startDate, Date endDate) {
        final Criteria criteria = createCriteria();
        criteria.andOperator(Criteria.where("ipaddress").is(ipaddress), Criteria.where("timestamp").gte(startDate), Criteria.where("timestamp").lte(endDate));
        Query query= Query.query(criteria).with(new Sort(Sort.Direction.ASC, "timestamp"));
        return getMongoTemplate().find(query, getEntityClass());
    }
}
