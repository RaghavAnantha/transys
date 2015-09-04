package com.transys.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//import com.transys.model.BusinessObject;
import com.transys.model.User;
//import com.transys.model.menu.MenuTree;
//import com.transys.model.menu.Node;

@Controller
@SuppressWarnings("unchecked")
public class HomepageController extends GenericController {

	@RequestMapping("/home")
	public String displayMainpage(HttpServletRequest request) {
		/*User user = getUserInfo(request);
		MenuTree menuTree = (MenuTree)request.getSession().getAttribute("menuTree");
		List<Node<BusinessObject>> objects = menuTree.toList();
		if (objects!=null && objects.size()>=1) {
			return "redirect:/"+objects.get(1).getData().getAction();
		}*/
		return "home";
		/*if ("ADMIN".equalsIgnoreCase(user.getRole().getName())) {
			return "redirect:/admin/home.do";
		}
		if ("OPERATOR".equalsIgnoreCase(user.getRole().getName())) {
			return "redirect:/operator/home.do";
		}
		if ("REPORTUSER".equalsIgnoreCase(user.getRole().getName())) {
			return "redirect:/reportuser/home.do";
		}*/
		//return null;
	}

}
