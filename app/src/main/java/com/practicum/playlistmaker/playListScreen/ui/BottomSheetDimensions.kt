package com.practicum.playlistmaker.playListScreen.ui

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomSheetDimensions(private val activity: Activity?) {

    fun setupBottomSheetHeightForDialog(
        container: LinearLayout,
        percentage: Float,
    ) {
        val bottomSheetBehavior = BottomSheetBehavior
            .from(container)
            .apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
            }

        val screenHeight = getWindowHeight()
        val bottomSheetHeight = (screenHeight * percentage).toInt()

        bottomSheetBehavior.peekHeight = bottomSheetHeight
        bottomSheetBehavior.isFitToContents = false
        bottomSheetBehavior.isHideable = true

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    fun setupBottomSheetHeightForDialogFragment(
        bottomSheetDialog: BottomSheetDialog,
        percentage: Float,
    ) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout

        val bottomSheetBehavior: BottomSheetBehavior<FrameLayout> = BottomSheetBehavior
            .from(bottomSheet)
            .apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
            }

        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = getWindowHeight()
        bottomSheet.layoutParams = layoutParams

        val bottomSheetHeight = (layoutParams.height * percentage).toInt()

        bottomSheetBehavior.peekHeight = bottomSheetHeight
        bottomSheetBehavior.isFitToContents = false
        bottomSheetBehavior.isHideable = true

    }

    private fun getWindowHeight(): Int {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowManager = activity?.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
            val windowMetrics = windowManager?.currentWindowMetrics
            val insets = windowMetrics?.windowInsets?.getInsetsIgnoringVisibility(
                WindowInsets.Type.systemBars()
            )
            val height = windowMetrics?.bounds?.height() ?: 0
            val insetsHeight = (insets?.top ?: 0) + (insets?.bottom ?: 0)
            height - insetsHeight
        } else {
            val displayMetrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            displayMetrics.heightPixels
        }
    }
}