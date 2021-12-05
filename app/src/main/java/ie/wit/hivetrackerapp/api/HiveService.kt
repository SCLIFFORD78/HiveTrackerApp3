package ie.wit.hivetrackerapp.api

import ie.wit.donationx.models.DonationModel
import ie.wit.hivetrackerapp.models.HiveModel
import retrofit2.Call
import retrofit2.http.*


interface HiveService {
    @GET("/api/hives")
    fun getall(): Call<List<HiveModel>>

    @GET("/api/hives/{id}")
    fun get(@Path("id") id: String): Call<DonationModel>

    @DELETE("/donations/{id}")
    fun delete(@Path("id") id: String): Call<DonationWrapper>

    @POST("/donations")
    fun post(@Body donation: DonationModel): Call<DonationWrapper>

    @PUT("/donations/{id}")
    fun put(@Path("id") id: String,
            @Body donation: DonationModel
    ): Call<DonationWrapper>
}