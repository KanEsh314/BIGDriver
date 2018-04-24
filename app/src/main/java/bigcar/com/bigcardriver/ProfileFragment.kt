package bigcar.com.bigcardriver


import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.*
import bigcar.com.bigcardriver.R.id.completed_trip
import bigcar.com.bigcardriver.R.id.my_account
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_profile, container, false)
        setHasOptionsMenu(true)
        return myView
    }

//    override fun onPrepareOptionsMenu(menu: Menu) {
//        super.onPrepareOptionsMenu(menu)
//        if (menu.findItem(R.id.log_out).isChecked){
//            AlertDialog.Builder(activity, R.style.DialogTheme)
//                    .setCancelable(false)
//                    .setTitle("Logout")
//                    .setMessage("Are Sure you Want To Logout")
//                    .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
//                        override fun onClick(dialog: DialogInterface?, which: Int) {
//                            //context.getSharedPreferences("myPref", MODE_PRIVATE).edit().remove("myToken").commit()
//                        }
//                    })
//                    .setNegativeButton("No", object : DialogInterface.OnClickListener {
//                        override fun onClick(dialog: DialogInterface, which: Int) {
//                            dialog.dismiss()
//                        }
//                    })
//                    .create()
//                    .show()
//        }
//        else if (menu.findItem(R.id.log_in).isChecked){
//            startActivity(Intent(context, StartActivity::class.java))
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_main_menu,menu)
        //val log_out = menu.findItem(R.id.log_out)
        //log_out.setVisible(false)
        super.onCreateOptionsMenu(menu, inflater)
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
                                //context.getSharedPreferences("myPref", MODE_PRIVATE).edit().remove("myToken").commit()
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

        completed_trip.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(Intent(context, CompletedTripActivity::class.java))
            }
        })
    }
}
