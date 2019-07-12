package life.el.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOTFOUND("页面不存在");

    private String message;

    CustomizeErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
