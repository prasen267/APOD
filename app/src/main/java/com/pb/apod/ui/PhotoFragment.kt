package com.pb.apod.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.pb.apod.R
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class PhotoFragment : Fragment(),KodeinAware {

    override val kodein by kodein()
    private val factory: ApodViewModelFactory by instance<ApodViewModelFactory>()
    private lateinit var photo_view:PhotoView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_photo, container, false)
        photo_view=view.findViewById(R.id.photo_view)
        val apodViewModel= ViewModelProviders.of(this,factory).get(ApodViewModel::class.java)
       apodViewModel.apod.observe(viewLifecycleOwner, Observer { state->
           Glide.with(this)
               .load(state.hdurl)
               .into(photo_view)
       })
        return view
    }


}