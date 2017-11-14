package com.transys.controller;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
		binder.registerCustomEditor(Employee.class, new AbstractModelEditor(Employee.class));
		binder.registerCustomEditor(Role.class, new AbstractModelEditor(Role.class));
		super.initBinder(binder);
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(User.class, criteria, "employee.firstName asc, employee.lastName asc", null, null));
		return urlContext + "/list";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(User.class, criteria, "employee.firstName asc, employee.lastName asc", null, null));
		return urlContext + "/list";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		// TODO:
		criteria.getSearchMap().remove("_csrf");
		criteria.setPageSize(25);
		model.addAttribute("list", genericDAO.search(User.class, criteria, "employee.firstName asc, employee.lastName asc", null, null));
		return urlContext + "/list";
	}
	
	@Override
	public String create(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		
		Map criterias = new HashMap();
		//model.addAttribute("employees", genericDAO.findByCriteria(Employee.class, criterias, "firstName", false));
				
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
		model.addAttribute("employeeStatus", genericDAO.findByCriteria(EmployeeStatus.class, criterias, "status", false));
		model.addAttribute("roles", genericDAO.findByCriteria(Role.class, criterias, "name", false));
		
		SearchCriteria searchCriteria = new SearchCriteria();
		model.addAttribute("employees", genericDAO.search(Employee.class, searchCriteria, "firstName asc, lastName asc", null));
	}
	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") User entity,
			BindingResult bindingResult, ModelMap model) {
		setupCreate(model, request);
		model.addAttribute("msgCtx", "manageLoginUsers");
		
		// Encode the password before storing in database
		entity.setPassword(passwordEncoder.encode(entity.getPassword()));
		try {
			beforeSave(request, entity, model);
			genericDAO.saveOrUpdate(entity);
			cleanUp(request);
		} catch (PersistenceException e){
			String errorMsg = extractSaveErrorMsg(e);
			model.addAttribute("error", errorMsg);
			
			return urlContext + "/form";
		}
		
		model.addAttribute("msg", "Login User saved successfully");
		
		if (entity.getModifiedBy() == null) {
			model.addAttribute("modelObject", new User());
		}
				
		return urlContext + "/form";
	}
	
	private String extractSaveErrorMsg(Exception e) {
		String errorMsg = StringUtils.EMPTY;
		if (isConstraintError(e, "employee")) {
			errorMsg = "Duplicate employee id - employee id already exists"; 
		} else if (isConstraintError(e, "username")) {
			errorMsg = "Duplicate username - username already exists"; 
		} else {
			errorMsg = "Error occured while saving User";
		}
		
		return errorMsg;
	}
}
