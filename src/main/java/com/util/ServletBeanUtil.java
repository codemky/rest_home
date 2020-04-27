package com.util;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**Servlet业务中实体工具
 * @author LiuDing
 * 2014-2-16-下午08:10:06
 */
public class ServletBeanUtil {

    /**自动匹配参数赋值到实体bean中
     * @author LiuDing
     * 2014-2-16 下午10:23:37
     * @param bean
     * @param request
     */
    public static void populate(Object bean, HttpServletRequest request){


        Class<? extends Object> clazz = bean.getClass();
        Method[] ms = clazz.getDeclaredMethods();
        String mname;
        String field;
        String fieldType;
        String value;
        for(Method m : ms){
            mname = m.getName();
            if(!mname.startsWith("set")
                    || ArrayUtils.isEmpty(m.getParameterTypes())){
                continue;
            }
            try{
                field = mname.toLowerCase().charAt(3) + mname.substring(4, mname.length());
                value = request.getParameter(field);
                if(StringUtils.isEmpty(value)){
                    continue;
                }
                fieldType = m.getParameterTypes()[0].getName();
                //以下可以确认value为String类型
                if(String.class.getName().equals(fieldType)){
                    m.invoke(bean, value);
                } else if (Integer.class.getName().equals(fieldType) && NumberUtils.isDigits(value)) {
                    m.invoke(bean, Integer.valueOf(value));
                } else if (Short.class.getName().equals(fieldType) && NumberUtils.isDigits(value)) {
                    m.invoke(bean, Short.valueOf(value));
                } else if (Float.class.getName().equals(fieldType) && NumberUtils.isCreatable(value)) {
                    m.invoke(bean, Float.valueOf(value));
                } else if (Double.class.getName().equals(fieldType) && NumberUtils.isCreatable(value)) {
                    m.invoke(bean, Double.valueOf(value));
                }else if(Long.class.getName().equals(fieldType)){
                    m.invoke(bean, Long.valueOf(value));
                }else if(Date.class.getName().equals(fieldType)){
                    m.invoke(bean, DateUtils.parseDate(value, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"));
                }else if(LocalDateTime.class.getName().equals(fieldType)){
                    m.invoke(bean, LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }else{
                    //找不到类型处理方法，什么都不做。。
                    System.out.println("无法处理的类型:" + fieldType);
                }
            }catch(Exception e){
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * Author: mokuanyuan
     *
     * @apiNote: 获取request中的员工数组
     * @since: 2019/10/16 9:54
     */
    public static List<Long> populateEmployeeIds(HttpServletRequest request) {
        String employIds = "employeeIds";
        if (request.getParameterValues(employIds) != null) {
            return Arrays.stream(request.getParameterValues(employIds)).filter(x -> !x.isEmpty())
                    .map(Long::parseLong).collect(Collectors.toList());
        } else {
            return null;
        }

    }

    /**
     * Author: mokuanyuan
     *
     * @apiNote: 返回模糊查询字符串
     * @since: 2019/10/16 9:54
     */
    public static String getVague(String str) {
        if (str != null) {
            return "%" + str + "%";
        } else {
            return null;
        }
    }

}