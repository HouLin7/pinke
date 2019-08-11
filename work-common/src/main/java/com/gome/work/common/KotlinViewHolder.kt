package com.gome.work.common

import android.view.View
import com.gome.work.common.adapter.BaseViewHolder
import kotlinx.android.extensions.LayoutContainer

abstract class KotlinViewHolder<T>(view: View) : BaseViewHolder<T>(view), LayoutContainer {

    override val containerView: View?
        get() = itemView;


}