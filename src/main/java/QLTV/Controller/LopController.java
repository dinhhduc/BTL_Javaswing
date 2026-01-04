/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLTV.Controller;

import QLTV.Domain.Khoa;
import QLTV.Domain.Lop;
import QLTV.Model.KhoaDAO;
import QLTV.Model.LopDAO;
import QLTV.Views.FormLop;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */

public class LopController {
    private final FormLop view;
    private final LopDAO dao = new LopDAO();
    private final KhoaDAO khoaDAO = new KhoaDAO();

    public LopController(FormLop view) {
        this.view = view;
        initKhoaCombo();
        registerEvents();
        loadTable();

        view.setMaLop(dao.taoMaLopMoi());
    }
    private void initKhoaCombo() {
        view.getCboKhoa().removeAllItems();
        for (Khoa k : khoaDAO.findAll()) {
            view.getCboKhoa().addItem(k);
        }
    }

    private Lop readForm() {
        Khoa k = (Khoa) view.getCboKhoa().getSelectedItem();
        if (k == null) {
            JOptionPane.showMessageDialog(view, "Chọn khoa!");
            return null;
        }

        String maKhoa = k.getMaKhoa();
        String maLop = view.getMaLop();
        String tenLop = view.getTenLop();

        return new Lop(maLop, tenLop,maKhoa);
    }
    private void registerEvents() {
        view.getBtnThem().addActionListener(e -> handleInsert());
        view.getBtnSua().addActionListener(e -> handleUpdate());
        view.getBtnXoa().addActionListener(e -> handleDelete());

        view.getBtnLamMoi().addActionListener(e -> {
            view.clearForm();
            initKhoaCombo();
            view.setMaLop(dao.taoMaLopMoi());
        });

        view.getBtnSearch().addActionListener(e -> handleSearch());
        view.getTxtSearch().addActionListener(e -> handleSearch());

        view.getBtnNhapFile().addActionListener(e -> importCSVToTable());
        view.getBtnXuatFile().addActionListener(e -> exportTableToCSV());

        view.getTblLop().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromSelectedRow();
        });
    }

    private void loadTable() {
        fillTable(dao.findAll());
    }

    private void fillTable(List<Lop> list) {
        DefaultTableModel m = view.getModel();
        m.setRowCount(0);
        for (Lop lop : list) {
            m.addRow(new Object[]{lop.getMaLop(), lop.getTenLop(), lop.getMaKhoa()});
        }
    }

    private void handleSearch() {
        String key = view.getTxtSearch().getText().trim();
        if (key.isEmpty()) { loadTable(); return; }
        fillTable(dao.search(key));
    }

    private void handleInsert() {
        String ma = dao.taoMaLopMoi();
        String ten = view.getTenLop();
        String maKhoa = view.getMaKhoa();

        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Tên lớp không được để trống!");
            return;
        }
        if (maKhoa == null || maKhoa.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chọn Mã khoa!");
            return;
        }
        if (dao.checkTrungTenLop(ten)) {
            JOptionPane.showMessageDialog(view, "Tên lớp đã tồn tại!");
            return;
        }

        int ok = dao.insert(new Lop(ma, ten, maKhoa));
        if (ok > 0) {
            JOptionPane.showMessageDialog(view, "Thêm lớp thành công!");
            loadTable();
            view.clearForm();
            initKhoaCombo();
            view.setMaLop(dao.taoMaLopMoi());
        } else {
            JOptionPane.showMessageDialog(view, "Thêm thất bại! Có thể trùng mã.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdate() {
        String ma = view.getMaLop();
        String ten = view.getTenLop();
        String maKhoa = view.getMaKhoa();

        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chọn 1 dòng để sửa!");
            return;
        }
        if (dao.checkTrungTenLopKhacMa(ten, ma)) {
           JOptionPane.showMessageDialog(view, "Tên lớp đã tồn tại!");
           return;
        }
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Tên lớp không được để trống!");
            return;
        }
        if (maKhoa == null || maKhoa.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chọn Mã khoa!");
            return;
        }

        int ok = dao.update(new Lop(ma, ten, maKhoa));
        if (ok > 0) {
            JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
            loadTable();
        } else {
            JOptionPane.showMessageDialog(view, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        int row = view.getTblLop().getSelectedRow();
        String ma = view.getMaLop();

        if (ma.isEmpty() && row >= 0) ma = String.valueOf(view.getModel().getValueAt(row, 0));
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chọn 1 dòng để xóa.");
            return;
        }

        int cf = JOptionPane.showConfirmDialog(view, "Xóa lớp " + ma + " ?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (cf != JOptionPane.YES_OPTION) return;

        int ok = dao.delete(ma);
        if (ok > 0) {
            JOptionPane.showMessageDialog(view, "Xóa thành công!");
            loadTable();
            view.clearForm();
            initKhoaCombo();
            view.setMaLop(dao.taoMaLopMoi());
        } else {
            JOptionPane.showMessageDialog(view, "Xóa thất bại! Lớp có thể đang được độc giả tham chiếu.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fillFormFromSelectedRow() {
        int row = view.getTblLop().getSelectedRow();
            if (row < 0) return;

        DefaultTableModel m = view.getModel();
        String maLop = m.getValueAt(row, 0).toString();
        String tenLop = m.getValueAt(row, 1).toString();
        String maKhoa = m.getValueAt(row, 2).toString();


        setSelectedKhoa(maKhoa);
        view.setForm(maLop, tenLop);
    }

    private void importCSVToTable() {
    JFileChooser fc = new JFileChooser();
    if (fc.showOpenDialog(view) != JFileChooser.APPROVE_OPTION) return;

    List<Lop> dbList = dao.findAll();

    int insert = 0, skip = 0, dup = 0;

    try (BufferedReader br = new BufferedReader(new FileReader(fc.getSelectedFile()))) {
        String line;
        boolean header = true;

        while ((line = br.readLine()) != null) {
            if (header) { header = false; continue; }
            if (line.trim().isEmpty()) continue;

            String[] p = line.split(",", -1);
            if (p.length < 3) continue;

            String ma = p[0].trim();
            String ten = p[1].trim();
            String maKhoa = p[2].trim();

            if (ma.isEmpty() || ten.isEmpty() || maKhoa.isEmpty()) continue;

            boolean same = false, dupMa = false, dupTen = false;

            for (Lop l : dbList) {
                if (l.getMaLop().equals(ma)
                 && l.getTenLop().equals(ten)
                 && l.getMaKhoa().equals(maKhoa)) {
                    same = true; break;      
                }
                if (l.getMaLop().equals(ma)) dupMa = true;
                if (l.getTenLop().equals(ten)) dupTen = true;
            }

            if (same) { skip++; continue; }
       
                if (dupMa || dupTen || dao.checkTrungTenLop(ten)) {
                    dup++;
                    continue;
                }


            Lop lop = new Lop(ma, ten, maKhoa);
            dao.insert(lop);
            dbList.add(lop);
            insert++;
        }

        loadTable();
        view.clearForm();
        view.setMaLop(dao.taoMaLopMoi());

        JOptionPane.showMessageDialog(
            view,
            "Import xong!\n"
          + "Thêm: " + insert + "\n"
          + "Bỏ qua: " + skip + "\n"
          + "Trùng: " + dup
        );

    } catch (Exception e) {
        JOptionPane.showMessageDialog(view, "Lỗi nhập file!");
    }
}


    private void exportTableToCSV() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Chọn nơi lưu file CSV Lớp");
        int choose = fc.showSaveDialog(view);
        if (choose != JFileChooser.APPROVE_OPTION) return;

        java.io.File file = fc.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".csv")) {
            file = new java.io.File(file.getAbsolutePath() + ".csv");
        }

        try (java.io.OutputStream os = new java.io.FileOutputStream(file);
             java.io.OutputStreamWriter osw = new java.io.OutputStreamWriter(os, java.nio.charset.StandardCharsets.UTF_8);
             java.io.BufferedWriter bw = new java.io.BufferedWriter(osw);
             java.io.PrintWriter pw = new java.io.PrintWriter(bw)) {

            pw.print('\uFEFF');
            DefaultTableModel m = view.getModel();

            for (int c = 0; c < m.getColumnCount(); c++) {
                pw.print(m.getColumnName(c));
                if (c < m.getColumnCount() - 1) pw.print(",");
            }
            pw.println();

            for (int r = 0; r < m.getRowCount(); r++) {
                for (int c = 0; c < m.getColumnCount(); c++) {
                    Object val = m.getValueAt(r, c);
                    String s = (val == null) ? "" : String.valueOf(val);

                    if (s.contains(",") || s.contains("\"")) {
                        s = s.replace("\"", "\"\"");
                        s = "\"" + s + "\"";
                    }

                    pw.print(s);
                    if (c < m.getColumnCount() - 1) pw.print(",");
                }
                pw.println();
            }

            JOptionPane.showMessageDialog(view, "Xuất CSV thành công!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Xuất CSV thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void setSelectedKhoa(String maKhoa) {
    JComboBox<Khoa> cbo = view.getCboKhoa();
        for (int i = 0; i < cbo.getItemCount(); i++) {
            if (cbo.getItemAt(i).getMaKhoa().equals(maKhoa)) {
                cbo.setSelectedIndex(i);
                break;
            }
        }
    }
}
