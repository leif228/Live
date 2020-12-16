package com.wj.work.utils;

public interface WebViewJavaScriptFunction {

	public void toTcp(String ip,String port, String fzwno);
	public void toNetInfo(String fzwno, String userName, String preip,String preport,String loginfzwno,String owerip,String owerport,String owerfzwno,String passWord);
	public void deviceComp(String name,String path);
	public void saveDevice();
	public void authOver(String path);
	public String getMobileFzwno();
//	public String getEventNo();
	public String toSure();
	public String sendChatMsg(String eventNo,String msg);
	public void toGenEvent();
	public void toLightOn();
	public void toLightOff();
	public void toSearchNet();


}
