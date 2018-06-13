package bigcar.com.bigcardriver

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import bigcar.com.bigcardriver.R.id.go_login_btn
import bigcar.com.bigcardriver.R.id.go_register_btn
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        go_login_btn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        })

        go_register_btn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(Intent(applicationContext, SignUpActivity::class.java))
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        moveTaskToBack(true)
    }
}
