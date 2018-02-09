package in.classguru.classguru.models;

/**
 * Created by a2z on 2/9/2018.
 */

public class FacSalaryModel {
    String Amt;
    String PayMode;
    String PayDate;
    String ChqNo;
    String ChqDate;
    String BankNo;

    public String getAmt() {
        return Amt;
    }

    public void setAmt(String amt) {
        Amt = amt;
    }

    public String getPayMode() {
        return PayMode;
    }

    public void setPayMode(String payMode) {
        PayMode = payMode;
    }

    public String getPayDate() {
        return PayDate;
    }

    public void setPayDate(String payDate) {
        PayDate = payDate;
    }

    public String getChqNo() {
        return ChqNo;
    }

    public void setChqNo(String chqNo) {
        ChqNo = chqNo;
    }

    public String getChqDate() {
        return ChqDate;
    }

    public void setChqDate(String chqDate) {
        ChqDate = chqDate;
    }

    public String getBankNo() {
        return BankNo;
    }

    public void setBankNo(String bankNo) {
        BankNo = bankNo;
    }

    public String getTrancID() {
        return TrancID;
    }

    public void setTrancID(String trancID) {
        TrancID = trancID;
    }

    String TrancID;
}
