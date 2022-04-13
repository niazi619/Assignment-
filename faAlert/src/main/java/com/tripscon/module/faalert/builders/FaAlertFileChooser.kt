/*
 * Copyright 2021 Muhammad Fahad
 */

package com.tripscon.module.faalert.builders

import androidx.fragment.app.FragmentManager
import com.tripscon.module.faalert.helper.FaAlert
import com.tripscon.module.faalert.fragments.DialogFileChooser

class FaAlertFileChooser(private val manager: FragmentManager) {

    companion object {
        fun with(manager: FragmentManager): FaAlertFileChooser {
            return FaAlertFileChooser(manager)
        }
    }

    private var title: String = "Choose"
    private var cancelable: Boolean = false
    private var titleCamera: String = "Camera"
    private var titleGallery: String = "Gallery"
    private var onCameraClickListener: (() -> Unit)? = null
    private var onGalleryClickListener: (() -> Unit)? = null
    private var onDismissClickListener: (() -> Unit)? = null

    fun title(title: String) = apply { this.title = title }

    fun titleCameraButton(titleCamera: String) = apply { this.titleCamera = titleCamera }

    fun titleGalleryButton(titleGallery: String) = apply { this.titleGallery = titleGallery }

    fun cancelable(cancelable: Boolean) = apply { this.cancelable = cancelable }

    fun onCameraClickListener(onCameraClickListener: (() -> Unit)) =
        apply { this.onCameraClickListener = onCameraClickListener }

    fun onGalleryClickListener(onGalleryClickListener: (() -> Unit)) =
        apply { this.onGalleryClickListener = onGalleryClickListener }

    fun onDismissClickListener(onDismissClickListener: (() -> Unit)) =
        apply { this.onDismissClickListener = onDismissClickListener }

    fun show() {
        DialogFileChooser(
            title,
            cancelable,
            titleCamera,
            titleGallery,
            onCameraClickListener,
            onGalleryClickListener,
            onDismissClickListener
        ).show(
            manager,
            FaAlert.TAG
        )
    }

}