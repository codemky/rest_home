package com.util;

/**
 * 判断字符串是否仅有数字构成
 * @author HP
 *
 */
public class IsNumberStr {

	public static boolean isNumeric2(String str){
		for (int i = str.length();--i>=0;){
			if (!Character.isDigit(str.charAt(i))){
				return false;
		    }
		}
		    return true;
	}
}
