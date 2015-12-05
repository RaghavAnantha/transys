package com.transys.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.transys.controller.editor.AbstractModelEditor;
import com.transys.model.PaymentMethodType;
import com.transys.model.PermitFee;
import com.transys.model.SearchCriteria;

@Controller
@RequestMapping("/masterData/paymentMethod")
public class PaymentMethodController extends CRUDController<PaymentMethodType> {
	public PaymentMethodController() {
		setUrlContext("masterData/paymentMethod");
	}

	@Override
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(PaymentMethodType.class, new AbstractModelEditor(PaymentMethodType.class));
		super.initBinder(binder);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(PaymentMethodType.class, criteria, "method", null, null));
		return urlContext + "/list";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		// TODO:
		criteria.getSearchMap().remove("_csrf");
		criteria.setPageSize(25);
		model.addAttribute("list", genericDAO.search(PaymentMethodType.class, criteria, "method", false));
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
		return urlContext + "/form";
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(PaymentMethodType.class, criteria, "method", false));
		
		return urlContext + "/list";
	}

	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("paymentMethods", genericDAO.findByCriteria(PaymentMethodType.class, criterias, "method", false));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/save.do")
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") PaymentMethodType entity,
			BindingResult bindingResult, ModelMap model) {
		super.save(request, entity, bindingResult, model);

		model.addAttribute("msgCtx", "managePaymentMethods");
		model.addAttribute("msg", "Payment Method saved successfully");
		
		if (entity.getModifiedBy() == null) {
			model.addAttribute("modelObject", new PaymentMethodType());
		}
		
		return urlContext + "/form";
	}
}
