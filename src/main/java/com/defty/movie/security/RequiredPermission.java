package com.defty.movie.security;

import com.defty.movie.entity.Account;
import com.defty.movie.entity.Permission;
import com.defty.movie.repository.IPermissionRepository;
import com.defty.movie.service.IAuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequiredPermission {
    IAuthService authService;
    IPermissionRepository permissionRepository;

    public boolean checkPermission(String permissionCheck){
        Optional<Account> account = authService.getCurrentAccount();
        if(account.isPresent()){
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
