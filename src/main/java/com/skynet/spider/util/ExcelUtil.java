package com.skynet.spider.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.helper.StringUtil;

import com.skynet.spider.bean.Fund;

public class ExcelUtil {

	public static List<List<String>> read(String path) throws Exception{
		List<List<String>> data = new ArrayList<List<String>>();
		// 构造 XSSFWorkbook 对象，strPath 传入文件路径  
		File excel = new File(path);  // 读取文件
		 Workbook xwb = null;
	        try {
	        	xwb = new XSSFWorkbook(excel);
	        } catch (Exception ex) {
//	        	xwb = WorkbookFactory.create(new FileInputStream(excel));
	        	xwb = new HSSFWorkbook(new FileInputStream(excel));
	        }
		// 读取第一章表格内容  
		Sheet sheet = xwb.getSheetAt(0);  
		// 定义 row、cell  
		Row row;  
		String cell;  
		// 循环输出表格中的内容  
		for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++) {  
		    row = sheet.getRow(i);
		    List<String> dataRow = new ArrayList<String>();
		    for (int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++) {  
		        // 通过 row.getCell(j).toString() 获取单元格内容，  
		        cell = row.getCell(j).toString();  
		        System.out.print(cell + "\t");  
		        dataRow.add(cell);
		    }  
		    data.add(dataRow);
		    System.out.println("");  
		}  	  
		return data;
	}
	
	public static List<Fund> getFundCode(String path) throws Exception{
		List<Fund> list = new ArrayList<Fund>();
		// 构造 XSSFWorkbook 对象，strPath 传入文件路径  
		XSSFWorkbook xwb = new XSSFWorkbook(path);  
		// 读取第一章表格内容  
		XSSFSheet sheet = xwb.getSheetAt(0);  
		// 定义 row、cell  
		XSSFRow row;  
		String cell;  
		// 循环输出表格中的内容  
		for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++) {  
		    row = sheet.getRow(i); 
		    Fund fund = new Fund();
		    String code = row.getCell(0).toString();
		    if(!StringUtil.isBlank(code)) {
		    	fund.FundClassId = code.replace(".0", "");
		    }
		    fund.FundName = row.getCell(1).toString();
		    list.add(fund);
		}  	  
		return list;
	}
	
	public static void write(String fileName, String sheetName, List<String> head, List<List<String>> data) throws Exception{
		XSSFWorkbook wb = null;
		File excel = new File(fileName);  // 读取文件
		if(!excel.exists()) {
			wb = new XSSFWorkbook();
		} else {
			FileInputStream in = new FileInputStream(excel); // 转换为流
			wb = new XSSFWorkbook(in);  
		}
		//添加Worksheet（不添加sheet时生成的xls文件打开时会报错）  
		@SuppressWarnings("unused")  
//		WritableWorkbook wb = Workbook.createWorkbook(new File(fileName));
			Sheet sheet1 = wb.createSheet(sheetName);// 创建一个工作薄对象 
//        wb.setSheetName(sheetIndex, sheetName);
			Row headRow = sheet1.createRow(0);
			for(int i=0;i<head.size();i++) {
				String headName = head.get(i);
				Cell headCell = headRow.createCell(i);
				headCell.setCellValue(headName);
			}
			
			int offSet = 1;
			for(int i=0;i<data.size();i++) {
				List<String> dataRow = data.get(i);
				Row row1 = sheet1.createRow(i+offSet);
				for(int j=0;j<dataRow.size();j++) {
					String col = dataRow.get(j);
					Cell cell1_1 = row1.createCell(j);  
					cell1_1.setCellValue(col);  
				}
			}
		  
		//保存为Excel文件  
		FileOutputStream out = new FileOutputStream(fileName);  
		wb.write(out);        
	}
	
	public static void main(String[] args) throws Exception{
		String path = "/Users/air/downloads/sz_lrb_000001_2015.csv";
		read(path);
//		read(path);
//		List<String> codeList =getFundCode(path);
//		for(String code : codeList) {
//			System.out.println(code);
//		}
//		String filePath = "/Users/air/downloads/测试写入.xlsx";
//		List<String> head = Arrays.asList("一", "二", "三");
//		List<List<String>> data = new ArrayList<List<String>>();
//		data.add(Arrays.asList("1","2","3"));
//		data.add(Arrays.asList("1","2","3"));
//		data.add(Arrays.asList("1","2","3"));
//		write(filePath, "基金数据", head, data);
	}
}
