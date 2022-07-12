package com.wlq.willymodule.base.apm.dyn.util;

import android.text.TextUtils;
import java.io.InputStream;
import java.security.MessageDigest;

public class DynamicSoUtils {

    public static String sha256Hex(InputStream data) {
        return hashHex(data, "SHA-256");
    }

    public static String hashHex(InputStream data, String type) {
        if (data != null && !TextUtils.isEmpty(type)) {
            byte[] buffer = new byte[4096];
            try {
                MessageDigest hash = MessageDigest.getInstance(type);
                int length;
                while ((length = data.read(buffer)) != -1) {
                    hash.update(buffer, 0, length);
                }
                return bytes2String(hash.digest());
            } catch (Exception var9) {
                var9.printStackTrace();
            }
        }
        return null;
    }

    private static String bytes2String(byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            StringBuilder hexString = new StringBuilder();
            for (byte aByte : bytes) {
                String t = Integer.toHexString(255 & aByte);
                if (t.length() == 1) {
                    hexString.append("0").append(t);
                } else {
                    hexString.append(t);
                }
            }

            return hexString.toString();
        }
    }
}
