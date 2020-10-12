package com.example.pengtok;

import java.sql.Timestamp;

public class ChatData {
    private String msg;
    private String destinationUid;
    private String time;
    public ChatData(){}


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ChatData(String msg, String nickname, String time) {
        this.msg = msg;
        this.destinationUid = nickname;
        this.time = time;
    }

    public String getDestinationUid() {
        return destinationUid;
    }

    public void setDestinationUid(String destinationUid) {
        this.destinationUid = destinationUid;
    }

    @Override
    public String toString() {
        return "ChatData{" +
                "msg='" + msg + '\'' +
                ", destinationUid='" + destinationUid + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
