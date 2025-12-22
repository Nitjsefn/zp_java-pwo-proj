package com.csuni.pjp.client.services;

import com.csuni.pjp.client.models.UserLoginDTO;
import com.csuni.pjp.client.models.UserRegisterDTO;

public interface IWebAuthService {
    void login(UserLoginDTO credentials);
    void register(UserRegisterDTO user);
}
