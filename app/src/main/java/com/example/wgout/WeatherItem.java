package com.example.wgout;

public class WeatherItem {
    private String date;
    private String time;

    private int code;

    private String POP; //강수확률
    private String PTY; //강수형태
    private String RN1; //1시간 강수량
    private String SKY; //하늘상태
    private String T1H; //기온
    private String REH; //습도

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getPOP() {
        return POP;
    }

    public void setPOP(String POP) {
        this.POP = POP;
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
