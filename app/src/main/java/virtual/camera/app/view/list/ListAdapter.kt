package virtual.camera.app.view.list

import android.view.View
import android.view.ViewGroup
import virtual.camera.app.compat.RVHolder
import virtual.camera.app.compat.RVHolderFactory
import virtual.camera.app.R
import virtual.camera.app.bean.InstalledAppBean
import virtual.camera.app.databinding.ItemPackageBinding

class ListAdapter : RVHolderFactory<InstalledAppBean>() {

    override fun createViewHolder(parent: ViewGroup?, viewType: Int, item: Any): RVHolder<InstalledAppBean> {
        return ListVH(inflate(R.layout.item_package, parent))
    }

    class ListVH(itemView: View) : RVHolder<InstalledAppBean>(itemView) {
        private val binding = ItemPackageBinding.bind(itemView)

        override fun setContent(item: InstalledAppBean, isSelected: Boolean, payload: Any?) {
            binding.icon.setImageDrawable(item.icon)
            binding.name.text = item.name
            binding.packageName.text = item.packageName
        }
    }
}
