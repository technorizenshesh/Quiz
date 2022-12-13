package com.smsjuegos.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuccessResGetOtherUserData implements Serializable {

    @SerializedName("result")
    @Expose
    public List<Result> result = null;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("event_id")
        @Expose
        public String eventId;
        @SerializedName("event_status")
        @Expose
        public String eventStatus;
        @SerializedName("event_start_time")
        @Expose
        public String eventStartTime;
        @SerializedName("event_code")
        @Expose
        public String eventCode;
        @SerializedName("event_end_time")
        @Expose
        public String eventEndTime;
        @SerializedName("event_total_time")
        @Expose
        public String eventTotalTime;
        @SerializedName("penalty_time")
        @Expose
        public Integer penaltyTime;
        @SerializedName("team_name")
        @Expose
        public String teamName;
        @SerializedName("total_ticket")
        @Expose
        public String totalTicket;

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

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
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

        public String getEventCode() {
            return eventCode;
        }

        public void setEventCode(String eventCode) {
            this.eventCode = eventCode;
        }

        public String getEventEndTime() {
            return eventEndTime;
        }

        public void setEventEndTime(String eventEndTime) {
            this.eventEndTime = eventEndTime;
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

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public String getTotalTicket() {
            return totalTicket;
        }

        public void setTotalTicket(String totalTicket) {
            this.totalTicket = totalTicket;
        }

    }
    
}