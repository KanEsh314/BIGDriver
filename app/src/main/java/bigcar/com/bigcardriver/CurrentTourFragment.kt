package bigcar.com.bigcardriver


import android.app.ProgressDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
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
import kotlinx.android.synthetic.main.fragment_current_tour.*
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 *
 */
class CurrentTourFragment : Fragment() {

    var currentdTourURL = "https://gentle-atoll-11837.herokuapp.com/api/currenttrip"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_tour, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressDialog = ProgressDialog(activity, R.style.DialogTheme)
        progressDialog.setCancelable(false)
        progressDialog.isIndeterminate=true
        progressDialog.show()

        val ctAdapter = CurrentTourAdapter(activity)
        val ctLayoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, true)
        recycleViewCurrentTour!!.layoutManager = ctLayoutManager
        recycleViewCurrentTour!!.itemAnimator = DefaultItemAnimator()
        recycleViewCurrentTour!!.adapter = ctAdapter

        val sharedPreferences = activity.getSharedPreferences("myPref", MODE_PRIVATE).getString("myToken","")
        var jsonRequest = object  : JsonObjectRequest(Request.Method.GET, currentdTourURL, null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {

                val atTour = response.getJSONObject("data").getJSONArray("attractionbookings")
                for (i in 0 until atTour.length()){
                    ctAdapter.addJsonObject(atTour.getJSONObject(i))
                }
                progressDialog.dismiss()
                ctAdapter.notifyDataSetChanged()

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

        val requestVolley = Volley.newRequestQueue(activity)
        requestVolley.add(jsonRequest)
    }

}

class CurrentTourAdapter(private val context: Context) : RecyclerView.Adapter<CurrentTourAdapter.ViewHolder>() {

    private val CurrentTour = ArrayList<JSONObject>()

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
        holder?.tag?.text = CurrentTour.get(position).getString("trip_type")
        holder?.date?.text = CurrentTour.get(position).getString("datetime")
        holder?.pick_up?.text = CurrentTour.get(position).getString("pickup_address")
        holder?.drop_off?.text = CurrentTour.get(position).getString("dropoff_address")
        holder?.trip_fare?.text = "12"
        holder?.payment_type?.text = CurrentTour.get(position).getString("paymentmethod")
    }

    override fun getItemCount(): Int {
        return CurrentTour.size
    }

    fun addJsonObject(jsonObject: JSONObject) {
        CurrentTour.add(jsonObject)
    }

}
