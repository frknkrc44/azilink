package org.lfx.azilink;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.RouteInfo;
import android.os.Build;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Reflection {
    private Reflection() {}

    @SuppressLint("PrivateApi")
    public static List<CharSequence> getSystemDNS() {
        List<CharSequence> allDNSServers = new ArrayList<>();

        // Try to pull System DNS from net.dns1 variable
        // It's valid until Oreo
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            try {
                Class<?> clazz = Class.forName("android.os.SystemProperties");
                Method getMethod = clazz.getDeclaredMethod("get", String.class);
                getMethod.setAccessible(true);
                for (int i = 1;i < 5;i++) {
                    allDNSServers.add((String) getMethod.invoke(null, "net.dns" + i));
                }
            } catch (Throwable ignored) {}

            addFallbackDNSAddresses(allDNSServers);
            return allDNSServers;
        }

        /*
         * Thanks:
         * https://stackoverflow.com/a/48973823
         */
        ConnectivityManager connectivityManager = (ConnectivityManager)
                AziLinkApplication.getCtx().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            // List all current networks
            for (Network network : connectivityManager.getAllNetworks()) {
                // Pull network info from Network object
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
                // Log.d(AziLinkApplication.getLogTag(), "Network type: " + networkInfo.getTypeName());

                // If network is connected, pull DNS info from LinkProperties object
                if (networkInfo.isConnected()) {
                    LinkProperties linkProperties = connectivityManager.getLinkProperties(network);
                    List<InetAddress> dnsServersList = linkProperties.getDnsServers();

                    if (!linkPropertiesHasDefaultRoute(linkProperties)) {
                        for (InetAddress element : dnsServersList) {
                            allDNSServers.add(element.getHostAddress());
                        }
                    }
                    else {
                        for (InetAddress element: dnsServersList) {
                            allDNSServers.add(element.getHostAddress());
                        }
                    }
                }
            }
        }

        addFallbackDNSAddresses(allDNSServers);
        return allDNSServers;
    }

    private static void addFallbackDNSAddresses(List<CharSequence> dns) {
        addDNSIfNotExists(dns, "9.9.9.9");
        addDNSIfNotExists(dns, "1.1.1.1");
        addDNSIfNotExists(dns, "1.0.0.1");
        addDNSIfNotExists(dns, "8.8.8.8");
        addDNSIfNotExists(dns, "8.8.4.4");
    }

    private static void addDNSIfNotExists(List<CharSequence> dns, CharSequence target) {
        if (!dns.contains(target)) {
            dns.add(target);
        }
    }

    @TargetApi(21)
    private static boolean linkPropertiesHasDefaultRoute(LinkProperties linkProperties) {
        for (RouteInfo route : linkProperties.getRoutes()) {
            if (route.isDefaultRoute()) {
                return true;
            }
        }
        return false;
    }
}
