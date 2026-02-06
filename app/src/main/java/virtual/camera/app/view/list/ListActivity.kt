package virtual.camera.app.view.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import virtual.camera.app.compat.RVAdapter
import virtual.camera.app.compat.StateView
import com.afollestad.materialdialogs.MaterialDialog
import virtual.camera.app.R
import virtual.camera.app.bean.InstalledAppBean
import virtual.camera.app.databinding.ActivityListBinding
import virtual.camera.app.util.InjectionUtil
import virtual.camera.app.util.inflate
import virtual.camera.app.util.toast
import virtual.camera.app.view.base.LoadingActivity

class ListActivity : LoadingActivity() {

    private lateinit var viewModel: ListViewModel

    private lateinit var mAdapter: ListAdapter

    private val viewBinding: ActivityListBinding by inflate()
    
    private lateinit var stateView: StateView
    
    private var userID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        initToolbar(viewBinding.toolbarLayout.toolbar, R.string.app_list, true)
        
        userID = intent.getIntExtra("userID", 0)
        
        initViewModel()
        initRecyclerView()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, InjectionUtil.getListFactory())[ListViewModel::class.java]

        viewModel.mAppsLiveData.observe(this) { apps ->
            hideLoading()
            if (apps.isNullOrEmpty()) {
                stateView.showEmpty()
            } else {
                mAdapter.setData(apps)
                stateView.showContent()
            }
        }

        viewModel.mInstallResultLiveData.observe(this) { result ->
            hideLoading()
            if (result.isNullOrEmpty()) {
                return@observe
            }
            toast(result)
        }

        showLoading()
        viewModel.getInstalledApps(userID)
    }

    private fun initRecyclerView() {
        stateView = StateView.wrap(viewBinding.recyclerView)
        mAdapter = ListAdapter()

        mAdapter.onItemClickListener = { item, position ->
            showInstallDialog(item)
        }

        viewBinding.recyclerView.adapter = mAdapter
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun showInstallDialog(app: InstalledAppBean) {
        MaterialDialog(this).show {
            title(text = app.name)
            message(text = "Install ${app.name}?")
            positiveButton(R.string.confirm) {
                showLoading()
                viewModel.installApp(app.packageName, userID)
            }
            negativeButton(R.string.cancel)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.list_search)
        val searchView = searchItem?.actionView as? SearchView
        
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText ?: "")
                return true
            }
        })
        
        return true
    }

    companion object {
        fun start(context: Context, userID: Int = 0) {
            val intent = Intent(context, ListActivity::class.java)
            intent.putExtra("userID", userID)
            context.startActivity(intent)
        }
    }
}
