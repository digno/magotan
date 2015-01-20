package com.leadtone.where;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.leadtone.where.notify.WhereNotificationService;
import com.leadtone.where.server.WhereChannelManager;
import com.leadtone.where.server.WhereWebSocketServer;

public class WhereServer {
    private static Logger log = Logger.getLogger(WhereServer.class);
    
    public ApplicationContext context;
    
    public WhereWebSocketServer server;
    
    public WhereChannelManager cManager;
    
    public WhereNotificationService notificationService;
    
    public void init() {
        context = InitEnvironment.initAppclicatContext();
        InitEnvironment.initLog4j();
        context.getBean(InitEnvironment.class).initSchedule();
        server = context.getBean(WhereWebSocketServer.class);
        cManager = context.getBean(WhereChannelManager.class);
        notificationService = context.getBean(WhereNotificationService.class);
    }
    

    
    
    public static void main(String args[]) {
        WhereServer service = new WhereServer();
            service.init();
            log.info("inited environment successed!");
            try {
            	service.cManager.checkRiderChannel();
            	service.notificationService.start();
				service.server.start();
			} catch (Exception e) {
				log.error("start where server error ." ,e);
			} 
    }
    
}
