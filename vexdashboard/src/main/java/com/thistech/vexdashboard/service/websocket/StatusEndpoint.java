package com.thistech.vexdashboard.service.websocket;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@ServerEndpoint("/websocket/status")
public class StatusEndpoint {

    @Resource
    StatusUpdateTimer statusUpdateTimer = new StatusUpdateTimer();

    @OnOpen
    public void onOpen(Session session) {
        DashboardClient client = new DashboardClient(session.getId(), session);
        statusUpdateTimer.addClient(client);
    }

    @OnMessage
    public void  onMessage(Session session, String msg, boolean last) {
        try {
            if (session.isOpen()) {
                List<String> ipList = new ArrayList<String>();
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(msg);
                JSONArray ipaddresses = (JSONArray) jsonObject.get("ipaddresses");
                for (int i = 0; i < ipaddresses.size(); i++) {
                    ipList.add(ipaddresses.get(i).toString());
                }
                statusUpdateTimer.updateClientIPList(session.getId(), ipList);
                session.getBasicRemote().sendText("IP addresses have been parsed.", last);
            }
        } catch (ParseException pe) {

        } catch (IOException e) {
            try {
                statusUpdateTimer.removeClients(session.getId());
                session.close();
                e.printStackTrace();
            } catch (IOException e1) {
                // Ignore
            }
        }


    }

    @OnClose
    public void onClose(Session session, CloseReason reason) throws IOException {
        //prepare the endpoint for closing.
        statusUpdateTimer.removeClients(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable t) {
        t.printStackTrace();
    }
}
