package virtual.camera.app.compat

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

/**
 * Replacement for roger.catloadinglibrary.CatLoadingView
 * Simple loading dialog
 */
class CatLoadingView {
    
    companion object {
        fun show(context: Context, message: String = "Loading..."): android.app.Dialog {
            val dialog = android.app.Dialog(context)
            dialog.setCancelable(false)
            
            val layout = LinearLayout(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.VERTICAL
                gravity = android.view.Gravity.CENTER
                setPadding(32, 32, 32, 32)
            }
            
            val progressBar = ProgressBar(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            
            val textView = TextView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                text = message
                setPadding(0, 16, 0, 0)
                textSize = 14f
            }
            
            layout.addView(progressBar)
            layout.addView(textView)
            
            dialog.setContentView(layout)
            dialog.window?.setBackgroundDrawableResource(android.R.color.white)
            dialog.show()
            
            return dialog
        }
    }
}
