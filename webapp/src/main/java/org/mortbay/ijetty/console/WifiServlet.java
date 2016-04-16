package org.mortbay.ijetty.console;

import android.content.ContentResolver;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 14-9-26.
 */
public class WifiServlet extends HttpServlet {
    private ContentResolver resolver;
    private Context androidContext;
    private static WifiManager wifiManager;

    public void init(ServletConfig config) throws ServletException {
        resolver = (ContentResolver) config.getServletContext().getAttribute("org.mortbay.ijetty.contentResolver");
        androidContext = (Context) config.getServletContext().getAttribute("org.mortbay.ijetty.context");
        wifiManager = (WifiManager) androidContext.getSystemService(Context.WIFI_SERVICE);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        String command = request.getParameter("command");
        String msg = null;
        String json = null;
        if(command!=null&&"secret".equals(command)){
            json = findStore();
            writer.write(json);
            writer.flush();
            writer.close();
        }
        //开启wifi
        else if(command!=null&&"startWifi".equals(command)){
            String ssid = request.getParameter("ssid");
            String secret = request.getParameter("secret");
            String pwd = null;
            if(secret!=null&&"1".equals(secret)){
                 pwd = request.getParameter("pwd");
            }
            if(ssid!=null) {
                Boolean b = false;
                try {
                    b = setWifiApEnabled(true, ssid, pwd);
                    if (b) {
                        saveStatus(request.getRealPath("/xml/wifi.xml"),"on");
                        msg = "操作:WLAN热点开启,操作结果:成功";
                        LogUtils.write(request.getRealPath("/system.log"),msg,"INFO");
                        writer.write("{success:true,msg:'"+msg+"'}");
                        writer.flush();
                        writer.close();
                    } else {
                        msg = "操作:WLAN热点开启,操作结果:失败";
                        LogUtils.write(request.getRealPath("/system.log"),msg,"ERROR");
                        writer.write("{success:false,msg:'"+msg+"'}");
                        writer.flush();
                        writer.close();
                    }
                } catch (Exception e) {
                    msg = "操作:WLAN热点开启,操作结果:失败";
                    LogUtils.write(request.getRealPath("/system.log"),msg,"ERROR");
                    writer.write("{success:false,msg:'"+msg+"'}");
                    writer.flush();
                    writer.close();
                }
            }else {
                msg = "SSID未输入";
                msg = "操作:WLAN热点开启,操作结果:失败,原因:"+msg;
                LogUtils.write(request.getRealPath("/system.log"),msg,"ERROR");
                writer.write("{success:false,msg:'"+msg+"'}");
                writer.flush();
                writer.close();
            }

        }
        else if(command!=null&&"stopWifi".equals(command)){
            String ssid = request.getParameter("ssid");
            String secret = request.getParameter("secret");
            String pwd = null;
            if(secret!=null&&"1".equals(secret)){
                pwd = request.getParameter("pwd");
            }
            if(ssid!=null) {
                Boolean b = false;
                try {
                    b = setWifiApEnabled(false, ssid, pwd);
                    if (b) {
                        saveStatus(request.getRealPath("/xml/wifi.xml"),"off");
                        msg = "操作:WLAN热点关闭,操作结果:成功";
                        LogUtils.write(request.getRealPath("/system.log"),msg,"INFO");
                        writer.write("{success:true,msg:'"+msg+"'}");
                        writer.flush();
                        writer.close();
                    } else {
                        msg = "操作:WLAN热点关闭,操作结果:失败";
                        LogUtils.write(request.getRealPath("/system.log"),msg,"ERROR");
                        writer.write("{success:false,msg:'" + msg + "'}");
                        writer.flush();
                        writer.close();
                    }
                } catch (Exception e) {
                    msg = "操作:WLAN热点关闭,操作结果:失败";
                    LogUtils.write(request.getRealPath("/system.log"),msg,"ERROR");
                    writer.write("{success:false,msg:'"+msg+"'}");
                    writer.flush();
                    writer.close();
                }
            }else {
                msg = "SSID未输入";
                msg = "操作:WLAN热点关闭,操作结果:失败,原因:"+msg;
                LogUtils.write(request.getRealPath("/system.log"),msg,"ERROR");
                writer.write("{success:false,msg:'"+msg+"'}");
                writer.flush();
                writer.close();
            }
        }
        //保存wifi信息
        else if(command!=null&&"saveWifi".equals(command)){
            String ssid = request.getParameter("ssid");
            String secret = request.getParameter("secret");
            String pwd = "";
            if(secret!=null&&"1".equals(secret)){
                pwd = request.getParameter("pwd");
            }
            if(ssid!=null){
                File file = new File(request.getRealPath("/xml"));
                if(!file.exists()){
                    file.mkdirs();
                }
                String path = request.getRealPath("/xml/wifi.xml");
                boolean flag = save(path,ssid,pwd,secret);
                if(flag){
                    msg = "操作:保存WLAN热点信息,操作结果:成功";
                    LogUtils.write(request.getRealPath("/system.log"),msg,"INFO");
                    writer.write("{success:true,msg:'"+msg+"'}");
                    writer.flush();
                    writer.close();
                }else {
                    msg = "操作:保存WLAN热点信息,操作结果:失败";
                    LogUtils.write(request.getRealPath("/system.log"),msg,"ERROR");
                    writer.write("{success:false,msg:'"+msg+"'}");
                    writer.flush();
                    writer.close();
                }
            }
        }
        //查寻wifi配置信息
        else if(command!=null&&"find".equals(command)){
            String path = request.getRealPath("/xml/wifi.xml");
            json =  find(path);
            int totalCount =0;
            totalCount = totalCount+1;
            StringBuilder result = new StringBuilder("{totalCount:"+totalCount+",root:[");
            result.append(json);
            result.append("]}");
            writer.write(result.toString());
            writer.flush();
            writer.close();
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * 开启WIFI热点
     * @param enabled
     * @param ssid
     * @param pwd
     * @return
     * @throws Exception
     */
    public boolean setWifiApEnabled(boolean enabled, String ssid,String pwd) throws Exception {
        if (!enabled) {
            wifiManager.setWifiEnabled(false);
            setWifiApEnabled(true,ssid,pwd);
            return true;
        }else {
            WifiConfiguration apConfig = new WifiConfiguration();
            apConfig.SSID = ssid;
            apConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            apConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            apConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            apConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            apConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            apConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            apConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

            if (pwd != null) {
                if (pwd.length() < 8) {
                    throw new Exception("the length of wifi password must be 8 or longer");
                }
                // 设置wifi热点密码
                apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                apConfig.preSharedKey = pwd;
            } else {
                // 设置wifi热点密码
                apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            }
            Method method = null;
            try {
                method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {// 返回热点打开状态
                return (Boolean) method.invoke(wifiManager, apConfig, enabled);
            } catch (Exception e) {
                return false;
            }
        }
    }


    /**
     * 更新WIFI配置
     * @return
     */
    private boolean save(String path,String ssid,String pwd,String secret){
        boolean flag = false;
        try {
            Dom4jXMLUtils dom4jXMLUtils = new Dom4jXMLUtils();
            File file = new File(path);
            Document document = dom4jXMLUtils.getDocument(file);
            if(document!=null){
                Element root = document.getRootElement();
                if(root!=null){
                    Element e_ssid = root.element("ssid");
                    Element e_pwd = root.element("pwd");
                    Element e_secret = root.element("secret");
                    if(e_ssid!=null){
                        e_ssid.setText(ssid);
                    }else {
                        Element new_ssid = root.addElement("ssid");
                        new_ssid.setText(ssid);
                    }
                    if(e_secret!=null){
                        e_secret.setText(secret);
                    }else {
                        Element new_secret = root.addElement("secret");
                        new_secret.setText(secret);
                    }
                    if(e_pwd!=null){
                        e_pwd.setText(pwd);
                    }else {
                        Element new_pwd = root.addElement("pwd");
                        new_pwd.setText(pwd);
                    }
                }else {
                    Element wifi_root = document.addElement("wifi");
                    Element e_ssid = wifi_root.addElement("ssid");
                    Element e_pwd = wifi_root.addElement("pwd");
                    Element e_secret = wifi_root.addElement("secret");
                    e_ssid.setText(ssid);
                    e_pwd.setText(pwd);
                    e_secret.setText(secret);
                }
                dom4jXMLUtils.writeXML(document,path);
            }else {
                Document new_document = dom4jXMLUtils.getNewDocument();
                Element wifi = new_document.addElement("wifi");
                Element e_ssid = wifi.addElement("ssid");
                Element e_pwd = wifi.addElement("pwd");
                Element e_secret = wifi.addElement("secret");
                e_ssid.setText(ssid);
                e_pwd.setText(pwd);
                e_secret.setText(secret);
                dom4jXMLUtils.writeXML(new_document,path);
            }
            flag = true;
        }catch (Exception e){

        }
        return flag;
    }



    private boolean saveStatus(String path,String status){
        boolean flag = false;
        try {
            Dom4jXMLUtils dom4jXMLUtils = new Dom4jXMLUtils();
            File file = new File(path);
            Document document = dom4jXMLUtils.getDocument(file);
            if(document!=null){
                Element root = document.getRootElement();
                if(root!=null){
                    Element e_status = root.element("status");
                    if(e_status!=null){
                        e_status.setText(status);
                    }else {
                        Element new_status = root.addElement("status");
                        new_status.setText(status);
                    }
                }else {
                    Element wifi_root = document.addElement("wifi");
                    Element e_status = wifi_root.addElement("status");
                    e_status.setText(status);
                }
                dom4jXMLUtils.writeXML(document,path);
            }else {
                Document new_document = dom4jXMLUtils.getNewDocument();
                Element wifi = new_document.addElement("wifi");
                Element e_status = wifi.addElement("status");
                e_status.setText(status);
                dom4jXMLUtils.writeXML(new_document,path);
            }
            flag = true;
        }catch (Exception e){

        }
        return flag;
    }


    private String find(String path){
        String ssid = null;
        String pwd = null;
        String secret = null;
        String status = null;
        Dom4jXMLUtils dom4jXMLUtils = new Dom4jXMLUtils();
        File  file = new File(path);
        Document document = dom4jXMLUtils.getDocument(file);
        if(document!=null){
            Element root = document.getRootElement();
            Element e_ssid = root.element("ssid");
            if(e_ssid!=null)
                ssid = e_ssid.getText();
            else {
                ssid = "";
            }
            Element e_pwd = root.element("pwd");
            if(e_pwd!=null){
                pwd = e_pwd.getText();
            }else {
                pwd = "";
            }

            Element e_secret = root.element("secret");
            if(e_secret!=null){
                secret = e_secret.getText();
            }else {
                secret = "";
            }

            Element e_status = root.element("status");
            if(e_status!=null){
                status = e_status.getText();
            }else {
                status = "off";
            }
        }else {
            ssid = "";
            pwd = "";
            secret = "";
        }
        return "{ssid:'"+ssid+"',pwd:'"+pwd+"',secret:'"+secret+"',status:'"+status+"'}";
    }


    /**
     *
     * @return
     */
    public String findStore(){
        StringBuffer json = new StringBuffer();
        json.append("{'totalCount':").append(2).append(",'rows':[");
        json.append("{id:'").append("0");
        json.append("',name:'").append("开放").append("'},");

        json.append("{id:'").append("1");
        json.append("',name:'").append("WPA2 PSK").append("'},");

        json.deleteCharAt(json.length() - 1);
        json.append("]}");
        return json.toString();
    }
}
