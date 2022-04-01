package com.my.quiz.retrofit;

import com.my.quiz.model.SuccessResAddToCart;
import com.my.quiz.model.SuccessResDeleteCart;
import com.my.quiz.model.SuccessResForgetPassword;
import com.my.quiz.model.SuccessResGetBanner;
import com.my.quiz.model.SuccessResGetCart;
import com.my.quiz.model.SuccessResGetEventDetail;
import com.my.quiz.model.SuccessResGetEvents;
import com.my.quiz.model.SuccessResGetMyEvents;
import com.my.quiz.model.SuccessResGetProfile;
import com.my.quiz.model.SuccessResLogin;
import com.my.quiz.model.SuccessResNearbyEvents;
import com.my.quiz.model.SuccessResSignup;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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

    @GET("get_banner")
    Call<SuccessResGetBanner> getBanners();

    @GET("get_event")
    Call<SuccessResGetEvents> getEventsList();

    @FormUrlEncoded
    @POST("get_event_details")
    Call<SuccessResGetEventDetail> getEventDetails(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("add_to_cart")
    Call<ResponseBody> addToCart(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_cart")
    Call<SuccessResGetCart> getCart(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_neareast_event")
    Call<SuccessResNearbyEvents> getNearbyEvents(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("delete_cart_data")
    Call<ResponseBody> deleteCart(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("stripe_payment")
    Call<ResponseBody> stipePayment(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("booking_request")
    Call<ResponseBody> booking(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST("event_apply_code")
    Call<ResponseBody> applyCode(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST("get_my_event")
    Call<SuccessResGetMyEvents> getMyEvent(@FieldMap Map<String, String> paramHashMap);


}
