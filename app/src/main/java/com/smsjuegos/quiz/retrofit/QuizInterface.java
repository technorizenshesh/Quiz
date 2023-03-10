package com.smsjuegos.quiz.retrofit;

import com.smsjuegos.quiz.model.EventCodeResSuccess;
import com.smsjuegos.quiz.model.SuccessResForgetPassword;
import com.smsjuegos.quiz.model.SuccessResGetBanner;
import com.smsjuegos.quiz.model.SuccessResGetCart;
import com.smsjuegos.quiz.model.SuccessResGetEventDetail;
import com.smsjuegos.quiz.model.SuccessResGetEvents;
import com.smsjuegos.quiz.model.SuccessResGetFinalTime;
import com.smsjuegos.quiz.model.SuccessResGetInstruct;
import com.smsjuegos.quiz.model.SuccessResGetInstruction;
import com.smsjuegos.quiz.model.SuccessResGetInventory;
import com.smsjuegos.quiz.model.SuccessResGetMyEvents;
import com.smsjuegos.quiz.model.SuccessResGetOtherUserData;
import com.smsjuegos.quiz.model.SuccessResGetPP;
import com.smsjuegos.quiz.model.SuccessResGetProfile;
import com.smsjuegos.quiz.model.SuccessResGetVirusEvent;
import com.smsjuegos.quiz.model.SuccessResLogin;
import com.smsjuegos.quiz.model.SuccessResNearbyEvents;
import com.smsjuegos.quiz.model.SuccessResSignup;
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

    @FormUrlEncoded
    @POST("get_event")
    Call<SuccessResGetEvents> getEventsList(@FieldMap Map<String, String> paramHashMap);

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

    @FormUrlEncoded
    @POST("get_intro")
    Call<SuccessResGetInstruct> geApplicationInstruction(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_event_instructions_game")
    Call<SuccessResGetInstruction> getInstruction(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("event_start_time")
    Call<ResponseBody> addStartTime(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("add_hint")
    Call<ResponseBody> addPanalties(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("event_instructions_game_ans")
    Call<ResponseBody> submitAnswer(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("event_instructions_game4_ans")
    Call<ResponseBody> submitCrimeAnswer(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_inventory_event")
    Call<SuccessResGetInventory> getInventory(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_inventory_event_game4")
    Call<SuccessResGetInventory> getGame4Inventory(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_all_inventory_event")
    Call<SuccessResGetInventory> getAllInventory(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("event_end_time")
    Call<ResponseBody> puzzelCompleted(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_event_finish_time")
    Call<SuccessResGetFinalTime> myPuzzelCompletedTime(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_event_finish_time_game4")
    Call<SuccessResGetFinalTime> myGame4CompletedTime(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_all_event_finish_time")
    Call<SuccessResGetOtherUserData> otherPuzzelCompletedTime(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_my_event")
    Call<SuccessResGetMyEvents> getMyFinishEvent(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("add_contact_us")
    Call<ResponseBody> addContactUs(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("search_product")
    Call<SuccessResGetEvents> searchEvent(@FieldMap Map<String, String> paramHashMap);

//    @FormUrlEncoded
//    @POST("get_virus_event_answer")
//    Call<SuccessResGetVirusEvent> getVirusEvent(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_virus_event_answer_new")
    Call<SuccessResGetVirusEvent> getVirusEvent(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("virus_event_start_time")
    Call<ResponseBody> addVirusStartTime(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("add_virus_hint")
    Call<ResponseBody> addVirusPanalties(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("add_virus_event_ans")
    Call<ResponseBody> submitVirusAnswer(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("virus_event_end_time")
    Call<ResponseBody> addVirusEndTime(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("add_hint_game4")
    Call<ResponseBody> addPanaltiesGame4(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("event_start_time_game4")
    Call<ResponseBody> addCrimeEventStartTime(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_all_inventory_event_game4")
    Call<SuccessResGetInventory> getGame4AllInventory(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("event_end_time_game4")
    Call<ResponseBody> Game4Completed(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("virus_event_apply_code")
    Call<ResponseBody> virus_event_apply_code(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("get_event_code")
    Call<EventCodeResSuccess> get_event_code(@FieldMap Map<String, String> paramHashMap);
    @FormUrlEncoded
    @POST("get_term_conditions")
    Call<SuccessResGetPP> getPrivacyPolicy(@FieldMap Map<String, String> paramHashMap);
}
