package com.csuni.pjp.client.services;

import com.csuni.pjp.client.models.AuthResponse;
import com.csuni.pjp.client.models.UserLoginDTO;
import com.csuni.pjp.client.models.UserRegisterDTO;

public interface IWebAuthGateway {
    AuthResponse login(UserLoginDTO userLoginDTO);
    AuthResponse register(UserRegisterDTO userRegisterDTO);
    void logout();
}
