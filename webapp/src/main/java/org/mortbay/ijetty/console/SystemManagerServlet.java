package org.mortbay.ijetty.console;


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * Created by Administrator on 14-9-26.
 */
public class SystemManagerServlet extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        String command = request.getParameter("command");
        String msg = null;
        String json = null;
        //系统日志下载
        if(command!=null&&"downLog".equals(command)){
//            json = "{success:false}";
            try {
                String Agent = request.getHeader("User-Agent");
                StringTokenizer st = new StringTokenizer(Agent,";");
                st.nextToken();
                //得到用户的浏览器名  MSIE  Firefox
                String userBrowser = st.nextToken();
                String path = request.getRealPath("/system.log");
                File source = new File(path);
                if (source != null) {
                    String name = source.getName();
                    FileUtil.downType(response, name, userBrowser);
                    response = FileUtil.copy(source, response);
//                    json = "{success:true}";
                    msg = "操作:下载系统日志,操作结果:成功";
                    LogUtils.write(request.getRealPath("/system.log"),msg,"INFO");
                }
            } catch (Exception e) {
//                json = "success:false";
                msg = "操作:下载系统日志,操作结果:失败";
                LogUtils.write(request.getRealPath("/system.log"),msg,"ERROR");
            }
            /*PrintWriter writer = response.getWriter();
            writer.write(json);
            writer.flush();
            writer.close();*/
        }
        //获取连接wifi列表
        else if(command!=null&&"reboot".equals(command)){
            reboot();
            msg = "操作:系统重启,操作结果:正在重启.....";
            LogUtils.write(request.getRealPath("/system.log"),msg,"INFO");
            json = "{success:true,msg:'"+msg+"'}";
            PrintWriter writer = response.getWriter();
            writer.write(json);
            writer.flush();
            writer.close();

        }
        //SSLVPN状态
        else if(command!=null&&"shutdown".equals(command)){
            shutdown();
            msg = "操作:系统关闭,操作结果:正在关闭.....";
            LogUtils.write(request.getRealPath("/system.log"),msg,"INFO");
            json = "{success:true,msg:'"+msg+"'}";
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
     * 系统重启cc
     *
     * @return
     */
    private void reboot() {
        RebootOrShutdown rebootOrShutdown = new RebootOrShutdown();
        rebootOrShutdown.process(rebootOrShutdown.CMD_REBOOT);
    }

    /**
     * 关机
     *
     * @return
     */
    private void shutdown() {
        RebootOrShutdown rebootOrShutdown = new RebootOrShutdown();
        rebootOrShutdown.process(rebootOrShutdown.CMD_SHUTDOWN);
    }
}
