package com.deadpineapple.dal.dao;

import com.deadpineapple.dal.entity.UserAccount;

/**
 * Created by mikael on 30/03/16.
 */
public interface IUserDao {
        public UserAccount find (String email);
        public UserAccount checkCredentials (String login, String password);
        public void saveUser(UserAccount user) ;
        public void deleteUser(UserAccount user);
        public void deleteUserById(Long id);
}