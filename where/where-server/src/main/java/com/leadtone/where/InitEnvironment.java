package com.leadtone.where;

import java.util.Timer;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.leadtone.mig.tools.annotations.Config;
import com.leadtone.where.server.DbCheckTimerTask;


@Component
public class InitEnvironment {
    
    private static final Logger log = Logger.getLogger(InitEnvironment.class);
    
    public static ApplicationContext context = null;
    

    @Config("check_consistence_thread")
    private int consistenceThread = 5;
    

    @Config("db_check_period")
    private int dbCheckPeriod = 30;
    
    public InitEnvironment() {
        
    }
    
    public static boolean initLog4j() {
        boolean result = false;
        try {
            DOMConfigurator.configure(ClassLoader
                .getSystemResource("log4j.xml"));
            result = true;
            log.info("init log4j successed!");
        } catch (Exception e) {
            log.error("init log4j error!" +  e.getMessage() ,e );
        }
        return result;
    }
    
    public static ApplicationContext initAppclicatContext() {
        try {
            context = new ClassPathXmlApplicationContext(
                new String[] {"classpath:applicationContext.xml"});
            log.info("init applicationContext successed!");
        } catch (Exception e) {
            log.error("init applicationContext error! " + e.getMessage(),e);
        }
        
        return context;
    }
    
    public static ApplicationContext getAppclicatContextInstance(){
    	if (context == null){
    		initAppclicatContext();
    	}
    	return context;
    }
    
   
    
    public void initSchedule(){
//    	Timer timer = new Timer();
//        timer.schedule(new DbCheckTimerTask(),0,dbCheckPeriod*1000);
    	
    	// 定期删除未投递的消息
    }

  
}
