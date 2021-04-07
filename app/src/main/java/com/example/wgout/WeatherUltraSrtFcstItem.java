package com.example.wgout;

public class WeatherUltraSrtFcstItem {
    private String date;
    private String time;

    private String PTY;
    private String RN1;
    private String SKY;
    private String T1H;
    private String REH;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getPTY() {
        return PTY;
    }

    public void setPTY(String PTY) {
        this.PTY = PTY;
    }

    public String getRN1() {
        return RN1;
    }

    public void setRN1(String RN1) {
        this.RN1 = RN1;
    }

    public String getSKY() {
        return SKY;
    }

    public void setSKY(String SKY) {
        this.SKY = SKY;
    }

    public String getT1H() {
        return T1H;
    }

    public void setT1H(String t1H) {
        T1H = t1H;
    }

    public String getREH() {
        return REH;
    }

    public void setREH(String REH) {
        this.REH = REH;
    }
}
