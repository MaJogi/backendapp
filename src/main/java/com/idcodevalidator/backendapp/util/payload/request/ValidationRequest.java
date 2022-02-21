package com.idcodevalidator.backendapp.util.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

/**
 * Request which states exact values that backend is requesting from frontend form.
 */
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
