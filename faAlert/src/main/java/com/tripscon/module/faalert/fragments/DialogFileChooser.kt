package com.tripscon.module.faalert.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.tripscon.module.faalert.databinding.DialogMediaChooserBinding

class DialogFileChooser(
    private val title: String? = "Choose",
    private val cancelable: Boolean,
    private val titleCamera: String,
    private val titleGallery: String,
    private val onCameraClickListener: (() -> Unit)? = null,
    private val onGalleryClickListener: (() -> Unit)? = null,
    private val onDismissClickListener: (() -> Unit)? = null,
) : DialogFragment() {

    private var _binding: DialogMediaChooserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogMediaChooserBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireContext())

        title?.let { binding.textViewTitle.text = it } ?: run {
            binding.textViewTitle.visibility = View.GONE
        }
        binding.buttonCamera.text = titleCamera
        binding.buttonGallery.text = titleGallery
        binding.imageViewClose.setOnClickListener {
            dismiss()
            onDismissClickListener?.invoke()
        }
        binding.buttonCamera.setOnClickListener {
            dismiss()
            onCameraClickListener?.invoke()
        }
        binding.buttonGallery.setOnClickListener {
            dismiss()
            onGalleryClickListener?.invoke()
        }
        builder.setView(binding.root)
        return builder.create()
            .apply {
                setCanceledOnTouchOutside(cancelable)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}