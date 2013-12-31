package com.poliana.core.ideology;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Transient;
import com.poliana.core.legislators.Legislator;
import org.apache.commons.math3.linear.SingularValueDecomposition;

import java.util.HashMap;
import java.util.List;

/**
 * @author David Gilmore
 * @date 12/6/13
 */
@Entity("ideology_analysis")
public class IdeologyMatrix {

    private String chamber;
    @Property("begin_timestamp")
    private int beginTimestamp;
    @Property("end_timestamp")
    private int endTimestamp;
    private double[][] sponsorshipMatrix;
    private double[][] u;
    private double[][] vt;
    @Transient
    private SingularValueDecomposition svd;
    private HashMap<String,Integer> idToIndex;
    @Embedded
    private List<Legislator> legislators;

    public String getChamber() {
        return chamber;
    }

    public void setChamber(String chamber) {
        this.chamber = chamber;
    }

    public int getBeginTimestamp() {
        return beginTimestamp;
    }

    public void setBeginTimestamp(int beginTimestamp) {
        this.beginTimestamp = beginTimestamp;
    }

    public int getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(int endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public double[][] getSponsorshipMatrix() {
        return sponsorshipMatrix;
    }

    public void setSponsorshipMatrix(double[][] sponsorshipMatrix) {
        this.sponsorshipMatrix = sponsorshipMatrix;
    }

    public double[][] getU() {
        return u;
    }

    public void setU(double[][] u) {
        this.u = u;
    }

    public double[][] getVt() {
        return vt;
    }

    public void setVt(double[][] vt) {
        this.vt = vt;
    }

    public SingularValueDecomposition getSvd() {
        return svd;
    }

    public void setSvd(SingularValueDecomposition svd) {
        this.svd = svd;
    }

    public HashMap<String, Integer> getIdToIndex() {
        return idToIndex;
    }

    public void setIdToIndex(HashMap<String, Integer> idToIndex) {
        this.idToIndex = idToIndex;
    }

    public List<Legislator> getLegislators() {
        return legislators;
    }

    public void setLegislators(List<Legislator> legislators) {
        this.legislators = legislators;
    }
}
