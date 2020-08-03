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

package com.littlegreens.netty.client.extra;

import android.util.Log;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;


public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    private String TAG = "ly";
    private final WebSocketClientHandshaker handShaker;
    private ChannelPromise handshakeFuture;

    public WebSocketClientHandler(WebSocketClientHandshaker handShaker) {
        this.handShaker = handShaker;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        handShaker.handshake(ctx.channel());
        Log.v(TAG, "WebSocket Client connected!");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Log.v(TAG, "WebSocket Client disconnected!");
        if (listener!=null)listener.onChannelInactive();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        if (!handShaker.isHandshakeComplete()) {
            try {
                handShaker.finishHandshake(ch, (FullHttpResponse) msg);
                Log.v(TAG, "WebSocket 连接成功！");
                handshakeFuture.setSuccess();
            } catch (WebSocketHandshakeException e) {
                Log.v(TAG, "WebSocket 连接失败！");
                handshakeFuture.setFailure(e);
            }
            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException(
                    "Unexpected FullHttpResponse (getStatus=" + response.status() +
                            ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            if (listener!=null)listener.receiveMsg(textFrame.text());
//            Log.v(TAG, "WebSocket Client received message: " + textFrame.text());
        } else if (frame instanceof PongWebSocketFrame) {
            Log.v(TAG, "WebSocket Client received pong");
        } else if (frame instanceof CloseWebSocketFrame) {
            Log.v(TAG, "WebSocket Client received closing");
            ch.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }

    private long lastHeatBeatTime=0;

    /**
     * <p>设定IdleStateHandler心跳检测每x秒进行一次读检测，
     * 如果x秒内ChannelRead()方法未被调用则触发一次userEventTrigger()方法 </p>
     *
     * @param ctx ChannelHandlerContext
     * @param evt IdleStateEvent
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (!isSendHeartBeat)return;
        if (evt instanceof IdleStateEvent) {
            if (System.currentTimeMillis()-lastHeatBeatTime>heartBeatInterval-1000){
                MqttFixedHeader pingReqFixedHeader =
                        new MqttFixedHeader(MqttMessageType.PINGREQ, false, MqttQoS.AT_MOST_ONCE, false, 0);
                MqttMessage pingReqMessage = new MqttMessage(pingReqFixedHeader);
                ctx.writeAndFlush(pingReqMessage);
                Log.v("ly", "Send  --  心跳");
                lastHeatBeatTime=System.currentTimeMillis();
            }
//            IdleStateEvent event = (IdleStateEvent) evt;
//            if (event.state() == IdleState.WRITER_IDLE) {   //发送心跳
//////                ctx.channel().writeAndFlush("Heartbeat" + System.getProperty("line.separator"));
////                if (isSendHeartBeat) {
////                    if (heartBeatData == null) {
////                        ctx.channel().writeAndFlush("Heartbeat");
////                    } else {
////                        ctx.channel().writeAndFlush(heartBeatData);
////                        Log.v("ly","heartBeatData");
////                    }
////                } else {
////                    Log.e(TAG, "不发送心跳");
////                }
//            }
        }else{
            super.userEventTriggered(ctx, evt);
        }
    }

    private boolean isSendHeartBeat = true;
    private String heartBeatData = "jxj_beat";
    private long heartBeatInterval=10000;
    private ChannelHandlerListener listener;

    public WebSocketClientHandler setSendHeartBeat(boolean sendHeartBeat) {
        isSendHeartBeat = sendHeartBeat;
        return this;
    }

    public WebSocketClientHandler setHeartBeatData(String heartBeatData) {
        this.heartBeatData = heartBeatData;
        return this;
    }

    public WebSocketClientHandler setHeartBeatInterval(long heartBeatInterval) {
        this.heartBeatInterval = heartBeatInterval;
        return this;
    }

    public WebSocketClientHandler setListener(ChannelHandlerListener listener) {
        this.listener = listener;
        return this;
    }

    public interface ChannelHandlerListener{
        void onChannelInactive();
        void receiveMsg(String msg);
    }
}
