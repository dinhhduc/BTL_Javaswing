/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLTV.Model;

import QLTV.Domain.NhaXuatBan;
import QLTV.Domain.Sach;
import QLTV.Domain.TacGia;
import QLTV.Domain.Theloai;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
/**
 *
 * @author dinhd
 */
public class SachDAO {

    public List<TacGia> findAllTacGiaForSach() {
        List<TacGia> list = new ArrayList<>();
        String sql = "SELECT MaTG, TenTG FROM tacgia";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new TacGia(
                    rs.getString("MaTG"),
                    rs.getString("TenTG")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Theloai> findAllTheLoaiForSach() {
        List<Theloai> list = new ArrayList<>();
        String sql = "SELECT MaTL, TenTL FROM theloai";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Theloai(
                    rs.getString("MaTL"),
                    rs.getString("TenTL")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<NhaXuatBan> findAllNhaXuatBanForSach() {
        List<NhaXuatBan> list = new ArrayList<>();
        String sql = "SELECT MaNXB, TenNXB FROM nhaxuatban";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new NhaXuatBan(
                    rs.getString("MaNXB"),
                    rs.getString("TenNXB")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean checkTrungTenSach(String tenSach) {
        String sql = "SELECT 1 FROM sach WHERE TenSach = ? LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tenSach);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean checkTrungTenSachKhacMa(String tenSach, String maSach) {
        String sql = "SELECT 1 FROM sach WHERE TenSach = ? AND MaSach <> ? LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tenSach);
            ps.setString(2, maSach);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // ====== CRUD ======
    public List<Sach> findAll() {
        String sql = "SELECT MaSach, MaTG, MaNXB, MaTL, TenSach, NamXB, SoLuong, TinhTrang, MoTa, MaNN, MaViTri FROM sach";
        List<Sach> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(map(rs));
        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }

    public List<Sach> search(String keyword) {
        String sql = "SELECT s.MaSach, s.TenSach, s.NamXB, s.SoLuong, s.TinhTrang, s.MoTa, " +
                     "s.MaTG, tg.TenTG, s.MaNXB, nxb.TenNXB, s.MaTL, tl.TenTL, s.MaNN, nn.TenNN, s.MaViTri, vt.TenKe " +
                     "FROM sach s " +
                     "JOIN tacgia tg ON s.MaTG = tg.MaTG " +
                     "JOIN nhaxuatban nxb ON s.MaNXB = nxb.MaNXB " +
                     "JOIN theloai tl ON s.MaTL = tl.MaTL " +
                     "JOIN ngonngu nn ON s.MaNN = nn.MaNN " +
                     "JOIN vitri vt ON s.MaViTri = vt.MaViTri " +
                     "WHERE s.MaSach LIKE ? OR s.TenSach LIKE ? OR tg.TenTG LIKE ? OR nxb.TenNXB LIKE ? " +
                     "OR tl.TenTL LIKE ? OR nn.TenNN LIKE ? OR vt.TenKe LIKE ? OR s.NamXB = ? OR s.TinhTrang LIKE ?";
        List<Sach> list = new ArrayList<>();
        String k = "%" + keyword + "%";
        boolean isYear = keyword.trim().matches("\\d{4}");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, k);
            ps.setString(2, k);
            ps.setString(3, k);
            ps.setString(4, k);
            ps.setString(5, k);
            ps.setString(6, k);
            ps.setString(7, k);
            ps.setInt(8, isYear ? Integer.parseInt(keyword.trim()) : 0);
            ps.setString(9, k);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (Exception e) {
    JOptionPane.showMessageDialog(null, "SEARCH ERROR: " + e.getMessage());
    e.printStackTrace();}

        return list;
    }

    public int insert(Sach s) {
        String sql = "INSERT INTO sach(MaSach, MaTG, MaNXB, MaTL, TenSach, NamXB, SoLuong, TinhTrang, MoTa, MaNN, MaViTri) " +
                     "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            bind(ps, s);
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public int update(Sach s) {
        String sql = "UPDATE sach SET MaTG=?, MaNXB=?, MaTL=?, TenSach=?, NamXB=?, SoLuong=?, TinhTrang=?, MoTa=?, MaNN=?, MaViTri=? " +
                     "WHERE MaSach=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getMatg());
            ps.setString(2, s.getManxb());
            ps.setString(3, s.getMatl());
            ps.setString(4, s.getTensach());
            ps.setInt(5, s.getNamxb());
            if (s.getSoluong() == null) ps.setObject(6, null);
            else ps.setInt(6, s.getSoluong());
            ps.setString(7, s.getTinhtrang());
            ps.setString(8, s.getMota());
            ps.setString(9, s.getMann());
            ps.setString(10, s.getMavitri());
            ps.setString(11, s.getMasach());

            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public int delete(String maSach) {
        String sql = "DELETE FROM sach WHERE MaSach=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maSach);
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public String taoMaSachMoi() {
        String sql = "SELECT MaSach FROM sach ORDER BY MaSach DESC LIMIT 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (!rs.next()) return "S001";

            String maCu = rs.getString("MaSach"); // S006
            int so = Integer.parseInt(maCu.substring(1));
            so++;
            return String.format("S%03d", so);

        } catch (Exception e) { e.printStackTrace(); }

        return "S001";
    }

    private Sach map(ResultSet rs) throws Exception {
        return new Sach(
                rs.getString("MaSach"),
                rs.getString("MaTG"),
                rs.getString("MaNXB"),
                rs.getString("MaTL"),
                rs.getString("TenSach"),
                rs.getInt("NamXB"),
                (Integer) rs.getObject("SoLuong"),
                rs.getString("TinhTrang"),
                rs.getString("MoTa"),
                rs.getString("MaNN"),
                rs.getString("MaViTri")
        );
    }

    private void bind(PreparedStatement ps, Sach s) throws Exception {
        ps.setString(1, s.getMasach());
        ps.setString(2, s.getMatg());
        ps.setString(3, s.getManxb());
        ps.setString(4, s.getMatl());
        ps.setString(5, s.getTensach());
        ps.setInt(6, s.getNamxb());
        if (s.getSoluong() == null) ps.setObject(7, null);
        else ps.setInt(7, s.getSoluong());
        ps.setString(8, s.getTinhtrang());
        ps.setString(9, s.getMota());
        ps.setString(10, s.getMann());
        ps.setString(11, s.getMavitri());
    }
}
