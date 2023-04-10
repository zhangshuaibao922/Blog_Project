package com.ricardo.constants;

public class SystemConstants {
    /**
     *  文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     *  文章是正常分布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;
    public static final String STATUS_NORMAL="0";

    /** 正常状态 */
    public static final String NORMAL = "0";
    public static final String ADMIN = "1";

    /**
     * 友链状态为审核通过
     */
    public static final String LINK_STATUS_NORMAL="0";

    public static final String REDIS_CACHE_KEY="bloglogin:";
    public static final String ADMIN_REDIS_CACHE_KEY="login:";

    /**
     * 根评论
     */
    public static final int COMMENT_ROOT= -1;

    /**
     * 评论类型为：文章评论
     */
    public static final String ARTICLE_COMMENT = "0";
    /**
     * 评论类型为：友联评论
     */
    public static final String LINK_COMMENT = "1";
    /**
     * 测试域名
     */
    public static final String  CDN="rrx9s1pne.hn-bkt.clouddn.com";

    public static final String VIEW_COUNT="Article:viewCount";

    public static final String MENU = "C";
    public static final String BUTTON = "F";
}