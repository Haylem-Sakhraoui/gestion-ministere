package com.esprit.pfe.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ChartDataResponse {
    private Date Creation_Date;
    private long claimCount;
    public ChartDataResponse(Date dateCreation, long reclamationCount) {
        this.Creation_Date = dateCreation;
        this.claimCount = reclamationCount;
    }
}
