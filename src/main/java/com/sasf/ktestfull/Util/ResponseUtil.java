package com.sasf.ktestfull.Util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sasf.ktestfull.Constant.ApiStatusConst;
import com.sasf.ktestfull.Dto.ApiGenericResponse;

/**
 * Utility class for generating standardized API responses.
 */
public class ResponseUtil {
    /**
     * Generates a successful response with the provided data.
     *
     * @param message the message to include in the response
     * @param data    the data to include in the response
     * @return a ResponseEntity containing the ApiGenericResponse
     */
    public static <T> ResponseEntity<ApiGenericResponse<T>> ok(String message, T data) {
        return ResponseEntity.ok(ApiGenericResponse.<T>builder()
                .data(data)
                .message(message != null ? message : ApiStatusConst.SUCCESS_MESSAGE)
                .build());
    }

    /**
     * Generates a created response with the provided data.
     *
     * @param message the message to include in the response
     * @param data    the data to include in the response
     * @return a ResponseEntity containing the ApiGenericResponse
     */
    public static <T> ResponseEntity<ApiGenericResponse<T>> created(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiGenericResponse.<T>builder()
                .data(data)
                .message(message != null ? message : ApiStatusConst.CREATED_MESSAGE)
                .status(ApiStatusConst.CREATED_STATUS)
                .statusCode(ApiStatusConst.CREATED_CODE)
                .build());
    }

    /**
     * Generates an error response with the provided message and status.
     *
     * @param message the error message to include in the response
     * @param status  the HTTP status to set for the response
     * @return a ResponseEntity containing the ApiGenericResponse
     */
    public static <T> ResponseEntity<ApiGenericResponse<T>> error(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(ApiGenericResponse.<T>builder()
                        .message(message != null ? message : ApiStatusConst.ERROR_MESSAGE)
                        .status(ApiStatusConst.ERROR_STATUS)
                        .statusCode(status.value())
                        .build());
    }
}
