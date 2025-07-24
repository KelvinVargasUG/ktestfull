package com.sasf.ktestfull.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuplicateResourceException extends RuntimeException {

    private String resourceName;

    private String fieldName;

    private Object fieldValue;

    public DuplicateResourceException(String message) {
        super(message);
    }
}
