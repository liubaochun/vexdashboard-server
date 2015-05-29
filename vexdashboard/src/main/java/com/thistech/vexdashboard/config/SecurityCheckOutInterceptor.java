package com.thistech.vexdashboard.config;

import java.util.*;
import com.google.common.net.HttpHeaders;
import org.apache.cxf.interceptor.*;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.slf4j.*;

/**
 * remove the AUTHORIZATION header in outbound messages to external components for security.
 */
public class SecurityCheckOutInterceptor extends AbstractOutDatabindingInterceptor {
    private static final Logger log = LoggerFactory.getLogger(SecurityCheckOutInterceptor.class);
    private String vinzEndpoint;

    public SecurityCheckOutInterceptor() {
        super(Phase.SEND);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleMessage(Message outMessage) throws Fault {
        Map<String, List<String>> headers = (Map<String, List<String>>)outMessage.get(Message.PROTOCOL_HEADERS);
        if (headers == null) {
            headers = new TreeMap<String, List<String>>(String.CASE_INSENSITIVE_ORDER);
            outMessage.put(Message.PROTOCOL_HEADERS, headers);
        }
        String endpoint_address = (String)outMessage.get(Message.ENDPOINT_ADDRESS);

        boolean sendToVinz = endpoint_address != null && vinzEndpoint != null && endpoint_address.startsWith(vinzEndpoint);
        if (!sendToVinz) { //don't remove the header of outbound messages to vinz
            headers.remove(HttpHeaders.AUTHORIZATION); //remove token for security consideration
        }

    }

    public String getVinzEndpoint() {
        return vinzEndpoint;
    }

    public SecurityCheckOutInterceptor setVinzEndpoint(String vinzEndpoint) {
        this.vinzEndpoint = vinzEndpoint;
        return this;
    }
}
