package com.idealizer.review_x.common.dtos;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ResponseError(int status, String message, List<FieldError> errors) {

   public static ResponseError defaultMessage(String message){
       return new ResponseError(HttpStatus.BAD_GATEWAY.value(), message, List.of());
   }

   public static ResponseError conflict(String message){
       return new ResponseError(HttpStatus.CONFLICT.value(), message, List.of());
   }
   public static ResponseError badRequest(String message){
       return new ResponseError(HttpStatus.BAD_REQUEST.value(), message, List.of());
   }
}
