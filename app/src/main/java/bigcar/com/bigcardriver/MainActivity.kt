package bigcar.com.bigcardriver

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val alltrip = ArrayList<Trip>()
        prepareAllTrip(alltrip)
        val tripAdapter = AllTaskAdapter(this, alltrip, object: AllTaskAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                Toast.makeText(applicationContext, "Trying", Toast.LENGTH_LONG).show()
            }
        })
        val tripLayoutManager = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        recycleViewAllTrip!!.layoutManager = tripLayoutManager
        recycleViewAllTrip!!.itemAnimator = DefaultItemAnimator()
        recycleViewAllTrip!!.adapter = tripAdapter
    }

    private fun prepareAllTrip(alltrip: ArrayList<Trip>) {
        alltrip.add(Trip(R.mipmap.ic_launcher,"Singapore"))
        alltrip.add(Trip(R.mipmap.ic_launcher, "Jurong"))
    }
}

class Trip(val img : Int, val name : String)
