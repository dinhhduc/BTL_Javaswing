/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLTV.Controller;

import QLTV.Domain.NhaXuatBan;
import QLTV.Domain.Sach;
import QLTV.Domain.TacGia;
import QLTV.Domain.Theloai;
import QLTV.Model.SachDAO;
import QLTV.Views.FormSach;
import QLTV.Model.TacGiaDAO;

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

        initTacGiaCombo();
        initTheLoaiCombo();
        initNXBCombo();

        registerEvents();
        loadTable();

        view.setMaSach(dao.taoMaSachMoi());
    }

    private void registerEvents() {
        view.getBtnThem().addActionListener(e -> handleInsert());
        view.getBtnSua().addActionListener(e -> handleUpdate());
        view.getBtnXoa().addActionListener(e -> handleDelete());
        view.getBtnLamMoi().addActionListener(e -> {
            view.clearForm();
            view.setMaSach(dao.taoMaSachMoi());
        });

        view.getBtnNhapFile().addActionListener(e -> importCSVToTable());
        view.getBtnXuatFile().addActionListener(e -> exportTableToCSV());

        view.getTblSach().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromSelectedRow();
        });

        try {
            view.getBtnSearch().addActionListener(e -> handleSearch());
            view.getTxtSearch().addActionListener(e -> handleSearch());
        } catch (Exception ignored) {}
    }

    private void initTacGiaCombo() {
        view.getCboTacGia().removeAllItems();
        for (TacGia tg : dao.findAllTacGiaForSach()) {
            view.getCboTacGia().addItem(tg);
        }
    }

    private void initTheLoaiCombo() {
        view.getCboTheLoai().removeAllItems();
        for (Theloai tl : dao.findAllTheLoaiForSach()) {
            view.getCboTheLoai().addItem(tl);
        }
    }

    private void initNXBCombo() {
        view.getCboNXB().removeAllItems();
        for (NhaXuatBan nxb : dao.findAllNhaXuatBanForSach()) {
            view.getCboNXB().addItem(nxb);
        }    }

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
                    s.getMatg(),
                    s.getMatl(),
                    s.getManxb(),
                    s.getNamxb(),
                    s.getSoluong() == null ? "" : s.getSoluong()
            });
        }
    }

    private void handleSearch() {
        String key = view.getTxtSearch().getText().trim();
        if (key.isEmpty()) {
            loadTable();
            return;
        }
        fillTable(dao.search(key));
    }

    private void handleInsert() {
        String ma = view.getMaSach();
        if (ma.isEmpty()) ma = dao.taoMaSachMoi();

        String tenSach = view.getTenSach();
        if (!tenSach.isEmpty() && dao.checkTrungTenSach(tenSach)) {
            JOptionPane.showMessageDialog(view, "Tên sách đã tồn tại!");
            return;
        }

        Sach s = readFormToSach(true);
        if (s == null) return;

        int ok = dao.insert(s);
        if (ok > 0) {
            JOptionPane.showMessageDialog(view, "Thêm sách thành công!");
            loadTable();
            view.clearForm();
            view.setMaSach(dao.taoMaSachMoi());
        } else {
            JOptionPane.showMessageDialog(view, "Thêm thất bại! Có thể trùng Mã sách hoặc sai mã FK.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdate() {
        String ma = view.getMaSach();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chọn 1 dòng để sửa!");
            return;
        }

        String tenSach = view.getTenSach();
        if (!tenSach.isEmpty() && dao.checkTrungTenSachKhacMa(tenSach, ma)) {
            JOptionPane.showMessageDialog(view, "Tên sách đã tồn tại!");
            return;
        }

        Sach s = readFormToSach(true);
        if (s == null) return;

        int ok = dao.update(s);
        if (ok > 0) {
            JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
            loadTable();
        } else {
            JOptionPane.showMessageDialog(view, "Cập nhật thất bại! Kiểm tra Mã sách có tồn tại không.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

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
            view.setMaSach(dao.taoMaSachMoi());
        } else {
            JOptionPane.showMessageDialog(view, "Xóa thất bại! Sách có thể đang bị tham chiếu (mượn trả/chi tiết...).",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void setSelectedTacGia(String maTG) {
    JComboBox<TacGia> cbo = view.getCboTacGia();
    for (int i = 0; i < cbo.getItemCount(); i++) {
        if (cbo.getItemAt(i).getMaTG().equals(maTG)) {
            cbo.setSelectedIndex(i);
            break;
        }
    }
    }
    private void setSelectedTheLoai(String maTL) {
    JComboBox<Theloai> cbo = view.getCboTheLoai();
    for (int i = 0; i < cbo.getItemCount(); i++) {
        if (cbo.getItemAt(i).getMaTL().equals(maTL)) {
            cbo.setSelectedIndex(i);
            break;
        }
    }
    }
    private void setSelectedNhaXuatBan(String maNXB) {
    JComboBox<NhaXuatBan> cbo = view.getCboNXB();
    for (int i = 0; i < cbo.getItemCount(); i++) {
        if (cbo.getItemAt(i).getMaNXB().equals(maNXB)) {
            cbo.setSelectedIndex(i);
            break;
        }
    }
    }

    private void fillFormFromSelectedRow() {
        int row = view.getTblSach().getSelectedRow();
        if (row < 0) return;

        DefaultTableModel m = view.getModel();

        String maSach = String.valueOf(m.getValueAt(row, 0));
        String tenSach = String.valueOf(m.getValueAt(row, 1));
        String maTG = String.valueOf(m.getValueAt(row, 2));
        String maTL = String.valueOf(m.getValueAt(row, 3));
        String maNXB = String.valueOf(m.getValueAt(row, 4));
        String namXB = String.valueOf(m.getValueAt(row, 5));
        String soLuong = String.valueOf(m.getValueAt(row, 6));

        view.setMaSach(maSach);
        view.setTenSach(tenSach);
        view.setNamXB(namXB);
        view.setSoLuong(soLuong);

        setSelectedTacGia(maTG);
        setSelectedTheLoai(maTL);
        setSelectedNhaXuatBan(maNXB);
    }


    private Sach readFormToSach(boolean requireMaSach) {
        String ma = view.getMaSach();
        String ten = view.getTenSach();

        String maTG = view.getTacGia();
        String maTL = view.getTheLoai();
        String maNXB = view.getNXB();

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

        String tinhTrang = "Tốt";
        String moTa = "";
        String maNN = "NN01";
        String maViTri = "VT01";

        return new Sach(ma, maTG, maNXB, maTL, ten, nam, soLuong, tinhTrang, moTa, maNN, maViTri);
    }

        private void importCSVToTable() {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Chọn file CSV danh sách sách");

            int choose = fc.showOpenDialog(view);
            if (choose != JFileChooser.APPROVE_OPTION) return;

            int readCount = 0;
            int insertCount = 0;

            try (BufferedReader br = new BufferedReader(new FileReader(fc.getSelectedFile()))) {
                String line;

                br.readLine();

                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;

                    String[] p = line.split(",", -1);
                    if (p.length < 7) continue;

                    try {
                        String maSach = p[0].trim();
                        String tenSach = p[1].trim();
                        String maTG = p[2].trim();
                        String maTL = p[3].trim();
                        String maNXB = p[4].trim();
                        int namXB = Integer.parseInt(p[5].trim());

                        Integer soLuong = null;
                        if (!p[6].trim().isEmpty()) {
                            soLuong = Integer.parseInt(p[6].trim());
                        }

                        Sach s = new Sach(
                                maSach,
                                maTG,
                                maNXB,
                                maTL,
                                tenSach,
                                namXB,
                                soLuong,
                                "Tốt",
                                "",
                                "NN01",
                                "VT01"
                        );

                        try {
                            int ok = dao.insert(s);
                            if (ok > 0) insertCount++;
                            readCount++;

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(view,
                                    "Lỗi ở dòng CSV:\n" + line + "\n\nChi tiết:\n" + ex.getMessage(),
                                    "IMPORT THẤT BẠI", JOptionPane.ERROR_MESSAGE);
                            return;
                        }


                    } catch (Exception rowErr) {
                        rowErr.printStackTrace();
                    }
                }

                loadTable();
                view.setMaSach(dao.taoMaSachMoi());

                JOptionPane.showMessageDialog(view,
                        "Đọc hợp lệ: " + readCount + " dòng\n"
                      + "Đã lưu DB: " + insertCount + " dòng",
                        "OK", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view,
                        "Nhập CSV thất bại!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }





    private void exportTableToCSV() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Chọn nơi lưu file CSV");
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
}