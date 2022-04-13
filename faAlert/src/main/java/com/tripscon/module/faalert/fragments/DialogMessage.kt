/*
 * Copyright 2021 Muhammad Fahad
 */

package com.tripscon.module.faalert.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.tripscon.module.faalert.databinding.DialogMessageBinding

class DialogMessage(
    private val title: String? = "Alert",
    private val message: String,
    private val cancelable: Boolean,
    private val onDismissClickListener: (() -> Unit)? = null,
) : DialogFragment() {

    private var _binding: DialogMessageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogMessageBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireContext())

        title?.let { binding.textViewTitle.text = it } ?: run {
            binding.textViewTitle.visibility = View.GONE
        }
        binding.textViewMessage.text = message
        binding.buttonDismiss.setOnClickListener {
            dismiss()
            onDismissClickListener?.invoke()
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