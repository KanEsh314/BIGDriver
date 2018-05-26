package bigcar.com.bigcardriver

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONException
import java.util.HashMap
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.ArrayList

class SignUpActivity : AppCompatActivity() {

    var CheckEditText:Boolean = false
    var registerURL = "https://gentle-atoll-11837.herokuapp.com/api/registerdriver"
    var vehicleTypeURL = "https://gentle-atoll-11837.herokuapp.com/api/vehicletypes"
    var locationURL = "https://gentle-atoll-11837.herokuapp.com/api/states"

    val vehicleTypMaterial = ArrayList<JSONObject>()
    val locationMaterial = ArrayList<JSONObject>()

    var profileHolder: String = ""
    var icpassportHolder: String = ""
    var nameHolder: String = ""
    var emailHolder: String = ""
    var addressHolder: String = ""
    var phoneHolder: String = ""
    var passwordHolder: String = ""
    var regHolder: String = ""
    var modelHolder: String = ""
    var licenseHolder: String = ""
    var vehicleTypeHolder: String = ""
    var locationHolder: String = ""

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

        val requestVolley = Volley.newRequestQueue(applicationContext)

        val vehicleTypeAdapter = VehicleTypeAdapter(this)
        vehicle_id.setAdapter(vehicleTypeAdapter)
        vehicle_id.setOnItemSelectedListener(object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                vehicleTypeHolder = vehicleTypMaterial.get(position).getString("vehicletype_id")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

        var jsonObjectRequestvehicleType = JsonObjectRequest(Request.Method.GET, vehicleTypeURL,null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) = try {

                val tourVehicle = response.getJSONArray("data")

                for (i in 0 until tourVehicle.length()){
                    vehicleTypMaterial.add(tourVehicle.getJSONObject(i))
                    vehicleTypeAdapter.addJsonObject(tourVehicle.getJSONObject(i))
                }
                vehicleTypeAdapter.notifyDataSetChanged()
            } catch (e : JSONException){
                e.printStackTrace()
            }
        },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        Log.d("Debug", error.toString())
                    }
                })

        requestVolley.add(jsonObjectRequestvehicleType)

        val locationAdapter = LocationAdapter(this)
        location.setAdapter(locationAdapter)
        location.setOnItemSelectedListener(object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                locationHolder = locationMaterial.get(position).getString("state_id")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

        var jsonObjectRequestlocation = JsonObjectRequest(Request.Method.GET, locationURL,null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) = try {

                val tourLocation = response.getJSONArray("data")

                for (i in 0 until tourLocation.length()){
                    locationMaterial.add(tourLocation.getJSONObject(i))
                    locationAdapter.addJsonObject(tourLocation.getJSONObject(i))
                }
                locationAdapter.notifyDataSetChanged()
            } catch (e : JSONException){
                e.printStackTrace()
            }
        },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        Log.d("Debug", error.toString())
                    }
                })

        requestVolley.add(jsonObjectRequestlocation)
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
                params.put("profilepic", profileHolder)
                params.put("name", nameHolder)
                params.put("ic", icpassportHolder)
                params.put("email", emailHolder)
                params.put("address", addressHolder)
                params.put("phonenumber", phoneHolder)
                params.put("password", passwordHolder)
                params.put("reg_number", regHolder)
                params.put("car_model", modelHolder)
                params.put("license_number", licenseHolder)
                params.put("vehicletype_id", vehicleTypeHolder)
                params.put("location", locationHolder)
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
        regHolder = register_number.text.toString()
        modelHolder = register_model.text.toString()
        licenseHolder = register_license.text.toString()

        if (TextUtils.isEmpty(nameHolder) || TextUtils.isEmpty(icpassportHolder) || TextUtils.isEmpty(emailHolder) || TextUtils.isEmpty(addressHolder) || TextUtils.isEmpty(phoneHolder) || TextUtils.isEmpty(passwordHolder) || TextUtils.isEmpty(regHolder) || TextUtils.isEmpty(modelHolder) || TextUtils.isEmpty(licenseHolder)){
            CheckEditText = false
        }else {
            CheckEditText = true
        }
    }
}

class LocationAdapter(private val context: Context) : BaseAdapter() {

    private val location = ArrayList<JSONObject>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val textView = TextView(context)
        textView.gravity = Gravity.LEFT
        textView.setPadding(16, 16, 16, 16)
        textView.textSize = 14F
        textView.text = location.get(position).getString("state_name")
        textView.setTextColor(Color.parseColor("#000000"))
        return textView
    }

    override fun getItem(position: Int): Any {
        return location
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return location.size
    }

    fun addJsonObject(jsonObject: JSONObject) {
        location.add(jsonObject)
    }
}

class VehicleTypeAdapter(private val context: Context) : BaseAdapter() {

    private val vehicleType = ArrayList<JSONObject>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val textView = TextView(context)
        textView.gravity = Gravity.LEFT
        textView.setPadding(16, 16, 16, 16)
        textView.textSize = 14F
        textView.text = vehicleType.get(position).getString("type")
        textView.setTextColor(Color.parseColor("#000000"))
        return textView
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return vehicleType.size
    }

    fun addJsonObject(jsonObject: JSONObject) {
        vehicleType.add(jsonObject)
    }

}
