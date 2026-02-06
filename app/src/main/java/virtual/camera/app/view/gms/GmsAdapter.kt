package virtual.camera.app.view.gms

import android.view.View
import android.view.ViewGroup
import virtual.camera.app.compat.RVHolder
import virtual.camera.app.compat.RVHolderFactory
import virtual.camera.app.R
import virtual.camera.app.bean.GmsBean
import virtual.camera.app.databinding.ItemGmsBinding

class GmsAdapter : RVHolderFactory<GmsBean>() {

    override fun createViewHolder(parent: ViewGroup?, viewType: Int, item: Any): RVHolder<GmsBean> {
        return GmsVH(inflate(R.layout.item_gms, parent))
    }

    class GmsVH(itemView: View) : RVHolder<GmsBean>(itemView) {
        val binding = ItemGmsBinding.bind(itemView)

        override fun setContent(item: GmsBean, isSelected: Boolean, payload: Any?) {
            binding.tvTitle.text = item.userName
            binding.checkbox.isChecked = item.isInstalledGms
        }
    }
}
