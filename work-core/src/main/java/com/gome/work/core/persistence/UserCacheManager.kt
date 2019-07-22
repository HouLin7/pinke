package com.gome.work.core.persistence

import android.content.Context
import com.gome.core.greendao.UserInfoDao
import com.gome.work.core.model.UserInfo
import com.gome.work.core.net.IResponseListener
import com.gome.work.core.net.WebApi
import java.util.*

class UserCacheManager private constructor(context: Context) {

    private var userDao: UserInfoDao? = null

    private var context: Context? = null

    private var webApi: WebApi? = null

    private var listeners = ArrayList<IUserGetResultListener>()

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
        return userDao!!.queryBuilder().where(UserInfoDao.Properties.Id.eq(id)).unique()
    }

    fun notifyListener(user: UserInfo) {
        for (item in listeners) {
            item?.let {
                item.onResult(user)
            }
        }
    }

    private fun getUserFromWeb(id: String) {
        webApi!!.getUserDetail(id, object : IResponseListener<UserInfo> {
            override fun onSuccess(result: UserInfo?) {
                userDao!!.insertOrReplace(result)
                notifyListener(result!!)
            }

            override fun onError(code: String?, message: String?) {

            }

        })
    }


    interface IUserGetResultListener {
        fun onResult(result: UserInfo)
    }

}
