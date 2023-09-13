package com.mycompany.service;

import com.mycompany.dto.JwtReponseDTO;
import com.mycompany.dto.LoginDTO;
import com.mycompany.dto.SignupDTO;

public interface AuthenticationService {

    public JwtReponseDTO login(LoginDTO loginDTO);
    public Long signup(SignupDTO signupDTO);
}
