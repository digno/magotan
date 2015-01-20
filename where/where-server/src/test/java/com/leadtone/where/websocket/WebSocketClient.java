/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
//The MIT License
//
//Copyright (c) 2009 Carl Bystršm
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in
//all copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//THE SOFTWARE.
package com.leadtone.where.websocket;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.apache.commons.lang3.RandomStringUtils;

import com.leadtone.where.protocol.beans.Content;
import com.leadtone.where.protocol.beans.WhereMessage;
import com.leadtone.where.protocol.converter.ProtocolConverter;
import com.leadtone.where.utils.DateProvider;

public class WebSocketClient {

    private final URI uri;

    public WebSocketClient(URI uri) {
        this.uri = uri;
    }
    

    
    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            String protocol = uri.getScheme();
            if (!"ws".equals(protocol)) {
                throw new IllegalArgumentException("Unsupported protocol: " + protocol);
            }

            HttpHeaders customHeaders = new DefaultHttpHeaders();
            customHeaders.add("MyHeader", "MyValue");

            // Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
            // If you change it to V00, ping is not supported and remember to change
            // HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.
            final WebSocketClientHandler handler =
                    new WebSocketClientHandler(
                            WebSocketClientHandshakerFactory.newHandshaker(
                                    uri, WebSocketVersion.V13, null, false, customHeaders));

            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline pipeline = ch.pipeline();
                     pipeline.addLast("http-codec", new HttpClientCodec());
                     pipeline.addLast("aggregator", new HttpObjectAggregator(8192));
                     pipeline.addLast("ws-handler", handler);
                 }
             });

            System.out.println("WebSocket Client connecting");
            Channel ch = b.connect(uri.getHost(), uri.getPort()).sync().channel();
            handler.handshakeFuture().sync();

            System.out.println("WebSocket Client sending message");
            
//            ch.writeAndFlush(new TextWebSocketFrame(genRegMessage()));
//            ch.writeAndFlush(new TextWebSocketFrame(genLoginMessage()));
//            ch.writeAndFlush(new TextWebSocketFrame(genGetUserListMessage()));
//            ch.writeAndFlush(new TextWebSocketFrame(genGetUserDetailMessage()));
//            ch.writeAndFlush(new TextWebSocketFrame(genModifyUserMessage()));
//            ch.writeAndFlush(new TextWebSocketFrame(genAddActivityMessage()));
//            ch.writeAndFlush(new TextWebSocketFrame(genGetActivityMessage()));
//            ch.writeAndFlush(new TextWebSocketFrame(genFindActivityMessage()));
//            ch.writeAndFlush(new TextWebSocketFrame(genListActivitiesMessage()));
//            
//            ch.writeAndFlush(new TextWebSocketFrame(genJoinActivitiesMessage()));
            ch.writeAndFlush(new TextWebSocketFrame(genChatMessage()));
            
            
            // Ping
            System.out.println("WebSocket Client sending ping");
            ch.writeAndFlush(new PingWebSocketFrame(Unpooled.copiedBuffer(new byte[]{1, 2, 3, 4, 5, 6})));

            // Close
            System.out.println("WebSocket Client sending close");
            ch.writeAndFlush(new CloseWebSocketFrame());

            // WebSocketClientHandler will close the connection when the server
            // responds to the CloseWebSocketFrame.
            ch.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

	private DateProvider dateProvider = DateProvider.DEFAULT;
	
    // GEN MSGS
    private WhereMessage genBasicMessage(String subject,String from,String to){
    	
    	WhereMessage message = new WhereMessage();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
    	message.setCreateDate(sdf.format(dateProvider.getDate()));
    	message.setFrom(from);
    	message.setMsg_id(RandomStringUtils.randomNumeric(10));
    	message.setStatus("0");
    	message.setSubject(subject);
    	message.setTo(to);
    	message.setVersion("1.0");
    	return message;
    	
    }
    
    // 用户登陆
    private String genLoginMessage(){
    	WhereMessage message = genBasicMessage("user", "13910766800", "server");
    	Content content = new Content();
    	content.setType("login");
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("mobile", "13910766800");
    	map.put("password","1234");
    	content.setData(map);
    	message.setContent(content);
    	
    	return ProtocolConverter.unmarshallMsg(message);
    }
    
    // 用户注册
    private String genRegMessage(){
    	WhereMessage message = genBasicMessage("user", "13910766842", "server");
    	Content content = new Content();
    	content.setType("register");
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("mobile", "13910766800");
    	map.put("password","1234");
    	map.put("nickname", "");
    	map.put("sex", "MAN");
    	map.put("picture", "");
    	content.setData(map);
    	message.setContent(content);
    	return ProtocolConverter.unmarshallMsg(message);
    }
    
    // 获取用户列表
    private String genGetUserListMessage(){
    	WhereMessage message = genBasicMessage("user", "13910766842", "server");
    	Content content = new Content();
    	content.setType("getUserList");
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	content.setData(map);
    	message.setContent(content);
    	return ProtocolConverter.unmarshallMsg(message);
    }
    
 // 获取用户信息
    private String genGetUserDetailMessage(){
    	WhereMessage message = genBasicMessage("user", "13910766842", "server");
    	Content content = new Content();
    	content.setType("getUserDetail");
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("mobile", "13910766800");
    	content.setData(map);
    	message.setContent(content);
    	return ProtocolConverter.unmarshallMsg(message);
    }
    
    // 更新用户信息
    private String genModifyUserMessage(){
    	WhereMessage message = genBasicMessage("user", "13910766842", "server");
    	Content content = new Content();
    	content.setType("modifyUser");
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("picture", "http://124.251.8.70/pics/wawa.jpg");
    	map.put("nickname", "digno");
    	map.put("mobile", "13910766800");
    	content.setData(map);
    	message.setContent(content);
    	return ProtocolConverter.unmarshallMsg(message);
    }
    
    // 添加一个活动
    private String genAddActivityMessage(){
    	WhereMessage message = genBasicMessage("activity", "13910766842", "server");
    	Content content = new Content();
    	content.setType("add_activity");
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("content", "this is a 00000 Activity content , you can save target here.");
    	map.put("title", "Second Activity");
    	String str = "1391076" + RandomStringUtils.randomNumeric(4);
    	map.put("owner", str);
    	content.setData(map);
    	message.setContent(content);
    	return ProtocolConverter.unmarshallMsg(message);
    }
    
    // 查找一个活动
    private String genGetActivityMessage(){
    	WhereMessage message = genBasicMessage("activity", "13910766842", "server");
    	Content content = new Content();
    	content.setType("get_activity");
    	HashMap<String,Object> map = new HashMap<String,Object>();
//    	map.put("aid", "604853");
//    	map.put("owner", "13910766840");
    	map.put("title", "00000 Activity");
    	content.setData(map);
    	message.setContent(content);
    	return ProtocolConverter.unmarshallMsg(message);
    }
    
 // 查询一个活动
    private String genFindActivityMessage(){
    	WhereMessage message = genBasicMessage("activity", "13910766842", "server");
    	Content content = new Content();
    	content.setType("find_activity");
    	HashMap<String,Object> map = new HashMap<String,Object>();
//    	map.put("title", "First");
    	map.put("title", "00000");
    	content.setData(map);
    	message.setContent(content);
    	return ProtocolConverter.unmarshallMsg(message);
    }
    
    // 获取活动列表
    private String genListActivitiesMessage(){
    	WhereMessage message = genBasicMessage("activity", "13910766842", "server");
    	Content content = new Content();
    	content.setType("list_activities");
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	content.setData(map);
    	message.setContent(content);
    	return ProtocolConverter.unmarshallMsg(message);
    }
    
 // 获取活动列表
    private String genJoinActivitiesMessage(){
    	WhereMessage message = genBasicMessage("activity", "13910766800", "server");
    	Content content = new Content();
    	content.setType("join_activity");
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("aid", "605756");
    	map.put("mobile", "13910766800");
    	map.put("nickname", "moumou");
    	content.setData(map);
    	message.setContent(content);
    	return ProtocolConverter.unmarshallMsg(message);
    }
    
    // 聊天信息
    private String genChatMessage(){
    	WhereMessage message = genBasicMessage("chat", "13910766842", "604853");
    	Content content = new Content();
    	content.setType("msg");
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("msg_type", "text");
    	map.put("body", "aaaaaaaaaaaaaaa");
    	content.setData(map);
    	message.setContent(content);
    	return ProtocolConverter.unmarshallMsg(message);
    }
    
    public static void main(String[] args) throws Exception {
        URI uri;
        if (args.length > 0) {
            uri = new URI(args[0]);
        } else {
            uri = new URI("ws://localhost:10009/websocket");
        }
        WebSocketClient client  = new WebSocketClient(uri);
        client.run();
//        client.genLoginMessage();
        
    }
}
