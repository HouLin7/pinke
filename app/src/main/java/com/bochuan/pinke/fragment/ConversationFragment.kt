package com.bochuan.pinke.fragment

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bochuan.pinke.R
import com.bochuan.pinke.util.ConversationManager
import com.bochuan.pinke.util.IConversationChangedListener
import com.gome.core.greendao.ConversationInfoDao
import com.gome.work.common.KotlinViewHolder
import com.gome.work.common.adapter.BaseRecyclerAdapter
import com.gome.work.common.divider.CustomNewsDivider
import com.gome.work.core.model.im.ConversationInfo
import com.gome.work.core.model.im.ConversationInfo.MSG_TYPE_CHAT
import com.gome.work.core.persistence.DaoUtil
import com.hyphenate.EMConnectionListener
import com.hyphenate.EMConversationListener
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.util.DateUtils
import kotlinx.android.synthetic.main.adapter_conversation_list_item.*
import kotlinx.android.synthetic.main.fragment_conversation.*
import java.util.*
import kotlin.collections.ArrayList

class ConversationFragment : BaseFragment(), IConversationChangedListener {

    override fun onChangeData(dataList: List<ConversationInfo>) {
        if (conversationManager != null && mAdapter != null) {
            mAdapter!!.setItemList(conversationManager!!.getAllConversationList())
        }
    }

    private var mDaoUtil: DaoUtil? = null;

    private var mAdapter: MyAdapter? = null;

    private var conversationManager: ConversationManager? = null;

    protected var connectionListener: EMConnectionListener = object : EMConnectionListener {

        override fun onDisconnected(error: Int) {
//            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE || error == EMError.SERVER_SERVICE_RESTRICTED
//                || error == EMError.USER_KICKED_BY_CHANGE_PASSWORD || error == EMError.USER_KICKED_BY_OTHER_DEVICE
//            ) {
//                isConflict = true
//            } else {
//                handler.sendEmptyMessage(0)
//            }
        }

        override fun onConnected() {
//            handler.sendEmptyMessage(1)
        }

    }

    override fun getLayoutID(): Int {
        return R.layout.fragment_conversation
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mDaoUtil = DaoUtil(activity!!);
        conversationManager = ConversationManager.get(activity!!);
        conversationManager!!.addListener(this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(activity);
        recyclerView.addItemDecoration(
            CustomNewsDivider(
                context,
                DividerItemDecoration.HORIZONTAL,
                2,
                R.color.divider_color
            )
        )
        mAdapter = MyAdapter(activity!!)
        recyclerView.adapter = mAdapter
        EMClient.getInstance().chatManager().addConversationListener(EMConversationListener { })

    }

    override fun refreshData() {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        super.onDestroy()
        EMClient.getInstance().removeConnectionListener(connectionListener)
        conversationManager!!.removeListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EMClient.getInstance().addConnectionListener(connectionListener)

    }


    inner class MyAdapter(activity: FragmentActivity) : BaseRecyclerAdapter<ConversationInfo>(activity) {

        override fun onCreateMyViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            var view: View = layoutInflater.inflate(R.layout.adapter_conversation_list_item, null);
            return MyViewHolder(view);
        }

        override fun onBindMyViewHolder(holder: RecyclerView.ViewHolder?, dataItem: ConversationInfo?, position: Int) {
            var myViewholder: MyViewHolder = holder as MyViewHolder
            myViewholder.bind(dataItem!!)
        }
    }

    inner class MyViewHolder(view: View) : KotlinViewHolder<ConversationInfo>(view) {
        override fun bind(t: ConversationInfo) {
            tv_title.setText(t.title)
            tv_title.setText(t.content)
            tv_time.setText(DateUtils.getTimestampString(Date(t.updateTime)))
            if (t.unReadCount > 0) {
                tv_unread_count.visibility = View.VISIBLE
                tv_unread_count.setText("" + t.unReadCount)
            } else {
                tv_unread_count.visibility = View.GONE
            }

            if (MSG_TYPE_CHAT == t.msgType) {

            }


        }


    }


    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private fun sortConversationByLastChatTime(conversationList: List<EMConversation>) {
        Collections.sort(conversationList) { con1, con2 ->
            (con1.lastMessage.msgTime - con2.lastMessage.msgTime).toInt()
        }
    }


    fun loadCacheData() {
        var dao: ConversationInfoDao = mDaoUtil!!.conversationInfoDao
        var conversationInfoList = dao.loadAll();
        mAdapter!!.setItemList(conversationInfoList);
    }

    /**
     * load conversation list
     *
     * @return
     * +
     */
    private fun loadIMConversationList(): List<EMConversation> {
        val conversations = EMClient.getInstance().chatManager().allConversations
        val sortList = ArrayList<EMConversation>()

        for (conversation in conversations.values) {
            if (conversation.allMessages.size != 0) {
                sortList.add(conversation);
            }
        }

        try {
            sortConversationByLastChatTime(sortList)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return sortList
    }

    private fun convertMyConvertObj(source: EMConversation) {

    }

}
