package id.testing.cermatitest.Controller

import android.util.Log
import id.testing.cermatitest.ApiService.ApiService
import id.testing.cermatitest.Callback.MyCallback
import id.testing.cermatitest.Connection.MyConnection
import id.testing.cermatitest.Model.UserModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class SearchController(private val searchCallback: MyCallback.Companion.SearchUserCallback) {

    private val dataList = ArrayList<UserModel>()

    fun searchUser(data : HashMap<String,Any>){

        searchCallback.onSearchPrepare()

        val from = data["from"] as Int
        val search = data["search"] as String
        val page = data["page"] as Int
        val per_page = data["per_page"] as Int

        val retrofit = MyConnection.myClient(ApiService::class.java,null)
        val userSearch = retrofit.getUserSearch(search,page,per_page)

        userSearch.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
                Observer<Response<ResponseBody>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Response<ResponseBody>) {
               if (t.isSuccessful){

                   if (from == 1){
                        dataList.clear()
                   }

                   try {
                       val jsonBody  = JSONObject(t.body()?.string() ?: "")
                       val totalCount = jsonBody.getInt("total_count")
                       val incompleteResults = jsonBody.getBoolean("incomplete_results")
                       val items = jsonBody.getJSONArray("items")
                       val itemsCount = items.length()

                       for (i in 0 until itemsCount){
                           val itemObject = items[i] as JSONObject
                           val id  = itemObject.getInt("id")
                           val login = itemObject.getString("login")
                           val avatar_url = itemObject.getString("avatar_url")
                           val viewType = 0

                           val userModel = UserModel()
                           userModel.id = id
                           userModel.login = login
                           userModel.avatar_url = avatar_url
                           userModel.viewType = viewType

                           dataList.add(userModel)
                       }

                       val metaMap = HashMap<String,Any>()
                       metaMap["totalCount"] = totalCount
                       metaMap["incompleteResults"] = incompleteResults

                       searchCallback.onSearchSuccess(metaMap,dataList)
                   }
                   catch (jsonException : JSONException){
                       jsonException.printStackTrace()
                   }
               }
                else{
                   val errorCode = t.code()
                   searchCallback.onSearchError(errorCode)
               }
            }

            override fun onError(e: Throwable) {
               searchCallback.onSearchError(0)
               Log.d("search_error", e.message ?: "")
            }
        })
    }
}