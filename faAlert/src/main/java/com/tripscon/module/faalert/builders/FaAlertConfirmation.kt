/*
 * Copyright 2021 Muhammad Fahad
 */

package com.tripscon.module.faalert.builders

import androidx.fragment.app.FragmentManager
import com.tripscon.module.faalert.fragments.DialogConfirmation
import com.tripscon.module.faalert.helper.FaAlert

class FaAlertConfirmation(private val manager: FragmentManager) {

    companion object {
        fun with(manager: FragmentManager): FaAlertConfirmation {
            return FaAlertConfirmation(manager)
        }
    }

    private var title: String = "Alert"
    private var message: String = ""
    private var cancelable: Boolean = false
    private var buttonConfirmTitle: String = "Confirm"
    private var buttonCancelTitle: String = "Cancel"
    private var onConfirmClickListener: (() -> Unit)? = null
    private var onCancelClickListener: (() -> Unit)? = null

    fun title(title: String) = apply { this.title = title }

    fun message(message: String) = apply { this.message = message }

    fun titleConfirmButton(titleConfirmButton: String) =
        apply { this.buttonConfirmTitle = titleConfirmButton }

    fun titleCancelButton(titleCancelButton: String) =
        apply { this.buttonCancelTitle = titleCancelButton }

    fun cancelable(cancelable: Boolean) = apply { this.cancelable = cancelable }

    fun onConfirmClickListener(onConfirmClickListener: (() -> Unit)) =
        apply { this.onConfirmClickListener = onConfirmClickListener }

    fun onCancelClickListener(onCancelClickListener: (() -> Unit)) =
        apply { this.onCancelClickListener = onCancelClickListener }

    fun show() {
        DialogConfirmation(
            title,
            message,
            cancelable,
            buttonConfirmTitle,
            buttonCancelTitle,
            onConfirmClickListener,
            onCancelClickListener,
        ).show(
            manager,
            FaAlert.TAG
        )
    }

}