package bigcar.com.bigcardriver

import android.app.ProgressDialog
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.*
import bigcar.com.bigcardriver.R.id.name_user
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : Fragment() {

    var userURL = "http://gentle-atoll-11837.herokuapp.com/user"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_profile, container, false)
        setHasOptionsMenu(true)

        val seperator_layout = myView.findViewById<View>(R.id.seperator)
        if (context.getSharedPreferences("myPref", MODE_PRIVATE).getString("myToken","") == ""){
            seperator_layout.visibility = View.GONE
        }else {
            seperator_layout.visibility = View.VISIBLE
        }

        return myView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val progressDialog = ProgressDialog(activity, R.style.DialogTheme)
        progressDialog.setMessage("Please Wait")

        val sharedPreferences = context.getSharedPreferences("myPref", MODE_PRIVATE).getString("myToken","")
        var jsonRequest = object  : JsonObjectRequest(Request.Method.GET, userURL, null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                val userInfo = response.getJSONObject("data")
                name_user.text = userInfo.getString("name")

                if (userInfo.getString("profilepic") == ""){
                    user_dp.setImageResource(R.mipmap.ic_app_user)
                } else {
                    Picasso.with(context).load(userInfo.getString("profilepic")).into(user_dp)
                }
                progressDialog.dismiss()
            }

        }, object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError) {
                Log.d("Debug", error.toString())
                progressDialog.dismiss()
            }

        }){
            @Throws(AuthFailureError::class)
            override fun getHeaders():Map<String,String>{
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer "+sharedPreferences)
                return headers
            }
        }

        val requestVolley = Volley.newRequestQueue(context)
        requestVolley.add(jsonRequest)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.log_out -> {
                AlertDialog.Builder(activity, R.style.DialogTheme)
                        .setCancelable(false)
                        .setTitle("Logout")
                        .setMessage("Are Sure you Want To Logout")
                        .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                context.getSharedPreferences("myPref", MODE_PRIVATE).edit().remove("myToken").commit()
                            }
                        })
                        .setNegativeButton("No", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface, which: Int) {
                                dialog.dismiss()
                            }
                        })
                        .create()
                        .show()
                return true
            }
            R.id.log_in -> {
                startActivity(Intent(context, StartActivity::class.java))
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        my_account.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(Intent(context, AccountActivity::class.java))
            }
        })

//        accept_trip.setOnClickListener(object : View.OnClickListener{
//            override fun onClick(v: View?) {
//                startActivity(Intent(context, AcceptedTripActivity::class.java))
//            }
//        })

        current_trip.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(Intent(context, CurrentTripActivity::class.java))
            }
        })

        completed_trip.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(Intent(context, CompletedTripActivity::class.java))
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_main_menu,menu)

        val log_in = menu.findItem(R.id.log_in)
        log_in.setVisible(false)
        val log_out = menu.findItem(R.id.log_in)
        log_out.setVisible(false)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val log_out = menu.findItem(R.id.log_out)
        val log_in = menu.findItem(R.id.log_in)

        if(context.getSharedPreferences("myPref", MODE_PRIVATE).contains("myToken")) {
            log_in.setVisible(false)
            log_out.setVisible(true)
        }else {
            log_in.setVisible(true)
            log_out.setVisible(false)
        }

    }

}
