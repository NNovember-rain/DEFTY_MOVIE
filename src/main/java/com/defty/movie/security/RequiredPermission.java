package com.defty.movie.security;

import com.defty.movie.entity.Account;
import com.defty.movie.entity.Permission;
import com.defty.movie.entity.RolePermission;
import com.defty.movie.repository.IPermissionRepository;
import com.defty.movie.repository.IRolePermissionRepository;
import com.defty.movie.service.impl.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequiredPermission {
    AccountService accountService;
    IRolePermissionRepository rolePermissionRepository;
    IPermissionRepository permissionRepository;

    public boolean checkPermission(String permissionCheck){
        if (permissionCheck == null || permissionCheck.isEmpty()) {
            return true;
        }
        Optional<Account> account = accountService.getCurrentAccount();
        boolean success = false;
        if(account.isPresent()){
            Set<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(account.get().getRole().getId());
            Set<Permission> permissions = new HashSet<>();
            for (RolePermission rolePermission : rolePermissions) {
                Permission permission = permissionRepository.findById(rolePermission.getPermission().getId()).orElse(null);
                permissions.add(permission);
            }
            for(Permission permission: permissions){
                if (Objects.equals(permission.getName(), permissionCheck)) {
                    success = true;
                    break;
                }
            }
        }
        return success;
    }
}
