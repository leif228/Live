package com.littlegreens.netty.client;

import android.os.SystemClock;
import android.util.Log;

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
    private EventLoopGroup group;
    private NettyClientListener listener;
    private Channel channel;

    //最大重连次数
//    private int MAX_CONNECT_TIMES = Integer.MAX_VALUE;
//    private int reconnectNum = MAX_CONNECT_TIMES;
////    private boolean isNeedReconnect = true;
////    private boolean isConnecting = false;// 是否正在连接
////    private boolean isConnected = false;// 是否建立连接
    private Builder mBuilder;

    private NettyTcpClient(Builder builder) {
        mBuilder = builder;
    }

//    private WebSocketClientHandler.ChannelHandlerListener handlerListener =
//            new WebSocketClientHandler.ChannelHandlerListener() {
//                @Override
//                public void onChannelInactive() {
//                    Log.v(TAG, "onChannelInactive");
//                    isConnected = false;
//                    attemptReConnect();
//                }
//
//                @Override
//                public void receiveMsg(String msg) {
//                    if (listener != null) listener.onMessageResponseClient(msg, mBuilder.mIndex);
//                }
//            };

    private void connectServer() {

//        if (isConnecting) return;
        Log.v("ly", "准备连接到Sever");
        synchronized (NettyTcpClient.this) {
            URI uri;
            try {
                uri = new URI(System.getProperty("url", mBuilder.URL));
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return;
            }
            String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
            final String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
            final int port;
            if (uri.getPort() == -1) {
                if ("ws".equalsIgnoreCase(scheme)) {
                    port = 80;
                } else if ("wss".equalsIgnoreCase(scheme)) {
                    port = 443;
                } else {
                    port = -1;
                }
            } else {
                port = uri.getPort();
            }

            if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
                System.err.println("Only WS(S) is supported.");
                return;
            }

            final boolean ssl = "wss".equalsIgnoreCase(scheme);
            final SslContext sslCtx;
            if (ssl) {
                try {
                    sslCtx = SslContextBuilder.forClient()
                            .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
                } catch (SSLException e) {
                    e.printStackTrace();
                    return;
                }
            } else {
                sslCtx = null;
            }

            group = new NioEventLoopGroup();
//            final WebSocketClientHandler handler =
//                    new WebSocketClientHandler(
//                            WebSocketClientHandshakerFactory.newHandshaker(
//                                    uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()))
//                            .setHeartBeatData(mBuilder.heartBeatData)
//                            .setHeartBeatInterval(mBuilder.heartBeatInterval)
//                            .setSendHeartBeat(true)
//                            .setListener(handlerListener);
//            if (!isConnected) {
                Bootstrap bootstrap;
//                isConnecting = true;
                bootstrap = new Bootstrap().group(group)
                        .option(ChannelOption.TCP_NODELAY, true)// 屏蔽Nagle算法试图
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) {

                                ChannelPipeline p = ch.pipeline();
//                                if (mBuilder.isSendHeartBeat) {
//                                    p.addLast("ping", new IdleStateHandler(0, mBuilder.heartBeatInterval,
//                                            0, TimeUnit.SECONDS));//5s未发送数据，回调userEventTriggered
//                                }
//
//                                if (sslCtx != null) {
//                                    p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
//                                }
//
//                                p.addLast(
//                                        new HttpClientCodec(),
//                                        new HttpObjectAggregator(8192),
//                                        WebSocketClientCompressionHandler.INSTANCE,
//                                        handler);
                                p.addLast(new IdleStateHandler(0, 0, 30));
                                p.addLast(new WjEncoderHandler());
                                p.addLast(new WjDecoderHandler(new TaskHandler(NettyTcpClient.this),NettyTcpClient.this));//解码器，接收消息时候用
                            }
                        });
                // ---------------------------
                try {
//                    channel = bootstrap.connect(host, port)
                    channel = bootstrap.connect(mBuilder.host, mBuilder.port)
                            .addListener(new ChannelFutureListener() {
                                @Override
                                public void operationComplete(ChannelFuture cf) throws Exception {
                                    if (cf.isSuccess()) {
                                        Log.v(TAG, "连接server成功");
//                                        reconnectNum = MAX_CONNECT_TIMES;
//                                        isConnected = true;
                                        channel = cf.channel();
                                        listener.connectSuccess(mBuilder.host,mBuilder.mIndex);
                                    } else {
                                        Log.e(TAG, "连接server失败");
                                        Log.v(TAG, "每隔5s重连....");
                                        cf.channel().eventLoop().schedule(new Runnable() {

                                            @Override
                                            public void run() {
                                                reconnect();
                                            }
                                        }, 5, TimeUnit.SECONDS);
//                                        isConnected = false;
                                    }
//                                    isConnecting = false;
                                }
                            })
                            .sync().channel();
//                    handler.handshakeFuture().sync();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.v(TAG, "连接server报错...."+ e.getMessage());
//                    isConnected = false;
                    listener.onClientStatusConnectChanged(ConnectState.STATUS_CONNECT_ERROR, mBuilder.mIndex);
                    if (channel != null && channel.isOpen()) {
                        channel.close();
                    }
                    group.shutdownGracefully();
//                    reconnect();
                }
            }
//        }
    }

//    private boolean isClosed = false; // 是否已经关闭

    /**
     * 将会重新创建通道
     */
    public void connect() {
//        isConnecting = false;
//        isConnected = false;
//        isClosed = false;
        Thread clientThread = new Thread("client-Netty") {
            @Override
            public void run() {
                super.run();
//                isNeedReconnect = true;
//                reconnectNum = mBuilder.maxConnectTimes;
                connectServer();
            }
        };
        clientThread.start();
    }

    /**
     * 主动断开连接
     * 断开之后将不能重连
     */
    public void positiveDisconnect() {
        Log.v(TAG, "function -- positiveDisconnect");
//        if (isClosed) return;
//        isNeedReconnect = false;
//        isConnected = false;
//        isClosed = true;
        Log.v(TAG, "isClosed 置 true");

        if (null != channel && channel.isOpen()) {
            channel.close();
        }

        if (group != null && !group.isShutdown()) {
            group.shutdownGracefully();
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
//        if (isClosed) return;
//        reconnectNum = mBuilder.maxConnectTimes;
//        isNeedReconnect = true;
        reconnect();
    }

    // 执行重连
    public void reconnect() {
//        Log.v(TAG, "reconnect in");
//        if (isNeedReconnect && reconnectNum > 0 && !isConnected) {
//            reconnectNum--;
//            SystemClock.sleep(mBuilder.reconnectIntervalTime);
//            if (isNeedReconnect && reconnectNum > 0 && !isConnected) {
                Log.v(TAG, "重新连接server");
                connectServer();
//            }
//        }
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
//        boolean flag = channel != null && isConnected;
//        if (!flag) {
//            Log.e(TAG, "连接断开的--");
//            return;
//        }
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
//        boolean flag = channel != null && isConnected;
//        if (!flag) {
//            Log.e(TAG, "连接断开的--");
//            return;
//        }
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


//    public boolean sendMsgToServer(byte[] data, final MessageStateListener listener) {
//        boolean flag = channel != null && isConnected;
//        if (flag) {
//            ByteBuf buf = Unpooled.copiedBuffer(data);
//            channel.writeAndFlush(buf).addListener(new ChannelFutureListener() {
//                @Override
//                public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                    listener.isSendSuccss(channelFuture.isSuccess());
//                }
//            });
//        }
//        return flag;
//    }

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
