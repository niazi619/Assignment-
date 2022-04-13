/*
 * Copyright 2021 Muhammad Fahad
 */
package com.tripscon.module.faalert.builders

import androidx.fragment.app.FragmentManager
import com.tripscon.module.faalert.fragments.DialogMessage
import com.tripscon.module.faalert.helper.FaAlert

class FaAlertMessage(private val manager: FragmentManager) {

    companion object {
        fun with(manager: FragmentManager): FaAlertMessage {
            return FaAlertMessage(manager)
        }
    }

    private var title: String = "Alert"
    private var message: String = ""
    private var cancelable: Boolean = false
    private var onDismissClickListener: (() -> Unit)? = null

    fun title(title: String) = apply { this.title = title }

    fun message(message: String) = apply { this.message = message }

    fun onDismissClickListener(onDismissClickListener: (() -> Unit)) =
        apply { this.onDismissClickListener = onDismissClickListener }

    fun show() {
        DialogMessage(
            title,
            message,
            cancelable,
            onDismissClickListener
        ).show(
            manager,
            FaAlert.TAG
        )
    }

}