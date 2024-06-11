package com.esprit.pfe.repository;

import com.esprit.pfe.DTO.ChartDataResponse;
import com.esprit.pfe.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim,Long> {
    @Query("SELECT NEW com.esprit.pfe.DTO.ChartDataResponse(c.Creation_Date, COUNT(c.id)) FROM Claim c GROUP BY c.Creation_Date")
    List<ChartDataResponse> getReclamationChartData();
}
