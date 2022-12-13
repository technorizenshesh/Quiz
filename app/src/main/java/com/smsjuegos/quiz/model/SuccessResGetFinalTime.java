package com.smsjuegos.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuccessResGetFinalTime implements Serializable {

    @SerializedName("result")
    @Expose
    public List<Result> result = null;
    @SerializedName("event_total_time")
    @Expose
    public String eventTotalTime;
    @SerializedName("penalty_time")
    @Expose
    public Integer penaltyTime;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("team_name")
    @Expose
    public String teamName;
    @SerializedName("event_details")
    @Expose
    public EventDetails eventDetails;
    @SerializedName("total_ticket")
    @Expose
    public String totalTicket;
    @SerializedName("message")
    @Expose
    public String message;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getEventTotalTime() {
        return eventTotalTime;
    }

    public void setEventTotalTime(String eventTotalTime) {
        this.eventTotalTime = eventTotalTime;
    }

    public Integer getPenaltyTime() {
        return penaltyTime;
    }

    public void setPenaltyTime(Integer penaltyTime) {
        this.penaltyTime = penaltyTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public EventDetails getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(EventDetails eventDetails) {
        this.eventDetails = eventDetails;
    }

    public String getTotalTicket() {
        return totalTicket;
    }

    public void setTotalTicket(String totalTicket) {
        this.totalTicket = totalTicket;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public class EventDetails {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("event_name")
        @Expose
        public String eventName;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("lat")
        @Expose
        public String lat;
        @SerializedName("lon")
        @Expose
        public String lon;
        @SerializedName("event_date")
        @Expose
        public String eventDate;
        @SerializedName("event_time")
        @Expose
        public String eventTime;
        @SerializedName("amount")
        @Expose
        public String amount;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("total_ticket")
        @Expose
        public String totalTicket;
        @SerializedName("event_instructions")
        @Expose
        public String eventInstructions;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("event_status")
        @Expose
        public String eventStatus;
        @SerializedName("event_start_time")
        @Expose
        public String eventStartTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getEventDate() {
            return eventDate;
        }

        public void setEventDate(String eventDate) {
            this.eventDate = eventDate;
        }

        public String getEventTime() {
            return eventTime;
        }

        public void setEventTime(String eventTime) {
            this.eventTime = eventTime;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTotalTicket() {
            return totalTicket;
        }

        public void setTotalTicket(String totalTicket) {
            this.totalTicket = totalTicket;
        }

        public String getEventInstructions() {
            return eventInstructions;
        }

        public void setEventInstructions(String eventInstructions) {
            this.eventInstructions = eventInstructions;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getEventStatus() {
            return eventStatus;
        }

        public void setEventStatus(String eventStatus) {
            this.eventStatus = eventStatus;
        }

        public String getEventStartTime() {
            return eventStartTime;
        }

        public void setEventStartTime(String eventStartTime) {
            this.eventStartTime = eventStartTime;
        }

    }


    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("event_id")
        @Expose
        public String eventId;
        @SerializedName("event_instructions_id")
        @Expose
        public String eventInstructionsId;
        @SerializedName("time")
        @Expose
        public String time;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("event_code")
        @Expose
        public String eventCode;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public String getEventInstructionsId() {
            return eventInstructionsId;
        }

        public void setEventInstructionsId(String eventInstructionsId) {
            this.eventInstructionsId = eventInstructionsId;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getEventCode() {
            return eventCode;
        }

        public void setEventCode(String eventCode) {
            this.eventCode = eventCode;
        }

    }


}