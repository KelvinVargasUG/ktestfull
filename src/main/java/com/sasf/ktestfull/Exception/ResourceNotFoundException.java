package com.sasf.ktestfull.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {

    private String resourceName;

    private String fieldName;

    private Object fieldValue;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
