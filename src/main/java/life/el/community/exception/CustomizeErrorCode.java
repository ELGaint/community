package life.el.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOTFOUND(2001,"问题页面不存在"),
    TARGET_PARAM_NOTFOUND(2002,"未选中任何问题或评论进行回复"),
    NO_LOGIN(2003,"当前页面未登录，请登录后重试"),
    SYSTEM_ERROE(2004,"233333333!!!"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"回复评论不存在");

    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
