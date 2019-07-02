package com.bochuan.pinke.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bochuan.pinke.R

class ConversationFragment : BaseFragment() {
    override fun refreshData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var contentView: View = inflater.inflate(R.layout.fragment_conversation, null)

        return super.onCreateView(inflater, container, savedInstanceState)
    }


}
