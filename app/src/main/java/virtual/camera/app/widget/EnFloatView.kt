package virtual.camera.app.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import virtual.camera.app.compat.FloatingMagnetView
import virtual.camera.app.R

/**
 * Enhanced floating view
 */
class EnFloatView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FloatingMagnetView(context, attrs, defStyleAttr) {

    init {
        // Inflate layout
        inflate(context, R.layout.view_float_rocker, this)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Handle touch events
        return super.onTouchEvent(event)
    }
}
