package com.thistech.vexdashboard.common.model;

import com.thistech.common.cxf.ThisObjectMapper;
import org.testng.annotations.Test;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class VexBoxTest {

    @Test
    public void testJson() throws Exception {
        StringWriter sw = new StringWriter();
        new ThisObjectMapper().writeValue(sw, createVexBox());
        String json = sw.toString();
        System.out.println(json);
        assertNotNull(json);
        VexBox vexBox = new ThisObjectMapper().readValue(json, VexBox.class);
        validate(vexBox);

    }

    private VexBox createVexBox() {
        List<Threshold> thresholds = new ArrayList<>();
        thresholds.add(new Threshold().setType(MetricType.CPU).setValue(40));
        thresholds.add(new Threshold().setType(MetricType.MEMORY).setValue(80));
        VexBox vexBox = new VexBox();
        vexBox.setApplicationType(ApplicationType.COREVEX).setCpu("Intel Core8").setDiskSize("256G").setIpAddress("192.168.204.1")
                .setMemory("16G").setJavaVersion("1.7.0").setApplicatonVersion("2.3.1").setThreholds(thresholds);
        return vexBox;
    }

    private void validate(VexBox vexBox) {
        assertEquals(ApplicationType.COREVEX, vexBox.getApplicationType() );
        assertEquals("Intel Core8", vexBox.getCpu());
        assertEquals("256G", vexBox.getDiskSize());
        assertEquals("192.168.204.1", vexBox.getIpAddress());

    }
}
