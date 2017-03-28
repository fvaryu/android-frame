package com.lowett.core.net.internal;

/**
 * Created by Hyu on 2016/7/11.
 * Email: fvaryu@qq.com
 */
public class ResponseData {


    /**
     * success : true
     * level : info
     * type : normal
     * code : 00
     * message : null
     * data : 5514
     */

    private boolean success;
    private String level;
    private String type;
    private String code;
    private String bizType;
    private String message = "网络异常,请稍后再试";

    public ResponseData() {
    }

    public ResponseData(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "success=" + success +
                ", level='" + level + '\'' +
                ", type='" + type + '\'' +
                ", code='" + code + '\'' +
                ", bizType='" + bizType + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
