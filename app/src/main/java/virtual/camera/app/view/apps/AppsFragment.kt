package virtual.camera.app.view.apps

import android.graphics.Point
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import virtual.camera.app.compat.RVAdapter
import virtual.camera.app.compat.StateView
import com.afollestad.materialdialogs.MaterialDialog
import com.hack.opensdk.HackApi
import virtual.camera.app.R
import virtual.camera.app.bean.AppInfo
import virtual.camera.app.databinding.FragmentAppsBinding
import virtual.camera.app.util.InjectionUtil
import virtual.camera.app.util.ShortcutUtil
import virtual.camera.app.util.inflate
import virtual.camera.app.util.toast
import virtual.camera.app.view.base.LoadingActivity
import virtual.camera.app.view.main.MainActivity
import java.util.*
import kotlin.math.abs


/**
 *
 * @Description:
 * @Author: wukaicheng
 * @CreateDate: 2021/4/29 22:21
 */
class AppsFragment : Fragment() {

    var userID: Int = 0

    private lateinit var viewModel: AppsViewModel

    private lateinit var mAdapter: AppsAdapter

    private val viewBinding: FragmentAppsBinding by inflate()

    private var popupMenu: PopupMenu? = null
    
    private lateinit var stateView: StateView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this, InjectionUtil.getAppsFactory()).get(AppsViewModel::class.java)
        userID = requireArguments().getInt("userID", 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        stateView = StateView.wrap(viewBinding.recyclerView)
        stateView.showEmpty()

        mAdapter = AppsAdapter()

        viewBinding.recyclerView.adapter = mAdapter
        viewBinding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        val touchCallBack = AppsTouchCallBack { from, to ->
            onItemMove(from, to)
            viewModel.updateSortLiveData.postValue(true)
        }

        val itemTouchHelper = ItemTouchHelper(touchCallBack)
        itemTouchHelper.attachToRecyclerView(viewBinding.recyclerView)

        mAdapter.onItemClickListener = { item, position ->
            val intent = HackApi.getLaunchIntentForPackage(item.packageName, userID)
            if (intent != null) {
                HackApi.startActivity(intent, userID)
            }
        }

        mAdapter.onItemLongClickListener = { item, position ->
            showAppMenu(item, position)
            true
        }

        setupObservers()
        
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel.getAppList(userID)
    }

    private fun setupObservers() {
        viewModel.appsLiveData.observe(viewLifecycleOwner) { appInfos ->
            if (appInfos.isNullOrEmpty()) {
                stateView.showEmpty()
            } else {
                mAdapter.setData(appInfos)
                stateView.showContent()
            }
        }

        viewModel.installResultLiveData.observe(viewLifecycleOwner) { msg ->
            toast(msg)
            (activity as? LoadingActivity)?.hideLoading()
            viewModel.getAppList(userID)
        }

        viewModel.unInstallResultLiveData.observe(viewLifecycleOwner) { msg ->
            toast(msg)
            viewModel.getAppList(userID)
        }

        viewModel.launchResultLiveData.observe(viewLifecycleOwner) { success ->
            if (!success) {
                toast("Launch failed")
            }
        }

        viewModel.updateSortLiveData.observe(viewLifecycleOwner) { needUpdate ->
            if (needUpdate) {
                updateSort()
            }
        }
    }

    private fun onItemMove(fromPosition: Int, toPosition: Int) {
        val list = mAdapter.getData().toMutableList()
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(list, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(list, i, i - 1)
            }
        }
        mAdapter.setData(list)
    }

    private fun updateSort() {
        val appList = mAdapter.getData()
        viewModel.updateSortList(userID, appList)
    }

    private fun showAppMenu(appInfo: AppInfo, position: Int) {
        popupMenu = PopupMenu(requireContext(), viewBinding.recyclerView.findViewHolderForAdapterPosition(position)?.itemView ?: return)
        popupMenu?.menuInflater?.inflate(R.menu.app_menu, popupMenu?.menu)
        
        popupMenu?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_uninstall -> {
                    showUninstallDialog(appInfo)
                    true
                }
                R.id.menu_shortcut -> {
                    ShortcutUtil.createShortcut(requireContext(), userID, appInfo)
                    toast("Shortcut created")
                    true
                }
                R.id.menu_clone -> {
                    (activity as? MainActivity)?.cloneApp(appInfo.packageName)
                    true
                }
                else -> false
            }
        }
        
        popupMenu?.show()
    }

    private fun showUninstallDialog(appInfo: AppInfo) {
        MaterialDialog(requireContext()).show {
            title(text = "Uninstall")
            message(text = "Uninstall ${appInfo.name}?")
            positiveButton(text = "Confirm") {
                viewModel.unInstall(appInfo.packageName, userID)
            }
            negativeButton(text = "Cancel")
        }
    }
    
    fun installApk(source: String) {
        (activity as? LoadingActivity)?.showLoading()
        viewModel.install(source, userID)
    }

    companion object {
        fun newInstance(userID: Int): AppsFragment {
            return AppsFragment().apply {
                arguments = bundleOf("userID" to userID)
            }
        }
    }
}
