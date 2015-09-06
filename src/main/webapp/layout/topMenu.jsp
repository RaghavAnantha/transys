<%@ include file="/common/taglibs.jsp" %>
<%@page import="java.util.List,
			    java.util.ArrayList,
			    com.transys.model.BusinessObject,
			    com.transys.model.menu.*"%>
<c:if test="${sessionScope.userInfo.role.id == 1}">
<div style="width:100%" id="navigation" class="navmenu">
<%
    MenuTree menuTree = (MenuTree) session.getAttribute("menuTree");
	BusinessObject bo = new BusinessObject();
	bo.setAction("customer");
	bo.setId(1l);
	bo.setUrl("customer");
	List<BusinessObject> businessObjects = new ArrayList<BusinessObject>();
	businessObjects.add(bo);
    //List<BusinessObject> businessObjects = MenuHelper.getMenuForLevel(menuTree, 2);
if (menuTree!=null) {
%>
<ul>
    <%
	    String cssClass="";
		BusinessObject currentBo = (BusinessObject)session.getAttribute("curObj"); 
		String hierarchy ="";
		if (currentBo!=null) {
			hierarchy = currentBo.getObjectHierarchy();
		}
		for (BusinessObject businessObject : businessObjects) {
			if(businessObject.getId()==110 || businessObject.getId()==120 || businessObject.getId()==130 
			   || businessObject.getId()==140 || businessObject.getId()==150 || businessObject.getId()==201 
			   || businessObject.getId()==202 || businessObject.getId()==203 || businessObject.getId()==204
			   || businessObject.getId()==205 || businessObject.getId()==206 || businessObject.getId()==301 
			   || businessObject.getId()==306 || businessObject.getId()==20012 || businessObject.getId()==1005
			   || businessObject.getId()==600110){ 
			
				if (hierarchy.contains("/"+businessObject.getId()+"/")) {
				cssClass="selected";
			}
			else {
				cssClass="";
			}
			BusinessObject bo2=null;
    		List<BusinessObject> submenus = MenuHelper.getMenuForParent(menuTree, businessObject.getId());
    		if (submenus!=null && submenus.size()>0) {
    			bo2 = submenus.get(0);
    		}
    		else {
    			bo2 = businessObject;
    		}
	%>
    <li>
    <c:set var="obj" value="<%=businessObject.getObjectName()%>"></c:set>
       <a href="${ctx}<%=bo2.getAction()%>" class="<%=cssClass%>">${obj}</a>
    </li>
    <%}}%>
</ul>
<% }
%>
</div>
<div style="width:100%" id="navigation" class="navmenu">
<%
    MenuTree menuTree2 = (MenuTree) session.getAttribute("menuTree");
 	BusinessObject bot = new BusinessObject();
 	bot.setAction("customer");
 	bot.setId(1l);
 	bot.setUrl("customer");
 	List<BusinessObject> businessObjects2 = new ArrayList<BusinessObject>();
 	businessObjects2.add(bot);
     //List<BusinessObject> businessObjects2 = MenuHelper.getMenuForLevel(menuTree2, 2);
if (menuTree!=null) {
%>
<ul>
    <%
	    String cssClass="";
		BusinessObject currentBo = (BusinessObject)session.getAttribute("curObj"); 
		String hierarchy ="";
		if (currentBo!=null) {
			hierarchy = currentBo.getObjectHierarchy();
		}
		for (BusinessObject businessObject : businessObjects2) {
			if(businessObject.getId()==401 || businessObject.getId()==406 || businessObject.getId()==501 
			|| businessObject.getId()==601 || businessObject.getId()==701 || businessObject.getId()==801 
			|| businessObject.getId()==901 || businessObject.getId()==902 || businessObject.getId()==903 
			|| businessObject.getId()==904 || businessObject.getId()==905 || businessObject.getId()==908 
			|| businessObject.getId()==1001 || businessObject.getId()==2222){
			if (hierarchy.contains("/"+businessObject.getId()+"/")) {
				cssClass="selected";
			}
			else {
				cssClass="";
			}
			BusinessObject bo2=null;
    		List<BusinessObject> submenus = MenuHelper.getMenuForParent(menuTree2, businessObject.getId());
    		if (submenus!=null && submenus.size()>0) {
    			bo2 = submenus.get(0);
    		}
    		else {
    			bo2 = businessObject;
    		}
	%>
    <li>
    <c:set var="obj" value="<%=businessObject.getObjectName()%>"></c:set>
       <a href="${ctx}<%=bo2.getAction()%>" class="<%=cssClass%>">${obj}</a>
    </li>
    <%}}%>
</ul>
<% }
%>
</div>
</c:if>


<c:if test="${sessionScope.userInfo.role.id != 1}">
<div style="width:100%" id="navigation" class="navmenu">
<%
	MenuTree menuTree = (MenuTree) session.getAttribute("menuTree");
	BusinessObject bo = new BusinessObject();
	bo.setId(1l);
	bo.setObjectName("Customer");
	bo.setAction("customer");
	bo.setUrl("customer");
	bo.setObjectHierarchy("/1/");
	List<BusinessObject> businessObjects = new ArrayList<BusinessObject>();
 	businessObjects.add(bo);
     //List<BusinessObject> businessObjects2 = MenuHelper.getMenuForLevel(menuTree2, 2);
	
if (menuTree!=null) {
%>
<ul>
    <%
	    String cssClass="";
		BusinessObject currentBo = (BusinessObject)session.getAttribute("curObj"); 
		String hierarchy ="";
		if (currentBo!=null) {
			hierarchy = currentBo.getObjectHierarchy();
		}
		for (BusinessObject businessObject : businessObjects) {			
			if (hierarchy.contains("/"+businessObject.getId()+"/")) {
				cssClass="selected";
			}
			else {
				cssClass="";
			}
			BusinessObject bo2=null;
    		List<BusinessObject> submenus = MenuHelper.getMenuForParent(menuTree, businessObject.getId());
    		if (submenus!=null && submenus.size()>0) {
    			bo2 = submenus.get(0);
    		}
    		else {
    			bo2 = businessObject;
    		}
	%>
    <li>
    <c:set var="obj" value="<%=businessObject.getObjectName()%>"></c:set>
       <a href="${ctx}<%=bo2.getAction()%>" class="<%=cssClass%>">${obj}</a>
    </li>
    <%}%>
</ul>
<% }
%>
</div>
</c:if>