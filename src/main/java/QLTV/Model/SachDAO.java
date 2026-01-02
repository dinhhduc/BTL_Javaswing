/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLTV.Model;

import QLTV.Domain.Sach;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author dinhd
 */
public class SachDAO {

    public List<Sach> findAll() {
        String sql = "SELECT MaSach, MaTG, MaNXB, MaTL, TenSach, NamXB, SoLuong, TinhTrang, MoTa, MaNN, MaViTri FROM sach";
        List<Sach> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Sach> search(String keyword) {
        String sql = "SELECT MaSach, MaTG, MaNXB, MaTL, TenSach, NamXB, SoLuong, TinhTrang, MoTa, MaNN, MaViTri " +
                     "FROM sach WHERE MaSach LIKE ? OR TenSach LIKE ?";
        List<Sach> list = new ArrayList<>();
        String k = "%" + keyword + "%";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, k);
            ps.setString(2, k);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insert(Sach s) {
        String sql = "INSERT INTO sach(MaSach, MaTG, MaNXB, MaTL, TenSach, NamXB, SoLuong, TinhTrang, MoTa, MaNN, MaViTri) " +
                     "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            bind(ps, s);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(String maSach) {
        String sql = "DELETE FROM sach WHERE MaSach=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maSach);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
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
    
    public String taoMaSachMoi() {
    String sql = "SELECT MaSach FROM sach ORDER BY MaSach DESC LIMIT 1";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (!rs.next()) {
            return "S001";
        }

        String maCu = rs.getString("MaSach"); 
        int so = Integer.parseInt(maCu.substring(1)); 
        so++;

        return String.format("S%03d", so); 

    } catch (Exception e) {
        e.printStackTrace();
    }

    return "S001";
}

}
