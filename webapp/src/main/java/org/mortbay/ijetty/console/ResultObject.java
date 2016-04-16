package org.mortbay.ijetty.console;

/**
 * Created by Administrator on 14-11-9.
 */
public class ResultObject {
    private boolean flag;
    private String msg;

    public ResultObject(boolean flag, String msg) {
        this.flag = flag;
        this.msg = msg;
    }

    public ResultObject() {
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
