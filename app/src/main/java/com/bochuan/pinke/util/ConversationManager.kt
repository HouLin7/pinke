package com.bochuan.pinke.util

import android.content.Context
import android.os.Handler
import com.gome.core.greendao.ConversationInfoDao
import com.gome.core.greendao.GroupInfoDao
import com.gome.work.core.model.UserInfo
import com.gome.work.core.model.dao.MyGroupInfo
import com.gome.work.core.model.im.ConversationInfo
import com.gome.work.core.model.im.GroupInfo
import com.gome.work.core.model.im.PushDataExtraInfo
import com.gome.work.core.persistence.DaoUtil
import com.gome.work.core.utils.SharedPreferencesHelper
import com.hyphenate.chat.EMMessage
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class ConversationManager private constructor(context: Context) {

    private var mDaoUtil: DaoUtil? = null

    private var userInfo: UserInfo? = null

    val conversations = mutableListOf<ConversationInfo>()

    val changedListeners = HashSet<IConversationChangedListener>()

    val emMessages = mutableListOf<EMMessage>()

    val pushMessages = mutableListOf<PushDataExtraInfo>();

    private val mHandler = Handler()

    private var context: Context? = null

    init {
        mDaoUtil = DaoUtil(context)

        val mScheduledExecutorService = Executors.newScheduledThreadPool(1)
        mScheduledExecutorService.scheduleWithFixedDelay(Runnable {
            var isOK1 = handleEmMessage()
            var isOK2 = handlePushMessage()
            if (isOK1 || isOK2) {
                var lastResult = getSortList(conversations)
                lastResult?.let {
                    conversations.clear();
                    conversations.addAll(lastResult)
                    notifyListener(lastResult)
                }
            }
        }, 2, 2, TimeUnit.SECONDS)


    }

    companion object {
        private var instance: ConversationManager? = null
        fun get(context: Context): ConversationManager {
            if (instance == null) {
                synchronized(ConversationManager::class) {
                    if (instance == null) {
                        instance = ConversationManager(context)
                    }
                }
            }
            return instance!!
        }
    }

    /**
     * 记录聊天方法
     * @param xMessage
     */
    @Synchronized
    fun recoredEMMessage(xMessage: EMMessage) {
        emMessages.add(xMessage)
    }

    @Synchronized
    fun recoredPushMessage(pushMsg: PushDataExtraInfo) {
        if (pushMessages.isEmpty()) {
            pushMessages.add(pushMsg)
        } else {
            var lastItem = pushMessages[0];
            if (lastItem.createTime < pushMsg.createTime) {
                pushMessages.clear()
                pushMessages.add(pushMsg)
            }
        }
    }

    /**
     * 处理推送消息
     */
    private fun handlePushMessage(): Boolean {
        userInfo = SharedPreferencesHelper.getAccessTokenInfo().userInfo
        if (userInfo == null) {
            return false;
        }

        if (pushMessages.isEmpty()) {
            return false
        }

        var pushConversationInfo: ConversationInfo? = null
        for (item in conversations) {
            if (item.msgType == ConversationInfo.MSG_TYPE_PUSH) {
                pushConversationInfo = item
                break;
            }
        }
        var pushMsg: PushDataExtraInfo? = null
        synchronized(this) {
            pushMsg = pushMessages[0]
            pushMessages.clear()
        }
        if (pushConversationInfo != null) {
            ConversationUtil.assignConversation(pushMsg!!, pushConversationInfo)
        } else {
            pushConversationInfo = ConversationUtil.getConversation(pushMsg!!)
            pushConversationInfo.dataUseId = userInfo!!.id
            conversations.add(pushConversationInfo)
        }
        mDaoUtil!!.conversationInfoDao.insertOrReplace(pushConversationInfo)
        return true
    }


    /**
     * 处理聊天类消息
     */
    private fun handleEmMessage(): Boolean {
        userInfo = SharedPreferencesHelper.getAccessTokenInfo().userInfo
        if (userInfo == null) {
            return false;
        }

        if (emMessages.isEmpty()) {
            return false
        }
        val xMessageList = ArrayList<EMMessage>()
        synchronized(this) {
            xMessageList.addAll(emMessages)
            emMessages.clear()
        }

        var maps = hashMapOf<String, EMMessage>()
        for (message in xMessageList) {
            if (maps.containsKey(message.conversationId())) {
                val dateTime = maps.get(message.conversationId())!!.msgTime;
                if (dateTime < message.msgTime) {
                    maps.put(message.conversationId(), message);
                }
            } else {
                maps.put(message.conversationId(), message);
            }

        }
        var filteredMsgList = maps.entries.toList();

        var changedConversatinos = hashSetOf<ConversationInfo>()
        for (message in filteredMsgList) {
            message?.run {
                var conversation = findConversation(value.conversationId(), conversations)
                if (conversation == null) {
                    conversation = ConversationUtil.getConversation(value, context!!)
                    conversation.dataUseId = userInfo!!.id
                } else {
                    ConversationUtil.assignConversation(value, conversation, context!!)
                }
                changedConversatinos.add(conversation)
            }
        }
        if (!changedConversatinos.isEmpty()) {
            mDaoUtil!!.conversationInfoDao.insertOrReplaceInTx(changedConversatinos)
        }

        return true
    }

    private fun findConversation(conversationId: String, conversations: List<ConversationInfo>): ConversationInfo? {
        for (item in conversations) {
            if (item.conversationId.equals(conversationId)) {
                return item
            }
        }
        return null
    }


    /**
     * 获取数据库所有的聊天列表
     *
     * @return
     */
    fun getAllConversationList(): List<ConversationInfo> {
        return mDaoUtil!!.conversationInfoDao.loadAll()
    }

    /**
     * 根据groupId设置是否置顶聊天列表
     *
     * @param groupId
     */
    fun setIsTop(item: ConversationInfo, flag: Boolean) {
        val value = if (flag) 1 else 0
        item.setIsTop(value)
        updateNoticeTab(item)
        notifyListener(conversations)
    }

    /**
     * 根据groupId设置是否免打扰
     *
     * @param groupId
     */
    fun setIsShield(item: ConversationInfo, flag: Boolean) {
        val value = if (flag) 1 else 0
        item.setIsShield(value)
        updateNoticeTab(item)
        notifyListener(conversations)
    }


    /**
     * 根据IM 用户登录的Id 来获取本地聊天列表   跟登录用户绑定
     *
     * @param im_id
     * @return
     */

    private fun getConversationListByUserId(userId: String): MutableList<ConversationInfo>? {
        return mDaoUtil!!.conversationInfoDao.queryBuilder().where(ConversationInfoDao.Properties.DataUseId.eq(userId))
            .orderDesc(ConversationInfoDao.Properties.IsTop, ConversationInfoDao.Properties.UpdateTime).list()
    }


    /**
     * 保存接收发送信息列聊天列表List方法
     *
     * @param data
     */
    private fun updateChatList(data: List<ConversationInfo>) {
        if (data.size != 0) {
            mDaoUtil!!.conversationInfoDao.insertOrReplaceInTx(data)
        }
    }

    private fun notifyListener(data: List<ConversationInfo>?) {
        mHandler.post {
            for (listener in changedListeners) {
                listener.onChangeData(data!!)
            }
        }
    }


    fun addListener(listener: IConversationChangedListener) {
        changedListeners.add(listener)
    }

    fun removeListener(listener: IConversationChangedListener) {
        changedListeners.remove(listener)
    }

    /**
     * 删除聊天列表List方法
     */
    fun deleteAllChatList() {
        mDaoUtil!!.conversationInfoDao.deleteInTx(mDaoUtil!!.conversationInfoDao.loadAll())
    }


    /**
     * 更新数据
     *
     * @param associationTab
     */
    private fun updateNoticeTab(associationTab: ConversationInfo) {
        mDaoUtil!!.conversationInfoDao.getSession()
            .runInTx(Runnable { mDaoUtil!!.conversationInfoDao.insertOrReplace(associationTab) })
    }


    /**
     * 获取所有的 chatlist 聊天列表
     *
     * @return
     */
    private fun getAllChatList(): List<ConversationInfo> {
        return mDaoUtil!!.conversationInfoDao.loadAll()
    }


    private fun getSortList(conversations: MutableList<ConversationInfo>): List<ConversationInfo> {
        val isTopList = ArrayList<ConversationInfo>()
        val otherList = ArrayList<ConversationInfo>()
        for (item in conversations) {
            if (item.isTop()) {
                isTopList.add(item);
            } else {
                otherList.add(item)
            }
        }
        Collections.sort(isTopList)
        Collections.sort(otherList)
        var result = arrayListOf<ConversationInfo>();
        result.addAll(isTopList)
        result.addAll(otherList)
        return result
    }


    /**
     * 保存通知关系表
     *
     * @param data
     */
    fun updateNoticeInfo(data: ConversationInfo?) {
        updateNoticeInfo(data, userInfo!!.getId())
    }

    fun updateNoticeInfo(data: ConversationInfo?, loginUserId: String) {
        data!!.setDataUseId(loginUserId)
        mDaoUtil!!.conversationInfoDao.insertOrReplaceInTx(data)
    }

    /**
     * 获取会话列表appId对应的记录表
     *
     * @param appId
     * @return
     */
    fun getNoticeTabByAppId(appId: String): ConversationInfo? {
        return mDaoUtil!!.conversationInfoDao.queryBuilder().where(
            ConversationInfoDao.Properties.DataUseId.eq(userInfo!!.getId())
        ).unique()
    }


    /**
     * 保存push通知
     *
     * @param data
     */
    fun updatePushInfo(data: PushDataExtraInfo?) {
        mDaoUtil!!.pushDataExtraInfoDao.insertOrReplaceInTx(data!!)
    }


    /**
     * 返回通知列表，
     *
     * @return
     */
    fun getNoticeList(userId: String): List<ConversationInfo>? {
        return mDaoUtil!!.conversationInfoDao.queryBuilder()
            .where(
                ConversationInfoDao.Properties.DataUseId.eq(userId)
            ).list()
    }


    /**
     * 更新保存群组信息到DB
     */
    fun updateGroupInfo(data: GroupInfo) {
        updateGroupInfo(data, userInfo!!.getId())
    }

    /**
     * 保存群组信息到DB
     */
    fun updateGroupInfo(data: GroupInfo, loginUserId: String) {
        val item = MyGroupInfo()
        item.dataUseId = loginUserId
        item.groupId = data.groupId
        mDaoUtil!!.groupInfoDao.insertOrReplace(data)
        mDaoUtil!!.myGroupInfoDao.insertOrReplace(item)
    }

    /**
     * 根据groupId获取群组信息
     *
     * @param groupId
     * @return
     */
    fun getGroupInfo(groupId: String): GroupInfo {
        return mDaoUtil!!.groupInfoDao.queryBuilder().where(GroupInfoDao.Properties.GroupId.eq(groupId)).unique()
    }

    /**
     * 保存会话列表
     *
     * @param bean
     */
    fun updateChatListInfo(bean: ConversationInfo) {
        mDaoUtil!!.conversationInfoDao.insertOrReplace(bean)
    }

    /**
     * 获取所有的未读数
     */

    fun getAllUnReadCount(): Int {
        val chatListBeanList = getNoticeList(userInfo!!.getId())
        var noticeCount = 0
        if (chatListBeanList != null) {
            for (tab in chatListBeanList) {
                noticeCount += tab.getUnReadCount()
            }
        }
        return noticeCount
    }


    /**
     * 清除聊天列表的角标
     *
     * @param chatListBean
     */
    fun clearReadCount(chatListBean: ConversationInfo?) {
        if (chatListBean != null) {
            chatListBean!!.setUnReadCount(0)
            updateChatListInfo(chatListBean)
        }
    }


}