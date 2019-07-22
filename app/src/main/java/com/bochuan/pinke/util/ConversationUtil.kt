package com.bochuan.pinke.util

import android.content.Context
import com.gome.work.core.model.im.ConversationInfo
import com.gome.work.core.model.im.PushDataExtraInfo
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import com.hyphenate.easeui.utils.EaseCommonUtils

object ConversationUtil {

    fun getConversation(msg: PushDataExtraInfo): ConversationInfo {
        var result = ConversationInfo();
        assignConversation(msg, result)
        return result
    }

    fun assignConversation(msg: PushDataExtraInfo, conv: ConversationInfo) {
        with(conv) {
            msgType = ConversationInfo.MSG_TYPE_PUSH
            conversationId = "push"
            title = msg.title
            content = msg.content
        }
    }

    fun getConversation(msg: EMMessage, context: Context): ConversationInfo {
        var result = ConversationInfo();
        result.msgType = ConversationInfo.MSG_TYPE_CHAT
        result.conversationId = msg.conversationId()
        assignConversation(msg, result, context)
        return result
    }

    fun assignConversation(msg: EMMessage, con: ConversationInfo, context: Context) {
        with(con) {
            unReadCount += 1
            updateTime = msg.msgTime;
            content = EaseCommonUtils.getMessageDigest(msg, context);
            title = msg.userName;
        }
    }

    fun getConversationList(context: Context, sourceList: ArrayList<EMConversation>) {
        var result: ArrayList<ConversationInfo>;

        for (item in sourceList) {
            var conInfo = ConversationInfo();
            conInfo.unReadCount = item.unreadMsgCount;
            conInfo.updateTime = item.lastMessage.msgTime;
            conInfo.content = EaseCommonUtils.getMessageDigest(item.lastMessage, context);
            conInfo.title = item.lastMessage.userName;
            conInfo.conversationId = item.conversationId()


        }

    }
}
