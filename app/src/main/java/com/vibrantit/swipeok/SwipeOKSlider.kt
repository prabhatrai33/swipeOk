package com.vibrantit.swipeok

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import com.vibrantit.swipeok.databinding.ViewSwipeOkSliderBinding

class SwipeOKSlider @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding = ViewSwipeOkSliderBinding.inflate(layoutInflater(), this, true)

    private var onSlideComplete: (() -> Unit)? = null
    private var startX = 0f
    private var isSliding = false

    private fun layoutInflater() = android.view.LayoutInflater.from(context)

    init {
        setupSlider()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSlider() {
        val sliderHandle = binding.sliderHandle
        val track = binding.sliderTrack

        sliderHandle.setOnTouchListener { _, event ->
            val parentWidth = track.width - sliderHandle.width
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.rawX
                    isSliding = true
                }
                MotionEvent.ACTION_MOVE -> {
                    if (isSliding) {
                        val dx = (event.rawX - startX).toInt()
                        val newLeft = dx.coerceIn(0, parentWidth)
                        sliderHandle.updateLayoutParams<LayoutParams> {
                            leftMargin = newLeft
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    isSliding = false
                    if (sliderHandle.x >= parentWidth * 0.9f) {
                        onSlideComplete?.invoke()
                    }
                    resetSlider()
                }
            }
            true
        }
    }

    private fun resetSlider() {
        binding.sliderHandle.animate().x(0f).setDuration(300).start()
    }

    fun setOnSlideCompleteListener(callback: () -> Unit) {
        onSlideComplete = callback
    }

    fun setSliderText(text: String) {
        binding.sliderText.text = text
    }
}
