package com.littlegreens.netty.client.extra;

import android.util.Log;

import com.littlegreens.netty.client.NettyTcpClient;

import com.alibaba.fastjson.JSONObject;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;


public class WjDecoderHandler extends ByteToMessageDecoder {
    private String TAG = "ly";
    //最小的数据长度：开头标准位1字节
    private static int MIN_DATA_LEN = 21;
    //数据解码协议的开始标志
    private static String PROTOCOL_HEADER = "$TCUB&";

    private static String FORMAT_TX = "TX";
    private static String FORMAT_JS = "JS";
    private static String FORMAT_AT = "AT";
    NettyTcpClient nettyTcpClient;

    public WjDecoderHandler(NettyTcpClient nettyTcpClient) {
        this.nettyTcpClient = nettyTcpClient;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Log.v(TAG,"DecoderHandler  收到数据长度：" + in.readableBytes());
        int totalLen = in.readableBytes();
        if (in.readableBytes() >= MIN_DATA_LEN) {
            Log.v(TAG,"开始解码数据……");
            WjProtocol wjProtocol = new WjProtocol();
            //标记读操作的指针
            in.markReaderIndex();
            byte[] headerbyte = new byte[6];//##6
            in.readBytes(headerbyte);
            String headerStr = new String(headerbyte);
            wjProtocol.setHeader(headerStr);

            if (PROTOCOL_HEADER.equals(headerStr)) {
                Log.v(TAG,"数据开头格式正确");
                //读取字节数据的长度
                short lenShort = in.readShort();//##2
                wjProtocol.setLen(lenShort);
                int len = lenShort;
                int subHeaderLen = len - 8;
                //数据可读长度必须要大于len，因为结尾还有一字节的解释标志位
                int rr = in.readableBytes();
                if (subHeaderLen >= in.readableBytes()) {
                    Log.v(TAG,String.format("数据长度不够，数据协议len长度为：%1$d,数据包实际可读内容为：%2$d正在等待处理拆包……", len, in.readableBytes()));
                    in.resetReaderIndex();
                    /*
                     **结束解码，这种情况说明数据没有到齐，在父类ByteToMessageDecoder的callDecode中会对out和in进行判断
                     * 如果in里面还有可读内容即in.isReadable为true,cumulation中的内容会进行保留，，直到下一次数据到来，将两帧的数据合并起来，再解码。
                     * 以此解决拆包问题
                     */
                    return;
                }
                char verChar = in.readChar();//##1
                wjProtocol.setVer(verChar);
                char encryptChar = in.readChar();//##1
                wjProtocol.setEncrypt(encryptChar);
                short platShort = in.readShort();//##2
                wjProtocol.setPlat(platShort);
                short maincmdShort = in.readShort();//##2
                wjProtocol.setMaincmd(maincmdShort);
                short subcmdShort = in.readShort();//##2
                wjProtocol.setSubcmd(subcmdShort);

                byte[] formatbyte = new byte[2];//##2
                in.readBytes(formatbyte);
                String format = new String(formatbyte);
                wjProtocol.setFormat(format);

                short backShort = in.readShort();//##2
                wjProtocol.setBack(backShort);

                int dataLen = len - MIN_DATA_LEN;
                if (dataLen > 0) {
                    byte[] data = new byte[dataLen];
                    in.readBytes(data);//读取核心的数据##n
                    wjProtocol.setUserdata(data);
                }

                char checkSumChar = in.readChar();//##1
                wjProtocol.setCheckSum(checkSumChar);

                doProtocol(ctx, wjProtocol);
            } else {
                Log.v(TAG,"开头不对，可能不是期待的客服端发送的数，将自动略过这一个字节");
                return;
            }
        } else {
            Log.v(TAG,"数据长度不符合要求，期待最小长度是：" + MIN_DATA_LEN + " 字节");
            return;
        }

    }

    private void doProtocol(ChannelHandlerContext ctx, WjProtocol wjProtocol) {
        JSONObject objParam = null;
        String tx = "";

        if (FORMAT_TX.equals(wjProtocol.getFormat())) {
            if(wjProtocol.getUserdata() != null){
                String dataStr = new String(wjProtocol.getUserdata());
                tx = dataStr;
            }
        } else if (FORMAT_JS.equals(wjProtocol.getFormat())) {
            if(wjProtocol.getUserdata() != null){
                String jsonStr = new String(wjProtocol.getUserdata());
                Log.v(TAG,jsonStr);
                objParam = JSONObject.parseObject(jsonStr);
            }
        } else if (FORMAT_AT.equals(wjProtocol.getFormat())) {
            if(wjProtocol.getUserdata() != null){
                tx = new String(wjProtocol.getUserdata());
            }
        }

        //======业务处理======
//        if(wjProtocol.getMaincmd() == 0 && wjProtocol.getSubcmd() == 1){//终端→服务 登录
//            this.nettyLogin(ctx,objParam);
//        }
        if (wjProtocol.getMaincmd() == 0 && wjProtocol.getSubcmd() == 0) {//终端→服务 心跳
            this.nettyIdle(ctx, tx);
        }
        if (wjProtocol.getMaincmd() == 12 && wjProtocol.getSubcmd() == 0) {//手机←网关 搜索返回
            this.nettyNetSearchBack(ctx, tx);
        }
    }

    private void nettyIdle(ChannelHandlerContext ctx, String tx) {
        Log.v(TAG,"心跳:" + tx);
        if ("ping".equalsIgnoreCase(tx))
            sendIdleData(ctx, "pong");
    }

    private void nettyNetSearchBack(ChannelHandlerContext ctx, String tx) {
        Log.v(TAG,"nettyNetSearchBack:" + tx);
        nettyTcpClient.nettyNetSearchBack();
    }

    /**
     * 报错 处理事件
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {

        Log.v(TAG,"DecoderHandler# # 连接  Netty 出错..."+ctx.channel().remoteAddress());
//        cause.printStackTrace();
        //关闭连接
//        closeConnection(ctx);
    }

    /**
     * 心跳机制  用户事件触发
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;

            //检测 是否 这段时间没有和服务器联系
            if (e.state() == IdleState.ALL_IDLE) {
                //检测心跳
//                checkIdle(ctx);
                sendIdleData(ctx, "ping");
                Log.v(TAG,String.format("DecoderHandler# # client userEventTriggered... : %s", ctx.channel().remoteAddress().toString()));
            }
        }

        super.userEventTriggered(ctx, evt);
    }

    private void sendIdleData(ChannelHandlerContext ctx, String pipo) {
        WjProtocol wjProtocol = new WjProtocol();
        wjProtocol.setPlat(Short.parseShort("20"));
        wjProtocol.setMaincmd(Short.parseShort("0"));
        wjProtocol.setSubcmd(Short.parseShort("0"));
        wjProtocol.setFormat("TX");
        wjProtocol.setBack(Short.parseShort("0"));

//        LoginTask loginTask = new LoginTask();
//        Properties properties= FileUtils.readFile("E:\\config.properties");
//        if(properties != null)
//            loginTask.setOid(properties.getProperty("fzwno"));
//        else
//            loginTask.setOid("88888888");

//        byte [] objectBytes= ByteUtils.InstanceObjectMapper().writeValueAsBytes(loginTask);

        String jsonStr = pipo;
        Log.v(TAG,jsonStr);
        byte[] objectBytes = jsonStr.getBytes();

        int len = 21 + objectBytes.length;
        wjProtocol.setLen((short) len);
        wjProtocol.setUserdata(objectBytes);

        ctx.write(wjProtocol);

        ctx.flush();
    }


    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
        Log.v(TAG,"DecoderHandler# 客户端 channelWritabilityChanged ... :" + ctx.channel());
    }

    /**
     * 读取完毕客户端发送过来的数据之后的操作
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        Log.v(TAG,"DecoderHandler# 客户端接收数据完毕.." + ctx.channel());
    }

    /**
     * 客户端 失去连接
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        Log.v(TAG,String.format("DecoderHandler# # connet out... : %s", ctx.channel().remoteAddress()));
        nettyTcpClient.attemptReConnect();
//        TcpClient.doConnect();
//        Channel incoming = ctx.channel();
//        if (incoming.hasAttr(ChannelManager.deviceInfoVoAttr)) {
//            ChannelManager.deviceChannels.remove(incoming.attr(ChannelManager.deviceInfoVoAttr).get().getUniqueNo());
//        }

//        DeviceSession session = ctx.channel().attr(KEY).getAndSet(null);
//
//        // 移除  session 并删除 该客户端
//        SessionManager.getSingleton().removeClient(session, true);
//
//        if (session.getDeviceID() != null) {
//            //  producer.onData(new Request(new RootMessage(MessageType.LOGOUT, null, null), session));
//        }

    }
}
