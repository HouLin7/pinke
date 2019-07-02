package com.gome.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class PhoneStateUtils {
    /**
     * 获取手机唯一标识(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
    public static String getDeviceID(Context ctx) {
        String result = "";
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
            result = tm.getDeviceId();
        }
        return result == null ? "" : result;
    }


    public static String getMacAddress(Context context) {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        while (interfaces.hasMoreElements()) {
            NetworkInterface netWork = interfaces.nextElement();

            if (!netWork.getName().equals("wlan0")) {
                continue;
            }

            byte[] by = new byte[0];
            try {
                by = netWork.getHardwareAddress();
            } catch (SocketException e) {
                e.printStackTrace();
            }
            if (by == null || by.length == 0) {
                continue;
            }
            StringBuilder builder = new StringBuilder();
            for (byte b : by) {
                builder.append(String.format("%02X:", b));
            }

            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }

            return builder.toString();
        }
        return "";
    }


    public static String getNetConnectType(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMan!=null){
            NetworkInfo networkInfo = conMan.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.getTypeName();
            }
        }
        return "";
    }

    public static String getIPAddress(Context context) {
        String ip = "";
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // mobile 3G Data Network
        NetworkInfo mobileNetWorkInfo = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
        if (mobileNetWorkInfo != null) {
            NetworkInfo.State state = mobileNetWorkInfo.getState();
            if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                ip = getLocalIpAddress();
                return ip;
            }
        }

        // wifi
        NetworkInfo wifiNetWordInfo = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetWordInfo != null) {
            NetworkInfo.State state = wifiNetWordInfo.getState();
            if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                //获取wifi服务
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                //判断wifi是否开启
                if (wifiManager.isWifiEnabled()) {
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    int ipAddress = wifiInfo.getIpAddress();
                    ip = (ipAddress & 0xFF) + "." +
                            ((ipAddress >> 8) & 0xFF) + "." +
                            ((ipAddress >> 16) & 0xFF) + "." +
                            (ipAddress >> 24 & 0xFF);
                }
            }
        }

        return ip;
    }

    /**
     * @return 手机GPRS网络的IP
     */
    private static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {//获取IPv4的IP地址
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }
}
