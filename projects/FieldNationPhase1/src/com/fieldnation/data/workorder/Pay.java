package com.fieldnation.data.workorder;

import com.fieldnation.json.annotations.Json;

public class Pay {
	// TODO shouldn't use double for money!
	@Json(name = "bonuses")
	private double _bonuses = 0;
	@Json(name = "maximumAmount")
	private double _maximumAmount = 0;
	private double _expenses = 0;
	private double _fixedAmount = 35;
	private int _maxHour = 5;
	private double _perHour = 10;
	private String _basis;
}
