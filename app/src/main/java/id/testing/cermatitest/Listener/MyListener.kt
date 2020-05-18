package id.testing.cermatitest.Listener

import id.testing.cermatitest.Model.UserModel

class MyListener {
    companion object{
        interface SearchListener{
            fun onSearchClicked(userModel: UserModel)
            fun onSearchErrorClicked()
        }
    }
}