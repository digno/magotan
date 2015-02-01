package com.leadtone.where.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.leadtone.mig.tools.annotations.Config;
import com.leadtone.where.ConcurrentContext;
import com.leadtone.where.MsgConstants;
import com.leadtone.where.protocol.beans.Content;
import com.leadtone.where.protocol.beans.WhereMessage;
import com.leadtone.where.protocol.converter.ProtocolConverter;

//firefox 在每次请求后回自动关闭上个websocket连接，而chrom则不会
// 因此在开发期间，无论同步异步消息均需要merge连接
// 问题1：有一部分同步消息，没有email(用户无需登录)，如果请求中from == null,则用户无法收到聊天信息的提示
// 问题2：但是如果每次都megre 连接会导致2个 CurrentHashMap的比对，如果连接太多，会影响性能

@Component
public class WhereChannelManager {

	private Logger log = Logger.getLogger(WhereChannelManager.class);

	@Config("idel_peroid")
	private int idelPeroid = 300;
	
	// 登陆后的链路信息key为用户的email
	private ConcurrentHashMap<String, WhereChannel> channelsMap = ConcurrentContext
			.getChannelMapInstance();

	// 所有的链路集合 key 为channelid
	private ConcurrentHashMap<Integer, WhereChannel> cMap = ConcurrentContext
			.getAvailableChannelMapInstance();

	/**
	 * channelsMap(email,riderChannel),cMap(channel_id,riderChannel) 如果服务器收到
	 * close 的textFrame 1：关闭channel 2：删除cMap中的channel 3: 删除channelMap 中的channel
	 * 
	 * 如果服务器收到业务处理消息 1：from为空 2：不做任何处理，此时连接数量上cMap>channelsMap 3：from不为空
	 * 4：检查channelsMap 中key 为from 的连接是否与 cMap中的相同 if (channelMap.get(email) ==
	 * cMap.get(channelMap.get(email).getChannelId()));
	 * 5：如果不同，channelMap.replace(email,channel)
	 * 
	 * 
	 */

	public synchronized void closeRiderChannel(Integer channelId) {
		long start = System.currentTimeMillis();
		WhereChannel rc = cMap.get(channelId);
		for (String mobile : channelsMap.keySet()) {
			WhereChannel temp = channelsMap.get(mobile);
			if (channelId.intValue() == temp.getChannelId().intValue()) {
				channelsMap.remove(mobile);
				log.info("remove logined channel [ " + mobile+" ].");
				break;
			}
		}
		rc.getChannel().close();
		cMap.remove(channelId);
		long end = System.currentTimeMillis();
		log.info("closeRiderChannel used " + (end - start) + " ms.");
	}

	public synchronized void registerRiderChannel(Channel channel) {
		WhereChannel riderChannel = new WhereChannel();
		riderChannel.setChannelId(channel.hashCode());
		riderChannel.setChannel(channel);
		cMap.put(channel.hashCode(), riderChannel);
	}

	/**
	 * 将客户端的websocket连接与用户的email对应起来 如果连接channel 与 缓存中email对应的channel 不同 则关闭缓存中的
	 * channel 并将新的channel更新至缓存
	 * 
	 * @param mobile
	 * @param channel
	 */
	public synchronized void mergeRiderChannels(String mobile, Channel channel) {
		long start = System.currentTimeMillis();
		WhereChannel rc = cMap.get(channel.hashCode());
		Integer nowChannelId = rc.getChannelId();
		if (StringUtils.isBlank(mobile) || "null".equals(mobile)) {
			log.warn("mobile is :" + mobile);
//			logChannels();
			return;
		}
		WhereChannel oldChannel = channelsMap.get(mobile);
		if (oldChannel != null) {
			Integer oldChannelId = oldChannel.getChannelId();
			if (nowChannelId.intValue() != oldChannelId.intValue()) {
				cMap.remove(oldChannelId);
				channelsMap.replace(mobile, rc).getChannel().close();
				log.warn(oldChannelId + " Channel for " + mobile + " meraged! ");
			}
		} else {
			channelsMap.put(mobile, rc);
		}
		long end = System.currentTimeMillis();
		log.info("mergeRiderChannels used " + (end - start) + " ms.");
		logChannels();

	}

	public synchronized void updateLastAliveTime(Integer channel_id){
		WhereChannel rc = cMap.get(channel_id);
		if(rc != null){
			rc.setLastAliveTime(System.currentTimeMillis());
		}
	}
	
	public boolean isTheSameChannel(String mobile, Integer channelid) {
		// 如果 email 为空则返回用户需要登陆
		return channelsMap.get(mobile) == cMap.get(channelid);
	}

	
	public void checkRiderChannel() {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						// 3个5分钟
						long checkPeriod = idelPeroid * 1000; //
						for (Integer channelid : cMap.keySet()) {
							WhereChannel rc = cMap.get(channelid);
							// 如果链接不可用，立即关闭
							if (!sendPingMsg(rc.getChannel())){
								closeRiderChannel(channelid);
							}
							// 如果长时间闲置则关闭
							if ((System.currentTimeMillis() - rc.getLastAliveTime()) > checkPeriod * 3) {
								closeRiderChannel(channelid);
							}
						}
						logChannels();
						Thread.sleep(checkPeriod);
					} catch (Exception e) {
						e.printStackTrace();
						log.error("check riderChannel error !");
					}
				}
			}
		};
		Thread checkThread = new Thread(r);
		checkThread.start();
	}

	private boolean sendPingMsg(Channel channel) {
		if (!channel.isActive() || !channel.isWritable()){
			return false;
		}
		WhereMessage message = new WhereMessage();
		message.setMsg_id("-1");
		message.setFrom(MsgConstants.SERVER);
		message.setTo(MsgConstants.CLIENT);
		message.setSubject(MsgConstants.SYSTEM);
		Content content = new Content();
		content.setType(MsgConstants.PING);
		content.setData(new HashMap<String,Object>());
		message.setContent(content);
		message.setCreateDate(Long.toString(System.currentTimeMillis()));
		message.setVersion(MsgConstants.VERSION);
		String msgStr = ProtocolConverter.unmarshallMsg(message);
		ChannelFuture cf = channel.writeAndFlush(new TextWebSocketFrame(msgStr));
		return cf.isSuccess();
		
	}

	// Channel 状态变化过程  open -> register -> active 
	// isWritable @see http://netty.io/4.0/api/io/netty/buffer/ByteBuf.html
	// Returns true if and only if this buffer has enough room to allow writing the specified number of elements.
	private void logChannels() {
		log.info("total user is " + channelsMap.size() + " channel size is "
				+ cMap.size());
		log.info("------------ channelsMap -----------------");
		for (String e : channelsMap.keySet()) {
			WhereChannel r = channelsMap.get(e);
			log.info("        " + e + " [" + r.getChannelId() + "] : " + r + " status : " + r.getChannel().isActive());
		}
		log.info("--------------- cMap --------------------");
		for (Integer e : cMap.keySet()) {
			WhereChannel r = cMap.get(e);
			log.info("        " + e + " : " + cMap.get(e) + " status : " + r.getChannel().isActive());
		}
	}
	
	public ConcurrentHashMap<String, WhereChannel> getActiveWhereChannel(){
		return channelsMap;
	}

}
