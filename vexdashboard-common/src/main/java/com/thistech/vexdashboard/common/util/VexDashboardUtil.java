package com.thistech.vexdashboard.common.util;

import com.thistech.common.cxf.CxfUtil;
import com.thistech.common.jaxb.XmlUtil;
import org.apache.cxf.jaxb.JAXBDataBinding;

import javax.xml.bind.JAXBContext;

public class VexDashboardUtil {

    private VexDashboardUtil() {}

    public static final String VEXDASHBOARD_NAMESPACE = "http://www.thistech.com/schemas/vexdashboard/1";
    private static final String[] JAXB_PACKAGES = new String[] {
            "com.thistech.common.esni",
            "com.thistech.common.esam.i03",
            "com.thistech.vexdashboard.common.model",
            "com.thistech.common.util"
    };

    public static final JAXBContext VEXDASHBOARD_JAXB_CONTEXT = XmlUtil.newJaxbContext(JAXB_PACKAGES);
    public static final JAXBDataBinding VEXDASHBOARD_JAXB_DATA_BINDING = CxfUtil.newPooledDataBinding(VEXDASHBOARD_JAXB_CONTEXT);
}
