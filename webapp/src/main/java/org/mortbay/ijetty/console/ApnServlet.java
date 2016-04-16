package org.mortbay.ijetty.console;

import android.content.ContentResolver;
import android.content.Context;
import android.telephony.TelephonyManager;
import com.apn.utils.ApnDao;
import com.apn.utils.ApnInfo;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by hhm on 2014/12/4.
 */
public class ApnServlet extends HttpServlet {
    private ContentResolver resolver;
    private Context androidContext;

    public void init(ServletConfig config) throws ServletException {
        resolver = (ContentResolver) config.getServletContext().getAttribute("org.mortbay.ijetty.contentResolver");
        androidContext = (Context) config.getServletContext().getAttribute("org.mortbay.ijetty.context");
    }


    /**
     * 得到SIM卡上的信息
     */
    private String getNumeric() {
        TelephonyManager iPhoneManager = (TelephonyManager) androidContext.getSystemService(Context.TELEPHONY_SERVICE);
        return iPhoneManager.getSimOperator();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        String command = request.getParameter("command");
        String msg = null;
        String json = "";
        if (command != null && "findAllApn".equals(command)) {
            ApnDao apnDao = new ApnDao(resolver);
            List<ApnInfo> apnInfos = apnDao.findAll();

            Long default_id = apnDao.getDefaultAPN();
            if (apnInfos != null && apnInfos.size() > 0) {
                json += "{totalCount:" + apnInfos.size() + ",rows:[";
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < apnInfos.size(); i++) {
                    ApnInfo apnInfo = apnInfos.get(i);
                    if(apnInfo.getId()==default_id) {
                        sb.append("{");
                        sb.append("id:'" + apnInfo.getId() + "'").append(",");
                        sb.append("apn:'" + apnInfo.getApn() + "'").append(",");
                        sb.append("type:'" + apnInfo.getType() + "'").append(",");
                        sb.append("name:'" + apnInfo.getName() + "'").append(",");
                        sb.append("numeric:'" + apnInfo.getNumeric() + "'").append(",");
                        sb.append("mcc:'" + apnInfo.getMcc() + "'").append(",");
                        sb.append("mnc:'" + apnInfo.getMnc() + "'").append(",");
                        sb.append("proxy:'" + apnInfo.getProxy() + "'").append(",");
                        sb.append("port:'" + apnInfo.getPort() + "'").append(",");
                        sb.append("mmsproxy:'" + apnInfo.getMmsproxy() + "'").append(",");
                        sb.append("mmsport:'" + apnInfo.getMmsport() + "'").append(",");
                        sb.append("user:'" + apnInfo.getUser() + "'").append(",");
                        sb.append("password:'" + apnInfo.getPassword() + "'").append(",");
                        sb.append("server:'" + apnInfo.getServer() + "'").append(",");
                        sb.append("default:'1'").append(",");
                        sb.append("mmsc:'" + apnInfo.getMmsc() + "'");/*.append(",");
                        sb.append("authtype:'" + apnInfo.getAuthtype() + "'");*/
                        sb.append("}").append(",");
                    }else {
                        sb.append("{");
                        sb.append("id:'" + apnInfo.getId() + "'").append(",");
                        sb.append("apn:'" + apnInfo.getApn() + "'").append(",");
                        sb.append("type:'" + apnInfo.getType() + "'").append(",");
                        sb.append("name:'" + apnInfo.getName() + "'").append(",");
                        sb.append("numeric:'" + apnInfo.getNumeric() + "'").append(",");
                        sb.append("mcc:'" + apnInfo.getMcc() + "'").append(",");
                        sb.append("mnc:'" + apnInfo.getMnc() + "'").append(",");
                        sb.append("proxy:'" + apnInfo.getProxy() + "'").append(",");
                        sb.append("port:'" + apnInfo.getPort() + "'").append(",");
                        sb.append("mmsproxy:'" + apnInfo.getMmsproxy() + "'").append(",");
                        sb.append("mmsport:'" + apnInfo.getMmsport() + "'").append(",");
                        sb.append("user:'" + apnInfo.getUser() + "'").append(",");
                        sb.append("password:'" + apnInfo.getPassword() + "'").append(",");
                        sb.append("server:'" + apnInfo.getServer() + "'").append(",");
                        sb.append("default:'0'").append(",");
                        sb.append("mmsc:'" + apnInfo.getMmsc() + "'");/*.append(",");
                        sb.append("authtype:'" + apnInfo.getAuthtype() + "'");*/
                        sb.append("}").append(",");
                    }
                }
                json += sb.toString();
                json = json.substring(0, json.indexOf(",", json.length() - 1));
                json += "]}";

                writer.write(json.toString());
                writer.flush();
                writer.close();
            }
        } else if (command != null && "addApn".equals(command)) {
            String apn = request.getParameter("apn");
            String type = request.getParameter("type");
            String name = request.getParameter("name");
            String numeric = request.getParameter("numeric");
            String mcc = request.getParameter("mcc");
            String mnc = request.getParameter("mnc");
            String proxy = request.getParameter("proxy");
            String port = request.getParameter("port");
            String mmsproxy = request.getParameter("mmsproxy");
            String mmsport = request.getParameter("mmsport");
            String user = request.getParameter("user");
            String password = request.getParameter("password");
            String server = request.getParameter("server");
            String mmsc = request.getParameter("mmsc");
            String authtype = request.getParameter("authtype");

            String sim_numeric = getNumeric();
            ApnInfo apnInfo = new ApnInfo();
            apnInfo.setApn(apn);
            apnInfo.setType(type);
            apnInfo.setName(name);
            if (numeric != null && numeric.length() > 0)
                apnInfo.setNumeric(numeric);
            else
                apnInfo.setNumeric(sim_numeric);
            if (mcc != null && mcc.length() > 0)
                apnInfo.setMcc(mcc);
            else
                apnInfo.setMcc(sim_numeric.substring(0, 3));
            if (mnc != null && mnc.length() > 0)
                apnInfo.setMnc(mnc);
            else
                apnInfo.setMnc(sim_numeric.substring(3, sim_numeric.length()));
            apnInfo.setProxy(proxy);
            apnInfo.setPort(port);
            apnInfo.setMmsproxy(mmsproxy);
            apnInfo.setMmsport(mmsport);
            apnInfo.setUser(user);
            apnInfo.setPassword(password);
            apnInfo.setServer(server);
            apnInfo.setMmsc(mmsc);
//            apnInfo.setAuthtype(authtype);

            ApnDao apnDao = new ApnDao(resolver);
            long result = apnDao.addAPN(apnInfo);
            if (result != -1) {
                msg = "操作:新增APN,结果:成功";
                LogUtils.write(request.getRealPath("/system.log"), msg, "INFO");
                json = "{success:true,msg:'" + msg + "'}";
                writer.write(json);
                writer.flush();
                writer.close();
            } else if (result == -2) {
                msg = "操作:新增APN,结果:失败,信息:新增记录已在在";
                LogUtils.write(request.getRealPath("/system.log"), msg, "ERROR");
                json = "{success:false,msg:'" + msg + "'}";
                writer.write(json);
                writer.flush();
                writer.close();
            } else {
                msg = "操作:新增APN,结果:失败";
                LogUtils.write(request.getRealPath("/system.log"), msg, "ERROR");
                json = "{success:false,msg:'" + msg + "'}";
                writer.write(json);
                writer.flush();
                writer.close();
            }
        } else if (command != null && "modifyApn".equals(command)) {
            String id = request.getParameter("id");
            String apn = request.getParameter("apn");
            String type = request.getParameter("type");
            String name = request.getParameter("name");
            String numeric = request.getParameter("numeric");
            String mcc = request.getParameter("mcc");
            String mnc = request.getParameter("mnc");
            String proxy = request.getParameter("proxy");
            String port = request.getParameter("port");
            String mmsproxy = request.getParameter("mmsproxy");
            String mmsport = request.getParameter("mmsport");
            String user = request.getParameter("user");
            String password = request.getParameter("password");
            String server = request.getParameter("server");
            String mmsc = request.getParameter("mmsc");
            String authtype = request.getParameter("authtype");

            String sim_numeric = getNumeric();

            ApnInfo apnInfo = new ApnInfo();
            apnInfo.setApn(apn);
            apnInfo.setType(type);
            apnInfo.setName(name);
            if (numeric != null && numeric.length() > 0)
                apnInfo.setNumeric(numeric);
            else
                apnInfo.setNumeric(sim_numeric);
            if (mcc != null && mcc.length() > 0)
                apnInfo.setMcc(mcc);
            else
                apnInfo.setMcc(sim_numeric.substring(0, 3));
            if (mnc != null && mnc.length() > 0)
                apnInfo.setMnc(mnc);
            else
                apnInfo.setMnc(sim_numeric.substring(3, sim_numeric.length()));
            apnInfo.setProxy(proxy);
            apnInfo.setPort(port);
            apnInfo.setMmsproxy(mmsproxy);
            apnInfo.setMmsport(mmsport);
            apnInfo.setUser(user);
            apnInfo.setPassword(password);
            apnInfo.setServer(server);
            apnInfo.setMmsc(mmsc);
//            apnInfo.setAuthtype(authtype);
            apnInfo.setId(Long.parseLong(id));

            ApnDao apnDao = new ApnDao(resolver);
            long result = apnDao.modifyApn(apnInfo);
            if (result != -1) {
                msg = "操作:修改APN,结果:成功";
                LogUtils.write(request.getRealPath("/system.log"), msg, "INFO");
                json = "{success:true,msg:'" + msg + "'}";
                writer.write(json);
                writer.flush();
                writer.close();
            } else if (result == -2) {
                msg = "操作:修改APN,结果:失败,信息:未找到要修改记录";
                LogUtils.write(request.getRealPath("/system.log"), msg, "ERROR");
                json = "{success:true,msg:'" + msg + "'}";
                writer.write(json);
                writer.flush();
                writer.close();
            } else {
                msg = "操作:修改APN,结果:失败";
                LogUtils.write(request.getRealPath("/system.log"), msg, "ERROR");
                json = "{success:true,msg:'" + msg + "'}";
                writer.write(json);
                writer.flush();
                writer.close();
            }
        } else if (command != null && "delApn".equals(command)) {
            String id = request.getParameter("id");
            ApnDao apnDao = new ApnDao(resolver);
            long result = apnDao.deleteApnId(Long.parseLong(id));
            if (result != -1) {
                msg = "操作:删除APN,结果:成功";
                LogUtils.write(request.getRealPath("/system.log"), msg, "INFO");
                json = "{success:true,msg:'" + msg + "'}";
                writer.write(json);
                writer.flush();
                writer.close();
            } else {
                msg = "操作:删除APN,结果:失败";
                LogUtils.write(request.getRealPath("/system.log"), msg, "ERROR");
                json = "{success:false,msg:'" + msg + "'}";
                writer.write(json);
                writer.flush();
                writer.close();
            }
        }

        else if (command != null && "defaultApn".equals(command)) {
            String id = request.getParameter("id");
            ApnDao apnDao = new ApnDao(resolver);
            apnDao.setDefaultAPN(Long.parseLong(id));
            msg = "操作:设置默认APN,结果:成功";
            LogUtils.write(request.getRealPath("/system.log"), msg, "INFO");
            json = "{success:true,msg:'" + msg + "'}";
            writer.write(json);
            writer.flush();
            writer.close();
        }



    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
