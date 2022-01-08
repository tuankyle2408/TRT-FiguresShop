package com.example.trt_figuresshop.Model;

import java.io.Serializable;

public class NewProduct implements Serializable {
    int id;
    String tenfigures;
    String mota;
    String hinh;
    String giafigure;
    int loai;


    public int getLoai() {
        return loai;
    }

    public void setLoai(int loai) {
        this.loai = loai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenfigures() {
        return tenfigures;
    }

    public void setTenfigures(String tenfigures) {
        this.tenfigures = tenfigures;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public String getGiafigure() {
        return giafigure;
    }

    public void setGiafigure(String giafigure) {
        this.giafigure = giafigure;
    }
}
