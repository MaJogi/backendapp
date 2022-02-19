package com.idcodevalidator.backendapp.util.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class ValidationRequest {
    @NotBlank
    private String idCode;

    public ValidationRequest() {
        super();
    }
}
