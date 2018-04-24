package bigcar.com.bigcardriver


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_home, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return myView
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mMap.isMyLocationEnabled
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        mMap.setOnMapLoadedCallback(object :GoogleMap.OnMapLoadedCallback{
            override fun onMapLoaded() {
                val locations = ArrayList<LatLng>()
                locations.add(LatLng(3.1577636,101.71186))
                locations.add(LatLng(3.1614164,101.71887160000006))
                locations.add(LatLng(3.1537798,101.71322140000007))

                for (latlng in locations){
                    mMap.addMarker(MarkerOptions().position(latlng).title("Title can be anything"))
                }

                val builder = LatLngBounds.Builder()
                builder.include(locations.get(0)) //Taking Point A (First LatLng)
                builder.include(locations.get(locations.size - 1)) //Taking Point B (Second LatLng)
                val bounds = builder.build()
                val cu = CameraUpdateFactory.newLatLngBounds(bounds, 200)
                mMap.moveCamera(cu)
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15F), 2000, null)
            }

        })
    }
}
