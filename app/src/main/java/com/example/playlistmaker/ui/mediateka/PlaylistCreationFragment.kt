package com.example.playlistmaker.ui.mediateka
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistCreationBinding
import com.example.playlistmaker.ui.mediateka.view_model.PlaylistCreationViewModel
import com.example.playlistmaker.ui.rootActivity.RootActivity
import com.example.playlistmaker.utils.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistCreationFragment : BindingFragment<FragmentPlaylistCreationBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistCreationBinding {
        return FragmentPlaylistCreationBinding.inflate(inflater, container, false)
    }
    private val viewModel by viewModel<PlaylistCreationViewModel>()
    private var playlistName = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backBtn.setOnClickListener {
            handleBackPress()
        }
        binding.createPlaylistBtn.isEnabled = false
        binding.playlistTitleEt.addTextChangedListener(
            onTextChanged = { charSequence, _, _, _ ->
                viewModel.setName(charSequence.toString())
                playlistName = charSequence.toString()
                binding.createPlaylistBtn.isEnabled = !charSequence.isNullOrBlank()
            })
        binding.playlistDescriptionEt.addTextChangedListener(
            onTextChanged = {charSequence, _, _, _ ->
                viewModel.setDescription(charSequence.toString())
            }
        )
        val imageUri = viewModel.requestUri()
        if(imageUri!=null){
            binding.playlistImageIv.setImageURI(imageUri)
        } else{
            binding.playlistImageIv.setImageResource(R.drawable.image_picker_background)
        }
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.playlistImageIv.setImageURI(uri)
                    viewModel.setUri(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                    binding.playlistImageIv.setImageResource(R.drawable.image_picker_background)
                }
            }
        binding.playlistImageIv.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.createPlaylistBtn.setOnClickListener {
            viewModel.createPlaylist()
            showCustomToast(playlistName)
            findNavController().popBackStack()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            handleBackPress()
        }
    }
    private fun showDialog(){
        AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
            .setTitle(getString(R.string.exit_playlist_creation))
            .setMessage(getString(R.string.all_unsaved_changes_will_be_lost))
            .setNeutralButton("Отмена"){ dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Завершить"){ dialog, which ->
                dialog.dismiss()
                findNavController().popBackStack()
            }.show()
    }
    private fun handleBackPress(){
        if (viewModel.checkUnsavedChanges()){
            showDialog()
        } else{
            findNavController().popBackStack()
        }
    }
    private fun showCustomToast(playlistName: String){
        val toastText = getString(R.string.playlist_name_created, playlistName)
        (requireActivity() as RootActivity).showFakeToast(toastText)
    }

}