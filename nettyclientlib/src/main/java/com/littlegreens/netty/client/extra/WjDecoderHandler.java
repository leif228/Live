package com.littlegreens.netty.client.extra;

import android.util.Log;

import com.littlegreens.netty.client.NettyTcpClient;

import java.util.Arrays;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;


public class WjDecoderHandler extends ByteToMessageDecoder {
    public static final String TAG = "ly";

    NettyTcpClient nettyTcpClient;
    TaskHandler taskHandler;

    public WjDecoderHandler(TaskHandler taskHandler,NettyTcpClient nettyTcpClient) {
        this.taskHandler = taskHandler;
        this.nettyTcpClient = nettyTcpClient;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Log.v(TAG,"DecoderHandler  收到数据长度：" + in.readableBytes());
        int totalLen = in.readableBytes();
        if (in.readableBytes() >= WjProtocol.MIN_DATA_LEN) {
            Log.v(TAG,"开始解码数据……");
            WjProtocol wjProtocol = new WjProtocol();
            //标记读操作的指针
            in.markReaderIndex();
            byte[] headerbyte = new byte[6];//##6
            in.readBytes(headerbyte);
            String headerStr = new String(headerbyte);
            wjProtocol.setHeader(headerStr);

            if (WjProtocol.PROTOCOL_HEADER.equals(headerStr)) {
                Log.v(TAG,"数据开头格式正确");
                //读取字节数据的长度
//                short lenShort = in.readShort();//##2
                byte[] lenShortbyte = new byte[2];//##2
                in.readBytes(lenShortbyte);
                wjProtocol.setLen(lenShortbyte);

                int len = wjProtocol.byte2shortSmall(lenShortbyte);
                Log.v(TAG,"数据解码的长度：" + len);
                int subHeaderLen = len - 8;
                //数据可读长度必须要大于len，因为结尾还有一字节的解释标志位
                int rr = in.readableBytes();
                if (subHeaderLen > rr) {
                    Log.v(TAG,String.format("数据长度不够，数据协议len长度为：%1$d,数据包实际可读内容为：%2$d正在等待处理拆包……", len, rr));
                    in.resetReaderIndex();
                    /*
                     **结束解码，这种情况说明数据没有到齐，在父类ByteToMessageDecoder的callDecode中会对out和in进行判断
                     * 如果in里面还有可读内容即in.isReadable为true,cumulation中的内容会进行保留，，直到下一次数据到来，将两帧的数据合并起来，再解码。
                     * 以此解决拆包问题
                     */
                    return;
                }
                byte verChar = in.readByte();//##1
                wjProtocol.setVer(verChar);
                byte encryptChar = in.readByte();//##1
                wjProtocol.setEncrypt(encryptChar);

//                short platShort = in.readShort();//##2
                byte[] platShortbyte = new byte[2];//##2
                in.readBytes(platShortbyte);
                wjProtocol.setPlat(platShortbyte);

//                short maincmdShort = in.readShort();//##2
                byte[] maincmdShortbyte = new byte[2];//##2
                in.readBytes(maincmdShortbyte);
                wjProtocol.setMaincmd(maincmdShortbyte);

//                short subcmdShort = in.readShort();//##2
                byte[] subcmdShortbyte = new byte[2];//##2
                in.readBytes(subcmdShortbyte);
                wjProtocol.setSubcmd(subcmdShortbyte);

                byte[] formatbyte = new byte[2];//##2
                in.readBytes(formatbyte);
                String format = new String(formatbyte);
                wjProtocol.setFormat(format);

//                short backShort = in.readShort();//##2
                byte[] backShortbyte = new byte[2];//##2
                in.readBytes(backShortbyte);
                wjProtocol.setBack(backShortbyte);

                int dataLen = len - WjProtocol.MIN_DATA_LEN;
                if (dataLen > 0) {
                    byte[] data = new byte[dataLen];
                    in.readBytes(data);//读取核心的数据##n
                    wjProtocol.setUserdata(data);
                }

                byte checkSumChar = in.readByte();//##1
                wjProtocol.setCheckSum(checkSumChar);

                Log.e(TAG, "数组打印："+Arrays.toString(wjProtocol.getCheckSumArray(wjProtocol)));

                boolean check = wjProtocol.checkXOR(wjProtocol.getCheckSumArray(wjProtocol),checkSumChar);
                if(!check){
                    Log.v(TAG,"数据异或校验不对");
                    return;
                }

                taskHandler.doProtocol(ctx, wjProtocol);
            } else {
                Log.v(TAG,"开头不对，可能不是期待的客服端发送的数，将自动略过这一个字节");
//                return;
            }
        } else {
            Log.v(TAG,"数据长度不符合要求，期待最小长度是：" + WjProtocol.MIN_DATA_LEN + " 字节");
            return;
        }

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
            if (e.state() == IdleState.WRITER_IDLE) {
                //检测心跳
//                checkIdle(ctx);
                taskHandler.sendIdleData(ctx);
                Log.v(TAG,String.format("DecoderHandler# # client userEventTriggered... : %s", ctx.channel().remoteAddress().toString()));
            }
        }

        super.userEventTriggered(ctx, evt);
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
