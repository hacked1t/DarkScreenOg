package com.example.darkscreen

import android.R.attr.alpha
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import androidx.core.graphics.ColorUtils
import android.os.Build
import android.util.Log.e
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import com.example.darkscreen.ScreenOverlayManager.overlayView

object ScreenOverlayManager {
    private var overlayView: View? = null
    private var windowManager: WindowManager? = null

    fun applyOverlay(context: Context, alpha: Float) {
        val applicationContext: Context = context.applicationContext
        if (windowManager == null) {
            windowManager =
                applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        }

        if (overlayView == null) {
            overlayView = FrameLayout(applicationContext).apply {
                setBackgroundColor(Color.BLACK)
            }

            val layoutFlag =
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    WindowManager.LayoutParams.TYPE_APPLICATION
                }
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                layoutFlag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
            )
            params.gravity = Gravity.TOP or Gravity.START
            params.alpha = alpha
            try {
                windowManager?.addView(overlayView, params)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            val params =
                overlayView?.layoutParams as? WindowManager.LayoutParams
            params?.let {
                it.alpha = alpha
                windowManager?.updateViewLayout(overlayView, it)
            }
        }
        val clampedAlpha: Float = alpha.coerceIn(0.0f, 1f)
        val backgroundColor: Int = ColorUtils.setAlphaComponent(
            android
                .graphics.Color.BLACK, (clampedAlpha * 255).toInt()
        )
        overlayView?.setBackgroundColor(backgroundColor)
    }

    fun removeOverlay(context: Context) {
        if (overlayView != null && windowManager != null) {
            try {
                windowManager?.removeView(overlayView)
                overlayView = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}