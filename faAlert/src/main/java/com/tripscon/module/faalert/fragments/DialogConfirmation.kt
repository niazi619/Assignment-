/*
 * Copyright 2021 Muhammad Fahad
 */

package com.tripscon.module.faalert.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.tripscon.module.faalert.databinding.DialogConfirmationBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogConfirmation(
    private val title: String? = "Alert",
    private val message: String,
    private val cancelable: Boolean,
    private val buttonConfirmTitle: String = "Confirm",
    private val buttonCancelTitle: String = "Cancel",
    private val onConfirmClickListener: (() -> Unit)? = null,
    private val onCancelClickListener: (() -> Unit)? = null,
) : DialogFragment() {

    private var _binding: DialogConfirmationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogConfirmationBinding.inflate(layoutInflater)

        val builder = MaterialAlertDialogBuilder(requireContext())

        title?.let { binding.textViewTitle.text = it } ?: run {
            binding.textViewTitle.visibility = View.GONE
        }
        binding.textViewMessage.text = message
        binding.buttonConfirm.text = buttonConfirmTitle
        binding.buttonCancel.text = buttonCancelTitle
        binding.buttonConfirm.setOnClickListener {
            dismiss()
            onConfirmClickListener?.invoke()
        }
        binding.buttonCancel.setOnClickListener {
            dismiss()
            onCancelClickListener?.invoke()
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