/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLTV.Domain;
import java.sql.Date;
/**
 *
 * @author khanh
 */
public class TK_QuaHan {
    private String maMT;
    private String tenDG;
    private Date hanTra;
    private int soNgayQuaHan;

    public TK_QuaHan() {
    }
    
    public TK_QuaHan(String maMT, String tenDG, Date hanTra, int soNgayQuaHan) {
        this.maMT = maMT;
        this.tenDG = tenDG;
        this.hanTra = hanTra;
        this.soNgayQuaHan = soNgayQuaHan;
    }

    public String getMaMT() {
        return maMT;
    }

    public void setMaMT(String maMT) {
        this.maMT = maMT;
    }

    public String getTenDG() {
        return tenDG;
    }

    public void setTenDG(String tenDG) {
        this.tenDG = tenDG;
    }

    public Date getHanTra() {
        return hanTra;
    }

    public void setHanTra(Date hanTra) {
        this.hanTra = hanTra;
    }

    public int getSoNgayQuaHan() {
        return soNgayQuaHan;
    }

    public void setSoNgayQuaHan(int soNgayQuaHan) {
        this.soNgayQuaHan = soNgayQuaHan;
    }

}
