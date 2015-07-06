package com.thistech.vexdashboard.job;


import com.thistech.vexdashboard.common.model.Metric;
import com.thistech.vexdashboard.common.model.MetricType;
import com.thistech.vexdashboard.common.model.VexboxStatus;
import com.thistech.vexdashboard.repository.VexboxStatusRepository;
import com.thistech.vexdashboard.service.BoxStatusService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class BoxMetricsMockJob extends VexdashboardSigletonJob{
    @Resource
    BoxStatusService boxStatusService;

    @Resource
    VexboxStatusRepository vexboxStatusRepository;

    @Value("${mock.corevex.ipaddresses}")
    String coreIPAddresses;
    @Value("${mock.director.ipaddresses}")
    String directorIPAddresses;
    @Value("${mock.fe.ipaddresses}")
    String feIPAddresses;

    @Override
    protected Boolean doWork() {
        Date now = new Date();

        String[] ipcoreArray = StringUtils.split(coreIPAddresses, ';');
        String[] ipDirectorArray = StringUtils.split(directorIPAddresses, ';');
        String[] ipFeArray = StringUtils.split(feIPAddresses, ';');

        int CORE = 0;
        int DIRECTOR = 1;
        int FE = 2;
        String[] ipArray = null;
        for (int i = 0; i < 3; i++) {

            if (i == CORE) {
                ipArray = ipcoreArray;
            } else if (i == DIRECTOR) {
                ipArray = ipDirectorArray;
            } else if (i == FE) {
                ipArray = ipFeArray;
            }

            for (String ip : ipArray) {
                VexboxStatus status = new VexboxStatus();
                status.setIpaddress(ip);
                status.setTimestamp(now);
                status.setOrganizationId("1ea15585-1075-4833-86c6-9321695b5ce4");

                int cpuUsage = 0, memoryUsage = 0;
                if ( i == CORE) {
                    status.setLevel("GREEN");
                    status.setNotificationMsg("It is OK.");
                    cpuUsage = 0 + (int) (Math.random() * (45 - 0)); //0-45
                    memoryUsage = 0 + (int) (Math.random() * (50 - 0)); //0-50
                } else if (i == DIRECTOR ) {
                    status.setLevel("YELLOW");
                    status.setNotificationMsg("WANING!");
                    cpuUsage = 45 + (int)(Math.random() * (70 - 45)); //45-70
                    memoryUsage = 80 + (int)(Math.random() * (80 - 50)); //50-80
                } else if (i == FE ) {
                    status.setLevel("RED");
                    status.setNotificationMsg("ERROR!ERROR~");
                    cpuUsage = 70 + (int)(Math.random() * (100 - 70) + 1); //70-100
                    memoryUsage = 80 + (int)(Math.random() * (100 - 80) + 1); //80-100
                }
                List<Metric> metricList = new ArrayList<Metric>();
                metricList.add(new Metric().setType(MetricType.CPU).setValue(cpuUsage));
                metricList.add(new Metric().setType(MetricType.MEMORY).setValue(memoryUsage));
                status.setMetrics(metricList);
                vexboxStatusRepository.save(status);
            }
        }
        return true;
    }
}
