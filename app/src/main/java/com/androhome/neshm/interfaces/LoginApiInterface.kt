package com.androhome.neshm.interfaces

import com.androhome.neshm.model.NearbyProviderModel
import com.androhome.neshm.model.SignupModel
import com.androhome.neshm.model.loginModel
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginApiInterface {
    @GET("loginJSON.php")
    fun login(
        @Query("userdata") emailId: String,
        @Query("userpass") password: String
    ): Observable<loginModel.allUsers>

    @GET("signupJSON.php")
    fun signIn(
        @Query("role") role: String,
        @Query("email") email: String,
        @Query("phone") phone: String,
        @Query("name") name: String,
        @Query("password") password: String
    ): Observable<SignupModel.status>

    @GET("getNearbyProviderJson.php")
    fun nearbyProviders(
        @Query("state") city: String
    ): Observable<NearbyProviderModel.allUsers>

    @GET("verifyOtp.php")
    fun verifyOtp(
        @Query("user_id") emailId: String,
        @Query("otp") otp: Int
    ): Observable<SignupModel.status>

    companion object {
        fun create(): LoginApiInterface {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val httpClient = OkHttpClient.Builder()

            httpClient.addInterceptor(logging)
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create()
                )
                .addConverterFactory(
                    GsonConverterFactory.create(gson)
                )
                .baseUrl("http://www.neshm.com/android/")
                .client(httpClient.build())
                .build()
            return retrofit.create(LoginApiInterface::class.java)
        }
    }
}