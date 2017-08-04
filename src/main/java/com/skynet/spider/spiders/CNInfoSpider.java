package com.skynet.spider.spiders;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.skynet.spider.util.PropUtil;

public class CNInfoSpider {

	public static void main(String[] args) throws Exception{
		Map<String, String> data = getCodes();
		PropUtil.updateProperties("conf/company.properties", data);
	}
	
	public static Map<String, String> getCodes() throws Exception{
		String url = "http://quote.eastmoney.com/stock_list.html";
		Document doc = Jsoup.connect(url).get();
//		System.out.println(doc.html());
		Elements body = doc.getElementsByAttributeValue("class", "quotebody");
//		System.out.println(body.get(0).html());
		Elements uls = body.get(0).getElementsByTag("ul");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd(HHmmss)");
		String date = sdf.format(new Date());
		//上海
		Element shUl = uls.get(0);
		Elements lis = shUl.getElementsByTag("li");
		
		Map<String, String> data = new HashMap<String, String>();
		for(Element li : lis) {
			String codeName = li.text();
			String regex = "\\(.*?\\)";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(codeName);
			while (matcher.find()) {
				String codeWrap = matcher.group();
				String name = codeName.replace(codeWrap, "");
				String code = codeWrap.replace("(", "").replace(")", "");
//				System.out.println(code + " " + name);
				data.put(code, name);
			}
		}
		List<String> head = Arrays.asList("代码", "名称");
//		ExcelUtil.write("/Users/air/downloads/股票代码"+date+".xlsx", "上海股票代码", head, data);
		
		
		//深圳
		Element szUl = uls.get(1);
		Elements szlis = szUl.getElementsByTag("li");
		
		for(Element li : szlis) {
			String codeName = li.text();
			String regex = "\\(.*?\\)";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(codeName);
			while (matcher.find()) {
				String codeWrap = matcher.group();
				String name = codeName.replace(codeWrap, "");
				String code = codeWrap.replace("(", "").replace(")", "");
//				System.out.println(code + " " + name);
				data.put(code, name);
			}
		}
		
		return data;
	}
}
