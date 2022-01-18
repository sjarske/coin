package com.coin.coin.services;

import com.coin.coin.models.Role;
import com.coin.coin.models.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    void addExchangeToUser(String username, String name, String ApiKey, String secretKey);
    User getUser(String username);
    List<User> getUsers();
}
