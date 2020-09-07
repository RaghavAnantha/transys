<%@ page language="java" errorPage="/common/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tg" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="transys" uri="/WEB-INF/tlds/transys.tld" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="resourceCtx" value="${pageContext.request.contextPath}"/>

<c:set var="reportsMenuCtx" value="${ctx}/reports"/>
<c:set var="masterDataMenuCtx" value="${ctx}/masterData"/>

<c:set var="imageCtx" value="${ctx}/images"/>
<c:set var="preLoaderImage" value="${imageCtx}/preloader.gif"/>
<c:set var="addImage" value="${imageCtx}/add.png"/>
<c:set var="editImage" value="${imageCtx}/edit.png"/>
<c:set var="deleteImage" value="${imageCtx}/delete.png"/>
<c:set var="csvImage" value="${imageCtx}/csv.png"/>
<c:set var="pdfImage" value="${imageCtx}/pdf.png"/>
<c:set var="excelImage" value="${imageCtx}/excel.png"/>

