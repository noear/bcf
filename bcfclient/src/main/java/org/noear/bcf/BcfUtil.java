package org.noear.bcf;

import org.noear.bcf.models.BcfResourceModel;

import java.security.MessageDigest;

public  class BcfUtil {
    public static String sha1(String cleanData) {
        return hashEncode("SHA-1", cleanData);
    }

    public static String md5(String cleanData) {
        return hashEncode("MD5", cleanData);
    }

    private static String hashEncode(String algorithm, String cleanData) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = cleanData.getBytes("UTF-16LE");
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance(algorithm);
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //================

    public static String buildBcfPassWd(String userId, String passWd) {
        return BcfUtil.sha1(userId + "#" + passWd.trim());
    }

    //================

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    //================

    public static String buildBcfUnipath(BcfResourceModel res) {
        if (res == null || isEmpty(res.uri_path)) {
            return "";
        } else {
            if (res.uri_path.indexOf("/$") > 0) {
                if (res.tags == null || res.tags.indexOf("@=") < 0) {
                    return res.uri_path;
                } else {
                    return res.uri_path + "?@=";
                }
            } else {
                if (res.tags == null || res.tags.indexOf("@=") < 0) {
                    return res.uri_path + "/@" + res.cn_name;
                } else {
                    return res.uri_path + "/@" + res.cn_name + "?@=";
                }
            }
        }
    }
}
