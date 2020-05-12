package com.dcjt518.oms.common.exception;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据验证失败异常
 * 
 * @author Zzy
 *
 */
public class DataValidateFailException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 1L;

	private String errorMsg;
	private Map<String, String> errorsFields;

	public DataValidateFailException(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public DataValidateFailException(Map<String, String> errorsFields) {
		this.errorsFields = errorsFields;
	}

	public DataValidateFailException(String errorMsg, Map<String, String> errorsFields) {
		this.errorMsg = errorMsg;
		this.errorsFields = errorsFields;
	}

	public String getErrorMsg() {
		if (errorMsg == null) {
			errorMsg = "";
		}
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Map<String, String> getErrorsFields() {
		if (errorsFields == null) {
			errorsFields = new HashMap<>();
		}
		return errorsFields;
	}

	public void setErrorsFields(Map<String, String> errorsFields) {
		this.errorsFields = errorsFields;
	}

}
