package com.fieldnation.data.workorder;

import com.fieldnation.json.annotations.Json;

public class Schedule{
	@Json(name="start_time")
	private String _startTime;

	public Schedule(){
	}
	public String getStartTime(){
		return _startTime;
	}

}
