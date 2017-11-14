package com.transys.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.transys.model.JobTitle;
import com.transys.model.Role;
import com.transys.model.SearchCriteria;
import com.transys.model.State;
import com.transys.model.User;

@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/masterData/employee")
public class EmployeeController extends CRUDController<Employee> {
	@Autowired
   private PasswordEncoder passwordEncoder;
	
	public EmployeeController(){
		setUrlContext("masterData/employee");
	}

	@Override
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(JobTitle.class, new AbstractModelEditor(JobTitle.class));
		binder.registerCustomEditor(State.class, new AbstractModelEditor(State.class));
		binder.registerCustomEditor(EmployeeStatus.class, new AbstractModelEditor(EmployeeStatus.class));
		super.initBinder(binder);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(Employee.class, criteria, "firstName asc, lastName asc", null, null));
		return urlContext + "/list";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		// TODO:
		criteria.getSearchMap().remove("_csrf");
		criteria.setPageSize(25);
		model.addAttribute("list", genericDAO.search(Employee.class, criteria, "firstName asc, lastName asc", null, null));
		return urlContext + "/list";
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(Employee.class, criteria, "firstName asc, lastName asc", null, null));
		return urlContext + "/list";
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}	
	
	@Override
	public String create(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		
		Map criterias = new HashMap();
		model.addAttribute("employee", genericDAO.findByCriteria(Employee.class, criterias, "firstName", false));
				
//		super.create(model, request);
		return urlContext + "/form";
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("jobTitleValues", genericDAO.findByCriteria(JobTitle.class, criterias, "jobTitle", false));
		model.addAttribute("state", genericDAO.findByCriteria(State.class, criterias, "name", false));
		model.addAttribute("employeeStatus", genericDAO.findByCriteria(EmployeeStatus.class, criterias, "status", false));
		
		SearchCriteria searchCriteria = new SearchCriteria();
		model.addAttribute("employee", genericDAO.search(Employee.class, searchCriteria, "firstName asc, lastName asc", null));
	}
	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") Employee entity,
			BindingResult bindingResult, ModelMap model) {
		setupCreate(model, request);
		model.addAttribute("msgCtx", "manageEmployees");
		
		try {
			beforeSave(request, entity, model);
			genericDAO.saveOrUpdate(entity);
			updateUserInfo(entity);
			cleanUp(request);
		} catch (PersistenceException e){
			String errorMsg = extractSaveErrorMsg(e);
			model.addAttribute("error", errorMsg);
			
			return urlContext + "/form";
		}
		
		model.addAttribute("msg", "Employee saved successfully");
		
		if (entity.getModifiedBy() == null) {
			model.addAttribute("modelObject", new Employee());
		}
				
		return urlContext + "/form";
	}
	
	private void updateUserStatus(User userInfo, Employee entity) {
		if (userInfo == null) {
			return;
		}
		if (userInfo.getAccountStatus().getId().longValue() == entity.getStatus().getId().longValue()) {
			return;
		}
		
		userInfo.setAccountStatus(entity.getStatus());
		updateUserInfo(userInfo, entity, null);
	}
	
	private void updateUserInfo(Employee entity) {
		User userInfo = retrieveUserInfo(entity);
		updateUserStatus(userInfo, entity);
		
		JobTitle jobTitle = entity.getJobTitle();
		if (jobTitle == null) {
			return;
		}
		
		String driverJobTitleIds = "|2|3|4|7|";
		String jobTitleId = String.valueOf(jobTitle.getId().longValue());
		if (!StringUtils.contains(driverJobTitleIds, "|"+jobTitleId+"|")) {
			return;	
		}
		
		if (userInfo == null) {
			userInfo = createUserInfo(entity, Role.DRIVER_ROLE);
		} else {
			/*if (!StringUtils.equals(Role.DRIVER_ROLE, userInfo.getRole().getName())) {
				updateUserInfo(userInfo, entity, Role.DRIVER_ROLE);
			}*/
		}
	}
	
	private User retrieveUserInfo(Employee entity) {
		String userQuery = "select obj from User obj where obj.deleteFlag='1' and obj.employee.id=" + entity.getId();
		List<User> userList = genericDAO.executeSimpleQuery(userQuery);
		return (userList == null || userList.isEmpty() ? null : userList.get(0));
	}
	
	private User createUserInfo(Employee entity, String roleStr) {
		User userInfo = new User();
		userInfo.setEmployee(entity);
		
		Role role = retrieveRole(roleStr);
		userInfo.setRole(role);
		
		populateUserCredentials(userInfo, entity);
		
		//EmployeeStatus employeeStatus = retrieveEmployeestatus(EmployeeStatus.ACTIVE_STATUS);
		userInfo.setAccountStatus(entity.getStatus());
		
		Long createdBy = (entity.getModifiedBy() != null ? entity.getModifiedBy() : entity.getCreatedBy());
		Date createdAt = (entity.getModifiedAt() != null ? entity.getModifiedAt() : entity.getCreatedAt());
		userInfo.setCreatedBy(createdBy);
		userInfo.setCreatedAt(createdAt);
		
		userInfo.setComments("AUTO_CREATED while creating/updating employee");
		genericDAO.saveOrUpdate(userInfo);
		return userInfo;
	}
	
	private void updateUserInfo(User userInfo, Employee entity, String roleStr) {
		if (StringUtils.isNotEmpty(roleStr)) {
			Role role = retrieveRole(roleStr);
			userInfo.setRole(role);
		}
		
		userInfo.setModifiedAt(entity.getModifiedAt());
		userInfo.setModifiedBy(entity.getModifiedBy());
		
		userInfo.setComments("AUTO_UPDATED while creating/updating employee");
		genericDAO.saveOrUpdate(userInfo);
	}
	
	private EmployeeStatus retrieveEmployeestatus(String statusStr) {
		String statusQuery = "select obj from EmployeeStatus obj where obj.deleteFlag='1' and obj.status='" + statusStr + "'";
		List<EmployeeStatus> statusList = genericDAO.executeSimpleQuery(statusQuery);
		return (statusList == null || statusList.isEmpty() ? null : statusList.get(0));
	}
	
	private Role retrieveRole(String roleStr) {
		String roleQuery = "select obj from Role obj where obj.deleteFlag='1' and obj.name='" + roleStr + "'";
		List<Role> roleList = genericDAO.executeSimpleQuery(roleQuery);
		return (roleList == null || roleList.isEmpty() ? null : roleList.get(0));
	}
	
	private boolean isDuplicateUserName(String userName) {
		String baseQuery = "select obj from User obj where"; 
		String whereClause = " obj.username='"+userName+"'";		
		List<User> userList = genericDAO.executeSimpleQuery(baseQuery + whereClause);
		
		return !userList.isEmpty();
	}
	
	private void populateUserCredentials(User user, Employee entity) {
		String modFirstName = StringUtils.trimToEmpty(entity.getFirstName());
		String modLastName = StringUtils.trimToEmpty(entity.getLastName());
		
		modFirstName = StringUtils.lowerCase(modFirstName);
		modLastName = StringUtils.lowerCase(modLastName);
		
		String modLastNameTokens[] = modLastName.split("[-\\s]");
		modLastName = modLastNameTokens[0];
		
		String firstNameFirstChar = modFirstName.substring(0, 1);
		
		String origUserName = (modLastName + firstNameFirstChar);
		int i = 0;
		while (origUserName.length() < 5) {
			i++;
			origUserName += i;
		}
		
		String nonDupUserName = origUserName;
		i = 0;
		while (isDuplicateUserName(nonDupUserName)) {
			i++;
			nonDupUserName = (origUserName + i);
		}
		user.setUsername(nonDupUserName);
		
		String password = generatePassword(nonDupUserName);
		user.setPassword(password);
	}
	
	private String generatePassword(String userName) {
		String modUserName = StringUtils.trimToEmpty(userName);
		
		String password = modUserName + "123456";
		// Encode the password before storing in database
		password = passwordEncoder.encode(password);
		
		return password;
	}
	
	private String extractSaveErrorMsg(Exception e) {
		String errorMsg = StringUtils.EMPTY;
		if (isConstraintError(e, "employeeId")) {
			errorMsg = "Duplicate employee id - employee id already exists"; 
		} else if (isConstraintError(e, "employeeName")) {
			errorMsg = "Duplicate employee name - employee name already exists"; 
		} else {
			errorMsg = "Error occured while saving Employee";
		}
		
		return errorMsg;
	}
}
