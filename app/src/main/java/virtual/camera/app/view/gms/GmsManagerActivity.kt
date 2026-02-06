package virtual.camera.app.view.gms

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Switch
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import virtual.camera.app.compat.RVAdapter
import com.afollestad.materialdialogs.MaterialDialog
import virtual.camera.app.R
import virtual.camera.app.bean.GmsBean
import virtual.camera.app.databinding.ActivityGmsBinding
import virtual.camera.app.util.InjectionUtil
import virtual.camera.app.util.inflate
import virtual.camera.app.util.toast
import virtual.camera.app.view.base.LoadingActivity

class GmsManagerActivity : LoadingActivity() {

    private lateinit var viewModel: GmsViewModel

    private lateinit var mAdapter: GmsAdapter

    private val viewBinding: ActivityGmsBinding by inflate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        initToolbar(viewBinding.toolbarLayout.toolbar, R.string.gms_manager, true)
        initViewModel()

        initRecyclerView()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, InjectionUtil.getGmsFactory())[GmsViewModel::class.java]
        showLoading()

        viewModel.mInstalledLiveData.observe(this) {
            hideLoading()
            mAdapter.setData(it)
        }

        viewModel.mUpdateInstalledLiveData.observe(this) { result ->
            if (result == null) {
                return@observe
            }

            val items = mAdapter.getData().toMutableList()
            for (index in items.indices) {
                val bean = items[index]
                if (bean.userID == result.userID) {
                    if (result.success) {
                        bean.isInstalledGms = !bean.isInstalledGms
                    }
                    items[index] = bean
                    break
                }
            }
            mAdapter.setData(items)

            hideLoading()

            if (result.success) {
                toast(result.msg)
            } else {
                MaterialDialog(this).show {
                    title(R.string.gms_manager)
                    message(text = result.msg)
                    positiveButton(R.string.done)
                }
            }
        }

        viewModel.getInstalledUser()
    }

    private fun initRecyclerView() {
        mAdapter = GmsAdapter()
        
        mAdapter.onItemClickListener = { item, position ->
            val view = viewBinding.recyclerView.findViewHolderForAdapterPosition(position)?.itemView
            val checkbox = view?.findViewById<Switch>(R.id.checkbox)
            if (item.isInstalledGms) {
                uninstallGms(item.userID, checkbox)
            } else {
                installGms(item.userID, checkbox)
            }
        }
        
        viewBinding.recyclerView.adapter = mAdapter
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun installGms(userID: Int, checkbox: Switch?) {
        showLoading()
        checkbox?.isChecked = true
        viewModel.installGms(userID)
    }

    private fun uninstallGms(userID: Int, checkbox: Switch?) {
        MaterialDialog(this).show {
            title(R.string.gms_manager)
            message(R.string.uninstall_gms_tips)
            positiveButton(R.string.confirm) {
                showLoading()
                checkbox?.isChecked = false
                viewModel.uninstallGms(userID)
            }
            negativeButton(R.string.cancel)
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, GmsManagerActivity::class.java))
        }
    }
}
