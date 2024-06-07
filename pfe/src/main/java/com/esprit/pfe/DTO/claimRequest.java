package com.esprit.pfe.DTO;

import com.esprit.pfe.entity.Status_Claim;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class claimRequest {

    private String content;
    private Status_Claim status;
}
