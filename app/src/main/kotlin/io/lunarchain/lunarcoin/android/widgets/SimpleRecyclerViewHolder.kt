package io.lunarchain.lunarcoin.android.widgets

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class SimpleRecyclerViewHolder<D> constructor(itemRootView: View) : RecyclerView.ViewHolder(itemRootView) {

    abstract fun bindData(itemData: D)

}