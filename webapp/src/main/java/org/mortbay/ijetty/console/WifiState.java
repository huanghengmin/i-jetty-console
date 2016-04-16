package org.mortbay.ijetty.console;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-9-23
 * Time: 下午9:24
 * To change this template use File | Settings | File Templates.
 */
public class WifiState {
    public static final int NETWORK_CLASS_UNKNOWN = 0;

    public static final int NETWORK_CLASS_2_G = 1;

    public static final int NETWORK_CLASS_3_G = 2;

    public static final int NETWORK_CLASS_4_G = 3;

    public static final int NETWORK_TYPE_UNKNOWN = 0;

    public static final int NETWORK_TYPE_GPRS = 1;
    public static final int NETWORK_TYPE_EDGE = 2;
    public static final int NETWORK_TYPE_UMTS = 3;
    public static final int NETWORK_TYPE_CDMA = 4;
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    public static final int NETWORK_TYPE_EVDO_A = 6;
    public static final int NETWORK_TYPE_1xRTT = 7;
    public static final int NETWORK_TYPE_HSDPA = 8;
    public static final int NETWORK_TYPE_HSUPA = 9;
    public static final int NETWORK_TYPE_HSPA = 10;
    public static final int NETWORK_TYPE_IDEN = 11;
    public static final int NETWORK_TYPE_EVDO_B = 12;
    public static final int NETWORK_TYPE_LTE = 13;
    public static final int NETWORK_TYPE_EHRPD = 14;
    public static final int NETWORK_TYPE_HSPAP = 15;

    public static ArrayList<String> getConnectedIP() {
        ArrayList<String> connectedIP = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(
                    "/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4) {
                    String ip = splitted[0];
                    connectedIP.add(ip);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connectedIP;
    }

    public static String getMacFromFile(String mIP) {
        if (mIP == null || mIP.length() <= 0)
            return null;
        List<String> mResult = readFileLines("/proc/net/arp");
        for (int i = 0; i < mResult.size(); ++i)
            if (mResult != null && mResult.size() > 1) {
                for (int j = 1; j < mResult.size(); ++j) {
                    List<String> mList = new ArrayList<String>();
                    String[] mType = mResult.get(j).split(" ");
                    for (int l = 0; l < mType.length; ++l) {
                        if (mType[l] != null && mType[l].length() > 0)
                            mList.add(mType[l]);
                    }
                    if (mList != null && mList.size() > 4 && mList.get(0).equalsIgnoreCase(mIP)) {
                        String result = "";
                        String[] tmp = mList.get(3).split(":");
                        for (int k = 0; k < tmp.length; ++k) {
                            result += tmp[k];
                        }
                        result = result.toUpperCase();
                        return result;
                    }
                }
            }
        return null;
    }

    private static List<String> readFileLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        String tempString = "";
        List<String> mResult = new ArrayList<String>();
        try {
            reader = new BufferedReader(new FileReader(file));
            while ((tempString = reader.readLine()) != null) {
                mResult.add(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return mResult;
    }


    public static int getNetworkClass(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;
            case NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }
}
