package bigcar.com.bigcardriver

import android.app.ProgressDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_vehicle.*
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class VehicleActivity : AppCompatActivity() {

    var userURL = "https://gentle-atoll-11837.herokuapp.com/user"
    var vehicleTypeURL = "https://gentle-atoll-11837.herokuapp.com/api/vehicletypes"
    var locationURL = "https://gentle-atoll-11837.herokuapp.com/api/states"

    val vehicleTypMaterial = ArrayList<JSONObject>()
    val locationMaterial = ArrayList<JSONObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle)

        val progressDialog = ProgressDialog(this, R.style.DialogTheme)
        progressDialog.setMessage("Please Wait")
        progressDialog.show()

        val requestVolley = Volley.newRequestQueue(applicationContext)

        val vehicleTypeAdapter = VehicleTypeAdapter(this)
        vehicle_type.setAdapter(vehicleTypeAdapter)
        vehicle_type.setOnItemSelectedListener(object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //vehicleTypeHolder = vehicleTypMaterial.get(position).getString("vehicletype_id")
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
        country.setAdapter(locationAdapter)
        country.setOnItemSelectedListener(object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //locationHolder = locationMaterial.get(position).getString("state_id")
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

        val sharedPreferences = applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","")
        var jsonRequest = object  : JsonObjectRequest(Request.Method.GET, userURL, null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                val userInfo = response.getJSONObject("data")
                register_number?.text = Editable.Factory.getInstance().newEditable(userInfo.getString("reg_number"))
                brand_model?.text = Editable.Factory.getInstance().newEditable(userInfo.getString("car_model"))
                license_number?.text = Editable.Factory.getInstance().newEditable(userInfo.getString("license_number"))
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

        requestVolley.add(jsonRequest)
    }
}
