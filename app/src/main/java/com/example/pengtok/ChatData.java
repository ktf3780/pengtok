package com.example.pengtok;

import java.sql.Timestamp;

public class ChatData {
    private String msg;
    private String nickname;
    private String time;
    public ChatData(){}


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ChatData(String msg, String nickname, String time) {
        this.msg = msg;
        this.nickname = nickname;
        this.time = time;
    }

    @Override
    public String toString() {
        return "ChatData{" +
                "msg='" + msg + '\'' +
                ", nickname='" + nickname + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
