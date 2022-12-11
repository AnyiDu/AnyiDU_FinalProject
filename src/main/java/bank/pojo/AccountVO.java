package bank.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class AccountVO {

    private int account_no;
    private String account_name;
    private String account_password;
    private BigDecimal account_money;
    private String account_status;
    private String account_modify_time;

    private BigDecimal before_money;
    private BigDecimal after_money;
    private BigDecimal change_money;
    private String change_type;

    public String getChange_type() {
        return change_type;
    }

    public void setChange_type(String change_type) {
        this.change_type = change_type;
    }

    public BigDecimal getBefore_money() {
        return before_money;
    }

    public void setBefore_money(BigDecimal before_money) {
        this.before_money = before_money;
    }

    public BigDecimal getAfter_money() {
        return after_money;
    }

    public void setAfter_money(BigDecimal after_money) {
        this.after_money = after_money;
    }

    public BigDecimal getChange_money() {
        return change_money;
    }

    public void setChange_money(BigDecimal change_money) {
        this.change_money = change_money;
    }

    public int getAccount_no() {
        return account_no;
    }

    public void setAccount_no(int account_no) {
        this.account_no = account_no;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_password() {
        return account_password;
    }

    public void setAccount_password(String account_password) {
        this.account_password = account_password;
    }

    public BigDecimal getAccount_money() {
        return account_money;
    }

    public void setAccount_money(BigDecimal account_money) {
        this.account_money = account_money;
    }

    public String getAccount_status() {
        return account_status;
    }

    public void setAccount_status(String account_status) {
        this.account_status = account_status;
    }

    public String getAccount_modify_time() {
        return account_modify_time;
    }

    public void setAccount_modify_time(String account_modify_time) {
        this.account_modify_time = account_modify_time;
    }
}
