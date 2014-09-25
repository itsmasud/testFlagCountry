package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class WorkorderPenaltyInfo{
	@Json(name="charge_type")
	private Integer _chargeType;
	@Json(name="amount")
	private Double _amount;
	@Json(name="wocpf_id")
	private Integer _wocpfId;
	@Json(name="rule_explanation")
	private String _ruleExplanation;
	@Json(name="id")
	private Integer _id;
	@Json(name="currencyString")
	private String _currencyString;
	@Json(name="status")
	private Integer _status;
	@Json(name="8")
	private Integer _8;
	@Json(name="7")
	private Integer _7;
	@Json(name="6")
	private Integer _6;
	@Json(name="5")
	private String _5;
	@Json(name="4")
	private Double _4;
	@Json(name="charged")
	private Integer _charged;
	@Json(name="3")
	private Integer _3;
	@Json(name="2")
	private String _2;
	@Json(name="1")
	private Integer _1;
	@Json(name="0")
	private Integer _0;
	@Json(name="name")
	private String _name;
	@Json(name="company_id")
	private Integer _companyId;

	public WorkorderPenaltyInfo(){
	}
	public Integer getChargeType(){
		return _chargeType;
	}

	public Double getAmount(){
		return _amount;
	}

	public Integer getWocpfId(){
		return _wocpfId;
	}

	public String getRuleExplanation(){
		return _ruleExplanation;
	}

	public Integer getId(){
		return _id;
	}

	public String getCurrencyString(){
		return _currencyString;
	}

	public Integer getStatus(){
		return _status;
	}

	public Integer get8(){
		return _8;
	}

	public Integer get7(){
		return _7;
	}

	public Integer get6(){
		return _6;
	}

	public String get5(){
		return _5;
	}

	public Double get4(){
		return _4;
	}

	public Integer getCharged(){
		return _charged;
	}

	public Integer get3(){
		return _3;
	}

	public String get2(){
		return _2;
	}

	public Integer get1(){
		return _1;
	}

	public Integer get0(){
		return _0;
	}

	public String getName(){
		return _name;
	}

	public Integer getCompanyId(){
		return _companyId;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(WorkorderPenaltyInfo workorderPenaltyInfo) {
		try {
			return Serializer.serializeObject(workorderPenaltyInfo);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static WorkorderPenaltyInfo fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(WorkorderPenaltyInfo.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
