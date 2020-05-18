package id.testing.cermatitest.Callback

import id.testing.cermatitest.Model.UserModel

class MyCallback {
    companion object{
        interface SearchUserCallback{
            fun onSearchPrepare()
            fun onSearchSuccess(metaMap : HashMap<String,Any>, userList : ArrayList<UserModel>)
            fun onSearchError(errorCode : Int)
        }
    }
}