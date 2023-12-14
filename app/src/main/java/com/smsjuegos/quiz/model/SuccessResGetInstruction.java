package com.smsjuegos.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SuccessResGetInstruction implements Serializable {

    @SerializedName("result")
    @Expose
    public ArrayList<Result> result = null;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("event_instructions")
    @Expose
    public String event_instructions;

    public String getEvent_instructions() {
        return event_instructions;
    }

    public void setEvent_instructions(String event_instructions) {
        this.event_instructions = event_instructions;
    }

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

    public static class Result implements Serializable {

        @SerializedName("custom_ans")
        @Expose
        public String custom_ans;
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("arrival_time")
        @Expose
        public String arrival_time;
        @SerializedName("event_id")
        @Expose
        public String eventId;
        @SerializedName("instructions")
        @Expose
        public String instructions;
        @SerializedName("instructions_hint1")
        @Expose
        public String instructionsHint1;
        @SerializedName("instructions_hint2")
        @Expose
        public String instructionsHint2;
        @SerializedName("instructions_hint3")
        @Expose
        public String instructionsHint3;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("lat")
        @Expose
        public String lat;
        @SerializedName("lon")
        @Expose
        public String lon;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("option_A")
        @Expose
        public String optionA;
        @SerializedName("option_B")
        @Expose
        public String optionB;
        @SerializedName("option_C")
        @Expose
        public String optionC;
        @SerializedName("option_D")
        @Expose
        public String optionD;
        @SerializedName("option_Ans")
        @Expose
        public String optionAns;
        @SerializedName("event_ans")
        @Expose
        public String eventAns;
        @SerializedName("final_puzzle_image")
        @Expose
        public String finalPuzzleImage;
        @SerializedName("event_code")
        @Expose
        public String eventCode;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("final_puzzle_status")
        @Expose
        public String finalPuzzleStatus;
        @SerializedName("event_type")
        @Expose
        public String eventType;
        @SerializedName("answer_status")
        @Expose
        public String answer_status;
        @SerializedName("timer")
        @Expose
        public String timer;
        @SerializedName("geolocation")
        @Expose
        public String geolocation;

        public String getGeolocation() {
            return geolocation;
        }

        public void setGeolocation(String geolocation) {
            this.geolocation = geolocation;
        }

        public String getCustom_ans() {
            return custom_ans;
        }

        public void setCustom_ans(String custom_ans) {
            this.custom_ans = custom_ans;
        }

        public String getArrival_time() {
            return arrival_time;
        }

        public void setArrival_time(String arrival_time) {
            this.arrival_time = arrival_time;
        }

        public String getTimer() {
            return timer;
        }

        public void setTimer(String timer) {
            this.timer = timer;
        }

        public String getAnswer_status() {
            return answer_status;
        }

        public void setAnswer_status(String answer_status) {
            this.answer_status = answer_status;
        }

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

        public String getInstructions() {
            return instructions;
        }

        public void setInstructions(String instructions) {
            this.instructions = instructions;
        }

        public String getInstructionsHint1() {
            return instructionsHint1;
        }

        public void setInstructionsHint1(String instructionsHint1) {
            this.instructionsHint1 = instructionsHint1;
        }

        public String getInstructionsHint2() {
            return instructionsHint2;
        }

        public void setInstructionsHint2(String instructionsHint2) {
            this.instructionsHint2 = instructionsHint2;
        }

        public String getInstructionsHint3() {
            return instructionsHint3;
        }

        public void setInstructionsHint3(String instructionsHint3) {
            this.instructionsHint3 = instructionsHint3;
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

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getOptionA() {
            return optionA;
        }

        public void setOptionA(String optionA) {
            this.optionA = optionA;
        }

        public String getOptionB() {
            return optionB;
        }

        public void setOptionB(String optionB) {
            this.optionB = optionB;
        }

        public String getOptionC() {
            return optionC;
        }

        public void setOptionC(String optionC) {
            this.optionC = optionC;
        }

        public String getOptionD() {
            return optionD;
        }

        public void setOptionD(String optionD) {
            this.optionD = optionD;
        }

        public String getOptionAns() {
            return optionAns;
        }

        public void setOptionAns(String optionAns) {
            this.optionAns = optionAns;
        }

        public String getEventAns() {
            return eventAns;
        }

        public void setEventAns(String eventAns) {
            this.eventAns = eventAns;
        }

        public String getFinalPuzzleImage() {
            return finalPuzzleImage;
        }

        public void setFinalPuzzleImage(String finalPuzzleImage) {
            this.finalPuzzleImage = finalPuzzleImage;
        }

        public String getEventCode() {
            return eventCode;
        }

        public void setEventCode(String eventCode) {
            this.eventCode = eventCode;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFinalPuzzleStatus() {
            return finalPuzzleStatus;
        }

        public void setFinalPuzzleStatus(String finalPuzzleStatus) {
            this.finalPuzzleStatus = finalPuzzleStatus;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "id='" + id + '\'' +
                    ", eventId='" + eventId + '\'' +
                    ", instructions='" + instructions + '\'' +
                    ", instructionsHint1='" + instructionsHint1 + '\'' +
                    ", instructionsHint2='" + instructionsHint2 + '\'' +
                    ", instructionsHint3='" + instructionsHint3 + '\'' +
                    ", address='" + address + '\'' +
                    ", lat='" + lat + '\'' +
                    ", lon='" + lon + '\'' +
                    ", dateTime='" + dateTime + '\'' +
                    ", image='" + image + '\'' +
                    ", optionA='" + optionA + '\'' +
                    ", optionB='" + optionB + '\'' +
                    ", optionC='" + optionC + '\'' +
                    ", optionD='" + optionD + '\'' +
                    ", optionAns='" + optionAns + '\'' +
                    ", eventAns='" + eventAns + '\'' +
                    ", finalPuzzleImage='" + finalPuzzleImage + '\'' +
                    ", eventCode='" + eventCode + '\'' +
                    ", type='" + type + '\'' +
                    ", finalPuzzleStatus='" + finalPuzzleStatus + '\'' +
                    ", eventType='" + eventType + '\'' +
                    ", answer_status='" + answer_status + '\'' +
                    ", timer='" + timer + '\'' +
                    '}';
        }
    }

}