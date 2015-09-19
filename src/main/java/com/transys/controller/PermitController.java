package com.transys.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.transys.controller.editor.AbstractModelEditor;
import com.transys.model.AbstractBaseModel;
import com.transys.model.Address;
import com.transys.model.BaseModel;
import com.transys.model.Customer;
import com.transys.model.LocationType;
import com.transys.model.Order;
import com.transys.model.OrderPermits;
import com.transys.model.Permit;
import com.transys.model.PermitClass;
import com.transys.model.PermitNotes;
import com.transys.model.PermitStatus;
import com.transys.model.PermitType;
import com.transys.model.SearchCriteria;

@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/permit")
public class PermitController extends CRUDController<Permit> {
	
	public PermitController(){
		setUrlContext("permit");
	}
	
	@Override
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Customer.class, new AbstractModelEditor(Customer.class));
		binder.registerCustomEditor(LocationType.class, new AbstractModelEditor(LocationType.class));
		binder.registerCustomEditor(PermitClass.class, new AbstractModelEditor(PermitClass.class));
		binder.registerCustomEditor(PermitType.class, new AbstractModelEditor(PermitType.class));
		binder.registerCustomEditor(Address.class, new AbstractModelEditor(Address.class));
		binder.registerCustomEditor(PermitNotes.class, new AbstractModelEditor(PermitNotes.class));
		super.initBinder(binder);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
//		criteria.getSearchMap().put("id!",0l);
//		model.addAttribute("list", createViewObjects(genericDAO.search(getEntityClass(), criteria, "number", null, null)));
		model.addAttribute("list", genericDAO.search(Permit.class, criteria, "id", null, null));
		// add order# corresponding to this permit in the attribute
		
		model.addAttribute("activeTab", "managePermits");
		model.addAttribute("mode", "MANAGE");
		return urlContext + "/permit";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/listOrderPermit.do")
	public String displayOrderPermitsAlert(ModelMap model, HttpServletRequest request) {
		System.out.println("Reached here?");
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		List<OrderPermits> orderPermits = genericDAO.search(OrderPermits.class, criteria, "id", null, null);
		model.addAttribute("orderPermitList", orderPermits);
		
		model.addAttribute("activeTab", "orderPermitAlert");
//		model.addAttribute("mode", "MANAGE");
		return urlContext + "/permit";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/notes.do")
	public String displayNotes(ModelMap model, HttpServletRequest request) {
		return urlContext + "/permit";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.CRUDController#setupList(org.springframework.ui.ModelMap, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}	
	
	@Override
	public String create(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		model.addAttribute("activeTab", "managePermits");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "permitDetails");
		
		model.addAttribute("notesModelObject", new PermitNotes());
//		model.addAttribute("orderPermitList", new OrderPermits());
				
		return urlContext + "/permit";
	}
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupUpdate(model, request);
		
		model.addAttribute("activeTab", "managePermits");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "permitDetails");

		Permit permitToBeEdited = (Permit)model.get("modelObject");
		
		Permit emptyPermit = new Permit();
		emptyPermit.setId(permitToBeEdited.getId());
		PermitNotes notes = new PermitNotes();
		notes.setPermit(emptyPermit);
		model.addAttribute("notesModelObject", notes);
	
		List<BaseModel> notesList = genericDAO.executeSimpleQuery("select obj from PermitNotes obj where obj.permit.id=" +  permitToBeEdited.getId() + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		// only in cases of Edit, an order ID can be associated with the permit
		OrderPermits orderPermitObj = (OrderPermits)genericDAO.executeSimpleQuery("select obj from OrderPermits obj where obj.permit.id=" +  permitToBeEdited.getId() + " order by obj.id desc").get(0);
		model.addAttribute("associatedOrderID", orderPermitObj);
	
		return urlContext + "/permit";
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		
		List<Address> addresses = genericDAO.findUniqueByCriteria(Address.class, criterias, "line1", false);
	   model.addAttribute("deliveryAddress", addresses);
		model.addAttribute("customer", genericDAO.findByCriteria(Customer.class, criterias, "contactName", false));
		model.addAttribute("locationType", genericDAO.findByCriteria(LocationType.class, criterias, "id", false));
		model.addAttribute("order", genericDAO.findByCriteria(Order.class, criterias, "id", false));
		model.addAttribute("permitClass", genericDAO.findByCriteria(PermitClass.class, criterias, "permitClass", false));
		model.addAttribute("permitType", genericDAO.findByCriteria(PermitType.class, criterias, "permitType", false));
		model.addAttribute("permitStatus", genericDAO.findByCriteria(PermitStatus.class, criterias, "status", false));
		model.addAttribute("permit", genericDAO.findByCriteria(Permit.class, criterias, "id", false));
		model.addAttribute("orderPermitList", genericDAO.findByCriteria(OrderPermits.class, criterias, "id", false));
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/save.do")
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") Permit entity,
			BindingResult bindingResult, ModelMap model) {
		
			String status = "Pending";
			if (entity.getNumber() != null && entity.getNumber().length() > 0) {
				status = "Available";
			} 
			
			System.out.println("Setting Permit Status to " + status);
			PermitStatus permitStatus = (PermitStatus)genericDAO.executeSimpleQuery("select obj from PermitStatus obj where obj.status='" + status + "'").get(0);
			entity.setStatus(permitStatus);
			
			super.save(request, entity, bindingResult, model);
			
			return saveSuccess(model, request, entity);
	}
	
	public String saveSuccess(ModelMap model, HttpServletRequest request, Permit entity) {
		setupCreate(model, request);
		model.addAttribute("modelObject", entity);
		model.addAttribute("activeTab", "managePermits");
		model.addAttribute("activeSubTab", "permitNotes");
		model.addAttribute("mode", "ADD");
		
		Permit permit = new Permit();
		permit.setId(entity.getId());
		PermitNotes notes = new PermitNotes();
		notes.setPermit(permit);
		model.addAttribute("notesModelObject", notes);
		List<BaseModel> notesList = genericDAO.executeSimpleQuery("select obj from PermitNotes obj where obj.permit.id=" +  entity.getId() + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
		//return urlContext + "/form";
		return urlContext + "/permit";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/customerDeliveryAddress")
	public @ResponseBody String displayCustomerDeliveryAddress(ModelMap model, HttpServletRequest request) {
		String customerId = request.getParameter("customerId");
		List<Address> addressList  = genericDAO.executeSimpleQuery("select obj from Address obj where obj.customer.id=" + customerId);
		String json = (new Gson()).toJson(addressList);
		return json;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/calculatePermitEndDate")
	public @ResponseBody String calculatePermitEndDate(ModelMap model, HttpServletRequest request) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String dateInString = request.getParameter("startDate");
		Date startDateObj = null;
		try {
			startDateObj = formatter.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
				
		String permitType = request.getParameter("permitType");
		PermitType permitTypeObj = (PermitType) genericDAO.executeSimpleQuery("select obj from PermitType obj where obj.id=" + permitType).get(0);
		if (startDateObj != null && permitTypeObj.getPermitType() != null) {
			String tokens[] = permitTypeObj.getPermitType().split("\\s");
			int noOfDays = new Integer(tokens[0]).intValue();
			Date endDate = DateUtils.addDays(startDateObj, noOfDays);
			System.out.println("End date = " + endDate);
			return formatter.format(endDate);
		} else {
			return null;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/savePermitNotes.do")
	public String savePermitNotes(HttpServletRequest request,
			@ModelAttribute("notesModelObject") PermitNotes entity,
			BindingResult bindingResult, ModelMap model) {
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}
		// return to form if we had errors
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);
			return urlContext + "/form";
		}
		//beforeSave(request, entity, model);
		if (entity instanceof AbstractBaseModel) {
			AbstractBaseModel baseModel = (AbstractBaseModel) entity;
			if (baseModel.getId() == null) {
				baseModel.setCreatedAt(Calendar.getInstance().getTime());
				if (baseModel.getCreatedBy()==null) {
					baseModel.setCreatedBy(getUser(request).getId());
				}
			} else {
				baseModel.setModifiedAt(Calendar.getInstance().getTime());
				if (baseModel.getModifiedBy()==null) {
					baseModel.setModifiedBy(getUser(request).getId());
				}
			}
		}
		
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);

		setupCreate(model, request);
		//model.addAttribute("modelObject", entity);
		/*model.addAttribute("activeTab", "managePermits");
		model.addAttribute("activeSubTab", "permitNotes");
		model.addAttribute("mode", "ADD");*/
		
		return urlContext + "/list";
	}
}
