package com.poliana.core.sponsorship;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author David Gilmore
 * @date 12/26/13
 */
@Repository
public class SponsorshipRepo {

    @Autowired
    private JdbcTemplate impalaTemplate;

    private static final Logger logger = Logger.getLogger(SponsorshipRepo.class);

    /**
     * For a HashMap of Cycle->Chamber entries, return a list of sponsorships for each entry indexed by congressional
     * cycle in a HashMap.
     *
     * @param termChamberMap
     * @return
     */
    public HashMap<Integer, List<SponsorshipCount>> getSponsorshipCounts(HashMap<Integer,String> termChamberMap) {

        Iterator it = termChamberMap.entrySet().iterator();
        Map.Entry pairs;
        String conditions;

        if (it.hasNext()) {
            pairs = (Map.Entry)it.next();
            conditions = "(congress = \'" + pairs.getKey() + "\' AND SUBSTR(bill_type,1,1) = \'" + pairs.getValue() + "\') ";

            while (it.hasNext()) {
                conditions += "OR (congress = \'" + pairs.getKey() + "\' AND SUBSTR(bill_type,1,1) = \'" + pairs.getValue() + "\')";
                it.remove(); // avoids a ConcurrentModificationException
            }

            try {
                String query = "SELECT s.bioguide_id as sponsor, c.bioguide_id as cosponsor, congress, " +
                        "count(c.bioguide_id) as counts, SUBSTR(bill_type,1,1) as bill_type FROM  " +
                        "    bills.bill_sponsorship_flat b JOIN entities.legislators s ON b.sponsor_thomas_id = s.thomas_id " +
                        "    JOIN entities.legislators c ON b.cosponsor_thomas_id = c.thomas_id" +
                        "    WHERE " + conditions + " GROUP BY s.bioguide_id, c.bioguide_id, congress, bill_type";
                return impalaTemplate.query(query, new SponsorshipRSMapper());
            }
            catch (Exception e) {
                logger.error(e);
            }

        }
        return null;
    }

    /**
     * Queries impala for a count of how many times each legislator cosponsored a bill sponsored by each other
     * legislator in the same chamber during a specified congressional session. Returns a list of sponsorship
     * objects
     *
     * @param chamber
     * @param congress
     * @return
     */
    public List<SponsorshipCount> getSponsorshipCounts(String chamber, int congress) {

        try {
            String query = "SELECT s.bioguide_id, c.bioguide_id, count(c.bioguide_id) FROM " +
                    "bills.bill_sponsorship_flat b JOIN entities.legislators s ON b.sponsor_thomas_id = s.thomas_id " +
                    "JOIN entities.legislators c ON b.cosponsor_thomas_id = c.thomas_id WHERE congress = \"" +
                    congress + "\" " + "AND SUBSTR(bill_type,1,1) = \"" + chamber +"\" GROUP BY s.bioguide_id, c.bioguide_id";
            return impalaTemplate.query(query, new SponsorshipCountsMapper(chamber,congress));
        }
        catch (Exception e) {
            logger.error(e);
        }
        return null;
    }
}
