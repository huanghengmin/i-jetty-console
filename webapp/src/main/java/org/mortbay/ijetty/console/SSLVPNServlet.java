package org.mortbay.ijetty.console;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by Administrator on 14-9-26.
 */
public class SSLVPNServlet extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        String command = request.getParameter("command");
        String msg = null;
        String json = null;
        if (command != null && "find".equals(command)) {
            PrintWriter writer = response.getWriter();
            int totalCount =0;
            String sb = jsonResult(request.getRealPath("/xml/sslvpn.xml"));
            totalCount = totalCount+1;
            StringBuilder result = new StringBuilder("{totalCount:"+totalCount+",root:[");
            result.append(sb);
            result.append("]}");
            writer.write(result.toString());
            writer.flush();
            writer.close();
        }
        //导入VPN配置
        else if (command != null && "importVPN".equals(command)) {
            include();
            msg = "操作:导入SSLVPN配置,操作结果:成功";
            LogUtils.write(request.getRealPath("/system.log"),msg,"INFO");
            json = "{success:true,msg:'"+msg+"'}";
            PrintWriter writer = response.getWriter();
            writer.write(json);
            writer.flush();
            writer.close();
        }
        //开启VPN
        else if (command != null && "startVPN".equals(command)) {
            start();
            msg = "操作:开启SSLVPN连接,操作结果:成功";
            LogUtils.write(request.getRealPath("/system.log"),msg,"INFO");
            json = "{success:true,msg:'"+msg+"'}";
            PrintWriter writer = response.getWriter();
            writer.write(json);
            writer.flush();
            writer.close();
        }
        //停止VPN
        else if (command != null && "stopVPN".equals(command)) {
            close();
            msg = "操作:关闭SSLVPN连接,操作结果:成功";
            LogUtils.write(request.getRealPath("/system.log"),msg,"INFO");
            json = "{success:true,msg:'"+msg+"'}";
            PrintWriter writer = response.getWriter();
            writer.write(json);
            writer.flush();
            writer.close();
        }
        //下载VPN配置文件和保存配置信息
        else if (command != null && "downVPN".equals(command)) {
            msg = "下载SSLVPN证书信息失败";
            json = "{success:true,msg:'"+msg+"'}";

            String sslvpnIp = request.getParameter("SSLVPNIp");
            String strategyPort = request.getParameter("strategyPort");
            String connectPort = request.getParameter("connectPort");

            File ca_file =  new File(request.getRealPath("ca.crt"));
            File ta_file =  new File(request.getRealPath("ta.key"));
            File ovpn_file =  new File(request.getRealPath("client.ovpn"));

            downSSLVPNCA(sslvpnIp,strategyPort,ca_file);
            downSSLVPNStaticKey(sslvpnIp,strategyPort,ta_file);
            downSSLVPNConfig(sslvpnIp,strategyPort,ovpn_file);
            boolean update_flag = false;
            if(ovpn_file.exists()){
                update_flag = update_SSLVPNConfig(sslvpnIp,connectPort,ovpn_file);
            }
            if(update_flag&&ca_file.exists()&&ca_file.length()>0&&ta_file.exists()&&ta_file.length()>0&&ovpn_file.exists()&&ovpn_file.length()>0){
                File file = new File(request.getRealPath("/xml"));
                if(!file.exists()){
                    file.mkdirs();
                }
                boolean save_flag = save(request.getRealPath("/xml/sslvpn.xml"),sslvpnIp,strategyPort,connectPort);
                if(save_flag){
                    msg = "操作:下载SSLVPN证书信息,操作结果:成功";
                    json = "{success:true,msg:'"+msg+"'}";
                    LogUtils.write(request.getRealPath("/system.log"),msg,"INFO");
                }else {
                    msg = "操作:下载SSLVPN证书信息,操作结果:失败";
                    json = "{success:false,msg:'"+msg+"'}";
                    LogUtils.write(request.getRealPath("/system.log"),msg,"ERROR");
                }
            }else {
                msg = "操作:下载SSLVPN证书信息,操作结果:失败";
                json = "{success:false,msg:'"+msg+"'}";
                LogUtils.write(request.getRealPath("/system.log"),msg,"ERROR");
            }
            PrintWriter writer = response.getWriter();
            writer.write(json);
            writer.flush();
            writer.close();
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * 导入VPN配置
     *
     * @return
     */
    private void include() {
        final String host ="127.0.0.1";
        final int port = 7505;
        SocketClient client = new SocketClient(host,port,1000);
        client.sendMessage("importVPN");
    }

    /**
     * 开启SSLVPN
     *
     * @return
     */
    private void start() {
        final String host ="127.0.0.1";
        final int port = 7505;
        SocketClient client = new SocketClient(host,port,1000);
        client.sendMessage("startVPN");
    }

    /**
     * 停止SSLVPN
     *
     * @return
     */
    private void close() {
        final String host ="127.0.0.1";
        final int port = 7505;
        SocketClient client = new SocketClient(host,port,1000);
        client.sendMessage("stopVPN");
    }

    public boolean downSSLVPNConfig(String ip, String port, File file) {
        boolean flag = false;
        try {
            String url = "http://" + ip + ":" + port + "/ClientAction_downAndroidConfig.action";
            InputStream in = getUrlInputStream(url);
            if (in != null) {
                if (!file.exists()) {
                    file.createNewFile();
                }
                OutputStream os = new FileOutputStream(file);

                int len = -1;
                byte[] bt = new byte[2048]; // 可以根据实际情况调整，建议使用1024，即每次读1KB
                while ((len = in.read(bt)) != -1) {
                    os.write(bt, 0, len); // 建议不要直接用os.write(bt)
                }
                os.flush();
                in.close();
                os.close();
            }
            if (file.length() > 0) {
                flag = true;
            }
        } catch (Exception e) {

        }
        return flag;
    }

    public boolean downSSLVPNStaticKey(String ip, String port, File file) {
        boolean flag = false;
        try {
            String url = "http://" + ip + ":" + port + "/ClientAction_downStaticKey.action";
            InputStream in = getUrlInputStream(url);
            if (in != null) {
                if (!file.exists()) {
                    file.createNewFile();
                }
                OutputStream os = new FileOutputStream(file);
                int len = -1;
                byte[] bt = new byte[2048]; // 可以根据实际情况调整，建议使用1024，即每次读1KB
                while ((len = in.read(bt)) != -1) {
                    os.write(bt, 0, len); // 建议不要直接用os.write(bt)
                }
                os.flush();
                in.close();
                os.close();
            }
            if (file.length() > 0) {
                flag = true;
            }
        } catch (Exception e) {

        }
        return flag;
    }

    public Boolean downSSLVPNCA(String ip, String port, File file) {
        boolean result = false;
        try {
            String url = "http://" + ip + ":" + port + "/ClientAction_downCa.action";
            InputStream in = getUrlInputStream(url);
            if (in != null) {
                if (!file.exists()) {
                    file.createNewFile();
                }
                OutputStream os = new FileOutputStream(file);
                int len = -1;
                byte[] bt = new byte[2048]; // 可以根据实际情况调整，建议使用1024，即每次读1KB
                while ((len = in.read(bt)) != -1) {
                    os.write(bt, 0, len); // 建议不要直接用os.write(bt)
                }
                os.flush();
                in.close();
                os.close();
            }
            if (file.length() > 0) {
                result = true;
            }
        } catch (Exception ex) {

        }
        return result;
    }

    public boolean save(String savePath, String ip, String strategyPort, String connectPort) {
        boolean flag = false;
        try {

            Dom4jXMLUtils dom4jXMLUtils = new Dom4jXMLUtils();
            File file  = new File(savePath);
            Document document = dom4jXMLUtils.getDocument(file);
            if(document!=null){
                Element root = document.getRootElement();
                if(root!=null){
                    Element sslvpnIp = root.element("SSLVPNIp");
                    Element e_strategyPort = root.element("strategyPort");
                    Element e_connectPort = root.element("connectPort");
                    if(sslvpnIp!=null){
                        sslvpnIp.setText(ip);
                    }else {
                       Element new_sslvpnIp = root.addElement("SSLVPNIp");
                        new_sslvpnIp.setText(ip);
                    }

                    if(e_strategyPort!=null){
                        e_strategyPort.setText(strategyPort);
                    }else {
                        Element new_strategyPort = root.addElement("strategyPort");
                        new_strategyPort.setText(strategyPort);
                    }

                    if(e_connectPort!=null){
                        e_connectPort.setText(connectPort);
                    }else {
                        Element new_connectPort = root.addElement("connectPort");
                        new_connectPort.setText(connectPort);
                    }
                    dom4jXMLUtils.writeXML(document,savePath);
                }else {
                    Element sslvpn = document.addElement("SSLVPN");
                    Element sslvpnIp = sslvpn.addElement("SSLVPNIp");
                    Element new_strategyPort = sslvpn.addElement("strategyPort");
                    Element new_connectPort = sslvpn.addElement("connectPort");
                    sslvpnIp.setText(ip);
                    new_strategyPort.setText(strategyPort);
                    new_connectPort.setText(connectPort);
                    dom4jXMLUtils.writeXML(document,savePath);
                }
            }else {
                Document new_document = dom4jXMLUtils.getNewDocument();
                Element sslvpn = new_document.addElement("SSLVPN");
                Element sslvpnIp = sslvpn.addElement("SSLVPNIp");
                Element new_strategyPort = sslvpn.addElement("strategyPort");
                Element new_connectPort = sslvpn.addElement("connectPort");
                sslvpnIp.setText(ip);
                new_strategyPort.setText(strategyPort);
                new_connectPort.setText(connectPort);
                dom4jXMLUtils.writeXML(new_document,savePath);
            }
            flag = true;
        }catch (Exception e){

        }
        return flag;
    }

    public boolean update_SSLVPNConfig(String ip, String port, File file) {
        try {
            BufferedReader in_ = new BufferedReader(new FileReader(file));
            String line;
            String ling = "";
            while ((line = in_.readLine()) != null) {
                if (line.contains("remote")) {
                    ling += "remote" + " " + ip + " " + port + "\r\n";
                } else {
                    ling += line + "\r\n";
                }
            }
            BufferedWriter buff = new BufferedWriter(new FileWriter(file));
            buff.write(ling);
            in_.close();
            buff.flush();
            buff.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public InputStream getUrlInputStream(String url) {
        HttpClient client = new HttpClient();
        InputStream in = null;
        client.getHttpConnectionManager().getParams().setConnectionTimeout(2 * 1000 * 60);
        client.getHttpConnectionManager().getParams().setSoTimeout(2 * 1000 * 60);
        PostMethod post = new PostMethod(url);
        post.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 2 * 1000 * 60);
        post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        int statusCode = 0;
        try {
            statusCode = client.executeMethod(post);
            if (statusCode == 200) {
                in = post.getResponseBodyAsStream();
            }
        } catch (Exception e) {
            return null;
        }
        return in;
    }


    private String jsonResult(String path) {
        String SSLVPNIp = "";
        String strategyPort = "";
        String connectPort = "";
        try {
            Dom4jXMLUtils dom4jXMLUtils = new Dom4jXMLUtils();
            File file  = new File(path);
            Document document = dom4jXMLUtils.getDocument(file);
            if(document!=null){
                Element root = document.getRootElement();
                if(root!=null){
                    Element e_SSLVPNIp = root.element("SSLVPNIp");
                    Element e_strategyPort = root.element("strategyPort");
                    Element e_connectPort = root.element("connectPort");
                    if(e_SSLVPNIp!=null){
                        String t = e_SSLVPNIp.getText();
                        if(t!=null){
                            SSLVPNIp = t;
                        }
                    }

                    if(e_strategyPort!=null){
                        String t = e_strategyPort.getText();
                        if(t!=null){
                            strategyPort = t;
                        }
                    }

                    if(e_connectPort!=null){
                        String t = e_connectPort.getText();
                        if(t!=null){
                            connectPort = t;
                        }
                    }

                }
            }
        }catch (Exception e){

        }
        StringBuilder sb =  new StringBuilder();
        sb.append("{");
        sb.append("SSLVPNIp:'"+ SSLVPNIp+"',");
        sb.append("strategyPort:'"+ strategyPort+"',");
        sb.append("connectPort:'"+ connectPort+"'");
        sb.append("}");
        return sb.toString();
    }
}
