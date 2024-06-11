package com.esprit.pfe.service;

    
import com.esprit.pfe.repository.ClaimRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

    @Service
    @NoArgsConstructor
    public class claimStatService implements IclaimStatservice  {
        @Autowired
        private ClaimRepository claimRepository;
        @Override
        public List<Object> getReclamationStat(){
            List<Object> result = Collections.singletonList(claimRepository.getReclamationChartData());

            return result;
        }

    }




