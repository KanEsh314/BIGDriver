package bigcar.com.bigcardriver

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_current_trip.*
import org.json.JSONObject

class CurrentTripActivity : AppCompatActivity() {

    var currentdURL = "https://gentle-atoll-11837.herokuapp.com/api/currenttrip"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_trip)

        setSupportActionBar(toolbar_current)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val currentAdapter = CurrentAdapter()
        val currentLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recycleViewCurrent!!.layoutManager = currentLayoutManager
        recycleViewCurrent!!.itemAnimator = DefaultItemAnimator()
        recycleViewCurrent!!.adapter = currentAdapter

        val sharedPreferences = applicationContext.getSharedPreferences("myPref", MODE_PRIVATE).getString("myToken","")
        var jsonRequest = object  : JsonObjectRequest(Request.Method.GET, currentdURL, null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {

                val currentTour = response.getJSONObject("data")
                currentAdapter.addJsonObject(currentTour)
                currentAdapter.notifyDataSetChanged()
            }

        }, object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError) {
                Log.d("Debug", error.toString())
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId

        if(id == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}

class CurrentAdapter(): RecyclerView.Adapter<CurrentAdapter.ViewHolder>() {

    private val current = ArrayList<JSONObject>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tag: TextView
        var date: TextView
        var pick_up: TextView
        var drop_off: TextView
        var trip_fare: TextView
        var payment_type: TextView

        init {
            tag = itemView.findViewById(R.id.tripTag)
            date = itemView.findViewById(R.id.tripDate)
            pick_up = itemView.findViewById(R.id.driver_pick_up)
            drop_off = itemView.findViewById(R.id.driver_drop_off)
            trip_fare = itemView.findViewById(R.id.trip_fare)
            payment_type = itemView.findViewById(R.id.payment_type)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.trip_content,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.tag?.text = current.get(position).getString("trip_type")
        holder?.date?.text = current.get(position).getString("datetime")
        holder?.pick_up?.text = current.get(position).getString("pickup_address")
        holder?.drop_off?.text = current.get(position).getString("dropoff_address")
        holder?.trip_fare?.text = "12"
        holder?.payment_type?.text = current.get(position).getString("paymentmethod")
    }

    override fun getItemCount(): Int {
        return current.size
    }

    fun addJsonObject(jsonObject: JSONObject) {
        current.add(jsonObject)
    }
}
