package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.AccountRequest;
import com.defty.movie.dto.response.AccountResponse;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.service.IAccountService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin/account")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {
    String PREFIX_ACCOUNT = "ACCOUNT | ";
    IAccountService accountService;

    @PostMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_ACCOUNT')")
    public ResponseEntity<?> createAccount(@ModelAttribute AccountRequest accountRequest) {
        AccountResponse accountResponse = accountService.createAccount(accountRequest);
        log.info(PREFIX_ACCOUNT + "Create account success");
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(accountResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ALL_ACCOUNTS')")
    public ResponseEntity<?> getAllAccount(@Valid @RequestParam(value = "page", defaultValue = "0") int page,
                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                           @RequestParam(value = "username", required = false) String username) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<AccountResponse> accountResponses = accountService.findAccount(username, pageable);
        log.info(PREFIX_ACCOUNT + "Get all account success");
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(accountResponses)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_ACCOUNT')")
    public ResponseEntity<?> updateAccount(@Valid @PathVariable Integer id,
                                           @ModelAttribute AccountRequest accountRequest) {
        AccountResponse accountResponse = accountService.updateAccount(id, accountRequest);
        log.info(PREFIX_ACCOUNT + "Update account success");
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(accountResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ACCOUNT')")
    public ResponseEntity<?> getAccount(@PathVariable Integer id) {
        AccountResponse accountResponse = accountService.getAccount(id);
        log.info(PREFIX_ACCOUNT + "Get account success");
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(accountResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_ACCOUNTS')")
    public ResponseEntity<?> deleteAccount(@PathVariable List<Integer> ids) {
        accountService.deleteAccount(ids);
        log.info(PREFIX_ACCOUNT + "Delete account success");
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data("success")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
