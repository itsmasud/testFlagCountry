package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class WorkorderBonusInfo{
	@Json(name="0")
	private Integer _0;
	@Json(name="1")
	private Integer _1;
	@Json(name="2")
	private String _2;
	@Json(name="3")
	private Integer _3;
	@Json(name="4")
	private Integer _4;
	@Json(name="5")
	private String _5;
	@Json(name="6")
	private Integer _6;
	@Json(name="7")
	private Integer _7;
	@Json(name="8")
	private Integer _8;
	@Json(name="amount")
	private Integer _amount;
	@Json(name="charge_type")
	private Integer _chargeType;
	@Json(name="charged")
	private Integer _charged;
	@Json(name="company_id")
	private Integer _companyId;
	@Json(name="currencyString")
	private String _currencyString;
	@Json(name="id")
	private Integer _id;
	@Json(name="name")
	private String _name;
	@Json(name="rule_explanation")
	private String _ruleExplanation;
	@Json(name="status")
	private Integer _status;
	@Json(name="wocpf_id")
	private Integer _wocpfId;

	public WorkorderBonusInfo(){
	}
	public Integer get0(){
		return _0;
	}

	public Integer get1(){
		return _1;
	}

	public String get2(){
		return _2;
	}

	public Integer get3(){
		return _3;
	}

	public Integer get4(){
		return _4;
	}

	public String get5(){
		return _5;
	}

	public Integer get6(){
		return _6;
	}

	public Integer get7(){
		return _7;
	}

	public Integer get8(){
		return _8;
	}

	public Integer getAmount(){
		return _amount;
	}

	public Integer getChargeType(){
		return _chargeType;
	}

	public Integer getCharged(){
		return _charged;
	}

	public Integer getCompanyId(){
		return _companyId;
	}

	public String getCurrencyString(){
		return _currencyString;
	}

	public Integer getId(){
		return _id;
	}

	public String getName(){
		return _name;
	}

	public String getRuleExplanation(){
		return _ruleExplanation;
	}

	public Integer getStatus(){
		return _status;
	}

	public Integer getWocpfId(){
		return _wocpfId;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(WorkorderBonusInfo workorderBonusInfo) {
		try {
			return Serializer.serializeObject(workorderBonusInfo);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static WorkorderBonusInfo fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(WorkorderBonusInfo.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
