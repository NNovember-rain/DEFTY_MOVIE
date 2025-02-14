package com.defty.movie.security;

import com.defty.movie.entity.Account;
import com.defty.movie.entity.Permission;
import com.defty.movie.entity.Role;
import com.defty.movie.repository.IPermissionRepository;
import com.defty.movie.repository.IRoleRepository;
import com.defty.movie.service.IAuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequiredPermission {
    IAuthService authService;
    IRoleRepository roleRepository;
    IPermissionRepository permissionRepository;

    public boolean checkPermission(String permissionCheck){
        Optional<Account> account = authService.getCurrentAccount();
        if(account.isPresent()){
            Optional<Role> roleOptional = roleRepository.findById(account.get().getRole().getId());
            String PREFIX_SECURITY = "SECURITY | ";
            if (roleOptional.isEmpty()){
                log.error("{}Role not found", PREFIX_SECURITY);
                return false;
            }
            Role role = roleOptional.get();
            if(role.getStatus() == 0){
                log.error("{}Role is disabled", PREFIX_SECURITY);
                return false;
            }
            Set<Permission> permissions = permissionRepository.findPermissionsByRoleId(account.get().getRole().getId());
            for(Permission permission : permissions){
                if (permission.getName().equals(permissionCheck)){
                    return true;
                }
            }
        }
        return false;
    }
}
