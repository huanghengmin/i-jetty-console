package com.apn.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;

public final class ApnDao {

    // 网络类型
    private static final int NET_3G = 1, NET_WIFI = 2, NET_OTHER = -1;

    /**
     * 查看全部的apn数据
     */
    static final Uri CONTENT_URI = Uri.parse("content://telephony/carriers");

    /**
     * current=1即可用的apn数据
     */
    static final Uri CURRENT_CONTENT_URI = Uri.parse("content://telephony/carriers/current");

    /**
     * 查看当前正在使用的apn数据
     */
    static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

    private ContentResolver contentResolver;

    public ApnDao(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    /**
     * 获取当前网络类型
     * @return
     */
    public int GetCurrentNetType(Context context) {
        int net_type = getNetWorkType(context);
        if (net_type == ConnectivityManager.TYPE_MOBILE) {
            return NET_3G;
        } else if (net_type == ConnectivityManager.TYPE_WIFI) {
            return NET_WIFI;
        }
        return NET_OTHER;
    }

    private int getNetWorkType(Context context) {
        ConnectivityManager m_ConnectivityManager = (ConnectivityManager) context .getSystemService(Context.CONNECTIVITY_SERVICE);
        if(m_ConnectivityManager!=null){
            NetworkInfo networkInfo = m_ConnectivityManager.getActiveNetworkInfo();
            if (networkInfo != null)
                return networkInfo.getType();
            return -1;
        } else {
            return -1;
        }
    }

    /**
     * 要设置的APN是否与当前使用APN一致
     * @return
     */
    public boolean isCurrentApn(ApnInfo apnInfo) {
        // 初始化移动APN选项信息
        long old_id = getDefaultAPN();
        ApnInfo a = findApnInfoById(old_id);
        if ((apnInfo.getName().equals(a.getName()))
                && (apnInfo.getApn().equals(a.getApn()))
                && (apnInfo.getType().equals(a.getType()))) {
            return true;
        }
        return false;
    }

    /**
     * 转换APN状态
     * 将CMNET切换为要设置的APN
     */
    public void switchApnStatus(Context context,ApnInfo a) {
        // 判断网络类型
        switch (GetCurrentNetType(context)) {
            case NET_3G:
                // 如果3G网络则切换APN网络类型
                if (!(isCurrentApn(a))) {
                    long old_id = findApnByApn(a);
                    if (old_id == -1) {
                        setDefaultAPN(addAPN(a));
                    } else {
                        setDefaultAPN(old_id);
                    }
                }
                break;
            case NET_WIFI:
                // 如果是无线网络则转换为3G网络
                //closeWifiNetwork(context);
                break;
            case NET_OTHER:
                // 如果是其他网络则转化为3G网络
                break;
            default:
                break;
        }
    }


    /**
     * 关闭WIFI网络状态
     */
    public void closeWifiNetwork(Context context){
		WifiManager wifiManager = (WifiManager)context.getSystemService(context.WIFI_SERVICE);
		if(wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED)
			wifiManager.setWifiEnabled(false);
    }


    /**
     * 获取当前正在使用的apn id
     *
     * @return
     */
    public long getDefaultAPN() {
        Cursor cursor = this.contentResolver.query(PREFERRED_APN_URI, new String[]{"_id"}, null, null, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            return Long.parseLong(cursor.getString(cursor.getColumnIndex("_id")));
        }
        return -1;
    }

    /**
     * 设置指定id apn为正在使用的apn
     *
     * @param id
     */
    public void setDefaultAPN(long id) {
        ContentValues cv = new ContentValues();
        cv.putNull("apn_id");
        this.contentResolver.update(PREFERRED_APN_URI, cv, null, null);
        cv.put("apn_id", id);
        this.contentResolver.update(PREFERRED_APN_URI, cv, null, null);
    }

    /**
     * 插入数据到apn 返回插入的apn id
     *
     * @return
     */
    public long addAPN(ApnInfo a) {
        long apnId = -1;
        long id = findApnByApn(a);
        if (id == -1) {
            ContentValues values = new ContentValues();
            if (a.getName() != null && a.getName().length() > 0)
                values.put("name", a.getName());
            else
                values.put("name", "");

            if (a.getApn() != null && a.getApn().length() > 0)
                values.put("apn", a.getApn());
            else
                values.put("apn", "");

            if (a.getType() != null && a.getType().length() > 0)
                values.put("type", a.getType());
            else
                values.put("type", "");

            if (a.getNumeric() != null && a.getNumeric().length() > 0)
                values.put("numeric", a.getNumeric());
            else
                values.put("numeric", "");

            if (a.getMcc() != null && a.getMcc().length() > 0)
                values.put("mcc", a.getMcc());
            else
                values.put("mcc", "");

            if (a.getMnc() != null && a.getMnc().length() > 0)
                values.put("mnc", a.getMnc());
            else
                values.put("mnc", "");

            if (a.getProxy() != null && a.getProxy().length() > 0)
                values.put("proxy", a.getProxy());
            else
                values.put("proxy", "");

            if (a.getPort() != null && a.getPort().length() > 0)
                values.put("port", a.getPort());
            else
                values.put("port", "");

            if (a.getMmsproxy() != null && a.getMmsproxy().length() > 0)
                values.put("mmsproxy", a.getMmsproxy());
            else
                values.put("mmsproxy", "");

            if (a.getMmsport() != null && a.getMmsport().length() > 0)
                values.put("mmsport", a.getMmsport());
            else
                values.put("mmsport", "");

            if (a.getUser() != null && a.getUser().length() > 0)
                values.put("user", a.getUser());
            else
                values.put("user", "");

            if (a.getServer() != null && a.getServer().length() > 0)
                values.put("server", a.getServer());
            else
                values.put("server", "");

            if (a.getPassword() != null && a.getPassword().length() > 0)
                values.put("password", a.getPassword());
            else
                values.put("password", "");

            if (a.getMmsc() != null && a.getMmsc().length() > 0)
                values.put("mmsc", a.getMmsc());
            else
                values.put("mmsc", "");

//            if (a.getAuthtype() != null && a.getAuthtype().length() > 0)
//                values.put("authtype", a.getAuthtype());
//            else
//                values.put("authtype", "");

            if (a.getCurrent() != null && a.getCurrent().length() > 0)
                values.put("current", a.getCurrent());
            else
                values.put("current", "");

            Cursor c = null;

            try {
                Uri newRow = this.contentResolver.insert(CONTENT_URI, values);
                if (newRow != null) {
                    c = this.contentResolver.query(newRow, null, null, null, null);
                    int idindex = c.getColumnIndex("_id");
                    c.moveToFirst();
                    apnId = c.getShort(idindex);
                    return apnId;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (c != null)
                c.close();
        } else {
            return -2;
        }
        return apnId;
    }

    /**
     * 删除指定id apn
     *
     * @param id
     * @return
     */
    public long deleteApnId(long id) {
        long deleteId = -1;
        Uri deleteIdUri = ContentUris.withAppendedId(CONTENT_URI, id);
        try {
            deleteId = this.contentResolver.delete(deleteIdUri, null, null);
        } catch (Exception e) {
            return deleteId;
        }
        return deleteId;
    }

    /**
     * 更新指定apn 需要id
     *
     * @param a
     * @return
     */
    public long modifyApn(ApnInfo a) {
        long result = -1;
        long exist = findApnByApn(a);
        if (exist != -1) {
            deleteApnId(exist);
            result = addAPN(a);
            return result;
        } else {
            return -2;
        }
    }

    /**
     * 根据apn 查找指定apn id,需要name apn type 字段一致
     *
     * @return
     */
    public long findApnByApn(ApnInfo a) {
        long apnId = -1;
        String projection[] = {"_id,name,apn"};
        Cursor mCursor = this.contentResolver.query(CURRENT_CONTENT_URI, projection, "apn IN (?) ", new String[]{String.valueOf(a.getApn())}, null);
        while (mCursor != null && mCursor.moveToNext()) {
            apnId = mCursor.getShort(mCursor.getColumnIndex("_id"));
            String name = mCursor.getString(mCursor.getColumnIndex("name"));
            String apn = mCursor.getString(mCursor.getColumnIndex("apn"));
            if ((a.getName().equals(name)) && (a.getApn().equals(apn))) {
                return apnId;
            } else {
                apnId = -1;
            }
        }
        return apnId;
    }

    /**
     * 获取当前正在使用的apn id
     *
     * @return
     */
    public long findApnById(long id) {
        String projection[] = {"_id,name,apn"};
        Cursor cursor = this.contentResolver.query(CURRENT_CONTENT_URI, projection, "_id IN (?)", new String[]{String.valueOf(id)}, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            return cursor.getLong(0);
        }
        return -1L;
    }


    /**
     * 获取当前正在使用的apn
     *
     * @return
     */
    public ApnInfo findApnInfoById(long id) {
        String projection[] = {"_id,name,apn,port,user,server,password,mmsc,mmsproxy,mmsport,mcc,mnc,numeric,type,proxy,current"};
        Cursor cr = this.contentResolver.query(CURRENT_CONTENT_URI, projection, "_id IN (?)", new String[]{String.valueOf(id)}, null);
        cr.moveToFirst();
        if (!cr.isAfterLast()) {

            ApnInfo a = new ApnInfo();
            a.setId(Long.parseLong(cr.getString(cr.getColumnIndex("_id"))));
            if (cr.getString(cr.getColumnIndex("apn")) != null && cr.getString(cr.getColumnIndex("apn")).length() > 0)
                a.setApn(cr.getString(cr.getColumnIndex("apn")));
            else {
                a.setApn("");
            }
            if (cr.getString(cr.getColumnIndex("name")) != null && cr.getString(cr.getColumnIndex("name")).length() > 0)
                a.setName(cr.getString(cr.getColumnIndex("name")));
            else
                a.setName("");

            if (cr.getString(cr.getColumnIndex("type")) != null && cr.getString(cr.getColumnIndex("type")).length() > 0)
                a.setType(cr.getString(cr.getColumnIndex("type")));
            else
                a.setType("");

            if (cr.getString(cr.getColumnIndex("port")) != null && cr.getString(cr.getColumnIndex("port")).length() > 0)
                a.setPort(cr.getString(cr.getColumnIndex("port")));
            else
                a.setPort("");


            if (cr.getString(cr.getColumnIndex("user")) != null && cr.getString(cr.getColumnIndex("user")).length() > 0)
                a.setUser(cr.getString(cr.getColumnIndex("user")));
            else
                a.setUser("");

            if (cr.getString(cr.getColumnIndex("server")) != null && cr.getString(cr.getColumnIndex("server")).length() > 0)
                a.setServer(cr.getString(cr.getColumnIndex("server")));
            else
                a.setServer("");

            if (cr.getString(cr.getColumnIndex("password")) != null && cr.getString(cr.getColumnIndex("password")).length() > 0)
                a.setPassword(cr.getString(cr.getColumnIndex("password")));
            else
                a.setPassword("");

            if (cr.getString(cr.getColumnIndex("mmsc")) != null && cr.getString(cr.getColumnIndex("mmsc")).length() > 0)
                a.setMmsc(cr.getString(cr.getColumnIndex("mmsc")));
            else
                a.setMmsc("");

            if (cr.getString(cr.getColumnIndex("mmsproxy")) != null && cr.getString(cr.getColumnIndex("mmsproxy")).length() > 0)
                a.setMmsproxy(cr.getString(cr.getColumnIndex("mmsproxy")));
            else
                a.setMmsproxy("");


            if (cr.getString(cr.getColumnIndex("mmsport")) != null && cr.getString(cr.getColumnIndex("mmsport")).length() > 0)
                a.setMmsport(cr.getString(cr.getColumnIndex("mmsport")));
            else
                a.setMmsport("");

            if (cr.getString(cr.getColumnIndex("mcc")) != null && cr.getString(cr.getColumnIndex("mcc")).length() > 0)
                a.setMcc(cr.getString(cr.getColumnIndex("mcc")));
            else
                a.setMcc("");

            if (cr.getString(cr.getColumnIndex("mnc")) != null && cr.getString(cr.getColumnIndex("mnc")).length() > 0)
                a.setMnc(cr.getString(cr.getColumnIndex("mnc")));
            else
                a.setMnc("");

            if (cr.getString(cr.getColumnIndex("numeric")) != null && cr.getString(cr.getColumnIndex("numeric")).length() > 0)
                a.setNumeric(cr.getString(cr.getColumnIndex("numeric")));
            else
                a.setNumeric("");


            if (cr.getString(cr.getColumnIndex("proxy")) != null && cr.getString(cr.getColumnIndex("proxy")).length() > 0)
                a.setProxy(cr.getString(cr.getColumnIndex("proxy")));
            else
                a.setProxy("");

//            if (cr.getString(cr.getColumnIndex("authtype")) != null && cr.getString(cr.getColumnIndex("authtype")).length() > 0)
//                a.setAuthtype(cr.getString(cr.getColumnIndex("authtype")));
//            else
//                a.setAuthtype("");

            if (cr.getString(cr.getColumnIndex("current")) != null && cr.getString(cr.getColumnIndex("current")).length() > 0)
                a.setCurrent(cr.getString(cr.getColumnIndex("current")));
            else
                a.setCurrent("");

            return a;
        }
        return null;
    }

    /**
     * 查找所有apn list
     */
    public List<ApnInfo> findAll() {
        List<ApnInfo> apn_list = new ArrayList<ApnInfo>();
        /*
		 * //建议输入projection字段，提查询效率
		 * String projection[] = {"_id,name,apn,port,user,server,password,mmsc,mmsproxy,mmsport,mcc,mnc,numeric,type"};
		 * Cursor cr = context.getContentResolver().query(APN_LIST_URI,projection, null, null, null);
		 */
        String projection[] = {"_id,name,apn,port,user,server,password,mmsc,mmsproxy,mmsport,mcc,mnc,numeric,type,proxy,current"};
        Cursor cr = this.contentResolver.query(CURRENT_CONTENT_URI, projection, null, null, null);
        while (cr != null && cr.moveToNext()) {
            ApnInfo a = new ApnInfo();
            a.setId(Long.parseLong(cr.getString(cr.getColumnIndex("_id"))));
            if (cr.getString(cr.getColumnIndex("apn")) != null && cr.getString(cr.getColumnIndex("apn")).length() > 0)
                a.setApn(cr.getString(cr.getColumnIndex("apn")));
            else {
                a.setApn("");
            }
            if (cr.getString(cr.getColumnIndex("name")) != null && cr.getString(cr.getColumnIndex("name")).length() > 0)
                a.setName(cr.getString(cr.getColumnIndex("name")));
            else
                a.setName("");

            if (cr.getString(cr.getColumnIndex("type")) != null && cr.getString(cr.getColumnIndex("type")).length() > 0)
                a.setType(cr.getString(cr.getColumnIndex("type")));
            else
                a.setType("");

            if (cr.getString(cr.getColumnIndex("port")) != null && cr.getString(cr.getColumnIndex("port")).length() > 0)
                a.setPort(cr.getString(cr.getColumnIndex("port")));
            else
                a.setPort("");


            if (cr.getString(cr.getColumnIndex("user")) != null && cr.getString(cr.getColumnIndex("user")).length() > 0)
                a.setUser(cr.getString(cr.getColumnIndex("user")));
            else
                a.setUser("");

            if (cr.getString(cr.getColumnIndex("server")) != null && cr.getString(cr.getColumnIndex("server")).length() > 0)
                a.setServer(cr.getString(cr.getColumnIndex("server")));
            else
                a.setServer("");

            if (cr.getString(cr.getColumnIndex("password")) != null && cr.getString(cr.getColumnIndex("password")).length() > 0)
                a.setPassword(cr.getString(cr.getColumnIndex("password")));
            else
                a.setPassword("");

            if (cr.getString(cr.getColumnIndex("mmsc")) != null && cr.getString(cr.getColumnIndex("mmsc")).length() > 0)
                a.setMmsc(cr.getString(cr.getColumnIndex("mmsc")));
            else
                a.setMmsc("");

            if (cr.getString(cr.getColumnIndex("mmsproxy")) != null && cr.getString(cr.getColumnIndex("mmsproxy")).length() > 0)
                a.setMmsproxy(cr.getString(cr.getColumnIndex("mmsproxy")));
            else
                a.setMmsproxy("");


            if (cr.getString(cr.getColumnIndex("mmsport")) != null && cr.getString(cr.getColumnIndex("mmsport")).length() > 0)
                a.setMmsport(cr.getString(cr.getColumnIndex("mmsport")));
            else
                a.setMmsport("");

            if (cr.getString(cr.getColumnIndex("mcc")) != null && cr.getString(cr.getColumnIndex("mcc")).length() > 0)
                a.setMcc(cr.getString(cr.getColumnIndex("mcc")));
            else
                a.setMcc("");

            if (cr.getString(cr.getColumnIndex("mnc")) != null && cr.getString(cr.getColumnIndex("mnc")).length() > 0)
                a.setMnc(cr.getString(cr.getColumnIndex("mnc")));
            else
                a.setMnc("");

            if (cr.getString(cr.getColumnIndex("numeric")) != null && cr.getString(cr.getColumnIndex("numeric")).length() > 0)
                a.setNumeric(cr.getString(cr.getColumnIndex("numeric")));
            else
                a.setNumeric("");


            if (cr.getString(cr.getColumnIndex("proxy")) != null && cr.getString(cr.getColumnIndex("proxy")).length() > 0)
                a.setProxy(cr.getString(cr.getColumnIndex("proxy")));
            else
                a.setProxy("");

//            if (cr.getString(cr.getColumnIndex("authtype")) != null && cr.getString(cr.getColumnIndex("authtype")).length() > 0)
//                a.setAuthtype(cr.getString(cr.getColumnIndex("authtype")));
//            else
//                a.setAuthtype("");

            if (cr.getString(cr.getColumnIndex("current")) != null && cr.getString(cr.getColumnIndex("current")).length() > 0)
                a.setCurrent(cr.getString(cr.getColumnIndex("current")));
            else
                a.setCurrent("");

            apn_list.add(a);
        }
        if (apn_list.size() > 0) {
            return apn_list;
        } else {
            return null;
        }
    }
}
