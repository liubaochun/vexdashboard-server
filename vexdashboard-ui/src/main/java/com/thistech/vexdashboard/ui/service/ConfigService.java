package com.thistech.vexdashboard.ui.service;

import com.thistech.vexdashboard.ui.model.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Service
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/config")
public class ConfigService {
    private static final Logger log = LoggerFactory.getLogger(ConfigService.class);
    private Config config = new Config();
    // proxyAddress should match the servlet-mapping from web.xml (couldn't find a way to get the mappings programmatically)

    @Value("${vexdashboardProxyAddress:/vexdashboard}")
    public void setVexdashboardProxyAddress(String s) { config.setVexdashboardProxyAddress(s); }

    @Value("${vinzProxyAddress:/vinz}")
    public void setVinzProxyAddress(String s) { config.setVinzProxyAddress(s); }

    @Value("${project.version}")
    public void setProjectVersion(String s) { config.setProjectVersion(s); }

    @Value("${build.date}")
    public void setBuildDate(String s) { config.setBuildDate(s); }

    @Value("${build.revision}")
    public void setBuildRevision(String s) { config.setBuildRevision(s); }

    @Value("${authCorssOriginWhitelist:http://localhost:8080}")
    public void setAuthCrossOriginWhitelist(String spaceSeparated) {
        List<String> whitelist = new ArrayList<>();
        for (String s : spaceSeparated.split(" ")) {
            whitelist.add(s.trim());
        }
        config.setAuthCrossOriginWhitelist(whitelist);
    }

    @Value("${enable.password.reset:true}")
    public void setEnablePasswordReset(String s){
        config.setEnablePasswordReset(Boolean.valueOf(s));
    }

    @Value("${modules:vinz}")
    public void setModules(String s){
        config.setModules(s.split(",\\s*"));
    }

    @Context
    private ServletContext servletContext;

    /**
     * Retrieve user interface configuration information such as proxy URLs and build revision.
     * Note: app.js requests scripts/configuration.json which is forwarded here in urlrewrite.xml
     * @return configuration information
     */
    @GET
    public Config getConfig() {
        return config.withProxyPrefix(servletContext.getContextPath());
    }
}

