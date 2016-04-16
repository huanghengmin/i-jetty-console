package com.socks5;

public class Socks5 {

        public native boolean start(int port,String string);

        public native boolean stop();

        public native int pipeAdd(String bhost,String  bport,String thost,String tport ,String type);
  
    	public native int pipeDel(int processId);
    	
      static {
        System.loadLibrary("socks5");
    }

}
