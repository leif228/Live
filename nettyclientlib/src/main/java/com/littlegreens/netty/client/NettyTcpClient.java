package com.littlegreens.netty.client;

import android.os.SystemClock;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.littlegreens.netty.client.extra.TaskHandler;
import com.littlegreens.netty.client.extra.WebSocketClientHandler;
import com.littlegreens.netty.client.extra.WjDecoderHandler;
import com.littlegreens.netty.client.extra.WjEncoderHandler;
import com.littlegreens.netty.client.extra.WjProtocol;
import com.littlegreens.netty.client.listener.MessageStateListener;
import com.littlegreens.netty.client.listener.NettyClientListener;
import com.littlegreens.netty.client.status.ConnectState;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Netty WebSocket 客户端
 * Ly
 */
public class NettyTcpClient {

    private static final String TAG = "ly";
    private NioEventLoopGroup worker = new NioEventLoopGroup();

    Bootstrap bootstrap;
    private NettyClientListener listener;
    private Channel channel;

    //最大重连次数
//    private int MAX_CONNECT_TIMES = Integer.MAX_VALUE;
//    private int reconnectNum = MAX_CONNECT_TIMES;
    private boolean isNeedReconnect = true;
    //    private boolean isConnecting = false;// 是否正在连接
////    private boolean isConnected = false;// 是否建立连接
    private Builder mBuilder;

    private NettyTcpClient(Builder builder) {
        mBuilder = builder;
        init();
    }

    public void init() {
        bootstrap = new Bootstrap();
        bootstrap.group(worker);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
               ChannelPipeline p = ch.pipeline();
                        p.addLast(new IdleStateHandler(0, 0, 30));
                        p.addLast(new WjEncoderHandler());
                        p.addLast(new WjDecoderHandler(new TaskHandler(NettyTcpClient.this), NettyTcpClient.this));//解码器，接收消息时候用
            }
        });
    }

    private void connectServer() {

        Log.v("ly", "准备连接到Sever");

        if (channel != null && channel.isActive()) {
            return;
        }

        //设置不要重连接
        if(!isNeedReconnect)
            return;

        try {
            bootstrap.remoteAddress(mBuilder.host, mBuilder.port);
            ChannelFuture connect = bootstrap.connect();//使用了Future来启动线程

            //实现监听通道连接的方法
            connect.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {

                    if (channelFuture.isSuccess()) {
                        channel = channelFuture.channel();
                        Log.v(TAG, "连接server成功:===" +mBuilder.mIndex);
                        listener.connectSuccess(mBuilder.host, mBuilder.mIndex);
                    } else {
                        Log.e(TAG, "连接server失败:===" + mBuilder.mIndex);
                        listener.onClientStatusConnectChanged(ConnectState.STATUS_CONNECT_ERROR, mBuilder.mIndex);
                        channelFuture.channel().eventLoop().schedule(new Runnable() {

//                            @SneakyThrows
                            @Override
                            public void run() {
                                Log.v(TAG,"每隔5s重连....==="+mBuilder.mIndex);
                                connectServer();
                            }
                        }, 5, TimeUnit.SECONDS);
                    }
                }
            });
        } catch (Exception e) {
            Log.v(TAG, "连接server报错...." + e.getMessage());
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
        }
    }

    /**
     * 将会重新创建通道
     */
    public void connect() {
        Thread clientThread = new Thread("client-Netty") {
            @Override
            public void run() {
                super.run();
                connectServer();
            }
        };
        clientThread.start();
    }

    /**
     * 主动断开连接
     * 断开之后将不能重连
     */
    private void positiveDisconnect() {
        isNeedReconnect = false;

        if (null != channel && channel.isOpen()) {
            channel.close();
        }

        if (worker != null && !worker.isShutdown()) {
            worker.shutdownGracefully();
            Log.v(TAG, "主动 -- 断开连接");
        } else {
            Log.v(TAG, "主动断开连接 -- >  已经是断开的了");
        }
        channel = null;
    }

    /**
     * 因为客户端原因或者服务器原因
     * 掉线之后重新连接服务器
     */
    public void attemptReConnect() {
        isNeedReconnect = true;
        reconnect();
    }

    // 执行重连
    private void reconnect() {
        if (isNeedReconnect) {
            Log.v(TAG, "重新连接server");
            connectServer();
        }
    }

    public void release() {
        positiveDisconnect();
    }

    /**
     * 异步发送
     *
     * @param msg      要发送的数据
     * @param listener 发送结果回调
     * @return 方法执行结果
     */
    public void sendMsgToServer(final String msg, final MessageStateListener listener) {
        Log.v(TAG, "准备发送消息: " + msg);
        WebSocketFrame frame = new TextWebSocketFrame(msg);
        ChannelFuture channelFuture = channel.writeAndFlush(frame);
        if (listener != null) {
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) {
                    Log.v("ly", "发送消息 ：" + msg + " " + (channelFuture.isSuccess() ? "成功！" : "失败"));
                }
            });
        }
    }

    /**
     * 异步发送
     *
     * @param msg      要发送的数据
     * @param listener 发送结果回调
     * @return 方法执行结果
     */
    public void sendMsgToServerF(final WjProtocol msg, final MessageStateListener listener) {
        Log.v(TAG, "准备发送消息: " + msg);
        ChannelFuture channelFuture = channel.writeAndFlush(msg);
        if (listener != null) {
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) {
                    Log.v("ly", "发送消息 ：" + msg + " " + (channelFuture.isSuccess() ? "成功！" : "失败"));
                }
            });
        }
    }

    // 发送消息
    public void sendMsgToServer(String data) {
        sendMsgToServer(data, null);
    }


    //获取TCP连接状态
//    public boolean isConnected() {
//        return isConnected;
//    }

    public void setListener(NettyClientListener listener) {
        this.listener = listener;
    }

    public void nettyNetSearchBack() {
        listener.nettyNetSearchBack();
    }

    public void nettyNetGetDevListOver(String tx, JSONObject objParam) {
        listener.nettyNetGetDevListOver(tx,objParam);
    }

    public void nettyNetFileDownOver(String tx, JSONObject objParam) {
        listener.nettyNetFileDownOver(tx,objParam);
    }

    public void atTask(String tx, JSONObject objParam) {
        listener.atTask(tx,objParam);
    }

    //创建NettyTcpClient
    public static class Builder {
        // 远程链接地址
        private String URL;
        // 是否需要重连
        private boolean isNeedReconnect = true;
        //最大重连次数
        private int maxConnectTimes = 3;
        //服务器地址
        private String host;
        //服务器端口
        private int port;
        // 单次传输数据最大长度
        private int maxPacketLong = 1024;
        //重连间隔
        private long reconnectIntervalTime = 5000;
        //客户端标识，(因为可能存在多个连接)
        private int mIndex;
        //是否发送心跳
        private boolean isSendHeartBeat;
        // 心跳时间间隔
        private long heartBeatInterval = 5;
        //心跳数据，可以是String类型，也可以是byte[].
        private String heartBeatData;

        public Builder() {
            this.maxPacketLong = 1024;
        }

        public Builder setNeedReconnect(boolean needReconnect) {
            isNeedReconnect = needReconnect;
            return this;
        }

        public Builder setURL(String URL) {
            this.URL = URL;
            return this;
        }

        public Builder setMaxPacketLong(int maxPacketLong) {
            this.maxPacketLong = maxPacketLong;
            return this;
        }

        public Builder setMaxReconnectTimes(int reConnectTimes) {
            this.maxConnectTimes = reConnectTimes + 1;
            return this;
        }


        public Builder setReconnectIntervalTime(long reconnectIntervalTime) {
            this.reconnectIntervalTime = reconnectIntervalTime;
            return this;
        }

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setIndex(int mIndex) {
            this.mIndex = mIndex;
            return this;
        }

        public Builder setHeartBeatInterval(long intervalTime) {
            this.heartBeatInterval = intervalTime;
            return this;
        }

        public Builder setSendHeartBeat(boolean isSendheartBeat) {
            this.isSendHeartBeat = isSendheartBeat;
            return this;
        }

        public Builder setHeartBeatData(String heartBeatData) {
            this.heartBeatData = heartBeatData;
            return this;
        }

        public NettyTcpClient build() {
            return new NettyTcpClient(this);
        }
    }
}
