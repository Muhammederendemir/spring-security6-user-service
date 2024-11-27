package com.med.springsecurity6.user.service;



import com.med.springsecurity6.user.model.UserRequest;
import com.med.springsecurity6.user.model.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;


public interface UserService {

    UserResponse saveUser(UserRequest userRequest);

    UserResponse getUser();

    List<UserResponse> getAllUser();

}