package com.thistech.vexdashboard.config;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.zookeeper.WatchedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static org.apache.zookeeper.Watcher.Event.EventType.NodeDataChanged;

@Service
public class ConfigListener implements CuratorListener {
    private static final Logger log = LoggerFactory.getLogger(ConfigListener.class);

    @Override
    public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
        try{
            log.info("Event path: " + event.getPath());
            switch (event.getType()) {
                case CHILDREN:
                    //if(event.getPath().contains("/assign")) {
                    if(event.getPath().contains("/assignCurator")) {
                        log.info("Succesfully got a list of assignments: "
                                + event.getChildren().size()
                                + " tasks");
                            /*
                            * Delete the assignments of the absent worker
                            */
                        for(String task : event.getChildren()){

                        }

                            /*
                            * Delete the znode representing the absent worker
                            * in the assignments.
                            */


                            /*
                            * Reassign the tasks.
                            */

                    } else {
                        log.warn("Unexpected event: " + event.getPath());
                    }

                    break;
                case CREATE:
                        /*
                        * Result of a create operation when assigning
                        * a task.
                        */
                    //if(event.getPath().contains("/assign")) {
                    if(event.getPath().contains("/assignCurator")) {
                        log.info("Task assigned correctly: " + event.getName());
                    }

                    break;
                case DELETE:
                        /*
                        * We delete znodes in two occasions:
                        * 1- When reassigning tasks due to a faulty worker;
                        * 2- Once we have assigned a task, we remove it from
                        *    the list of pending tasks.
                        */
                    //if(event.getPath().contains("/tasks")) {
                    if(event.getPath().contains("/tasksCurator")) {
                        log.info("Result of delete operation: " + event.getResultCode() + ", " + event.getPath());
                        //} else if(event.getPath().contains("/assign")) {
                    } else if(event.getPath().contains("/assignCurator")) {
                        log.info("Task correctly deleted: " + event.getPath());
                        break;
                    }

                    break;
                case WATCHED:

                    final WatchedEvent watchedEvent = event.getWatchedEvent();
                    if (watchedEvent.getType() == NodeDataChanged && StringUtils.equals("/lock", event.getPath())) {

                    }
                    break;
                default:
                    log.error("Default case: " + event.getType());
            }
        } catch (Exception e) {
            log.error("Exception while processing event.", e);
//            try{
//                close();
//            } catch (IOException ioe) {
//                log.error("IOException while closing.", ioe);
//            }
        }
    }
}
