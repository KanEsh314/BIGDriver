package bigcar.com.bigcardriver

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {

    private val splash_screen = 2000;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            if(applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken", "") == ""){
                startActivity(Intent(this@SplashActivity, StartActivity::class.java))
            }else{
                startActivity(intent)
            }
            finish()
        },splash_screen.toLong())
    }
}
