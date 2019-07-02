package com.gome.work.common.webview.jsbridge;

/**
 * 定义执行action类型
 */

public interface JsActions {
    /**
     * 获取用户登录token
     */
    public String ACTION_GET_TOKEN = "getToken";

    /**
     * 获取用户登录token
     */
    public String ACTION_SET_TITLE = "setTitle";


    /**
     * 获取用户登录token
     */
    public String ACTION_GET_USER = "getUserInfo";

    /**
     * 关闭窗口
     */
    public String ACTION_CLOSE_WINDOW = "closeWindow";

    /**
     * 隐藏导航栏
     */
    public String ACTION_HIDE_TITLE_BAR = "hideTitleBar";

    /**
     * 显示导航栏
     */
    public String ACTION_SHOW_TITLE_BAR = "showTitleBar";


    /**
     * 获取手机硬件信息
     */
    public String ACTION_GET_DEVICE = "getDeviceInfo";

    /**
     * 获取手机网络信息
     */
    public String ACTION_GET_NETWORK = "getNetworkInfo";

    /**
     * 打开新窗口
     */
    public String ACTION_OPEN_NEW_WINDOW = "openNewWindow";

    /**
     * 选择联系人
     */
    public String ACTION_CHOOSE_USER = "chooseUser";

    /**
     * 创建菜单
     */
    public String ACTION_ADD_MENU = "addMenus";


    /**
     * 移除菜单
     */
    public String ACTION_REMOVE_MENU = "removeMenus";


    /**
     * 扫描二维码
     */
    public String ACTION_SCAN_QRCODE = "scanQRCode";

    /**
     * 这是标题栏颜色
     */
    public String ACTION_SET_TITLE_BG_COLOR = "setTitleBgColor";

    /* 签名
     */
    public String ACTION_SIGN = "sign";

    /* 加密
     */
    public String ACTION_ENCRYPT = "encrypt";


    /**
     * 显示图片
     */
    public String ACTION_SHOW_IMAGE = "showImage";

    /**
     * 批量显示图片
     */
    public String ACTION_SHOW_BULK_IMAGE = "showBulkImage";

    /**
     * 下载文件
     */
    public String ACTION_DOWNLOAD_FILE = "download";

    /**
     * 上传文件
     */
    public String ACTION_UPLOAD_FILE = "upload";

    /**
     * 设置返回键监听事件
     */
    public String ACTION_SET_BACK_LISTENER = "setBackKeyListener";

    /**
     * 分享
     */
    public String ACTION_SHARE_CONTENT = "shareContent";
    /**
     * 聊天
     */
    public String ACTION_CHAT_TO = "chat";

    /**
     * 获取app相关信息
     */
    public String ACTION_GET_APP = "getAppInfo";

    /**
     * 拍照
     */
    public String ACTION_TAKE_PHOTO = "takePhoto";

    /**
     * 人脸认证
     */
    public String ACTION_FACE_ID = "faceID";

}