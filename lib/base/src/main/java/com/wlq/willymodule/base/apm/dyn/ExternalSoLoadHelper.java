package com.wlq.willymodule.base.apm.dyn;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;

import com.wlq.willymodule.base.apm.dyn.util.ReflectUtils;
import com.wlq.willymodule.base.util.LogUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class ExternalSoLoadHelper {
    private static final String TAG = ExternalSoLoadHelper.class.getSimpleName();

    private static File lastSoDir = null;

    /**
     * 清除so路径，实际上是设置一个无效的path，用户测试无so库的情况
     *
     * @param classLoader
     */
    public static void clearSoPath(ClassLoader classLoader) {
        try {
            final String testDirNoSo = Environment.getExternalStorageDirectory().getAbsolutePath() + "/clearSo/";
            new File(testDirNoSo).mkdirs();
            installNativeLibraryPath(classLoader, testDirNoSo);
        } catch (Throwable throwable) {
            LogUtils.e(TAG, "clear path error" + throwable.toString());
            throwable.printStackTrace();
        }
    }

    public static synchronized boolean installNativeLibraryPath(ClassLoader classLoader, String folderPath) throws Throwable {
        return installNativeLibraryPath(classLoader, new File(folderPath));
    }

    public static synchronized boolean installNativeLibraryPath(ClassLoader classLoader, File folder) throws Throwable {
        if (classLoader == null || folder == null || !folder.exists()) {
            LogUtils.e(TAG, "classLoader or folder is illegal " + folder);
            return false;
        }
        final int sdkInt = Build.VERSION.SDK_INT;
        final boolean aboveM = (sdkInt == 25 && getPreviousSdkInt() != 0) || sdkInt > 25;
        if (aboveM) {
            V25.install(classLoader, folder);
        } else if (sdkInt >= 23) {
            V23.install(classLoader, folder);
        } else if (sdkInt >= 14) {
            V14.install(classLoader, folder);
        }
        lastSoDir = folder;
        return true;
    }

    private static final class V23 {
        private static void install(ClassLoader classLoader, File folder) throws Throwable {
            Field pathListField = ReflectUtils.findField(classLoader, "pathList");
            Object dexPathList = pathListField.get(classLoader);

            Field nativeLibraryDirectories = ReflectUtils.findField(dexPathList, "nativeLibraryDirectories");
            List<File> libDirs = (List<File>) nativeLibraryDirectories.get(dexPathList);

            //去重
            if (libDirs == null) {
                libDirs = new ArrayList<>(2);
            }
            final Iterator<File> libDirIt = libDirs.iterator();
            while (libDirIt.hasNext()) {
                final File libDir = libDirIt.next();
                if (folder.equals(libDir) || folder.equals(lastSoDir)) {
                    libDirIt.remove();
                    LogUtils.d(TAG, "libDirIt.remove() " + folder.getAbsolutePath());
                    break;
                }
            }

            libDirs.add(0, folder);
            Field systemNativeLibraryDirectories =
                    ReflectUtils.findField(dexPathList, "systemNativeLibraryDirectories");
            List<File> systemLibDirs = (List<File>) systemNativeLibraryDirectories.get(dexPathList);

            //判空
            if (systemLibDirs == null) {
                systemLibDirs = new ArrayList<>(2);
            }
            LogUtils.d(TAG, "systemLibDirs,size=" + systemLibDirs.size());

            Method makePathElements = ReflectUtils.findMethod(dexPathList, "makePathElements", List.class, File.class, List.class);
            ArrayList<IOException> suppressedExceptions = new ArrayList<>();
            libDirs.addAll(systemLibDirs);

            Object[] elements = (Object[]) makePathElements.invoke(dexPathList, libDirs, null, suppressedExceptions);
            Field nativeLibraryPathElements = ReflectUtils.findField(dexPathList, "nativeLibraryPathElements");
            nativeLibraryPathElements.setAccessible(true);
            nativeLibraryPathElements.set(dexPathList, elements);
        }
    }

    /**
     * 把自定义的native库path插入nativeLibraryDirectories最前面，即使安装包libs目录里面有同名的so，也优先加载指定路径的外部so
     */
    private static final class V25 {
        private static void install(ClassLoader classLoader, File folder) throws Throwable {
            Field pathListField = ReflectUtils.findField(classLoader, "pathList");
            Object dexPathList = pathListField.get(classLoader);
            Field nativeLibraryDirectories = ReflectUtils.findField(dexPathList, "nativeLibraryDirectories");

            List<File> libDirs = (List<File>) nativeLibraryDirectories.get(dexPathList);
            //去重
            if (libDirs == null) {
                libDirs = new ArrayList<>(2);
            }
            final Iterator<File> libDirIt = libDirs.iterator();
            while (libDirIt.hasNext()) {
                final File libDir = libDirIt.next();
                if (folder.equals(libDir) || folder.equals(lastSoDir)) {
                    libDirIt.remove();
                    LogUtils.d(TAG, "libDirIt.remove()" + folder.getAbsolutePath());
                    break;
                }
            }

            libDirs.add(0, folder);
            //system/lib
            Field systemNativeLibraryDirectories = ReflectUtils.findField(dexPathList, "systemNativeLibraryDirectories");
            List<File> systemLibDirs = (List<File>) systemNativeLibraryDirectories.get(dexPathList);

            //判空
            if (systemLibDirs == null) {
                systemLibDirs = new ArrayList<>(2);
            }
            LogUtils.d(TAG, "systemLibDirs,size=" + systemLibDirs.size());

            Method makePathElements = ReflectUtils.findMethod(dexPathList, "makePathElements", List.class);
            libDirs.addAll(systemLibDirs);

            Object[] elements = (Object[]) makePathElements.invoke(dexPathList, libDirs);
            Field nativeLibraryPathElements = ReflectUtils.findField(dexPathList, "nativeLibraryPathElements");
            nativeLibraryPathElements.setAccessible(true);
            nativeLibraryPathElements.set(dexPathList, elements);
        }
    }

    private static final class V14 {
        private static void install(ClassLoader classLoader, File folder) throws Throwable {
            Field pathListField = ReflectUtils.findField(classLoader, "pathList");
            Object dexPathList = pathListField.get(classLoader);
            ReflectUtils.expandFieldArray(dexPathList, "nativeLibraryDirectories", new File[]{folder});
        }
    }

    /**
     * 部分机型删了该成员属性，兼容
     *
     * @return 被厂家删了返回1，否则正常读取
     */
    @TargetApi(Build.VERSION_CODES.M)
    private static int getPreviousSdkInt() {
        try {
            return Build.VERSION.PREVIEW_SDK_INT;
        } catch (Throwable ignore) {
        }
        return 1;
    }

}
