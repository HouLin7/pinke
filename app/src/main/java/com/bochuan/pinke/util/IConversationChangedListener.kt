package com.bochuan.pinke.util

import com.gome.work.core.model.im.ConversationInfo

interface IConversationChangedListener {

    fun onChangeData(dataList: List<ConversationInfo>)

}
