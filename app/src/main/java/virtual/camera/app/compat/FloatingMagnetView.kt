package virtual.camera.app.compat

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

/**
 * Replacement for com.imuxuan.floatingview.FloatingMagnetView
 * Simple draggable floating view
 */
open class FloatingMagnetView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    private var downX = 0f
    private var downY = 0f
    private var lastX = 0f
    private var lastY = 0f
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.rawX
                downY = event.rawY
                lastX = x
                lastY = y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.rawX - downX
                val dy = event.rawY - downY
                x = lastX + dx
                y = lastY + dy
                return true
            }
            MotionEvent.ACTION_UP -> {
                // Magnetic effect - snap to edge
                val screenWidth = resources.displayMetrics.widthPixels
                if (x + width / 2 < screenWidth / 2) {
                    // Snap to left
                    animate().x(0f).setDuration(200).start()
                } else {
                    // Snap to right
                    animate().x((screenWidth - width).toFloat()).setDuration(200).start()
                }
                return performClick()
            }
        }
        return super.onTouchEvent(event)
    }
    
    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
