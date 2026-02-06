package virtual.camera.app.compat

import android.view.View
import android.view.ViewGroup

/**
 * Replacement for StateView library
 * Simple state management for loading/empty/content states
 */
class StateView(private val contentView: View) {
    
    private var loadingView: View? = null
    private var emptyView: View? = null
    
    fun showLoading() {
        contentView.visibility = View.GONE
        emptyView?.visibility = View.GONE
        
        if (loadingView == null) {
            loadingView = createLoadingView()
            (contentView.parent as? ViewGroup)?.addView(loadingView)
        }
        loadingView?.visibility = View.VISIBLE
    }
    
    fun showContent() {
        loadingView?.visibility = View.GONE
        emptyView?.visibility = View.GONE
        contentView.visibility = View.VISIBLE
    }
    
    fun showEmpty() {
        loadingView?.visibility = View.GONE
        contentView.visibility = View.GONE
        
        if (emptyView == null) {
            emptyView = createEmptyView()
            (contentView.parent as? ViewGroup)?.addView(emptyView)
        }
        emptyView?.visibility = View.VISIBLE
    }
    
    private fun createLoadingView(): View {
        val loadingLayout = android.widget.LinearLayout(contentView.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            gravity = android.view.Gravity.CENTER
            orientation = android.widget.LinearLayout.VERTICAL
        }
        
        val progressBar = android.widget.ProgressBar(contentView.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        
        val textView = android.widget.TextView(contentView.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            text = "Loading..."
            setPadding(0, 16, 0, 0)
        }
        
        loadingLayout.addView(progressBar)
        loadingLayout.addView(textView)
        
        return loadingLayout
    }
    
    private fun createEmptyView(): View {
        val emptyLayout = android.widget.LinearLayout(contentView.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            gravity = android.view.Gravity.CENTER
            orientation = android.widget.LinearLayout.VERTICAL
        }
        
        val textView = android.widget.TextView(contentView.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            text = "No Data"
            textSize = 16f
            setTextColor(0xFF666666.toInt())
        }
        
        emptyLayout.addView(textView)
        
        return emptyLayout
    }
    
    companion object {
        fun wrap(view: View): StateView {
            return StateView(view)
        }
    }
}
