function validateAndFormatPhone(phoneId) {	
	var phone = document.getElementById(phoneId).value;
	if (phone.trim() == "") {
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
	if (phone.trim() == "") {
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

function validateAmountAndRange(amt, minValue, maxValue) {
	if (!validateAmount(amt, maxValue)) {
		return false;
	}
	
	var intAmt = parseInt(amt);
	if (minValue != null && intAmt <= minValue) {
		return false;
	}
	
	return true;
}

function validateAmount(amt, maxValue) {
	if (amt.trim() == "") {
		return false;
	}
	
	if (isNaN(amt)) {
		return false;
	}
	
	if (!/(^\d+\.\d{2}$)|(^\d+$)/.test(amt)) {
		return false;
	}
	
	var intAmt = parseInt(amt);
	if (maxValue != null && intAmt > maxValue) {
		return false;
	}
	
	return true;
}

function validateWeight(weight, maxValue) {
	if (weight.trim() == "") {
		return false;
	}
	
	if (!/(^\d+\.\d{1,2}$)|(^\d+$)|(^\.\d{1,2}$)/.test(weight)) {
		return false;
	}
	
	if (weight != null && weight > maxValue) {
		return false;
	}
	
	return true;
}

function validatePhone(phone) {
	if (phone.trim() == "") {
		return false;
	}
	
	return /^[2-9]{1}\d{2}(-)[2-9]{1}\d{2}(-)\d{4}$/.test(phone);
}

function validateDate(date) {
	if (date.trim() == "") {
		return false;
	}
	
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

function validateExpiryDate(date) {
	if (date.trim() == "") {
		return false;
	}
	
	var datePattern = new RegExp("^([0-9]{2})\/([0-9]{4})$");
	if(!datePattern.test(date)) {
		return false;
	}
	
	var data = date.split("/");
    // using ISO 8601 Date String
    if (isNaN(Date.parse(data[1] + "-" + data[0]))) {
    	return false;
    }

    return true;
}

//Checks a string to see if it in a valid date format
//of (M)M/(D)D/(YY)YY and returns true/false
function isValidDate(s) {
	// format D(D)/M(M)/(YY)YY
	var dateFormat = /^\d{1,2}[\.|\/|-]\d{1,2}[\.|\/|-]\d{1,4}$/;
	
	if (dateFormat.test(s)) {
	   // remove any leading zeros from date values
	   s = s.replace(/0*(\d*)/gi,"$1");
	   var dateArray = s.split(/[\.|\/|-]/);
	 
	   // correct month value
	   dateArray[0] = dateArray[0]-1;
	
	   // correct year value
	   if (dateArray[2].length<4) {
	       // correct year value
	       dateArray[2] = (parseInt(dateArray[2]) < 50) ? 2000 + parseInt(dateArray[2]) : 1900 + parseInt(dateArray[2]);
	   }
	
	   var testDate = new Date(dateArray[2], dateArray[0], dateArray[1]);
	   if (testDate.getDate()!=dateArray[1] || testDate.getMonth()!=dateArray[0] || testDate.getFullYear()!=dateArray[2]) {
	       return false;
	   } else {
	       return true;
	   }
	} else {
	   return false;
	}
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
	if (zipcode.trim() == "") {
		return false;
	}
	
	return /(^\d{5}$)|(^\d{5}-\d{4}$)/.test(zipcode);
}

function validateEmail(email) {
	if (email.trim() == "") {
		return false;
	}
	
	if (email.length > 50) {
		return false;
	}
	
	return /^[a-zA-Z0-9-_.]+@[a-zA-Z0-9-_]+\.[a-zA-Z0-9-_]+$/.test(email);
	//return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function validateText(text, validLength) {
	if (text.trim() == "") {
		return false;
	}
	
	var textPattern = new RegExp("^[a-zA-Z0-9-_():;.\"',?/*&%$#@!`\\s\\\\=]{1," + validLength + "}$");
	return textPattern.test(text);
}

function validateAddressLine(addressLine, validLength) {
	if (addressLine.trim() == "") {
		return false;
	}
	
	var addressLinePattern = new RegExp("^[a-zA-Z0-9-_,:/'#\\s\\\\]{1," + validLength + "}$");
	return addressLinePattern.test(addressLine);
}
	
function validateAlphaOnly(text, validLength) {
	if (text.trim() == "") {
		return false;
	}
	
	var alphaPattern = new RegExp("^[a-zA-Z]{1," + validLength + "}$");
	return alphaPattern.test(text);
}

function validateName(name, validLength) {
	if (name.trim() == "") {
		return false;
	}
	
	var namePattern = new RegExp("^[a-zA-Z-'`\\s/]{1," + validLength + "}$");
	return namePattern.test(name);
}

function validateUserName(userName, validLength) {
	if (userName.trim() == "") {
		return false;
	}
	
	var userNamePattern = new RegExp("^[a-zA-Z0-9]{5," + validLength + "}$");
	return userNamePattern.test(userName);
}

function validatePassword(passwd, validLength) {
	if (passwd.trim() == "") {
		return false;
	}
	
	if (passwd.trim().length < 8) {
		return false;
	}

	var passwdPattern = new RegExp("^[a-zA-Z0-9-_():.,~/*&%$#\\\\]{1," + validLength + "}$");
	return passwdPattern.test(passwd);
}

function validateCompanyName(companyName, validLength) {
	if (companyName.trim() == "") {
		return false;
	}
	
	var companyNamePattern = new RegExp("^[a-zA-Z0-9-_'`\\s/.,&]{1," + validLength + "}$");
	return companyNamePattern.test(companyName);
}

function validateReferenceNum(refNum, validLength) {
	if (refNum.trim() == "") {
		return false;
	}
	
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

function validateRegex(text, regex, validLength) {
	if (text.trim() == "") {
		return false;
	}
	
	var textPattern = new RegExp("^[" + regex + "]{1," + validLength + "}$");
	return textPattern.test(text);
}

function validateTime(timeField) {
	var time = document.getElementById(timeField).value;
	if (time == "") {
		return true;
	}
	
	if (time.length < 4 || time.length > 5) {
		alert("Invalidte time format");
		clearTextAndFocus(timeField);
		return true;
	} 
	
	var str = new String(time);
	var hour = str.substring(0, 2);
	var min = "";
	if (str.match(":")) {
		if (time.length < 5) {
			alert("Invalidte time format");
			clearTextAndFocus(timeField);
			return true;
		} 
		if (str.indexOf(":") != 2) {
			alert("Invalidte time format");
			clearTextAndFocus(timeField);
			return true;
		}
		min = str.substring(3, 5);
	} else {
		if (time.length > 4) {
			alert("Invalidte time format");
			clearTextAndFocus(timeField);
			return true;
		} 
		min = str.substring(2, 4);
	}
	
	if (hour >= 24 || min >= 60){
		alert("Invalidte time format");
		clearTextAndFocus(timeField);
		return true;
	}
	
	var time = hour+":"+min;
	document.getElementById(timeField).value = time;
}

function validateCCNumber(ccNumber) {
	var validationMsg = "";
	if (ccNumber.length <= 0) {
		return validationMsg;
	}
	if (ccNumber.length < 15 
			|| ccNumber.length > 19) {
		validationMsg = "Credit card no. should be 15 or 16 digits in length, ";
	}
	return validationMsg;
}

/*function validate() {
	var error = false;
	var ids = ["companyName", "billingAddressLine1", "city"];
	for (var i= 0; i < ids.length; i++) {	
		if ($("#typeForm").find('input[id="'+ids[i] +'"]').val().length == 0) {
			$("#typeForm").find('input[id="'+ids[i] +'"]').addClass("border");
			error = true;
		}		
	} 
	
	if (error) {
		//$("#validations").html("Please fill out the required fields Name, Address Line 1 and City");	
		return false;
	} else {
		return true;
	}
}*/
