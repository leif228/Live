package com.littlegreens.netty.client.extra;

import java.io.Serializable;

public class NetInfoTask extends BaseTask {
    private String OID;
    private String Name;
    private String Identity;
    private String PhoneNum;
    private String Address;
    private String OwnerAddress;
    private String ServerIP;
    private String ServerPort;
    private String ServerOID;
    private String OwnerServerIP;
    private String OwnerServerPort;
    private String OwnerServerOID;
    private String PassNum;

    public String getOID() {
        return OID;
    }

    public void setOID(String OID) {
        this.OID = OID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIdentity() {
        return Identity;
    }

    public void setIdentity(String identity) {
        Identity = identity;
    }

    public String getPhoneNum() {
        return PhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getOwnerAddress() {
        return OwnerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        OwnerAddress = ownerAddress;
    }

    public String getServerIP() {
        return ServerIP;
    }

    public void setServerIP(String serverIP) {
        ServerIP = serverIP;
    }

    public String getServerPort() {
        return ServerPort;
    }

    public void setServerPort(String serverPort) {
        ServerPort = serverPort;
    }

    public String getServerOID() {
        return ServerOID;
    }

    public void setServerOID(String serverOID) {
        ServerOID = serverOID;
    }

    public String getOwnerServerIP() {
        return OwnerServerIP;
    }

    public void setOwnerServerIP(String ownerServerIP) {
        OwnerServerIP = ownerServerIP;
    }

    public String getOwnerServerPort() {
        return OwnerServerPort;
    }

    public void setOwnerServerPort(String ownerServerPort) {
        OwnerServerPort = ownerServerPort;
    }

    public String getOwnerServerOID() {
        return OwnerServerOID;
    }

    public void setOwnerServerOID(String ownerServerOID) {
        OwnerServerOID = ownerServerOID;
    }

    public String getPassNum() {
        return PassNum;
    }

    public void setPassNum(String passNum) {
        PassNum = passNum;
    }
}
