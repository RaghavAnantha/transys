package com.transys.dao.admin;

import java.util.HashMap;
import java.util.Map;

import com.transys.core.dao.GenericJpaDAO;
import com.transys.model.User;

@SuppressWarnings("unchecked")
public class UserDAOImpl extends GenericJpaDAO implements UserDAO {

    @Override
    public User getUserByName(String name) {
		Map map = new HashMap();
		map.put("name", name);
		return ((User)getByNamedQuery("user.getByName", map));
    }
}
