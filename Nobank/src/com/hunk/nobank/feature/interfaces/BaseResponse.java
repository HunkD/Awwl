package com.hunk.nobank.feature.interfaces;

public class BaseResponse<T> {
	public boolean isSuccess;
	public T result;
	public String message;
	public int error;
	
	public BaseResponse(boolean isSuccess) {
		super();
		this.isSuccess = isSuccess;
	}

	public BaseResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
}
