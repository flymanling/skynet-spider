package com.skynet.spider.spiders;

import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skynet.spider.bean.CodeBean;
import com.skynet.spider.bean.Fund;
import com.skynet.spider.bean.HoldingData;
import com.skynet.spider.bean.PerformanceData;
import com.skynet.spider.bean.ReturnData;
import com.skynet.spider.bean.RiskData;

public class MorningStarSpider {

	public static String codeUrl = "http://cn.morningstar.com/cacheapi/json/quickquery.ashx?callback=jsonp1489212025409&q={code}"
			+ "&limit=31&timestamp=1489212098318&usid=false&type=fund";
	//基金详情页
	public static String fundPageUrl = "http://cn.morningstar.com/quicktake/{code}?place=qq";
	
	public static String baseDataUrl = "http://cn.morningstar.com/handler/quicktake.ashx?command={command}&fcid={code}&randomid=0.2214766691772938";
	
	//回报接口
	public static String returnCommand = "return";
	//业绩和最差回报
	public static String performanceCommand = "performance";
	//风险数据
	public static String riskCommand = "rating";
	//持仓数据
	public static String holdingCommand = "portfolio";
	//同类基金
	public static String sameFundCommand = "samefund";
	
	public static Gson gson = new Gson();
	
	public static String getCrawUrl(String command, String code) {
		return baseDataUrl.replace("{command}", command).replace("{code}", code);
	}
	
	public static String crawCode(String fundCode) throws Exception{
		String fundCodeUrl = codeUrl.replace("{code}", fundCode);
		System.out.println("fundCodeUrl:" + fundCodeUrl);
		Document doc = Jsoup.connect(fundCodeUrl).get();
		String codeData = doc.body().text();
		System.out.println("codeData:" + codeData);
		String regex = "\\{.*?\\}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(codeData);
		System.out.println("replace: "+codeData.replaceAll(regex, "O"));
		while (matcher.find()) {
			String mstr = matcher.group(0);
			System.out.println("matcher: " + mstr);
			CodeBean codebean = gson.fromJson(mstr, CodeBean.class);	
			if(codebean.Type != null && "fund".equals(codebean.Type)) {
				return codebean.Key;			}
		}
		return null;
	}
	
	public static <T> T crawData(String code, String command, Class<T> t) throws Exception{
		String url = getCrawUrl(command, code);
		Document doc = Jsoup.connect(url).get();
		String body = doc.body().text();
		System.out.println("body:" + body);
		return gson.fromJson(body, t);
	}
	
	public static <T> T crawData(String code, String command, Type t) throws Exception{
		String url = getCrawUrl(command, code);
		Document doc = Jsoup.connect(url).get();
		String body = doc.body().text();
		return gson.fromJson(body, t);
	}
	
	public static ReturnData crawReturnData(String code) throws Exception{
		return crawData(code, returnCommand, ReturnData.class);
	}
	
	public static PerformanceData crawPerformanceData(String code) throws Exception{
		return crawData(code, performanceCommand, PerformanceData.class);
	}
	
	public static RiskData crawRiskData(String code) throws Exception{
		return crawData(code, riskCommand, RiskData.class);
	}
	
	public static HoldingData crawHoldingData(String code) throws Exception{
		return crawData(code, holdingCommand, HoldingData.class);
	}
	
	public static List<Fund> crawSameFundData(String code) throws Exception{
		return crawData(code, sameFundCommand, new TypeToken<List<Fund>>(){}.getType());
	}
}
