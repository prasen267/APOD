package com.pb.apod.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.pb.apod.R
import com.pb.apod.common.EspressoIdlingResource
import com.pb.apod.common.extractYoutubeVideoId
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.regex.Pattern


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class VideoFragment : Fragment(),KodeinAware {
    override val kodein by kodein()
    private val factory: ApodViewModelFactory by instance<ApodViewModelFactory>()
    private lateinit var apodViewModel: ApodViewModel
    private lateinit var playerView: PlayerView
    private var player: SimpleExoPlayer? = null
    private lateinit var playbackStateListener: PlaybackStateListener
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var uri: Uri? = null
    private var mediaSource: MediaSource? = null
    private val STATE_RESUME_WINDOW = "resumeWindow"
    private val STATE_RESUME_POSITION = "resumePosition"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_video, container, false)
        apodViewModel = ViewModelProviders.of(requireActivity(), factory).get(ApodViewModel::class.java)
        if(savedInstanceState!=null){
            playbackPosition = savedInstanceState.getLong(STATE_RESUME_POSITION, 0L)
            currentWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW, 0)
        }
        playerView = view.findViewById(R.id.video_view)

        playbackStateListener = PlaybackStateListener()
        //initializePlayer()
        return view
    }

    private fun initializePlayer(uri: Uri) {
        EspressoIdlingResource.increment()
        if (player == null) {
            val trackSelector = DefaultTrackSelector()
            trackSelector.setParameters(
                trackSelector.buildUponParameters().setMaxVideoSizeSd()
            )
            player = ExoPlayerFactory.newSimpleInstance(requireContext())

        }
        playerView.player = player

        mediaSource = buildMediaSource(uri)


        player!!.setPlayWhenReady(playWhenReady)
        player!!.seekTo(currentWindow, playbackPosition)
        player!!.addListener(playbackStateListener)
        player!!.prepare(mediaSource, false, false)
        EspressoIdlingResource.decrement()
    }

    override fun onSaveInstanceState(outState: Bundle) {

        super.onSaveInstanceState(outState)
        outState.putInt(STATE_RESUME_WINDOW, currentWindow)
        outState.putLong(STATE_RESUME_POSITION, playbackPosition)

        super.onSaveInstanceState(outState)
    }



    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory = DefaultDataSourceFactory(requireContext(), "APOD")
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            extractYoutubeUrl()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT < 24 || player == null) {
            extractYoutubeUrl()
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        playerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun extractYoutubeUrl() {
        @SuppressLint("StaticFieldLeak") val mExtractor: YouTubeExtractor =
            object : YouTubeExtractor(requireContext()) {
                override fun onExtractionComplete(
                    sparseArray: SparseArray<YtFile>?,
                    videoMeta: VideoMeta
                ) {
                    if (sparseArray != null) {
                        uri = Uri.parse(sparseArray[22].url)
                        initializePlayer(uri!!)
                    }
                    Log.e("APOD", sparseArray.toString())
                }
            }
        val videoId = extractYoutubeVideoId(apodViewModel.apod.value!!.url)
        val BASE_URL = "https://www.youtube.com"
        val mYoutubeLink = BASE_URL + "/watch?v=" + videoId
        mExtractor.extract(mYoutubeLink, true, true)


    }




    private fun releasePlayer() {

        playWhenReady = player!!.playWhenReady
        playbackPosition = player!!.currentPosition
        currentWindow = player!!.currentWindowIndex
        player!!.release()
        player = null
    }

    private class PlaybackStateListener : Player.EventListener {
        override fun onPlayerStateChanged(
            playWhenReady: Boolean,
            playbackState: Int
        ) {
            val stateString: String
            stateString = when (playbackState) {
                ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
                ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING -"
                ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY     -"
                ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED     -"
                else -> "UNKNOWN_STATE             -"
            }
            Log.d(
                "APOD", "changed state to " + stateString
                        + " playWhenReady: " + playWhenReady
            )
        }
    }
}


