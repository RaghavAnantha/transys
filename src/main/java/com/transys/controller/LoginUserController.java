package com.transys.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.transys.controller.editor.AbstractModelEditor;
import com.transys.model.Employee;
import com.transys.model.EmployeeStatus;
import com.transys.model.Role;
import com.transys.model.SearchCriteria;
import com.transys.model.User;

@Controller
@RequestMapping("/masterData/loginUser")
public class LoginUserController extends CRUDController<User> {

	@Autowired
   private PasswordEncoder passwordEncoder;

	public LoginUserController(){
		setUrlContext("masterData/loginUser");
	}

	@Override
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(EmployeeStatus.class, new AbstractModelEditor(EmployeeStatus.class));
		super.initBinder(binder);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(User.class, criteria, "id", null, null));
		return urlContext + "/list";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		// TODO:
		criteria.getSearchMap().remove("_csrf");
		criteria.setPageSize(25);
		model.addAttribute("list",genericDAO.search(User.class, criteria));
		return urlContext + "/list";
	}
	
	@Override
	public String create(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		
		Map criterias = new HashMap();
		model.addAttribute("employees", genericDAO.findByCriteria(Employee.class, criterias, "id", false));
				
//		super.create(model, request);
		return urlContext + "/form";
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}	
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("employees", genericDAO.findByCriteria(Employee.class, criterias, "id", false));
		model.addAttribute("employeeStatus", genericDAO.findByCriteria(EmployeeStatus.class, criterias, "status", false));
		model.addAttribute("roles", genericDAO.findByCriteria(Role.class, criterias, "name", false));
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/save.do")
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") User entity,
			BindingResult bindingResult, ModelMap model) {
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.getSearchMap().remove("_csrf");
		
		// encode the password before storing in database
		entity.setPassword(passwordEncoder.encode(entity.getPassword()));
		super.save(request, entity,  bindingResult, model);
		
		model.addAttribute("msgCtx", "manageLoginUsers");
		model.addAttribute("msg", "Login User saved successfully");
		
		return urlContext + "/form";
		
	}
}
