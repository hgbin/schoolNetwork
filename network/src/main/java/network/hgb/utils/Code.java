package network.hgb.utils;

/**
 * @author HUAWEI - Hinsane
 * @create 2022/5/29 13:43
 */
public enum Code {
    SuccessCode("200","访问后台成功"),
    FailCode("500","网络异常"),
    NotFoundCode("404","网络异常");

    public final String code;
    public final String desc;

    public String getDesc() {
        return desc;
    }

    Code(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

}
