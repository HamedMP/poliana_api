package com.poliana.core.industryFinance.jobs;

import com.poliana.core.industries.IndustryRepo;
import com.poliana.core.industryFinance.repositories.IndustryContributionMongoRepo;
import com.poliana.core.industryFinance.IndustryContributionService;
import com.poliana.core.industries.Industry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * @author David Gilmore
 * @date 11/22/13
 */
@Component
public class IndContrSessionBatch {

    @Autowired
    private IndustryContributionService industryContributionService;

    @Autowired
    private IndustryRepo industryRepo;

    @Autowired
    private IndustryContributionMongoRepo industryContributionMongoRepo;

    public void processIndustryTotals(int congress) {
        Iterator<Industry> industries = industryRepo.getIndustriesFromMongo();
        while (industries.hasNext()) {
            String industry = industries.next().getIndustryId();
            industryContributionMongoRepo.saveIndTimeRangeTotal(
                    industryContributionService.industryTimeRangeTotals(industry,congress,24));
        }
    }
}