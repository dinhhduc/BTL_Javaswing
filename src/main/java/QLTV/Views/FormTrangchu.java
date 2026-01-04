/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLTV.Views;

import QLTV.Domain.Dangnhap;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


/**
 *
 * @author dinhd
 */
public class FormTrangchu extends JFrame {
    private JPanel pnlContent = new JPanel(new BorderLayout());

    private JLabel lbTaiKhoan = new JLabel();
    private JLabel lbQuyen = new JLabel();

    public FormTrangchu(Dangnhap tk) {
        setTitle("Quản lý thư viện - UTT");
        setSize(1050, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createHeader(tk), BorderLayout.NORTH);
        add(createMenuLeft(tk), BorderLayout.WEST);

        pnlContent.setBackground(new Color(235, 242, 250));
        pnlContent.setBorder(new EmptyBorder(12, 12, 12, 12));
        pnlContent.add(new JLabel("Chọn chức năng bên trái", SwingConstants.CENTER), BorderLayout.CENTER);
        add(pnlContent, BorderLayout.CENTER);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);

    }

    private JPanel createHeader(Dangnhap tk) {
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(1000, 70));
        header.setBackground(new Color(25, 118, 210));
        header.setBorder(new EmptyBorder(10, 14, 10, 14));

        ImageIcon icon = new ImageIcon(
            getClass().getResource("/QLTV/Resource/logo.png")
        );
        Image img = icon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(img));


        JLabel title = new JLabel("HỆ THỐNG QUẢN LÝ THƯ VIỆN");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        left.setOpaque(false);
        left.add(logo);
        left.add(title);

        JLabel admin = new JLabel("Admin: " + safe(tk.getName()));
        admin.setForeground(Color.WHITE);
        admin.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        header.add(left, BorderLayout.WEST);
        header.add(admin, BorderLayout.EAST);
        return header;
    }

    private JPanel createMenuLeft(Dangnhap tk) {
        JPanel menu = new JPanel();
        menu.setPreferredSize(new Dimension(240, 0));
        menu.setBackground(new Color(230, 236, 243));
        menu.setLayout(new BorderLayout());
        menu.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.setLayout(new GridLayout(0, 1, 0, 8));

        JButton btnSach = menuButton("Quản lý Sách");
        JButton btnTheLoai = menuButton("Quản lý Thể Loại");
        JButton btnTacGia = menuButton("Quản lý Tác Giả");
        JButton btnNXB = menuButton("Quản lý Nhà Xuất Bản");
        JButton btnDocGia = menuButton("Quản lý Độc Giả");
        JButton btnNhanVien = menuButton("Quản lý Nhân Viên");
        JButton btnMuonTra = menuButton("Quản lý Mượn Trả");
        JButton btnViTri = menuButton("Quản lý Kệ Sách");
        JButton btnKhoa = menuButton("Quản lý Khoa");
        JButton btnLop = menuButton("Quản lý Lớp");
        JButton btnTheThuVien = menuButton("Quản lý Thẻ Thư Viện");
        JButton btnThongKe = menuButton("Thống Kê");

        btnPanel.add(btnSach);
        btnPanel.add(btnTheLoai);
        btnPanel.add(btnTacGia);
        btnPanel.add(btnNXB);
        btnPanel.add(btnDocGia);
        btnPanel.add(btnNhanVien);
        btnPanel.add(btnMuonTra);
        btnPanel.add(btnViTri);
        btnPanel.add(btnKhoa);
        btnPanel.add(btnLop);
        btnPanel.add(btnTheThuVien);
        btnPanel.add(btnThongKe);

        btnSach.addActionListener(e -> {
            pnlContent.removeAll();

            FormSach p = new FormSach();
            new QLTV.Controller.SachController(p);

            pnlContent.add(p, BorderLayout.CENTER);
            pnlContent.revalidate();
            pnlContent.repaint();
        });
        btnTheLoai.addActionListener(e -> {
            pnlContent.removeAll();

            FormTheloai p = new FormTheloai();
            new QLTV.Controller.TheloaiController(p);

            pnlContent.add(p, BorderLayout.CENTER);
            pnlContent.revalidate();
            pnlContent.repaint();
        });
        btnTacGia.addActionListener(e -> {
            pnlContent.removeAll();

            FormTacGia p = new FormTacGia();
            new QLTV.Controller.TacGiaController(p);

            pnlContent.add(p, BorderLayout.CENTER);
            pnlContent.revalidate();
            pnlContent.repaint();
        });
        btnNXB.addActionListener(e -> {
            pnlContent.removeAll();

            FormNXB p = new FormNXB();
            new QLTV.Controller.NhaXuatBanController(p);

            pnlContent.add(p, BorderLayout.CENTER);
            pnlContent.revalidate();
            pnlContent.repaint();
        });
        btnDocGia.addActionListener(e -> {
            pnlContent.removeAll();

            FormDocGia p = new FormDocGia();
            new QLTV.Controller.DocGiaController(p);

            pnlContent.add(p, BorderLayout.CENTER);
            pnlContent.revalidate();
            pnlContent.repaint();
        });
        btnNhanVien.addActionListener(e -> {
            pnlContent.removeAll();

            FormNhanVien p = new FormNhanVien();
            new QLTV.Controller.NhanVienController(p);

            pnlContent.add(p, BorderLayout.CENTER);
            pnlContent.revalidate();
            pnlContent.repaint();
        });
        btnMuonTra.addActionListener(e -> showScreen("Quản lý Mượn Trả"));
        btnViTri.addActionListener(e -> {
            pnlContent.removeAll();

            FormKeSach p = new FormKeSach();
            new QLTV.Controller.KeSachController(p);

            pnlContent.add(p, BorderLayout.CENTER);
            pnlContent.revalidate();
            pnlContent.repaint();
        });
        btnKhoa.addActionListener(e -> {
            pnlContent.removeAll();

            FormKhoa p = new FormKhoa();
            new QLTV.Controller.KhoaController(p);

            pnlContent.add(p, BorderLayout.CENTER);
            pnlContent.revalidate();
            pnlContent.repaint();
        });
        btnLop.addActionListener(e -> {
            pnlContent.removeAll();

            try {
                FormLop p = new FormLop();
                new QLTV.Controller.LopController(p);

                pnlContent.add(p, BorderLayout.CENTER);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Lỗi mở form Lớp: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

            pnlContent.revalidate();
            pnlContent.repaint();
        });
        btnThongKe.addActionListener(e -> {
           try{
               FormThongKe ftk = new FormThongKe();
               new QLTV.Controller.ThongKeController(ftk);
               pnlContent.add(ftk, BorderLayout.CENTER);
           } catch (Exception ex)
           {
               JOptionPane.showMessageDialog(this,
                        "Lỗi mở form: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
               ex.printStackTrace();
           }
        });
      
        btnTheThuVien.addActionListener(e -> {
            pnlContent.removeAll();

            FormTheThuVien p = new FormTheThuVien();
            new QLTV.Controller.TheThuVienController(p);

            pnlContent.add(p, BorderLayout.CENTER);
            pnlContent.revalidate();
            pnlContent.repaint();
        });
        btnThongKe.addActionListener(e -> showScreen("Thống Kê"));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new GridLayout(0, 1, 0, 4));
        info.setBorder(new EmptyBorder(10, 4, 0, 4));

        lbTaiKhoan.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbQuyen.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        lbTaiKhoan.setText("Tài khoản: " + safe(tk.getUsername()));
        lbQuyen.setText("Email: " + safe(tk.getEmail())); 

        info.add(new JSeparator());
        info.add(lbTaiKhoan);
        info.add(lbQuyen);

        menu.add(btnPanel, BorderLayout.NORTH);
        menu.add(info, BorderLayout.SOUTH);
        return menu;
    }

    private JButton menuButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBackground(new Color(165, 165, 165));
        b.setForeground(Color.BLACK);
        b.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(200, 38));
        return b;
    }

    private void showScreen(String title) {
        pnlContent.removeAll();
        JLabel lb = new JLabel(title, SwingConstants.CENTER);
        lb.setFont(new Font("Segoe UI", Font.BOLD, 22));
        pnlContent.add(lb, BorderLayout.CENTER);
        pnlContent.revalidate();
        pnlContent.repaint();
    }

    private String safe(String s) {
        return (s == null || s.trim().isEmpty()) ? "-" : s.trim();
    }
}
