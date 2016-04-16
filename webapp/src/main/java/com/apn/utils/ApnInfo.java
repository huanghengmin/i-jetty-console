package com.apn.utils;

/**
 * Selection of few interesting columns from APN table
 */
public class ApnInfo {
    private long id;
    private String apn;
    // APN(default、mms、supl、dun、hipri接入点类型的区别)
    private String type;
    private String name;
    private String numeric;
    private String mcc;
    private String mnc;
    private String proxy;
    private String port;
    private String mmsproxy;
    private String mmsport;
    private String user;
    private String password;
    private String server;
    private String mmsc;
//    private String authtype;
    private String current;

    public ApnInfo() {
    }

    public long getId() {
        return id;
    }

    public String getApn() {
        return apn;
    }

    public String getType() {
        return type;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setApn(String apn) {
        this.apn = apn;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumeric() {
        return numeric;
    }

    public void setNumeric(String numeric) {
        this.numeric = numeric;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getMmsproxy() {
        return mmsproxy;
    }

    public void setMmsproxy(String mmsproxy) {
        this.mmsproxy = mmsproxy;
    }

    public String getMmsport() {
        return mmsport;
    }

    public void setMmsport(String mmsport) {
        this.mmsport = mmsport;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getMmsc() {
        return mmsc;
    }

    public void setMmsc(String mmsc) {
        this.mmsc = mmsc;
    }

//    public String getAuthtype() {
//        return authtype;
//    }
//
//    public void setAuthtype(String authtype) {
//        this.authtype = authtype;
//    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }
}