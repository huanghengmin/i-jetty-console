package org.mortbay.ijetty.console;

import com.socks5.Socks5;
import org.dom4j.Attribute;
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
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 14-9-26.
 */
public class PortForwardServlet extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        String command = request.getParameter("command");
        String msg = null;
        String json = null;
        //find
        if (command != null && "find".equals(command)) {
            json = find(request.getRealPath("/xml/port.xml"));
            int totalCount = 0;
            totalCount = totalCount + 1;
            StringBuilder result = new StringBuilder("{totalCount:" + totalCount + ",root:[");
            result.append(json);
            result.append("]}");
            PrintWriter writer = response.getWriter();
            writer.write(result.toString());
            writer.flush();
            writer.close();
        }
        //find rule
        else if (command != null && "findRule".equals(command)) {
            String bindIp = request.getParameter("bindIp");
            String bindPort = request.getParameter("bindPort");
            String protocol = request.getParameter("protocol");
            json = findRule(bindIp,bindPort,protocol,request.getRealPath("/xml/port.xml"));
            int totalCount = 0;
            totalCount = totalCount + 1;
            StringBuilder result = new StringBuilder("{totalCount:" + totalCount + ",root:[");
            result.append(json);
            result.append("]}");
            PrintWriter writer = response.getWriter();
            writer.write(result.toString());
            writer.flush();
            writer.close();
        }

        else if (command != null && "addRule".equals(command)) {
            String bindIp = request.getParameter("bindIp");
            String bindPort = request.getParameter("bindPort");
            String accessIp = request.getParameter("accessIp");
            String accessPort = request.getParameter("accessPort");
            String protocol = request.getParameter("protocol");
            String desc = request.getParameter("desc");
            File file = new File(request.getRealPath("/xml"));
            if(!file.exists()){
                file.mkdirs();
            }
            ResultObject resultObject =  add(request.getRealPath("/system.log"),bindIp,bindPort,accessIp,accessPort,protocol,desc,"0",request.getRealPath("/xml/port.xml"));
            if(resultObject.isFlag()){
                msg = resultObject.getMsg();
                LogUtils.write(request.getRealPath("/system.log"), msg, "INFO");
                json = "{success:true,msg:'" + msg + "'}";
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
                writer.close();
            }else {
                msg = resultObject.getMsg();
                LogUtils.write(request.getRealPath("/system.log"), msg, "ERROR");
                json = "{success:false,msg:'" + msg + "'}";
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
                writer.close();
            }
        }


        else if (command != null && "modifyRule".equals(command)) {
            String oldBindIp = request.getParameter("oldBindIp");
            String oldBindPort = request.getParameter("oldBindPort");
            String oldProtocol = request.getParameter("oldProtocol");
            String oldAccessIp = request.getParameter("oldAccessIp");
            String oldAccessPort = request.getParameter("oldAccessPort");
            String oldDesc = request.getParameter("oldDesc");

            String pId = request.getParameter("pId");
            String bindIp = request.getParameter("bindIp");
            String bindPort = request.getParameter("bindPort");
            String accessIp = request.getParameter("accessIp");
            String accessPort = request.getParameter("accessPort");
            String protocol = request.getParameter("protocol");
            String desc = request.getParameter("desc");
            ResultObject resultObject =  modify(request.getRealPath("/system.log"),pId,oldBindIp, oldBindPort,oldProtocol,oldAccessIp,oldAccessPort,oldDesc, bindIp, bindPort, accessIp, accessPort, protocol, desc, request.getRealPath("/xml/port.xml"));
            if(resultObject.isFlag()){
                msg = resultObject.getMsg();
                LogUtils.write(request.getRealPath("/system.log"), msg, "INFO");
                json = "{success:true,msg:'" + msg + "'}";
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
                writer.close();
            }else {
                msg = resultObject.getMsg();
                LogUtils.write(request.getRealPath("/system.log"), msg, "ERROR");
                json = "{success:false,msg:'" + msg + "'}";
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
                writer.close();
            }
        }

        else if (command != null && "delRule".equals(command)) {
            String bindIp = request.getParameter("bindIp");
            String bindPort = request.getParameter("bindPort");
            String protocol = request.getParameter("protocol");
            String pId = request.getParameter("pId");
            ResultObject resultObject =  delete(request.getRealPath("/system.log"),pId,bindIp, bindPort, protocol,request.getRealPath("/xml/port.xml"));
            if(resultObject.isFlag()){
                msg = resultObject.getMsg();
                LogUtils.write(request.getRealPath("/system.log"), msg, "INFO");
                json = "{success:true,msg:'" + msg + "'}";
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
                writer.close();
            }else {
                msg = resultObject.getMsg();
                LogUtils.write(request.getRealPath("/system.log"), msg, "ERROR");
                json = "{success:false,msg:'" + msg + "'}";
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
                writer.close();
            }
        }

        else if (command != null && "enableRule".equals(command)) {
            String bindIp = request.getParameter("bindIp");
            String bindPort = request.getParameter("bindPort");
            String accessIp = request.getParameter("accessIp");
            String accessPort = request.getParameter("accessPort");
            String protocol = request.getParameter("protocol");
            Socks5 socks5 = new Socks5();
            int pid = socks5.pipeAdd(bindIp,bindPort,accessIp,accessPort,protocol);
            if(pid!=-1) {
                ResultObject resultObject = enable(request.getRealPath("/system.log"), bindIp, bindPort, protocol, String.valueOf(pid), request.getRealPath("/xml/port.xml"));
                if (resultObject.isFlag()) {
                    msg = resultObject.getMsg();
                    LogUtils.write(request.getRealPath("/system.log"), msg, "INFO");
                    json = "{success:true,msg:'" + msg + "'}";
                    PrintWriter writer = response.getWriter();
                    writer.write(json);
                    writer.flush();
                    writer.close();
                } else {
                    msg = resultObject.getMsg();
                    LogUtils.write(request.getRealPath("/system.log"), msg, "ERROR");
                    json = "{success:false,msg:'" + msg + "'}";
                    PrintWriter writer = response.getWriter();
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }else {
                msg = "操作:开启端口转发规则,结果:失败,监听IP" + bindIp + ",端口" + bindPort + ",协议:" + protocol+"开启端口转发服务出错";
                LogUtils.write(request.getRealPath("/system.log"), msg, "ERROR");
                json = "{success:false,msg:'" + msg + "'}";
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
                writer.close();
            }
        }

        else if (command != null && "disableRule".equals(command)) {
            String bindIp = request.getParameter("bindIp");
            String bindPort = request.getParameter("bindPort");
            String protocol = request.getParameter("protocol");
            String pId = request.getParameter("pId");
            Socks5 socks5 = new Socks5();
            int result = socks5.pipeDel(Integer.parseInt(pId));
            if(result==-1){
                msg = "操作:更新端口转发规则,结果:失败,监听IP" + bindIp + ",端口" + bindPort + ",协议:" + protocol+"清除失败";
                LogUtils.write(request.getRealPath("/system.log"), msg, "ERROR");
            }else if(result==-2){
                msg = "操作:更新端口转发规则,结果:失败,监听IP" + bindIp + ",端口" + bindPort + ",协议:" + protocol+"进程不存在";
                LogUtils.write(request.getRealPath("/system.log"), msg, "ERROR");
            }
            ResultObject resultObject =  disable(request.getRealPath("/system.log"),bindIp, bindPort,protocol,request.getRealPath("/xml/port.xml"));
            if(resultObject.isFlag()){
                msg = resultObject.getMsg();
                LogUtils.write(request.getRealPath("/system.log"), msg, "INFO");
                json = "{success:true,msg:'" + msg + "'}";
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
                writer.close();
            }else {
                msg = resultObject.getMsg();
                LogUtils.write(request.getRealPath("/system.log"), msg, "ERROR");
                json = "{success:false,msg:'" + msg + "'}";
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
                writer.close();
            }
        }

        //start
        else if (command != null && "start".equals(command)) {
            String serverPort = findServerPort(request.getRealPath("/xml/port.xml"));
            if (serverPort != null) {
                start(Integer.parseInt(serverPort), "");
                msg = "操作:开启端口映射,操作结果:成功";
                save_status(request.getRealPath("/xml/port.xml"), "on");
                LogUtils.write(request.getRealPath("/system.log"), msg, "INFO");
                json = "{success:true,msg:'" + msg + "'}";
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
                writer.close();
            }
        }
        //stop
        else if (command != null && "stop".equals(command)) {
            stop();
            msg = "操作:关闭端口映射,操作结果:成功";
            save_status(request.getRealPath("/xml/port.xml"), "off");
            LogUtils.write(request.getRealPath("/system.log"), msg, "INFO");
            json = "{success:true,msg:'" + msg + "'}";
            PrintWriter writer = response.getWriter();
            writer.write(json);
            writer.flush();
            writer.close();
        }
        //save
        else if (command != null && "save".equals(command)) {
            String serverPort = request.getParameter("serverPort");
            File file = new File(request.getRealPath("/xml"));
            if(!file.exists()){
                file.mkdirs();
            }
            boolean flag = save_server(request.getRealPath("/xml/port.xml"), serverPort);
            if (flag) {
                msg = "操作:保存端口映射配置,操作结果:成功";
                LogUtils.write(request.getRealPath("/system.log"), msg, "INFO");
                json = "{success:true,msg:'" + msg + "'}";
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
                writer.close();
            } else {
                msg = "操作:保存端口映射配置,操作结果:失败";
                LogUtils.write(request.getRealPath("/system.log"), msg, "INFO");
                json = "{success:false,msg:'" + msg + "'}";
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
                writer.close();
            }
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }


    private void start(int serverPort, String accessString) {
        Socks5 socks5 = new Socks5();
        socks5.start(serverPort, accessString);
    }

    private void stop() {
        Socks5 socks5 = new Socks5();
        socks5.stop();
    }

    /**
     * 查找端口条目
     *
     * @return
     */
    private String find(String path) {
        String port = "";
        String status = "off";
        try {
            Dom4jXMLUtils dom4jXMLUtils = new Dom4jXMLUtils();
            File file = new File(path);
            Document document = dom4jXMLUtils.getDocument(file);
            if (document != null) {
                Element root = document.getRootElement();
                if (root != null) {
                    Element e_port = root.element("serverPort");
                    if (e_port != null) {
                        port = e_port.getText();
                        if (port == null) {
                            port = "";
                        }
                    }
                    Element e_status = root.element("status");
                    if (e_status != null) {
                        status = e_status.getText();
                        if (status == null) {
                            status = "off";
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
        return "{serverPort:'" + port + "',status:'" + status + "'}";
    }


    /**
     * @param path
     * @return
     */
    private String findRule(String bindIp,String bindPort,String protocol,String path) {
        StringBuilder sb = new StringBuilder();
        try {
            Dom4jXMLUtils dom4jXMLUtils = new Dom4jXMLUtils();
            File file = new File(path);
            Document document = dom4jXMLUtils.getDocument(file);
            if (document != null) {
                Element root = document.getRootElement();
                if (root != null) {
                    if (bindIp != null &&!"".equals(bindIp)&& bindPort != null &&!"".equals(bindPort)&& protocol != null&&!"".equals(protocol)) {
                        List<Element> composites = document.selectNodes("/portForward/rule[@bindIp='" + bindIp + "'][@bindPort='" + bindPort + "'][@protocol='" + protocol + "']");
                        if (composites != null && composites.size() > 0) {
                            Iterator<Element> iterator = composites.iterator();
                            while (iterator.hasNext()) {
                                Element e = iterator.next();
                                Attribute a_bind_ip = e.attribute("bindIp");
                                Attribute a_bind_port = e.attribute("bindPort");
                                Attribute a_protocol = e.attribute("protocol");
                                Attribute a_status = e.attribute("status");
                                Element e_access_ip = e.element("accessIp");
                                Element e_access_port = e.element("accessPort");
                                Element e_pId = e.element("pId");
                                Element e_desc = e.element("desc");
                                sb.append("{");
                                sb.append("bindIp:'" + a_bind_ip.getValue() + "'").append(",");
                                sb.append("bindPort:'" + a_bind_port.getValue() + "'").append(",");
                                sb.append("accessIp:'" + e_access_ip.getText() + "'").append(",");
                                sb.append("accessPort:'" + e_access_port.getText() + "'").append(",");
                                sb.append("status:'" + a_status.getValue() + "'").append(",");
                                sb.append("protocol:'" + a_protocol.getValue() + "'").append(",");
                                if (e_pId != null)
                                    sb.append("pId:'" + e_pId.getText() + "'").append(",");
                                sb.append("desc:'" + e_desc.getText() + "'");
                                sb.append("}");
                                if (iterator.hasNext()) {
                                    sb.append(",");
                                }
                            }
                        }
                    } else if (bindIp != null&&!"".equals(bindIp) && bindPort != null&&!"".equals(bindPort)) {
                        List<Element> composites = document.selectNodes("/portForward/rule[@bindIp='" + bindIp + "'][@bindPort='" + bindPort + "']");
                        if (composites != null && composites.size() > 0) {
                            Iterator<Element> iterator = composites.iterator();
                            while (iterator.hasNext()) {
                                Element e = iterator.next();
                                Attribute a_bind_ip = e.attribute("bindIp");
                                Attribute a_bind_port = e.attribute("bindPort");
                                Attribute a_protocol = e.attribute("protocol");
                                Attribute a_status = e.attribute("status");
                                Element e_access_ip = e.element("accessIp");
                                Element e_access_port = e.element("accessPort");
                                Element e_pId = e.element("pId");
                                Element e_desc = e.element("desc");
                                sb.append("{");
                                sb.append("bindIp:'" + a_bind_ip.getValue() + "'").append(",");
                                sb.append("bindPort:'" + a_bind_port.getValue() + "'").append(",");
                                sb.append("accessIp:'" + e_access_ip.getText() + "'").append(",");
                                sb.append("accessPort:'" + e_access_port.getText() + "'").append(",");
                                sb.append("status:'" + a_status.getValue() + "'").append(",");
                                sb.append("protocol:'" + a_protocol.getValue() + "'").append(",");
                                if (e_pId != null)
                                    sb.append("pId:'" + e_pId.getText() + "'").append(",");
                                sb.append("desc:'" + e_desc.getText() + "'");
                                sb.append("}");
                                if (iterator.hasNext()) {
                                    sb.append(",");
                                }
                            }
                        }
                    } else if (bindIp != null&&!"".equals(bindIp) && protocol != null&&!"".equals(protocol)) {
                        List<Element> composites = document.selectNodes("/portForward/rule[@bindIp='" + bindIp + "'][@protocol='" + protocol + "']");
                        if (composites != null && composites.size() > 0) {
                            Iterator<Element> iterator = composites.iterator();
                            while (iterator.hasNext()) {
                                Element e = iterator.next();
                                Attribute a_bind_ip = e.attribute("bindIp");
                                Attribute a_bind_port = e.attribute("bindPort");
                                Attribute a_protocol = e.attribute("protocol");
                                Attribute a_status = e.attribute("status");
                                Element e_access_ip = e.element("accessIp");
                                Element e_access_port = e.element("accessPort");
                                Element e_pId = e.element("pId");
                                Element e_desc = e.element("desc");
                                sb.append("{");
                                sb.append("bindIp:'" + a_bind_ip.getValue() + "'").append(",");
                                sb.append("bindPort:'" + a_bind_port.getValue() + "'").append(",");
                                sb.append("accessIp:'" + e_access_ip.getText() + "'").append(",");
                                sb.append("accessPort:'" + e_access_port.getText() + "'").append(",");
                                sb.append("status:'" + a_status.getValue() + "'").append(",");
                                sb.append("protocol:'" + a_protocol.getValue() + "'").append(",");
                                if (e_pId != null)
                                    sb.append("pId:'" + e_pId.getText() + "'").append(",");
                                sb.append("desc:'" + e_desc.getText() + "'");
                                sb.append("}");
                                if (iterator.hasNext()) {
                                    sb.append(",");
                                }
                            }
                        }
                    } else if (bindPort != null&&!"".equals(bindPort) && protocol != null&&!"".equals(protocol)) {
                        List<Element> composites = document.selectNodes("/portForward/rule[@bindIp='" + bindIp + "'][@protocol='" + protocol + "']");
                        if (composites != null && composites.size() > 0) {
                            Iterator<Element> iterator = composites.iterator();
                            while (iterator.hasNext()) {
                                Element e = iterator.next();
                                Attribute a_bind_ip = e.attribute("bindIp");
                                Attribute a_bind_port = e.attribute("bindPort");
                                Attribute a_protocol = e.attribute("protocol");
                                Attribute a_status = e.attribute("status");
                                Element e_access_ip = e.element("accessIp");
                                Element e_access_port = e.element("accessPort");
                                Element e_pId = e.element("pId");
                                Element e_desc = e.element("desc");
                                sb.append("{");
                                sb.append("bindIp:'" + a_bind_ip.getValue() + "'").append(",");
                                sb.append("bindPort:'" + a_bind_port.getValue() + "'").append(",");
                                sb.append("accessIp:'" + e_access_ip.getText() + "'").append(",");
                                sb.append("accessPort:'" + e_access_port.getText() + "'").append(",");
                                sb.append("status:'" + a_status.getValue() + "'").append(",");
                                sb.append("protocol:'" + a_protocol.getValue() + "'").append(",");
                                if (e_pId != null)
                                    sb.append("pId:'" + e_pId.getText() + "'").append(",");
                                sb.append("desc:'" + e_desc.getText() + "'");
                                sb.append("}");
                                if (iterator.hasNext()) {
                                    sb.append(",");
                                }
                            }
                        }
                    } else if (bindIp != null&&!"".equals(bindIp)) {
                        List<Element> composites = document.selectNodes("/portForward/rule[@bindIp='" + bindIp + "']");
                        if (composites != null && composites.size() > 0) {
                            Iterator<Element> iterator = composites.iterator();
                            while (iterator.hasNext()) {
                                Element e = iterator.next();
                                Attribute a_bind_ip = e.attribute("bindIp");
                                Attribute a_bind_port = e.attribute("bindPort");
                                Attribute a_protocol = e.attribute("protocol");
                                Attribute a_status = e.attribute("status");
                                Element e_access_ip = e.element("accessIp");
                                Element e_access_port = e.element("accessPort");
                                Element e_pId = e.element("pId");
                                Element e_desc = e.element("desc");
                                sb.append("{");
                                sb.append("bindIp:'" + a_bind_ip.getValue() + "'").append(",");
                                sb.append("bindPort:'" + a_bind_port.getValue() + "'").append(",");
                                sb.append("accessIp:'" + e_access_ip.getText() + "'").append(",");
                                sb.append("accessPort:'" + e_access_port.getText() + "'").append(",");
                                sb.append("status:'" + a_status.getValue() + "'").append(",");
                                sb.append("protocol:'" + a_protocol.getValue() + "'").append(",");
                                if (e_pId != null)
                                    sb.append("pId:'" + e_pId.getText() + "'").append(",");
                                sb.append("desc:'" + e_desc.getText() + "'");
                                sb.append("}");
                                if (iterator.hasNext()) {
                                    sb.append(",");
                                }
                            }
                        }
                    } else if (bindPort != null&&!"".equals(bindPort)) {
                        List<Element> composites = document.selectNodes("/portForward/rule[@bindPort='" + bindPort + "']");
                        if (composites != null && composites.size() > 0) {
                            Iterator<Element> iterator = composites.iterator();
                            while (iterator.hasNext()) {
                                Element e = iterator.next();
                                Attribute a_bind_ip = e.attribute("bindIp");
                                Attribute a_bind_port = e.attribute("bindPort");
                                Attribute a_protocol = e.attribute("protocol");
                                Attribute a_status = e.attribute("status");
                                Element e_access_ip = e.element("accessIp");
                                Element e_access_port = e.element("accessPort");
                                Element e_pId = e.element("pId");
                                Element e_desc = e.element("desc");
                                sb.append("{");
                                sb.append("bindIp:'" + a_bind_ip.getValue() + "'").append(",");
                                sb.append("bindPort:'" + a_bind_port.getValue() + "'").append(",");
                                sb.append("accessIp:'" + e_access_ip.getText() + "'").append(",");
                                sb.append("accessPort:'" + e_access_port.getText() + "'").append(",");
                                sb.append("status:'" + a_status.getValue() + "'").append(",");
                                sb.append("protocol:'" + a_protocol.getValue() + "'").append(",");
                                if (e_pId != null)
                                    sb.append("pId:'" + e_pId.getText() + "'").append(",");
                                sb.append("desc:'" + e_desc.getText() + "'");
                                sb.append("}");
                                if (iterator.hasNext()) {
                                    sb.append(",");
                                }
                            }
                        }
                    } else if (protocol != null&&!"".equals(protocol)) {
                        List<Element> composites = document.selectNodes("/portForward/rule[@protocol='" + protocol + "']");
                        if (composites != null && composites.size() > 0) {
                            Iterator<Element> iterator = composites.iterator();
                            while (iterator.hasNext()) {
                                Element e = iterator.next();
                                Attribute a_bind_ip = e.attribute("bindIp");
                                Attribute a_bind_port = e.attribute("bindPort");
                                Attribute a_protocol = e.attribute("protocol");
                                Attribute a_status = e.attribute("status");
                                Element e_access_ip = e.element("accessIp");
                                Element e_access_port = e.element("accessPort");
                                Element e_pId = e.element("pId");
                                Element e_desc = e.element("desc");
                                sb.append("{");
                                sb.append("bindIp:'" + a_bind_ip.getValue() + "'").append(",");
                                sb.append("bindPort:'" + a_bind_port.getValue() + "'").append(",");
                                sb.append("accessIp:'" + e_access_ip.getText() + "'").append(",");
                                sb.append("accessPort:'" + e_access_port.getText() + "'").append(",");
                                sb.append("status:'" + a_status.getValue() + "'").append(",");
                                sb.append("protocol:'" + a_protocol.getValue() + "'").append(",");
                                if (e_pId != null)
                                    sb.append("pId:'" + e_pId.getText() + "'").append(",");
                                sb.append("desc:'" + e_desc.getText() + "'");
                                sb.append("}");
                                if (iterator.hasNext()) {
                                    sb.append(",");
                                }
                            }
                        }
                    } else {
                        List<Element> elementList = root.elements("rule");
                        if (elementList != null && elementList.size() > 0) {
                            Iterator<Element> iterator = elementList.iterator();
                            while (iterator.hasNext()) {
                                Element e = iterator.next();
                                Attribute a_bind_ip = e.attribute("bindIp");
                                Attribute a_bind_port = e.attribute("bindPort");
                                Attribute a_protocol = e.attribute("protocol");
                                Attribute a_status = e.attribute("status");
                                Element e_access_ip = e.element("accessIp");
                                Element e_access_port = e.element("accessPort");
                                Element e_pId = e.element("pId");
                                Element e_desc = e.element("desc");
                                sb.append("{");
                                sb.append("bindIp:'" + a_bind_ip.getValue() + "'").append(",");
                                sb.append("bindPort:'" + a_bind_port.getValue() + "'").append(",");
                                sb.append("accessIp:'" + e_access_ip.getText() + "'").append(",");
                                sb.append("accessPort:'" + e_access_port.getText() + "'").append(",");
                                sb.append("status:'" + a_status.getValue() + "'").append(",");
                                sb.append("protocol:'" + a_protocol.getValue() + "'").append(",");
                                if (e_pId != null)
                                    sb.append("pId:'" + e_pId.getText() + "'").append(",");
                                sb.append("desc:'" + e_desc.getText() + "'");
                                sb.append("}");
                                if (iterator.hasNext()) {
                                    sb.append(",");
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String findServerPort(String path) {
        String port = "";
        try {
            Dom4jXMLUtils dom4jXMLUtils = new Dom4jXMLUtils();
            File file = new File(path);
            Document document = dom4jXMLUtils.getDocument(file);
            if (document != null) {
                Element root = document.getRootElement();
                if (root != null) {
                    Element e_port = root.element("serverPort");
                    if (e_port != null) {
                        port = e_port.getText();
                        if (port == null) {
                            port = "";
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
        return port;
    }

    /**
     * 添加端口映射规则
     *
     * @return
     */
    private ResultObject add(String logPath,String bindIp, String bindPort, String accessIp, String accessPort, String protocol, String desc, String status, String path) {
        try {
            Dom4jXMLUtils dom4jXMLUtils = new Dom4jXMLUtils();
            File file = new File(path);
            Document document = dom4jXMLUtils.getDocument(file);
            if (document != null) {
                List<Element> composites = document.selectNodes("/portForward/rule[@bindIp='" + bindIp +"'][@bindPort='"+bindPort+"'][@protocol='"+protocol+"']");
                if (composites != null&&composites.size()>0) {
                    String msg = "操作:新增端口转发规则,结果:失败,原因:监听IP"+bindIp+",端口"+bindPort+",协议:"+protocol+"记录已存在";
                    LogUtils.write(logPath, msg, "ERROR");
                    return new ResultObject(false,msg);
                } else {
                    Element root = document.getRootElement();
                    if (root != null) {
                        Element rule_element = root.addElement("rule");

                        Attribute a_bind_ip = rule_element.attribute("bindIp");
                        if (a_bind_ip != null) {
                            a_bind_ip.setValue(bindIp);
                        } else {
                            rule_element.addAttribute("bindIp", bindIp );
                        }

                        Attribute a_bind_port = rule_element.attribute("bindPort");
                        if (a_bind_port != null) {
                            a_bind_port.setValue(bindPort);
                        } else {
                            rule_element.addAttribute("bindPort", bindPort );
                        }

                        Attribute a_bind_protocol = rule_element.attribute("protocol");
                        if (a_bind_protocol != null) {
                            a_bind_protocol.setValue(protocol);
                        } else {
                            rule_element.addAttribute("protocol", protocol );
                        }

                        Attribute a_bind_status = rule_element.attribute("status");
                        if (a_bind_status != null) {
                            a_bind_status.setValue(status);
                        } else {
                            rule_element.addAttribute("status", status );
                        }

                        Element e_access_ip = rule_element.element("accessIp");
                        if (e_access_ip != null) {
                            e_access_ip.setText(accessIp);
                        } else {
                            Element add_e = rule_element.addElement("accessIp");
                            add_e.setText(accessIp);
                        }

                        Element e_access_port = rule_element.element("accessPort");
                        if (e_access_port != null) {
                            e_access_port.setText(accessPort);
                        } else {
                            Element add_e = rule_element.addElement("accessPort");
                            add_e.setText(accessPort);
                        }
                        Element e_desc = rule_element.element("desc");
                        if (e_desc != null) {
                            e_desc.setText(desc);
                        } else {
                            Element add_e = rule_element.addElement("desc");
                            add_e.setText(desc);
                        }


                    } else {
                        Element portForward = document.addElement("portForward");
                        Element rule_element = portForward.addElement("rule");

                        Attribute a_bind_ip = rule_element.attribute("bindIp");
                        if (a_bind_ip != null) {
                            a_bind_ip.setValue(bindIp);
                        } else {
                            rule_element.addAttribute("bindIp", bindIp );
                        }

                        Attribute a_bind_port = rule_element.attribute("bindPort");
                        if (a_bind_port != null) {
                            a_bind_port.setValue(bindPort);
                        } else {
                            rule_element.addAttribute("bindPort", bindPort );
                        }

                        Attribute a_bind_protocol = rule_element.attribute("protocol");
                        if (a_bind_protocol != null) {
                            a_bind_protocol.setValue(protocol);
                        } else {
                            rule_element.addAttribute("protocol", protocol );
                        }

                        Attribute a_bind_status = rule_element.attribute("status");
                        if (a_bind_status != null) {
                            a_bind_status.setValue(status);
                        } else {
                            rule_element.addAttribute("status", status );
                        }


                        Element e_access_ip = rule_element.element("accessIp");
                        if (e_access_ip != null) {
                            e_access_ip.setText(accessIp);
                        } else {
                            Element add_e = rule_element.addElement("accessIp");
                            add_e.setText(accessIp);
                        }

                        Element e_access_port = rule_element.element("accessPort");
                        if (e_access_port != null) {
                            e_access_port.setText(accessPort);
                        } else {
                            Element add_e = rule_element.addElement("accessPort");
                            add_e.setText(accessPort);
                        }

                        Element e_desc = rule_element.element("desc");
                        if (e_desc != null) {
                            e_desc.setText(desc);
                        } else {
                            Element add_e = rule_element.addElement("desc");
                            add_e.setText(desc);
                        }
                    }
                }
                dom4jXMLUtils.writeXML(document, path);
                String msg = "操作:新增端口转发规则,结果:成功,监听IP"+bindIp+",端口"+bindPort+",协议:"+protocol;
                LogUtils.write(logPath, msg, "INFO");
                return new ResultObject(true,msg);
            } else {
                Document new_document = dom4jXMLUtils.getNewDocument();
                Element portForward = new_document.addElement("portForward");
                Element rule_element = portForward.addElement("rule");


                Attribute a_bind_ip = rule_element.attribute("bindIp");
                if (a_bind_ip != null) {
                    a_bind_ip.setValue(bindIp);
                } else {
                    rule_element.addAttribute("bindIp", bindIp );
                }

                Attribute a_bind_port = rule_element.attribute("bindPort");
                if (a_bind_port != null) {
                    a_bind_port.setValue(bindPort);
                } else {
                    rule_element.addAttribute("bindPort", bindPort );
                }

                Attribute a_bind_protocol = rule_element.attribute("protocol");
                if (a_bind_protocol != null) {
                    a_bind_protocol.setValue(protocol);
                } else {
                    rule_element.addAttribute("protocol", protocol );
                }

                Attribute a_bind_status = rule_element.attribute("status");
                if (a_bind_status != null) {
                    a_bind_status.setValue(status);
                } else {
                    rule_element.addAttribute("status", status );
                }

                Element e_access_ip = rule_element.element("accessIp");
                if (e_access_ip != null) {
                    e_access_ip.setText(accessIp);
                } else {
                    Element add_e = rule_element.addElement("accessIp");
                    add_e.setText(accessIp);
                }

                Element e_access_port = rule_element.element("accessPort");
                if (e_access_port != null) {
                    e_access_port.setText(accessPort);
                } else {
                    Element add_e = rule_element.addElement("accessPort");
                    add_e.setText(accessPort);
                }
                Element e_desc = rule_element.element("desc");
                if (e_desc != null) {
                    e_desc.setText(desc);
                } else {
                    Element add_e = rule_element.addElement("desc");
                    add_e.setText(desc);
                }
                dom4jXMLUtils.writeXML(new_document, path);
                String msg = "操作:新增端口转发规则,结果:成功,监听IP"+bindIp+",端口"+bindPort+",协议:"+protocol;
                LogUtils.write(logPath, msg, "INFO");
                return new ResultObject(true,msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String msg = "操作:新增端口转发规则,结果:失败,监听IP"+bindIp+",端口"+bindPort+",协议:"+protocol;
        LogUtils.write(logPath, msg, "ERROR");
        return new ResultObject(false,msg);
    }

    /**
     * 删除端口映射规则
     *
     * @return
     */
    private ResultObject delete(String logPath,String pId,String bindIp, String bindPort,String protocol,String path) {
        try {
            Dom4jXMLUtils dom4jXMLUtils = new Dom4jXMLUtils();
            File file = new File(path);
            Document document = dom4jXMLUtils.getDocument(file);
            if (document != null) {
                List<Element> composites = document.selectNodes("/portForward/rule[@bindIp='" + bindIp +"'][@bindPort='"+bindPort+"'][@protocol='"+protocol+"']");
                if (composites != null&&composites.size()>0) {
                    Element e = composites.get(0);
                    Attribute status_e = e.attribute("status");
                    if(status_e!=null&&"1".equals(status_e.getValue())){
                        Socks5 socks5 = new Socks5();
                        int result = socks5.pipeDel(Integer.parseInt(pId));
                        if(result==-1){
                            String  msg = "操作:更新端口转发规则,结果:失败,监听IP" + bindIp + ",端口" + bindPort + ",协议:" + protocol+"清除失败";
                            LogUtils.write(logPath, msg, "ERROR");
//                            return new ResultObject(false, msg);
                        }else if(result==-2){
                            String msg = "操作:更新端口转发规则,结果:失败,监听IP" + bindIp + ",端口" + bindPort + ",协议:" + protocol+"进程不存在";
                            LogUtils.write(logPath, msg, "ERROR");
//                            return new ResultObject(false, msg);
                        }
                    }
                    document.getRootElement().remove(e);
                    dom4jXMLUtils.writeXML(document, path);
                    String msg = "操作:删除端口转发规则,结果:成功,监听IP"+bindIp+",端口"+bindPort+",协议:"+protocol;
                    LogUtils.write(logPath, msg, "INFO");
                    return new ResultObject(true,msg);
                }else {
                    String msg = "操作:删除端口转发规则,结果:失败,监听IP"+bindIp+",端口"+bindPort+",协议:"+protocol+",记录不存在";
                    LogUtils.write(logPath, msg, "ERROR");
                    return new ResultObject(false,msg);
                }
            }else {
                String msg = "操作:删除端口转发规则,结果:失败,监听IP"+bindIp+",端口"+bindPort+",协议:"+protocol+",记录不存在";
                LogUtils.write(logPath, msg, "ERROR");
                return new ResultObject(false,msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String msg = "操作:删除端口转发规则,结果:失败,监听IP"+bindIp+",端口"+bindPort+",协议:"+protocol;
        return new ResultObject(false,msg);
    }


    /**
     * enable
     *
     * @return
     */
    private ResultObject enable(String logPath,String bindIp, String bindPort,String protocol,String pId,String path) {
        try {
            Dom4jXMLUtils dom4jXMLUtils = new Dom4jXMLUtils();
            File file = new File(path);
            Document document = dom4jXMLUtils.getDocument(file);
            if (document != null) {
                List<Element> composites = document.selectNodes("/portForward/rule[@bindIp='" + bindIp +"'][@bindPort='"+bindPort+"'][@protocol='"+protocol+"']");
                if (composites != null&&composites.size()>0) {
                    Element e = composites.get(0);
                    Attribute a_bind_status = e.attribute("status");
                    if (a_bind_status != null) {
                        a_bind_status.setValue("1");
                    } else {
                        e.addAttribute("status", "1");
                    }
                    Element pId_e =  e.element("pId");
                    if(pId_e!=null){
                        pId_e.setText(pId);
                    }else {
                        Element a_e = e.addElement("pId");
                        a_e.setText(pId);
                    }
                    dom4jXMLUtils.writeXML(document, path);
                    String msg = "操作:启用端口转发规则,结果:成功,监听IP"+bindIp+",端口"+bindPort+",协议:"+protocol;
                    LogUtils.write(logPath, msg, "INFO");
                    return new ResultObject(true,msg);
                }else {
                    String msg = "操作:启用端口转发规则,结果:失败,监听IP"+bindIp+",端口"+bindPort+",协议:"+protocol+",未找到记录";
                    LogUtils.write(logPath, msg, "ERROT");
                    return new ResultObject(false,msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String msg = "操作:启用端口转发规则,结果:失败,监听IP"+bindIp+",端口"+bindPort+",协议:"+protocol;
        LogUtils.write(logPath, msg, "ERROR");
        return new ResultObject(false,msg);
    }


    /**
     * enable
     *
     * @return
     */
    private ResultObject disable(String logPath,String bindIp, String bindPort,String protocol,String path) {
        try {
            Dom4jXMLUtils dom4jXMLUtils = new Dom4jXMLUtils();
            File file = new File(path);
            Document document = dom4jXMLUtils.getDocument(file);
            if (document != null) {
                List<Element> composites = document.selectNodes("/portForward/rule[@bindIp='" + bindIp +"'][@bindPort='"+bindPort+"'][@protocol='"+protocol+"']");
                if (composites != null&&composites.size()>0) {
                    Element e = composites.get(0);
                    Attribute a_bind_status = e.attribute("status");
                    if (a_bind_status != null) {
                        a_bind_status.setValue("0");
                    } else {
                        e.addAttribute("status", "0" );
                    }
                    dom4jXMLUtils.writeXML(document, path);
                    String msg = "操作:停用端口转发规则,结果:成功,监听IP"+bindIp+",端口"+bindPort+",协议:"+protocol;
                    LogUtils.write(logPath, msg, "INFO");
                    return new ResultObject(true,msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String msg = "操作:停用端口转发规则,结果:失败,监听IP"+bindIp+",端口"+bindPort+",协议:"+protocol;
        LogUtils.write(logPath, msg, "ERROR");
        return new ResultObject(false,msg);
    }

    /**
     * 更新服务端口
     *
     * @return
     */
    private ResultObject modify(String logPath,String pId,String oldBindIp,String oldBindPort,String oldProtocol,String oldAccessIp,String oldAccessPort,String oldDesc,String bindIp, String bindPort, String accessIp, String accessPort, String protocol, String desc, String path) {
        try {
            Dom4jXMLUtils dom4jXMLUtils = new Dom4jXMLUtils();
            File file = new File(path);
            Document document = dom4jXMLUtils.getDocument(file);
            if (document != null) {
                List<Element> composites = document.selectNodes("/portForward/rule[@bindIp='" + oldBindIp +"'][@bindPort='"+oldBindPort+"'][@protocol='"+oldProtocol+"']");
                if (composites != null&&composites.size()>0) {
                    List<Element> t_composites = document.selectNodes("/portForward/rule[@bindIp='" + bindIp +"'][@bindPort='"+bindPort+"'][@protocol='"+protocol+"']");
                    if(t_composites!= null&&t_composites.size()>0){
                        if(bindIp.equals(oldBindIp)&&bindPort.equals(oldBindPort)&&protocol.equals(oldProtocol)){
                            if(!accessIp.equals(oldAccessIp)||!accessPort.equals(oldAccessPort)){
                                Element e = composites.get(0);
                                Attribute status_e = e.attribute("status");
                                if(status_e!=null&&"1".equals(status_e.getValue())){
                                    Socks5 socks5 = new Socks5();
                                    int result = socks5.pipeDel(Integer.parseInt(pId));
                                    if(result==-1){
                                        String msg = "操作:更新端口转发规则,结果:失败,监听IP" + bindIp + ",端口" + bindPort + ",协议:" + protocol+"清除失败";
                                        LogUtils.write(logPath, msg, "ERROR");
//                                return new ResultObject(false, msg);
                                    }else if(result==-2){
                                        String msg = "操作:更新端口转发规则,结果:失败,监听IP" + bindIp + ",端口" + bindPort + ",协议:" + protocol+"进程不存在";
                                        LogUtils.write(logPath, msg, "ERROR");
//                                return new ResultObject(false, msg);
                                    }
                                }
                                Attribute a_bind_ip = e.attribute("bindIp");
                                if (a_bind_ip != null) {
                                    a_bind_ip.setValue(bindIp);
                                } else {
                                    e.addAttribute("bindIp", bindIp);
                                }

                                Attribute a_bind_port = e.attribute("bindPort");
                                if (a_bind_port != null) {
                                    a_bind_port.setValue(bindPort);
                                } else {
                                    e.addAttribute("bindPort", bindPort);
                                }

                                Attribute a_bind_protocol = e.attribute("protocol");
                                if (a_bind_protocol != null) {
                                    a_bind_protocol.setValue(protocol);
                                } else {
                                    e.addAttribute("protocol", protocol);
                                }

                                Element e_accessIp = e.element("accessIp");
                                if (e_accessIp != null) {
                                    e_accessIp.setText(accessIp);
                                }

                                Element e_accessPort = e.element("accessPort");
                                if (e_accessPort != null) {
                                    e_accessPort.setText(accessPort);
                                }

                                Element e_desc = e.element("desc");
                                if (e_desc != null) {
                                    e_desc.setText(desc);
                                }
                                Element e_pId = e.element("pId");
                                if(status_e!=null&&"1".equals(status_e.getValue())){
                                    Socks5 socks5 = new Socks5();
                                    int result = socks5.pipeAdd(bindIp,bindPort,accessIp,accessPort,protocol);
                                    if(result!=-1) {
                                        if (e_pId != null)
                                            e_pId.setText(String.valueOf(result));
                                        else {
                                            Element add_pId = e.addElement("pId");
                                            add_pId.setText(String.valueOf(result));
                                        }
                                    }else {
                                        status_e.setValue("0");
                                        String msg = "操作:更新端口转发规则,结果:失败,监听IP"+bindIp+",端口"+bindPort+",协议:"+protocol+",开启端口服务出错";
                                        LogUtils.write(logPath, msg, "ERROR");
                                    }
                                }
                                dom4jXMLUtils.writeXML(document, path);
                                String msg = "操作:更新端口转发规则,结果:成功,监听IP" + bindIp + ",端口" + bindPort + ",协议:" + protocol;
                                LogUtils.write(logPath, msg, "INFO");
                                return new ResultObject(true, msg);
                            }else {
                                Element e = composites.get(0);
                                Element e_desc = e.element("desc");
                                if (e_desc != null) {
                                    e_desc.setText(desc);
                                }
                                dom4jXMLUtils.writeXML(document, path);
                                String msg = "操作:更新端口转发规则,结果:成功,监听IP" + bindIp + ",端口" + bindPort + ",协议:" + protocol;
                                LogUtils.write(logPath, msg, "INFO");
                                return new ResultObject(true, msg);
                            }
                        }else {
                            String msg = "操作:更新端口转发规则,结果:失败,监听IP" + bindIp + ",端口" + bindPort + ",协议:" + protocol+"记录已存在";
                            LogUtils.write(logPath, msg, "ERROR");
                            return new ResultObject(false, msg);
                        }
                    }else {
                        Element e = composites.get(0);
                        Attribute status_e = e.attribute("status");
                        if(status_e!=null&&"1".equals(status_e.getValue())){
                            Socks5 socks5 = new Socks5();
                            int result = socks5.pipeDel(Integer.parseInt(pId));
                            if(result==-1){
                                String msg = "操作:更新端口转发规则,结果:失败,监听IP" + bindIp + ",端口" + bindPort + ",协议:" + protocol+"清除失败";
                                LogUtils.write(logPath, msg, "ERROR");
//                                return new ResultObject(false, msg);
                            }else if(result==-2){
                                String msg = "操作:更新端口转发规则,结果:失败,监听IP" + bindIp + ",端口" + bindPort + ",协议:" + protocol+"进程不存在";
                                LogUtils.write(logPath, msg, "ERROR");
//                                return new ResultObject(false, msg);
                            }
                        }
                        Attribute a_bind_ip = e.attribute("bindIp");
                        if (a_bind_ip != null) {
                            a_bind_ip.setValue(bindIp);
                        } else {
                            e.addAttribute("bindIp", bindIp);
                        }

                        Attribute a_bind_port = e.attribute("bindPort");
                        if (a_bind_port != null) {
                            a_bind_port.setValue(bindPort);
                        } else {
                            e.addAttribute("bindPort", bindPort);
                        }

                        Attribute a_bind_protocol = e.attribute("protocol");
                        if (a_bind_protocol != null) {
                            a_bind_protocol.setValue(protocol);
                        } else {
                            e.addAttribute("protocol", protocol);
                        }

                        Element e_accessIp = e.element("accessIp");
                        if (e_accessIp != null) {
                            e_accessIp.setText(accessIp);
                        }

                        Element e_accessPort = e.element("accessPort");
                        if (e_accessPort != null) {
                            e_accessPort.setText(accessPort);
                        }

                        Element e_desc = e.element("desc");
                        if (e_desc != null) {
                            e_desc.setText(desc);
                        }
                        Element e_pId = e.element("pId");
                        if(status_e!=null&&"1".equals(status_e.getValue())){
                            Socks5 socks5 = new Socks5();
                            int result = socks5.pipeAdd(bindIp,bindPort,accessIp,accessPort,protocol);
                            if(result!=-1) {
                                if (e_pId != null)
                                    e_pId.setText(String.valueOf(result));
                                else {
                                    Element add_pId = e.addElement("pId");
                                    add_pId.setText(String.valueOf(result));
                                }
                            }else {
                                status_e.setValue("0");
                                String msg = "操作:更新端口转发规则,结果:失败,监听IP"+bindIp+",端口"+bindPort+",协议:"+protocol+",开启端口服务出错";
                                LogUtils.write(logPath, msg, "ERROR");
                            }
                        }
                        dom4jXMLUtils.writeXML(document, path);
                        String msg = "操作:更新端口转发规则,结果:成功,监听IP" + bindIp + ",端口" + bindPort + ",协议:" + protocol;
                        LogUtils.write(logPath, msg, "INFO");
                        return new ResultObject(true, msg);
                    }
                }else {
                    String msg = "操作:更新端口转发规则,结果:失败,监听IP"+bindIp+",端口"+bindPort+",协议:"+protocol+",记录不存在";
                    LogUtils.write(logPath, msg, "ERROR");
                    return new ResultObject(false,msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String msg = "操作:更新端口转发规则,结果:失败,监听IP"+bindIp+",端口"+bindPort+",协议:"+protocol;
        LogUtils.write(logPath, msg, "ERROR");
        return new ResultObject(false,msg);
    }


    private boolean save_status(String path, String status) {
        boolean flag = false;
        try {
            Dom4jXMLUtils dom4jXMLUtils = new Dom4jXMLUtils();
            File file = new File(path);
            Document document = dom4jXMLUtils.getDocument(file);
            if (document != null) {
                Element root = document.getRootElement();
                if (root != null) {
                    Element e_status = root.element("status");

                    if (e_status != null) {
                        e_status.setText(status);
                    } else {
                        Element new_e_status = root.addElement("status");
                        new_e_status.setText(status);
                    }
                } else {
                    Element server_root = document.addElement("portForward");
                    Element new_e_status = server_root.addElement("status");
                    new_e_status.setText(status);
                }
                dom4jXMLUtils.writeXML(document, path);
            } else {
                Document new_document = dom4jXMLUtils.getNewDocument();
                Element portForward = new_document.addElement("portForward");
                Element new_e_status = portForward.addElement("status");
                new_e_status.setText(status);
                dom4jXMLUtils.writeXML(new_document, path);
            }
            flag = true;
        } catch (Exception e) {

        }
        return flag;
    }


    /**
     * 更新WIFI配置
     *
     * @return
     */
    private boolean save_server(String path, String port) {
        boolean flag = false;
        try {
            Dom4jXMLUtils dom4jXMLUtils = new Dom4jXMLUtils();
            File file = new File(path);
            Document document = dom4jXMLUtils.getDocument(file);
            if (document != null) {
                Element root = document.getRootElement();
                if (root != null) {
                    Element e_port = root.element("serverPort");

                    if (e_port != null) {
                        e_port.setText(port);
                    } else {
                        Element new_port = root.addElement("serverPort");
                        new_port.setText(port);
                    }
                } else {
                    Element server_root = document.addElement("portForward");
                    Element e_port = server_root.addElement("serverPort");
                    e_port.setText(port);
                }
                dom4jXMLUtils.writeXML(document, path);
            } else {
                Document new_document = dom4jXMLUtils.getNewDocument();
                Element portForward = new_document.addElement("portForward");
                Element e_serverPort = portForward.addElement("serverPort");
                e_serverPort.setText(port);
                dom4jXMLUtils.writeXML(new_document, path);
            }
            flag = true;
        } catch (Exception e) {

        }
        return flag;
    }
}
