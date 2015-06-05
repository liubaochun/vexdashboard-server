package com.thistech.vexdashboard.ui.util;

import com.thistech.vexdashboard.ui.model.Config;
import com.thistech.common.cxf.CxfUtil;
import com.thistech.common.jaxb.XmlUtil;
import org.apache.cxf.jaxb.JAXBDataBinding;

import javax.xml.bind.JAXBContext;

public class VexdashboardUiUtil {
    private static final Class[] JAXB_CLASSES = new Class[] {
            Config.class
    };

    public static final JAXBContext VEXDASHBOARD_UI_JAXB_CONTEXT = XmlUtil.newJaxbContext(JAXB_CLASSES);
    public static final JAXBDataBinding VEXDASHBOARD_UI_JAXB_DATA_BINDING = CxfUtil.newPooledDataBinding(VEXDASHBOARD_UI_JAXB_CONTEXT);
}
