
package com.gome.work.core;

public interface Constants {

    public static final String SING_VALUE = "nGtEcEHTNUAir5UctyjjYbSOoSQ=";

    /**
     *
     */
    public static final String SMART_OFFICE_APPID = "app0001";


    /**
     * 会议文件
     */
    public static final String SCOPE_TYPE_MEETING = "meeting";

    /**
     * 最近文件
     */
    public static final String SCOPE_TYPE_RECENT = "recent";

    public static final String EXTRA_MODEL = "extra.model";

    public static final String MODEL_VIEW = "model.view";

    /**
     * 单选
     */
    public static final String MODEL_PICK_SINGLE = "model.pick.single";

    /**
     * 多选
     */
    public static final String MODEL_PICK_MULTI = "model.pick.multi";

    public static final String EXTRA_MAX_LIMIT = "extra.max.limit";

    public static final String EXTRA_USER_FILTER_LIST = "extra.user.filter.list";

    public static final String EXTRA_USER_SELECT_RESULT_LIST = "extra.user.select.result.list";

    public static final String FLAG_MAIN_TAB_INDEX = "flag.main.tab.index";


    /**
     * 全局定义所有需要shared-preference 机制保存数据的key值
     */
    public interface PreferKeys {

        String BANNER_LIST = "pre.banner";

        String NEWS_LIST = "pre.news";

        String CHANNEL_LIST = "channel.news";

        public String ACCOUNT = "pre.account";

        public String PASSWORD = "pre.password";

        public String ACCESS_TOKEN_INFO = "pre.access.token.info";

        /**
         * 当前登陆者的用户信息
         */
        public String LOGIN_USER_INFO = "pre.login.user";

        public static final String ACCESS_TOKEN = "pre.access.token";

        public static final String REQUEST_TOKEN = "pre.request.token";

        public static final String SHARED_PREFERENCES_NAME = "sp_work";

        /**
         * 上一版本Code
         */
        public static final String LAST_VERSION_CODE = "last.version.code";

        public static final String IS_SHOW_GUIDE_PAGE = "guide.page";

        public static final String SETTING_INFO = "setting_info";

        /**
         * 广告信息
         */
        public static final String AD_DATA = "ad.data";

        /**
         * 工作banner广告
         */
        public static final String JSON_WORK_BANNER = "json.work.banner";

        /**
         * 通讯录我的好友
         */
        public static final String JSON_CONTACTS_MY_FRIENDS = "json.contacts.my.friends";

        /**
         * 应用最近的升级信息
         */
        public static final String UPDATE_LAST_INFO = "update.last.info";

        /**
         * 各厂商离线推送push token
         */
        public static final String DEVICE_PUSH_TOKEN = "device.push.token";
    }

}
