package com.pantaubersama.app.data.remote

import com.pantaubersama.app.data.model.AppVersionResponse
import com.pantaubersama.app.data.model.bannerinfo.BannerInfoResponse
import com.pantaubersama.app.data.model.bannerinfo.BannerInfosResponse
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitikResponse
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitiksResponse
import com.pantaubersama.app.data.model.kuis.* // ktlint-disable
import com.pantaubersama.app.data.model.linimasa.FeedsResponse
import com.pantaubersama.app.data.model.rekapitulasi.RekapitulasiResponse
import com.pantaubersama.app.data.model.rekapitulasi.TotalParticipantResponse
import com.pantaubersama.app.data.model.tanyakandidat.TanyaKandidatListResponse
import com.pantaubersama.app.data.model.tanyakandidat.TanyaKandidatResponse
import com.pantaubersama.app.data.model.tps.* // ktlint-disable
import com.pantaubersama.app.data.model.tps.c1.C1FormResponse
import com.pantaubersama.app.data.model.tps.candidate.CandidateResponse
import com.pantaubersama.app.data.model.tps.image.ImageResponse
import com.pantaubersama.app.data.model.tps.realcount.realcountoutput.RealCountResponse
import com.pantaubersama.app.data.model.wordstadium.LawanDebatResponse
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.* // ktlint-disable

/**
 * @author edityomurti on 14/12/2018 14:41
 */
interface PantauAPI {
    @GET("linimasa/v1/banner_infos")
    fun getBannerInfos(): Single<BannerInfosResponse>

    @GET("linimasa/v1/banner_infos/show")
    fun getBannerInfo(
        @Query("page_name") pageName: String?
    ): Single<BannerInfoResponse>

    @GET("linimasa/v1/feeds/pilpres")
    fun getFeeds(
        @Query("q") keyword: String,
        @Query("filter_by") filterBy: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<FeedsResponse>

    @GET("linimasa/v1/janji_politiks/{id}")
    fun getJanjiPolitik(@Path("id") janpolId: String): Single<JanjiPolitikResponse>

    @GET("linimasa/v1/janji_politiks")
    fun getJanjiPolitikList(
        @Query("q") keyword: String?,
        @Query("cluster_id") clusterId: String?,
        @Query("filter_by") filterBy: String?,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?
    ): Single<JanjiPolitiksResponse>

    @GET("linimasa/v1/janji_politiks/me")
    fun getMyJanjiPolitikList(
        @Query("q") keyword: String?,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?
    ): Single<JanjiPolitiksResponse>

    @Multipart
    @POST("linimasa/v1/janji_politiks")
    fun createJanjiPolitik(
        @Part("title") title: RequestBody,
        @Part("body") body: RequestBody,
        @Part image: MultipartBody.Part?
    ): Single<JanjiPolitikResponse>

    @DELETE("linimasa/v1/janji_politiks")
    fun deleteJanjiPolitik(
        @Query("id") id: String
    ): Completable

    @FormUrlEncoded
    @POST("pendidikan_politik/v1/questions")
    fun createTanyaKandidat(@Field("body") body: String?): Single<TanyaKandidatListResponse>

    @GET("pendidikan_politik/v1/questions")
    fun getTanyaKandidatList(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("order_by") orderBy: String?,
        @Query("direction") direction: String?,
        @Query("filter_by") filterBy: String?
    ): Single<TanyaKandidatListResponse>

    @GET("pendidikan_politik/v1/me/questions")
    fun getMyTanyaKandidatList(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<TanyaKandidatListResponse>

    @GET("pendidikan_politik/v1/questions/{id}")
    fun getTanyaKandidatById(@Path("id") questionId: String): Single<TanyaKandidatResponse>

    @FormUrlEncoded
    @POST("pendidikan_politik/v1/votes")
    fun upVoteQuestion(
        @Field("id") id: String?,
        @Field("class_name") className: String?
    ): Completable

    @FormUrlEncoded
    @POST("pendidikan_politik/v1/reports")
    fun reportQuestion(
        @Field("id") id: String?,
        @Field("class_name") className: String?
    ): Completable

    @DELETE("pendidikan_politik/v1/questions")
    fun deleteQuestion(
        @Query("id") id: String?
    ): Completable

    @DELETE("pendidikan_politik/v1/votes")
    fun unVoteQuestion(
        @Query("id") id: String?,
        @Query("class_name") className: String
    ): Completable

    @GET("pendidikan_politik/v1/quizzes/participated")
    fun getKuisInProgress(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("q") keyword: String,
        @Query("filter_by") filterBy: String = "in_progress"
    ): Single<KuisResponse>

    @GET("pendidikan_politik/v1/quizzes/participated")
    fun getKuisFinished(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("q") keyword: String,
        @Query("filter_by") filterBy: String = "finished"
    ): Single<KuisResponse>

    @GET("pendidikan_politik/v1/quizzes")
    fun getKuisNotParticipating(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("q") keyword: String
    ): Single<KuisResponse>

    @GET("pendidikan_politik/v1/quizzes/{id}")
    fun getKuisById(@Path("id") quizId: String): Single<KuisItemResponse>

    @GET("pendidikan_politik/v1/me/quizzes")
    fun getKuisUserResult(): Single<KuisUserResultResponse>

    @GET("pendidikan_politik//v1/quiz_participations/quizzes")
    fun getKuisUserResultByUserId(
        @Query("user_id") userId: String
    ): Single<KuisUserResultResponse>

    @GET("pendidikan_politik/v1/quizzes/{id}/questions")
    fun getKuisQuestions(@Path("id") kuisId: String): Single<KuisQuestionResponse>

    @FormUrlEncoded
    @POST("pendidikan_politik/v1/quizzes/{id}/questions")
    fun answerQuestion(
        @Path("id") kuisId: String,
        @Field("question_id") questionId: String,
        @Field("answer_id") answerId: String
    ): Completable

    @GET("pendidikan_politik/v1/quizzes/{id}/result")
    fun getKuisResult(@Path("id") kuisId: String): Single<KuisResultResponse>

    @GET("pendidikan_politik/v1/quiz_participations/{quiz_participation_id}/result")
    fun getKuisResultByQuizParticipationId(@Path("quiz_participation_id") quizParticipationId: String): Single<KuisResultResponse>

    @GET("pendidikan_politik/v1/quizzes/{id}/summary")
    fun getKuisSummary(@Path("id") kuisId: String): Single<KuisSummaryResponse>

    @GET("dashboard/v1/app_versions/last_version?app_type=android")
    fun getLatestAppVersion(): Single<AppVersionResponse>

    @GET("pendidikan_politik/v1/questions")
    fun searchTanyaKandidat(
        @Query("q") keyword: String,
        @Query("page") page: Int,
        @Query("per_page") perpage: Int,
        @Query("order_by") orderBy: String?,
        @Query("direction") direction: String?,
        @Query("filter_by") filterBy: String?
    ): Single<TanyaKandidatListResponse>

    @GET("pendidikan_politik/v1/users/{id}/questions")
    fun getUserTanyaKandidatList(
        @Path("id") userId: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<TanyaKandidatListResponse>

    @GET("linimasa/v1/janji_politiks/user/{id}")
    fun getUserJanpul(
        @Path("id") userId: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<JanjiPolitiksResponse>

    @GET("hitung/v1/provinces")
    fun getProvinces(): Single<LocationResponse<ProvinceData>>

    @GET("hitung/v1/regencies")
    fun getRegencies(
        @Query("province_code") provinceId: Int
    ): Single<LocationResponse<RegencyData>>

    @GET("hitung/v1/districts")
    fun getDistricts(
        @Query("regency_code") regencyId: Int
    ): Single<LocationResponse<DistrictData>>

    @GET("hitung/v1/villages")
    fun getVillages(
        @Query("district_code") districtId: Int
    ): Single<LocationResponse<VillageData>>

    @GET("hitung/v1/real_counts")
    fun getMyTPSes(
        @Query("user_id") userId: String?
    ): Single<TpsResponse>

    @DELETE("hitung/v1/real_counts/{id}")
    fun deleteTPS(
        @Path("id") id: String
    ): Completable

    @FormUrlEncoded
    @POST("hitung/v1/real_counts")
    fun publishTPS(
        @Field("tps") tpsNumber: Int,
        @Field("province_code") selectedProvince: Province,
        @Field("regency_code") selectedRegency: Regency,
        @Field("district_code") selectedDistrict: District,
        @Field("village_code") selectedVillage: Village,
        @Field("latitude") lat: Double,
        @Field("longitude") long: Double
    )

    @FormUrlEncoded
    @PUT("hitung/v1/real_counts/{id}")
    fun updateTPS(
        @Path("id") tpsId: String,
        @Field("tps") tpsNumber: Int
    ): Completable
    @GET("dashboard/v1/linimasa/suggest/username")
    fun getLawanDebatTwitter(
        @Query("q") q: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<LawanDebatResponse>

    @GET("hitung/v1/dapils/region")
    fun getDapil(
        @Query("province_code") provinceCode: Int,
        @Query("regency_code") regencyCode: Int,
        @Query("district_code") districtCode: Int,
        @Query("village_code") villageCode: Long? = null,
        @Query("tingkat") tingkat: String
    ): Single<DapilResponse>

    @GET("hitung/v1/candidates")
    fun getRealCountList(
        @Query("dapil_id") dapilId: Int,
        @Query("tingkat") tingkat: String
    ): Single<CandidateResponse>

    @FormUrlEncoded
    @POST("hitung/v1/real_counts")
    fun uploadTps(
        @Field("tps") tpsNumber: Int,
        @Field("province_code") provinceCode: Int,
        @Field("regency_code") regencyCode: Int,
        @Field("district_code") districtCode: Int,
        @Field("village_code") villageCode: Long? = null,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double
    ): Single<TpsResponse>

    @PUT("hitung/v1/calculations")
    fun uploadRealCount(@Body presidenBody: RequestBody): Completable

    @PUT("hitung/v1/form_c1")
    fun uploadC1Form(@Body c1Body: RequestBody): Completable

    @Multipart
    @POST("hitung/v1/images")
    fun uploadImage(
        @Part("hitung_real_count_id") apiTpsId: String,
        @Part("image_type") imagesUploadType: String,
        @Part image: MultipartBody.Part
    ): Completable

    @POST("hitung/v1/real_counts/{id}/publish")
    fun publishRealCount(
        @Path("id") apiTpsId: String
    ): Completable

    @GET("hitung/v1/form_c1")
    fun getC1(
        @Query("hitung_real_count_id") tpsId: String,
        @Query("form_c1_type") c1Type: String
    ): Single<C1FormResponse>

    @GET("hitung/v1/calculations")
    fun getRealCount(
        @Query("hitung_real_count_id") tpsId: String,
        @Query("calculation_type") realCountType: String
    ): Single<RealCountResponse>

    @GET("hitung/v1/summary/president/show")
    fun getRekapitulasiNasional(
        @Query("level") level: Int = 0
    ): Single<RekapitulasiResponse>

    @GET("hitung/v1/summary/contribution")
    fun getTotalParticipant(): Single<TotalParticipantResponse>

    @GET("hitung/v1/summary/president/list")
    fun getRekapitulasiList(
        @Query("level") level: Int? = 0,
        @Query("region") regencyCode: Long? = null
    ): Single<RekapitulasiResponse>

    @GET("hitung/v1/real_counts")
    fun getTPSes(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("village_code") villageCode: Long
    ): Single<TpsResponse>

    @GET("hitung/v1/summary/president/show")
    fun getRekapitulasiDetail(
        @Query("hitung_real_count_id") tpsId: String,
        @Query("region") villageCode: Long,
        @Query("tps") tpsNumber: Int,
        @Query("level") level: Int = 6
    ): Single<RekapitulasiResponse>

    @GET("hitung/v1/images")
    fun getImages(
        @Query("hitung_real_count_id") tpsId: String,
        @Query("image_type") imagesType: String? = null
    ): Single<ImageResponse>
}