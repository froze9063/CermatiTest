package id.testing.cermatitest.ApiService

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search/users?")
    fun getUserSearch(@Query("q") search : String,
                     @Query("page") page : Int,
                     @Query("per_page") per_page : Int) : Observable<Response<ResponseBody>>

}