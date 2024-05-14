package com.testForCS.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.testForCS.constant.Code;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error {

    private Code code;
    private String message;

}
