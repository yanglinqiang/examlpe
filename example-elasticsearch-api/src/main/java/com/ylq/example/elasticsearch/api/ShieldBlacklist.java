package com.ylq.example.elasticsearch.api;

/**
 * Created by panda on 16/1/8.
 */
public class ShieldBlacklist {

    /**
     * 统计数据的时间区间,单位秒
     */
    private Integer blacklistPeriod;

    private String ip;

    private String errorCode;

    private Integer count;

    private Long timestamp;

    public Integer getBlacklistPeriod() {
        return blacklistPeriod;
    }

    public void setBlacklistPeriod(Integer blacklistPeriod) {
        this.blacklistPeriod = blacklistPeriod;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
