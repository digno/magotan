package com.leadtone.where.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.leadtone.where.MsgConstants;
import com.leadtone.where.ServerConstants;
import com.leadtone.where.protocol.beans.Content;
import com.leadtone.where.protocol.beans.WhereMessage;
import com.leadtone.where.protocol.converter.ProtocolConverter;
import com.leadtone.where.protocol.converter.ResponseContentHelper;
import com.leadtone.where.service.IProtocolService;

@Scope("prototype")
@Component
public class WhereMessageProcessHandler extends
		SimpleChannelInboundHandler<Object> {

	private Logger log = Logger.getLogger(WhereMessageProcessHandler.class);

	@Autowired
	private IProtocolService protocolService;

	@Autowired
	private WhereChannelManager rcManager;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		String request = (String) msg;
		try {
			WhereMessage message = ProtocolConverter.marshallBasicMsg(request);

			rcManager.updateLastAliveTime(ctx.channel().hashCode());
			if (message == null) {
				return;
			}
			String result = "";

			// if(MsgConstants.SYSTEM.equals(message.getSubject())){
			// if(MsgConstants.PONG.equals(message.getContent().getType())){
			// return;
			// }
			// }

			if (ServerConstants.CONNECT.equals(message.getSubject())) {
				message.setContent(ResponseContentHelper
						.genSimpleResponseContentWithDefaultType(
								MsgConstants.ERROR_CODE_0, "connected"));
				result = ResponseHelper.genSyncResponse(message);
				ctx.writeAndFlush(new TextWebSocketFrame(result));
				return;
			}

			// 如果TO== server 则表明为同步操作，否则为异步
			if (ServerConstants.SERVER.equals(message.getTo())) {
				result = handleSyncMessage(ctx, message);
			} else {
				result = handleAsyncMessage(ctx, request, message);
			}
			// logger.info("current channel is : " + ctx.channel() +
			// " user use channel is : "+
			// channelsMap.get(message.getFrom()).getChannel());
			// ctx.fireChannelRead(result);
			ctx.writeAndFlush(new TextWebSocketFrame(result));

		} catch (Exception e) {
			log.error("handleWebSocketFrame error : " + e.getMessage());
			e.printStackTrace();
		}

	}

	private String handleAsyncMessage(ChannelHandlerContext ctx,
			String request, WhereMessage message) {
		String mobile = message.getFrom();
		Integer channelid = ctx.channel().hashCode();
		if (mobile != null) {
			if (!rcManager.isTheSameChannel(mobile, channelid)) {
				rcManager.mergeRiderChannels(message.getFrom(),ctx.channel());
			}
			// if (protocolService.boardcast(channelid,email,message.getTo(),request)) {
			if (protocolService.route(mobile, message.getTo(), request)) {
				message.setContent(ResponseContentHelper
						.genSimpleResponseContentWithDefaultType(
								MsgConstants.ERROR_CODE_0,
								"message send successed."));
			} else {
				message.setContent(ResponseContentHelper
						.genSimpleResponseContentWithDefaultType(
								MsgConstants.ERROR_CODE_1,
								"message send failed."));
			}
		} else {
			message.setContent(ResponseContentHelper
					.genSimpleResponseContentWithDefaultType(
							MsgConstants.ERROR_CODE_2,
							"please login first!"));
		}
		return ResponseHelper.genAsyncResponse(message);
	}

	private String handleSyncMessage(ChannelHandlerContext ctx, WhereMessage message) {
		Content resultContent;
		resultContent = protocolService.dispatch(message);
		// 如果是登陆请求，成功后才会加入 channelsMap
		// 如果不是登陆请求，则直接将 from作为 key 加入channelMap,因为客户端会缓存登陆成功的email地址
		// 如果登陆失败，则会删除缓存中的数据，email为null.
		if (ServerConstants.SUBJECT_LOGIN.equalsIgnoreCase(message.getContent().getType())) {
			String resultCode = (String) resultContent.getData().get("result");
			if (Integer.valueOf(resultCode) == 0) {
				String mobile = (String) message.getContent().getData().get("mobile");
				rcManager.mergeRiderChannels(mobile, ctx.channel());
			}
		} else {
			// 是否需要每次都megre呢
			rcManager.mergeRiderChannels(message.getFrom(),ctx.channel());
		}
		message.setContent(resultContent);
		return ResponseHelper.genSyncResponse(message);
	}

}
