package com.wlq.willymodule.base.apm.dyn

import android.annotation.SuppressLint
import android.os.Environment
import com.wlq.willymodule.base.apm.dyn.bean.DynamicSoInfo
import com.wlq.willymodule.base.apm.dyn.callback.DynamicSoCallback
import com.wlq.willymodule.base.apm.dyn.util.*
import com.wlq.willymodule.base.ext.I
import com.wlq.willymodule.base.util.*
import java.io.*

class ExternalSoLoadManager {

    @Synchronized
    fun clearSo() {
        try {
            val testDirNoSo = Environment.getExternalStorageDirectory().absolutePath + "/clearSo/"
            File(testDirNoSo).mkdirs()
            ExternalSoLoadHelper.installNativeLibraryPath(Utils.getApp().classLoader, testDirNoSo)
        } catch (throwable: Exception) {
            LogUtils.e(TAG, "clear so path error${throwable.message}")
        }
    }

    @Synchronized
    fun checkAndLoadSo(info: DynamicSoInfo, callback: DynamicSoCallback) {
        synchronized(mSyncObject) {
            if (sCallbackMap.containsKey(info)) {
                var callbacks = sCallbackMap[info]
                if (callbacks == null) {
                    callbacks = ArrayList()
                }
                callbacks.add(callback)
                sCallbackMap[info] = callbacks
            } else {
                val callbacks = ArrayList<DynamicSoCallback>()
                callbacks.add(callback)
                sCallbackMap[info] = callbacks

                ensureSoDirs(info)
                val exist = checkSoExist(info)
                if (exist) {
                    if (verifySoHash(info)) {
                        info.path = getSoFilePath(info)
                        if (callbacks.isNotEmpty()) {
                            for (cb in callbacks) {
                                cb.onSuccess(info)
                            }
                        }
                        sCallbackMap.remove(info)
                    } else {
                        FileUtils.delete(getSoFilePath(info))
                        FileUtils.delete(getSoZipPath(info))
                        downloadSo(info, callback)
                    }
                } else {
                    downloadSo(info, callback)
                }
            }
        }
    }

    @SuppressLint("UnsafeDynamicallyLoadedCode")
    fun loadExternalSo(info: DynamicSoInfo): Boolean {
        val nativeLibPathFile = File(getSoFileDir(info))
        try {
            val result = ExternalSoLoadHelper.installNativeLibraryPath(Utils.getApp().classLoader, nativeLibPathFile)
            if (result) {
                val dir = File(getSoFileDir(info))
                val currentFiles: Array<File> = dir.listFiles() as Array<File>
                if (currentFiles.isEmpty()) {
                    LogUtils.e(TAG, "loadExternal so failed: so files is empty...")
                    return false
                }
                for (currentFile in currentFiles) {
                    LogUtils.i(TAG, "loadExternalSo path:${currentFile.absolutePath}")
                    System.load(currentFile.absolutePath)
                }
                return true
            }
        } catch (e: Exception) {
            LogUtils.e(TAG, "loadExternal so failed:${e.message}")
        }
        return false
    }

    private fun downloadSo(info: DynamicSoInfo, callback: DynamicSoCallback) {
        ThreadUtils.executeByIo(object : ThreadUtils.SimpleTask<Boolean>() {

            override fun doInBackground(): Boolean {
                val result = downloadSoFile(info, callback)
                var downloadSuccess = false
                if (result) {
                    downloadSuccess = verifyFile(info)
                } else {
                    LogUtils.e(TAG, "downloadSoFile failed...")
                }
                return downloadSuccess
            }

            override fun onSuccess(result: Boolean) {
                synchronized(mSyncObject) {
                    val callbacks = sCallbackMap[info]
                    if (!callbacks.isNullOrEmpty()) {
                        for (cb in callbacks) {
                            if (result) {
                                cb.onSuccess(info)
                            } else {
                                cb.onFail()
                            }
                        }
                    } else {
                        if (result) {
                            callback.onSuccess(info)
                        } else {
                            callback.onFail()
                        }
                    }
                    sCallbackMap.remove(info)
                }
            }
        })
    }

    private fun verifyFile(info: DynamicSoInfo): Boolean {
        if (isZipFile(info)) {
            if (verifyZipFile(info)) {
                val dir = getSoFileDir(info)
                val name = info.hash + ZIP_SUFFIX
                val unzip = ZipUtils.unzipFile(dir + name, dir)
                if (unzip.isNotEmpty()) {
                    if (verifySoHash(info)) {
                        info.path = getSoFilePath(info)
                        return true
                    } else {
                        FileUtils.delete(getSoFilePath(info))
                        LogUtils.e(TAG, "verifySoHash failed...")
                    }
                } else {
                    FileUtils.delete(getSoZipPath(info))
                    LogUtils.e(TAG, "unzip failed...")
                }
            } else {
                FileUtils.delete(getSoZipPath(info))
                LogUtils.e(TAG, "verifyZipFile failed...")
            }
        } else {
            if (verifySoFile(info)) {
                if (verifySoHash(info)) {
                    info.path = getSoFilePath(info)
                    return true
                } else {
                    FileUtils.delete(getSoFilePath(info))
                    LogUtils.e(TAG, "verifySoHash failed...")
                }
            } else {
                FileUtils.delete(getSoFilePath(info))
                LogUtils.e(TAG, "verifySoFile failed...")
            }
        }
        return false
    }

    private fun downloadSoFile(info: DynamicSoInfo, callback: DynamicSoCallback): Boolean {
        return if (RegexUtils.isURL(info.url)) {
            //TODO so资源来源于网络，先将so下载至sdcard，再校验，再将so拷贝至私有目录，最后加载(System.load())
            callback.onProgress(100)
            true
        } else {
            //TODO so资源来源于sdcard，先校验，再将so拷贝至私有目录，最后加载(System.load())
            FileUtils.copy(info.url, getSoFilePath(info))
        }
    }

    private fun checkSoExist(info: DynamicSoInfo): Boolean {
        val soFile = File(getSoFilePath(info))
        return soFile.exists()
    }

    private fun ensureSoDirs(info: DynamicSoInfo) {
        val dir = Utils.getApp().filesDir
        val dyn = File(dir, ROOT_DIR_NAME)
        if (!dyn.exists()) {
            dyn.mkdir()
        }
        val name = File(dyn, info.name)
        if (!name.exists()) {
            name.mkdir()
        }
        val abi = File(name, info.abi)
        if (!abi.exists()) {
            abi.mkdir()
        }
        val hash = File(abi, info.hash)
        if (!hash.exists()) {
            hash.mkdir()
        }
    }

    private fun getRootPath(): String {
        val dir = Utils.getApp().filesDir
        return dir.absolutePath + File.separator + ROOT_DIR_NAME
    }

    private fun getNativeLibInstallPath(info: DynamicSoInfo): String {
        return getRootPath() + File.separator + info.name + File.separator
    }

    private fun getSoFileDir(info: DynamicSoInfo): String {
        return getRootPath() + File.separator +
                info.name + File.separator +
                info.abi + File.separator +
                info.hash + File.separator
    }

    private fun getSoZipPath(info: DynamicSoInfo): String {
        return getSoFileDir(info) + info.hash + ZIP_SUFFIX
    }

    private fun getSoFilePath(info: DynamicSoInfo): String {
        return getSoFileDir(info) + info.libName
    }

    private fun verifyZipFile(info: DynamicSoInfo): Boolean {
        val file = File(getSoZipPath(info))
        return info.size === file.length()
    }

    private fun verifySoFile(info: DynamicSoInfo): Boolean {
        val file = File(getSoFilePath(info))
        I("verifySoFile--->info.size:${info.size},file.length:${file.length()}")
        return info.size === file.length()
    }

    private fun verifySoHash(info: DynamicSoInfo): Boolean {
        val destHash = sha256(info)
        return info.hash.equals(destHash, true)
    }

    private fun sha256(info: DynamicSoInfo): String? {
        val filename = getSoFilePath(info)
        var fis: InputStream? = null
        try {
            fis = FileInputStream(filename)
            return DynamicSoUtils.sha256Hex(fis)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            if (fis != null) {
                try {
                    fis.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    private fun isZipFile(info: DynamicSoInfo): Boolean {
        return info.url.endsWith(ZIP_SUFFIX)
    }

    companion object {
        private val TAG = ExternalSoLoadManager::class.java.simpleName
        private const val ROOT_DIR_NAME = "dynamic"
        private const val ZIP_SUFFIX = ".zip"
        private val sCallbackMap: MutableMap<DynamicSoInfo, MutableList<DynamicSoCallback>> =
            HashMap()
        private val mSyncObject = Any()
    }
}