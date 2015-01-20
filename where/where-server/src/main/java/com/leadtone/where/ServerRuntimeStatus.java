package com.leadtone.where;

import java.util.concurrent.atomic.AtomicBoolean;

public class ServerRuntimeStatus {
	
    public static volatile AtomicBoolean dbStatus = new AtomicBoolean(true);
	
}
