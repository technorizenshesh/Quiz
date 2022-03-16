package com.my.quiz.retrofit;

import com.my.quiz.model.SuccessResForgetPassword;
import com.my.quiz.model.SuccessResGetProfile;
import com.my.quiz.model.SuccessResLogin;
import com.my.quiz.model.SuccessResSignup;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface QuizInterface {

    @FormUrlEncoded
    @POST("signup")
    Call<SuccessResSignup> signup(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("login")
    Call<SuccessResLogin> login(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("forgot_password")
    Call<SuccessResForgetPassword> forgotPassword(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_profile")
    Call<SuccessResGetProfile> getProfile(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("change_password")
    Call<SuccessResForgetPassword> changePass(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("update_profile")
    Call<SuccessResGetProfile> updateProfile(@FieldMap Map<String, String> paramHashMap);


}
