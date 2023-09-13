package com.mycompany;

import com.mycompany.entity.RoleEntity;
import com.mycompany.enums.ERole;
import com.mycompany.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Startup implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        Optional<RoleEntity> optRoleAd = roleRepository.findByRoleName(ERole.ROLE_ADMIN);
        if(optRoleAd.isEmpty()){
            RoleEntity re = new RoleEntity();
            re.setRoleName(ERole.ROLE_ADMIN);
            roleRepository.save(re);
        }
        Optional<RoleEntity> optRoleMod = roleRepository.findByRoleName(ERole.ROLE_MODERATOR);
        if(optRoleMod.isEmpty()){
            RoleEntity re = new RoleEntity();
            re.setRoleName(ERole.ROLE_MODERATOR);
            roleRepository.save(re);
        }
        Optional<RoleEntity> optRoleUs = roleRepository.findByRoleName(ERole.ROLE_USER);
        if(optRoleUs.isEmpty()){
            RoleEntity re = new RoleEntity();
            re.setRoleName(ERole.ROLE_USER);
            roleRepository.save(re);
        }
    }
}
