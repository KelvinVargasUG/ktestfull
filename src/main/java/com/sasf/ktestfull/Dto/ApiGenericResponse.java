package com.sasf.ktestfull.Dto;

import com.sasf.ktestfull.Constant.ApiStatusConst;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Generic response DTO for API responses.
 *
 * @param <T> the type of data contained in the response
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ApiGenericResponse<T> {

    @Builder.Default
    private int statusCode = ApiStatusConst.SUCCESS_CODE;

    @Builder.Default
    private String status = ApiStatusConst.SUCCESS_STATUS;

    @Builder.Default
    private String message = ApiStatusConst.SUCCESS_MESSAGE;

    private T data;
}
