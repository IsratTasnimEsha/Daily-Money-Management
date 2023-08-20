package com.example.manageit;

public class UserSpendings {
    String reason, amount, time, updated_time;

    public UserSpendings() {
    }

    public UserSpendings(String reason, String amount, String time, String updated_time) {
        this.reason = reason;
        this.amount = amount;
        this.time = time;
        this.updated_time = updated_time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(String updated_time) {
        this.updated_time = updated_time;
    }
}
