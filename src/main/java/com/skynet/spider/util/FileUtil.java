package com.skynet.spider.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.skynet.spider.common.Constants;

public class FileUtil {

	public static String readFile(String path) throws Exception{
		String encoding="GBK";
        File file=new File(path);
        StringBuffer sb = new StringBuffer();
        if(file.isFile() && file.exists()){ //判断文件是否存在
            InputStreamReader read = new InputStreamReader(
            new FileInputStream(file),encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while((lineTxt = bufferedReader.readLine()) != null){
                System.out.println(lineTxt);
                sb.append(lineTxt);
            }
            read.close();
        }
        return sb.toString();
	}
	
	public static void createDir(String fileName) {
		File file = new File(fileName);
		if(!file.exists()) {
			file.mkdirs();
		}
	}
	
	public static List<List<String>> readCSVFile(String path) {
		List<List<String>> data = new ArrayList<List<String>>();
		try {
			File file = new File(path);
			BufferedReader br= new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));  
	        // 读取直到最后一行 
	        String line = ""; 
	        while ((line = br.readLine()) != null) { 
	        	List<String> row = new ArrayList<String>();
	        	// 把一行数据分割成多个字段 
	            StringTokenizer st = new StringTokenizer(line, "	");
	            while (st.hasMoreTokens()) { 
	            	String value = st.nextToken();
	            	// 每一行的多个字段用TAB隔开表示 
//	                System.out.print(value + "/t"); 
	                row.add(value);
	            } 
//	            System.out.println();
	            data.add(row);
	        } 
	        br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	public static void rename(String path) {
		String[] fileNames = new File(path).list();
		for(String fileName : fileNames) {
			String newName = fileName.replace("(", "_").replace(")", "");
			System.out.println("oldName:" + fileName + ", newName:" + newName);
			File file = new File(path + fileName);
			if(file.exists()) {
				System.out.println(fileName + "rename to" + newName);
				file.renameTo(new File(path + newName));
			}
		}
	}
	
	
	public static void main(String[] args) throws Exception{
//		readFile("conf/codes.txt");
//		String path = Constants.BASE_PATH + "finance2017-07-19(121035)/balance/红豆股份(600400)_资产负债表.xls";
		String path = Constants.BASE_PATH + "finance2017-07-19(170614)/cashFlow/";
		rename(path);
//		List<List<String>> data = readCSVFile(path);
//		for(List<String> row : data) {
//			System.out.println(row.size() + " " + row);
//		}
	}
}
