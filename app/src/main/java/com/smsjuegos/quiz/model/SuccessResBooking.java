package com.smsjuegos.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SuccessResBooking implements Serializable {

    @SerializedName("result")
    @Expose
    public Result result;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("amount")
        @Expose
        public String amount;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("req_datetime")
        @Expose
        public String reqDatetime;
        @SerializedName("timezone")
        @Expose
        public String timezone;
        @SerializedName("book_date")
        @Expose
        public String bookDate;
        @SerializedName("book_time")
        @Expose
        public String bookTime;
        @SerializedName("apply_code")
        @Expose
        public String applyCode;
        @SerializedName("payment_type")
        @Expose
        public String paymentType;
        @SerializedName("notification_status")
        @Expose
        public String notificationStatus;
        @SerializedName("event_id")
        @Expose
        public String eventId;
        @SerializedName("total_ticket")
        @Expose
        public String totalTicket;
        @SerializedName("cart_id")
        @Expose
        public String cartId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReqDatetime() {
            return reqDatetime;
        }

        public void setReqDatetime(String reqDatetime) {
            this.reqDatetime = reqDatetime;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        public String getBookDate() {
            return bookDate;
        }

        public void setBookDate(String bookDate) {
            this.bookDate = bookDate;
        }

        public String getBookTime() {
            return bookTime;
        }

        public void setBookTime(String bookTime) {
            this.bookTime = bookTime;
        }

        public String getApplyCode() {
            return applyCode;
        }

        public void setApplyCode(String applyCode) {
            this.applyCode = applyCode;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getNotificationStatus() {
            return notificationStatus;
        }

        public void setNotificationStatus(String notificationStatus) {
            this.notificationStatus = notificationStatus;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public String getTotalTicket() {
            return totalTicket;
        }

        public void setTotalTicket(String totalTicket) {
            this.totalTicket = totalTicket;
        }

        public String getCartId() {
            return cartId;
        }

        public void setCartId(String cartId) {
            this.cartId = cartId;
        }

    }
    
}