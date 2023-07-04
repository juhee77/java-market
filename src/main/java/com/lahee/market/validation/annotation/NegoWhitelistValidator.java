package com.lahee.market.validation.annotation;

import com.lahee.market.annotation.NegoStatusWhiteList;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class NegoWhitelistValidator implements ConstraintValidator<NegoStatusWhiteList, String> {
    private final Set<String> whiteList;

    public NegoWhitelistValidator() {
        this.whiteList = new HashSet<>();
        this.whiteList.add("수락");
        this.whiteList.add("거절");
        this.whiteList.add("확정");
        this.whiteList.add(null);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return whiteList.contains(value);
    }
}