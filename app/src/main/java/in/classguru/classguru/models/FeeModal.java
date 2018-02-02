package in.classguru.classguru.models;

/**
 * Created by a2z on 2/2/2018.
 */

public class FeeModal {
    private String pay_mode;
    private String pay_date;
    private String chq_date;
    private String bank_name;
    private String chq_no;
    private String transc_id;
    private String received;
    private String paid_rec;

    public String getPay_mode() {
        return pay_mode;
    }

    public void setPay_mode(String pay_mode) {
        this.pay_mode = pay_mode;
    }

    public String getPay_date() {
        return pay_date;
    }

    public void setPay_date(String pay_date) {
        this.pay_date = pay_date;
    }

    public String getChq_date() {
        return chq_date;
    }

    public void setChq_date(String chq_date) {
        this.chq_date = chq_date;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getChq_no() {
        return chq_no;
    }

    public void setChq_no(String chq_no) {
        this.chq_no = chq_no;
    }

    public String getTransc_id() {
        return transc_id;
    }

    public void setTransc_id(String transc_id) {
        this.transc_id = transc_id;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    public String getPaid_rec() {
        return paid_rec;
    }

    public void setPaid_rec(String paid_rec) {
        this.paid_rec = paid_rec;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    private String balance;
}
