package com.skynet.spider.util;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.StringUtil;

import com.skynet.spider.bean.Fund;
import com.skynet.spider.bean.HoldingData;
import com.skynet.spider.bean.PerformanceData;
import com.skynet.spider.bean.Return;
import com.skynet.spider.bean.ReturnData;
import com.skynet.spider.bean.RiskAssessment;
import com.skynet.spider.bean.RiskData;
import com.skynet.spider.bean.RiskStats;
import com.skynet.spider.bean.StockHolding;

/**
 * 晨星网数据封装工具
 * @author air
 *
 */
public class MSDataUtil {

	/**
	 * 十大股票持仓数据组装
	 * 表头：基金代码 基金名称 股票代码 股票名称 股票市值 持仓百分比 所属板块
	 * @param fundCode
	 * @param fundName
	 * @param list
	 * @param holdingData
	 */
	public static List<List<String>> formatHoldingData(String fundCode, String fundName, HoldingData holdingData) {
		List<List<String>> data = new ArrayList<List<String>>();
		if(holdingData.Top10StockHoldings != null) {
			for(StockHolding sh : holdingData.Top10StockHoldings) {
				List<String> row = new ArrayList<String>();
				row.add(fundCode);
				row.add(fundName);
				row.add(sh.Symbol);
				row.add(sh.HoldingName);
				row.add(sh.MarketValue);
				row.add(sh.Percent);
				row.add(sh.SectorName);
				data.add(row);
			}
		}
		return data;
	}
	
	/**
	 * 组装回报数据
	 * 表头 基金代码 基金名称 一个月回报，基准，平均 
	 * 三个月回报，基准，平均 六个月回报，基准，平均 今年以来回报，基准，平均 一年回报，基准，平均 二年回报（年化），基准，平均
	 * 三年回报（年化），基准，平均 五年回报（年化），基准，平均 十年回报（年化），基准，平均
	 * @param fundCode
	 * @param fundName
	 * @param returnData
	 * @return
	 */
	public static List<String> formatReturnData(String fundCode, String fundName, ReturnData returnData, PerformanceData performanceData) {
		List<String> data = new ArrayList<String>();
		data.add(fundCode);
		data.add(fundName);
		data.add(performanceData.Worst3MonReturn);
		data.add(performanceData.Worst6MonReturn);
		if(returnData.CurrentReturn != null && returnData.CurrentReturn.Return != null) {
			for(Return rt : returnData.CurrentReturn.Return) {
				data.add(StringUtil.isBlank(rt.Return)?"-":rt.Return);
				data.add(StringUtil.isBlank(rt.ReturnToInd)?"-":rt.ReturnToInd);
				data.add(StringUtil.isBlank(rt.ReturnToCat)?"-":rt.ReturnToCat);
			}
		}
		
		return data;
	}
	
	/**
	 * 风险数据组装
	 * 表头 基金代码 基金名称 三年平均回报 三年评价 五年平均回报 五年评价 十年平均回报 十年评价
	 *   三年标准差 三年评价 五年标准差 五年评价 十年标准差 十年评价 三年晨星风险系数 三年评价
	 *   五年晨星风险系数 五年评价 十年晨星风险系数 十年评价 三年夏普比率 三年评价 五年夏普比率 五年评价 
	 *   十年夏普比率 十年评价 阿尔法系数基准 阿尔法系数平均 贝塔系数基准 贝塔系数平均 R平方基准 R平方平均
	 * @param fundCode
	 * @param fundName
	 * @param riskData
	 * @return
	 */
	public static List<String> formatRiskData(String fundCode, String fundName, RiskData riskData) {
		List<String> data = new ArrayList<String>();
		data.add(fundCode);
		data.add(fundName);
		for(RiskAssessment ra : riskData.RiskAssessment) {
			data.add(StringUtil.isBlank(ra.Year3)?"-":ra.Year3);
			data.add(getRiskRank(ra.Year3Rank));
			data.add(StringUtil.isBlank(ra.Year5)?"-":ra.Year5);
			data.add(getRiskRank(ra.Year5Rank));
			data.add(StringUtil.isBlank(ra.Year10)?"-":ra.Year10);
			data.add(getRiskRank(ra.Year10Rank));
		}
		
		for(RiskStats rs : riskData.RiskStats) {
			data.add(StringUtil.isBlank(rs.ToInd)?"-":rs.ToInd);
			data.add(StringUtil.isBlank(rs.ToCat)?"-":rs.ToCat);
		}
		return data;
	}
	
	public static String getRiskRank(String rank) {
		if(StringUtil.isBlank(rank)) {
			return "-";
		} else if("B".equals(rank)) {
			return "偏低";
		} else if("M".equals(rank)) {
			return "中";
		} else if("H".equals(rank)) {
			return "高";
		}
		return "-";
	}
	
	/**
	 * 组装同类基金数据
	 * 表头 基金代码 基金名称 同类基金1代码 同类基金1名称 同类基金1净值 同类基金2代码 同类基金2名称 同类基金2净值
	 * 同类基金3代码 同类基金3名称 同类基金3净值 同类基金4代码 同类基金4名称 同类基金4净值 同类基金5代码 同类基金5名称 同类基金5净值
	 * @param fundCode
	 * @param fundName
	 * @param sameFunds
	 * @return
	 */
	public static List<String> formatSameFund(String fundCode, String fundName, List<Fund> sameFunds) {
		List<String> data = new ArrayList<String>();
		data.add(fundCode);
		data.add(fundName);
		for(Fund fund : sameFunds) {
			data.add(fund.FundClassId);
			data.add(fund.FundName);
			data.add(fund.NAV);
		}
		return data;
	}
}
