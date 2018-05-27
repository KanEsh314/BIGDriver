package bigcar.com.bigcardriver

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import bigcar.com.bigcardriver.R.id.*
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_account.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class AccountActivity : AppCompatActivity() {

    var CheckEditText:Boolean = false
    var userURL = "https://gentle-atoll-11837.herokuapp.com/api/user"
    var updateURL = "https://gentle-atoll-11837.herokuapp.com/api/updateuser"

    private val PICK_IMAGE_REQUEST = 111
    lateinit var bitmap: Bitmap

    var profileHolder: String = ""
    var nameHolder: String = ""
    var numberHolder: String = ""
    var addressHolder: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        setSupportActionBar(account_toolbar)

        profilePicture.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent()
                intent.setType("image/*")
                intent.setAction(Intent.ACTION_PICK)
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
            }
        })

        val progressDialog = ProgressDialog(this, R.style.DialogTheme)
        progressDialog.setMessage("Please Wait")
        progressDialog.show()


        val sharedPreferences = applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","")
        var jsonRequest = object  : JsonObjectRequest(Request.Method.GET, userURL, null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                val userInfo = response.getJSONObject("data")
                if (userInfo.getString("profilepic") == ""){
                    profilePicture.setImageResource(R.mipmap.ic_app_user)
                }else{
                    Picasso.with(applicationContext).load(userInfo.getString("profilepic")).into(profilePicture)
                }
                name.text = Editable.Factory.getInstance().newEditable(userInfo.getString("name"))
                hp_nbr.text = Editable.Factory.getInstance().newEditable(userInfo.getString("phonenumber"))
                address.text = Editable.Factory.getInstance().newEditable(userInfo.getString("address"))
                progressDialog.dismiss()
            }

        }, object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError) {
                onBackPressed()
            }

        }){
            @Throws(AuthFailureError::class)
            override fun getHeaders():Map<String,String>{
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer "+sharedPreferences)
                return headers
            }
        }

        val requestVolley = Volley.newRequestQueue(applicationContext)
        requestVolley.add(jsonRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null){
            var filePath = data.data

            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                profilePicture.setImageBitmap(bitmap)

                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
                val imageBytes = baos.toByteArray()
                profileHolder = Base64.encodeToString(imageBytes, Base64.DEFAULT)

            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.account_main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save -> {

                CheckEditTextIsEmptyOrNot()
                if (CheckEditText){
                    userUpadate()
                }else{
                    Toast.makeText(applicationContext, "Please fill all form fields.", Toast.LENGTH_LONG).show()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun userUpadate() {

        val sharedPreferences = applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","")
        val stringRequest = object : StringRequest(Request.Method.POST, updateURL, object : Response.Listener<String>{
            override fun onResponse(response: String?) {
                onBackPressed()
            }
        }, object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
                Log.d("Debug", error.toString())
            }
        }){
            @Throws(AuthFailureError::class)
            override fun getHeaders():Map<String,String>{
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer "+sharedPreferences)
                return headers
            }
            override fun getParams():Map<String, String> {
                val params = HashMap<String, String>()
                params.put("profilepic", profileHolder)
                params.put("name", nameHolder)
                params.put("phonenumber", numberHolder)
                params.put("address", addressHolder)
                Log.d("Debug", params.toString())
                return params
            }
        }

        val requestVolley = Volley.newRequestQueue(this)
        requestVolley.add(stringRequest)
    }

    private fun CheckEditTextIsEmptyOrNot() {
        nameHolder = name.text.toString()
        numberHolder = hp_nbr.text.toString()
        addressHolder = address.text.toString()

        if (TextUtils.isEmpty(nameHolder) || TextUtils.isEmpty(numberHolder) || TextUtils.isEmpty(addressHolder)){
            CheckEditText = false
        }else {
            CheckEditText = true
        }
    }
}
