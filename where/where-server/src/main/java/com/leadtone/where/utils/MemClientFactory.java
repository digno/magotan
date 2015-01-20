package com.leadtone.where.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.HashAlgorithm;
import net.spy.memcached.MemcachedClient;

import org.apache.log4j.Logger;

import com.leadtone.where.ServerConstants;

public class MemClientFactory {

    private static Logger log = Logger.getLogger(MemClientFactory.class);
    
    private final String NAMESPACE = "";
    
    private static MemClientFactory instance = null;
    
    private MemcachedClient[] m = null;
    
    private int n = 5;
    
    private AtomicInteger currentNum = new AtomicInteger(0);
    
    private int currentNum1 = 0;
    
    private String servers = "127.0.0.1:11211";
    
    private MemClientFactory() {
       
    }
    
    private MemClientFactory(String serverUri, int clientPoolSize) {
        try {
            n = clientPoolSize;
            servers = serverUri;
            m = new MemcachedClient[n];
            log.info("serverUri is " + servers);
            log.info("clientPoolSize is " + n);
            for (int i = 0; i <= n - 1; i++) {
                MemcachedClient c = new MemcachedClient(
                    new DefaultConnectionFactory(
                        DefaultConnectionFactory.DEFAULT_OP_QUEUE_LEN,
                        DefaultConnectionFactory.DEFAULT_READ_BUFFER_SIZE,
                        HashAlgorithm.CRC32_HASH),
                    AddrUtil.getAddresses(servers));
                m[i] = c;
            }
        } catch (Exception e) {
            // ignore
        }
    }
    
    public static synchronized MemClientFactory getInstance(String serverUri,
            int clientPoolSize) {
        log.debug("Instance: " + instance);
        if (instance == null) {
            log.info("Creating a new instance");
            instance = new MemClientFactory(serverUri, clientPoolSize);
        }
        return instance;
    }
    
    public static synchronized MemClientFactory getInstance() {
        return instance == null ? null : instance;
    }
    
    // new algorithm increase 30% performance.
    private int getNum() {
        int i = currentNum.incrementAndGet();
        if (i < 0)
            i = -i;
        return i % n;
    }

    // old algorithm
    private synchronized int getNum1() {
        
        if (currentNum1 == n-1) {
            currentNum1 = 0;
        }else{
            currentNum1++;
        }
        return currentNum1;
    }
    
    public void set(String key, int ttl, final Object o) {
        getCache().set(NAMESPACE + key, ttl, o);
    }
    
    public Object get(String key) {
        Object o = getCache().get(NAMESPACE + key);
        if (o == null) {
            log.info("Cache MISS for KEY: " + key);
        } else {
            log.info("Cache HIT for KEY: " + key);
        }
        return o;
    }
    
    public Object delete(String key) {
        return getCache().delete(NAMESPACE + key);
    }
    
    public MemcachedClient getCache() {
        MemcachedClient c = null;
        try {
            
            // random faster than synchronized increase number 
//             int i = (int) (Math.random() * (n - 1));
//             c = m[i];
             // get unused client by synchronized method.
             int i = getNum();
             c = m[i];
            log.debug("get MemClient from MemcachedFactory Pool. instance[" + i +"]");
        } catch (Exception e) {
            log.error("Getting MemeClient form MemcachedFactory Pool Error .",
                e);
            // ignore
        }
        return c;
    }
     
    public void checkConnection(long cycleTime) {
        new CheckClientConnection(cycleTime).start();
    }
    
    private class CheckClientConnection extends Thread {
        
        private long cycleTime = 60000;
        
        private String[] serverUris = servers.split(ServerConstants.SEPARATOR_COMMA);
        
        public CheckClientConnection(long s) {
            this.cycleTime = s;
        }
        
        private  Map<Integer,String> getTestConnKeys(){
//            serverUris = servers.split(",");
            Map<Integer,String> map = new HashMap<Integer,String>();
            for(int i = 0;i<100;i++){
                String tempKey = Integer.valueOf(i).toString();
                map.put(getServerForKey(tempKey), tempKey);
            }
            return map;
        }
        
        private int getServerForKey(String key) {
            int rv=(int)(HashAlgorithm.CRC32_HASH.hash(key) % serverUris.length);
            assert rv >= 0 : "Returned negative key for key " + key;
            assert rv < serverUris.length
                : "Invalid server number " + rv + " for key " + key;
            return rv;
        }
        
        @Override
        public void run() {
            log.info("test Connection instance");
            Map<Integer,String> map = getTestConnKeys();
            while (true) {
                try {
                    for (int n = 0 ; n<m.length;n++ ) {
                        MemcachedClient c = m[n];
                        if (c != null) {
                            Set<Entry<Integer,String>> entries = map.entrySet();
                            for (Entry<Integer,String> entry : entries){
                                c.get(entry.getValue());
                            }
//                            for (Integer key :map.keySet()){
//                                c.get(map.get(key));
//                            }
//                            log.debug("Check Connection ...." + c.toString());
                        }
                        log.debug("test Connection instance["+n+"]");  
                    }
                    Thread.sleep(cycleTime);
                } catch (Exception e) {
                    log.warn("Connection caught Excetion . reconnection automatically. ");
                    // ignore
                }
            }
        }
    }
    
    public static void main (String [] args){
        MemClientFactory mf = new MemClientFactory();
        long t1 = System.nanoTime();
        for(int i = 0; i<= 10000000 ;i++){
            mf.getNum();
        }
        long t2 = System.nanoTime();
        System.out.println("loop 100000 used " + (t2-t1));
        t1 = System.nanoTime();
        for(int i = 0; i<= 10000000 ;i++){
            mf.getNum1();
        }
        t2 = System.nanoTime();
        System.out.println("loop 10000000 used " + (t2-t1));
    }
}
