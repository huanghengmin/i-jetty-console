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
public class CertificateServlet extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        String command = request.getParameter("command");
        String msg = null;
        String json = null;

        if(command!=null&&"find".equals(command)) {
            PrintWriter writer = response.getWriter();
            int totalCount =0;
            String sb = jsonResult(request.getRealPath("/xml/certificate.xml"));
            totalCount = totalCount+1;
            StringBuilder result = new StringBuilder("{totalCount:"+totalCount+",root:[");
            result.append(sb);
            result.append("]}");
            writer.write(result.toString());
            writer.flush();
            writer.close();
        }
        //检测证书服务器
        else if(command!=null&&"checkCertificate".equals(command)){
            PrintWriter writer = response.getWriter();
            String ip = request.getParameter("ip");
            String port = request.getParameter("port");
            if(ip!=null&&port!=null){
                boolean flag = checkCertificate(ip,Integer.parseInt(port));
                if(flag) {
                    msg = "操作:检测证书服务器,操作结果:成功,服务器IP:"+ip+"服务器端口:"+port;
                    LogUtils.write(request.getRealPath("/system.log"),msg,"INFO");
                    json = "{success:true,msg:'"+msg+"'}";
                }else{
                    msg = "操作:检测证书服务器,操作结果:失败,服务器IP:"+ip+"服务器端口:"+port;
                    LogUtils.write(request.getRealPath("/system.log"),msg,"ERROR");
                    json = "{success:false,msg:'"+msg+"'}";
                }
            }else {
                msg = "检测证书服务器操作,操作结果:失败,服务器IP:"+ip+"服务器端口:"+port;
                LogUtils.write(request.getRealPath("/system.log"),msg,"ERROR");
                json = "{success:false,msg:'"+msg+"'}";
            }
            writer.write(json);
            writer.flush();
            writer.close();
        }
        //保存证书服务器
        else if(command!=null&&"saveCertificate".equals(command)){
            PrintWriter writer = response.getWriter();
            String ip = request.getParameter("ip");
            String port = request.getParameter("port");
            if(ip!=null&&port!=null){

                File file = new File(request.getRealPath("/xml"));
                if(!file.exists()){
                    file.mkdirs();
                }
                boolean flag = update_url(request.getRealPath("/xml/certificate.xml"), ip,port);
                if(flag) {
                    msg = "操作:保存证书服务器,操作结果:成功,服务器IP:"+ip+"服务器端口:"+port;
                    LogUtils.write(request.getRealPath("/system.log"),msg,"INFO");
                    json = "{success:true,msg:'"+msg+"'}";
                }else{
                    msg = "操作:保存证书服务器,操作结果:失败,服务器IP:"+ip+"服务器端口:"+port;
                    LogUtils.write(request.getRealPath("/system.log"),msg,"ERROR");
                    json = "{success:false,msg:'"+msg+"'}";
                }
            }else {
                msg = "操作:保存证书服务器,操作结果:失败,服务器IP:"+ip+"服务器端口:"+port;
                LogUtils.write(request.getRealPath("/system.log"),msg,"ERROR");
                json = "{success:false,msg:'"+msg+"'}";
            }
            writer.write(json);
            writer.flush();
            writer.close();
        }
        //申请
        else if(command!=null&&"requestCertificate".equals(command)){
            String cn = request.getParameter("cn");
            String idCard = request.getParameter("idCard");
            String province = request.getParameter("province");
            String city = request.getParameter("city");
            String organization = request.getParameter("organization");
            String institution = request.getParameter("institution");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String email = request.getParameter("email");
            msg = "申请失败";
            json = "{success:false,msg:'"+msg+"'}";
            String url = null;
            if(cn!=null&&idCard!=null&&province!=null&&city!=null&&organization!=null&&institution!=null&&phone!=null&&address!=null&&email!=null){
                Dom4jXMLUtils dom4jXMLUtils = new Dom4jXMLUtils();
                File f = new File(request.getRealPath("/xml/certificate.xml"));
                if(f.exists()) {
                    Document document = dom4jXMLUtils.getDocument(f);
                    if (document != null) {
                        Element element = document.getRootElement();
                        if (element != null) {
                            Element e_url = element.element("url");
                            if (e_url != null) {
                                Element e_ip = e_url.element("ip");
                                Element e_port = e_url.element("port");
                                if(e_ip!=null&&e_port!=null){
                                    String e_ip_text = e_ip.getText();
                                    String e_port_text = e_port.getText();
                                    if(e_ip_text!=null&&e_port_text!=null)
                                    url = "http://" + e_ip_text + ":" + e_port_text + "/ExternalUser";
                                }
                            }
                        }
                    }
                }
                if(url!=null){
                    json = reqCertificate(url,cn,idCard,province,city,organization,institution,phone,address,email);
                    if(json.contains("true")){
                        File file = new File(request.getRealPath("/xml"));
                        if(!file.exists()){
                            file.mkdirs();
                        }
                        boolean flag = save(request.getRealPath("/xml/certificate.xml"),cn,idCard,province,city,organization,institution,phone,address,email,"1");
                        if(flag){
                            msg = "操作:保存证书配置信息,操作结果:成功,信息:"+msg;
                            LogUtils.write(request.getRealPath("/system.log"),msg,"INFO");
                            PrintWriter writer = response.getWriter();
                            writer.write(json);
                            writer.flush();
                            writer.close();
                        }else {
                            msg = "操作:保存证书配置信息,操作结果:失败";
                            LogUtils.write(request.getRealPath("/system.log"),msg,"ERROR");
                            json = "{success:false,msg:'"+msg+"'}";
                        }
                    }
                }else {
                    msg = "操作:保存证书配置信息,操作结果:失败,原因:未配置证书服务器地址,请配置后申请证书";
                    LogUtils.write(request.getRealPath("/system.log"),msg,"ERROR");
                    json = "{success:false,msg:'"+msg+"'}";
                }
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
                writer.close();
            }
        }
        //重新申请
        else if(command!=null&&"restoreCertificate".equals(command)){
            boolean flag =  update_status(request.getRealPath("/xml/certificate.xml"),"0");
            if(flag){
                msg = "操作:重新申请请求,操作结果:成功";
                LogUtils.write(request.getRealPath("/system.log"),msg,"INFO");
                json = "{success:true,msg:'"+msg+"'}";
            }else {
                msg = "操作:重新申请请求,操作结果:失败";
                LogUtils.write(request.getRealPath("/system.log"),msg,"ERROR");
                json = "{success:false,msg:'"+msg+"'}";
            }
            PrintWriter writer = response.getWriter();
            writer.write(json);
            writer.flush();
            writer.close();
        }
        //下载证书
        else if(command!=null&&"downCertificate".equals(command)){
            String cn = request.getParameter("cn");
            String idCard = request.getParameter("idCard");
            String province = request.getParameter("province");
            String city = request.getParameter("city");
            String organization = request.getParameter("organization");
            String institution = request.getParameter("institution");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String email = request.getParameter("email");
            msg = "申请失败";
            json = "{success:false,msg:'"+msg+"'}";
            String url = null;
            if(cn!=null&&idCard!=null&&province!=null&&city!=null&&organization!=null&&institution!=null&&phone!=null&&address!=null&&email!=null){
                Dom4jXMLUtils dom4jXMLUtils = new Dom4jXMLUtils();
                File f = new File(request.getRealPath("/xml/certificate.xml"));
                if(f.exists()) {
                    Document document = dom4jXMLUtils.getDocument(f);
                    if (document != null) {
                        Element element = document.getRootElement();
                        if (element != null) {
                            Element e_url = element.element("url");
                            if (e_url != null) {
                                Element e_ip = e_url.element("ip");
                                Element e_port = e_url.element("port");
                                if(e_ip!=null&&e_port!=null){
                                    String e_ip_text = e_ip.getText();
                                    String e_port_text = e_port.getText();
                                    if(e_ip_text!=null&&e_port_text!=null)
                                        url = "http://" + e_ip_text + ":" + e_port_text + "/ExternalUser";
                                }
                            }
                        }
                    }
                }
                if(url!=null){
                    final String filePathCert = request.getRealPath("/client.crt");
                    final String filePathKey = request.getRealPath("/client.key");
                    final String typeCommandCert = "0x000003";
                    final String typeCommandKey = "0x000002";
                    downCertificate(url,cn,idCard,province,city,organization,institution,phone,address,email,typeCommandCert,filePathCert);
                    downCertificate(url,cn,idCard,province,city,organization,institution,phone,address,email,typeCommandKey,filePathKey);
                    File key = new File(filePathKey);
                    File cert = new File(filePathCert);
                    if(key.exists()&&key.length()>0&&cert.exists()&&cert.length()>0){

                        msg = "操作:下载证书,操作结果:成功";
                        LogUtils.write(request.getRealPath("/system.log"),msg,"INFO");
                        json = "{success:true,msg:'"+msg+"'}";
                    }else {
                        msg = "操作:下载证书,操作结果:失败";
                        LogUtils.write(request.getRealPath("/system.log"),msg,"ERROR");
                        json = "{success:false,msg:'"+msg+"'}";
                    }
                }else {
                    msg = "未配置证书服务器地址,请配置后申请证书";
                    msg = "操作:下载证书,操作结果:失败,信息:"+msg;
                    LogUtils.write(request.getRealPath("/system.log"),msg,"ERROR");
                    json = "{success:false,msg:'"+msg+"'}";
                }
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
     * 校验证书发布地址是否可用
     * @return
     */
    private boolean checkCertificate(String ip, int port) {
        boolean result = false;
        final String command = "0x000004";
        String request_url = "http://" + ip + ":" + port + "/ExternalUser";
        String[][] params = new String[][]{{"command", command}};
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(2 * 1000 * 60);
        client.getHttpConnectionManager().getParams().setSoTimeout(2 * 1000 * 60);
        PostMethod post = new PostMethod(request_url);
        post.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 2 * 1000 * 60);
        post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        for (String[] param : params) {
            post.addParameter(param[0], param[1]);
        }
        int statusCode = 0;
        try {
            statusCode = client.executeMethod(post);
        } catch (IOException e) {
            return false;
        }
        if (statusCode == 200) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    /**
     * 申请证书
     *
     * @return
     */
    private String reqCertificate(String url,String cn, String idCard, String province, String city, String organization, String institution, String phone, String address, String email) {
        String result = null;
        final String command = "0x000001";
        String[][] params = new String[][]{
                {"cn", cn},
                {"idCard", idCard},
                {"province", province},
                {"city", city},
                {"organization", organization},
                {"institution", institution},
                {"phone", phone},
                {"address", address},
                {"userEmail", email},
                {"command", command}};

        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(2 * 1000 * 60);
        client.getHttpConnectionManager().getParams().setSoTimeout(2 * 1000 * 60);
        PostMethod post = new PostMethod(url);
        post.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 2 * 1000 * 60);
        post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
        for (String[] param : params) {
            post.addParameter(param[0], param[1]);
        }
        int statusCode = 0;
        try {
            statusCode = client.executeMethod(post);
            if (statusCode == 200) {
                result = post.getResponseBodyAsString();
            }
        } catch (IOException e1) {
            return result;
        }
        return result;
    }

    /**
     * 下载证书
     * @return
     */
    private boolean downCertificate(String url, String cn, String idCard, String province, String city, String organization, String institution, String phone, String address, String email, String typeCommand, String filePath) {
        boolean flag = false;
        String[][] params = new String[][]{
                {"cn", cn},
                {"idCard", idCard},
                {"province", province},
                {"city", city},
                {"organization", organization},
                {"institution", institution},
                {"phone", phone},
                {"address", address},
                {"userEmail", email},
                {"command", typeCommand}};
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams() .setConnectionTimeout(2 * 1000 * 60);
        client.getHttpConnectionManager().getParams().setSoTimeout(2 * 1000 * 60);
        PostMethod post = new PostMethod(url);
        post.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 2 * 1000 * 60);
        post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
        for (String[] param : params) {
            post.addParameter(param[0], param[1]);
        }
        int statusCode = 0;
        try {
            statusCode = client.executeMethod(post);
            if (statusCode == 200) {
                InputStream in = post.getResponseBodyAsStream();
                if (in != null) {
                    File file = new File(filePath);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    OutputStream os = new FileOutputStream(new File(filePath));
                    int len = -1;
                    byte[] bt = new byte[2048];
                    while ((len = in.read(bt)) != -1) {
                        os.write(bt, 0, len);
                    }
                    os.flush();
                    os.close();
                    flag = true;
                }
                in.close();
            }
        } catch (Exception e1) {
           return false;
        }
        return flag;
    }

    /**
     * 保存用户信息
     *
     * @return
     */
    private boolean save(String filePath,String cn, String idCard,String province, String city, String organization, String institution,String phone, String address, String email,String status) {
        boolean flag = false;
        try {
            Dom4jXMLUtils dom4jXMLUtils = new Dom4jXMLUtils();
            File f = new File(filePath);
            if(f.exists()){
                Document document = dom4jXMLUtils.getDocument(f);
                if(document!=null){
                    Element element = document.getRootElement();
                    if(element!=null){
                        Element e_user = element.element("user");
                        if(e_user!=null){
                            Element e_cn = e_user.element("cn");
                            Element e_idCard = e_user.element("idCard");
                            Element e_province = e_user.element("province");
                            Element e_city = e_user.element("city");
                            Element e_organization = e_user.element("organization");
                            Element e_institution = e_user.element("institution");
                            Element e_phone = e_user.element("phone");
                            Element e_address = e_user.element("address");
                            Element e_email = e_user.element("email");
                            Element e_status = e_user.element("status");
                            if(e_cn!=null)
                                e_cn.setText(cn);
                            else {
                                Element new_e_cn = e_user.addElement("cn");
                                new_e_cn.setText(cn);
                            }

                            if(e_idCard!=null)
                                e_idCard.setText(idCard);
                            else {
                                Element new_e_idCard = e_user.addElement("idCard");
                                new_e_idCard.setText(cn);
                            }

                            if(e_province!=null)
                                e_province.setText(province);
                            else {
                                Element new_e_province = e_user.addElement("province");
                                new_e_province.setText(province);
                            }

                            if(e_city!=null)
                                e_city.setText(city);
                            else {
                                Element new_e_city = e_user.addElement("city");
                                new_e_city.setText(city);
                            }

                            if(e_organization!=null)
                                e_organization.setText(organization);
                            else {
                                Element new_e_organization = e_user.addElement("organization");
                                new_e_organization.setText(organization);
                            }

                            if(e_institution!=null)
                                e_institution.setText(institution);
                            else {
                                Element new_e_institution = e_user.addElement("institution");
                                new_e_institution.setText(institution);
                            }

                            if(e_phone!=null)
                                e_phone.setText(phone);
                            else {
                                Element new_e_phone = e_user.addElement("phone");
                                new_e_phone.setText(phone);
                            }

                            if(e_address!=null)
                                e_address.setText(address);
                            else {
                                Element new_e_address = e_user.addElement("address");
                                new_e_address.setText(address);
                            }

                            if(e_email!=null)
                                e_email.setText(email);
                            else {
                                Element new_e_email = e_user.addElement("email");
                                new_e_email.setText(email);
                            }

                            if(e_status!=null)
                                e_status.setText(status);
                            else {
                                Element new_e_status = e_user.addElement("status");
                                new_e_status.setText(status);
                            }
                            dom4jXMLUtils.writeXML(document,filePath);
                        }else {
                            Element new_e_url = element.addElement("user");
                            Element e_cn = new_e_url.addElement("cn");
                            Element e_idCard = new_e_url.addElement("idCard");
                            Element e_province = new_e_url.addElement("province");
                            Element e_city = new_e_url.addElement("city");
                            Element e_organization = new_e_url.addElement("organization");
                            Element e_institution = new_e_url.addElement("institution");
                            Element e_phone = new_e_url.addElement("phone");
                            Element e_address = new_e_url.addElement("address");
                            Element e_email = new_e_url.addElement("email");
                            Element e_status = new_e_url.addElement("status");
                            e_cn.setText(cn);
                            e_idCard.setText(idCard);
                            e_province.setText(province);
                            e_city.setText(city);
                            e_organization.setText(organization);
                            e_institution.setText(institution);
                            e_phone.setText(phone);
                            e_address.setText(address);
                            e_email.setText(email);
                            e_status.setText(status);
                            dom4jXMLUtils.writeXML(document,filePath);
                        }
                    }else {
                        Element root = document.addElement("certificate");
                        Element user_element = root.addElement("user");
                        Element e_cn = user_element.addElement("cn");
                        Element e_idCard = user_element.addElement("idCard");
                        Element e_province = user_element.addElement("province");
                        Element e_city = user_element.addElement("city");
                        Element e_organization = user_element.addElement("organization");
                        Element e_institution = user_element.addElement("institution");
                        Element e_phone = user_element.addElement("phone");
                        Element e_address = user_element.addElement("address");
                        Element e_email = user_element.addElement("email");
                        Element e_status = user_element.addElement("status");
                        e_cn.setText(cn);
                        e_idCard.setText(idCard);
                        e_province.setText(province);
                        e_city.setText(city);
                        e_organization.setText(organization);
                        e_institution.setText(institution);
                        e_phone.setText(phone);
                        e_address.setText(address);
                        e_email.setText(email);
                        e_status.setText(status);
                        dom4jXMLUtils.writeXML(document,filePath);
                    }
                }else {
                    Document doc = dom4jXMLUtils.getNewDocument();
                    Element root = doc.addElement("certificate");
                    Element e_user =root.addElement("user");
                    Element e_cn = e_user.addElement("cn");
                    Element e_idCard = e_user.addElement("idCard");
                    Element e_province = e_user.addElement("province");
                    Element e_city = e_user.addElement("city");
                    Element e_organization = e_user.addElement("organization");
                    Element e_institution = e_user.addElement("institution");
                    Element e_phone = e_user.addElement("phone");
                    Element e_address = e_user.addElement("address");
                    Element e_email = e_user.addElement("email");
                    Element e_status = e_user.addElement("status");
                    e_cn.setText(cn);
                    e_idCard.setText(idCard);
                    e_province.setText(province);
                    e_city.setText(city);
                    e_organization.setText(organization);
                    e_institution.setText(institution);
                    e_phone.setText(phone);
                    e_address.setText(address);
                    e_email.setText(email);
                    e_status.setText(status);
                    dom4jXMLUtils.writeXML(doc,filePath);
                }
            }else {
                Document doc = dom4jXMLUtils.getNewDocument();
                Element root = doc.addElement("certificate");
                Element e_user =root.addElement("user");
                Element e_cn = e_user.addElement("cn");
                Element e_idCard = e_user.addElement("idCard");
                Element e_province = e_user.addElement("province");
                Element e_city = e_user.addElement("city");
                Element e_organization = e_user.addElement("organization");
                Element e_institution = e_user.addElement("institution");
                Element e_phone = e_user.addElement("phone");
                Element e_address = e_user.addElement("address");
                Element e_email = e_user.addElement("email");
                Element e_status = e_user.addElement("status");
                e_cn.setText(cn);
                e_idCard.setText(idCard);
                e_province.setText(province);
                e_city.setText(city);
                e_organization.setText(organization);
                e_institution.setText(institution);
                e_phone.setText(phone);
                e_address.setText(address);
                e_email.setText(email);
                e_status.setText(status);
                dom4jXMLUtils.writeXML(doc,filePath);
            }
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     *
     * @param filePath
     * @param ip
     * @param port
     * @return
     */
    private boolean update_url(String filePath, String ip,String port) {
        boolean flag = false;
        try {
            Dom4jXMLUtils dom4jXMLUtils = new Dom4jXMLUtils();
            File f = new File(filePath);
            if(f.exists()){
                Document document = dom4jXMLUtils.getDocument(f);
                if(document!=null){
                    Element element = document.getRootElement();
                    if(element!=null){
                       Element e_url = element.element("url");
                       if(e_url!=null){
                           Element e_ip = e_url.element("ip");
                           Element e_port = e_url.element("port");
                           if(e_ip!=null) {
                               e_ip.setText(ip);
                           }else {
                               Element new_ip = e_url.addElement("ip");
                               new_ip.setText(ip);
                           }
                           if(e_port!=null) {
                               e_port.setText(port);
                           }else {
                               Element new_port = e_url.addElement("port");
                               new_port.setText(port);
                           }
                           dom4jXMLUtils.writeXML(document,filePath);
                       }else {
                          Element new_e_url = element.addElement("url");
                           Element new_ip = new_e_url.addElement("ip");
                           Element new_port = new_e_url.addElement("port");
                           new_ip.setText(ip);
                           new_port.setText(port);
                          dom4jXMLUtils.writeXML(document,filePath);
                       }
                    }else {
                        Element root = document.addElement("certificate");
                        Element url_element = root.addElement("url");
                        Element new_ip = url_element.addElement("ip");
                        Element new_port = url_element.addElement("port");
                        new_ip.setText(ip);
                        new_port.setText(port);
                        dom4jXMLUtils.writeXML(document,filePath);
                    }
                }else {
                    Document doc = dom4jXMLUtils.getNewDocument();
                    Element root = doc.addElement("certificate");
                    Element e_url =root.addElement("url");
                    Element new_ip = e_url.addElement("ip");
                    Element new_port = e_url.addElement("port");
                    new_ip.setText(ip);
                    new_port.setText(port);
                    dom4jXMLUtils.writeXML(doc,filePath);
                }
            }else {
                Document doc = dom4jXMLUtils.getNewDocument();
                Element root = doc.addElement("certificate");
                Element e_url =root.addElement("url");
                Element new_ip = e_url.addElement("ip");
                Element new_port = e_url.addElement("port");
                new_ip.setText(ip);
                new_port.setText(port);
                dom4jXMLUtils.writeXML(doc,filePath);
            }
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     *
     * @param filePath
     * @param status
     * @return
     */
    private boolean update_status(String filePath,String status) {
        boolean flag = false;
        try {
            Dom4jXMLUtils dom4jXMLUtils = new Dom4jXMLUtils();
            File f = new File(filePath);
            if(f.exists()){
                Document document = dom4jXMLUtils.getDocument(f);
                if(document!=null){
                    Element element = document.getRootElement();
                    if(element!=null){
                        Element e_user = element.element("user");
                        if(e_user!=null){
                            Element e_status = e_user.element("status");
                            e_status.setText(status);
                            dom4jXMLUtils.writeXML(document,filePath);
                            flag = true;
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }



    private String jsonResult(String path) {
        String cn = "";
        String idCard = "";
        String province = "";
        String city = "";
        String organization = "";
        String institution = "";
        String phone = "";
        String address = "";
        String email = "";
        String status = "";
        String ip = "";
        String port = "";
        try {
            Dom4jXMLUtils dom4jXMLUtils = new Dom4jXMLUtils();
            File file  = new File(path);
            Document document = dom4jXMLUtils.getDocument(file);
            if(document!=null){
                Element root = document.getRootElement();
                if(root!=null){
                    Element e_user = root.element("user");
                    if(e_user!=null){
                        Element e_cn = e_user.element("cn");
                        Element e_idCard = e_user.element("idCard");
                        Element e_province = e_user.element("province");
                        Element e_city = e_user.element("city");
                        Element e_organization = e_user.element("organization");
                        Element e_institution = e_user.element("institution");
                        Element e_phone = e_user.element("phone");
                        Element e_address = e_user.element("address");
                        Element e_email = e_user.element("email");
                        Element e_status = e_user.element("status");
                        if(e_cn!=null){
                            String t = e_cn.getText();
                            if(t!=null){
                                cn = t;
                            }
                        }

                        if(e_idCard!=null){
                            String t = e_idCard.getText();
                            if(t!=null){
                                idCard = t;
                            }
                        }

                        if(e_province!=null){
                            String t = e_province.getText();
                            if(t!=null){
                                province = t;
                            }
                        }

                        if(e_city!=null){
                            String t = e_city.getText();
                            if(t!=null){
                                city = t;
                            }
                        }

                        if(e_organization!=null){
                            String t = e_organization.getText();
                            if(t!=null){
                                organization = t;
                            }
                        }

                        if(e_institution!=null){
                            String t = e_institution.getText();
                            if(t!=null){
                                institution = t;
                            }
                        }

                        if(e_phone!=null){
                            String t = e_phone.getText();
                            if(t!=null){
                                phone = t;
                            }
                        }

                        if(e_address!=null){
                            String t = e_address.getText();
                            if(t!=null){
                                address = t;
                            }
                        }

                        if(e_email!=null){
                            String t = e_email.getText();
                            if(t!=null){
                                email = t;
                            }
                        }

                        if(e_status!=null){
                            String t = e_status.getText();
                            if(t!=null){
                                status = t;
                            }
                        }
                    }
                    Element e_url = root.element("url");
                    if(e_url!=null){
                        Element e_ip = e_url.element("ip");
                        Element e_port = e_url.element("port");
                        if(e_ip!=null){
                            String t = e_ip.getText();
                            if(t!=null){
                                ip = t;
                            }
                        }

                        if(e_port!=null){
                            String t = e_port.getText();
                            if(t!=null){
                                port = t;
                            }
                        }
                    }
                }
            }
        }catch (Exception e){

        }
        StringBuilder sb =  new StringBuilder();
        sb.append("{");
        sb.append("ip:'"+ ip+"',");
        sb.append("port:'"+ port+"',");
        sb.append("cn:'"+cn+"',");
        sb.append("idCard:'"+ idCard+"',");
        sb.append("province:'"+ province+"',");
        sb.append("city:'"+ city+"',");
        sb.append("organization:'"+ organization+"',");
        sb.append("institution:'"+ institution+"',");
        sb.append("phone:'"+ phone+"',");
        sb.append("address:'"+ address+"',");
        sb.append("email:'"+ email+"',");
        sb.append("status:'"+ status+"'");
        sb.append("}");
        return sb.toString();
    }

}
