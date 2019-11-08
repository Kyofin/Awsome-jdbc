package com.github.huzekang.jdbcservice.util;

import cn.hutool.crypto.SecureUtil;

/**
 * @author lijiakai@gz-yibo.com
 * @description md5密码加密器
 * @date 2019-10-08
 */
public class MD5Util {
    private static final String salt = "a7714778b5524543a04c87811cffa88a";

    public MD5Util() {
    }

    public static String encode(String src) {
        return SecureUtil.md5(src);
    }

    public static String encodePassword(String password) {
        return encode(password + salt);
    }
}
