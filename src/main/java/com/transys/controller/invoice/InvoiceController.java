package com.transys.controller.invoice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.math.BigDecimal;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import com.transys.service.DynamicReportService;

import com.transys.controller.BaseController;

import com.transys.core.util.CoreUtil;
import com.transys.core.util.MimeUtil;

import com.transys.model.Customer;
import com.transys.model.Order;
import com.transys.model.OrderFees;
import com.transys.model.SearchCriteria;

import com.transys.model.vo.invoice.InvoiceVO;

@Controller
@RequestMapping("/invoice")
public class InvoiceController extends BaseController {
	@Autowired
	private DynamicReportService dynamicReportService;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public InvoiceController() {
		setUrlContext("invoice");
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/createInvoiceMain.do")
	public String createInvoiceMain(ModelMap model, HttpServletRequest request) {
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (searchCriteria != null && searchCriteria.getSearchMap() != null) {
			searchCriteria.getSearchMap().clear();
		}
		
		setupCreateInvoiceList(model, request);
		
		return getUrlContext() + "/invoice";
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/manageInvoiceMain.do")
	public String manageInvoiceMain(ModelMap model, HttpServletRequest request) {
		/*SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (searchCriteria != null && searchCriteria.getSearchMap() != null) {
			searchCriteria.getSearchMap().clear();
		}*/
		
		setupManageInvoiceList(model, request);
		
		return getUrlContext() + "/manageInvoiceList";
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST }, value = "/createInvoiceSearch.do")
	public String createInvoiceSearch(ModelMap model, HttpServletRequest request,
			@ModelAttribute("modelObject") InvoiceVO input,
			BindingResult bindingResult) {
		setupCreateInvoiceList(model, request);
		
		String returnUrl = getUrlContext() + "/invoice";
		
		validateSearch(input, bindingResult);
		if(bindingResult.hasErrors()) {
        	return returnUrl;
      }
		
		request.getSession().setAttribute("input", input);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		List<Order> orderList = performCreateInvoiceSearch(criteria, input);
		model.addAttribute("list", orderList);
		
		return returnUrl;
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST }, value = "/manageInvoiceSearch.do")
	public String manageInvoiceSearch(ModelMap model, HttpServletRequest request) {
		setupManageInvoiceList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		List<InvoiceVO> invoiceVOList = performManageInvoiceSearch(criteria);
		model.addAttribute("list", invoiceVOList);
		
		return getUrlContext() + "/manageInvoiceList";
	}
	
	private void setupCreateInvoiceList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		searchCriteria.setPageSize(150);
		
		model.addAttribute("msgCtx", "createInvoice");
		model.addAttribute("errorCtx", "createInvoice");
		model.addAttribute("activeTab", "createInvoice");

		List<Customer> customerList = genericDAO.executeSimpleQuery("select obj from Customer obj where obj.deleteFlag='1' order by obj.companyName asc");
		model.addAttribute("customers", customerList);
	}
	
	private void setupManageInvoiceList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		searchCriteria.setPageSize(100);
		
		model.addAttribute("msgCtx", "manageInvoice");
		model.addAttribute("errorCtx", "manageInvoice");
		model.addAttribute("activeTab", "manageInvoice");

		List<Customer> customerList = genericDAO.executeSimpleQuery("select obj from Customer obj where obj.deleteFlag='1' order by obj.companyName asc");
		model.addAttribute("customers", customerList);
		
		List<String> invoiceNoList = new ArrayList<String>();
		invoiceNoList.add("1234");
		model.addAttribute("invoiceNos", invoiceNoList);
	}
	
	private List<Order> performCreateInvoiceSearch(SearchCriteria criteria, InvoiceVO input) {
		String customerId = input.getCustomerId();
		String orderDateFrom = input.getOrderDateFrom();
		String orderDateTo = input.getOrderDateTo();
		
		StringBuffer query = new StringBuffer("select obj from Order obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from Order obj where 1=1");
		StringBuffer whereClause = new StringBuffer(" and obj.deleteFlag=1");
		
		whereClause.append(" and obj.balanceAmountDue > " + 0.0);
		
		if (StringUtils.isNotEmpty(customerId)) {
			whereClause.append(" and obj.customer.id=" + customerId);
		}
		if (StringUtils.isNotEmpty(orderDateFrom)){
        	try {
        		whereClause.append(" and obj.createdAt >='"+sdf.format(dateFormat.parse(orderDateFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      if (StringUtils.isNotEmpty(orderDateTo)){
	     	try {
	     		whereClause.append(" and obj.createdAt <='"+sdf.format(dateFormat.parse(orderDateTo))+"'");
	     	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      
      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by obj.createdAt desc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<Order> orderList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList();
		
		return orderList;
	}
	
	private List<InvoiceVO> performManageInvoiceSearch(SearchCriteria criteria) {
		String customerId = (String) criteria.getSearchMap().get("manageInvoiceCustomerId");
		String orderDateFrom = (String) criteria.getSearchMap().get("manageInvoiceOrderDateFrom");
		String orderDateTo = (String) criteria.getSearchMap().get("manageInvoiceOrderDateTo");
		
		List<InvoiceVO> invoiceVOList = new ArrayList<InvoiceVO>();
		
		InvoiceVO invoiceVO = new InvoiceVO();
		invoiceVO.setId(1l);
		invoiceVO.setInvoiceNo("12345");
		invoiceVO.setInvoiceDate(new Date());
		invoiceVO.setCompanyName("City Of Chicago DSS");
		invoiceVO.setTotalFees(new BigDecimal(200.00));
		invoiceVO.setTotalAmountPaid(new BigDecimal(100.00));
		invoiceVO.setBalanceAmountDue(new BigDecimal(100.00));
		invoiceVOList.add(invoiceVO);
		
		criteria.setRecordCount(1);
		return invoiceVOList;
	}
	
	private void validateSearch(InvoiceVO input, BindingResult bindingResult) {
		if (StringUtils.isEmpty(input.getCustomerId())) {
			bindingResult.rejectValue("customerId", "NotNull.java.lang.String", null, null);
		}
		if (StringUtils.isEmpty(input.getOrderDateFrom())) {
			bindingResult.rejectValue("orderDateFrom", "NotNull.java.lang.String", null, null);
		}
		if (StringUtils.isEmpty(input.getOrderDateTo())) {
			bindingResult.rejectValue("orderDateTo", "NotNull.java.lang.String", null, null);
		}
	}
	
	private List<InvoiceVO> performPreviewInvoiceSearch(InvoiceVO input) {
		String[] orderIdsArr = input.getIds();
		if (orderIdsArr == null || orderIdsArr.length <= 0) {
			return new ArrayList<InvoiceVO>();
		}
		
		StringBuffer query = new StringBuffer("select obj from Order obj where 1=1");
		StringBuffer whereClause = new StringBuffer(" and obj.deleteFlag=1");
		
		String orderIds = CoreUtil.toString(orderIdsArr);
		whereClause.append(" and obj.id in(" + orderIds + ")");
      
      query.append(whereClause);
      
      query.append(" order by obj.createdAt desc");
      
      List<Order> orderList = genericDAO.executeSimpleQuery(query.toString());
		List<InvoiceVO> invoiceReportVOList = new ArrayList<InvoiceVO>();
		map(orderList, invoiceReportVOList);
		
		return invoiceReportVOList;
	}
	
	private void map(List<Order> orderList, List<InvoiceVO> invoiceReportVOList) {
		if (orderList == null || orderList.isEmpty()) {
			return;
		}
		
		for (Order anOrder : orderList) {
			InvoiceVO anInvoiceReportVO = new InvoiceVO();
			map(anOrder, anInvoiceReportVO);
			invoiceReportVOList.add(anInvoiceReportVO);
		}
	}
	
	private void map(Order anOrder, InvoiceVO anInvoiceVO) {
		anInvoiceVO.setId(anOrder.getId());
		anInvoiceVO.setOrderDate(anOrder.getFormattedCreatedAt());
		anInvoiceVO.setStatus(anOrder.getOrderStatus().getStatus());
		
		anInvoiceVO.setDeliveryContactName(anOrder.getDeliveryContactName());
		anInvoiceVO.setDeliveryContactPhone1(anOrder.getDeliveryContactPhone1());
		anInvoiceVO.setDeliveryAddressFullLine(anOrder.getDeliveryAddress().getFullLine());
		anInvoiceVO.setDeliveryCity(anOrder.getDeliveryAddress().getCity());
		
		anInvoiceVO.setDeliveryDate(anOrder.getFormattedDeliveryDate());
		anInvoiceVO.setPickupDate(anOrder.getFormattedPickupDate());
		
		OrderFees anOrderFees = anOrder.getOrderFees();
		anInvoiceVO.setDumpsterPrice(anOrderFees.getDumpsterPrice());
		
		anInvoiceVO.setCityFee(anOrderFees.getCityFee() == null ? (new BigDecimal("0.00")) : anOrderFees.getCityFee());
		
		anInvoiceVO.setPermitFees(anOrderFees.getTotalPermitFees());
		anInvoiceVO.setOverweightFee(anOrderFees.getOverweightFee());
		anInvoiceVO.setAdditionalFees(anOrderFees.getTotalAdditionalFees());
		anInvoiceVO.setTonnageFees(anOrderFees.getTonnageFee());
		anInvoiceVO.setTotalFees(anOrderFees.getTotalFees());
		
		anInvoiceVO.setTotalAmountPaid(anOrder.getTotalAmountPaid());
		anInvoiceVO.setBalanceAmountDue(anOrder.getBalanceAmountDue());
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/deleteInvoice.do")
	public String deleteInvoice(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("id") Long id) {
		//genericDAO.deleteById(Invoice.class, id);
		
		setSuccessMsg(request, "Invoice deleted sucessfully");
		return "redirect:/" + getUrlContext() + "/manageInvoiceSearch.do";
		//return getUrlContext() + "/manageInvoiceList";
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/saveInvoice.do")
	public String saveInvoice(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		setupPreviewInvoice(request, model);
		
		InvoiceVO input = (InvoiceVO) request.getSession().getAttribute("input");
		input.setHistoryCount(-2);
		
		setSuccessMsg(request, "Invoice saved sucessfully");
		
		return processPreviewInvoiceCommon(request, response, input);
	}	
	
	@RequestMapping(method = {RequestMethod.POST }, value = "/previewInvoice.do")
	public String previewInvoice(ModelMap model, HttpServletRequest request, HttpServletResponse response,
					@RequestParam("id") String[] ids,
					@RequestParam("invoiceNo") String invoiceNo,
					@RequestParam("invoiceDate") Date invoiceDate) {
		setupPreviewInvoice(request, model);
		
		InvoiceVO input = (InvoiceVO) request.getSession().getAttribute("input");
		input.setIds(ids);
		input.setInvoiceNo(invoiceNo);
		input.setInvoiceDate(invoiceDate);
		input.setHistoryCount(-1);
		
		return processPreviewInvoiceCommon(request, response, input);
	}
	
	private String processPreviewInvoiceCommon(HttpServletRequest request, HttpServletResponse response,
			InvoiceVO input) {
		Map<String, Object> datas = generateInvoiceData(request, input);
		List<InvoiceVO> invoiceReportVOList = (List<InvoiceVO>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		String type = "html";
		response.setContentType(MimeUtil.getContentType(type));
		
		String reportName = "previewInvoice";
		try {
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(reportName,
					invoiceReportVOList, params, request);
			if (jasperPrint == null) {
				setErrorMsg(request, response, "Error occured while processing invoice preview");
			} else {
				//request.setAttribute("japserPrint", jasperPrint);
				request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
			}
		} catch (Exception e) {
			e.printStackTrace();
			setErrorMsg(request, response, e.getMessage());
		}
		
		return getUrlContext() + "/previewInvoice";
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/previewInvoiceExport.do")
	public String previewInvoiceExport(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = true, value = "type") String type) {
		setupPreviewInvoice(request, model);
		
		InvoiceVO input = (InvoiceVO) request.getSession().getAttribute("input");
		Map<String, Object> datas = generateInvoiceData(request, input);
		List<InvoiceVO> invoiceReportVOList = (List<InvoiceVO>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		String reportName = "previewInvoice";
		if (!StringUtils.equals("html", type) && !StringUtils.equals("print", type)) {
			response.setHeader("Content-Disposition", "attachment;filename="+reportName+"." + type);
		}
		response.setContentType(MimeUtil.getContentType(type));
		
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.generateStaticReport(reportName,
						invoiceReportVOList, params, type, request);
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to generate static report:" + e);
			
			setupPreviewInvoice(request, model);
			setErrorMsg(request, response, e.getMessage());
			return getUrlContext() + "/previewInvoice";
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/createInvoiceExport.do")
	public String createInvoiceExport(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("type") String type,
			@RequestParam("dataQualifier") String dataQualifier,
			Object objectDAO, Class clazz) {
		response.setContentType(MimeUtil.getContentType(type));
		
		String reportName = "ordersBalanceDueReport";
		if (!StringUtils.equals("html", type) && !StringUtils.equals("print", type)) {
			response.setHeader("Content-Disposition", "attachment;filename=" + reportName + "." + type);
		}
		
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		InvoiceVO input = (InvoiceVO) request.getSession().getAttribute("input");
		List<Order> orderList = performCreateInvoiceSearch(searchCriteria, input);
		Map<String, Object> params = new HashMap<String, Object>();
		
		List columnPropertyList = (List) request.getSession().getAttribute(dataQualifier + "ColumnPropertyList");
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.exportReport(reportName, type, Order.class, orderList,
						columnPropertyList, request);
			/*out = dynamicReportService.generateStaticReport(reportName,
					orderList, params, type, request);*/
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to generate report:" + e);
			
			setupCreateInvoiceList(model, request);
			setErrorMsg(request, response, e.getMessage());
			return getUrlContext() + "/invoice";
		} finally {
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/manageInvoiceExport.do")
	public String manageInvoiceExport(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("type") String type,
			@RequestParam("dataQualifier") String dataQualifier,
			Object objectDAO, Class clazz) {
		response.setContentType(MimeUtil.getContentType(type));
		
		String reportName = "invoiceReport";
		if (!StringUtils.equals("html", type) && !StringUtils.equals("print", type)) {
			response.setHeader("Content-Disposition", "attachment;filename=" + reportName + "." + type);
		}
		
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		List<InvoiceVO> invoiceList = performManageInvoiceSearch(searchCriteria);
		Map<String, Object> params = new HashMap<String, Object>();
		
		List columnPropertyList = (List) request.getSession().getAttribute(dataQualifier + "ColumnPropertyList");
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.exportReport(reportName, type, InvoiceVO.class, invoiceList,
						columnPropertyList, request);
			/*out = dynamicReportService.generateStaticReport(reportName,
					invoiceList, params, type, request);*/
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to generate report:" + e);
			
			setupManageInvoiceList(model, request);
			setErrorMsg(request, response, e.getMessage());
			return getUrlContext() + "/manageInvoiceList";
		} finally {
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/downloadInvoice.do")
	public String downloadInvoice(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = true, value = "type") String type) {
		setupManageInvoiceList(model, request);
		
		//InvoiceVO input = (InvoiceVO) request.getSession().getAttribute("input");
		InvoiceVO input = new InvoiceVO();
		input.setCustomerId("997");
		input.setOrderDateFrom("01/14/2019");
		input.setOrderDateTo("06/13/2019");
		input.setIds(new String[]{"17558", "17513", "17512", "17411"});
		input.setInvoiceNo("1234");
		input.setInvoiceDate(new Date());
		Map<String, Object> datas = generateInvoiceData(request, input);
		List<InvoiceVO> invoiceReportVOList = (List<InvoiceVO>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		String reportName = "previewInvoice";
		if (!StringUtils.equals("html", type) && !StringUtils.equals("print", type)) {
			response.setHeader("Content-Disposition", "attachment;filename="+reportName+"." + type);
		}
		response.setContentType(MimeUtil.getContentType(type));
		
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.generateStaticReport(reportName,
						invoiceReportVOList, params, type, request);
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to generate static report:" + e);
			
			setupManageInvoiceList(model, request);
			setErrorMsg(request, response, e.getMessage());
			return getUrlContext() + "/manageInvoiceList";
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void setupPreviewInvoice(HttpServletRequest request, ModelMap model) {
		Map<String, Object> imagesMap = new HashMap<String, Object>();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		model.addAttribute("msgCtx", "invoicePreview");
		model.addAttribute("errorCtx", "invoicePreview");
		model.addAttribute("activeTab", "createInvoice");
	}
	
	private Map<String,Object> generateInvoiceData(HttpServletRequest request, InvoiceVO input) {
		List<InvoiceVO> invoiceReportVOList = performPreviewInvoiceSearch(input);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("invoiceNo", input.getInvoiceNo());
		params.put("invoiceDate", input.getInvoiceDate());
		
		Customer customer = retrieveCustomer(input.getCustomerId());
		params.put("customer", customer.getCompanyName());
		params.put("billingAddress", customer.getBillingAddress("\n"));
		
		String orderDateRange = StringUtils.isEmpty(input.getOrderDateFrom()) ? StringUtils.EMPTY : input.getOrderDateFrom();
		orderDateRange += " - ";
		orderDateRange += StringUtils.isEmpty(input.getOrderDateTo()) ? StringUtils.EMPTY : input.getOrderDateTo();
		params.put("orderDateRange", orderDateRange);
		
		String logoFilePath = CoreUtil.getLogoFilePath(request);
		params.put("LOGO_FILE_PATH", logoFilePath);
		
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("data", invoiceReportVOList);
		data.put("params",params);
		     
		return data;
	}
	
	private Customer retrieveCustomer(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		
		return genericDAO.getById(Customer.class, Long.valueOf(id));
	}
	
	@ModelAttribute("modelObject")
	public InvoiceVO setupModel(HttpServletRequest request) {
		return new InvoiceVO();
	}
}
