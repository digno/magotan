package com.leadtone.where.service;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leadtone.where.ConcurrentContext;
import com.leadtone.where.MsgConstants;
import com.leadtone.where.ServerConstants;
import com.leadtone.where.ServiceKeyEnum;
import com.leadtone.where.bean.ActivityUser;
import com.leadtone.where.protocol.beans.Content;
import com.leadtone.where.protocol.beans.WhereMessage;
import com.leadtone.where.protocol.converter.ResponseContentHelper;
import com.leadtone.where.server.WhereChannel;
import com.leadtone.where.service.biz.ActivityServiceImpl;
import com.leadtone.where.service.biz.GeoServiceImpl;
import com.leadtone.where.service.biz.MessageServiceImpl;
import com.leadtone.where.service.biz.SystemServiceImpl;
import com.leadtone.where.service.biz.UserServiceImpl;

@Service
public class ProtocolServiceImpl implements IProtocolService {

	private static final Logger log = Logger
			.getLogger(ProtocolServiceImpl.class);

	private ConcurrentHashMap<String, WhereChannel> channelsMap = ConcurrentContext
			.getChannelMapInstance();
	
	private ConcurrentHashMap<Integer, WhereChannel> cMap = ConcurrentContext.getAvailableChannelMapInstance();

	@Autowired
	private IBizService bizService;
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private ActivityServiceImpl activityService;
	@Autowired
	private SystemServiceImpl sysService;
	
	@Autowired
	private MessageServiceImpl messageService;
	
	@Autowired
	private GeoServiceImpl geoService;

	@Override
	public Content dispatch(WhereMessage message) {
		Content result = null;
		String subject = message.getSubject();
		if (StringUtils.isBlank(subject)) {
			result = ResponseContentHelper.genSimpleResponseContentWithoutType(
					MsgConstants.ERROR_CODE_2, "unsupport subject");
			result.setType(MsgConstants.SERVER);
			return result;

		}
		ServiceKeyEnum mk = ServiceKeyEnum.getEnum(subject.toUpperCase());
		Content requestContent = message.getContent();
		switch (mk) {
		case USER:
			result = bizService.process(requestContent,userService);
			break;
		case FRIEND:
			break;
		case ACTIVITY:
			result = bizService.process(requestContent,activityService);
			break;
		case COMMENT:
			break;
		case TEAM:
			break;
		case SOS:
			break;
		case GEO:
			result = bizService.process(requestContent,geoService);
			break;
		case SUGGESTION:
			break;
		case VERSION:
			break;
		case SYSTEM:
			if (requestContent.getType() == null ){
				requestContent.setType(MsgConstants.TIDE);
			}
			result = bizService.process(requestContent,sysService);
			break;
		default:
			result = ResponseContentHelper.genSimpleResponseContentWithoutType(
					MsgConstants.ERROR_CODE_2, "unsupport subject");
			result.setType(MsgConstants.SERVER);
		}
		return result;

	}

	@Override
	public boolean boardcast(Integer channelId , String from, String to, String request) {
		if (ServerConstants.TO_ALL.equalsIgnoreCase(to)) {
//			for (String email : channelsMap.keySet()) {
//				if (from.equalsIgnoreCase(email)) {
//					continue;
//				}
//				RiderChannel channel = channelsMap.get(email);
//				writeToChannel(channel, request);
//			}
			for (Integer id : cMap.keySet()){
				
				if (channelId == id) {
					continue;
				}
				WhereChannel channel = cMap.get(id);
				writeToChannel(channel, request);
			}
		} else {
			WhereChannel channel = channelsMap.get(to);
			writeToChannel(channel, request);
		}
		return true;
	}
	
	private boolean isGroupChat(String to){
		return to.length() == 6;
	}

	@Override
	public boolean route(String from, String to, String message) {
		
		if (isGroupChat(to)) {
			List<ActivityUser> activityUsers = messageService.findActivityMembers(to);
			if (activityUsers.size() == 0 ){
				log.info("no member join Activity : " + to );
				return true;
			}
			for (ActivityUser user : activityUsers) {
				if (from.equalsIgnoreCase(user.getMobile())) {
					continue;
				}
				WhereChannel channel = channelsMap.get(user.getMobile());
				if (channel!=null){
					writeToChannel(channel, message);
				}else {
					messageService.saveUndeliverMessage(user.getMobile(),message);
				}
			}
		} else {
			WhereChannel channel = channelsMap.get(to);
			if(channel!=null){
				writeToChannel(channel, message);
			}else{
				messageService.saveUndeliverMessage(to,message);
			}
			
		}
		return true;
	}
	
	
	public boolean routeOld(String from, String to, String message) {
		if (ServerConstants.TO_ALL.equalsIgnoreCase(to)) {
			for (String mobile : channelsMap.keySet()) {
				if (from.equalsIgnoreCase(mobile)) {
					continue;
				}
				WhereChannel channel = channelsMap.get(mobile);
				writeToChannel(channel, message);
			}
		} else {
			WhereChannel channel = channelsMap.get(to);
			writeToChannel(channel, message);
		}
		return true;
	}
	
	
	// 如果客户端响应了ping 请求则服务器会同步数据给客户端
	// 就如同潮水一样，一波一波的。^_^
	
	private void writeToChannel(WhereChannel channel, String request) {
		try {
			channel.getChannel().write(new TextWebSocketFrame(request));
			channel.getChannel().flush();
//			channel.getChannel().writeAndFlush(new TextWebSocketFrame(request), promise)
//			if (channel.isLogined()) {
//				channel.getChannel().write(new TextWebSocketFrame(request));
//			} else {
//				log.info("BAD REQUEST. (not logined)");
//			}
		} catch (Exception e) {
			log.error(e);
		}
	}

	

}
