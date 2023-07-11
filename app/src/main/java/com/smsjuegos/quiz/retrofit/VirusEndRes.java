package com.smsjuegos.quiz.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VirusEndRes  implements Serializable {
        @SerializedName("result")
        @Expose
        private Result result;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("status")
        @Expose
        private String status;

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

    public class Result  implements   Serializable{

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
        @SerializedName("event_code")
        @Expose
        private String eventCode;
        @SerializedName("event_end_time")
        @Expose
        private String eventEndTime;
        @SerializedName("team_name")
        @Expose
        private String teamName;
        @SerializedName("end_video")
        @Expose
        private String endVideo;
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

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public String getEndVideo() {
            return endVideo;
        }

        public void setEndVideo(String endVideo) {
            this.endVideo = endVideo;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }


}
