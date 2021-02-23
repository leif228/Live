package com.littlegreens.netty.client.extra;

import android.util.Log;

public class AtProtocol {

    public static final String HEARD = "AT@";

    private String oid = "";//

    private String priority = "";//
    private String businessNum = "";//
    private String port = "";//
    private String command = "";//
    private String para = "";//
    private String txt = "";//

    public static final String END = "#*";

    public static AtProtocol doAtTask(String tx) throws Exception {
        try {
            Log.v(WjDecoderHandler.TAG, "接收到的at为:" + tx);
            if (tx == null || "".equals(tx))
                throw new Exception("传输的数据错误！数据为：" + tx);

            AtProtocol atProtocol = new AtProtocol();

            int headpoint = tx.indexOf(HEARD);//找出帧头
            int endpoint = tx.indexOf(END);//找出帧尾

            if (headpoint != -1 && endpoint != -1) {
                //AT@Nchn0L0a002020091100011000000000E00200010500A001000100010027chn0L0a00202009110001100000000011110102#*
                int index = 0;
                String strhead = tx.substring(index, HEARD.length());//取出帧头
                index += strhead.length();
                String flag = tx.substring(index, index + 1);
                index += flag.length();
                String oid = tx.substring(index, index + 39);
                Log.v(WjDecoderHandler.TAG, "解码后的oid为：" + oid);
                atProtocol.setOid(oid);
                index += oid.length();
                String pri = tx.substring(index, index + 4);
                atProtocol.setPriority(pri);
                index += pri.length();
                String buss = tx.substring(index, index + 4);
                Log.v(WjDecoderHandler.TAG, "解码后的buss为：" + buss);
                atProtocol.setBusinessNum(buss);
                index += buss.length();
                String port = tx.substring(index, index + 4);
                Log.v(WjDecoderHandler.TAG, "解码后的port为：" + port);
                atProtocol.setPort(port);
                index += port.length();
                String cmd = tx.substring(index, index + 4);
                Log.v(WjDecoderHandler.TAG, "解码后的cmd为：" + cmd);
                atProtocol.setCommand(cmd);
                index += cmd.length();

                String tempEnd = tx.substring(index);

                if(END.equals(tempEnd)){
                    String strend = tx.substring(index, index + END.length());//取出帧尾
                }else{
                    String paramLen = tx.substring(index, index + 4);
                    Log.v(WjDecoderHandler.TAG, "解码后的paramLen为：" + paramLen);
                    int len = HexStringToInt(paramLen);
                    Log.v(WjDecoderHandler.TAG, "len：" + len);
                    index += paramLen.length();
                    String param = tx.substring(index, index + len);
                    Log.v(WjDecoderHandler.TAG, "解码后的param为：" + param);
                    atProtocol.setPara(param);
                    index += param.length();

                    String strend = tx.substring(index, index + END.length());//取出帧尾
                }

                return atProtocol;
            } else {
                throw new Exception("传输的数据格式错误！数据为：" + tx);
            }
        } catch (Exception e) {
            throw new Exception("AT数据解析出错了！" + e.getMessage());
        }
    }

    private static int HexStringToInt(String HexString) {

        int inJTFingerLockAddress = Integer.valueOf(HexString, 16);

        return inJTFingerLockAddress;
    }


    public static String IntToHexStringLimit4(int num) {

        String hexString = Integer.toHexString(num);
        switch (hexString.length()) {
            case 1:
                hexString = "000" + hexString;
                break;
            case 2:
                hexString = "00" + hexString;
                break;
            case 3:
                hexString = "0" + hexString;
                break;
            case 4:
                hexString = hexString;
                break;
            default:
                hexString = null;
                break;
        }

        return hexString;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getBusinessNum() {
        return businessNum;
    }

    public void setBusinessNum(String businessNum) {
        this.businessNum = businessNum;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public static void main(String[] args) {
        try {
            doAtTask("AT@Nchn0L0a002020091100011000000000E00200010500A001000100010027chn0L0a00202009110001100000000011110102#*");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
