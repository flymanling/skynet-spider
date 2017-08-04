package com.skynet.spider.spiders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.skynet.spider.util.ExcelUtil;
import com.skynet.spider.util.PropUtil;

public class EastMoneySpider {

	public static String baseUrl = "http://vip.stock.finance.sina.com.cn/corp/go.php/vFD_FinanceSummary/stockid/{code}/displaytype/4.phtml";
	
	public static void main(String[] args) throws Exception{
		String companyPath = "conf/company.properties";
		Map<String, String> companys = PropUtil.readValue(companyPath);
		int count = 0;
		List<String> keys = new ArrayList<String>();
		keys.add("公司代码");
		keys.add("公司名称");
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> err = new HashMap<String, String>();
		for(String code : companys.keySet()) {
//			if(StringUtil.isBlank(code) || !code.contains("600")) {
//				continue;
//			}
			Map<String, String> map = null;
			try{
				map = getData(code.trim(), companys.get(code));
			}catch(Exception e) {
				e.printStackTrace();
				err.put(code, companys.get(code));
			}
			if(map == null || map.size() == 0) {
				continue;
			}
			data.add(map);
			for(String key : map.keySet()) {
				if(!keys.contains(key)) {
					keys.add(key);
				}
			}
			count++;
			if(count > 50) {
//				break;
			}
		}
//		System.out.println(data);
		
		List<List<String>> rowData = new ArrayList<List<String>>();
		for(Map<String, String> map : data) {
			List<String> row = new ArrayList<String>();
			for(String key : keys) {
				String value = map.get(key);
				if(value != null) {
					row.add(value);
				} else {
					row.add("-");
				}
			}
			rowData.add(row);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd(HHmmss)");
		String date = sdf.format(new Date());
		ExcelUtil.write("/Users/air/downloads/公司财务数据"+date+".xlsx", "公司财务数据", keys, rowData);
		System.out.println("err:" + err);
	}
	
	public static Map<String, String> getData(String code, String company) throws Exception{
		String url = baseUrl.replace("{code}", code);
//		System.out.println(url);
		Document doc = Jsoup.connect(url).get();
//		String body = doc.html();//.body().text();
//		System.out.println(body);
		Element table = doc.getElementById("FundHoldSharesTable");
//		System.out.println(table.html());
		Map<String, String> rowMap = new HashMap<String, String>();
		rowMap.put("公司代码", code);
		rowMap.put("公司名称", company);
		Elements tbody = table.getElementsByTag("tbody");
		if(tbody == null || tbody.size() == 0) {
			return rowMap;
		}
		Elements lines = tbody.get(0).getElementsByTag("tr");
		
		
		for(Element line : lines) {
			if(line.childNodeSize() > 1) {
				Element td = line.child(0);
				String name = td.html();
				
				Elements strongs = td.getElementsByTag("strong");
				if(strongs != null && strongs.size() > 0) {
					name = strongs.html();
				}
				
				
				Element td1 = line.child(1);
				String value = td1.html();
				Elements links1 = td1.getElementsByTag("a");
				if(links1 != null && links1.size() > 0) {
					value = links1.text();
				}
				Elements strongs1 = td1.getElementsByTag("strong");
				if(strongs1 != null && strongs1.size() > 0) {
					value = strongs1.html();
				}
				if("&nbsp;".equals(value)) {
					value = "-";
				} else {
					value = value.replace("元", "");
				}
//				System.out.println(name + " " + value);
				rowMap.put(name.trim(), value.trim());
			}
		}
		System.out.println(rowMap);
		Thread.sleep(500);
		return rowMap;
	}
}
