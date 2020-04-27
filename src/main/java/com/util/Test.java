package com.util;

public class Test {

	/**
	* 加密，把一个字符串在原有的基础上+1
	* @param data 需要解密的原字符串
	* @return 返回解密后的新字符串
	*/
	 public static String encode(String data) {
	      byte[] b = data.getBytes();
	      for(int i=0;i<b.length;i++) {
	          b[i] += 1;//在原有的基础上+1
	      }
	      return new String(b);
	  }
}
