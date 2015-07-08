package com.thistech.vexdashboard.repository;

import com.mongodb.*;
import com.thistech.vexdashboard.service.websocket.StatusUpdateTimer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by brent on 7/6/15.
 */
public class ParserTest {
    public static void main(String[] args) {

                /*List<String> ipList = new ArrayList<String>();
                String msg = "{\"ipaddresses\": [\"192.168.204,1\", \"192.168.204.2\"]}";
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(msg);
                JSONArray ipaddresses = (JSONArray) jsonObject.get("ipaddresses");
                for (int i = 0; i < ipaddresses.size(); i++) {
                    ipList.add(ipaddresses.get(i).toString());
                }
                System.out.println(ipList);
                */
            try {
                MongoClientURI uri = new MongoClientURI("mongodb://vexdashboard:vexdashboard@localhost:27017/vexdashboard");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());
                DBCollection collection = db.getCollection("VexboxStatus");

                List<BasicDBObject> criteria = new ArrayList<BasicDBObject>();
                Date endDate = new Date();
                long milliseconds = endDate.getTime();
                ArrayList<String> ips = new ArrayList<String>();
                ips.add("192.168.204.1");
                ips.add("192.168.204.101");
                Date startDate = new Date(milliseconds - 30000);
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
                System.out.println(bf.toString());
            } catch (UnknownHostException uhe) {
                System.out.println(uhe);
            }

    }
}
