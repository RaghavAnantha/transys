package com.transys.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class LoginController {
	@RequestMapping(value = "login.do", method = RequestMethod.GET)
	public String displayLogin(HttpServletRequest request, ModelMap model) {
		return "login/login";
	}
}
