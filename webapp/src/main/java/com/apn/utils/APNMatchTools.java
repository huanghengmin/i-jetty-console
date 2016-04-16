package com.apn.utils;

/**
 * APN匹配，用于匹配移动或者联通的APN
 */
public final class APNMatchTools {

    public static class APNNet {
        /**
         * 中国移动cmwap
         */
        public static String CMWAP = "cmwap";

        /**
         * 中国移动cmnet
         */
        public static String CMNET = "cmnet";

        /**
         * 3G wap 中国联通3gwap APN
         */
        public static String GWAP_3 = "3gwap";

        /**
         * 3G net 中国联通3gnet APN
         */
        public static String GNET_3 = "3gnet";

        /**
         * uni wap 中国联通uni wap APN
         */
        public static String UNIWAP = "uniwap";
        /**
         * uni net 中国联通uni net APN
         */
        public static String UNINET = "uninet";
    }

    public static String matchAPN(String currentName) {
        if ("".equals(currentName) || null == currentName) {
            return "";
        }
        currentName = currentName.toLowerCase();
        if (currentName.startsWith(APNNet.CMNET))
            return APNNet.CMNET;
        else if (currentName.startsWith(APNNet.CMWAP))
            return APNNet.CMWAP;
        else if (currentName.startsWith(APNNet.GNET_3))
            return APNNet.GNET_3;
        else if (currentName.startsWith(APNNet.GWAP_3))
            return APNNet.GWAP_3;
        else if (currentName.startsWith(APNNet.UNINET))
            return APNNet.UNINET;
        else if (currentName.startsWith(APNNet.UNIWAP))
            return APNNet.UNIWAP;
        else if (currentName.startsWith("default"))
            return "default";
        else
            return "";
    }
}
