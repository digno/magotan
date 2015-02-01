package com.leadtone.where.notify;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.leadtone.where.ConcurrentContext;
import com.leadtone.where.protocol.beans.WhereMessage;
import com.leadtone.where.protocol.converter.ProtocolConverter;
import com.leadtone.where.server.WhereChannel;
import com.leadtone.where.server.WhereChannelManager;
import com.leadtone.where.service.biz.MessageServiceImpl;

@Component
public class WhereNotificationService {

	private Logger log = Logger.getLogger(WhereNotificationService.class);

	@Autowired
	private WhereChannelManager wmc;

	@Autowired
	private MessageServiceImpl messageService;

	private LinkedBlockingQueue<WhereMessage> nbox;

	public void start() {
		nbox = WhereContext.getNotificationQueue();
		ExecutorService es = Executors.newSingleThreadExecutor();
		es.execute(new WhereNotifictionEmitter());
		log.info("WhereNotificationService started.");
	}

	private class WhereNotifictionEmitter implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					WhereMessage mail = nbox.take();
					String to = mail.getTo();
					String strMsg = ProtocolConverter.unmarshallMsg(mail);
//					WhereChannel channel = wmc.getActiveWhereChannel().get(to);
					WhereChannel channel = ConcurrentContext.getChannelMapInstance().get(to);
					if (channel != null && channel.isActive()) {
						log.info("channel : [ id = " + channel.getChannelId()  +"] send WhereNotification to " + to
								+ " , Content is " + strMsg);
						channel.getChannel().write(new TextWebSocketFrame(strMsg));
						channel.getChannel().flush();
//						ChannelFuture cf = channel.getChannel().writeAndFlush(new TextWebSocketFrame(strMsg));
//						if (!cf.isSuccess()){
//							log.info("can not send msg to " + to + " . save notification.");
//							messageService.saveUndeliverMessage(mail);
//						}
					} else {
						log.info(to + " is not logon . save notification.");
						messageService.saveUndeliverMessage(mail);
					}
					Thread.sleep(50);
				} catch (Exception e) {
					log.error("send notification error .", e);
				}
			}
		}

	}

	public static void main(String[] args) {
		WhereNotificationService service = new WhereNotificationService();
		service.start();
		try {
			Thread.sleep(1000);
			for (int i = 0; i < 10; i++) {
				WhereContext.getNotificationBox().put(
						new WhereNotification("aaaa" + i));
				Thread.sleep(500);
			}
			System.out.println(WhereContext.getNotificationBox().size());

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
