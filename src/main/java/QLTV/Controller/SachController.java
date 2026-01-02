/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLTV.Controller;

import QLTV.Domain.Sach;
import QLTV.Model.SachDAO;
import QLTV.Views.FormSach;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author dinhd
 */
public class SachController {

    private final FormSach view;
    private final SachDAO dao = new SachDAO();

    public SachController(FormSach view) {
        this.view = view;
        registerEvents();
        loadTable();
        
        view.setMaSach(dao.taoMaSachMoi());

    }

    private void registerEvents() {
        // CRUD
        
        view.getBtnThem().addActionListener(e -> handleInsert());
        view.getBtnSua().addActionListener(e -> handleUpdate());
        view.getBtnXoa().addActionListener(e -> handleDelete());
        view.getBtnLamMoi().addActionListener(e -> view.clearForm());

        // Import CSV -> thêm vào bảng (chỉ demo UI)
        view.getBtnNhapFile().addActionListener(e -> importCSVToTable());

        // Click row -> đổ dữ liệu lên form
        view.getTblSach().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromSelectedRow();
        });

        // Tìm kiếm (nếu bạn đã thêm getter btnSearch + txtSearch)
        try {
            view.getBtnSearch().addActionListener(e -> handleSearch());
            view.getTxtSearch().addActionListener(e -> handleSearch()); // Enter để tìm
        } catch (Exception ignored) {
            // Nếu bạn chưa thêm getter btnSearch/txtSearch thì thôi
        }
    }

    // ================= LOAD TABLE =================
    private void loadTable() {
        List<Sach> list = dao.findAll();
        fillTable(list);
    }

    private void fillTable(List<Sach> list) {
        DefaultTableModel m = view.getModel();
        m.setRowCount(0);
        for (Sach s : list) {
            m.addRow(new Object[]{
                    s.getMasach(),
                    s.getTensach(),
                    s.getMatg(),     // đang hiển thị mã TG
                    s.getMatl(),     // mã TL
                    s.getManxb(),    // mã NXB
                    s.getNamxb(),
                    s.getSoluong() == null ? "" : s.getSoluong()
            });
        }
    }

    // ================= SEARCH =================
    private void handleSearch() {
        String key = view.getTxtSearch().getText().trim();
        if (key.isEmpty()) {
            loadTable();
            return;
        }
        fillTable(dao.search(key));
    }

    // ================= INSERT =================
    private void handleInsert() {
        
        String ma = view.getMaSach();
        if (ma.isEmpty()) {
            ma = dao.taoMaSachMoi();
        }
        
        Sach s = readFormToSach(true);
        if (s == null) return;

        int ok = dao.insert(s);
        if (ok > 0) {
            JOptionPane.showMessageDialog(view, "Thêm sách thành công!");
            loadTable();
            view.clearForm();
        } else {
            JOptionPane.showMessageDialog(view, "Thêm thất bại! Có thể trùng Mã sách hoặc sai mã FK.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================= UPDATE =================
    private void handleUpdate() {
        Sach s = readFormToSach(true);
        if (s == null) return;

        int ok = dao.update(s);
        if (ok > 0) {
            JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
            loadTable();
        } else {
            JOptionPane.showMessageDialog(view, "Cập nhật thất bại! Kiểm tra Mã sách có tồn tại không.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================= DELETE =================
    private void handleDelete() {
        int row = view.getTblSach().getSelectedRow();
        String ma = view.getMaSach();

        if (ma.isEmpty() && row >= 0) ma = String.valueOf(view.getModel().getValueAt(row, 0));
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chọn 1 dòng hoặc nhập Mã sách để xóa.");
            return;
        }

        int cf = JOptionPane.showConfirmDialog(view, "Xóa sách " + ma + " ?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (cf != JOptionPane.YES_OPTION) return;

        int ok = dao.delete(ma);
        if (ok > 0) {
            JOptionPane.showMessageDialog(view, "Xóa thành công!");
            loadTable();
            view.clearForm();
        } else {
            JOptionPane.showMessageDialog(view, "Xóa thất bại! Sách có thể đang bị tham chiếu (mượn trả/chi tiết...).", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================= TABLE -> FORM =================
    private void fillFormFromSelectedRow() {
        int row = view.getTblSach().getSelectedRow();
        if (row < 0) return;

        DefaultTableModel m = view.getModel();
        // đổ vào các ô (FormSach của bạn đang có get... nhưng chưa có set... nên ta set qua clearForm + reflection? => cách dễ là thêm setText methods trong View)
        // Vì View hiện không có setter/get field trực tiếp, ta dùng cách: tạo thêm hàm setForm(...) trong FormSach.
        // Nếu bạn chưa có, thì làm cách tối giản: yêu cầu bạn thêm các setter/getter cho textfield.
        // => Mình đưa luôn cách dùng "public void setForm(...)" ở dưới.
        try {
            view.setForm(
                    String.valueOf(m.getValueAt(row, 0)),
                    String.valueOf(m.getValueAt(row, 1)),
                    String.valueOf(m.getValueAt(row, 2)),
                    String.valueOf(m.getValueAt(row, 3)),
                    String.valueOf(m.getValueAt(row, 4)),
                    String.valueOf(m.getValueAt(row, 5)),
                    String.valueOf(m.getValueAt(row, 6))
            );
        } catch (Exception ex) {
            // Nếu bạn chưa thêm setForm, bạn thêm theo hướng dẫn ở mục 3 bên dưới.
        }
    }

    // ================= FORM -> ENTITY =================
    private Sach readFormToSach(boolean requireMaSach) {
        String ma = view.getMaSach();
        String ten = view.getTenSach();
        String maTG = view.getTacGia();    // coi là MaTG
        String maTL = view.getTheLoai();   // coi là MaTL
        String maNXB = view.getNXB();      // coi là MaNXB
        String namStr = view.getNamXB();
        String slStr = view.getSoLuong();

        if (requireMaSach && ma.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Mã sách không được để trống!");
            return null;
        }
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Tên sách không được để trống!");
            return null;
        }
        if (maTG.isEmpty() || maTL.isEmpty() || maNXB.isEmpty()) {
            JOptionPane.showMessageDialog(view, "MaTG / MaTL / MaNXB không được để trống!");
            return null;
        }

        int nam;
        try {
            nam = Integer.parseInt(namStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Năm XB phải là số!");
            return null;
        }

        Integer soLuong = null;
        if (!slStr.isEmpty()) {
            try {
                soLuong = Integer.parseInt(slStr);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Số lượng phải là số!");
                return null;
            }
        }

        // Entity Sach của bạn có thêm các trường DB: TinhTrang, MoTa, MaNN, MaViTri
        // Form hiện chưa nhập 4 trường này => gán mặc định để insert không lỗi (tùy DB bạn cho phép null/DEFAULT)
        String tinhTrang = "Tốt";
        String moTa = "";
        String maNN = "NN01";
        String maViTri = "VT01";

        return new Sach(ma, maTG, maNXB, maTL, ten, nam, soLuong, tinhTrang, moTa, maNN, maViTri);
    }

    // ================= CSV -> TABLE (demo UI) =================
    // CSV mẫu: MaSach,TenSach,MaTG,MaTL,MaNXB,NamXB,SoLuong
    private void importCSVToTable() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Chọn file CSV danh sách sách");

        int choose = fc.showOpenDialog(view);
        if (choose != JFileChooser.APPROVE_OPTION) return;

        try (BufferedReader br = new BufferedReader(new FileReader(fc.getSelectedFile()))) {
            String line;
            int count = 0;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(",", -1);
                if (p.length < 7) continue;

                view.getModel().addRow(new Object[]{
                        p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(),
                        p[4].trim(), p[5].trim(), p[6].trim()
                });
                count++;
            }

            JOptionPane.showMessageDialog(view, "Nhập thành công " + count + " dòng (chỉ lên bảng, chưa lưu DB)!", "OK", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Nhập file thất bại! Kiểm tra định dạng CSV.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}