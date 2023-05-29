package com.smsjuegos.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuccessResGetInstructionTwo implements Serializable {
    @SerializedName("result")
    @Expose
    private List<Result> result;
    @SerializedName("event_instructions")
    @Expose
    private String eventInstructions;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getEventInstructions() {
        return eventInstructions;
    }

    public void setEventInstructions(String eventInstructions) {
        this.eventInstructions = eventInstructions;
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

        @SerializedName("answer_status")
        @Expose
        private Integer answerStatus;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("lon")
        @Expose
        private String lon;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("final_puzzle_image")
        @Expose
        private String final_puzzle_image;

        public String getFinal_puzzle_image() {
            return final_puzzle_image;
        }

        public void setFinal_puzzle_image(String final_puzzle_image) {
            this.final_puzzle_image = final_puzzle_image;
        }

        public Integer getAnswerStatus() {
            return answerStatus;
        }

        public void setAnswerStatus(Integer answerStatus) {
            this.answerStatus = answerStatus;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

    }
}