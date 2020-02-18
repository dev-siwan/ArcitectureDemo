package com.ksw.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<M, D : ViewDataBinding>(private val bindingVariable: Int? = null) :
    RecyclerView.Adapter<BaseAdapter.ViewHolder<D>>(),
    MutableList<M> {
    override val size: Int get() = item.size
    override fun contains(element: M) = item.contains(element)
    override fun containsAll(elements: Collection<M>) = item.containsAll(elements)
    override operator fun get(index: Int) = item[index]
    override fun indexOf(element: M) = item.indexOf(element)
    override fun isEmpty() = item.isEmpty()
    override fun iterator() = item.iterator()
    override fun lastIndexOf(element: M) = item.lastIndexOf(element)
    override fun add(element: M) = item.add(element).apply {
        notifyDataSetChanged()
    }
    override fun add(index: Int, element: M) {
        item.add(index, element)
        notifyDataSetChanged()
    }
    override fun addAll(index: Int, elements: Collection<M>) = item.addAll(index, elements).apply {
        notifyDataSetChanged()
    }
    override fun addAll(elements: Collection<M>) = item.addAll(elements).apply {
        notifyDataSetChanged()
    }
    override fun clear() {
        item.clear()
        notifyDataSetChanged()
    }
    override fun listIterator() = item.listIterator()
    override fun listIterator(index: Int) = item.listIterator(index)
    override fun remove(element: M) = item.remove(element).apply {
        notifyDataSetChanged()
    }
    override fun removeAll(elements: Collection<M>) = item.removeAll(elements).apply {
        notifyDataSetChanged()
    }
    override fun removeAt(index: Int) = item.removeAt(index).apply {
        notifyDataSetChanged()
    }

    override fun retainAll(elements: Collection<M>) = item.retainAll(elements)
    override fun set(index: Int, element: M) = item.set(index, element).apply {
        notifyDataSetChanged()
    }
    override fun subList(fromIndex: Int, toIndex: Int) = item.subList(fromIndex, toIndex).apply {
        notifyDataSetChanged()
    }

    @LayoutRes
    protected abstract fun layoutId(viewType: Int): Int

    private var item = mutableListOf<M>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<D> {
        val dataBinding: D = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutId(viewType),
            parent,
            false
        )
        onBeforeCreateViewHolder(dataBinding, viewType)
        return ViewHolder(dataBinding).apply {
            onAfterCreateViewHolder(dataBinding, viewType)
        }
    }

    protected open fun onBeforeCreateViewHolder(dataBinding: D, viewType: Int) = Unit
    protected open fun onAfterCreateViewHolder(dataBinding: D, viewType: Int) = Unit

    override fun getItemCount(): Int = size

    override fun onBindViewHolder(holder: ViewHolder<D>, position: Int) {
        onBeforeBindViewHolder(holder, position)
        bindingVariable?.let {
            holder.dataBinding.setVariable(it, item[position])
            holder.dataBinding.executePendingBindings()
        }
        onAfterBindViewHolder(holder, position)
    }

    protected open fun onBeforeBindViewHolder(holder: ViewHolder<D>, position: Int) = Unit
    protected open fun onAfterBindViewHolder(holder: ViewHolder<D>, position: Int) = Unit

    class ViewHolder<D : ViewDataBinding>(val dataBinding: D) : RecyclerView.ViewHolder(dataBinding.root)
}