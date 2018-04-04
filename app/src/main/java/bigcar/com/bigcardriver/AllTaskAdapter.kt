package bigcar.com.bigcardriver

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by iTrain on 04-Apr-18.
 */

class AllTaskAdapter(private val context: Context, private val allTrip : List<Trip>, private val listener: OnItemClickListener) : RecyclerView.Adapter<AllTaskAdapter.ViewHolder>(){

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        var trip_customer_img : ImageView
        var trip_name : TextView

        init {
            trip_customer_img = itemView.findViewById(R.id.trip_customer_img)
            trip_name = itemView.findViewById(R.id.trip_name)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.view_all_trip,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return allTrip.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val allTrip : Trip = allTrip.get(position)
        holder?.trip_customer_img.setImageResource(allTrip.img)
        holder?.trip_name?.text = allTrip.name
    }

    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }
}