package com.skynet.spider.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class PropUtil {

	/**
    * 更新（或插入）一对properties信息(主键及其键值)
    * 如果该主键已经存在，更新该主键的值；
    * 如果该主键不存在，则插件一对键值。
    * @param keyname 键名
    * @param keyvalue 键值
    */
    public static void writeProperties(String profilepath, Map<String, String> map) {       
        Properties props = new Properties();
    	try {
            // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
            OutputStream fos = new FileOutputStream(profilepath);
            for(Entry<String, String> entry : map.entrySet()) {
            	props.setProperty(entry.getKey(), entry.getValue());
            }
            // 以适合使用 load 方法加载到 Properties 表中的格式，
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流
            props.store(fos, null);
        } catch (IOException e) {
            System.err.println("属性文件更新错误");
        }
    }
    
    public static void updateProperties(String profilepath, String keyname,String keyvalue) {
    	Properties props = new Properties();
    	try {
            props.load(new FileInputStream(profilepath));
            // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
            OutputStream fos = new FileOutputStream(profilepath);           
            props.setProperty(keyname, keyvalue);
            // 以适合使用 load 方法加载到 Properties 表中的格式，
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流
            props.store(fos, "Update '" + keyname + "' value");
        } catch (IOException e) {
            System.err.println("属性文件更新错误");
        }
    }
    
    public static void updateProperties(String profilepath, Map<String, String> map) {
    	Properties props = new Properties();
    	try {
            props.load(new FileInputStream(profilepath));
            // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
            OutputStream fos = new FileOutputStream(profilepath); 
            for(Entry<String, String> entry : map.entrySet()) {
            	props.setProperty(entry.getKey(), entry.getValue());
            }
            // 以适合使用 load 方法加载到 Properties 表中的格式，
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流
            props.store(fos, "");
        } catch (IOException e) {
        	e.printStackTrace();
            System.err.println("属性文件更新错误");
        }
    }

    /**
     * 根据主键key读取主键的值value
     * @param filePath 属性文件路径
     * @param key 键名
     */
     @SuppressWarnings("unchecked")
	public static Map<String, String> readValue(String filePath) {
         Properties props = new Properties();
         Map<String, String> map = new HashMap<String, String>();
         try {
             InputStream in = new BufferedInputStream(new FileInputStream(
                     filePath));
             props.load(in);
             return new HashMap<String, String>((Map)props);
         } catch (Exception e) {
             e.printStackTrace();
             return null;
         }
     }
    
    public static void main(String[] args) {
    	Map<String, String> map = readValue("conf/company.properties");
    	System.out.println(map);
	}
}
