/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLTV.Model;

import QLTV.Domain.TacGia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author dinhd
 */
public class TacGiaDAO {

    public static List<TacGia> findAll() {
        String sql = "SELECT MaTG, TenTG, NamSinh, GioiTinh, QuocTich FROM tacgia";
        List<TacGia> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(map(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<TacGia> search(String keyword) {
        String sql = "SELECT MaTG, TenTG, NamSinh, GioiTinh, QuocTich " +
                     "FROM tacgia WHERE MaTG LIKE ? OR TenTG LIKE ? OR GioiTinh LIKE ? or NamSinh = ? OR QuocTich LIKE ?";
        List<TacGia> list = new ArrayList<>();
        String k = "%" + keyword + "%";
        boolean isYear = keyword.trim().matches("\\d{4}");
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, k);
            ps.setString(2, k);
            ps.setString(3, k);
            ps.setInt(4, isYear ? Integer.parseInt(keyword.trim()) : 0);
            ps.setString(5, k);
            

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    public boolean checkTrungTacGia(String tentg) {
        String sql = "SELECT 1 FROM tacgia WHERE TenTG = ? LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tentg);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean checkTrungTenTacGiaKhacMa(String tentg, String matg) {
        String sql = "SELECT 1 FROM tacgia WHERE TenTG = ? AND MaTG <> ? LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tentg);
            ps.setString(2, matg);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public int insert(TacGia tg) {
        String sql = "INSERT INTO tacgia(MaTG, TenTG, NamSinh, GioiTinh, QuocTich) VALUES(?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tg.getMaTG());
            ps.setString(2, tg.getTenTG());
            if (tg.getNamSinh() == null) ps.setObject(3, null);
            else ps.setInt(3, tg.getNamSinh());
            ps.setString(4, tg.getGioiTinh());
            ps.setString(5, tg.getQuocTich());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public int update(TacGia tg) {
        String sql = "UPDATE tacgia SET TenTG=?, NamSinh=?, GioiTinh=?, QuocTich=? WHERE MaTG=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tg.getTenTG());
            if (tg.getNamSinh() == null) ps.setObject(2, null);
            else ps.setInt(2, tg.getNamSinh());
            ps.setString(3, tg.getGioiTinh());
            ps.setString(4, tg.getQuocTich());
            ps.setString(5, tg.getMaTG());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public int delete(String maTG) {
        String sql = "DELETE FROM tacgia WHERE MaTG=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maTG);
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public String taoMaTGMoi() {
        // DB bạn đang có TG01..TG06 (2 số). Mình tạo theo format TG%02d
        String sql = "SELECT MaTG FROM tacgia ORDER BY MaTG DESC LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (!rs.next()) return "TG001";
            String maCu = rs.getString("MaTG"); // TG06
            int so = Integer.parseInt(maCu.substring(2));
            so++;
            return String.format("TG%03d", so);
        } catch (Exception e) { e.printStackTrace(); }
        return "TG001";
    }
    public class ComboItem {
    private String key;
    private String value;

    public ComboItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() { return key; }
    public String getValue() { return value; }

    @Override
    public String toString() {
        return value; // hiển thị Tên
    }
}

    public List<ComboItem> findAllTacGiaItem() {
    String sql = "SELECT MaTG, TenTG FROM tacgia ORDER BY TenTG";
    List<ComboItem> list = new ArrayList<>();
    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            list.add(new ComboItem(
                rs.getString("MaTG"),
                rs.getString("TenTG")
            ));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}


    public List<String> getAllGioiTinh() {
        String sql = "SELECT DISTINCT GioiTinh FROM tacgia ORDER BY GioiTinh";
        List<String> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String v = rs.getString(1);
                if (v != null && !v.trim().isEmpty()) list.add(v.trim());
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<String> getAllQuocTich() {
        String sql = "SELECT DISTINCT QuocTich FROM tacgia ORDER BY QuocTich";
        List<String> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String v = rs.getString(1);
                if (v != null && !v.trim().isEmpty()) list.add(v.trim());
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    private static TacGia map(ResultSet rs) throws Exception {
        return new TacGia(
                rs.getString("MaTG"),
                rs.getString("TenTG"),
                (Integer) rs.getObject("NamSinh"),
                rs.getString("GioiTinh"),
                rs.getString("QuocTich")
        );
    }
}
