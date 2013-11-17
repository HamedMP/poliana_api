package com.poliana.bills.models;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;
import com.poliana.bills.entities.VoteGT.AmendmentRef;
import com.poliana.bills.entities.VoteGT.BillRef;
import com.poliana.bills.entities.VoteGT.Nomination;
import com.poliana.entities.models.LegislatorMorphia;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * @author David Gilmore
 * @date 11/13/13
 */
@Entity("votes")
public class VoteMorphia {

    @Id
    private String id;

    @Reference
    private BillMorphia bill;
    @Indexed @Property("vote_id")
    private String voteId;
    @Indexed @Property("bill_id")
    private String billId;
    private String category;
    private String congress;
    private int date;
    @Property("bill_info")
    private BillRef billInfo;
    private AmendmentRef amendment;
    private Nomination nomination;
    private String number;
    private String question;
    private String requires;
    private String result;
    @Property("result_text")
    private String resultText;
    private String session;
    @Property("source_url")
    private String sourceUrl;
    private String subject;
    private String type;
    @Property("updated_at")
    private String updatedAt;
    private String chamber;
    private int year;
    private int month;
    @Property("yea_total")
    private int yeaTotal;
    @Property("nay_total")
    private int nayTotal;
    @Property("not_voting_total")
    private int notVotingTotal;
    @Property("present_total")
    private int presentTotal;

    @Reference
    private List<LegislatorMorphia> yeas;
    @Reference
    private List<LegislatorMorphia> nays;
    @Reference @Property("not_voting")
    private List<LegislatorMorphia> notVoting;
    @Reference
    private List<LegislatorMorphia> present;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BillMorphia getBill() {
        return bill;
    }

    public void setBill(BillMorphia bill) {
        this.bill = bill;
    }

    public String getVoteId() {
        return voteId;
    }

    public void setVoteId(String voteId) {
        this.voteId = voteId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCongress() {
        return congress;
    }

    public void setCongress(String congress) {
        this.congress = congress;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public BillRef getBillInfo() {
        return billInfo;
    }

    public void setBillInfo(BillRef billInfo) {
        this.billInfo = billInfo;
    }

    public AmendmentRef getAmendment() {
        return amendment;
    }

    public void setAmendment(AmendmentRef amendment) {
        this.amendment = amendment;
    }

    public Nomination getNomination() {
        return nomination;
    }

    public void setNomination(Nomination nomination) {
        this.nomination = nomination;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getRequires() {
        return requires;
    }

    public void setRequires(String requires) {
        this.requires = requires;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getChamber() {
        return chamber;
    }

    public void setChamber(String chamber) {
        this.chamber = chamber;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYeaTotal() {
        return yeaTotal;
    }

    public void setYeaTotal(int yeaTotal) {
        this.yeaTotal = yeaTotal;
    }

    public int getNayTotal() {
        return nayTotal;
    }

    public void setNayTotal(int nayTotal) {
        this.nayTotal = nayTotal;
    }

    public int getNotVotingTotal() {
        return notVotingTotal;
    }

    public void setNotVotingTotal(int notVotingTotal) {
        this.notVotingTotal = notVotingTotal;
    }

    public int getPresentTotal() {
        return presentTotal;
    }

    public void setPresentTotal(int presentTotal) {
        this.presentTotal = presentTotal;
    }

    public List<LegislatorMorphia> getYeas() {
        return yeas;
    }

    public void setYeas(List<LegislatorMorphia> yeas) {
        this.yeas = yeas;
    }

    public List<LegislatorMorphia> getNays() {
        return nays;
    }

    public void setNays(List<LegislatorMorphia> nays) {
        this.nays = nays;
    }

    public List<LegislatorMorphia> getNotVoting() {
        return notVoting;
    }

    public void setNotVoting(List<LegislatorMorphia> notVoting) {
        this.notVoting = notVoting;
    }

    public List<LegislatorMorphia> getPresent() {
        return present;
    }

    public void setPresent(List<LegislatorMorphia> present) {
        this.present = present;
    }
}
