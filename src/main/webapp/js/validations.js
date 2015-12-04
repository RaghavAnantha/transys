function validateAndFormatPhone(phoneId) {	
	var phone = document.getElementById(phoneId).value;
	if (phone == "") {
		return;
	}
	
	if (phone.length < 10  
			|| phone.length > 12
			|| (phone.length > 10 && !phone.match("-"))
			|| (phone.match("-") && phone.length != 12)) {
		var alertMsg = "<p>Invalid Phone/Fax Number.</p>";
		showAlertDialog("Data validation", alertMsg);
		
		document.getElementById(phoneId).value = "";
		return false;
	} else {
		var formattedPhone = formatPhone(phone);
		document.getElementById(phoneId).value = formattedPhone;
	}	
}

function validateAndFormatPhoneModal(phoneId) {	
	var phone = document.getElementById(phoneId).value;
	if (phone == "") {
		return;
	}
	
	if (phone.length < 10  
			|| phone.length > 12
			|| (phone.length > 10 && !phone.match("-"))
			|| (phone.match("-") && phone.length != 12)) {
		var alertMsg = "Invalid Phone/Fax Number.";
		displayPopupDialogErrorMessage(alertMsg, false);
		
		document.getElementById(phoneId).value = "";
		return false;
	} else {
		var formattedPhone = formatPhone(phone);
		document.getElementById(phoneId).value = formattedPhone;
		
		clearPopupDialogErrorMessage();
	}	
}

function formatPhone(phone) {
	if (phone.length < 10) {
		return phone;
	}
	
	var str = new String(phone);
	if (str.match("-")) {
		return phone;
	}
	
	var p1 = str.substring(0,3);
	var p2 = str.substring(3,6);
	var p3 = str.substring(6,10);				
	return p1 + "-" + p2 + "-" + p3;
}

function validateAmount(amt, maxValue) {
	if (!/(^\d+\.\d{2}$)|(^\d+$)/.test(amt)) {
		return false;
	}
	
	if (maxValue !=  null && amt > maxValue) {
		return false;
	}
	
	return true;
}

function validateWeight(weight, maxValue) {
	if (!/(^\d+\.\d{1,2}$)|(^\d+$)|(^\.\d{1,2}$)/.test(weight)) {
		return false;
	}
	
	if (weight != null && weight > maxValue) {
		return false;
	}
	
	return true;
}

function validatePhone(phone) {
	return /^[2-9]{1}\d{2}(-)[2-9]{1}\d{2}(-)\d{4}$/.test(phone);
}

function validateDate(date) {
	var datePattern = new RegExp("^([0-9]{2})\/([0-9]{2})\/([0-9]{4})$");
	if(!datePattern.test(date)) {
		return false;
	}
	
	var data = date.split("/");
    // using ISO 8601 Date String
    if (isNaN(Date.parse(data[2] + "-" + data[0] + "-" + data[1]))) {
    	return false;
    }

    return true;
}

function validateTimeRange(timeFrom, timeTo) {
	var timeFromTrimmed = timeFrom.trim();
	var timeToTrimmed = timeTo.trim();
	
	if (timeFromTrimmed == "" || timeToTrimmed == "") {
		return false;
	}
	
	var timeFromTrimmedTokens = timeFromTrimmed.split(" ");
	var timeToTrimmedTokens = timeToTrimmed.split(" ");
	
	var dateFrom = new Date('03/11/2015 ' + timeFromTrimmedTokens[0] + ':00:00 ' + timeFromTrimmedTokens[1]);
	var dateTo = new Date('03/11/2015 ' + timeToTrimmedTokens[0] + ':00:00 ' + timeToTrimmedTokens[1]);
	if (dateTo < dateFrom) {
    	return false;
    } else {
    	return true;
    }
}
	
function validateDateRange(dateFrom, dateTo) {
	var dateFromTrimmed = dateFrom.trim();
	var dateToTrimmed = dateTo.trim();
	
	if (dateFromTrimmed == "" || dateToTrimmed == "") {
		return false;
	}
	
	var dateFromMsec = Date.parse(dateFromTrimmed);
	var dateToMsec = Date.parse(dateToTrimmed);
	
	if (dateToMsec < dateFromMsec) {
    	return false;
    } else {
    	return true;
    }
}

function validateZipCode(zipcode) {
	return /(^\d{5}$)|(^\d{5}-\d{4}$)/.test(zipcode);
}

function validateEmail(email) {
	if (email.length > 50) {
		return false;
	}
	
	return /^[a-zA-Z0-9-_]+@[a-zA-Z0-9-_]+\.[a-zA-Z0-9-_]+$/.test(email);
	//return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function validateText(text, validLength) {
	if (text.trim() == "") {
		return false;
	}
	
	var textPattern = new RegExp("^[a-zA-Z0-9-_():;.\"',?/*&%$#@!`\\s\\\\]{1," + validLength + "}$");
	return textPattern.test(text);
}

function validateAddressLine(addressLine, validLength) {
	var addressLinePattern = new RegExp("^[a-zA-Z0-9-_,:/'#\\s\\\\]{1," + validLength + "}$");
	return addressLinePattern.test(addressLine);
}
	
function validateAlphaOnly(text, validLength) {
	var alphaPattern = new RegExp("^[a-zA-Z]{1," + validLength + "}$");
	return alphaPattern.test(text);
}

function validateName(name, validLength) {
	var namePattern = new RegExp("^[a-zA-Z-'`\\s/]{1," + validLength + "}$");
	return namePattern.test(name);
}

function validateCompanyName(companyName, validLength) {
	var companyNamePattern = new RegExp("^[a-zA-Z0-9-_'`\\s/.,&]{1," + validLength + "}$");
	return companyNamePattern.test(companyName);
}

function validateReferenceNum(refNum, validLength) {
	var refNumPattern = new RegExp("^[a-zA-Z0-9-_():.,/*&%$#\\s\\\\]{1," + validLength + "}$");
	return refNumPattern.test(refNum);
}

function validateDumpsterNum(dumpsterNum, validLength) {
	if (dumpsterNum.trim() == "") {
		return false;
	}
	
	var dumpsterNumPattern = new RegExp("^[a-zA-Z0-9-_]{1," + validLength + "}$");
	return dumpsterNumPattern.test(dumpsterNum);
}

function validateMaterial(material, validLength) {
	if (material.trim() == "") {
		return false;
	}
	
	var materialPattern = new RegExp("^[a-zA-Z0-9-_()/&\\s\\\\]{1," + validLength + "}$");
	return materialPattern.test(material);
}
