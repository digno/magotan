package com.leadtone.where.server;

import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.leadtone.where.ServerRuntimeStatus;

@Service
public class DbCheckTimerTask extends TimerTask {
	
	private static Logger log = Logger.getLogger(DbCheckTimerTask.class);
	
	@Override
	public void run() {
		log.info("Task Begin...");
		ServerRuntimeStatus.dbStatus.set(true);
		log.info("reset DbStatus.");
	}

}
