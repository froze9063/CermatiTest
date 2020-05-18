package id.testing.cermatitest.VIew

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import id.testing.cermatitest.Adapter.UserAdapter
import id.testing.cermatitest.Callback.MyCallback
import id.testing.cermatitest.Controller.SearchController
import id.testing.cermatitest.Listener.MyListener
import id.testing.cermatitest.Model.UserModel
import id.testing.cermatitest.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MyCallback.Companion.SearchUserCallback,
    SwipeRefreshLayout.OnRefreshListener, MyListener.Companion.SearchListener{

    private lateinit var searchController : SearchController
    private lateinit var userDataList : ArrayList<UserModel>
    private lateinit var userAdapter: UserAdapter

    private var from = 1
    private var search = "Aldi"
    private var page = 1
    private var per_page = 10

    private var isLoading = false
    private var isErrorOccured = false
    private var totalRecords = 0

    private var lastIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setView()
    }

    private fun setView(){
        userDataList = ArrayList()
        userAdapter = UserAdapter(this,userDataList,this)
        rvUser.layoutManager = LinearLayoutManager(this)
        rvUser.adapter = userAdapter
        swipeRefresh.setOnRefreshListener(this)
        searchController = SearchController(this)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               search = newText ?: ""
               val searchLength = search.length

               if (searchLength > 0){
                   searchUser(false)
               }
                else{
                   rvUser.visibility = View.GONE
                   shimerLayout.visibility = View.GONE
                   linearEmpty.visibility = View.GONE
               }

               return false
            }
        })

        rvUser.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val lastVisiblePosition = (rvUser.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                val totalList = userDataList.size - 1

                if (lastVisiblePosition == totalList){
                    if (userDataList.size != totalRecords && !isLoading){
                        from = 2
                        searchUser(true)
                    }
                }
            }
        })
    }

    private fun searchUser(isLoadMore : Boolean){

        if (!isLoadMore){
            from = 1
            page = 1
            per_page = 10
        }
        else{
            page += 1
        }

        val dataMap = HashMap<String,Any>()
        dataMap["search"] = search
        dataMap["page"] = page
        dataMap["per_page"] = per_page
        dataMap["from"] = from

        searchController.searchUser(dataMap)
    }

    override fun onSearchPrepare() {
        if (from == 1){
            swipeRefresh.isRefreshing = false
            rvUser.visibility = View.GONE
            linearEmpty.visibility = View.GONE
            shimerLayout.visibility = View.VISIBLE
            shimerLayout.startShimmerAnimation()
        }
        else{
            lastIndex = userDataList.size - 1

            if (!isErrorOccured){
                userDataList.removeAt(lastIndex)
            }

            isLoading = true

            val userModel = UserModel()
            userModel.viewType = 1

            userDataList.add(userModel)
            userAdapter.updateUser(userDataList,0)
        }
    }

    override fun onSearchSuccess(metaMap: HashMap<String, Any>, userList: ArrayList<UserModel>) {
        totalRecords = metaMap["totalCount"] as Int

        if (from == 1){
            shimerLayout.visibility = View.GONE
            shimerLayout.stopShimmerAnimation()

            if (userList.size > 0){
                this.userDataList = userList
                rvUser.visibility = View.VISIBLE
                linearEmpty.visibility = View.GONE
                userAdapter.updateUser(userDataList,0)
            }
            else{
                rvUser.visibility = View.GONE
                linearEmpty.visibility = View.VISIBLE
            }
        }
        else{
            page += 1
            isLoading = false
            userDataList.removeAt(lastIndex)
            userDataList = userList
            userAdapter.updateUser(userDataList,0)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onSearchError(errorCode: Int) {
        if (from == 1){
            shimerLayout.visibility = View.GONE
            rvUser.visibility = View.GONE
            linearEmpty.visibility = View.VISIBLE
            shimerLayout.stopShimmerAnimation()

            if (errorCode == 403){
                txtError.text = "Request over limit, please try again in 1 minute"
            }
            else if (errorCode == 422){
                txtError.text = "No users found"
            }
            else{
                txtError.text = "There was an error please try again"
            }
        }
        else{
            isLoading = false
            userDataList.removeAt(lastIndex)

            val userModel = UserModel()
            userModel.viewType = 2
            userDataList.add(userModel)
            userAdapter.updateUser(userDataList, errorCode)
        }
    }

    override fun onRefresh() {
          searchUser(false)
    }

    override fun onSearchClicked(userModel: UserModel) {
        val name = userModel.login ?: ""
        Toast.makeText(this,"$name CLicked",Toast.LENGTH_LONG).show()
    }

    override fun onSearchErrorClicked() {
        searchUser(true)
    }
}
