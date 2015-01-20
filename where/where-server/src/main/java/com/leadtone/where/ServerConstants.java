package com.leadtone.where;

/**
 * 定义MEMCACHED协议中规定的命令，返回值 等等
 * 
 * @author lvqi
 */

public class ServerConstants {
    
    // 数据分隔符
    public static final String RES_SPLIT = "|";
    
    // 命令分隔符
    public static final String COMMAND_SPLIT = " ";
    
    // KEY 分隔符
    public static final String KEY_SPLIT = ":";
    
    // MEM SERVER分隔符
    public static final String SEPARATOR_COMMA = ",";
    
    public static final String SEPARATOR_SEMICOLON = ";";
    
    public static final String AT = "@";
    
    public static final String DELIMITER = "\r\n";
    
    public static final String SUBJECT_LOGIN="login";
    
    public static final String SUBJECT_PROFILE="profile";
    
    
	public static final String SERVER = "server";
	public static final String CLIENT = "client";
	public static final String PING = "ping";
	
	public static final String CONNECT = "connect";
	
	public static final String TO_ALL = "all";
	
	
}
