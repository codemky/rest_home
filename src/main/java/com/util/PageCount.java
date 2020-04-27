package com.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 根据当前页和页容量计算sql分页查询的起始位置, curPage 当前页码, pageSize 页容量,
 *         startRecord 分页查询的起始位置
 */
public class PageCount {
	public static Map<String, Integer> selectRecordByLimit(int curPage, int pageSize) {
		Map<String, Integer> map = new HashMap<String, Integer>();

		
		//int result = DateCompare.compareDate(Encryption.decrypt("312:162:")); if (1
		// == result) { return map; }
		

		if (curPage < 1) {
            curPage = 1;
        }
		if (pageSize < 0) {
            pageSize = 10;
        }
		int startRecord = (curPage - 1) * pageSize; // 从哪条记录开始
		map.put("startRecord", startRecord);
		map.put("pageSize", pageSize);
		return map;
	}
}
