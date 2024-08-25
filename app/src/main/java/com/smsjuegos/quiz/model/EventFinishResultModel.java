package com.smsjuegos.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class EventFinishResultModel implements Serializable {

    @SerializedName("result")
    @Expose
    private ArrayList<Result> result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public ArrayList<Result> getResult() {
        return result;
    }

    public void setResult(ArrayList<Result> result) {
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

    public class Result   implements Serializable{
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("event_id")
        @Expose
        private String eventId;
        @SerializedName("event_status")
        @Expose
        private String eventStatus;
        @SerializedName("event_start_time")
        @Expose
        private String eventStartTime;
        @SerializedName("event_end_time")
        @Expose
        private String eventEndTime;
        @SerializedName("event_code")
        @Expose
        private String eventCode;
        @SerializedName("team_name")
        @Expose
        private String teamName;
        @SerializedName("level")
        @Expose
        private String level;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("event_total_time")
        @Expose
        private String eventTotalTime;
        @SerializedName("penalty_time")
        @Expose
        private String penaltyTime;
        @SerializedName("total_ticket")
        @Expose
        private String totalTicket;
        @SerializedName("event_name")
        @Expose
        private String eventName;
        @SerializedName("image")
        @Expose
        private String image;

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

        public String getEventEndTime() {
            return eventEndTime;
        }

        public void setEventEndTime(String eventEndTime) {
            this.eventEndTime = eventEndTime;
        }

        public String getEventCode() {
            return eventCode;
        }

        public void setEventCode(String eventCode) {
            this.eventCode = eventCode;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEventTotalTime() {
            return eventTotalTime;
        }

        public void setEventTotalTime(String eventTotalTime) {
            this.eventTotalTime = eventTotalTime;
        }

        public String getPenaltyTime() {
            return penaltyTime;
        }

        public void setPenaltyTime(String penaltyTime) {
            this.penaltyTime = penaltyTime;
        }

        public String getTotalTicket() {
            return totalTicket;
        }

        public void setTotalTicket(String totalTicket) {
            this.totalTicket = totalTicket;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

}
