package com.smsjuegos.quiz.activities.cardigo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class PuzzleList implements Serializable {
    @SerializedName("result")
    @Expose
    private ArrayList<Result> result;
    @SerializedName("event_instructions")
    @Expose
    private String eventInstructions;
    @SerializedName("final_answer")
    @Expose
    private String final_answer;
    @SerializedName("after_finish_text")
    @Expose
    private String after_finish_text;
    @SerializedName("after_finish_image")
    @Expose
    private String after_finish_image;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public String getAfter_finish_text() {
        return after_finish_text;
    }

    public void setAfter_finish_text(String after_finish_text) {
        this.after_finish_text = after_finish_text;
    }

    public String getAfter_finish_image() {
        return after_finish_image;
    }

    public void setAfter_finish_image(String after_finish_image) {
        this.after_finish_image = after_finish_image;
    }

    public String getFinal_answer() {
        return final_answer;
    }

    public void setFinal_answer(String final_answer) {
        this.final_answer = final_answer;
    }

    public ArrayList<Result> getResult() {
        return result;
    }

    public void setResult(ArrayList<Result> result) {
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

    public static class Result implements Serializable {
        @SerializedName("answer_status")
        @Expose
        private Integer answerStatus;
        @SerializedName("final_puzzle_image")
        @Expose
        private String finalPuzzleImage;
        @SerializedName("image")
        @Expose
        private String image;

        public Integer getAnswerStatus() {
            return answerStatus;
        }

        public void setAnswerStatus(Integer answerStatus) {
            this.answerStatus = answerStatus;
        }

        public String getFinalPuzzleImage() {
            return finalPuzzleImage;
        }

        public void setFinalPuzzleImage(String finalPuzzleImage) {
            this.finalPuzzleImage = finalPuzzleImage;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
