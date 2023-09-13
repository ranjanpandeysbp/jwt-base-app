package com.mycompany.service;

import com.mycompany.dto.ErrorDTO;
import com.mycompany.dto.JwtReponseDTO;
import com.mycompany.dto.LoginDTO;
import com.mycompany.dto.SignupDTO;
import com.mycompany.entity.RoleEntity;
import com.mycompany.entity.UserEntity;
import com.mycompany.enums.ERole;
import com.mycompany.exception.BusinessException;
import com.mycompany.repository.RoleRepository;
import com.mycompany.repository.UserRepository;
import com.mycompany.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public JwtReponseDTO login(@RequestBody LoginDTO loginDTO) {
       Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtil.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(role->role.getAuthority())
                .collect(Collectors.toList());

        JwtReponseDTO jwtReponseDTO = new JwtReponseDTO();
        jwtReponseDTO.setFirstName(userDetails.getFirstName());
        jwtReponseDTO.setLastName(userDetails.getLastName());
        jwtReponseDTO.setToken(jwtToken);
        jwtReponseDTO.setRoles(roles);
        jwtReponseDTO.setId(userDetails.getId());

        return jwtReponseDTO;
    }

    @Override
    public Long signup(SignupDTO signupDTO) {
        List<ErrorDTO> errorDTOS = new ArrayList<>();
        if(userRepository.existsByEmail(signupDTO.getEmail())){
            errorDTOS.add(new ErrorDTO("AUTH_001", "Email already exist"));
            throw new BusinessException(errorDTOS);
        }
        String hashedPassword = passwordEncoder.encode(signupDTO.getPassword());
        Set<RoleEntity> roleEntities = new HashSet<>();
        Optional<RoleEntity> optRole = null;
        if (signupDTO.getRole() != null && signupDTO.getRole().equals("ADMIN")){
            optRole = roleRepository.findByRoleName(ERole.ROLE_ADMIN);
        }else if (signupDTO.getRole() != null && signupDTO.getRole().equals("MOD")){
            optRole = roleRepository.findByRoleName(ERole.ROLE_MODERATOR);
        }
        else{
            optRole = roleRepository.findByRoleName(ERole.ROLE_USER);
        }

        roleEntities.add(optRole.get());

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(signupDTO.getEmail());
        userEntity.setFirstName(signupDTO.getFirstName());
        userEntity.setLastName(signupDTO.getLastName());
        userEntity.setPhone(signupDTO.getPhone());
        userEntity.setPassword(hashedPassword);
        userEntity.setRoles(roleEntities);

        userEntity = userRepository.save(userEntity);
        return userEntity.getId();
    }
}
