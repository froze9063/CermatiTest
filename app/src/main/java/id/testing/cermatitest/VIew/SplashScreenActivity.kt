package id.testing.cermatitest.VIew

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import id.testing.cermatitest.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        btnNext.setOnClickListener(this)
        setView()
    }

    private fun setView(){
        Handler().postDelayed({
            btnNext.visibility = View.VISIBLE
        },3000)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btnNext -> {
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        }
    }
}
