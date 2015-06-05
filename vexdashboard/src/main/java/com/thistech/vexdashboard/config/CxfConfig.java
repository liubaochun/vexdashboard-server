package com.thistech.vexdashboard.config;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.thistech.common.cxf.AcceptRequestHandler;
import com.thistech.common.cxf.StringMessageBodyWriter;
import com.thistech.common.cxf.mapper.ApplicationExceptionMapper;
import com.thistech.common.cxf.mapper.ConstraintViolationExceptionMapper;
import com.thistech.common.cxf.mapper.DuplicateEntityExceptionMapper;
import com.thistech.vexdashboard.common.util.VexDashboardUtil;
import com.thistech.vexdashboard.service.TestService;
import com.thistech.vexdashboard.cxf.HttpRequestFilter;
import com.thistech.vexdashboard.service.VexBoxService;
import com.thistech.vinz.common.cxf.MockVinzFilter;
import com.thistech.vinz.common.cxf.VinzFilter;
import com.thistech.vinz.common.model.AccessCondition;
import com.thistech.vinz.common.model.Authorization;
import com.thistech.vinz.common.model.Identity;
import com.thistech.vinz.common.model.Organization;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.ext.RequestHandler;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * <p>CxfConfiguration class.</p>
 * @author <a href="mailto:matt@thistech.com">Matt Narrell</a>
 */
@Configuration
public class CxfConfig {

    @Resource
    private TestService testService;
    @Resource
    private VexBoxService vexBoxService;


    @Value("${testing:false}")
    private boolean testing;
    @Value("${vinz.endpoint}")
    private String vinzEndpoint;

    private static final Logger log = LoggerFactory.getLogger(CxfConfig.class);
    //if there're other urls that needs this, add it here.
    private static final Set<String> AUTH_COOKIE_TO_HEADER_URLS  = new HashSet<String>(Arrays.asList(new String[]{"/auditHistory", "/report"}));
    @Bean(destroyMethod = "shutdown")
    public Bus cxfBus() {
        Bus bus = CXFBusFactory.getDefaultBus();
        bus.getInInterceptors().add(new LoggingInInterceptor());
        bus.getInFaultInterceptors().add(new LoggingInInterceptor());
        bus.getOutInterceptors().add(new LoggingOutInterceptor());
        bus.getOutFaultInterceptors().add(new LoggingOutInterceptor());
        bus.getOutInterceptors().add(new SecurityCheckOutInterceptor().setVinzEndpoint(vinzEndpoint));
        return bus;
    }

    @Bean(destroyMethod = "destroy")
    public Server restEndpoint() {
        List<Object> serviceBeans = new ArrayList<>();
        if (testing) {
            serviceBeans.add(testService);
        }
        serviceBeans.add(vexBoxService);

        JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
        factoryBean.setAddress("");
        factoryBean.setServiceBeans(serviceBeans);
        factoryBean.setDataBinding(VexDashboardUtil.VEXDASHBOARD_JAXB_DATA_BINDING);

        Map<Object, Object> extMappings = new HashMap<>();
        extMappings.put("json", MediaType.APPLICATION_JSON);
        extMappings.put("xml", MediaType.APPLICATION_XML);
        factoryBean.setExtensionMappings(extMappings);

        factoryBean.setProviders(Arrays.asList(
                //createAuthToHeaderFilter(),
                //createVinzFilter(),
                new HttpRequestFilter(),
                new AcceptRequestHandler(),
                new ApplicationExceptionMapper(),
                new ConstraintViolationExceptionMapper(),
                new DuplicateEntityExceptionMapper(),
                //new JacksonJsonProvider(new ThisObjectMapper()),
                new JacksonJsonProvider(new VexdashboardObjectMapper()),
                new StringMessageBodyWriter()
        ));
        return factoryBean.create();
    }

    private VinzFilter createVinzFilter() {
        if (testing) {
            Identity testUser = new Identity("acstest@thistech.com", "acssecret", "ACS Test User")
                    .addOrganization(new Organization(Organization.THISTECH_ID, "This Tech", "THIS").setDmrId("99999"))
                    .addAuthorization(new Authorization("ACS Admin",
                            AccessCondition.ACS_DECISION_NETWORK,
                            AccessCondition.ACS_DECISION_SIGNAL,
                            AccessCondition.ACS_DEVICE_PROFILE_MANAGE,
                            AccessCondition.ACS_DEVICE_PROFILE_READ,
                            AccessCondition.ACS_EVENT_MANAGE,
                            AccessCondition.ACS_EVENT_READ,
                            AccessCondition.ACS_EVENT_TEMPLATE_READ,
                            AccessCondition.ACS_EVENT_TEMPLATE_MANAGE,
                            AccessCondition.ACS_NETWORK_READ,
                            AccessCondition.ACS_NETWORK_MANAGE,
                            AccessCondition.ACS_PROFILE_MANAGE,
                            AccessCondition.ACS_PROFILE_READ,
                            AccessCondition.ACS_REGION_READ,
                            AccessCondition.ACS_REGION_MANAGE,
                            AccessCondition.ACS_REPORT_NETWORK,
                            AccessCondition.ACS_REPORT_SUMMARY,
                            AccessCondition.ACS_SCHEDULE_MANAGE,
                            AccessCondition.ACS_SCHEDULE_READ,
                            AccessCondition.ACS_SOURCE_FEED_READ,
                            AccessCondition.ACS_SOURCE_FEED_MANAGE,
                            AccessCondition.ACS_STATUS_NETWORK,
                            AccessCondition.ACS_STATUS_SUMMARY,
                            AccessCondition.ACS_ZONE_READ,
                            AccessCondition.ACS_ZONE_MANAGE,
                            AccessCondition.ACS_GLOBAL_ZONE_MANAGE,
                            AccessCondition.EMP_ZONE_MANAGE,
                            AccessCondition.EMP_GLOBAL_ZONE_MANAGE,
                            AccessCondition.EMP_REGION_MANAGE,
                            AccessCondition.EMP_GLOBAL_REGION_MANAGE));
            return new MockVinzFilter(testUser);
        }
        else {
            return new VinzFilter(vinzEndpoint);
        }
    }
    private boolean authToHeader(String path) {
        if(AUTH_COOKIE_TO_HEADER_URLS.contains(path)) return true;
        for(String p: AUTH_COOKIE_TO_HEADER_URLS){
            if(path.startsWith(p)) return true;
        }
        return false;
    }
    private RequestHandler createAuthToHeaderFilter () {
        return new RequestHandler(){
            public Response handleRequest(Message message, ClassResourceInfo resourceClass) {
                try{
                    String path = resourceClass.getURITemplate().getValue();
                    resourceClass.getPath().value();
                    if(authToHeader(path)) {
                        Map<String, List<String>> headerMap = (Map<String, List<String>>)message.get(Message.PROTOCOL_HEADERS);
                        List<String> auths = headerMap.get(HttpHeaders.AUTHORIZATION);
                        if(auths == null || auths.isEmpty()){ //only do it if there's no authorization header
                            List<String> cookies = headerMap.get("cookie");
                            for(String cs : cookies) {
                                if(!StringUtils.isEmpty(cs)) {
                                    String cList [] = cs.split(";");
                                    if(cList != null){
                                        for(String oneCookie : cList) {
                                            log.debug("cookie found: "+oneCookie);
                                            Cookie c = Cookie.valueOf(oneCookie);
                                            if(c != null && "authToken".equals(c.getName()) && !StringUtils.isEmpty(c.getValue())) {
                                                String cValue = c.getValue();
                                                if(cValue.indexOf("%3B") > 0){
                                                    cValue = cValue.substring(0,cValue.indexOf("%3B"));
                                                }
                                                headerMap.put(HttpHeaders.AUTHORIZATION, Collections.singletonList(OAuthConstants.BEARER_AUTHORIZATION_SCHEME + " " + cValue));
                                                return null;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }catch(Exception ex) {
                    log.error("unable to reset authorization header based on auth cookie", ex);
                }
                return null;
            }
        };
    }
}

