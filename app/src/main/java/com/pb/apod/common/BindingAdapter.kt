package com.pb.apod.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    if(imageUrl!=null) {
        //val url: String
        // url = "https://www.tripmaza.com/Images/Airlinepng/" + imageUrl + ".png"
        Glide.with(view.context)
            .load(imageUrl)
            .into(view)
    }
}
/*
@BindingAdapter("photoViewUrl")
fun loadPhotoView(view: PhotoView, imageUrl: String?) {
    if(imageUrl!=null) {
        //val url: String
        // url = "https://www.tripmaza.com/Images/Airlinepng/" + imageUrl + ".png"
        Glide.with(view.context)
            .load(imageUrl)
            .into(view)
    }
}*/
