package virtual.camera.app.compat

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Replacement for cbfg.rvadapter.RVAdapter
 * Simplified RecyclerView adapter base class
 */
abstract class RVAdapter<T, VH : RVHolder<T>> : RecyclerView.Adapter<VH>() {
    
    private val items = mutableListOf<T>()
    var onItemClickListener: ((T, Int) -> Unit)? = null
    var onItemLongClickListener: ((T, Int) -> Boolean)? = null
    
    override fun getItemCount(): Int = items.size
    
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.setContent(item, false, null)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item, position)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.invoke(item, position) ?: false
        }
    }
    
    fun setData(newItems: List<T>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
    
    fun addData(newItems: List<T>) {
        val startPosition = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }
    
    fun addItem(item: T) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }
    
    fun removeItem(position: Int) {
        if (position >= 0 && position < items.size) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    
    fun getItem(position: Int): T? {
        return if (position >= 0 && position < items.size) items[position] else null
    }
    
    fun getData(): List<T> = items.toList()
    
    fun clearData() {
        items.clear()
        notifyDataSetChanged()
    }
}

/**
 * Replacement for cbfg.rvadapter.RVHolder
 */
abstract class RVHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun setContent(item: T, isSelected: Boolean, payload: Any?)
}

/**
 * Replacement for cbfg.rvadapter.RVHolderFactory
 */
abstract class RVHolderFactory<T> : RVAdapter<T, RVHolder<T>>() {
    
    abstract fun createViewHolder(parent: ViewGroup?, viewType: Int, item: Any): RVHolder<T>
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder<T> {
        return createViewHolder(parent, viewType, Any())
    }
    
    protected fun inflate(layoutId: Int, parent: ViewGroup?): View {
        return android.view.LayoutInflater.from(parent?.context)
            .inflate(layoutId, parent, false)
    }
}
