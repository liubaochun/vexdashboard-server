package com.thistech.vexdashboard.service.websocket;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DashboardClient {
    private final String id;
    private final Session session;
    private final List<String> ipList = new ArrayList<String>();

    public DashboardClient(String id, Session session) {
        this.id = id;
        this.session = session;
    }

    protected void sendMessage(String msg) {
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException ioe) {
            CloseReason cr =
                    new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, ioe.getMessage());
            try {
                session.close(cr);
            } catch (IOException ioe2) {
                // Ignore
            }
        }
    }

    public synchronized void setIpAddresses(List<String> ipAddresses) {
        this.ipList.clear();
        for (String ipAddress : ipAddresses) {
            ipList.add(ipAddress);
        }
    }

    public String getId() {
        return this.id;
    }

    public Session getSession() {
        return this.session;
    }

    public List<String> getIpList() {
        return this.ipList;
    }

}
