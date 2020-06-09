package com.pb.apod.ui

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.github.chrisbanes.photoview.PhotoView
import com.pb.apod.R
import com.pb.apod.common.EspressoIdlingResource
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
        val apodViewModel= ViewModelProviders.of(requireActivity(),factory).get(ApodViewModel::class.java)

        //Glide properties
        val reqOpt = RequestOptions
            .fitCenterTransform()
            .diskCacheStrategy(DiskCacheStrategy.ALL) // It will cache your image after loaded for first time
            .override(
                photo_view.getWidth(),
                photo_view.getHeight()
            ) // Overrides size of downloaded image and converts it's bitmaps to your desired image size;
        val circularProgressDrawable = CircularProgressDrawable(requireContext())
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        apodViewModel.apod.observe(viewLifecycleOwner, Observer { state->
            EspressoIdlingResource.increment()
           Glide.with(this)
               .asBitmap()
               .apply(reqOpt)
               .placeholder(circularProgressDrawable)
               .load(state.hdurl)
               .listener(object : RequestListener<Bitmap> {
                   override fun onLoadFailed(
                       e: GlideException?,
                       model: Any?,
                       target: Target<Bitmap>?,
                       isFirstResource: Boolean
                   ): Boolean {
                       TODO("Not yet implemented")
                   }

                   override fun onResourceReady(
                       resource: Bitmap?,
                       model: Any?,
                       target: Target<Bitmap>?,
                       dataSource: DataSource?,
                       isFirstResource: Boolean
                   ): Boolean {
                       EspressoIdlingResource.decrement()
                       return false
                   }

               })
               .into(photo_view)
       })
        return view
    }


}