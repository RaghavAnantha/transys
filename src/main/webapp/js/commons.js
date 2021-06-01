function log(msg) {
	console.log(msg);
}

function findTabToBeShown(tab) {
	var tabToBeUsed = tab;
	if (tab.indexOf(".do") == -1) {
		tabToBeUsed = "#" + tabToBeUsed;
	}
	
	return $('.nav-tabs a[href="' + tabToBeUsed + '"]');
}

function showTab(tab) {
	if (tab.length == 0) {
		return;
	}
	
	var $tabToBeShown = findTabToBeShown(tab);
	$tabToBeShown.tab('show');
}

function loadTab($tab, loadUrlPassed) {
	if (dataToggle == 'tab') {
		$tab.tab('show');
		return false;
	}
	
	var loadUrlToBeUsed = $tab.attr('href'),
		dataTarget = $tab.attr('data-target'),
		dataToggle = $tab.attr('data-toggle');
	if (loadUrlPassed != null) {
		loadUrlToBeUsed = loadUrlPassed;
	}
	
    $.get(loadUrlToBeUsed, function(data) {
        $(dataTarget).html(data);
    });

    //$tab.attr('data-toggle', 'tab')
    $tab.tab('show');
    return false;
}

function emptySelect(selectElem) {
	selectElem.empty();
	
	var firstOption = $('<option value="">'+ "-----Please Select-----" +'</option>');
	selectElem.append(firstOption);
}

function processBulkDelete() {
	var inputs = document.getElementsByName("id");
	var submitForm = false;
	for ( var i = 0; i < inputs.length; i++) {
		if (inputs[i].checked == true) {
			submitForm = true;
		}
	}
	if (submitForm) {
		if(confirm("Do you want to delete the selected record(s)?")) {
			document.deleteForm.action = "bulkdelete.do";
			document.deleteForm.submit();
		}
	} else {
		alert("Please select at least one record");
	}
}
				
function openWindow(url,title){
	 document.getElementById("iframe").src=url;
	 $('#popUpWindow').AeroWindow({
	      WindowTitle:          title,
	      WindowPositionTop:    'center',
	      WindowPositionLeft:   'center',
	      WindowWidth:          700,
	      WindowHeight:         300,
	      WindowAnimationSpeed: 1000,
	      WindowAnimation:      'easeOutCubic',
	      WindowResizable:      true,
	      WindowDraggable:      true,
	      WindowMinimize:       false,
	      WindowMaximize:       true,
	      WindowClosable:       true   
	              
	    });
}

$(function() {
	$( "#datepicker" ).datepicker({
		dateFormat: 'mm/dd/yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	$( "#datepicker1" ).datepicker({
		dateFormat: 'mm/dd/yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	$( "#datepicker2" ).datepicker({
		dateFormat: 'mm/dd/yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	$( "#datepicker3" ).datepicker({
		dateFormat: 'mm/dd/yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	$( "#datepicker4" ).datepicker({
		dateFormat: 'mm/dd/yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	
	$( "#datepicker5" ).datepicker({
		dateFormat: 'mm/dd/yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	
	$( "#datepicker6" ).datepicker({
		dateFormat: 'mm/dd/yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	$( "#datepicker7" ).datepicker({
		dateFormat: 'mm/dd/yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	$( "#datepicker8" ).datepicker({
		dateFormat: 'mm/dd/yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	$( "#datepicker9" ).datepicker({
		dateFormat: 'mm/dd/yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	$( "#datepicker10" ).datepicker({
		dateFormat: 'mm/dd/yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	$( "#datepicker11" ).datepicker({
		dateFormat: 'mm/dd/yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	$( "#datepicker12" ).datepicker({
		dateFormat: 'mm/dd/yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	$( "#datepicker13" ).datepicker({
		dateFormat: 'mm/dd/yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
	$( "#datepicker14" ).datepicker({
		dateFormat: 'mm/dd/yy',
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true
	});
});

function processDelete(url, id) {
	if (confirm("Do you want to Delete the selected record with id: " + id + "?")) {
		document.location = url;
	}	
}

function processCancel(url, id) {
	if (confirm("Do you want to Cancel the selected record with id: " + id + "?")) {
		document.location = url;
	}	
}

function processManageDocs(url, id) {
	document.location = url;
}

function processItemPrint(url, id) {
	document.location = url;
}

function processItemPrintHref(id) {
	var href = "printItem.do?id=" + id;
	document.location.href = href
}

function hideTr( objId ){
	var obj =  document.getElementById( objId );
	if( obj != null )
		obj.style.display="none";
}

function showTr( objId ){
	var obj =  document.getElementById( objId );
	if( obj != null )
		obj.style.display="table-row";
}

function enableButton(elementId) {
	var obj = document.getElementById(elementId);
	if(obj != null)
		obj.disabled = false;
}

function updateParentWindowInputValue( objId, value ){
	var obj =  top.document.getElementById( objId );
	if( obj != null )
		obj.value=value;
}

function checkUncheck(currObj, objId) {
    var checked;
    if (currObj.checked==true){
        checked=true;
    }
    else {
        checked=false;
    }
    var obj = document.getElementsByName(objId);
    if (obj==null)
        return;
    if (obj.length == 1 && obj.disabled != true) {
        obj.checked = checked;
    }
    else {
        for(var i=0; i< obj.length; i++) {
	         if(obj[i].disabled != true)
            	obj[i].checked=checked;
        }
    }
}

function popup(url) {
 	params  = 'width='+screen.width/2;
 	params += ', height='+screen.height/2;
 	params += ', titlebar=no';
 	params += ', scrollbars=yes';
 	params += ', resizable=yes';
 	params += ', toolbar=no';
 	params += ', location=no';
 	params += ', menubar=no';
 	params += ', directories=no';
 	params += ', status=yes';
 	params += ', top='+screen.height/4;
 	params += ', left='+screen.width/4;
 	newwin=window.open(url,'Print Window', params);
 	if (window.focus) {
	 	newwin.focus();
	 	}
 	return false;
}

GB_AdShow = function(caption, url,height, width, callback_fn) {
    var options = {
        caption: caption,
        height: height || 500,
        width: width || 950,
        fullscreen: false,
        show_loading: true,
        center_win:true,
        callback_fn: callback_fn
    };
    var win = new GB_Window(options);
    return win.show(url);
   
};

/**
 * Function to allow only numbers and/or doubles
 * @param evt window's event
 * @return
 */
function onlyNumbers(evt, dec) {
	var theEvent = evt || window.event;
	var key = theEvent.keyCode || theEvent.which;
	var keychar = String.fromCharCode(key);
	// allow decimal if dec is true
	if (dec && (keychar == "."))
		return true;
	if (key > 31 && (key < 48 || key > 57))
		return false;
	return true;
}

function toJSDate(screenDateStr) {
	// 07-26-2020
	var dateStrSplit = screenDateStr.split('-');
	// 2020-07-26
	var jsDateStr = dateStrSplit[2] + "-" + dateStrSplit[0] + "-" + dateStrSplit[1];
	return new Date(jsDateStr);
}

function dateDiff(fromDate, toDate) {
    // Take the difference between the dates and divide by milliseconds per day.
    // Round to nearest whole number to deal with DST.
    return Math.round((toDate - fromDate)/(1000*60*60*24));
}

function dateDiffForDateStr(fromDateStr, toDateStr) {
	var fromDate = toJSDate(fromDateStr);
	var toDate = toJSDate(toDateStr);
    return dateDiff(fromDate, toDate);
}

function formatPhone(phone) {
	if (phone.trim() == "") {
		return phone;
	}
	
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

function formatCCNumber(ccNumber) {
	var formattedCCNum = ccNumber;
	if (ccNumber.length < 15 
			|| ccNumber.length > 16) {
		return formattedCCNum;
	}
	
	var formattedCCNum = ccNumber.substring(0, 4);
	if (ccNumber.length == 16) {
		formattedCCNum += ("-xxxx-xxxx-" + ccNumber.substring(12));
	} else {
		formattedCCNum += ("-xxxx-xxx-" + ccNumber.substring(11));
	}
	return formattedCCNum;
}

function clearTextAndFocus(field) {
	document.getElementById(field).value = "";
	$("#"+field).focus();
}

function isImageLoaded(img) {
    // check for IE
    if (!img.complete) {
        return false;
    }
    // check for Firefox
    if (typeof img.naturalWidth != "undefined" && img.naturalWidth == 0) {
        return false;
    }
    //console.log("Complete: " + img.complete);
    //console.log("Natural width: " + img.naturalWidth);
    
    // assume it's ok
    return true;
}

function areReportImagesLoaded(ctx) {
	var imgLoaded = true;
	$("img").filter("[src^='" + ctx + "/image?image=img_']").each(function() {
		var img = $(this)[0];
		if (!isImageLoaded(img)) {
			imgLoaded = false;
		}
	});
	return imgLoaded;
}

function removeJasperPrint() {
	jQuery.ajax({
		url:'ajax.do?action=removeJasperPrint', 
		success: function(data) {
		}
	});
}

function verifyAndRemoveJasperPrint(ctx) {
	if (areReportImagesLoaded(ctx)) {
		removeJasperPrint();
	}
}

function processDGRowShowDetails(id, expandableId, expandableQualifier) {
	var detailsImageElem = $("#" + expandableId);
	
	var currentRow = detailsImageElem.closest('tr');
	var newTrId = expandableId + "_tr";
	var newTdId = expandableId + "_td";
	
	var imgClass = detailsImageElem.attr('class');
	if (imgClass.indexOf("plus") != -1) { 
		imgClass = imgClass.replace("plus", "minus");
		detailsImageElem.attr("class", imgClass);
		
		var newRowStr = "<tr id=\"" + newTrId + "\"><td></td><td colspan=\"" + getDGRowDetailsDataColSpan() + "\" id=\"" + newTdId + "\"></td></tr>";
		$(newRowStr).insertAfter(currentRow);
		
		var fnToCall = "addDGRowDetailsData_" + expandableQualifier;
		executeFunctionByName(fnToCall, window, id, newTdId);
	} else { 
		imgClass = imgClass.replace("minus", "plus");
		detailsImageElem.attr("class", imgClass);
		
		$("#" + newTrId).remove();
	}
}

function executeFunctionByName(functionName, context /*, args */) {
	//window["functionName"](arguments);
	//window["My"]["Namespace"]["functionName"](arguments);
	
	var args = Array.prototype.slice.call(arguments, 2);
	var namespaces = functionName.split(".");
	var func = namespaces.pop();
	for(var i = 0; i < namespaces.length; i++) {
		context = context[namespaces[i]];
	}
	return context[func].apply(context, args);
}

function setExpiryDatepicker($expiryDateElem) {
	$expiryDateElem.datepicker( {
		changeMonth: true,
        changeYear: true,
        showButtonPanel: true,
        dateFormat: 'mm/yy',
		onClose: function(dateText, inst) { 
	    	onExpiryDatepickerClose($expiryDateElem);
	    },
	    beforeShow : function(input, inst) {
	    	beforeExpiryDatepickerShow($expiryDateElem);
	    }
	});
}

function onExpiryDatepickerClose($expiryDateElem) { 
	var iMonth = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
	var iYear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
    $expiryDateElem.datepicker('setDate', new Date(iYear, iMonth, 1));
    
    $('#ui-datepicker-div').removeClass('hide-calendar');
}

function beforeExpiryDatepickerShow($expiryDateElem) {
	$('#ui-datepicker-div').addClass('hide-calendar');
	
	var selDate = $expiryDateElem.val();
	if (selDate.length > 0) {
		iYear = selDate.substring(selDate.length - 4, selDate.length);
		//iMonth = jQuery.inArray(selDate.substring(0, selDate.length - 5), $(this).datepicker('option', 'monthNames'));
		iMonth = selDate.substring(0, 2);
		iMonth = iMonth-1;
		$expiryDateElem.datepicker('option', 'defaultDate', new Date(iYear, iMonth, 1));
		$expiryDateElem.datepicker('setDate', new Date(iYear, iMonth, 1));
    }
}

function setDatepickerAutoComplete(setting) {
	$(".hasDatepicker").attr('autocomplete', setting);
}

function setDatepickerAutoCompleteOff() {
	setDatepickerAutoComplete('off');
}

function setDatepickerAutoCompleteOn() {
	setDatepickerAutoComplete('on');
}



