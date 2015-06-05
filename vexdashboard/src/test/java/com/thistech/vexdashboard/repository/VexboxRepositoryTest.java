package com.thistech.vexdashboard.repository;

import com.thistech.vexdashboard.common.model.ApplicationType;
import com.thistech.vexdashboard.common.model.MetricType;
import com.thistech.vexdashboard.common.model.Threshold;
import com.thistech.vexdashboard.common.model.VexBox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;
/**
 * Created by brent on 6/1/15.
 */
public class VexboxRepositoryTest extends AbstractRepositoryTest {

    @Resource
    private VexboxRepository vexboxRepository;

    @BeforeMethod
    public void setup() {

    }

    @Test
    public void saveAndFind() {
        List<Threshold> thresholds = new ArrayList<Threshold>();
        thresholds.add(new Threshold().setType(MetricType.CPU).setValue(60));
        thresholds.add(new Threshold().setType(MetricType.MEMORY).setValue(45));
        VexBox vexBox = new VexBox().setApplicationType(ApplicationType.COREVEX).setApplicatonVersion("0.0.1").setCpu("i7")
                .setDiskSize("10G").setIpAddress("192.168.0.1")
                .setJavaVersion("JDK7").setMemory("8G")
                .setThreholds(thresholds).setOrganizationId("THISTECH").setId("11111");

        vexboxRepository.save(vexBox);
        assertNotNull(vexboxRepository.findOne("11111"));
    }
}
