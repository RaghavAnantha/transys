package com.transys.model.vo;

import java.io.Serializable;

public class BaseVO implements Serializable {
	private int historyCount = -1;
	
	public int getHistoryCount() {
		return historyCount;
	}
	public void setHistoryCount(int historyCount) {
		this.historyCount = historyCount;
	}
}
