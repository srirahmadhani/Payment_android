package com.example.payment.model;

public class DataHistory {
    private String paymentDate;
    private String ticketName;
    private String qty;
    private String total;

    public DataHistory(String paymentDate, String ticketName, String qty, String total) {
        this.paymentDate = paymentDate;
        this.ticketName = ticketName;
        this.qty = qty;
        this.total = total;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
