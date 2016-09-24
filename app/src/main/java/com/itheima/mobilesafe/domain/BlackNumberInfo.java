package com.itheima.mobilesafe.domain;

/**
 * 黑名单
 * Created by billow on 2016/9/21.
 */
public class BlackNumberInfo {

    private String number;
    private String mode;
    private String displayName;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "BlackNumberInfo{" +
                "number='" + number + '\'' +
                ", mode='" + mode + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
