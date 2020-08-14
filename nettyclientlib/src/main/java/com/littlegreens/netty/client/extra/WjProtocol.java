package com.littlegreens.netty.client.extra;

import android.util.Log;

import java.nio.ByteOrder;
import java.util.Arrays;

public class WjProtocol {
    //最小的数据长度：开头标准位1字节
    public static final int MIN_DATA_LEN = 21;
    public static final int headerLength = 6;
    public static final int checkLength = 1;
    //数据解码协议的开始标志
    public static final String PROTOCOL_HEADER = "$TCUB&";

    public static final String FORMAT_TX = "TX";
    public static final String FORMAT_JS = "JS";
    public static final String FORMAT_AT = "AT";

    private String header = PROTOCOL_HEADER;//6
    private byte[] len;//2
    private byte ver = 0x01;//1
    private byte encrypt = 0x00;//1
    private byte[] plat;//2
    private byte[] maincmd;//2
    private byte[] subcmd;//2
    private String format;//2
    private byte[] back;//2
    private byte[] userdata;//n
    private byte checkSum;//1

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public byte[] getLen() {
        return len;
    }

    public void setLen(byte[] len) {
        this.len = len;
    }

    public byte getVer() {
        return ver;
    }

    public void setVer(byte ver) {
        this.ver = ver;
    }

    public byte getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(byte encrypt) {
        this.encrypt = encrypt;
    }

    public byte getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(byte checkSum) {
        this.checkSum = checkSum;
    }

    public byte[] getPlat() {
        return plat;
    }

    public void setPlat(byte[] plat) {
        this.plat = plat;
    }

    public byte[] getMaincmd() {
        return maincmd;
    }

    public void setMaincmd(byte[] maincmd) {
        this.maincmd = maincmd;
    }

    public byte[] getSubcmd() {
        return subcmd;
    }

    public void setSubcmd(byte[] subcmd) {
        this.subcmd = subcmd;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public byte[] getBack() {
        return back;
    }

    public void setBack(byte[] back) {
        this.back = back;
    }

    public byte[] getUserdata() {
        return userdata;
    }

    public void setUserdata(byte[] userdata) {
        this.userdata = userdata;
    }

    public short byte2shortBig(byte[] b) {
        short l = 0;
        for (int i = 0; i < 2; i++) {
            l <<= 8; //<<=和我们的 +=是一样的，意思就是 l = l << 8
            l |= (b[i] & 0xff); //和上面也是一样的  l = l | (b[i]&0xff)
        }
        return l;
    }
    public short byte2shortSmall(byte[] b) {
        short l = 0;
        for (int i = 1; i >= 0; i--) {
            l <<= 8; //<<=和我们的 +=是一样的，意思就是 l = l << 8
            l |= (b[i] & 0xff); //和上面也是一样的  l = l | (b[i]&0xff)
        }
        return l;
    }

    public byte[] short2byte(short s) {
        byte[] b = new byte[2];
//        for(int i = 0; i < 2; i++){
//            int offset = 16 - (i+1)*8; //因为byte占4个字节，所以要计算偏移量
//            b[i] = (byte)((s >> offset)&0xff); //把16位分为2个8位进行分别存储
//        }

        b[0] = (byte) (s & 0xFF);
        // int 倒数第二个字节
        b[1] = (byte) ((s & 0xFF00) >> 8);

        return b;
    }

    public boolean checkXOR(byte[] datas, byte checkData) {
        int result = 0;
        for (int i = 0; i < datas.length; i++) {
            result = result ^ byteToInt(datas[i]);
        }
        int data = byteToInt(checkData);
        if (result == data) {
            return true;
        }
        return false;
    }

    private byte getXOR(byte[] datas) {
        int result = 0;
        for (int i = 0; i < datas.length; i++) {
            result = result ^ byteToInt(datas[i]);
            result = result & 0xff;
        }
        return intToByte(result);
    }

    private byte intToByte(int x) {
        return (byte) (x & 0xFF);
    }

    private int byteToInt(byte b) {
        return (int) b;
    }

    public byte[] getCheckSumArray(WjProtocol protocol) {

        int dataLength = 0;
        if (protocol.getUserdata() != null) {
            dataLength = protocol.getUserdata().length;
        }

        int cslength = WjProtocol.MIN_DATA_LEN - WjProtocol.headerLength - WjProtocol.checkLength + dataLength;
        byte[] arr = new byte[cslength];

        int index = 0;
        System.arraycopy(protocol.getLen(), 0, arr, index, protocol.getLen().length);

        index += protocol.getLen().length;
        arr[index] = protocol.getVer();

        index += 1;
        arr[index] = protocol.getEncrypt();

        index += 1;
        System.arraycopy(protocol.getPlat(), 0, arr, index, protocol.getPlat().length);

        index += protocol.getPlat().length;
        System.arraycopy(protocol.getMaincmd(), 0, arr, index, protocol.getMaincmd().length);

        index += protocol.getMaincmd().length;
        System.arraycopy(protocol.getSubcmd(), 0, arr, index, protocol.getSubcmd().length);

        index += protocol.getSubcmd().length;
        System.arraycopy(protocol.getFormat().getBytes(), 0, arr, index, protocol.getFormat().getBytes().length);

        index += protocol.getFormat().getBytes().length;
        System.arraycopy(protocol.getBack(), 0, arr, index, protocol.getBack().length);

        if (protocol.getUserdata() != null) {
            index += protocol.getBack().length;
            System.arraycopy(protocol.getUserdata(), 0, arr, index, protocol.getUserdata().length);
        }

        return arr;
    }

    public byte getCheckSum(WjProtocol protocol) {

        return getXOR(getCheckSumArray(protocol));
    }

    public static void main(String[] args) {
        String header = "$TCUB&";//6
        byte[] bytes = header.getBytes();
        System.out.println(bytes.length);
        System.out.println(new String(bytes));
//        checkBS();
        byte[] arr = new byte[]{0x15, 0x00, 0x01, 0x00, 0x50, 0x00, 0x12, 0x00, 0x00, 0x00, 0x54, 0x58, 0x00, 0x00};

//        System.out.println(getXOR(arr));

    }
}
