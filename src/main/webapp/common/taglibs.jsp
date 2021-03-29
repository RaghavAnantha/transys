<%@ page language="java" errorPage="/transys/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>

<%@ taglib prefix="tg" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="transys" uri="/WEB-INF/tlds/transys.tld" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="resourceCtx" value="${pageContext.request.contextPath}"/>
<c:set var="urlCtx" value="${ctx}/${urlContext}"/>

<c:set var="jsCtx" value="${ctx}/js"/>
<c:set var="reportsMenuCtx" value="${ctx}/reports"/>
<c:set var="masterDataMenuCtx" value="${ctx}/masterData"/>

<c:set var="imageCtx" value="${ctx}/images"/>
<c:set var="mapImageCtx" value="${imageCtx}/map"/>
<c:set var="preLoaderImage" value="${imageCtx}/preloader.gif"/>
<c:set var="preLoaderReportImage" value="${imageCtx}/preLoaderReport.gif"/>
<c:set var="addImage" value="${imageCtx}/add.png"/>
<c:set var="addNewImage" value="${imageCtx}/addnew.png"/>
<c:set var="editImage" value="${imageCtx}/edit.png"/>
<c:set var="deleteImage" value="${imageCtx}/delete.png"/>
<c:set var="csvImage" value="${imageCtx}/csv.png"/>
<c:set var="pdfImage" value="${imageCtx}/pdf.png"/>
<c:set var="printImage" value="${imageCtx}/print.png"/>
<c:set var="excelImage" value="${imageCtx}/excel.png"/>
<c:set var="iconWarningImage" value="${imageCtx}/iconWarning.gif"/>
<c:set var="iconInformationImage" value="${imageCtx}/iconInformation.gif"/>

<c:set var="loadingMsg" value="Loading....<img src='${preLoaderImage}' border='0' class='toolbarButton'/>"/>
<!-- 
<c:set var="reportLoadingMsg" value="Loading....<img src='${preLoaderReportImage}' border='0' class='toolbarButton'/>"/>
-->
<c:set var="reportLoadingMsg" value="Loading....<i class='fa fa-spinner fa-spin' style='font-size:24px'/></i>"/>

<c:set var="csrfParam" value="${_csrf.parameterName}=${_csrf.token}"/>