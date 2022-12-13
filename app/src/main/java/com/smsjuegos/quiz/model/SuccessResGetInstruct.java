package com.smsjuegos.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SuccessResGetInstruct implements Serializable {

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

    public class Result implements Serializable {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("content")
        @Expose
        public String content;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("content_sp")
        @Expose
        public String contentSp;
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }
        public String getDateTime() {
            return dateTime;
        }
        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }
        public String getContentSp() {
            return contentSp;
        }
        public void setContentSp(String contentSp) {
            this.contentSp = contentSp;
        }
    }
}