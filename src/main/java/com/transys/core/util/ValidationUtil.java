package com.transys.core.util;

import org.apache.commons.lang3.StringUtils;

public class ValidationUtil {
	public static boolean validatePhone(String phone) {
		if (StringUtils.isEmpty(StringUtils.stripToEmpty(phone))) {
			return false;
		}
		
		return phone.matches("^[2-9]{1}\\d{2}(-)[2-9]{1}\\d{2}(-)\\d{4}$");
	}
	
	public static boolean validateZipcode(String zipcode) {
		if (StringUtils.isEmpty(StringUtils.stripToEmpty(zipcode))) {
			return false;
		}
		
		return zipcode.matches("(^\\d{5}$)|(^\\d{5}-\\d{4}$)");
	}
	
	public static boolean validateEmail(String email) {
		if (StringUtils.isEmpty(StringUtils.stripToEmpty(email))) {
			return false;
		}
		
		if (email.length() > 50) {
			return false;
		}
		
		//return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
		return email.matches("^[a-zA-Z0-9-_]+@[a-zA-Z0-9-_]+.[a-zA-Z0-9-_]+$");
	}
	
	public static boolean validateAddressLine(String addressLine, int validLength) {
		if (StringUtils.isEmpty(StringUtils.stripToEmpty(addressLine))) {
			return false;
		}
		
		return addressLine.matches("^[a-zA-Z0-9-_,:/'#\\s\\\\]{1," + validLength + "}$");
	}
	
	public static boolean validateName(String name, int validLength) {
		if (StringUtils.isEmpty(StringUtils.stripToEmpty(name))) {
			return false;
		}
		
		return name.matches("^[a-zA-Z-'`\\s/]{1," + validLength + "}$");
	}

	public static boolean validateCompanyName(String companyName, int validLength) {
		if (StringUtils.isEmpty(StringUtils.stripToEmpty(companyName))) {
			return false;
		}
		
		return companyName.matches("^[a-zA-Z0-9-_'`\\s/.,&]{1," + validLength + "}$");
	}
	
	public static boolean validateDumpsterNum(String dumpsterNum, int validLength) {
		if (StringUtils.isEmpty(StringUtils.stripToEmpty(dumpsterNum))) {
			return false;
		}
		
		return dumpsterNum.matches("^[a-zA-Z0-9-_]{1," + validLength + "}$");
	}
}
