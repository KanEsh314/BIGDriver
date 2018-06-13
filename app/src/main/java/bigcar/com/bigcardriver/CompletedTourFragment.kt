package bigcar.com.bigcardriver


import android.app.ProgressDialog
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_completed_tour.*
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 *
 */
class CompletedTourFragment : Fragment() {

    var acceptedtourbookingURL = "https://gentle-atoll-11837.herokuapp.com/api/completedtrip"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_completed_tour, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressDialog = ProgressDialog(activity, R.style.DialogTheme)
        progressDialog.setCancelable(false)
        progressDialog.isIndeterminate=true
        progressDialog.show()

        val atAdapter = AcceptedTourAdapter(activity)
        val atLayoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, true)
        recycleViewCompletedTour!!.layoutManager = atLayoutManager
        recycleViewCompletedTour!!.itemAnimator = DefaultItemAnimator()
        recycleViewCompletedTour!!.adapter = atAdapter

        val sharedPreferences = activity.getSharedPreferences("myPref", MODE_PRIVATE).getString("myToken","")
        var jsonRequest = object  : JsonObjectRequest(Request.Method.GET, acceptedtourbookingURL, null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {

                val atTour = response.getJSONObject("data").getJSONArray("attractionbookings")
                for (i in 0 until atTour.length()){
                    atAdapter.addJsonObject(atTour.getJSONObject(i))
                }
                progressDialog.dismiss()
                atAdapter.notifyDataSetChanged()

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
