package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

public class JsonHandler {

	public static String readJsonFromRequest(HttpServletRequest req) {
		StringBuffer jsonBuf = new StringBuffer();
		char[] buf = new char[1024];
		int len = -1;
		try {
			BufferedReader reader = req.getReader();
			while ((len = reader.read(buf)) != -1) {
				jsonBuf.append(new String(buf, 0, len));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonBuf.toString();
	}

	public static String getpath(String value) {
		JsonHandler jsonHandler = new JsonHandler();
		return jsonHandler.read(value, 2);
	}

	public static Map<String, Object> writeJsontoResponse(int code,
			Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> info = new HashMap<String, Object>();
		JsonHandler jsonHandler = new JsonHandler();
		info.put("codeinfo", jsonHandler.read(String.valueOf(code), 1));
		info.put("info", obj);
		map.put("code", code);
		map.put("result", info);
		return map;
	}

	public String read(String name, int type) {
		try {
			InputStream in = this.getClass().getResourceAsStream(
					"/code.properties");
			Properties p = new Properties();
			p.load(in);
			String ret = p.getProperty(name, "");
			in.close();
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
}
