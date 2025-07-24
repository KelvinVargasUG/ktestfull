package com.sasf.ktestfull.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessDeniedException extends RuntimeException {

    private String resource;

    private String action;

    private String reason;

    public AccessDeniedException(String message) {
        super(message);
    }
}
