package com.smsjuegos.quiz.model;



import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FinalPuzzerlImageModel {

    @SerializedName("result")
    @Expose
    private List<Result> result;
    @SerializedName("final_answer")
    @Expose
    private String finalAnswer;
    @SerializedName("after_finish_text")
    @Expose
    private String afterFinishText;
    @SerializedName("after_finish_image")
    @Expose
    private String afterFinishImage;
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

    public String getFinalAnswer() {
        return finalAnswer;
    }

    public void setFinalAnswer(String finalAnswer) {
        this.finalAnswer = finalAnswer;
    }

    public String getAfterFinishText() {
        return afterFinishText;
    }

    public void setAfterFinishText(String afterFinishText) {
        this.afterFinishText = afterFinishText;
    }

    public String getAfterFinishImage() {
        return afterFinishImage;
    }

    public void setAfterFinishImage(String afterFinishImage) {
        this.afterFinishImage = afterFinishImage;
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
        @SerializedName("Jigsaw_puzzle_image")
        @Expose
        private String jigsawPuzzleImage;
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

        public String getJigsawPuzzleImage() {
            return jigsawPuzzleImage;
        }

        public void setJigsawPuzzleImage(String jigsawPuzzleImage) {
            this.jigsawPuzzleImage = jigsawPuzzleImage;
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


