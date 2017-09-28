package com.favorapp.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserRolesService {

    @Autowired
    private UserRolesRepository userRolesRepository;

    public void save(UserRoles userRoles) {
        userRolesRepository.save(userRoles);
    }

    public Collection<UserRoles> getAllUserRoles() {
    Collection<UserRoles> rolelist = new ArrayList<>();
        userRolesRepository.findAll().forEach(userRoles -> rolelist.add(userRoles));
    return rolelist;
    }
}
