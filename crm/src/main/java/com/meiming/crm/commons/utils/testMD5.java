package com.meiming.crm.commons.utils;

import sun.security.provider.MD5;

public class testMD5 {
    public static void main(String[] args) {
        String lsmd5=MD5Util.getMD5("ls");
        System.out.println(lsmd5);

    }
}
