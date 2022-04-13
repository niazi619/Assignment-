package com.wanologicalsolutions.apps.bankadmin.helpers.alert

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tripscon.module.faalert.builders.FaAlertConfirmation
import com.tripscon.module.faalert.builders.FaAlertMessage

object Alert {

    fun show(
        manager: FragmentManager,
        message: String,
        primaryButton: String = "Dismiss",
        onDismissClickListener: (() -> Unit)? = null
    ) {
        FaAlertMessage.with(manager)
            .message(message)
            .onDismissClickListener {
                onDismissClickListener?.invoke()
            }
            .show()
    }

    fun confirm(
        manager: FragmentManager,
        message: String,
        primaryButton: String = "Yes",
        seconderyButton: String = "Dismiss",
        onPrimaryClickListener: (() -> Unit)? = null,
        onSeconderyClickListener: (() -> Unit)? = null,
    ) {
        FaAlertConfirmation.with(manager)
            .title("Alert")
            .message(message)
            .titleCancelButton(seconderyButton)
            .titleConfirmButton(primaryButton)
            .onConfirmClickListener {
                onPrimaryClickListener?.invoke()
            }
            .onCancelClickListener {
                onSeconderyClickListener?.invoke()
            }
            .show()
    }
}

fun AppCompatActivity.messageAlert(
    message: String,
    primaryButton: String = "Dismiss",
    onDismissClickListener: (() -> Unit)? = null
) {
    FaAlertMessage.with(supportFragmentManager)
        .message(message)
        .onDismissClickListener {
            onDismissClickListener?.invoke()
        }
        .show()
}

fun Fragment.messageAlert(
    message: String,
    primaryButton: String = "Dismiss",
    onDismissClickListener: (() -> Unit)? = null
) {
    FaAlertMessage.with(childFragmentManager)
        .message(message)
        .onDismissClickListener {
            onDismissClickListener?.invoke()
        }
        .show()
}

fun AppCompatActivity.confirmAlert(
    message: String,
    primaryButton: String = "Yes",
    seconderyButton: String = "Dismiss",
    onPrimaryClickListener: (() -> Unit)? = null,
    onSeconderyClickListener: (() -> Unit)? = null,
) {
    FaAlertConfirmation.with(supportFragmentManager)
        .title("Alert")
        .message(message)
        .titleCancelButton(seconderyButton)
        .titleConfirmButton(primaryButton)
        .onConfirmClickListener {
            onPrimaryClickListener?.invoke()
        }
        .onCancelClickListener {
            onSeconderyClickListener?.invoke()
        }
        .show()
}

fun Fragment.confirmAlert(
    title: String = "Alert",
    message: String,
    primaryButton: String = "Yes",
    secondaryButton: String = "Dismiss",
    onPrimaryClickListener: (() -> Unit)? = null,
    onSecondaryClickListener: (() -> Unit)? = null,
) {
    FaAlertConfirmation.with(childFragmentManager)
        .title(title)
        .message(message)
        .titleCancelButton(secondaryButton)
        .titleConfirmButton(primaryButton)
        .onConfirmClickListener {
            onPrimaryClickListener?.invoke()
        }
        .onCancelClickListener {
            onSecondaryClickListener?.invoke()
        }
        .show()
}