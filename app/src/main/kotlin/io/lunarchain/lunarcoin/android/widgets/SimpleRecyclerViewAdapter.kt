package io.lunarchain.lunarcoin.android.widgets

import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SimpleRecyclerViewAdapter<D, H : SimpleRecyclerViewHolder<D>> constructor(
    private val dataSource: List<D>,
    private val fragment: Fragment,
    private val itemLayoutResId: Int,
    private val clazz: Class<H>
) :
    RecyclerView.Adapter<H>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): H {
        var constructor = clazz.getConstructor(fragment.javaClass, View::class.java)
        return constructor.newInstance(fragment, LayoutInflater.from(fragment.context).inflate(itemLayoutResId, parent, false))
    }

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: H, position: Int) {
        holder.bindData(dataSource[position])
    }

}