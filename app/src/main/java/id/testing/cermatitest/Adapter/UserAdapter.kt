package id.testing.cermatitest.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import id.testing.cermatitest.Listener.MyListener
import id.testing.cermatitest.Model.UserModel
import id.testing.cermatitest.R
import id.testing.cermatitest.VIew.MyTextView

class UserAdapter(context : Context,userList : ArrayList<UserModel>, myListener : MyListener.Companion.SearchListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val contexts = context
    private val listeners = myListener
    private var userDataLists = userList
    private var errorCode = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0){
            val view = LayoutInflater.from(contexts).inflate(R.layout.item_user,parent,false)
            return UserViewHolder(view)
        }
        else if (viewType == 1){
            val view = LayoutInflater.from(contexts).inflate(R.layout.item_loading,parent,false)
            return LoadingViewHolder(view)
        }
        else{
            val view = LayoutInflater.from(contexts).inflate(R.layout.item_error,parent,false)
            return ErrorViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
       return userDataLists.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val userModel = userDataLists[position]
        val name = userModel.login ?: ""
        val imgUrl = userModel.avatar_url ?: ""

        if (holder is UserViewHolder){
            holder.txtName.text = name

            if (imgUrl.isNotEmpty()){
                Picasso.get().load(imgUrl).into(holder.imgUser)
            }

            holder.linearParent.setOnClickListener {
                listeners.onSearchClicked(userModel)
            }
        }
        else if (holder is LoadingViewHolder){

        }
        else{
            val myHolder = holder as ErrorViewHolder
            if (errorCode == 403){
                myHolder.txtContent.text = "Request over limit, please wait 1 minute"
            }
            else{
                myHolder.txtContent.text = "There was an error"
            }

            myHolder.txtTryAgain.setOnClickListener {
                listeners.onSearchErrorClicked()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return userDataLists[position].viewType
    }

    fun updateUser(userList : ArrayList<UserModel>, errorCode : Int){
        this.userDataLists = userList
        this.errorCode = errorCode
        notifyDataSetChanged()
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val linearParent = itemView.findViewById<LinearLayout>(R.id.linearParent)
        val imgUser = itemView.findViewById<CircleImageView>(R.id.imgUser)
        val txtName = itemView.findViewById<MyTextView>(R.id.txtName)
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtContent = itemView.findViewById<MyTextView>(R.id.txtContent)
        val txtTryAgain = itemView.findViewById<MyTextView>(R.id.txtTryAgain)
    }
}