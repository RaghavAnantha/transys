package com.transys.dao.admin;

import com.transys.core.dao.GenericDAO;
import com.transys.model.User;

public interface UserDAO extends GenericDAO {

    User getUserByName(String name);
}
