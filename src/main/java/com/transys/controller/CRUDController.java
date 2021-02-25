package com.transys.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import java.lang.reflect.ParameterizedType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.validation.ValidationException;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import org.hibernate.exception.ConstraintViolationException;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import org.springframework.util.ClassUtils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.transys.core.util.CoreUtil;
import com.transys.core.util.FormatUtil;
import com.transys.core.util.MimeUtil;

import com.transys.model.AbstractBaseModel;
import com.transys.model.BaseModel;
import com.transys.model.SearchCriteria;
//import com.transys.model.StaticData;

@SuppressWarnings("unchecked")
public abstract class CRUDController<T extends BaseModel> extends BaseController {
	public CRUDController() {
		super();
		setUrlContext(ClassUtils.getShortName(getEntityClass()).toLowerCase());
	}

	protected Class<T> getEntityClass() {
		return (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	protected T getEntityInstance() {
		try {
			return (T) Class.forName(getEntityClass().getName()).newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	// assume the primary key property is going to be the Entity Class plus Seq
	protected String getPkParam() {
		return "id";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria));
		return urlContext + "/list";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/create.do")
	public String create(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		return urlContext + "/form";
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria));
		return urlContext + "/list";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/edit.do")
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupUpdate(model, request);
		
		return urlContext + "/form";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/view.do")
	public String view(ModelMap model, HttpServletRequest request) {
		setupUpdate(model, request);
		return urlContext + "/view";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/save.do")
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") T entity,
			BindingResult bindingResult, ModelMap model) {
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			System.out.println("Error in validation " + e);
			log.warn("Error in validation :" + e);
		}
		
		// Return to form if we had errors
		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			for(ObjectError e : errors) {
				System.out.println("Error: " + e.getDefaultMessage());
			}
			
			setupCreate(model, request);
			return urlContext + "/form";
		}
		
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		return list(model, request);
		//return saveSuccess(model, request, entity);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/bulkdelete.do")
	public String bulkdelete(@RequestParam("id") String[] id) {
		if (id != null) {
			for (int i = 0; i < id.length; i++) {
				try {
					genericDAO.deleteById(getEntityClass(),
							Long.parseLong(id[i]));
				} catch (Exception ex) {
					ex.printStackTrace();
					log.warn("Error deleting record " + id, ex);
				}
			}
		}
		// return to list
		return "redirect:/" + urlContext + "/list.do";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/delete.do")
	public String delete(@ModelAttribute("modelObject") T entity,
			BindingResult bindingResult, HttpServletRequest request) {
		try {
			genericDAO.delete(entity);
		} catch (Exception ex) {
			request.getSession().setAttribute(
					"errors",
					"This" + entity.getClass().getSimpleName()
							+ " can't be deleted");
			log.warn("Error deleting record " + entity.getId(), ex);
			request.getSession().setAttribute("error","Cannot delete a parent row");
		}
		// return to list
		return "redirect:/" + urlContext + "/list.do";
	}

	@ModelAttribute("modelObject")
	public T setupModel(HttpServletRequest request) {
		String pk = request.getParameter(getPkParam());
		if (pk == null || StringUtils.isEmpty(pk)) {
			return getEntityInstance();
		} else {
			return genericDAO.getById(getEntityClass(), Long.parseLong(pk));
		}
	}

	protected void beforeSave(HttpServletRequest request,
			@ModelAttribute(MODEL_OBJECT_KEY) T entity, ModelMap model) {
		if (entity instanceof AbstractBaseModel) {
			AbstractBaseModel baseModel = (AbstractBaseModel) entity;
			setModifier(request, baseModel);
		}
	}
	
	protected void setModifier(HttpServletRequest request, AbstractBaseModel entity) {
		if (entity.getId() == null) {
			entity.setCreatedAt(Calendar.getInstance().getTime());
			if (entity.getCreatedBy() == null) {
				entity.setCreatedBy(getUserId(request));
			}
		} else {
			entity.setModifiedAt(Calendar.getInstance().getTime());
			if (entity.getModifiedBy() == null) {
				entity.setModifiedBy(getUserId(request));
			}
		}
	}

	public void setupCreate(ModelMap model, HttpServletRequest request) {
		// Default is no implementation
	}
	
	/*public String saveSuccess(ModelMap model, HttpServletRequest request, T entity) {
		// Default is no implementation
		return StringUtils.EMPTY;
	}*/

	public void setupUpdate(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
	}

	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		//List<StaticData> statuses = listStaticData("ENTITY_STATUS");
		//model.addAttribute("statuses", statuses);
	}

	public void cleanUp(HttpServletRequest request) {
		SearchCriteria criteria = getSearchCriteria(request);
		if (criteria == null || criteria.getSearchMap() == null) {
			return;
		}
		
		resetEffectiveDateInSearch(request);
		resetEmptyPermitNumberInSearch(request);
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/export2.do")
	public String export2(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("type") String type,
			@RequestParam("dataQualifier") String dataQualifier,
			Object objectDAO, Class clazz) {
		return StringUtils.EMPTY;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/export.do")
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			@RequestParam("dataQualifier") String dataQualifier,
			Object objectDAO, Class clazz) {
		List columnPropertyList = (List) request.getSession().getAttribute(dataQualifier + "ColumnPropertyList");
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");

		response.setContentType(MimeUtil.getContentType(type));
		if (!StringUtils.equals(type, "html") && !StringUtils.equals(type, "print")) {
			response.setHeader("Content-Disposition", "attachment;filename=" + urlContext + "Report." + type);
		}
		ByteArrayOutputStream out = null;
		try {
			criteria.setPageSize(10000);
			String label = getCriteriaAsString(criteria);
			System.out.println("Criteria: " + label);
			out = dynamicReportService.exportReport(urlContext + "Report", type, getEntityClass(),
						columnPropertyList, criteria, request);
			out.writeTo(response.getOutputStream());
			if (StringUtils.equals(type, "html") || StringUtils.equals(type, "print")) {
				response.getOutputStream().println("<script language=\"javascript\">window.print()</script>");
			}
			criteria.setPageSize(25);
		} catch (IOException e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
		} finally {
			CoreUtil.close(out);
		}  
	}

	// Set up any custom editors, adds a custom one for java.sql.date by default
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		//dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(FormatUtil.inputDateFormat, true));
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	}

	protected boolean isConstraintError(Exception e, String errorKey) {
		//String errorMsg = e.getCause().getCause().getMessage();
		if (!(e.getCause() instanceof ConstraintViolationException)) {
			return false;
		}
		
		ConstraintViolationException ce = (ConstraintViolationException) e.getCause();
		if (StringUtils.containsIgnoreCase(ce.getConstraintName(), errorKey)) {
			return true;
		} else {
			return false;
		}
	}
	
	protected void setupManageDocs(ModelMap model, T entity) {
		String[] fileNamesList = getUploadedFileNames(entity);
		model.addAttribute("fileList", fileNamesList);
	}
	
	@RequestMapping("/managedocs/uploaddoc.do")
	public String uploadDoc(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@ModelAttribute("modelObject") T entity,
			@RequestParam("dataFile") MultipartFile file) {
		List<String> errorList = new ArrayList<String>();
		model.addAttribute("errorList", errorList);
		
		try {
			if (!validateUploadDoc(errorList, entity, file)) {
				return docActionComplete(request, entity, model);
			}
			
			Long createdBy = getUser(request).getId();
			saveDoc(request, entity, file, createdBy, errorList);
			if (errorList.isEmpty()) {
				addMsg(model, MANAGE_DOCS_CTX, "Successfully uploaded the doc");
			} 
		} catch (Exception ex) {
			log.warn("Unable to upload doc:===>>>>>>>>>" + ex);
			addError(model, MANAGE_DOCS_CTX, "An error occurred while uploading doc!!");
		}
		
		return docActionComplete(request, entity, model);
	}
	
	@RequestMapping("/managedocs/deletedoc.do")
	public String deleteDoc(ModelMap model, HttpServletRequest request, 
				@ModelAttribute("modelObject") T entity) {
		String filePath = constructDocFilePath(entity);
		File file = new File(filePath);
		
		boolean status = file.delete();
		if (status) {
			if (!docsUploaded(entity)) {
				Long userId = getUserId(request);
				auditDocAction(entity, "Doc deleted", userId);
				
				entity.setHasDocs("N");
				entity.setModifiedAt(Calendar.getInstance().getTime());
				entity.setModifiedBy(userId);
				genericDAO.saveOrUpdate(entity);
			}
			
			addMsg(model, MANAGE_DOCS_CTX, "Successfully deleted the doc");
		} else {
			addError(model, MANAGE_DOCS_CTX, "An error occurred while deleting the doc!!");
		}
		
		return docActionComplete(request, entity, model);
	}
	
	protected void auditDocAction(T entity, String auditMsg, Long createdBy) {
	}
	
	@RequestMapping("/managedocs/downloaddoc.do")
	public String downloadDoc(ModelMap model, HttpServletRequest request, HttpServletResponse response,
				@ModelAttribute("modelObject") T entity) {
		try {
			processDocDownload(request, response, entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void processDocDownload(HttpServletRequest request, HttpServletResponse response, T entity) {
		// Reads input file from an absolute path
		String filePath = constructDocFilePath(entity);
		File downloadFile = new File(filePath);
		FileInputStream inStream = null;
		try {
			inStream = new FileInputStream(downloadFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
      
		// Obtains ServletContext
		ServletContext context = request.getServletContext();
     
		/*// If you want to use a relative path to context root:
     	String relativePath = context.getRealPath("");
     	System.out.println("relativePath = " + relativePath);*/
      
		// Gets MIME type of the file
		String mimeType = context.getMimeType(filePath);
		if (mimeType == null) {        
			// Set to binary type if MIME mapping not found
         mimeType = "application/pdf";
		}
		
		// Modifies response
		response.setContentType(mimeType);
		response.setContentLength((int)downloadFile.length());
      
		// Forces download
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
		response.setHeader(headerKey, headerValue);
      
		// Obtains response's output stream
		OutputStream outStream = null;
		try {
			outStream = response.getOutputStream();
			byte[] buffer = new byte[4096];
			int bytesRead = -1;
	      
			while ((bytesRead = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			CoreUtil.close(inStream);
			CoreUtil.close(outStream);
		}  
	}
	
	protected boolean validateUploadDoc(List<String> errorList, T entity, MultipartFile file) {
		if (StringUtils.isEmpty(file.getOriginalFilename())) {
		    errorList.add("Please choose a file to upload !!");
		    return false;
	   }
		
		String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		if (!(ext.equalsIgnoreCase(".pdf"))) {
      	errorList.add("Please choose a file to upload with extention .pdf!!");
		   return false;
		}
		
		/*if (doesDocExist(entity, file)) {
			errorList.add("File with same name has been already uploaded");
		   return false;
		}*/
		
		if (file.isEmpty()) {
			errorList.add("Empty file");
			return false;
		}
		
		return true;
	}
	
	protected String docActionComplete(HttpServletRequest request, T entity, ModelMap model)	{
		return StringUtils.EMPTY;
	}
	
	protected void saveDoc(HttpServletRequest request, T entity, MultipartFile file,
			Long userId, List<String> errorList) {
		try {
			/*String realPathToUploads =  request.getServletContext().getRealPath(UPLOAD_DIR);
			if (!new File(realPathtoUploads).exists()) {
			    new File(realPathtoUploads).mkdir();
			}*/

			String filePath = constructDocFilePath(entity.getId(), file);
			File dest = new File(filePath);
			file.transferTo(dest);
			
			auditDocAction(entity, "Doc uploaded", userId);
			
			entity.setHasDocs("Y");
			entity.setModifiedAt(Calendar.getInstance().getTime());
			entity.setModifiedBy(userId);
			genericDAO.saveOrUpdate(entity);
		} catch (Exception e) {
			errorList.add("Error occured while uploading file");
			return;
		}
	}
	
	protected String constructDocFilePath() {
		String filePath = DOCS_UPLOAD_DIR + "/" + getEntityDocUploadSubDir(); 
		return filePath;
	}
	
	protected String constructDocFilePath(Long id, MultipartFile file) {
		return constructDocFilePath(id, file.getOriginalFilename());
	}
	
	protected String constructDocFilePath(Long id, String file) {
		String filePath = constructDocFilePath() 
					+ "/" + id + "_" + getEntityDocFileSuffix();
		String originalFileName = file.replaceAll("\\s", StringUtils.EMPTY);
		return filePath + "_" + originalFileName;
	}
	
	protected String constructDocFilePath(T entity) {
		String filePath = constructDocFilePath() + "/" + entity.getFileList()[0];
		return filePath;
	}
	
	protected String getEntityDocUploadSubDir() {
		return StringUtils.EMPTY;
	}
	
	protected String getEntityDocFileSuffix() {
		return StringUtils.EMPTY;
	}
	
	protected boolean docsUploaded(T entity) {
		String[] filaeNamesList = getUploadedFileNames(entity);
		return (filaeNamesList.length > 0) ? true : false;
	}
	
	protected String[] getUploadedFileNames(T entity) {
		if (entity == null || entity.getId() == null) {
			return (new String[0]);
		}
		
		String docPattern = constructDocFilePattern(entity.getId());
		FileFilter fileFilter = new WildcardFileFilter(docPattern);
		File dir = new File(constructDocFilePath());
		File[] files = dir.listFiles(fileFilter);
		String[] fileNamesList = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			fileNamesList[i] = files[i].getName();
		}
		return fileNamesList;
	}
	
	protected String constructDocFilePattern(Long id) {
		String filePath = id + "_" + getEntityDocFileSuffix() + "*.*";
		return filePath;
	}
	
	@Override
	protected String processAjaxRequest(HttpServletRequest request, String action, Model model) {
		 if (StringUtils.equalsIgnoreCase("doesDocExist", action)) {
			String file = request.getParameter("file");
			if (StringUtils.isEmpty(file)) {
				return StringUtils.EMPTY;
			}
			
			String idStr = request.getParameter("id");
			Long id = new Long(idStr);
			boolean responseBool = doesDocExist(id, file);
			return BooleanUtils.toStringTrueFalse(responseBool);
		} else {
			return super.processAjaxRequest(request, action, model);
		}
	}
	
	protected boolean doesDocExist(Long id, String file) {
		String filePath = constructDocFilePath(id, file);
		return doesDocExist(filePath);
	}
	
	protected boolean doesDocExist(String file) {
		File fileToCheck = new File(file);
		return fileToCheck.exists();
	}
}
