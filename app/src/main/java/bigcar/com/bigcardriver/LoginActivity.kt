package bigcar.com.bigcardriver

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    var CheckEditText:Boolean = false
    var loginURL = "http://gentle-atoll-11837.herokuapp.com/auth/login"

    var emailHolder:String = ""
    var passwordHolder:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        forgot_password.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(Intent(applicationContext, ForgotActivity::class.java))
            }
        })

        loginBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {

                CheckEditTextIsEmptyOrNot()
                if (CheckEditText){
                    userLogin()
                    try{

                    }catch (e : JSONException){

                    }
                }else{
                    Toast.makeText(applicationContext, "Please fill all form fields.", Toast.LENGTH_LONG).show()
                }

            }
        })

        back_btn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                onBackPressed()
            }
        })
    }

    private fun userLogin() {
        val progressDialog = ProgressDialog(this, R.style.DialogTheme)
        progressDialog.setMessage("Please Wait, We are Inserting Your Data on Server")
        progressDialog.show()

        val stringRequest = object : StringRequest(Request.Method.POST, loginURL, object : Response.Listener<String>{
            override fun onResponse(response: String?) {
                val sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE)
                try{
                    val myToken = JSONObject(response)
                    sharedPreferences.edit().putString("myToken",myToken.getJSONObject("result").getString("token")).commit()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                }catch (e : JSONException){
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Please Check Your Email and Password", Toast.LENGTH_LONG).show()
                }
                progressDialog.dismiss()
                //Toast.makeText(applicationContext, response, Toast.LENGTH_LONG).show()
            }
        }, object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
            }
        }){
            @Throws(AuthFailureError::class)
            override fun getParams():Map<String, String> {
                val params = HashMap<String, String>()
                params.put("email", emailHolder)
                params.put("password", passwordHolder)
                return params
            }
        }

        val requestVolley = Volley.newRequestQueue(this)
        requestVolley.add(stringRequest)
    }

    private fun CheckEditTextIsEmptyOrNot() {
        emailHolder = login_email.text.toString()
        passwordHolder = login_password.text.toString()

        if (TextUtils.isEmpty(emailHolder) || TextUtils.isEmpty(passwordHolder)){
            CheckEditText = false
        }else {
            CheckEditText = true
        }
    }
}
