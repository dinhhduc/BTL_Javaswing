/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLTV.Model;

import QLTV.Domain.DocGia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */


public class DocGiaDAO {

    public List<DocGia> findAll() {
        String sql = "SELECT MaDG, MaKhoa, MaLop, TenDG, GioiTinh, DiaChi, Email, Sdt FROM docgia";
        List<DocGia> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(map(rs));
        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }

    public List<DocGia> search(String keyword) {
        String sql = "SELECT MaDG, MaKhoa, MaLop, TenDG, GioiTinh, DiaChi, Email, Sdt FROM docgia " +
                     "WHERE MaDG LIKE ? OR TenDG LIKE ? OR Email LIKE ? OR Sdt LIKE ? OR MaKhoa LIKE ? OR MaLop LIKE ?";
        List<DocGia> list = new ArrayList<>();
        String k = "%" + keyword + "%";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, k);
            ps.setString(2, k);
            ps.setString(3, k);
            ps.setString(4, k);
            ps.setString(5, k);
            ps.setString(6, k);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }

    public int insert(DocGia dg) {
        String sql = "INSERT INTO docgia(MaDG, MaKhoa, MaLop, TenDG, GioiTinh, DiaChi, Email, Sdt) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dg.getMaDG());
            ps.setString(2, dg.getMaKhoa());
            ps.setString(3, dg.getMaLop());
            ps.setString(4, dg.getTenDG());
            ps.setString(5, dg.getGioiTinh());
            ps.setString(6, dg.getDiaChi());
            ps.setString(7, dg.getEmail());
            ps.setString(8, dg.getSdt());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public int update(DocGia dg) {
        String sql = "UPDATE docgia SET MaKhoa=?, MaLop=?, TenDG=?, GioiTinh=?, DiaChi=?, Email=?, Sdt=? WHERE MaDG=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dg.getMaKhoa());
            ps.setString(2, dg.getMaLop());
            ps.setString(3, dg.getTenDG());
            ps.setString(4, dg.getGioiTinh());
            ps.setString(5, dg.getDiaChi());
            ps.setString(6, dg.getEmail());
            ps.setString(7, dg.getSdt());
            ps.setString(8, dg.getMaDG());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public int delete(String maDG) {
        String sql = "DELETE FROM docgia WHERE MaDG=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maDG);
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public String taoMaDGMoi() {
        String sql = "SELECT MaDG FROM docgia ORDER BY MaDG DESC LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (!rs.next()) return "DG001";
            String maCu = rs.getString("MaDG"); // DG005
            int so = Integer.parseInt(maCu.substring(2));
            so++;
            return String.format("DG%03d", so);
        } catch (Exception e) { e.printStackTrace(); }
        return "DG001";
    }

    // ===== check trùng để bắt lỗi =====
    public boolean existsEmail(String email, String excludeMaDG) {
        String sql = "SELECT 1 FROM docgia WHERE Email=? AND MaDG<>? LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, excludeMaDG == null ? "" : excludeMaDG);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean existsSdt(String sdt, String excludeMaDG) {
        String sql = "SELECT 1 FROM docgia WHERE Sdt=? AND MaDG<>? LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sdt);
            ps.setString(2, excludeMaDG == null ? "" : excludeMaDG);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    private DocGia map(ResultSet rs) throws Exception {
        return new DocGia(
                rs.getString("MaDG"),
                rs.getString("MaKhoa"),
                rs.getString("MaLop"),
                rs.getString("TenDG"),
                rs.getString("GioiTinh"),
                rs.getString("DiaChi"),
                rs.getString("Email"),
                rs.getString("Sdt")
        );
    }
}
