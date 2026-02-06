package virtual.camera.app.view.gms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import virtual.camera.app.data.GmsRepository

class GmsFactory(private val repository: GmsRepository) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GmsViewModel::class.java)) {
            return GmsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
