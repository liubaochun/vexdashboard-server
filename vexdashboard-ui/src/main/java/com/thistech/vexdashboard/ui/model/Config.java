package com.thistech.vexdashboard.ui.model;

import java.util.ArrayList;
import java.util.List;

public class Config {

    private String vexdashboardProxyAddress;
    private String vinzProxyAddress;
    private List<String> authCrossOriginWhitelist = new ArrayList<>();
    private String projectVersion;
    private String buildDate;
    private String buildRevision;
    private String[] modules;
    private boolean enablePasswordReset;

    public Config() {}

    private Config(Config that) {
        this.vexdashboardProxyAddress = that.getVexdashboardProxyAddress();
        this.vinzProxyAddress = that.getVinzProxyAddress();
        this.authCrossOriginWhitelist = new ArrayList<>();
        this.authCrossOriginWhitelist.addAll(that.getAuthCrossOriginWhitelist());
        this.projectVersion = that.getProjectVersion();
        this.buildDate = that.getBuildDate();
        this.buildRevision = that.getBuildRevision();
        this.modules = that.modules;
        this.enablePasswordReset = that.enablePasswordReset;
    }

    public String getVexdashboardProxyAddress() { return vexdashboardProxyAddress; }
    public Config setVexdashboardProxyAddress(String vexdashboardProxyAddress) {
        this.vexdashboardProxyAddress = vexdashboardProxyAddress;
        return this;
    }

    public String getVinzProxyAddress() { return vinzProxyAddress; }
    public Config setVinzProxyAddress(String vinzProxyAddress) {
        this.vinzProxyAddress = vinzProxyAddress;
        return this;
    }

    public List<String> getAuthCrossOriginWhitelist() {
        if (authCrossOriginWhitelist == null) {
            authCrossOriginWhitelist = new ArrayList<>();
        }
        return authCrossOriginWhitelist;
    }
    public Config setAuthCrossOriginWhitelist(List<String> authCrossOriginWhitelist) {
        this.authCrossOriginWhitelist = authCrossOriginWhitelist;
        return this;
    }

    public String getProjectVersion() { return projectVersion; }
    public Config setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
        return this;
    }

    public String getBuildDate() { return buildDate; }
    public Config setBuildDate(String buildDate) {
        this.buildDate = buildDate;
        return this;
    }

    public String getBuildRevision() { return buildRevision; }
    public Config setBuildRevision(String buildRevision) {
        this.buildRevision = buildRevision;
        return this;
    }

    public String[] getModules() { return modules; }
    public Config setModules(String[] modules) {
        this.modules = modules;
        return this;
    }

    public boolean isEnablePasswordReset() {
        return enablePasswordReset;
    }

    public Config setEnablePasswordReset(boolean enablePasswordReset) {
        this.enablePasswordReset = enablePasswordReset;
        return this;
    }

    public Config withProxyPrefix(String prefix) {
        Config config = new Config(this);
        config.vexdashboardProxyAddress = prefix + config.getVexdashboardProxyAddress();
        config.vinzProxyAddress = prefix + config.getVinzProxyAddress();
        return config;
    }
}

