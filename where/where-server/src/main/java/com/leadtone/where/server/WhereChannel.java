package com.leadtone.where.server;

import io.netty.channel.Channel;

public class WhereChannel {

	// 通道ID
	private Integer channelId;
	
	// 通道
	private Channel channel;
	
	// 是否是可接受消息的状态
	private boolean isActive = true;
	
	private boolean isLogined = false;
	
	private long lastAliveTime = System.currentTimeMillis();

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isLogined() {
		return isLogined;
	}

	public void setLogined(boolean isLogined) {
		this.isLogined = isLogined;
	}

	public long getLastAliveTime() {
		return lastAliveTime;
	}

	public void setLastAliveTime(long lastAliveTime) {
		this.lastAliveTime = lastAliveTime;
	}

}
