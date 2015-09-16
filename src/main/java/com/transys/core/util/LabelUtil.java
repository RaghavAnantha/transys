package com.transys.core.util;

import com.transys.core.tags.CacheUtil;

public class LabelUtil {
    public static String getText(String code, String locale) {
	if (locale == null) {
	    locale = "en_US";
	}
	//return CacheUtil.getText("messageResourceCache", "label_" + code + "_" + locale);
	return  code;

    }
}
