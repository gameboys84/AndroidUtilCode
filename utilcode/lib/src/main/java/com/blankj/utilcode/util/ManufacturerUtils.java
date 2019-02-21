package com.blankj.utilcode.util;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by niqingfeng on 2019/01/31.
 * 设备兼容代码来源 ： https://blog.csdn.net/donkor_/article/details/79374442
 */
public final class ManufacturerUtils {

    private static final String TAG = "ManufacturerUtils";

    static boolean launchAppDetailsSettings() {
        String platformName = Build.MANUFACTURER;
        Log.i(TAG, "launcher manufacturer : " + platformName);

        switch (platformName)
        {
            case "Xiaomi":
                return goXiaoMiMainager();
            case "HUAWEI":
                return goHuaWeiMainager();
            case "vivo":
                return goVivoMainager();
            case "OPPO":
                return goOppoMainager();
            case "Coolpad":
                return goCoolpadMainager();
            case "Meizu":
                return goMeizuMainager();
            case "samsung":
                return goSangXinMainager();
            case "Sony":
                return goSonyMainager();
            case "LG":
                return goLGMainager();
            default:
                return launchAppDefaultSettings();
        }
    }

    private static boolean goLGMainager() {
        try {
            String packageName = Utils.getApp().getPackageName();
            Intent intent = new Intent(packageName);
            ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
            intent.setComponent(comp);
            Utils.getApp().startActivity(intent);

            return true;
        } catch (Exception e) {
            //Toast.makeText(mContext, "跳转失败", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return launchAppDefaultSettings();
        }
    }
    private static boolean goSonyMainager() {
        try {
            String packageName = Utils.getApp().getPackageName();
            Intent intent = new Intent(packageName);
            ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
            intent.setComponent(comp);
            Utils.getApp().startActivity(intent);
            

            return true;
        } catch (Exception e) {
            //Toast.makeText(mContext, "跳转失败", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return launchAppDefaultSettings();
        }
    }

    private static boolean goMeizuMainager() {
        try {
            String packageName = Utils.getApp().getPackageName();
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", packageName);
            Utils.getApp().startActivity(intent);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return launchAppDefaultSettings();
        }
    }

    private static boolean goSangXinMainager() {
        //三星4.3可以直接跳转
        return launchAppDefaultSettings();
    }

    /**
     * doStartApplicationWithPackageName("com.yulong.android.security:remote")
     * 和Intent open = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
     * startActivity(open);
     * 本质上没有什么区别，通过Intent open...打开比调用doStartApplicationWithPackageName方法更快，也是android本身提供的方法
     */
    private static boolean goCoolpadMainager() {
        return doStartApplicationWithPackageName("com.yulong.android.security:remote");
      /*  Intent openQQ = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
        startActivity(openQQ);*/
    }

    private static boolean goVivoMainager() {
        return doStartApplicationWithPackageName("com.bairenkeji.icaller");
     /*   Intent openQQ = getPackageManager().getLaunchIntentForPackage("com.vivo.securedaemonservice");
        startActivity(openQQ);*/
    }

    private static String getMiuiVersion() {
        String propName = "ro.miui.ui.version.name";
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(
                    new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return line;
    }

    private static boolean goXiaoMiMainager() {
        String rom = getMiuiVersion();
        Log.i(TAG, "goMiaoMiMainager --- rom : "+rom);

        String packageName = Utils.getApp().getPackageName();
        Intent intent=new Intent();
        if ("V6".equals(rom) || "V7".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            intent.putExtra("extra_pkgname", packageName);
        } else if ("V8".equals(rom) || "V9".equals(rom) || "V10".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            intent.putExtra("extra_pkgname", packageName);
        } else {
            return launchAppDefaultSettings();
        }
        Utils.getApp().startActivity(intent);
        return true;
    }

    private static boolean goHuaWeiMainager() {
        try {
            String packageName = Utils.getApp().getPackageName();
            Intent intent = new Intent(packageName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
            intent.setComponent(comp);
            Utils.getApp().startActivity(intent);

            return true;
        } catch (Exception e) {
            //Toast.makeText(mContext, "跳转失败", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return launchAppDefaultSettings();
        }
    }

    private static boolean goOppoMainager() {
        return doStartApplicationWithPackageName("com.coloros.safecenter");
    }


    private static boolean doStartApplicationWithPackageName(String packageName) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = Utils.getApp().getPackageManager().getPackageInfo(packageName, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (packageinfo == null) {
            return false;
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);
        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = Utils.getApp().getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        Log.e(TAG, "resolveinfoList" + resolveinfoList.size());
        for (int i = 0; i < resolveinfoList.size(); i++) {
            Log.e(TAG, resolveinfoList.get(i).activityInfo.packageName + resolveinfoList.get(i).activityInfo.name);
        }
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packageName参数2 = 参数 packname
            String packageName2 = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packageName参数2.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            // 设置ComponentName参数1:packageName参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName2, className);
            intent.setComponent(cn);
            try {
                Utils.getApp().startActivity(intent);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return launchAppDefaultSettings();
            }
        }

        return  false;
    }

    private static boolean launchAppDefaultSettings() {
        Log.i(TAG, "launcher appDefaultSettings");
        return false;
    }

}
