package com.ricardo.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    EXPORT_ERROR(518,"不允许访问"),
    USERNAME_EXIST(501,"用户名已存在"),
     PHONENUMBER_EXIST(502,"手机号已存在"),
     TAG_NAME_EXIST(515,"标签已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    CONTENT_NOT_NULL(506,"评论内容不能为空" ),
    FILE_TYPE_ERROR(507,"文件类型错误，请上传png、jpg文件" ),
    UserNAME_NOT_NUll(508,"用户名不能为空" ),
    NICKNAME_NOT_NUll(509,"昵称不能为空" ),
    PASSWORD_NOT_NUll(510,"密码不能为空" ),
    EMAIL_NOT_NUll(511,"邮箱不能为空" ),
    TAG_NAME_NOT_NUll(513,"标签不能为空" ),
    TAG_REMARK_NOT_NUll(514,"描述不能为空" ),
    CATE_NAME_NOT_NULL(515,"分类名不能为空"),
    CATE_NAME_EXIST(517,"分类名已存在"),
    CATE_DES_NOT_NULL(516,"描述不能为空"),
    NICKNAME_EXIST(512,"昵称已存在" );
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
