package com.rustam.modern_dentistry.dao.entity.enums;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum Role implements GrantedAuthority {

    ADMIN("ADMIN"),
    DOCTOR_FULL_PERMISSION("DOCTOR_FULL_PERMISSION"),
    DOCTOR("DOCTOR"),
    RECEPTION("RECEPTION"),
    //PATIENT("PATIENT"),
    WAREHOUSE_MAN("WAREHOUSE_MAN"),
    USER("USER"),
    PATIENT("PATIENT"),
    ACCOUNTANT("ACCOUNTANT");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
