package org.mortbay.ijetty.console;

import java.io.*;
import java.net.*;

public class SocketClient {
    /** 套接字地址 */
    private SocketAddress address;
    /** 超时时间 */
    private int timeout;
    /** 客户端监听接口 */

    public SocketClient(String host, int port, int timeout) {
        this.address = new InetSocketAddress(host, port);
        this.timeout = timeout;
    }

    /**
     * 发送一个消息
     *
     */
    public String sendMessage(String command) {
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(address, timeout);
            return sendMessage(socket, command);
        } catch (ConnectException e) { // 拒绝连接
            e.printStackTrace();
        } catch (SocketTimeoutException e) { // 连接超时
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != socket) {
                    socket.close(); // 关闭Socket
                    socket = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /** 通过Socket发送obj消息 */
    private String sendMessage(Socket socket, String command) {
        PrintWriter out = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            out.println(command);
            out.flush();
            if(in!=null){
                return in.readLine();
            }
        } catch (SocketException e) { // Connection reset

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                out.close();
            }
        }
        return null;
    }
}