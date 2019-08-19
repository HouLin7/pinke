package com.gome.work.core.persistence

import android.content.Context
import android.util.LruCache
import com.gome.core.greendao.UserInfoDao
import com.gome.work.core.model.UserInfo
import com.gome.work.core.net.WebApi
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class UserCacheManager private constructor(context: Context) {

    private var userDao: UserInfoDao? = null

    private var context: Context? = null

    private var webApi: WebApi? = null

    private var listeners = ArrayList<IUserGetResultListener>()

    private var cacheUsers: LruCache<String, UserInfo> = LruCache(200)

    private var cacheTask = LinkedList<GetUserTask>()

    private var executor = Executors.newSingleThreadExecutor()

    private var mUIHandler = android.os.Handler()

    class GetUserTask {

        enum class TaskState {
            IDLE, WORKING, DONE
        }

        var userId = ""

        var sate = TaskState.IDLE
    }

    init {
        this.context = context
        var daoUtil = DaoUtil(context)
        userDao = daoUtil.userInfoDao
        webApi = WebApi.getInstance()
    }


    companion object {
        private var instance: UserCacheManager? = null

        fun get(context: Context): UserCacheManager {
            synchronized(UserCacheManager::class) {
                if (instance == null) {
                    instance = UserCacheManager(context)
                }
            }
            return instance!!
        }
    }

    fun addListener(listener: IUserGetResultListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: IUserGetResultListener) {
        listeners.remove(listener)
    }

    fun getUserInfo(id: String): UserInfo? {
        if (cacheUsers[id] != null) {
            return cacheUsers[id]
        }
        var item = userDao!!.queryBuilder().where(UserInfoDao.Properties.Id.eq(id)).unique()
        if (item != null) {
            cacheUsers.put(id, item)
        } else {
            executor.submit {
                getUserFromWeb(id)
            }
        }
        return item
    }

    /**
     * 获取最新的 用户信息，强制和服务器同步一次
     */
    fun getLastUserInfo(id: String): UserInfo? {
        executor.submit {
            getUserFromWeb(id)
        }
        return getUserInfo(id);
    }


    private fun notifyListener(user: UserInfo) {
        for (item in listeners) {
            mUIHandler.post {
                item.onResult(user)
            }
        }
    }

    private fun getUserFromWeb(id: String) {
        for (item in cacheTask) {
            if (item.userId.equals(id) && item.sate == GetUserTask.TaskState.IDLE) {
                return
            }
        }

        var result = webApi!!.getUserInfoSyn(id)
        var userInfo = result?.body()?.data

        userInfo?.let {
            userDao!!.insertOrReplace(userInfo)
            notifyListener(userInfo!!)
        }


    }


    interface IUserGetResultListener {
        fun onResult(result: UserInfo)
    }

}
