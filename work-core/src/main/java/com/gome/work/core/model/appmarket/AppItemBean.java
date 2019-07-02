package com.gome.work.core.model.appmarket;

import com.gome.work.core.model.UserInfo;
import com.gome.work.core.model.converter.AppDeatilScrConverter;
import com.gome.work.core.model.converter.UserDetailBeanConverter;
import org.greenrobot.greendao.annotation.*;

import java.io.Serializable;
import java.util.List;


@Entity
public class AppItemBean implements Serializable {
    private static final long serialVersionUID = 8157811113708548515L;

    @Id
    private Long id;

    /**
     * 应用秘钥
     */
    public String appSecret;
    /**
     * 应用名称
     */
    public String name;
    /**
     * 应用分类名称
     */
    public String categoryName;

    /**
     * 应用分类id
     */
    public String categoryCode;

    /**
     * 应用标识
     */
    @Unique
    public String appId;

    /**
     * 图片资源地址  本地资源ID或者 urls 此处不做空判
     */
    public String iconUrl;

    /**
     * 跳转web页面是否显示titlte
     */
    public boolean isShowTitle;

    public String portalUrl;

    /**
     * 跳转原生还是web，1为web ，2为原生
     */
    public int type;

    /**
     * app图标
     */
    public String imgUrl;
    public int sort;

    /**
     * 评分
     */
    public int score;

    /**
     * 是否安装(收藏)
     */
    @Transient
    private String isFavorited;

    /**
     * 应用介绍
     */
    public String introduce;

    /**
     * 是否赞过
     */
    @Transient
    private String isPraised;

    /**
     * 应用截图，
     */
    @Convert(columnType = String.class, converter = AppDeatilScrConverter.class)
    public List<String> screenshotUrls;

    /**
     * 热度值
     */
    public String hotCount;

    /**
     * 评论数
     */
    public String commentCount;
    /**
     * 点赞数
     */
    public String praiseCount;
    /**
     * 客服信息
     */
    @Convert(columnType = String.class, converter = UserDetailBeanConverter.class)
    public UserInfo helperUser;
    /**
     * H5应用连接
     */
    public String homePageUrl;

    /**
     * 是否追加签名信息，当加载 homePageUrl的时候
     */
    public String isForceVerify;

    public String badgeCount;
    /**
     * 开发者信息
     */
    @Convert(columnType = String.class, converter = UserDetailBeanConverter.class)
    public UserInfo developerUser;



    @Generated(hash = 1861162756)
    public AppItemBean(Long id, String appSecret, String name, String categoryName,
            String categoryCode, String appId, String iconUrl, boolean isShowTitle,
            String portalUrl, int type, String imgUrl, int sort, int score,
            String introduce, List<String> screenshotUrls, String hotCount,
            String commentCount, String praiseCount, UserInfo helperUser,
            String homePageUrl, String isForceVerify, String badgeCount,
            UserInfo developerUser) {
        this.id = id;
        this.appSecret = appSecret;
        this.name = name;
        this.categoryName = categoryName;
        this.categoryCode = categoryCode;
        this.appId = appId;
        this.iconUrl = iconUrl;
        this.isShowTitle = isShowTitle;
        this.portalUrl = portalUrl;
        this.type = type;
        this.imgUrl = imgUrl;
        this.sort = sort;
        this.score = score;
        this.introduce = introduce;
        this.screenshotUrls = screenshotUrls;
        this.hotCount = hotCount;
        this.commentCount = commentCount;
        this.praiseCount = praiseCount;
        this.helperUser = helperUser;
        this.homePageUrl = homePageUrl;
        this.isForceVerify = isForceVerify;
        this.badgeCount = badgeCount;
        this.developerUser = developerUser;
    }

    @Generated(hash = 1938344343)
    public AppItemBean() {
    }



    public boolean isFavorited() {
        if ("0".equals(isFavorited)) {
            return false;
        } else if ("1".equals(isFavorited)) {
            return true;
        }
        return false;
    }

    public void setIsFavorited(boolean isFavorited) {
        if (isFavorited) {
            this.isFavorited = "1";
        } else {
            this.isFavorited = "0";
        }
    }

    public boolean isPraised() {
        if ("0".equals(isPraised)) {
            return false;
        } else if ("1".equals(isPraised)) {
            return true;
        }
        return false;
    }

    public void setIsPraised(boolean isPraised) {
        if (isPraised) {
            this.isPraised = "1";
        } else {
            this.isPraised = "0";
        }
    }



    public String getAppSecret() {
        return this.appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public boolean getIsShowTitle() {
        return this.isShowTitle;
    }

    public void setIsShowTitle(boolean isShowTitle) {
        this.isShowTitle = isShowTitle;
    }

    public String getPortalUrl() {
        return this.portalUrl;
    }

    public void setPortalUrl(String portalUrl) {
        this.portalUrl = portalUrl;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getSort() {
        return this.sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getIsFavorited() {
        return this.isFavorited;
    }

    public void setIsFavorited(String isFavorited) {
        this.isFavorited = isFavorited;
    }

    public String getIntroduce() {
        return this.introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getIsPraised() {
        return this.isPraised;
    }

    public void setIsPraised(String isPraised) {
        this.isPraised = isPraised;
    }

    public List<String> getScreenshotUrls() {
        return this.screenshotUrls;
    }

    public void setScreenshotUrls(List<String> screenshotUrls) {
        this.screenshotUrls = screenshotUrls;
    }

    public String getHotCount() {
        return this.hotCount;
    }

    public void setHotCount(String hotCount) {
        this.hotCount = hotCount;
    }

    public String getCommentCount() {
        return this.commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getPraiseCount() {
        return this.praiseCount;
    }

    public void setPraiseCount(String praiseCount) {
        this.praiseCount = praiseCount;
    }

    public UserInfo getHelperUser() {
        return this.helperUser;
    }

    public void setHelperUser(UserInfo helperUser) {
        this.helperUser = helperUser;
    }

    public String getHomePageUrl() {
        return this.homePageUrl;
    }

    public void setHomePageUrl(String homePageUrl) {
        this.homePageUrl = homePageUrl;
    }

    public UserInfo getDeveloperUser() {
        return this.developerUser;
    }

    public void setDeveloperUser(UserInfo developerUser) {
        this.developerUser = developerUser;
    }

    public String getCategoryCode() {
        return this.categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsForceVerify() {
        return this.isForceVerify;
    }

    public void setIsForceVerify(String isForceVerify) {
        this.isForceVerify = isForceVerify;
    }

    public String getBadgeCount() {
        return this.badgeCount;
    }

    public void setBadgeCount(String badgeCount) {
        this.badgeCount = badgeCount;
    }

}
