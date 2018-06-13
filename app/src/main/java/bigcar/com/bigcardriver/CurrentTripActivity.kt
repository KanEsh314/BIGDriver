package bigcar.com.bigcardriver

import android.app.ProgressDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_current_trip.*

class CurrentTripActivity : AppCompatActivity() {

    var currentdURL = "https://gentle-atoll-11837.herokuapp.com/api/currenttrip"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_trip)

        setSupportActionBar(toolbar_current)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val currentPagerAdapter = CurrentAdapter(supportFragmentManager)
        currentPagerAdapter!!.addFragment(AcceptedTourFragment(),"Tour")
        currentPagerAdapter!!.addFragment(AcceptedAttractionFragment(), "Attraction")

        viewPagerCurrent.adapter = currentPagerAdapter
        tabs_current.setupWithViewPager(viewPagerCurrent)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId

        if(id == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}

class CurrentAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    var mFm = fm
    var mFragmentItems: ArrayList<Fragment> = ArrayList()
    var mFragmentTitle: ArrayList<String> = ArrayList()

    fun addFragment(fragment: Fragment, fragmentTitle: String) {
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

