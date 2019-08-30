package lollipop.com.sms.adapter;

import java.util.ArrayList;

/**
 *  短信页面展示的，表示
 */
public class SmsFatherModel {

    private String name;        // 发件人姓名

    private String phone;        // 发件人姓名

    private String content;     // 内容

    private String time;      // 时间

    private boolean isUnRead;   // 是否未读

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isUnRead() {
        return isUnRead;
    }

    public void setUnRead(boolean unRead) {
        isUnRead = unRead;
    }
}
