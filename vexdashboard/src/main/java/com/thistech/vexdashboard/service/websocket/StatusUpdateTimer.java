package com.thistech.vexdashboard.service.websocket;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import com.thistech.vexdashboard.common.model.VexboxStatus;
import com.thistech.vexdashboard.repository.VexboxRepository;
import com.thistech.vexdashboard.repository.VexboxStatusRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StatusUpdateTimer {
    private static final Log log = LogFactory.getLog(StatusUpdateTimer.class);

    private Timer updateTimer = null;
    DB db = null;
    MongoClient mongoClient;
    DBCollection collection;


    @Autowired
    protected VexboxStatusRepository vexboxStatusRepository;

    private static final long TICK_DELAY = 30000;  //30 seconds

    private final ConcurrentHashMap<String, DashboardClient> clients = new ConcurrentHashMap<>();

    public StatusUpdateTimer() {
        try {
            MongoClientURI uri = new MongoClientURI("mongodb://vexdashboard:vexdashboard@localhost:27017/vexdashboard");
            mongoClient = new MongoClient(uri);
            db = mongoClient.getDB(uri.getDatabase());
            collection = db.getCollection("VexboxStatus");
        } catch (UnknownHostException uhe) {
            log.error(uhe);
        }
    }

    protected synchronized void addClient(DashboardClient client) {
        if (clients.size() == 0) {
            startTimer();
        }
        clients.put(client.getId(), client);
        sendStatusToClient(getClients());
    }


    protected  Collection<DashboardClient> getClients() {
        return Collections.unmodifiableCollection(clients.values());
    }


    protected synchronized void removeClients(String sessionId) {
        clients.remove(sessionId);
        if (clients.size() == 0) {
            stopTimer();
        }
    }

    protected synchronized void updateClientIPList(String sessionId, List<String> ipList){
        DashboardClient client = clients.get(sessionId);
        if (client != null) {
            client.setIpAddresses(ipList);
        }
        sendStatusToClient(getClients());
    }
    public  void startTimer() {
        updateTimer = new Timer(StatusUpdateTimer.class.getSimpleName() + " Timer");
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    sendStatusToClient(getClients());
                } catch (RuntimeException e) {
                    log.error("Caught to prevent timer from shutting down", e);
                }
            }
        }, TICK_DELAY, TICK_DELAY);
    }

    public void stopTimer() {
        if (updateTimer != null) {
            updateTimer.cancel();
        }
    }

    private void sendStatusToClient(Collection<DashboardClient> clients) {
        try {
            Date endDate = new Date();
            long milliseconds = endDate.getTime();
            Date startDate = new Date(milliseconds - 30000);


            ArrayList<String> ips = new ArrayList<String>();
            for (DashboardClient client : clients) {
                List<BasicDBObject> criteria = new ArrayList<BasicDBObject>();
                List<String> ipList = client.getIpList();
                for (String ip : ipList) {
                    ips.add(ip);
                }
                criteria.add(new BasicDBObject("timestamp", new BasicDBObject("$lt", endDate)));
                criteria.add(new BasicDBObject("timestamp", new BasicDBObject("$gte", startDate)));
                criteria.add(new BasicDBObject ("ipaddress", new BasicDBObject("$in", ips)));
                DBCursor dbCursor = collection.find(new BasicDBObject("$and", criteria));
                StringBuffer bf = new StringBuffer();
                bf.append("{\"status\":[");
                try {
                    boolean isFirst = true;
                    while (dbCursor.hasNext()) {
                        if (!isFirst) {
                            bf.append(",");
                        } else {
                            isFirst = false;
                        }
                        bf.append(dbCursor.next());
                    }
                } finally {
                    dbCursor.close();
                }
                bf.append("]}");
                log.debug(bf.toString());
                Session session = client.getSession();
                if (session.isOpen()) {
                    session.getBasicRemote().sendText(bf.toString());
                } else {
                    log.warn("session has been closed." + session.toString());
                }

            }

        } catch (UnknownHostException uhe) {
            log.error(uhe);
        } catch (IOException ie) {
            log.error(ie);
        }
    }
}
