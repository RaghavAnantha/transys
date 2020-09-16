package com.transys.service;

import java.awt.Color;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;

import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

import net.sf.jasperreports.engine.util.JRLoader;

import net.sf.jasperreports.export.SimpleCsvExporterConfiguration;
import net.sf.jasperreports.export.SimpleCsvReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterConfiguration;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import net.sf.jasperreports.export.SimpleRtfExporterConfiguration;
import net.sf.jasperreports.export.SimpleRtfReportConfiguration;
import net.sf.jasperreports.export.SimpleTextExporterConfiguration;
import net.sf.jasperreports.export.SimpleTextReportConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import net.sf.jasperreports.web.util.WebHtmlResourceHandler;

import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import org.apache.commons.lang.StringUtils;

import org.hibernate.Query;
import org.hibernate.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;

import ar.com.fdvs.dj.core.DynamicJasperHelper;

import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.layout.HorizontalBandAlignment;

import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;

import com.transys.core.dao.GenericDAO;

import com.transys.core.tags.IColumnTag;
import com.transys.core.tags.StaticDataColumn;
import com.transys.core.tags.StaticDataUtil;
import com.transys.core.util.LabelUtil;
import com.transys.core.util.ReportUtil;
import com.transys.core.util.ServletUtil;

import com.transys.model.BaseModel;
import com.transys.model.SearchCriteria;

/**
 * @author gaurav
 */
@SuppressWarnings({ "unchecked", "deprecation" })
public class DynamicReportServiceImpl implements DynamicReportService {
	public static String reportsCtx = "/reports";
	
	public static String SUBREPORT_DIR_KEY = "SUBREPORT_DIR";
	public static String IS_IGNORE_PAGINATION_KEY = "IS_IGNORE_PAGINATION";
	
	@Autowired
	private GenericDAO genericDAO;
	
	public void setGenericDAO(GenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public ByteArrayOutputStream exportReport(String reportName, String type,
			Class entityClass, List<IColumnTag> columnPropertyList,
			SearchCriteria criteria, HttpServletRequest request,
			Object objectDAO, Class clazz) {
		System.out.println("**************exportReport 1**************");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		List<Field> displayableFieldList = new ArrayList<Field>();
		try {
			Field[] fields = entityClass.getDeclaredFields();
			for (IColumnTag objCol : columnPropertyList) {
				for (Field field : fields) {
					if (field.getName().equalsIgnoreCase(objCol.getDataField())) {
						displayableFieldList.add(field);
					}
					if (objCol.getDataField() != null
							&& objCol.getDataField().indexOf(".") != -1
							&& field.getName()
									.equalsIgnoreCase(
											objCol.getDataField().substring(
													0,
													objCol.getDataField()
															.indexOf(".")))) {
						displayableFieldList.add(field);
					}
				}
			}
			if (entityClass.getSuperclass() != Object.class) {
				Field[] superClassfields = entityClass.getSuperclass()
						.getDeclaredFields();
				for (IColumnTag objCol : columnPropertyList) {
					for (Field field : superClassfields) {
						if (field.getName().equalsIgnoreCase(
								objCol.getDataField())) {
							displayableFieldList.add(field);
						}
						if (objCol.getDataField() != null
								&& objCol.getDataField().indexOf(".") != -1
								&& field.getName().equalsIgnoreCase(
										objCol.getDataField().substring(
												0,
												objCol.getDataField().indexOf(
														".")))) {
							displayableFieldList.add(field);
						}
					}
				}
			}
			int columnPropertyListSize = displayableFieldList.size();
			String query = "select ";
			for (int i = 0; i < columnPropertyListSize; i++) {
				query += columnPropertyList.get(i).getDataField();
				if (i < columnPropertyListSize - 1) {
					query += ",";// }
				}
			}
			query += " from " + ClassUtils.getShortName(entityClass);
			if (criteria != null) {
				if (criteria.getSearchMap().size() > 0) {
					Object[] keyArray = criteria.getSearchMap().keySet()
							.toArray();
					for (int i = 0; i < keyArray.length; i++) {
						// Report search by date
						if (!criteria.getSearchMap().get(keyArray[i])
								.equals("")) {
							if (keyArray[i].equals("fromDate")
									|| keyArray[i].equals("toDate")) {
								if (i == 0) {
									query += " where createdAt BETWEEN ";
								}
								query = query
										+ " '"
										+ criteria.getSearchMap().get(
												keyArray[i]) + "'";

							}
							// Report search by column name
							else {
								if (i == 0) {
									query += " where ";
								}
								query += keyArray[i]
										+ " like '%"
										+ criteria.getSearchMap().get(
												keyArray[i]) + "%'";
							}
							if (i != (keyArray.length - 1)) {
								query += " and ";
							}
						}
					}
				}
			}
			Class[] clazzs = new Class[1];
			clazzs[0] = "".getClass();
			Method method = clazz.getMethod("findByQuery", clazzs);
			List<Object> objectList1 = (List<Object>) method.invoke(objectDAO,
					query);
			// Query query1 = entityManager.createQuery(query);
			List objectList = objectList1;
			Object[] obj = new Object[objectList.size()];
			for (int i = 0; i < objectList.size(); i++) {
				obj[i] = new HashMap();
				if (columnPropertyListSize == 1) {
					((Map) obj[i]).put(
							columnPropertyList.get(0).getDataField(),
							objectList.get(i));
				} else {
					for (int j = 0; j < columnPropertyListSize; j++) {
						if (columnPropertyList.get(j) instanceof StaticDataColumn) {
							Object value=((Object[]) objectList.get(i))[j];
							StaticDataColumn column = (StaticDataColumn)columnPropertyList.get(j);
							String data = StaticDataUtil.getText(column.getDataType(), value.toString());
							((Map) obj[i]).put(columnPropertyList.get(j)
									.getDataField(),data);
						}
						else {
							((Map) obj[i]).put(columnPropertyList.get(j)
									.getDataField(),
									((Object[]) objectList.get(i))[j]);
						}
							
					}
				}
			}
			JasperPrint jp = getJasperPrint(reportName, obj,
					columnPropertyList, displayableFieldList, request,type,entityClass);
			out = getStreamByType(type, jp, request);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return out;
	}

	/*@Override
	public ByteArrayOutputStream exportReport(String reportName, String type,
			Class entityClass, List<IColumnTag> columnPropertyList,
			SearchCriteria criteria, HttpServletRequest request) {
		System.out.println("**************exportReport 2**************");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		List<Field> displayableFieldList = new ArrayList<Field>();
		try {
			if (entityClass.getSuperclass() != Object.class) {
				Field[] superClassfields = entityClass.getSuperclass()
						.getDeclaredFields();
				for (IColumnTag objCol : columnPropertyList) {
					for (Field field : superClassfields) {
						if (field.getName().equalsIgnoreCase(
								objCol.getDataField())) {
							displayableFieldList.add(field);
						}
						if (objCol.getDataField().indexOf(".") != -1
								&& field.getName().equalsIgnoreCase(
										objCol.getDataField().substring(
												0,
												objCol.getDataField().indexOf(
														".")))) {
							displayableFieldList.add(field);
						}
					}
				}
			}
			Field[] fields = entityClass.getDeclaredFields();
			for (IColumnTag objCol : columnPropertyList) {
				for (Field field : fields) {
					if (field.getName().equalsIgnoreCase(objCol.getDataField())) {
						displayableFieldList.add(field);
					}
					if (objCol.getDataField().indexOf(".") != -1
							&& field.getName()
									.equalsIgnoreCase(
											objCol.getDataField().substring(
													0,
													objCol.getDataField()
															.indexOf(".")))) {
						displayableFieldList.add(field);
					}
				}
			}
			
			String query = "select ";
			for (int i = 0; i < columnPropertyList.size(); i++) {
				query += columnPropertyList.get(i).getDataField();
				if (i < columnPropertyList.size() - 1)
					query += ",";
			}
			query += " from " + ClassUtils.getShortName(entityClass);
			if (criteria.getSearchMap().size() > 0) {
				Object[] keyArray = criteria.getSearchMap().keySet().toArray();
				for (int i = 0; i < keyArray.length; i++) {
					if (i == 0) {
						query += " where ";
					}
					//System.out.println("\nkeyArray[i]===="+keyArray[i]);
					String str=(criteria.getSearchMap().get(keyArray[i])).toString();
					//System.out.println("\nkeyArray[i]===str="+str);
					if (str.contains("-") || keyArray[i].toString().equalsIgnoreCase("driver.fullName") || keyArray[i].toString().equalsIgnoreCase("terminal.name") || keyArray[i].toString().equalsIgnoreCase("driverCompany.name") || ((criteria.getSearchMap().get(keyArray[i])).toString()==""))
					{
						System.out.println("contains(-)");
						query += keyArray[i] + " like '%"
						+ criteria.getSearchMap().get(keyArray[i]) + "%'";
					}
					else
					{
						if(!((criteria.getSearchMap().get(keyArray[i])).toString()==""))
					query += keyArray[i] + " like " + criteria.getSearchMap().get(keyArray[i]) ;
						
					}
					if (i != (keyArray.length - 1)) {
						query += " and ";
					}
				}
			}
			
			//query = "select companyName,id,billingAddressLine1,billingAddressLine2,city,state.name,zipcode,phone,fax from Customer where companyName like 'Testname' and id like 11";
			//query = "select companyName from Customer where companyName like 'bh' and id like 8";
			
			System.out.println("***** Download query is "+query);
			
			Session session = ((Session)genericDAO.getEntityManager().getDelegate());
			//System.out.println("\nDRSImpl----exportReport 2Last----Query==>"+query+"\n");
			Query query1 = session.createQuery(query);
			
			//System.out.println("\nDRSImpl----exportReport 2Last----Query1==>"+query1+"\n");
			List objectList = query1.list();
			Object[] obj = new Object[objectList.size()];
			for (int i = 0; i < objectList.size(); i++) {
				obj[i] = new HashMap();
				if (columnPropertyList.size() == 1) {
					((Map) obj[i]).put(
							columnPropertyList.get(0).getDataField(),
							objectList.get(i));
				} else {
					for (int j = 0; j < columnPropertyList.size(); j++) {
						if (columnPropertyList.get(j) instanceof StaticDataColumn) {
							Object value=((Object[]) objectList.get(i))[j];
							StaticDataColumn column = (StaticDataColumn)columnPropertyList.get(j);
							Object data = StaticDataUtil.getText(column.getDataType(), value.toString());
							((Map) obj[i]).put(columnPropertyList.get(j)
									.getDataField(),data);
						}
						else {
							((Map) obj[i]).put(columnPropertyList.get(j)
									.getDataField(), ((Object[]) objectList.get(i))[j]);
						}
					}
				}
			}
			session.close();
			JasperPrint jp = getJasperPrint(reportName, obj,
					columnPropertyList, displayableFieldList, request,type,entityClass);
			out = getStreamByType(type, jp, request);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return out;
	}*/
	
	@Override
	public <T extends BaseModel> ByteArrayOutputStream exportReport(String reportName, String type,
			Class<T> entityClass, List<IColumnTag> columnPropertyList,
			SearchCriteria criteria, HttpServletRequest request) {
		System.out.println("**************exportReport 2a**************");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		List<Field> displayableFieldList = new ArrayList<Field>();
		try {
			if (entityClass.getSuperclass() != Object.class) {
				Field[] superClassfields = entityClass.getSuperclass().getDeclaredFields();
				for (IColumnTag objCol : columnPropertyList) {
					for (Field field : superClassfields) {
						if (objCol.getDataField() != null) {
							if (field.getName().equalsIgnoreCase(objCol.getDataField())) {
								displayableFieldList.add(field);
							}
							if (objCol.getDataField().indexOf(".") != -1
									&& field.getName().equalsIgnoreCase(objCol.getDataField().substring(0, objCol.getDataField().indexOf(".")))) {
								displayableFieldList.add(field);
							}
						}
					}
				}
			}
			Field[] fields = entityClass.getDeclaredFields();
			for (IColumnTag objCol : columnPropertyList) {
				for (Field field : fields) {
					if (field.getName().equalsIgnoreCase(objCol.getDataField())) {
						displayableFieldList.add(field);
					}
					if (objCol.getDataField() != null) {
						if (objCol.getDataField().indexOf(".") != -1
								&& field.getName().equalsIgnoreCase(
												objCol.getDataField().substring(0, objCol.getDataField().indexOf(".")))) {
							displayableFieldList.add(field);
						}
					}
				}
			}
			
			List<Field> orderedDisplayableFieldList = new ArrayList<Field>();
			for (IColumnTag objCol : columnPropertyList) {
				for (Field aField : displayableFieldList) {
					if (objCol.getDataField() != null) {
						if (aField.getName().equalsIgnoreCase(objCol.getDataField())) {
							orderedDisplayableFieldList.add(aField);
							break;
						}
						if (objCol.getDataField().indexOf(".") != -1
								&& aField.getName().equalsIgnoreCase(objCol.getDataField().substring(0, objCol.getDataField().indexOf(".")))) {
							orderedDisplayableFieldList.add(aField);
							break;
						}
					}
				}
			}
			
			String query = "select ";
			for (int i = 0; i < columnPropertyList.size(); i++) {
				query += columnPropertyList.get(i).getDataField();
				if (i < columnPropertyList.size() - 1)
					query += ",";
			}
			
			String fromQuery = genericDAO.contructQuery(entityClass, criteria, "id", null, null);
			query += fromQuery;
			
			System.out.println("***** Download query is "+query);
			
			Session session = ((Session)genericDAO.getEntityManager().getDelegate());
			//System.out.println("\nDRSImpl----exportReport 2Last----Query==>"+query+"\n");
			Query query1 = session.createQuery(query);
			
			//System.out.println("\nDRSImpl----exportReport 2Last----Query1==>"+query1+"\n");
			List objectList = query1.list();
			Object[] obj = new Object[objectList.size()];
			for (int i = 0; i < objectList.size(); i++) {
				obj[i] = new HashMap();
				if (columnPropertyList.size() == 1) {
					((Map) obj[i]).put(
							columnPropertyList.get(0).getDataField(),
							objectList.get(i));
				} else {
					for (int j = 0; j < columnPropertyList.size(); j++) {
						if (columnPropertyList.get(j) instanceof StaticDataColumn) {
							Object value=((Object[]) objectList.get(i))[j];
							StaticDataColumn column = (StaticDataColumn)columnPropertyList.get(j);
							Object data = StaticDataUtil.getText(column.getDataType(), value.toString());
							((Map) obj[i]).put(columnPropertyList.get(j).getDataField(),data);
						}
						else {
							((Map) obj[i]).put(columnPropertyList.get(j).getDataField(), ((Object[]) objectList.get(i))[j]);
						}
					}
				}
			}
			session.close();
			JasperPrint jp = getJasperPrint(reportName, obj,
					columnPropertyList, orderedDisplayableFieldList, request,type,entityClass);
			out = getStreamByType(type, jp, request);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return out;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public <T> ByteArrayOutputStream exportReport(String reportName,
			String type, Class<T> entityClass, List<T> list,
			List<IColumnTag> columnPropertyList, HttpServletRequest request) {
		System.out.println("**************exportReport 3**************");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Map<String, Field> displayableFieldMap = new HashMap<String, Field>();
		try {
			Field[] fields = entityClass.getDeclaredFields();
			for (IColumnTag objCol : columnPropertyList) {
				for (Field field : fields) {
					if (field.getName().equalsIgnoreCase(objCol.getDataField())) {
						displayableFieldMap.put(objCol.getDataField(), field);
					}
					if (objCol.getDataField() != null
							&& objCol.getDataField().indexOf(".") != -1
							&& field.getName()
									.equalsIgnoreCase(
											objCol.getDataField().substring(
													0,
													objCol.getDataField()
															.indexOf(".")))) {
						displayableFieldMap.put(objCol.getDataField(), field);
					}
				}
			}
			if (entityClass.getSuperclass() != Object.class) {
				Field[] superClassfields = entityClass.getSuperclass()
						.getDeclaredFields();
				for (IColumnTag objCol : columnPropertyList) {
					for (Field field : superClassfields) {
						if (field.getName().equalsIgnoreCase(
								objCol.getDataField())) {
							displayableFieldMap.put(objCol.getDataField(),
									field);
						}
						if (objCol.getDataField() != null
								&& objCol.getDataField().indexOf(".") != -1
								&& field.getName().equalsIgnoreCase(
										objCol.getDataField().substring(
												0,
												objCol.getDataField().indexOf(
														".")))) {
							displayableFieldMap.put(objCol.getDataField(),
									field);
						}
					}
				}
			}
			JasperPrint jp = getJasperPrintFromBean(reportName, type, list.toArray(),
					columnPropertyList, displayableFieldMap, request);
			out = getStreamByType(type, jp, request);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return out;
	}

	/*
	@Override
	public <T> ByteArrayOutputStream exportReport(List<T> list,
			String reportName, String type, Class<T> entityClass,
			List<String> propertyList, HttpServletRequest request) {
		System.out.println("**************exportReport 4**************");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Map<String, Field> displayableFieldMap = new LinkedHashMap<String, Field>();
		try {
			Field[] fields = entityClass.getDeclaredFields();
			for (String property : propertyList) {
				for (Field field : fields) {
					if (field.getName().equalsIgnoreCase(property)) {
						displayableFieldMap.put(property, field);
					}
					if (property.indexOf(".") != -1
							&& field.getName()
									.equalsIgnoreCase(
											property.substring(0,
													property.indexOf(".")))) {
						String propertyName = property.substring(
								property.indexOf(".") + 1, property.length());
						Field[] subFields = field.getType().getDeclaredFields();
						for (Field subField : subFields) {
							if (subField.getName().equalsIgnoreCase(
									propertyName))
								displayableFieldMap.put(property, subField);
						}
					}
				}
			}
			if (entityClass.getSuperclass() != Object.class) {
				Field[] superClassfields = entityClass.getSuperclass()
						.getDeclaredFields();
				for (String property : propertyList) {
					for (Field field : superClassfields) {
						if (field.getName().equalsIgnoreCase(property)) {
							displayableFieldMap.put(property, field);
						}
						if (property.indexOf(".") != -1
								&& field.getName().equalsIgnoreCase(
										property.substring(0,
												property.indexOf(".")))) {
							String propertyName = property.substring(
									property.indexOf(".") + 1,
									property.length());
							Field[] subFields = field.getType()
									.getDeclaredFields();
							for (Field subField : subFields) {
								if (subField.getName().equalsIgnoreCase(
										propertyName))
									displayableFieldMap.put(property, subField);
							}
						}
					}
				}
			}
			JasperPrint jp = getJasperPrintFromList(reportName, list.toArray(),
					displayableFieldMap, request);
			out = getStreamByType(type, jp, request);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return out;
	}*/

	private JasperPrint getJasperPrint(String reportName, Object[] obj,
			List<IColumnTag> columnPropertyList,
			List<Field> displayableFieldList, HttpServletRequest request,String type,Class entityClass) {
		Style headerStyle = getHeaderStyle();
		Style detailStyle = new Style();
		detailStyle.setBorder(Border.THIN());
		JasperPrint jp = null;
		try {
			DynamicReportBuilder jasperReport = getReport(reportName,
					headerStyle, detailStyle, columnPropertyList,
					displayableFieldList, request,type,entityClass);
			DynamicReport dynaRep = jasperReport.build();
			jp = DynamicJasperHelper.generateJasperPrint(dynaRep,
					new ClassicLayoutManager(), new JRMapArrayDataSource(obj));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jp;
	}

	private JasperPrint getJasperPrintFromBean(String reportName, String type, Object[] obj,
			List<IColumnTag> columnPropertyList,
			Map<String, Field> displayableFieldMap, HttpServletRequest request) {
		Style headerStyle = getHeaderStyle();
		Style detailStyle = new Style();
		//detailStyle.setBorder(Border.THIN());
		JasperPrint jp = null;
		try {
			DynamicReportBuilder jasperReport = getReportFromMap(reportName, type,
					headerStyle, detailStyle, columnPropertyList,
					displayableFieldMap, request);
			DynamicReport dynaRep = jasperReport.build();
			jp = DynamicJasperHelper.generateJasperPrint(dynaRep,
					new ClassicLayoutManager(), new JRBeanArrayDataSource(obj,
							true));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jp;
	}

	/*private JasperPrint getJasperPrintFromList(String reportName, Object[] obj,
			Map<String, Field> displayableFieldMap, HttpServletRequest request) {
		Style headerStyle = getHeaderStyle();
		Style detailStyle = new Style();
		detailStyle.setBorder(Border.THIN);
		JasperPrint jp = null;
		try {
			DynamicReportBuilder jasperReport = getReportFromPropertyMap(
					reportName, headerStyle, detailStyle, displayableFieldMap,
					request);
			DynamicReport dynaRep = jasperReport.build();
			jp = DynamicJasperHelper.generateJasperPrint(dynaRep,
					new ClassicLayoutManager(), new JRBeanArrayDataSource(obj,
							true));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jp;
	}*/

	@Override
	public ByteArrayOutputStream generateStaticReport(String reportName,
			List datas, Map params, String type, HttpServletRequest request) {
		try {// @@@@@@@@@@@@@@@@@ 1
			System.out.println("-----------------------");
			JasperPrint jp = getJasperPrintFromFile(reportName, datas, params, type, request);
			ByteArrayOutputStream out = getStreamByType(type, jp, request);
			return out;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	@Override
	public ByteArrayOutputStream generateStaticMasterSubReport(String masterReportName, String subReportName, 
			List<?> datas, Map params, String type, HttpServletRequest request) {
		try {// @@@@@@@@@@@@@@@@@ 1a
			System.out.println("-----------------------");
			JasperPrint jp = getJasperPrintFromFile(masterReportName, subReportName, datas, params, request);
			ByteArrayOutputStream out = getStreamByType(type, jp, request);
			return out;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		
	}
	
	@Override
	public JasperPrint getJasperPrintFromFile(String masterReportName, String subReportName, List datas,
			Map params, HttpServletRequest request) {
		// @@@@@@@@@@@@@@@@@ 2a
		try {
			String masterReportFilePath = ServletUtil.getRealPath(request, reportsCtx + "/" + masterReportName + ".jasper");
			File masterReportFile = new File(masterReportFilePath);
			JasperReport masterJasperReport = (JasperReport) JRLoader.loadObject(masterReportFile);
			
			String subReportPathName = ServletUtil.getRealPath(request, reportsCtx);
			/*File subReportFile = new File(request.getSession().getServletContext().getRealPath(reportsCtx + "/"  + subReportName + ".jasper"));
			JasperReport subJasperReport = (JasperReport) JRLoader.loadObject(subReportFile);*/
			
			// ***********
			JasperPrint jp = null;
			//JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas, false);
			//JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(datas);
				
			//params.put("subreport", subJasperReport);
			params.put(SUBREPORT_DIR_KEY, subReportPathName);
				
			jp = JasperFillManager.fillReport(masterJasperReport, params, dataSource);
			/*JasperFillManager.fillReportToFile(jasperMasterReport, destFileName, parameters, beanColDataSource);*/
			
			return jp;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	private void addPaginationParams(Map<String, Object> params, String type) {
		if (params == null || StringUtils.isEmpty(type)) {
			return;
		}
		
		if (!StringUtils.equalsIgnoreCase("pdf", type)) {
			params.put(IS_IGNORE_PAGINATION_KEY, true);
		}
	}
	
	private void addReportProperties(JasperPrint jp) {
		if (jp == null) {
			return;
		}
		
		jp.setProperty("net.sf.jasperreports.export.xlsx.exclude.origin.keep.first.band.1", "pageHeader");
		jp.setProperty("net.sf.jasperreports.export.xlsx.exclude.origin.band.2", "pageFooter");
		jp.setProperty("net.sf.jasperreports.export.xls.exclude.origin.keep.first.band.1", "pageHeader");
		jp.setProperty("net.sf.jasperreports.export.xls.exclude.origin.band.2", "pageFooter");
		jp.setProperty("net.sf.jasperreports.export.html.exclude.origin.keep.first.band.1", "pageHeader");
		jp.setProperty("net.sf.jasperreports.export.html.exclude.origin.band.2", "pageFooter");
		
		jp.setProperty("net.sf.jasperreports.export.csv.exclude.origin.band.1", "pageHeader");
		jp.setProperty("net.sf.jasperreports.export.csv.exclude.origin.band.2", "pageFooter");
	}

	public JasperPrint getJasperPrintFromFile(String reportName, List datas,
			Map params, String type, HttpServletRequest request) {
		try {// @@@@@@@@@@@@@@@@@ 2
			File reportFile = new File(request.getSession().getServletContext().getRealPath(reportsCtx + "/"  + reportName + ".jasper"));
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportFile);
			//JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportFile.getPath());
			
			addPaginationParams(params, type);
			
			// ***********
			JasperPrint jp = null;
			if (datas != null) {
				 //JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas, false);
				//JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(datas);
				jp = JasperFillManager.fillReport(jasperReport, params, dataSource);
			} else {
				jp = JasperFillManager.fillReport(jasperReport, params);
			}
			
			addReportProperties(jp);
			return jp;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	private DynamicReportBuilder getReport(String reportName,
			Style headerStyle, Style detailStyle,
			List<IColumnTag> columnPropertyList,
			List<Field> displayableFieldList, HttpServletRequest request, String type,Class entityClass)
			throws ColumnBuilderException, ClassNotFoundException {
		DynamicReportBuilder report = new DynamicReportBuilder();
		int columnPropertyListSize = displayableFieldList.size();
		String locale = (String) request.getSession().getAttribute("lang");
		if (locale == null)
			locale = "en_US";
		for (int i = 0; i < columnPropertyListSize; i++) {
			addColumnByType(report, displayableFieldList.get(i),
					columnPropertyList.get(i),
					headerStyle, detailStyle, locale);

		}
		/*report.addImageBanner(
				request.getSession().getServletContext().getRealPath("/")+"/images"
						+ "/logo.png", new Integer(200), new Integer(80),
				ImageBanner.ALIGN_LEFT);*/
		// report.addImageBanner(request.getSession().getServletContext().getRealPath("images/logo_ar_SA.jpg"),
		// new Integer(200), new Integer(80),
		// ImageBanner.ALIGN_RIGHT);
		
		String clazz=ClassUtils.getShortName(entityClass);
		report.setHeaderHeight(new Integer("10"));
		Style oddRowStyle = new Style();
		oddRowStyle.setBackgroundColor(new Color(0xE6E6E6));
		report.setOddRowBackgroundStyle(oddRowStyle);
		report.setPrintBackgroundOnOddRows(true);
		report.setUseFullPageWidth(true); // make columns to fill the page width
		
		if(!clazz.equals("Ticket")){
		report.setPageSizeAndOrientation(Page.Page_A4_Landscape());
		}
		else{
			report.setPageSizeAndOrientation(Page.Page_Legal_Landscape());
		}
		if(!type.equals("pdf")){
		report.setIgnorePagination(true);
		}
		else{
			report.setIgnorePagination(false);
		}
		// report.setReportLocale(new Locale("ar", "SA"));

		/*Style atStyle = new StyleBuilder(true).setFont(Font.COMIC_SANS_SMALL)
				.setTextColor(Color.red).build();
		AutoText autoText = new AutoText(AutoText.AUTOTEXT_CREATED_ON,
				AutoText.POSITION_FOOTER, HorizontalBandAlignment.CENTER);
		autoText.setWidth(new Integer(200));
		autoText.setStyle(atStyle);
		report.setFooterVariablesHeight(20);
		report.addAutoText(autoText);*/

		return report;
	}

	private DynamicReportBuilder getReportFromMap(String reportName, String type,
			Style headerStyle, Style detailStyle,
			List<IColumnTag> columnPropertyList,
			Map<String, Field> displayableFieldMap, HttpServletRequest request)
			throws ColumnBuilderException, ClassNotFoundException {
		DynamicReportBuilder report = new DynamicReportBuilder();
		String locale = (String) request.getSession().getAttribute("lang");
		if (locale == null)
			locale = "en_US";
		for (IColumnTag column : columnPropertyList) {
			Field field = displayableFieldMap.get(column.getDataField());
			if (field == null)
				continue;
			addColumnByType(report, field, column,
					headerStyle, detailStyle, locale);
		}
		//report.addImageBanner(request.getSession().getServletContext()
		//		.getRealPath("images/logo_en_US.jpg"), new Integer(200),
		//		new Integer(80), ImageBanner.ALIGN_LEFT);
		//report.addImageBanner(request.getSession().getServletContext()
		//		.getRealPath("images/logo_ar_SA.jpg"), new Integer(200),
		//		new Integer(80), ImageBanner.ALIGN_RIGHT);
		report.setHeaderHeight(new Integer("10"));
		Style oddRowStyle = new Style();
		oddRowStyle.setBackgroundColor(new Color(0xE6E6E6));
		report.setOddRowBackgroundStyle(oddRowStyle);
		report.setPrintBackgroundOnOddRows(true);
		report.setUseFullPageWidth(true); // make columns to fill the page width
		//report.setWhenResourceMissing(JasperReport.WHEN_RESOURCE_MISSING_TYPE_NULL);
		report.setPageSizeAndOrientation(Page.Page_A4_Landscape());

		Style atStyle = new StyleBuilder(true).setFont(Font.COMIC_SANS_SMALL)
				.setTextColor(Color.red).build();
		AutoText autoText = new AutoText(AutoText.AUTOTEXT_CREATED_ON,
				AutoText.POSITION_FOOTER, HorizontalBandAlignment.CENTER);
		autoText.setWidth(new Integer(200));
		autoText.setStyle(atStyle);
		report.setFooterVariablesHeight(20);
		report.addAutoText(autoText);
		
		if (StringUtils.equals(type, "pdf")) {
			report.setIgnorePagination(false);
		} else {
			report.setIgnorePagination(true);
		}
		
		return report;
	}

	/*private DynamicReportBuilder getReportFromPropertyMap(String reportName,
			Style headerStyle, Style detailStyle,
			Map<String, Field> displayableFieldMap, HttpServletRequest request)
			throws ColumnBuilderException, ClassNotFoundException {
		DynamicReportBuilder report = new DynamicReportBuilder();
		String locale = (String) request.getSession().getAttribute("lang");
		if (locale == null)
			locale = "en_US";
		Object[] keys = displayableFieldMap.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			Field field = displayableFieldMap.get(keys[i].toString());
			if (field == null)
				continue;
			addColumnByType(report, field, keys[i].toString(), field.getName(),
					headerStyle, detailStyle, locale);
		}
		report.addImageBanner(
				Configuration.getProperty("storage.folder.repository")
						+ "/logo/logo.png", new Integer(200), new Integer(80),
				ImageBanner.ALIGN_LEFT);
		report.setHeaderHeight(new Integer("10"));
		Style oddRowStyle = new Style();
		oddRowStyle.setBackgroundColor(new Color(0xE6E6E6));
		report.setOddRowBackgroundStyle(oddRowStyle);
		report.setPrintBackgroundOnOddRows(true);
		report.setUseFullPageWidth(true); // make columns to fill the page width
		report.setWhenResourceMissing(JasperReport.WHEN_RESOURCE_MISSING_TYPE_NULL);
		report.setPageSizeAndOrientation(Page.Page_A4_Landscape());

		Style atStyle = new StyleBuilder(true).setFont(Font.COMIC_SANS_SMALL)
				.setTextColor(Color.red).build();
		AutoText autoText = new AutoText(AutoText.AUTOTEXT_CREATED_ON,
				AutoText.POSITION_FOOTER, HorizontalBandAlignment.CENTER);
		autoText.setWidth(new Integer(200));
		autoText.setStyle(atStyle);
		report.setFooterVariablesHeight(20);
		report.addAutoText(autoText);
		return report;
	}*/

	private Style getHeaderStyle() {
		Style headerStyle = new Style();
		Font font = new Font(10, "ARIAL", "ARIAL.TTF",
		Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing, true);
		headerStyle.setFont(font);
		//headerStyle.setBorder(Border.THIN());
		headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
		headerStyle.setBackgroundColor(new Color(0x99ccff));
		headerStyle.setTextColor(Color.WHITE);
		headerStyle.setTransparency(Transparency.OPAQUE);
		return headerStyle;
	}

	private AbstractColumn getColumn(String property, Class type, String title,
			int width, Style headerStyle, Style detailStyle)
			throws ColumnBuilderException {
		AbstractColumn columnState = ColumnBuilder.getNew()
				.setColumnProperty(property, type.getName()).setTitle(title)
				.setWidth(Integer.valueOf(width)).setStyle(detailStyle)
				.setHeaderStyle(headerStyle).build();
		return columnState;
	}
	
	private void addColumnByType(DynamicReportBuilder report, Field field,
			IColumnTag columnTag, Style headerStyle,
			Style detailStyle, String locale) throws ColumnBuilderException {
		if (columnTag instanceof StaticDataColumn) {
			Style intStyle = new Style();
			//intStyle.setBorder(Border.THIN());
			intStyle.setHorizontalAlign(HorizontalAlign.LEFT);
			intStyle.setTextColor(Color.BLACK);
			StaticDataExpression sde = new StaticDataExpression(columnTag.getDataField(),((StaticDataColumn) columnTag).getDataType());
			AbstractColumn staticDataColumn = ColumnBuilder.getNew().setColumnProperty(columnTag.getDataField(), field.getType().getName()).setTitle(columnTag.getHeaderText())
			.setWidth(100).setStyle(intStyle)
			.setHeaderStyle(headerStyle).setCustomExpression(sde).build();
			report.addColumn(staticDataColumn);
		}
		else if (field.getType() == int.class || field.getType() == Integer.class) {
			Style intStyle = new Style();
			//intStyle.setBorder(Border.THIN());
			intStyle.setHorizontalAlign(HorizontalAlign.LEFT);
			intStyle.setTextColor(Color.BLACK);
			AbstractColumn intColumn = getColumn(columnTag.getDataField(), Integer.class,
					LabelUtil.getText(columnTag.getHeaderText(), locale),
					Integer.valueOf("100"), headerStyle, intStyle);
			report.addColumn(intColumn);
		}  else if (field.getType() == Long.class) {
			Style longStyle = new Style();
			//longStyle.setBorder(Border.THIN());
			longStyle.setHorizontalAlign(HorizontalAlign.LEFT);
			longStyle.setTextColor(Color.MAGENTA);
			AbstractColumn longColumn = getColumn(columnTag.getDataField(), Long.class,
					LabelUtil.getText(columnTag.getHeaderText(), locale),
					Integer.valueOf("100"), headerStyle, longStyle);
			report.addColumn(longColumn);
		} else if (field.getType() == Double.class) {
			Style doubleStyle = new Style();
			//doubleStyle.setBorder(Border.THIN());
			doubleStyle.setHorizontalAlign(HorizontalAlign.LEFT);
			doubleStyle.setPattern("####.000");
			doubleStyle.setTextColor(Color.RED);
			AbstractColumn doubleColumn = getColumn(columnTag.getDataField(), Double.class,
					LabelUtil.getText(columnTag.getHeaderText(), locale),
					Integer.valueOf("100"), headerStyle, doubleStyle);
			report.addColumn(doubleColumn);
		} else if (field.getType() == Date.class) {
			Style timeStyle = new Style("Date");
			//timeStyle.setBorder(Border.THIN());
			timeStyle.setHorizontalAlign(HorizontalAlign.LEFT);
			timeStyle.setPattern("MM/dd/yyyy");
			AbstractColumn longColumn = getColumn(columnTag.getDataField(), Date.class,
					LabelUtil.getText(columnTag.getHeaderText(), locale),
					Integer.valueOf("100"), headerStyle, timeStyle);
			report.addColumn(longColumn);
		} else if (field.getType() == Timestamp.class) {
			Style timeStyle = new Style("Timestamp");
			//timeStyle.setBorder(Border.THIN());
			timeStyle.setHorizontalAlign(HorizontalAlign.LEFT);
			timeStyle.setPattern("MM/dd/yyyy");
			AbstractColumn timeStampColumn = getColumn(columnTag.getDataField(), Timestamp.class,
					LabelUtil.getText(columnTag.getHeaderText(), locale),
					Integer.valueOf("100"), headerStyle, timeStyle);
			report.addColumn(timeStampColumn);
		} else if (field.getType() == BigDecimal.class) {
			Style bigDecimalStyle = new Style();
			//doubleStyle.setBorder(Border.THIN());
			bigDecimalStyle.setHorizontalAlign(HorizontalAlign.LEFT);
			bigDecimalStyle.setPattern("#####0.00");
			//bigDecimalStyle.setTextColor(Color.RED);
			AbstractColumn bigDecimalColumn = getColumn(columnTag.getDataField(), BigDecimal.class,
					LabelUtil.getText(columnTag.getHeaderText(), locale),
					Integer.valueOf("100"), headerStyle, bigDecimalStyle);
			report.addColumn(bigDecimalColumn);
		} else {
			Style defaultStyle = detailStyle;
			String type= columnTag.getType();
			Class fieldType = String.class;
			if ("int".equalsIgnoreCase(type)) {
				fieldType = Integer.class;
			} else if ("double".equalsIgnoreCase(type)) {
				fieldType = Double.class;
			} else if ("java.math.BigDecimal".equalsIgnoreCase(type)){
				fieldType = BigDecimal.class;
				Style bigDecimalStyle = new Style();
				bigDecimalStyle.setHorizontalAlign(HorizontalAlign.LEFT);
				bigDecimalStyle.setPattern("#####0.00");
				defaultStyle = bigDecimalStyle;
			} else if ("java.sql.Timestamp".equalsIgnoreCase(type)){
				fieldType = Timestamp.class;
				Style timeStyle = new Style("Timestamp");
				timeStyle.setHorizontalAlign(HorizontalAlign.LEFT);
				timeStyle.setPattern("MM/dd/yyyy");
				defaultStyle = timeStyle;
			} else if ("java.lang.Long".equalsIgnoreCase(type)){
				fieldType = Long.class;
				Style longStyle = new Style();
				longStyle.setHorizontalAlign(HorizontalAlign.LEFT);
				longStyle.setTextColor(Color.MAGENTA);
				defaultStyle = longStyle;
			}
			AbstractColumn genericColumn = getColumn(columnTag.getDataField(), fieldType,
					LabelUtil.getText(columnTag.getHeaderText(), locale),
					Integer.valueOf("100"), headerStyle, defaultStyle);
			report.addColumn(genericColumn);
		}
	}

	private ByteArrayOutputStream getStreamByType(String type, JasperPrint jp,
			HttpServletRequest request) throws JRException {
		if ("xlsx".equalsIgnoreCase(type)) {
			return exportReportXlsx(jp);
		} else if ("xls".equalsIgnoreCase(type)) {
			return exportReportXls(jp);
		} else if ("pdf".equalsIgnoreCase(type)) {
			return exportReportPdf(jp);
		} else if ("csv".equalsIgnoreCase(type)) {
			return exportReportCsv(jp);
		} else if ("html".equalsIgnoreCase(type)) {
			return exportReportHtml(jp, request);
		} else if ("print".equalsIgnoreCase(type)) {
			return exportReportPrint(jp, request);
		} else if ("rtf".equalsIgnoreCase(type)) {
			return exportReportRtf(jp);
		} else {
			return exportReportText(jp);
		}
	}

	private ByteArrayOutputStream exportReportXls(JasperPrint jp)
			throws JRException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setExporterInput(new SimpleExporterInput(jp));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
		
		SimpleXlsExporterConfiguration xlsExporterConfig = new SimpleXlsExporterConfiguration();
		exporter.setConfiguration(xlsExporterConfig);
		
		SimpleXlsReportConfiguration reportConfig = new SimpleXlsReportConfiguration();
		reportConfig.setDetectCellType(true);
		reportConfig.setIgnoreGraphics(true);
		//reportConfig.setWhitePageBackground(true);
		//reportConfig.setRemoveEmptySpaceBetweenColumns(true);
		//reportConfig.setRemoveEmptySpaceBetweenRows(true);
		reportConfig.setIgnorePageMargins(true);
		reportConfig.setShowGridLines(true);
		exporter.setConfiguration(reportConfig); 
		
		exporter.exportReport();  

		return out;
	}

	private ByteArrayOutputStream exportReportXlsx(JasperPrint jp)
			throws JRException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		JRXlsxExporter exporter = new JRXlsxExporter();
		exporter.setExporterInput(new SimpleExporterInput(jp));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
		
		SimpleXlsxExporterConfiguration xlsxExporterConfig = new SimpleXlsxExporterConfiguration();
		exporter.setConfiguration(xlsxExporterConfig);
		
		SimpleXlsxReportConfiguration xlsxReportConfig = new SimpleXlsxReportConfiguration();
		xlsxReportConfig.setDetectCellType(true);
		xlsxReportConfig.setIgnoreGraphics(true);
		//xlsxReportConfig.setWhitePageBackground(true);
		//xlsxReportConfig.setRemoveEmptySpaceBetweenColumns(true);
		//xlsxReportConfig.setRemoveEmptySpaceBetweenRows(true);
		xlsxReportConfig.setIgnorePageMargins(true);
		xlsxReportConfig.setShowGridLines(true);
		exporter.setConfiguration(xlsxReportConfig);
		
		exporter.exportReport();

		return out;
	}

	private ByteArrayOutputStream exportReportPdf(JasperPrint jp)
			throws JRException {
		JRPdfExporter exporter = new JRPdfExporter();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		exporter.setExporterInput(new SimpleExporterInput(jp));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
		
		SimplePdfExporterConfiguration pdfExporterConfig = new SimplePdfExporterConfiguration();
		exporter.setConfiguration(pdfExporterConfig);

		SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
		exporter.setConfiguration(reportConfig);
		
		exporter.exportReport();
		return out;
	}

	private ByteArrayOutputStream exportReportRtf(JasperPrint jp)
			throws JRException {
		JRRtfExporter exporter = new JRRtfExporter();
		exporter.setExporterInput(new SimpleExporterInput(jp));
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		SimpleWriterExporterOutput output = new SimpleWriterExporterOutput(out);
		exporter.setExporterOutput(output);
		
		SimpleRtfExporterConfiguration exporterConfig = new SimpleRtfExporterConfiguration();
		exporter.setConfiguration(exporterConfig);
		
		SimpleRtfReportConfiguration reportConfig = new SimpleRtfReportConfiguration();
		exporter.setConfiguration(reportConfig);
		
		exporter.exportReport();

		return out;
	}

	private ByteArrayOutputStream exportReportText(JasperPrint jp)
			throws JRException {
		JRTextExporter exporter = new JRTextExporter();
		exporter.setExporterInput(new SimpleExporterInput(jp));
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		SimpleWriterExporterOutput output = new SimpleWriterExporterOutput(out);
		exporter.setExporterOutput(output);
		
		SimpleTextExporterConfiguration exporterConfig = new SimpleTextExporterConfiguration();
		exporter.setConfiguration(exporterConfig);
		
		SimpleTextReportConfiguration reportConfig = new SimpleTextReportConfiguration();
		reportConfig.setCharHeight(10F);
		reportConfig.setCharWidth(10F);
		exporter.setConfiguration(reportConfig);
		
		exporter.exportReport();
		return out;
	}
	
	private void configureForHtml(HtmlExporter htmlExporter, ByteArrayOutputStream out, JasperPrint jp,
			HttpServletRequest request, String footer) {
		ReportUtil.addJasperPrint(request, jp, getClass().getName());
		htmlExporter.setExporterInput(new SimpleExporterInput(jp));
		
		SimpleHtmlExporterOutput output = new SimpleHtmlExporterOutput(out);
		output.setImageHandler(new WebHtmlResourceHandler(request.getContextPath() + "/image?image={0}"));
		htmlExporter.setExporterOutput(output);
		
		SimpleHtmlExporterConfiguration htmlExporterConfig = new SimpleHtmlExporterConfiguration();
		//exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, false);*/
		
		String header = "<html><head><style> @page {size: landscape; margin-left: 0.00in;margin-right: 0.00in;margin-top: 0.00in;margin-bottom: 0.00in;}</style></head><body>";
		htmlExporterConfig.setHtmlHeader(header);
		
		htmlExporterConfig.setHtmlFooter(footer);
		
		String betweenPagesHtml = "<div style=\"display:block;page-break-before:always;\"></div>";
		htmlExporterConfig.setBetweenPagesHtml(betweenPagesHtml);
				
		htmlExporter.setConfiguration(htmlExporterConfig);

		SimpleHtmlReportConfiguration reportConfig = new SimpleHtmlReportConfiguration();
		reportConfig.setIgnorePageMargins(true);
		reportConfig.setRemoveEmptySpaceBetweenRows(true);
		//reportConfig.setBorderCollapse("separate");
		reportConfig.setZoomRatio(new Float(0.97));
		htmlExporter.setConfiguration(reportConfig);
	}
	
	private ByteArrayOutputStream exportReportHtml(JasperPrint jp,
			HttpServletRequest request) throws JRException {
		HtmlExporter htmlExporter = new HtmlExporter();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		configureForHtml(htmlExporter, out, jp, request, StringUtils.EMPTY);
		
		htmlExporter.exportReport();
		
		ReportUtil.removeJasperPrint(request, getClass().getName());
		return out;
	}

	private ByteArrayOutputStream exportReportPrint(JasperPrint jp,
			HttpServletRequest request) throws JRException {
		HtmlExporter htmlExporter = new HtmlExporter();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		String footer = "<script language=\"javascript\">  try  { document.execCommand('print', false, null); } catch(e) { window.print(); }</script></body></html>";
		configureForHtml(htmlExporter, out, jp, request, footer);
				
		htmlExporter.exportReport();
		
		ReportUtil.removeJasperPrint(request, getClass().getName());
		return out;
	}

	private ByteArrayOutputStream exportReportCsv(JasperPrint jp)throws JRException {
		JRCsvExporter exporter = new JRCsvExporter();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		exporter.setExporterInput(new SimpleExporterInput(jp));
		
		SimpleWriterExporterOutput output = new SimpleWriterExporterOutput(out);
		exporter.setExporterOutput(output);
		
		SimpleCsvExporterConfiguration csvExporterConfig = new SimpleCsvExporterConfiguration();
		exporter.setConfiguration(csvExporterConfig);

		SimpleCsvReportConfiguration reportConfig = new SimpleCsvReportConfiguration();
		exporter.setConfiguration(reportConfig);
		
		exporter.exportReport();

		return out;
	}
}
