package com.transys.core;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

//import com.transys.core.dao.GenericDAO;
//import com.transys.dao.admin.UserDAO;
import com.transys.model.BusinessObject;
import com.transys.model.Role;
import com.transys.model.User;
import com.transys.model.menu.MenuHelper;
import com.transys.model.menu.MenuTree;

public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	/*
	@Autowired
	private GenericDAO genericDAO;

	public void setGenericDAO(GenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}

	@Autowired
	private UserDAO userDAO;

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}*/

	public AuthenticationSuccessHandler() {
		super();
	}

	public AuthenticationSuccessHandler(String defaultUrl) {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication auth)
			throws IOException, ServletException {
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		//User user = userDAO.getUserByName(userDetails.getUsername());
		User user = mockUser(userDetails);
		
		Timestamp currTime = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
		user.setLastLoginDate(currTime);
		Role role = user.getRole();
		
		// request.getSession().setAttribute("theme", role.getTheme());
		//genericDAO.saveOrUpdate(user);
		request.getSession().setAttribute("userInfo", user);
		/*StringBuffer query = new StringBuffer(
				"select distinct bo from BusinessObject bo, RolePrivilege rp where rp.businessObject.id=bo.id and rp.role.id="
						+ role.getId());
		query.append(" order by bo.objectLevel, bo.displayOrder");*/
		try {
			//List<BusinessObject> businessObjects = genericDAO.executeSimpleQuery(query.toString());
			List<BusinessObject> businessObjects = mockBOList();
			MenuTree menuTree = MenuHelper.getMenuTree(businessObjects);
			request.getSession().setAttribute("menuTree", menuTree);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		super.onAuthenticationSuccess(request, response, auth);
	}
	
	private User mockUser(UserDetails userDetails) {
		User user = new User();
		user.setName(userDetails.getUsername());
		return user;
	}
	
	private List<BusinessObject> mockBOList() {
		/*
		 insert  into `business_object`(`ID`,`ACTION`,`DISPLAY_TAG`,`OBJECT_LEVEL`,`OBJECT_NAME`,`URL`,`created_at`,`created_by`,`modified_at`,`modified_by`,`status`,`display_order`,`hidden`,`parent_id`,`hierarchy`,`url_context`) 
		 values (1,'/home/home.do','LUTransport',1,'LUTransport','/home/home.do',NULL,NULL,NULL,NULL,1,1,0,NULL,'/1/',NULL),
		 (110,'/operator/tripsheet/list.do?rst=1','Trip Sheet',2,'Trip Sheet','/operator/tripsheet/home.do,/operator/tripsheet/list.do,/operator/tripsheet/create.do,/operator/tripsheet/save.do,/operator/tripsheet/ajax.do',NULL,NULL,NULL,NULL,1,2,0,1,'/1/110/',NULL),
		 */
		BusinessObject bo = new BusinessObject();
		bo.setId(1l);
		bo.setObjectName("Customer");
		bo.setAction("transys/customer");
		bo.setUrl("transys/customer");
		bo.setObjectHierarchy("/1/");
		
		List<BusinessObject> businessObjects = new ArrayList<BusinessObject>();
		businessObjects.add(bo);
		
		return businessObjects;
	}
	
}