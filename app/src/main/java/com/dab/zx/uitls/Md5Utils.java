package com.dab.zx.uitls;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by 八神火焰 on 2017/2/14.
 */

public class Md5Utils {
    public static byte[] removeMd5(byte[] bytes) {
        byte[] checkBytes = new byte[bytes.length - 2];
        System.arraycopy(bytes, 0, checkBytes, 0, bytes.length - 2);
        return checkBytes;
    }

    /**
     * 获取一个Byte数组的Md5值
     *
     * @param bytes 计算的Byte数组
     * @return 返回计算结果的前4位字符串
     */
    public static byte[] calculate(byte[] bytes) {
        byte[] md5 = new byte[2];
        try {
            byte[] temp = MessageDigest.getInstance("MD5").digest(bytes);
            System.arraycopy(temp, 0, md5, 0, 2);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }

    /**
     * 检查Byte数组的Md5是否正确
     *
     * @param bytes 检查的Byte数组
     * @return true|false
     */
    public static boolean check(byte[] bytes) {
        byte[] checkBytes = new byte[bytes.length - 2];
        byte[] md5Bytes   = new byte[2];
        System.arraycopy(bytes, 0, checkBytes, 0, bytes.length - 2);
        System.arraycopy(bytes, bytes.length - 2, md5Bytes, 0, 2);
        return TextUtils.equals(Arrays.toString(calculate(checkBytes)), Arrays.toString(md5Bytes));
    }

    /**
     * 合并两个Byte数组为一个新的Byte数组
     *
     * @param bytes1 第一个Byte数组
     * @param bytes2 第二个Byte数组
     * @return 合并后的Byte数组
     */
    public static byte[] combineByte(byte[] bytes1, byte[] bytes2) {
        byte[] finalBytes = new byte[bytes1.length + bytes2.length];
        System.arraycopy(bytes1, 0, finalBytes, 0, bytes1.length);
        System.arraycopy(bytes2, 0, finalBytes, bytes1.length, bytes2.length);
        return finalBytes;
    }
}
