package com.skynet.spider.spiders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jsoup.helper.StringUtil;

import com.skynet.spider.bean.Fund;
import com.skynet.spider.bean.HoldingData;
import com.skynet.spider.bean.PerformanceData;
import com.skynet.spider.bean.ReturnData;
import com.skynet.spider.bean.RiskData;
import com.skynet.spider.util.ExcelUtil;
import com.skynet.spider.util.MSDataUtil;

public class SpiderController {

	public static String holdingSheetName = "基金持仓数据";
	public static String returnSheetName = "基金回报数据";
	public static String riskSheetName = "基金风险数据";
	public static String sameFundSheetName = "同类基金数据";
	public static List<String> holdingHead = Arrays.asList("基金代码","基金名称", "股票代码", "股票名称", "股票市值", "持仓百分比", "所属板块");
	public static List<String> returnHead = Arrays.asList("基金代码", "基金名称", "三个月最差回报", "六个月最差回报", "一个月回报", "一个月基准", "一个月平均",
			"三个月回报", "三个月基准", "三个月平均", "六个月回报", "六个月基准", "六个月平均", "今年以来回报", "今年以来基准", 
			"今年以来平均", "一年回报", "一年基准", "一年平均", "二年回报（年化）", "二年基准", "二年平均",
			"三年回报（年化）", "三年基准", "三年平均", "五年回报（年化）", "五年基准", "五年平均",
			"十年回报（年化）", "十年基准", "十年平均");
	public static List<String> riskHead = Arrays.asList("基金代码", "基金名称", "三年平均回报", "三年评价", "五年平均回报", "五年评价", 
			"十年平均回报", "十年评价", "三年标准差", "三年评价", "五年标准差", "五年评价", "十年标准差", "十年评价", "三年晨星风险系数", 
			"三年评价", "五年晨星风险系数", "五年评价", "十年晨星风险系数", "十年评价", "三年夏普比率", "三年评价", "五年夏普比率", "五年评价", 
			"十年夏普比率", "十年评价", "阿尔法系数基准", "阿尔法系数平均", "贝塔系数基准", "贝塔系数平均", "R平方基准", "R平方平均");
	public static List<String> sameFundHead = Arrays.asList("基金代码", "基金名称", "同类基金1代码", "同类基金1名称", "同类基金1净值", 
			"同类基金2代码", "同类基金2名称", "同类基金2净值", "同类基金3代码", "同类基金3名称", "同类基金3净值", "同类基金4代码", "同类基金4名称", 
			"同类基金4净值", "同类基金5代码", "同类基金5名称", "同类基金5净值");
	
	public void run() {
		
	}
	
	public static void main(String[] args) throws Exception{
		String path = "/Users/air/downloads/基金历史数据.xlsx";
		List<Fund> fundList = ExcelUtil.getFundCode(path);
		System.out.println("基金总数：" + fundList.size());
		List<String> errCode = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd(HHmmss)");
		String date = sdf.format(new Date());
		String fileName = "/Users/air/downloads/晨星网基金数据" + date + ".xlsx";
		
		List<List<String>> holdingData = new ArrayList<List<String>>();
		List<List<String>> returnData = new ArrayList<List<String>>();
		List<List<String>> riskData = new ArrayList<List<String>>();
		List<List<String>> sameFundData = new ArrayList<List<String>>();
		int index = 1;
		for(Fund fund : fundList) {
			System.out.println("开始获取第" + index + "只基金,名称：" + fund.FundName + ", 代码：" + fund.FundClassId);
			if(StringUtil.isBlank(fund.FundClassId)) {
				continue;
			}
			String code = MorningStarSpider.crawCode(fund.FundClassId);
			if(code != null) {
//				List<String> fundData = new ArrayList<String>();
				HoldingData holdingDataPage = MorningStarSpider.crawHoldingData(code);
				List<List<String>> fundHoldingData = MSDataUtil.formatHoldingData(fund.FundClassId, fund.FundName, holdingDataPage);
				holdingData.addAll(fundHoldingData);
				
				PerformanceData performanceData = MorningStarSpider.crawPerformanceData(code);
				ReturnData returnDataPage = MorningStarSpider.crawReturnData(code);
				List<String> fundReturnData = MSDataUtil.formatReturnData(fund.FundClassId, fund.FundName, returnDataPage, performanceData);
				returnData.add(fundReturnData);
				
				RiskData riskDataPage = MorningStarSpider.crawRiskData(code);
				List<String> fundRiskData = MSDataUtil.formatRiskData(fund.FundClassId, fund.FundName, riskDataPage);
				riskData.add(fundRiskData);
				
				List<Fund> sameFundDataPage = MorningStarSpider.crawSameFundData(code);
				List<String> sameFundFormat = MSDataUtil.formatSameFund(fund.FundClassId, fund.FundName, sameFundDataPage);
				sameFundData.add(sameFundFormat);
				
				++index;
				if(index > 5) {
//					break;
				}
				Thread.sleep(100);
				
			} else {
				System.out.println("获取基金代码时出错, fundode:" + fund.FundClassId);
				errCode.add(fund.FundClassId);
//				break;
			}
		}
		ExcelUtil.write(fileName, returnSheetName, returnHead, returnData);
		ExcelUtil.write(fileName, holdingSheetName, holdingHead, holdingData);
		ExcelUtil.write(fileName, riskSheetName, riskHead, riskData);
		ExcelUtil.write(fileName, sameFundSheetName, sameFundHead, sameFundData);
		
		System.out.println("errCode:" + errCode);
	}
}
