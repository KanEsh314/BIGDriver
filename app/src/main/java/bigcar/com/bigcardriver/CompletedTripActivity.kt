package bigcar.com.bigcardriver

import android.app.ProgressDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_completed_trip.*

class CompletedTripActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_trip)

        setSupportActionBar(toolbar_completed)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val completedPagerAdapter = CTAdapter(supportFragmentManager)
        completedPagerAdapter!!.addFragment(AcceptedTourFragment(),"Tour")
        completedPagerAdapter!!.addFragment(AcceptedAttractionFragment(), "Attraction")

        viewPagerCompleted.adapter = completedPagerAdapter
        tabs_completed.setupWithViewPager(viewPagerCompleted)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId

        if(id == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}

class CTAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

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
