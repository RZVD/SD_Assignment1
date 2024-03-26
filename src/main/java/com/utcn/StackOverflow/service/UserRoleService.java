package com.utcn.StackOverflow.service;

import com.utcn.StackOverflow.entity.UserRole;
import com.utcn.StackOverflow.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {
    @Autowired
    private UserRoleRepository userRoleRepository;

   public UserRole save(UserRole userRole) {
       return userRoleRepository.save(userRole);
   }
}
