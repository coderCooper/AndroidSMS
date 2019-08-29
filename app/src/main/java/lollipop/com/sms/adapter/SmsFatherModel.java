package lollipop.com.sms.adapter;

import java.util.ArrayList;

public class SmsFatherModel {

    private String name;

    private ArrayList<SmsModel> sms;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<SmsModel> getSms() {
        return sms;
    }

    public void setSms(ArrayList<SmsModel> sms) {
        this.sms = sms;
    }
}
