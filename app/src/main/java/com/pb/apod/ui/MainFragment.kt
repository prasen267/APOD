package com.pb.apod.ui

import android.app.DatePickerDialog
import android.icu.text.CaseMap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.pb.apod.R
import com.pb.apod.common.NetworkState
import com.pb.apod.common.extractYoutubeVideoId
import com.pb.apod.data.model.ApodResponse
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*

class MainFragment : Fragment(), KodeinAware {
    override val kodein by kodein()
    private val factory: ApodViewModelFactory by instance<ApodViewModelFactory>()
    private lateinit var header_image: ImageView
    private lateinit var tv_title: TextView
    private lateinit var tv_explanation: TextView
    private lateinit var btn_play: ImageButton
    private lateinit var calendar: ImageButton
    private lateinit var dateSelected: String
    private lateinit var progressBar: ProgressBar
    private lateinit var tv_error: TextView
    private lateinit var apodViewModel: ApodViewModel
    private lateinit var title_layout:LinearLayout
    private var isVideo: Boolean = false
    var year_cal = -1
    var month_cal = -1
    var day_cal = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        setup(view)


        apodViewModel = ViewModelProviders.of(requireActivity(), factory).get(ApodViewModel::class.java)

        // binding.apodModel=apodViewModel
        apodViewModel.apod.observe(viewLifecycleOwner, Observer { state ->




            if (state.mediaType.equals("video")) {
                btn_play.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_play_arrow
                    )
                )
                val baseUrl =
                    "https://img.youtube.com/vi/" + extractYoutubeVideoId(state.url) + "/hqdefault.jpg"
               loadImage(baseUrl)

                isVideo = true
            } else {
                btn_play.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_zoom_out
                    )
                )
                loadImage(state.url)
            }

            tv_title.text = state.title
            tv_explanation.text = state.explanation
        })
        //if an error occurs a toast is displayed informing about the error
        apodViewModel.errorState.observe(viewLifecycleOwner, Observer {
            message->
           Toast.makeText(requireContext(),message,Toast.LENGTH_LONG).show()
        })

        //checks the status of network and display appropriate prompt (LOADING,SUCCESS,ERROR)
        apodViewModel.networkState.observe(viewLifecycleOwner, Observer {
            
            if (it == NetworkState.LOADING) {
                progressBar.visibility = View.VISIBLE
                title_layout.visibility=View.GONE

            }
            if (it == NetworkState.ERROR) {
                progressBar.visibility = View.GONE
                tv_error.visibility = View.VISIBLE
                title_layout.visibility=View.GONE
            }
            if(it==NetworkState.LOADED) {
                progressBar.visibility = View.GONE
                tv_error.visibility = View.GONE
                title_layout.visibility=View.VISIBLE
            }
        })
        /*Based on the Media type of the APod ,this action will take us to the appropriate fragment on click. */
        btn_play.setOnClickListener {
            if (isVideo) {
                val action = MainFragmentDirections.actionMainFragmentToVideoFragment()
                Navigation.findNavController(requireView()).navigate(action)
            } else {
                val action = MainFragmentDirections.actionMainFragmentToPhotoFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }
        }
        calendar.setOnClickListener {
            val cal = Calendar.getInstance()
            year_cal = cal[Calendar.YEAR]
            month_cal = cal[Calendar.MONTH]
            day_cal = cal[Calendar.DAY_OF_MONTH]



           val diag = DatePickerDialog(
                requireContext(),
                R.style.CustomDatePickerDialogTheme,
                datesetListener,
                year_cal,
                month_cal,
                day_cal
            )
            val tempDate1 = Calendar.getInstance()
            val tempDate2 = Calendar.getInstance()
            tempDate2.add(Calendar.YEAR, -25)
            tempDate1.add(Calendar.DAY_OF_MONTH, -1)
            diag.datePicker.minDate = tempDate2.timeInMillis
            diag.datePicker.maxDate = tempDate1.timeInMillis
            diag.show()
        }

        return view
    }

    private fun loadImage(state:String ) {

        //Glide properties
        val reqOpt = RequestOptions
            .fitCenterTransform()
            .diskCacheStrategy(DiskCacheStrategy.ALL) // It will cache your image after loaded for first time
            .override(
                header_image.getWidth(),
                header_image.getHeight()
            ) // Overrides size of downloaded image and converts it's bitmaps to your desired image size;

        val circularProgressDrawable = CircularProgressDrawable(requireContext())
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(this)
            .load(state)
            .apply(reqOpt)
            .placeholder(circularProgressDrawable)
            .into(header_image)
    }

    private fun setup(view: View) {
        header_image = view.findViewById(R.id.header_image)
        tv_title = view.findViewById(R.id.tv_title)
        tv_explanation = view.findViewById(R.id.tv_explanation)
        btn_play = view.findViewById(R.id.btn_play)
        calendar = view.findViewById(R.id.iv_calender)
        progressBar = view.findViewById(R.id.progressbar_main)
        tv_error = view.findViewById(R.id.tv_error)
        title_layout=view.findViewById(R.id.description_layout)
    }

    val datesetListener = object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            year_cal = year
            month_cal = month
            day_cal = dayOfMonth
            dateSelected = year.toString() + "-" + (month + 1) + "-" + dayOfMonth
            apodViewModel.getApodByDate(dateSelected)

        }
    }


}