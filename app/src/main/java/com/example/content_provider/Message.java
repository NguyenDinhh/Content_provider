package com.example.content_provider;

import java.io.Serializable;

public class Message implements Serializable {
    private  String sdt,thoigian,nd;

    public Message(String sdt, String nd, String thoigian) {
        this.sdt = sdt;
        this.nd = nd;
        this.thoigian = thoigian;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getThoigian() {
        return thoigian;
    }

    public void setThoigian(String thoigian) {
        this.thoigian = thoigian;
    }

    public String getNd() {
        return nd;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sdt='" + sdt + '\'' +
                ", thoigian='" + thoigian + '\'' +
                ", nd='" + nd + '\'' +
                '}';
    }
}
