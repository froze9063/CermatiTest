package id.testing.cermatitest.Connection

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class MyConnection {
    companion object {

        @Throws(IOException::class)
        private fun buildClient(token : String?) : OkHttpClient {

            val logging =  HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val httpClient = OkHttpClient.Builder()
            httpClient.readTimeout(60, TimeUnit.SECONDS)
            httpClient.connectTimeout(60, TimeUnit.SECONDS)
            httpClient.retryOnConnectionFailure(false)

            httpClient.addInterceptor {

                val original = it.request()
                val request = original.newBuilder()
                request.addHeader("Content-Type","application/x-www-form-urlencoded")
                request.addHeader("X-Requested-With","XMLHttpRequest")

                if (token != null){
                    request.addHeader("Authorization", "$token")
                }

                it.proceed(request.build())
            }

            httpClient.addInterceptor(logging)

            return httpClient.build()
        }

        private fun buildRetrofit(token : String?) : Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(buildClient(token))
                .build()
        }

        fun <T> myClient(myClass : Class<T>,token : String?) : T {
            return buildRetrofit(token).create(myClass)
        }
    }
}