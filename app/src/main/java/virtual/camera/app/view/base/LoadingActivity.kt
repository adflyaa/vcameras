package virtual.camera.app.view.base

import android.app.Dialog
import android.os.Bundle
import virtual.camera.app.compat.CatLoadingView

/**
 * Base activity with loading dialog support
 */
abstract class LoadingActivity : BaseActivity() {

    private var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun showLoading(message: String = "Loading...") {
        hideLoading()
        loadingDialog = CatLoadingView.show(this, message)
    }

    fun hideLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    override fun onDestroy() {
        super.onDestroy()
        hideLoading()
    }
}
