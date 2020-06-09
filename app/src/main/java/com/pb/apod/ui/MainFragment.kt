package com.pb.apod.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.pb.apod.R
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*

class MainFragment : Fragment(), KodeinAware {
    override val kodein by kodein()
    private val factory: ApodViewModelFactory by instance<ApodViewModelFactory>()
    private lateinit var header_image:ImageView
    private lateinit var tv_title:TextView
    private lateinit var tv_explanation:TextView
    private lateinit var btn_play:ImageButton
    private lateinit var calendar:ImageButton
    private lateinit var dateSelected:String
    var year_cal=-1
    var month_cal=-1
    var day_cal=-1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        header_image=view.findViewById(R.id.header_image)
        tv_title=view.findViewById(R.id.tv_title)
        tv_explanation=view.findViewById(R.id.tv_explanation)
        btn_play=view.findViewById(R.id.btn_play)
        calendar=view.findViewById(R.id.iv_calender)



        val apodViewModel= ViewModelProviders.of(this,factory).get(ApodViewModel::class.java)
        // binding.apodModel=apodViewModel
        apodViewModel.apod.observe(viewLifecycleOwner, Observer {  state->
            Glide.with(this)
                .load(state.hdurl)
                .into(header_image)
            tv_title.text=state.title
            tv_explanation.text=state.explanation
        })
        btn_play.setOnClickListener {
           // val action=MainFragmentDirections.actionMainFragmentToPhotoFragment()
           val action=MainFragmentDirections.actionMainFragmentToVideoFragment()
            Navigation.findNavController(requireView()).navigate(action)
            Toast.makeText(requireContext(),"imagebbutton clicked", Toast.LENGTH_LONG).show()
        }
        calendar.setOnClickListener {
            val cal = Calendar.getInstance()
            year_cal=cal[Calendar.YEAR]
            month_cal=cal[Calendar.MONTH]
            day_cal=cal[Calendar.DAY_OF_MONTH]


            var diag:DatePickerDialog?=null
            diag= DatePickerDialog(requireContext(),R.style.CustomDatePickerDialogTheme,datesetListener,year_cal,month_cal,day_cal)
            val tempDate1 = Calendar.getInstance()
            val tempDate2 = Calendar.getInstance()
            tempDate2.add(Calendar.YEAR, -25)
            tempDate1.add(Calendar.DAY_OF_MONTH,-1)
            diag.datePicker.minDate=tempDate2.timeInMillis
            diag.datePicker.maxDate=tempDate1.timeInMillis
            diag.show()
        }

        return view
    }
    val datesetListener =object : DatePickerDialog.OnDateSetListener{
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            year_cal=year
            month_cal=month
            day_cal=dayOfMonth
            dateSelected=year.toString()+"-"+(month+1)+"-"+dayOfMonth
            Toast.makeText(requireContext(),dateSelected,Toast.LENGTH_LONG).show()

        }
    }

}