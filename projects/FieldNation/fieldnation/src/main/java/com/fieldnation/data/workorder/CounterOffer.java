package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class CounterOffer{
	@Json(name="additionalHourRate")
	private Double _additionalHourRate;
	@Json(name="additionalMaxHours")
	private Double _additionalMaxHours;
	@Json(name="description")
	private String _description;
	@Json(name="expires")
	private Boolean _expires;
	@Json(name="expiresAfter")
	private String _expiresAfter;
	@Json(name="expiresAfterInSecond")
	private Integer _expiresAfterInSecond;
	@Json(name="fixedHourRate")
	private Double _fixedHourRate;
	@Json(name="fixedHours")
	private Double _fixedHours;
	@Json(name="fixedTotalAmount")
	private Double _fixedTotalAmount;
	@Json(name="hourlyRate")
	private Double _hourlyRate;
	@Json(name="maxDevices")
	private Double _maxDevices;
	@Json(name="maxHours")
	private Double _maxHours;
	@Json(name="maximumAmount")
	private Integer _maximumAmount;
	@Json(name="payBasis")
	private String _payBasis;
	@Json(name="payPerDevice")
	private Double _payPerDevice;
	@Json(name="proposedExpense")
	private AdditionalExpense[] _proposedExpense;
	@Json(name="proposedSchedule")
	private ProposedSchedule _proposedSchedule;
	@Json(name="providerExplanation")
	private String _providerExplanation;

	public CounterOffer(){
	}
	public Double getAdditionalHourRate(){
		return _additionalHourRate;
	}

	public Double getAdditionalMaxHours(){
		return _additionalMaxHours;
	}

	public String getDescription(){
		return _description;
	}

	public Boolean getExpires(){
		return _expires;
	}

	public String getExpiresAfter(){
		return _expiresAfter;
	}

	public Integer getExpiresAfterInSecond(){
		return _expiresAfterInSecond;
	}

	public Double getFixedHourRate(){
		return _fixedHourRate;
	}

	public Double getFixedHours(){
		return _fixedHours;
	}

	public Double getFixedTotalAmount(){
		return _fixedTotalAmount;
	}

	public Double getHourlyRate(){
		return _hourlyRate;
	}

	public Double getMaxDevices(){
		return _maxDevices;
	}

	public Double getMaxHours(){
		return _maxHours;
	}

	public Integer getMaximumAmount(){
		return _maximumAmount;
	}

	public String getPayBasis(){
		return _payBasis;
	}

	public Double getPayPerDevice(){
		return _payPerDevice;
	}

	public AdditionalExpense[] getProposedExpense(){
		return _proposedExpense;
	}

	public ProposedSchedule getProposedSchedule(){
		return _proposedSchedule;
	}

	public String getProviderExplanation(){
		return _providerExplanation;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(CounterOffer counterOffer) {
		try {
			return Serializer.serializeObject(counterOffer);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static CounterOffer fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(CounterOffer.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
