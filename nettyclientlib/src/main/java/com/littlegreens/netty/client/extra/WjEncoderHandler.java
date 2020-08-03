package com.littlegreens.netty.client.extra;

import android.util.Log;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class WjEncoderHandler extends MessageToByteEncoder {
    private String TAG = "ly";


    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        Log.v(TAG, "OutBusinessHandler#encode 服务端 发送数据 ... :" + ctx.channel() + "==>数据：" + msg.toString());
        if (msg instanceof WjProtocol) {
            WjProtocol protocol = (WjProtocol) msg;
            out.writeBytes(protocol.getHeader().getBytes());
            out.writeShort(protocol.getLen());
            out.writeChar(protocol.getVer());
            out.writeChar(protocol.getEncrypt());
            out.writeShort(protocol.getPlat());
            out.writeShort(protocol.getMaincmd());
            out.writeShort(protocol.getSubcmd());
            out.writeBytes(protocol.getFormat().getBytes());
            out.writeShort(protocol.getBack());
            if (protocol.getUserdata() != null)
                out.writeBytes(protocol.getUserdata());
            out.writeChar(protocol.getCheckSum());

            Log.v(TAG, "数据编码成功：" + out.readableBytes());
        } else {
            Log.v(TAG, "不支持的数据协议：" + msg.getClass() + "\t期待的数据协议类是：" + WjProtocol.class);
        }
    }


}
