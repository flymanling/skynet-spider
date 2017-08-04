package com.skynet.spider.common;

import java.util.HashMap;
import java.util.Map;

public class Constants {

	public static final String BASE_PATH = "/Users/air/downloads/";
	
	public static final String COMPANY_PATH = "conf/company.properties";
	
	public static Map<String, String> nameMap = new HashMap<String, String>();
	
	static {
		//资产负债
		nameMap.put("货币资金", "cash");
		nameMap.put("所有者权益(或股东权益)合计", "netAssets");
		nameMap.put("负债合计", "totalDebt");
		nameMap.put("资产总计", "totalAssets");
		
		//利润损益
		nameMap.put("一、营业总收入", "totalTaking");
		nameMap.put("五、净利润", "netProfit");
		
		
		//现金流量
		nameMap.put("五、现金及现金等价物净增加额", "cashFlowIncreasing");
		nameMap.put("六、期末现金及现金等价物余额", "cashFlowRemaining");
	}
}
