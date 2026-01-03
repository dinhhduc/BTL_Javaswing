/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLTV.Controller;

import QLTV.Domain.DocGia;
import QLTV.Model.DocGiaDAO;
import QLTV.Model.KhoaDAO;
import QLTV.Model.LopDAO;
import QLTV.Views.FormDocGia;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */

public class DocGiaController {

    private final FormDocGia view;
    private final DocGiaDAO dao = new DocGiaDAO();
    private final KhoaDAO khoaDAO = new KhoaDAO();
    private final LopDAO lopDAO = new LopDAO();

    public DocGiaController(FormDocGia view) {
        this.view = view;
        initCombos();
        registerEvents();
        loadTable();
        view.setMaDG(dao.taoMaDGMoi());
    }

    private void initCombos() {
        // Load MaKhoa
        view.getCboMaKhoa().removeAllItems();
        List<String> maKhoas = khoaDAO.findAllMaKhoa();
        for (String mk : maKhoas) view.getCboMaKhoa().addItem(mk);

        // Load MaLop theo khoa đang chọn
        reloadLopBySelectedKhoa();
    }

    private void reloadLopBySelectedKhoa() {
        String maKhoa = (String) view.getCboMaKhoa().getSelectedItem();
        view.getCboMaLop().removeAllItems();
        if (maKhoa == null) return;

        List<String> maLops = lopDAO.findMaLopByMaKhoa(maKhoa);
        for (String ml : maLops) view.getCboMaLop().addItem(ml);
    }

    private void registerEvents() {
        view.getBtnThem().addActionListener(e -> handleInsert());
        view.getBtnSua().addActionListener(e -> handleUpdate());
        view.getBtnXoa().addActionListener(e -> handleDelete());
        view.getBtnLamMoi().addActionListener(e -> {
            view.clearForm();
            initCombos();
            view.setMaDG(dao.taoMaDGMoi());
        });

        view.getBtnNhapFile().addActionListener(e -> importCSVToTable());
        view.getBtnXuatFile().addActionListener(e -> exportTableToCSV());

        view.getBtnSearch().addActionListener(e -> handleSearch());
        view.getTxtSearch().addActionListener(e -> handleSearch());

        // cascade: chọn khoa -> load lớp
        view.getCboMaKhoa().addActionListener(e -> reloadLopBySelectedKhoa());

        view.getTblDG().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromSelectedRow();
        });
    }

    private void loadTable() {
        fillTable(dao.findAll());
    }

    private void fillTable(List<DocGia> list) {
        DefaultTableModel m = view.getModel();
        m.setRowCount(0);
        for (DocGia dg : list) {
            m.addRow(new Object[]{
                dg.getMaDG(), dg.getMaKhoa(), dg.getMaLop(), dg.getTenDG(),
                dg.getGioiTinh(), dg.getDiaChi(), dg.getEmail(), dg.getSdt()
            });
        }
    }

    private void handleSearch() {
        String key = view.getTxtSearch().getText().trim();
        if (key.isEmpty()) { loadTable(); return; }
        fillTable(dao.search(key));
    }

    private void handleInsert() {
        String ma = view.getMaDG();
        if (ma.isEmpty()) ma = dao.taoMaDGMoi();

        DocGia dg = readForm(ma);
        if (dg == null) return;

        if (dao.existsEmail(dg.getEmail(), "")) {
            JOptionPane.showMessageDialog(view, "Email đã tồn tại!");
            return;
        }
        if (dao.existsSdt(dg.getSdt(), "")) {
            JOptionPane.showMessageDialog(view, "SĐT đã tồn tại!");
            return;
        }

        int ok = dao.insert(dg);
        if (ok > 0) {
            JOptionPane.showMessageDialog(view, "Thêm độc giả thành công!");
            loadTable();
            view.clearForm();
            initCombos();
            view.setMaDG(dao.taoMaDGMoi());
        } else {
            JOptionPane.showMessageDialog(view, "Thêm thất bại! Có thể trùng mã hoặc dữ liệu không hợp lệ.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdate() {
        String ma = view.getMaDG();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chọn 1 dòng để sửa!");
            return;
        }

        DocGia dg = readForm(ma);
        if (dg == null) return;

        if (dao.existsEmail(dg.getEmail(), dg.getMaDG())) {
            JOptionPane.showMessageDialog(view, "Email đã tồn tại ở độc giả khác!");
            return;
        }
        if (dao.existsSdt(dg.getSdt(), dg.getMaDG())) {
            JOptionPane.showMessageDialog(view, "SĐT đã tồn tại ở độc giả khác!");
            return;
        }

        int ok = dao.update(dg);
        if (ok > 0) {
            JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
            loadTable();
        } else {
            JOptionPane.showMessageDialog(view, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        int row = view.getTblDG().getSelectedRow();
        String ma = view.getMaDG();

        if (ma.isEmpty() && row >= 0) ma = String.valueOf(view.getModel().getValueAt(row, 0));
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chọn 1 dòng hoặc có Mã độc giả để xóa.");
            return;
        }

        int cf = JOptionPane.showConfirmDialog(view, "Xóa độc giả " + ma + " ?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (cf != JOptionPane.YES_OPTION) return;

        int ok = dao.delete(ma);
        if (ok > 0) {
            JOptionPane.showMessageDialog(view, "Xóa thành công!");
            loadTable();
            view.clearForm();
            initCombos();
            view.setMaDG(dao.taoMaDGMoi());
        } else {
            JOptionPane.showMessageDialog(view, "Xóa thất bại! Độc giả có thể đang được phiếu mượn tham chiếu.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fillFormFromSelectedRow() {
        int row = view.getTblDG().getSelectedRow();
        if (row < 0) return;

        DefaultTableModel m = view.getModel();
        String maDG = String.valueOf(m.getValueAt(row, 0));
        String maKhoa = String.valueOf(m.getValueAt(row, 1));
        String maLop = String.valueOf(m.getValueAt(row, 2));
        String ten = String.valueOf(m.getValueAt(row, 3));
        String gt = String.valueOf(m.getValueAt(row, 4));
        String dc = String.valueOf(m.getValueAt(row, 5));
        String email = String.valueOf(m.getValueAt(row, 6));
        String sdt = String.valueOf(m.getValueAt(row, 7));

        // set khoa -> reload lớp -> set lớp
        view.getCboMaKhoa().setSelectedItem(maKhoa);
        reloadLopBySelectedKhoa();
        view.getCboMaLop().setSelectedItem(maLop);

        view.setForm(maDG, maKhoa, maLop, ten, gt, dc, email, sdt);
    }

    private DocGia readForm(String ma) {
        String maKhoa = view.getMaKhoa();
        String maLop = view.getMaLop();
        String ten = view.getTenDG();
        String gt = view.getGioiTinh();
        String dc = view.getDiaChi();
        String email = view.getEmail();
        String sdt = view.getSdt();

        if (maKhoa == null || maKhoa.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chọn Mã khoa!");
            return null;
        }
        if (maLop == null || maLop.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chọn Mã lớp!");
            return null;
        }
        if (ten.isEmpty() || dc.isEmpty() || email.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Không được để trống: Tên/Địa chỉ/Email/SĐT!");
            return null;
        }

        if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$", email)) {
            JOptionPane.showMessageDialog(view, "Email không hợp lệ!");
            return null;
        }
        if (!Pattern.matches("^\\d{10,11}$", sdt)) {
            JOptionPane.showMessageDialog(view, "SĐT không hợp lệ (chỉ số, 10-11 chữ số)!");
            return null;
        }

        return new DocGia(ma, maKhoa, maLop, ten, gt, dc, email, sdt);
    }

    // CSV giữ nguyên như bản trước (8 cột)
    private void importCSVToTable() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Chọn file CSV Độc Giả");
        int choose = fc.showOpenDialog(view);
        if (choose != JFileChooser.APPROVE_OPTION) return;

        try (BufferedReader br = new BufferedReader(new FileReader(fc.getSelectedFile()))) {
            String line;
            boolean firstLine = true;
            int count = 0;
            DefaultTableModel m = view.getModel();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                if (firstLine){
                    firstLine = false;
                    continue;
                }
                String[] p = line.split(",", -1);
                if (p.length < 8) continue;

                m.addRow(new Object[]{
                        p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(),
                        p[4].trim(), p[5].trim(), p[6].trim(), p[7].trim()
                });
                count++;
            }
            JOptionPane.showMessageDialog(view, "Nhập thành công " + count + " dòng (chỉ lên bảng, chưa lưu DB)!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Nhập file thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportTableToCSV() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Chọn nơi lưu file CSV Độc Giả");
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

            pw.print('\uFEFF'); // BOM
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
}
