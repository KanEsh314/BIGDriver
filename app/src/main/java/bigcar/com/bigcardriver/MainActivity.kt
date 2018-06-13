package bigcar.com.bigcardriver

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.util.Log
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.RelativeLayout
import android.widget.Toast
import bigcar.com.bigcardriver.R.id.*
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.iid.FirebaseInstanceId
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var pagerAdapter: CustomPagerAdapter?=null

    var deviceURL = "https://gentle-atoll-11837.herokuapp.com/api/userdevice"
    var userURL = "https://gentle-atoll-11837.herokuapp.com/api/user"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar_main)

        pagerAdapter = CustomPagerAdapter(supportFragmentManager)
        pagerAdapter!!.addFragments(HomeFragment(),"Home")
        pagerAdapter!!.addFragments(ProfileFragment(), "Profile")

        viewPager.adapter = pagerAdapter
        tabs.setupWithViewPager(viewPager)
        tabs.addOnTabSelectedListener(object : TabLayout.ViewPagerOnTabSelectedListener(viewPager){
            override fun onTabSelected(tab : TabLayout.Tab){
                super.onTabSelected(tab)
                val tabIconColor = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
                tab!!.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                super.onTabUnselected(tab)
                val tabIconColor = ContextCompat.getColor(applicationContext, R.color.colorDark)
                tab!!.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                super.onTabReselected(tab)
            }
        })
        setupTabIcons()

        val intent = intent
        val message = intent.getStringExtra("message")
        Log.d("debug","message is "+message)
        if(message != null) {
            Log.d("debug","message.. I m here")
            AlertDialog.Builder(this)
                    .setTitle("Notification")
                    .setMessage(message)
                    .setPositiveButton("Ok", { dialog, which -> }).show()
        }
//        val hasRegistered = applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).getBoolean("device",false)
//
//       if(hasRegistered){
           sendToken()
//       }

        status.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                //Toast.makeText(applicationContext, "Status Is "+isChecked.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupTabIcons() {
        tabs.getTabAt(0)!!.setIcon(R.mipmap.ic_action_home)
        tabs.getTabAt(1)!!.setIcon(R.mipmap.ic_action_profile)
    }

    private fun getUser(){
        Handler().postDelayed({

            val sharedPreferences = applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","")
            var jsonRequest = object  : JsonObjectRequest(Request.Method.GET, userURL, null, object : Response.Listener<JSONObject>{
                override fun onResponse(response: JSONObject) {
                    val userInfo = response.getJSONObject("data")
                    if (userInfo.length() != 0){
                        AlertDialog.Builder(applicationContext, R.style.DialogTheme)
                                .setCancelable(false)
                                .setTitle("Logout")
                                .setMessage("Are Sure you Want To Logout")
                                .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        finish()
                                    }
                                })
                                .setNegativeButton("No", object : DialogInterface.OnClickListener {
                                    override fun onClick(dialog: DialogInterface, which: Int) {
                                        dialog.dismiss()
                                    }
                                })
                                .create()
                                .show()
                    }
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

        }, 2500)
    }

    private fun sendToken(){
        val sharedPreferences = applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","")
        var jsonRequest = object  : StringRequest(Request.Method.POST, deviceURL,  object : Response.Listener<String>{
            override fun onResponse(response: String) {
                applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).edit().putBoolean("device",true).commit()
                Log.d("debug","API SENT")
            }

        }, object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError) {
                Log.d("debug",FirebaseInstanceId.getInstance().getToken()!!)
                Log.d("Debug", error.toString())
            }

        }){
            @Throws(AuthFailureError::class)
            override fun getHeaders():Map<String,String>{
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer "+sharedPreferences)
                return headers
            }

                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = java.util.HashMap<String, String>()
                    params.put("device_id", FirebaseInstanceId.getInstance().getToken()!!)
                    return params
                }
        }

        val requestVolley = Volley.newRequestQueue(applicationContext)
        requestVolley.add(jsonRequest)
    }
}

class CustomPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    var mFm = fm
    var mFragmentItems: ArrayList<Fragment> = ArrayList()
    var mFragmentTitle: ArrayList<String> = ArrayList()

    fun addFragments(fragment: Fragment, fragmentTitle: String){
        mFragmentItems.add(fragment)
        mFragmentTitle.add(fragmentTitle)
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentItems[position]
    }

    override fun getCount(): Int {
        return mFragmentItems.count()
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitle[position]
    }

}
