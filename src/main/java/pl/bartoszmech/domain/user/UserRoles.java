package pl.bartoszmech.domain.user;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRoles {
    EMPLOYEE("employee"),
    MANAGER("manager"),
    ADMIN("admin");

    private final String roleName;

    UserRoles(String roleName) {
        this.roleName = roleName;
    }
    @JsonValue
    public String getRoleName() {
        return roleName;
    }
}

