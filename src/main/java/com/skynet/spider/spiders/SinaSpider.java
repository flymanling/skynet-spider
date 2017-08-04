package com.skynet.spider.spiders;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.skynet.spider.common.Constants;
import com.skynet.spider.util.FileUtil;
import com.skynet.spider.util.PropUtil;

public class SinaSpider {

	public static String balanceUrl = "http://money.finance.sina.com.cn/corp/go.php/vDOWN_BalanceSheet/displaytype/4/stockid/{code}/ctrl/all.phtml";
	public static String profitUrl = "http://money.finance.sina.com.cn/corp/go.php/vDOWN_ProfitStatement/displaytype/4/stockid/{code}/ctrl/all.phtml";
	public static String cashFlowUrl = "http://money.finance.sina.com.cn/corp/go.php/vDOWN_CashFlow/displaytype/4/stockid/{code}/ctrl/all.phtml";
	
	public static void run() throws Exception{
		Map<String, String> companys = PropUtil.readValue(Constants.COMPANY_PATH);
		String path = createPath();
		for(Entry<String, String> entry : companys.entrySet()) {
			getFile(entry.getKey(), entry.getValue(), path);
			Thread.sleep(500);
		}
	}
	
	public static void main(String[] args) throws Exception{
		run();
	}
	
	public static String createPath() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd(HHmmss)");
		String date = sdf.format(new Date());
		String path = Constants.BASE_PATH + "finance" + date;
		FileUtil.createDir(path + "/balance/");
		FileUtil.createDir(path + "/profit/");
		FileUtil.createDir(path + "/cashFlow/");
		return path;
	}
	
	public static void getFile(String code, String name, String path) {
		
		String balanceFile = path + "/balance/" + name + "_" + code + "_" + "_资产负债表.xls";
		String balanceLink = balanceUrl.replace("{code}", code);
		
		try {
			download(balanceLink, balanceFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String profitFile = path + "/profit/" + name + "_" + code + "_" + "_利润表.xls";
		String profitLink = profitUrl.replace("{code}", code);
		try {
			download(profitLink, profitFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String cashFlowFile = path + "/cashFlow/" + name + "_" + code + "_" + "_现金流量表.xls";
		String cashFlowLink = cashFlowUrl.replace("{code}", code);
		try {
			download(cashFlowLink, cashFlowFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void download(String url, String fileName) throws Exception{
		Response resultImageResponse = Jsoup.connect(url)
				.ignoreContentType(true)
//				.header("Content-Type", "application/xhtml+xml.Mimetype=application/vnd.ms-excel")
				//.header("Content-Type", "application/xhtml+xml")
				//.header("Mime-Type", "application/vnd.ms-excel")
				.execute();
		FileOutputStream out = (new FileOutputStream(new java.io.File(fileName)));
		out.write(resultImageResponse.charset("UTF-8").bodyAsBytes());           
		out.close();
	}
}
