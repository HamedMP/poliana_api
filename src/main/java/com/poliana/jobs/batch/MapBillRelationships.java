package com.poliana.jobs.batch;

import com.google.code.morphia.Key;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateResults;
import com.poliana.bills.models.BillMorphia;
import com.google.code.morphia.Datastore;
import com.mongodb.*;
import com.poliana.bills.entities.Bill;
import com.poliana.bills.models.Vote;
import com.poliana.bills.entities.VoteGT.VoteGT;
import com.poliana.bills.entities.VoteGT.Voter;
import com.poliana.bills.entities.VoteGT.Voters;
import com.poliana.bills.models.VoteMorphia;
import com.poliana.bills.repositories.BillCRUDRepo;
import com.poliana.bills.repositories.BillHadoopRepo;
import com.poliana.bills.repositories.VotesCRUDRepo;
import com.poliana.bills.services.BillService;
import com.poliana.entities.models.Legislator;
import com.poliana.entities.services.LegislatorService;
import org.apache.derby.impl.store.raw.data.UpdateOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author David Gilmore
 * @date 11/13/13
 */
@Component
public class MapBillRelationships {

   @Autowired
   private LegislatorService legislatorService;
   @Autowired
   private BillHadoopRepo billHadoopRepo;
   @Autowired
   private VotesCRUDRepo votesCRUDRepo;
   @Autowired
   private BillService billService;
   @Autowired
   private BillCRUDRepo billCRUDRepo;
   @Autowired
   private MongoTemplate mongoTemplate;
   @Autowired
   private DB mongoDb;
   @Autowired
   private Datastore mongoStore;

   public List<Vote> processVotes(List<VoteGT> votesGt) {

       List<Vote> votes = new LinkedList<>();
       for (VoteGT voteGt: votesGt) {
           Vote vote = new Vote();

           System.out.println("Processing: " + voteGt.getVoteId());
           vote.setVoteId(voteGt.getVoteId());
           vote.setCategory(voteGt.getCategory());
           vote.setCongress(voteGt.getCongress());

           int timeStamp = getTimestamp(voteGt.getDate());
           vote.setDate(timeStamp);
           vote.setBillInfo(voteGt.getBill());
           vote.setAmendment(voteGt.getAmendment());
           vote.setNomination(voteGt.getNomination());
           vote.setNumber(voteGt.getNumber());
           vote.setQuestion(voteGt.getQuestion());
           vote.setRequires(voteGt.getRequires());
           vote.setResult(voteGt.getResult());
           vote.setResultText(voteGt.getResultText());
           vote.setSession(voteGt.getSession());
           vote.setSourceUrl(voteGt.getSourceUrl());
           vote.setSubject(voteGt.getSubject());
           vote.setType(voteGt.getType());
           vote.setUpdatedAt(voteGt.getUpdatedAt());
           vote.setChamber(voteGt.getChamber());

           Calendar cal = Calendar.getInstance();
           cal.setTimeInMillis((long) timeStamp * 1000L);

           vote.setYear(cal.get(Calendar.YEAR));
           vote.setMonth(cal.get(Calendar.MONTH+1));

           Voters votersGt = voteGt.getVotes();
           List<Legislator> yeas = gtVoterToLegislator(votersGt.getYea(), timeStamp);
           List<Legislator> nays = gtVoterToLegislator(votersGt.getNay(), timeStamp);
           List<Legislator> notVoting = gtVoterToLegislator(votersGt.getNotVoting(), timeStamp);
           List<Legislator> present = gtVoterToLegislator(votersGt.getPresent(), timeStamp);

           vote.setYeaTotal(yeas.size());
           vote.setNayTotal(nays.size());
           vote.setNotVotingTotal(notVoting.size());
           vote.setPresentTotal(present.size());

           vote.setYeas(yeas);
           vote.setNays(nays);
           vote.setNotVoting(notVoting);
           vote.setPresent(present);

           votes.add(vote);
       }
       return votes;
   }

   public List<com.poliana.bills.models.Bill> processBills(List<Bill> bills) {
       List<com.poliana.bills.models.Bill> mappedBills = new LinkedList<>();
       int billIndex = 0;
       for (Bill bill : bills) {
           com.poliana.bills.models.Bill newBill = new com.poliana.bills.models.Bill();
           System.out.println("Processing bill: " +  ++billIndex + " " + bill.getBillId());

           newBill.setBillId(bill.getBillId());
           newBill.setVoteId(bill.getVoteId());
           newBill.setOfficialTitle(bill.getOfficialTitle());
           newBill.setPopularTitle(bill.getPopularTitle());
           newBill.setShortTitle(bill.getShortTitle());

           Legislator sponsor =
                   legislatorService.legislatorByIdTimestamp(bill.getSponsorId(), bill.getIntroducedAt());
           newBill.setSponsor(sponsor);

           List<Legislator> legislators = new LinkedList<>();
           for (String cosponsor: bill.getCosponsorIds()) {
               Legislator legislator =
                        legislatorService.legislatorByIdTimestamp(cosponsor, bill.getIntroducedAt());
                legislators.add(legislator);
           }
           newBill.setCosponsors(legislators);

           newBill.setTopSubject(bill.getTopSubject());
           newBill.setSubjects(bill.getSubjects());
           newBill.setSummary(bill.getSummary());
           newBill.setIntroducedAt(bill.getIntroducedAt());
           newBill.setHousePassageResult(bill.getHousePassageResult());
           newBill.setHousePassageResultAt(bill.getHousePassageResultAt());
           newBill.setSenateClotureResult(bill.getSenateClotureResult());
           newBill.setSenateClotureResultAt(bill.getSenateClotureResultAt());
           newBill.setSenatePassageResult(bill.getSenatePassageResult());
           newBill.setSenatePassageResultAt(bill.getSenatePassageResultAt());
           newBill.setAwaitingSignature(bill.isAwaitingSignature());
           newBill.setEnacted(bill.isEnacted());
           newBill.setStatus(bill.getStatus());
           newBill.setStatusAt(bill.getStatusAt());
           newBill.setCongress(bill.getCongress());
           newBill.setBillType(bill.getBillType());
           newBill.setYear(bill.getYear());
           newBill.setMonth(bill.getMonth());
           Vote vote = votesCRUDRepo.findByVoteId(bill.getVoteId());
           newBill.setVotes(vote);

           mappedBills.add(newBill);
       }
       return mappedBills;
   }

   public void mapVotesToBills() {


       DBCollection coll = mongoDb.getCollection("bills");

       try {
           for (BillMorphia bill: mongoStore.createQuery(BillMorphia.class).fetch()) {
               if(bill.getVoteId() != null) {

                   Query<VoteMorphia> voteQuery = mongoStore.find(VoteMorphia.class,"voteId",bill.getVoteId());
                   System.out.println("Processing " + bill.getVoteId());
                   Key<BillMorphia> billKey = mongoStore.getKey(bill);
                   VoteMorphia vote = voteQuery.get();
                   try {
                       Key<VoteMorphia> voteMorphiaKey = mongoStore.getKey(vote);
                       if (vote != null && vote.getBill() == null) {
                           mongoStore.update(
                                   voteMorphiaKey,
                                   mongoStore.createUpdateOperations(VoteMorphia.class).add("bill",bill));
                           System.out.println("worky worky?");
                       }
                   }
                   catch (Exception e){}
               }
           }
       } finally {
           System.out.println("AHHH");
       }




//
//       final int pageLimit = 20;
//       int pageNumber = 0;
//       Page<com.poliana.bills.models.Bill> page = billCRUDRepo.findAll(new PageRequest(pageNumber, pageLimit));
//       while (page.hasNextPage()) {
//           createVoteToBillLink(page.getContent());
//           page = billCRUDRepo.findAll(new PageRequest(++pageNumber, pageLimit));
//       }
//       // process last page
//       createVoteToBillLink(page.getContent());
   }

   public void createVoteToBillLink(List<com.poliana.bills.models.Bill> bills) {
       List<Vote> votes = new LinkedList<>();
       for (com.poliana.bills.models.Bill bill : bills) {
           if(bill.getVoteId() != null) {
               Vote vote = votesCRUDRepo.findByVoteId(bill.getVoteId());
               if (vote != null && vote.getBill() == null) {
                   System.out.println("Processing " + bill.getVoteId());
                   vote.setBill(bill);
                   mongoTemplate.save(vote);
                   votes.add(vote);
               }
           }
       }
   }

   public List<Legislator> bioguideToLegislator(List<String> bioguideIds, int timeStamp) {

       List<Legislator> legislators = new LinkedList<>();

       try{
           for (String bioguideId: bioguideIds) {
               Legislator legislator =
                       legislatorService.legislatorByIdTimestamp(bioguideId, timeStamp);
               legislators.add(legislator);
           }
       }
       catch (NullPointerException e) {}

       return legislators;
   }

   public List<Legislator> gtVoterToLegislator(List<Voter> votersGt, int timeStamp) {
       List<Legislator> legislators = new LinkedList<>();

        try {
           for (Voter voter : votersGt) {
               Legislator legislator =
                       legislatorService.legislatorByIdTimestamp(voter.getPoliticianId(), timeStamp);
               legislators.add(legislator);
           }
        }
        catch (NullPointerException e) {}

       return legislators;
   }

    public int getTimestamp(String dateString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            Date date = formatter.parse(dateString);
            long time = date.getTime();
            return (int) (time/1000L);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
