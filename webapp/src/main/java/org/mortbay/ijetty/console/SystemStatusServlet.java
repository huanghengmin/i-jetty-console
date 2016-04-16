package org.mortbay.ijetty.console;

import android.content.ContentResolver;
import android.content.Context;
import android.telephony.TelephonyManager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Administrator on 14-9-26.
 */
public class SystemStatusServlet extends HttpServlet {
    private ContentResolver resolver;
    private Context androidContext;
    private static TelephonyManager te;

    public void init(ServletConfig config) throws ServletException {
        resolver = (ContentResolver) config.getServletContext() .getAttribute("org.mortbay.ijetty.contentResolver");
        androidContext = (Context) config.getServletContext().getAttribute("org.mortbay.ijetty.context");
        te = (TelephonyManager) androidContext.getSystemService(Context.TELEPHONY_SERVICE);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        String command = request.getParameter("command");
        String msg = null;
        String json = null;
        //状态
         if(command!=null&&"status".equals(command)){
            String wifiStatus = wifi_status();
             msg = sslvpn_status();
            if(wifiStatus!=null)
                json = "{wifi:'"+wifiStatus+"',sslvpn:'"+msg+"'}";
            else
                json = "{wifi:'UNKNOWN',sslvpn:'"+msg+"'}";
             int totalCount =0;
             totalCount = totalCount+1;
             StringBuilder result = new StringBuilder("{totalCount:"+totalCount+",root:[");
             result.append(json);
             result.append("]}");
             writer.write(result.toString());
             writer.flush();
             writer.close();
        }
        //获取连接wifi列表
        else if(command!=null&&"getWifiList".equals(command)){
            String wifiList = getWifiList();
            if(wifiList!=null)
                json = wifiList;
            else
                json = "";
            writer.write(json);
            writer.flush();
            writer.close();

        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * 查找连接WIFI用户
     * @return
     */
    public String getWifiList() {
        String json = "";
        ArrayList<String> list = WifiState.getConnectedIP();
        json += "{totalCount:" + list.size() + ",rows:[";
        String ip;
        String mac = "";
        for (int i = 0; i < list.size(); i++) {
            ip = list.get(i);
            mac = WifiState.getMacFromFile(ip);
            if ("IP".equalsIgnoreCase(ip)) {
                continue;
            }
            json += "{ip:'" + ip + "',mac:'" + mac + "'},";
        }
        json = json.substring(0, json.indexOf(",", json.length() - 1));
        json += "]}";
        return json;
    }

    /**
     * 检测网络状态
     */
    public String wifi_status(){
        int i = te.getNetworkType();
        String status = null;
        int j = WifiState.getNetworkClass(i);
        if (j == WifiState.NETWORK_CLASS_2_G) {
            status = "2G";
        } else if (j == WifiState.NETWORK_CLASS_3_G) {
            status = "3G";
        } else if (j == WifiState.NETWORK_CLASS_4_G) {
            status = "4G";
        } else if(j==WifiState.NETWORK_CLASS_UNKNOWN){
            status = "UNKNOWN";
        }
        return status;
    }

    /**
     * SSLVPN连接状态
     */
    public String sslvpn_status(){
        String status = null;
        final String host ="127.0.0.1";
        final int port = 7505;
        SocketClient client = new SocketClient(host,port,1000);
        status = client.sendMessage("status");
        return status;
    }
}