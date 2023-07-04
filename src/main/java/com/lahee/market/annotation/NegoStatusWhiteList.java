package com.lahee.market.annotation;

import com.lahee.market.validation.annotation.NegoWhitelistValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NegoWhitelistValidator.class)
public @interface NegoStatusWhiteList {
    String message() default "status에 들어올 수 있는 값이 아닙니다.(확정,거절,수락)중 입력해주세요";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}