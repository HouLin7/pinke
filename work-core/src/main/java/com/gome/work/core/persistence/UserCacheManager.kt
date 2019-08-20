package com.gome.work.core.persistence

import android.content.Context
import android.util.LruCache
import com.gome.core.greendao.UserInfoDao
import com.gome.work.core.model.UserInfo
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class UserCacheManager private constructor(context: Context) {

    private var userDao: UserInfoDao? = null

    private var context: Context? = null

    private var webApi: WebApi? = null

    private var listeners = ArrayList<IUserGetResultListener>()

    private var cacheUsers: LruCache<String, UserInfo> = LruCache(200)

    private var cacheTask = LinkedList<GetUserTask>()

    private var executor = Executors.newFixedThreadPool(5)

    private var mUIHandler = android.os.Handler()

    enum class TaskState {
        IDLE, WORKING, DONE
    }


    inner class GetUserTask(var userId: String) : Callable<UserInfo> {

        var state = TaskState.IDLE

        override fun call(): UserInfo? {
            synchronized(this@UserCacheManager) {
                state = TaskState.WORKING
            }

            var result = webApi!!.getUserInfoSyn(userId)
            var userInfo: UserInfo? = null;
            result?.let {
                if (it.isSuccessful) {
                    if (result.body()!!.isSuccess) {
                        userInfo = result.body()!!.data
                    }
                }
            }

            userInfo?.let {
                userDao!!.insertOrReplace(userInfo)
                notifyListener(userInfo!!)
            }

            synchronized(this@UserCacheManager) {
                state = TaskState.DONE
                cacheTask.remove(this)
            }
            return userInfo
        }


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
            synchronized(this) {
                for (task in cacheTask) {
                    if (task.userId == id && task.state == TaskState.IDLE) {
                        return item
                    }
                }

                var task = GetUserTask(id)
                cacheTask.add(task)

                executor.submit(task)
            }

        }
        return item
    }

    fun getCacheUser(id: String): UserInfo? {
        var cacheData = cacheUsers[id];
        if (cacheData != null) {
            return cacheData
        } else {
            cacheData = userDao!!.queryBuilder().where(UserInfoDao.Properties.Id.eq(id)).unique()
            if (cacheData != null) {
                cacheUsers.put(id, cacheData)
                return cacheData
            }
        }
        return null
    }

    /**
     * 获取最新的 用户信息，强制和服务器同步一次
     * @param id  user id
     */
    fun getLastUserInfo(id: String, listener: IUserGetResultListener) {
        webApi!!.getUserInfo(id, object : IResponseListener<UserInfo> {
            override fun onError(code: String?, message: String?) {

            }

            override fun onSuccess(result: UserInfo?) {
                if (listener != null) {
                    listener.onResult(result!!)
                    executor.submit { userDao!!.insert(result!!) }
                }
            }
        })
    }


    private fun notifyListener(user: UserInfo) {
        for (item in listeners) {
            mUIHandler.post {
                item.onResult(user)
            }
        }
    }


    interface IUserGetResultListener {
        fun onResult(result: UserInfo)
    }

}
