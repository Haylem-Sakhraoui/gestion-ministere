package com.esprit.pfe.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private int status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object body;
    private String message;


    public AuthResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }


}
