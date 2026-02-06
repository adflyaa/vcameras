package virtual.camera.app.view.list

import androidx.lifecycle.MutableLiveData
import virtual.camera.app.bean.InstalledAppBean
import virtual.camera.app.data.AppsRepository
import virtual.camera.app.view.base.BaseViewModel

/**
 *
 * @Description:
 * @Author: wukaicheng
 * @CreateDate: 2021/4/29 22:36
 */
class ListViewModel(private val repo: AppsRepository) : BaseViewModel() {

    val appsLiveData = MutableLiveData<List<InstalledAppBean>>()
    
    // Alias for compatibility
    val mAppsLiveData = appsLiveData
    val mInstallResultLiveData = MutableLiveData<String>()

    val loadingLiveData = MutableLiveData<Boolean>()
    
    fun getInstalledApps(userID: Int) = getInstallAppList(userID)
    
    fun installApp(source: String, userID: Int) {
        launchOnUI {
            repo.installApk(source, userID, mInstallResultLiveData)
        }
    }
    
    fun search(query: String) {
        // Implement search functionality
        val currentList = appsLiveData.value ?: return
        val filtered = currentList.filter { 
            it.name.contains(query, ignoreCase = true) ||
            it.packageName.contains(query, ignoreCase = true)
        }
        appsLiveData.postValue(filtered)
    }

    fun previewInstalledList() {
        launchOnUI{
            repo.previewInstallList()
        }
    }

    fun getInstallAppList(userID:Int){
        launchOnUI {
            repo.getInstalledAppList(userID,loadingLiveData,appsLiveData)
        }
    }

    fun getInstalledModules() {
        launchOnUI {
            repo.getInstalledModuleList(loadingLiveData, appsLiveData)
        }
    }

}