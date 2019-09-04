package com.bochuan.pinke.activity

import android.os.Bundle
import com.bochuan.pinke.R
import com.gome.work.common.activity.BaseGomeWorkActivity
import kotlinx.android.synthetic.main.activity_conversation.*


open class ConversationActivity : BaseGomeWorkActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)
        getCustomToolbar(title_bar).bindActivity(this, "消息")

    }

}
