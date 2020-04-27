package com.util;

public class Encryption {

	public static String decrypt(String str) {
		byte[] b = str.getBytes();
        for(int i=0;i<b.length;i++) {
            b[i] -= 1;
        }
        return new String(b);
    }
}
