package bigcar.com.bigcardriver

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONException
import java.util.HashMap
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class SignUpActivity : AppCompatActivity() {

    var CheckEditText:Boolean = false
    var registerURL = "https://gentle-atoll-11837.herokuapp.com/api/registerdriver"

    var profileHolder: String = ""
    var icpassportHolder: String = ""
    var nameHolder: String = ""
    var emailHolder: String = ""
    var addressHolder: String = ""
    var phoneHolder: String = ""
    var passwordHolder: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        registerBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        })

        back_btn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                onBackPressed()
            }
        })

        registerBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {

                CheckEditTextIsEmptyOrNot()
                if (CheckEditText){
                    driverRegistration()
                    try {

                    }catch (e : JSONException){

                    }
                }else{
                    Toast.makeText(applicationContext, "Please fill all form fields.", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun driverRegistration() {
        val progressDialog = ProgressDialog(this, R.style.DialogTheme)
        progressDialog.setMessage("Please Wait, We are Inserting Your Data on Server")
        progressDialog.show()

        val stringRequest = object : StringRequest(Request.Method.POST, registerURL, object : Response.Listener<String> {
            override fun onResponse(response: String?) {
                progressDialog.dismiss()
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                //Toast.makeText(applicationContext, response, Toast.LENGTH_LONG).show()
            }

        },
                object : Response.ErrorListener{
                    override fun onErrorResponse(error: VolleyError?) {
                        progressDialog.dismiss()
                        //Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
                    }

                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
//                params.put("profilepic", profileHolder)
                params.put("name", nameHolder)
                params.put("ic", icpassportHolder)
                params.put("email", emailHolder)
                params.put("address", emailHolder)
                params.put("phonenumber", phoneHolder)
                params.put("password", passwordHolder)
                return params
            }
        }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun CheckEditTextIsEmptyOrNot() {

        nameHolder = register_name.text.toString()
        icpassportHolder = register_ic_passport.text.toString()
        emailHolder = register_email.text.toString()
        addressHolder = register_phonenumber.text.toString()
        phoneHolder = register_phonenumber.text.toString()
        passwordHolder = register_password.text.toString()

        if (TextUtils.isEmpty(nameHolder) || TextUtils.isEmpty(icpassportHolder) || TextUtils.isEmpty(emailHolder) || TextUtils.isEmpty(addressHolder) || TextUtils.isEmpty(phoneHolder) || TextUtils.isEmpty(passwordHolder)){
            CheckEditText = false
        }else {
            CheckEditText = true
        }
    }
}
