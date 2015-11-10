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
	if (!/(^\d+\.\d{2}$)|(^\d+$)/.test(weight)) {
		return false;
	}
	
	if (weight !=  null && weight > maxValue) {
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

function validateCompanyName(name, validLength) {
	var companyNamePattern = new RegExp("^[a-zA-Z0-9-_'`\\s/]{1," + validLength + "}$");
	return companyNamePattern.test(name);
}

function validateReferenceNum(refNum, validLength) {
	var refNumPattern = new RegExp("^[a-zA-Z0-9-_():.,/*&%$#\\s\\\\]{1," + validLength + "}$");
	return refNumPattern.test(refNum);
}