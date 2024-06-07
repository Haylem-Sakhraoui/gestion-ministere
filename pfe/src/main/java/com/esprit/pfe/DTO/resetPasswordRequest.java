package com.esprit.pfe.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class resetPasswordRequest {
    private String email;
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;

}
