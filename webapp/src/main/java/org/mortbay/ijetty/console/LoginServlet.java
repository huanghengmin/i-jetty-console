package org.mortbay.ijetty.console;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-9-5
 * Time: 下午12:43
 * To change this template use File | Settings | File Templates.
 */
public class LoginServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        String username = req.getParameter("username");
        String pwd = req.getParameter("password");
        if (username.equals("admin") && pwd.equals("admin")) {
            String msg = "操作:登陆路由器,操作结果:成功,用户名:"+username;
            LogUtils.write(req.getRealPath("/system.log"),msg,"INFO");
            RequestDispatcher rd = req.getRequestDispatcher("index.html");
            rd.forward(req, resp);
        } else {
            String msg = "操作:登陆路由器,操作结果:失败,用户名:"+username;
            LogUtils.write(req.getRealPath("/system.log"),msg,"ERROR");
            RequestDispatcher dispatcher = req.getRequestDispatcher("loginError.html");
            dispatcher .forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
