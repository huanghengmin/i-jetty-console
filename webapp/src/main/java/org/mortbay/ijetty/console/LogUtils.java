package org.mortbay.ijetty.console;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Administrator on 14-9-26.
 */

public class LogUtils {


    /**
     * format date
     *
     * @param date
     * @return
     */


    public static String formatDate(Date date) {
        String result = null;
        SimpleDateFormat sim = new SimpleDateFormat("yyyy年MM月dd日 hh时mm分ss秒");
        result = sim.format(date);
        return result;
    }


    /**
     * 追加写日志文件
     *
     * @return
     */

    public static boolean write(String filePath, String logMsg, String logLevel) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            if (logMsg != null && logMsg.length() > 0) {
                if (logLevel == null)
                    logLevel = "INFO";
                logMsg = formatDate(new Date()) + ",  " + logLevel.toUpperCase() + "  " + logMsg;
                logMsg += "\r\n";
                fileOutputStream.write(logMsg.getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}

