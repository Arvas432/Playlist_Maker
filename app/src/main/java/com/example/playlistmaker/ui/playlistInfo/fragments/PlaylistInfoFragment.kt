package com.example.playlistmaker.ui.playlistInfo.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.mediateka.fragments.PlaylistsFragment
import com.example.playlistmaker.ui.playlistInfo.adapters.PlaylistTracksAdapter
import com.example.playlistmaker.ui.playlistInfo.states.PlaylistInfoStates
import com.example.playlistmaker.ui.playlistInfo.viewmodel.PlaylistInfoViewModel
import com.example.playlistmaker.ui.rootActivity.RootActivity
import com.example.playlistmaker.utils.BindingFragment
import com.example.playlistmaker.utils.MinutesMapper
import com.example.playlistmaker.utils.TrackCountFormatter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistInfoFragment : BindingFragment<FragmentPlaylistInfoBinding>() {
    private val viewModel: PlaylistInfoViewModel by viewModel<PlaylistInfoViewModel>()
    private var tracks = ArrayList<Track>()
    private lateinit var trackListAdapter: PlaylistTracksAdapter
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private lateinit var moreMenuBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var clickAllowed = true
    private var shareMessage = ""
    private var playlistId = -1L
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistInfoBinding {
        return FragmentPlaylistInfoBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapters()
        viewModel.getScreenStateLiveData().observe(viewLifecycleOwner) {
            renderState(it)
        }
        viewModel.getShareTextLiveData().observe(viewLifecycleOwner) {
            shareMessage = it
        }
        viewModel.getPlaylistDeletionLiveData().observe(viewLifecycleOwner){
            if(it){
                findNavController().navigateUp()
            }
        }
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.playlistsBottomSheet.post {
            val displayPixelHeight = requireActivity().resources.displayMetrics.heightPixels
            Log.i("display", displayPixelHeight.toString())
            val constraintLayoutHeight = binding.playlistLayout.measuredHeight
            Log.i("layout", constraintLayoutHeight.toString())
            val bottomSheetPeekHeight = displayPixelHeight - constraintLayoutHeight
            Log.i("peek", bottomSheetPeekHeight.toString())
            bottomSheetBehavior.setPeekHeight(
                (bottomSheetPeekHeight - 50 * requireContext().resources.displayMetrics.density).toInt(),
                false
            )
        }
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        playlistId = requireArguments().getLong(PlaylistsFragment.PLAYLIST_ID_KEY)
        viewModel.requestPlaylistInfo(playlistId)
        moreMenuBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomMenuLayout).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        moreMenuBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }

                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + 1
            }
        })
        binding.moreButton.setOnClickListener {
            moreMenuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.shareButton.setOnClickListener {
            sharePlaylist()
        }
        binding.shareTv.setOnClickListener {
            sharePlaylist()
        }
        binding.deleteTv.setOnClickListener {
            moreMenuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            deletePlaylistSequence()
        }
        binding.editTv.setOnClickListener {
            findNavController().navigate(R.id.action_playlistInfoFragment_to_playlistEditingFragment, bundleOf(
                PLAYLIST_EDIT_KEY to playlistId))
        }
    }
    override fun onPause() {
        super.onPause()
        clickAllowed = true
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestPlaylistInfo(playlistId)
    }

    private fun setUpAdapters() {
        onTrackClickDebounce = { track ->
            run {
                if (clickDebounce()) {
                    findNavController().navigate(
                        R.id.action_playlistInfoFragment_to_playerFragment,
                        bundleOf(TRACK_PLAYER_KEY to Gson().toJson(track))
                    )
                }
            }
        }
        trackListAdapter = PlaylistTracksAdapter(
            tracks,
            { track -> onTrackClickDebounce(track) },
            { track -> deleteTrackSequence(track) })
        binding.playlistTracksRv.adapter = trackListAdapter
    }

    private fun deletePlaylistSequence(){
        AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
            .setTitle(getString(R.string.delete_playlist_other))
            .setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_playlist))
            .setNeutralButton(getString(R.string.cancel)) { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.delete)) { dialog, which ->
                dialog.dismiss()
                viewModel.deletePlaylist(playlistId)
            }.show()
    }
    private fun clickDebounce(): Boolean {
        val current = clickAllowed
        if (clickAllowed) {
            clickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                clickAllowed = true
                Log.i("DEBOUNCE", "CLICK ALLOWED")
            }
        }
        return current
    }

    private fun deleteTrackSequence(track: Track) {
        AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
            .setTitle(getString(R.string.delete_track))
            .setMessage(getString(R.string.are_you_sure_you_want_to_delete_track_from_playlist))
            .setNeutralButton(getString(R.string.cancel)) { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.delete)) { dialog, which ->
                dialog.dismiss()
                viewModel.deleteTrackFromPlaylist(playlistId, track.trackId)
            }.show()
    }

    private fun sharePlaylist() {
        if (tracks.isNotEmpty()) {
            Log.i("tracks", tracks.toString())
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            shareIntent.type = "text/plain"
            startActivity(shareIntent)
        } else {
            moreMenuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            (requireActivity() as RootActivity).showFakeToast(getString(R.string.playlist_track_list_is_empty))
        }

    }

    private fun setPlaylistInfoState(
        playlist: Playlist,
        results: List<Track>,
        formattedLengthSum: Int
    ) {
        if (playlist.imageFileUri == null) {
            binding.playlistIv.setImageResource(R.drawable.placeholder)
            binding.bottomPlaylistIv.setImageResource(R.drawable.placeholder)
        } else {
            binding.playlistIv.setImageURI(playlist.imageFileUri)
            binding.bottomPlaylistIv.setImageURI(playlist.imageFileUri)
        }
        binding.playlistTracksRv.isVisible = true
        binding.errorText.isVisible = false
        binding.playlistTitleTv.text = playlist.name
        binding.bottomPlaylistTitleTv.text = playlist.name
        binding.playlistDescriptionTv.text = playlist.description

        val minutesMapper = MinutesMapper(requireContext())
        binding.playlistLengthTv.text = minutesMapper.formatMinutes(formattedLengthSum)
        val trackFormatter = TrackCountFormatter(requireContext())
        binding.playlistTrackCountTv.text = trackFormatter.formatTrackCount(playlist.trackCount)
        binding.bottomPlaylistTrackCountTv.text = trackFormatter.formatTrackCount(playlist.trackCount)
        tracks.clear()
        tracks.addAll(results)
        trackListAdapter.notifyDataSetChanged()

    }

    private fun setEmptyPlaylistState(playlist: Playlist) {
        if (playlist.imageFileUri == null) {
            binding.playlistIv.setImageResource(R.drawable.placeholder)
            binding.bottomPlaylistIv.setImageResource(R.drawable.placeholder)
        } else {
            binding.playlistIv.setImageURI(playlist.imageFileUri)
            binding.bottomPlaylistIv.setImageURI(playlist.imageFileUri)
        }
        binding.playlistTracksRv.isVisible = false
        binding.errorText.isVisible = true
        binding.playlistTitleTv.text = playlist.name
        binding.bottomPlaylistTitleTv.text = playlist.name
        binding.playlistDescriptionTv.text = playlist.description
        val minutesMapper = MinutesMapper(requireContext())
        binding.playlistLengthTv.text = minutesMapper.formatMinutes(0)
        val trackFormatter = TrackCountFormatter(requireContext())
        binding.bottomPlaylistTrackCountTv.text = trackFormatter.formatTrackCount(playlist.trackCount)
        binding.playlistTrackCountTv.text = trackFormatter.formatTrackCount(playlist.trackCount)
        tracks.clear()
        trackListAdapter.notifyDataSetChanged()
    }

    private fun renderState(state: PlaylistInfoStates) {
        when (state) {
            is PlaylistInfoStates.Default -> Unit
            is PlaylistInfoStates.PlaylistContent -> {
                setPlaylistInfoState(
                    state.playlistObject,
                    state.tracks,
                    state.tracksLengthSum
                )
                viewModel.requestPlaylistShareText(state.playlistObject, state.tracks)
            }

            is PlaylistInfoStates.EmptyPlaylist -> setEmptyPlaylistState(state.playlistObject)
        }
    }

    companion object {
        const val TRACK_PLAYER_KEY = "TRACK_PLAYER_KEY"
        const val PLAYLIST_EDIT_KEY = "PLAYLIST_EDIT_KEY"
        private const val CLICK_DEBOUNCE_DELAY = 1000L

    }
}