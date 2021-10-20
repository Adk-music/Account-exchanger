package com.kutyrina.accountexchanger.component;

import com.kutyrina.accountexchanger.configuration.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class SessionHolderComponent {

    public String getCurrentUserLogin() {
        return getAuthUser().getUsername();
    }

    public Long getCurrentUserId() {
        return getAuthUser().getId();
    }

    public void validateAccessToAccount(Long accountId) {
        boolean isAccountAccessible = getAuthUser().getAccountIds().contains(accountId);
        if (!isAccountAccessible) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You not allowed to access this account");
        }
    }

    private AuthUser getAuthUser() {
        return ((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

}
