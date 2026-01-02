/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLTV.Views;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author dinhd
 */
public class FormSach extends JPanel{
    private JTextField txtSearch = new JTextField();
    private JButton btnSearch = new JButton("Tìm");


    private JTextField txtMaSach = new JTextField();
    private JTextField txtTenSach = new JTextField();
    private JTextField txtTacGia = new JTextField();
    private JTextField txtTheLoai = new JTextField();
    private JTextField txtNXB = new JTextField();
    private JTextField txtNamXB = new JTextField();
    private JTextField txtSoLuong = new JTextField();

    private JButton btnThem = new JButton("Thêm");
    private JButton btnSua = new JButton("Sửa");
    private JButton btnXoa = new JButton("Xóa");
    private JButton btnLamMoi = new JButton("Làm mới");
    private JButton btnNhapFile = new JButton("Nhập file (CSV)");

    private JTable tblSach;
    private DefaultTableModel model;

    public FormSach() {
        setLayout(new BorderLayout());
        setBackground(new Color(235, 242, 250));
        setBorder(new EmptyBorder(12, 12, 12, 12));

        add(createHeader(), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);
    }

    private JComponent createHeader() {
        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel title = new JLabel("QUẢN LÝ SÁCH");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(20, 40, 70));

        JPanel searchBox = new JPanel(new BorderLayout(8, 0));
        searchBox.setOpaque(false);

        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setPreferredSize(new Dimension(260, 36));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 220), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        txtSearch.setBackground(Color.WHITE);

        stylePrimary(btnSearch);
        btnSearch.setPreferredSize(new Dimension(80, 36));

        searchBox.add(txtSearch, BorderLayout.CENTER);
        searchBox.add(btnSearch, BorderLayout.EAST);

        header.add(title, BorderLayout.WEST);
        header.add(searchBox, BorderLayout.EAST);

        return header;
}

    private JComponent createBody() {
        JPanel body = new JPanel(new BorderLayout(12, 12));
        body.setOpaque(false);

        body.add(createFormCard(), BorderLayout.WEST);

        body.add(createTableCard(), BorderLayout.CENTER);

        return body;
    }

    private JComponent createFormCard() {
        JPanel card = new RoundedPanel(18);
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(360, 0));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel lb = new JLabel("Thông tin sách");
        lb.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lb.setForeground(new Color(30, 50, 90));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 2, 6, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        int r = 0;
        r = addRow(form, gbc, r, "Mã sách", txtMaSach);
        r = addRow(form, gbc, r, "Tên sách", txtTenSach);
        r = addRow(form, gbc, r, "Tác giả", txtTacGia);
        r = addRow(form, gbc, r, "Thể loại", txtTheLoai);
        r = addRow(form, gbc, r, "Nhà xuất bản", txtNXB);
        r = addRow(form, gbc, r, "Năm XB", txtNamXB);
        r = addRow(form, gbc, r, "Số lượng", txtSoLuong);


        JPanel actions = new JPanel(new GridLayout(3, 2, 10, 10));
        actions.setOpaque(false);
        actions.setBorder(new EmptyBorder(12, 0, 0, 0));

        stylePrimary(btnThem);
        stylePrimary(btnSua);
        styleDanger(btnXoa);
        styleGhost(btnLamMoi);
        styleAccent(btnNhapFile);

        actions.add(btnThem);
        actions.add(btnSua);
        actions.add(btnXoa);
        actions.add(btnLamMoi);
        actions.add(btnNhapFile);

        actions.add(new JLabel(""));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(lb, BorderLayout.NORTH);
        top.add(new JSeparator(), BorderLayout.SOUTH);

        card.add(top, BorderLayout.NORTH);
        card.add(form, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);

        styleInput(txtMaSach);
        txtMaSach.setEditable(false);
        txtMaSach.setBackground(new Color(230, 230, 230));
        styleInput(txtTenSach);
        styleInput(txtTacGia);
        styleInput(txtTheLoai);
        styleInput(txtNXB);
        styleInput(txtNamXB);
        styleInput(txtSoLuong);

        return card;
    }

    private int addRow(JPanel form, GridBagConstraints gbc, int row, String text, JComponent field) {
        JLabel lb = new JLabel(text);
        lb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lb.setAlignmentX(Component.LEFT_ALIGNMENT);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 1;
        form.add(lb, gbc);

        gbc.gridy = row + 1;
        form.add(field, gbc);

        return row + 2;
}


    private JComponent createTableCard() {
        JPanel card = new RoundedPanel(18);
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel lb = new JLabel("Danh sách sách");
        lb.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lb.setForeground(new Color(30, 50, 90));

        String[] cols = {"Mã sách", "Tên sách", "Tác giả", "Thể loại", "NXB", "Năm XB", "Số lượng"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblSach = new JTable(model);
        tblSach.setRowHeight(28);
        tblSach.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblSach.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblSach.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane sp = new JScrollPane(tblSach);
        sp.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 240)));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(lb, BorderLayout.NORTH);
        top.add(new JSeparator(), BorderLayout.SOUTH);

        card.add(top, BorderLayout.NORTH);
        card.add(sp, BorderLayout.CENTER);

        return card;
    }

    private void styleInput(JTextField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setPreferredSize(new Dimension(0, 36));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 230), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        f.setBackground(new Color(250, 252, 255));
    }

    private void stylePrimary(JButton b) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setForeground(Color.WHITE);
        b.setBackground(new Color(45, 126, 255));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(0, 36));
    }

    private void styleDanger(JButton b) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setForeground(Color.WHITE);
        b.setBackground(new Color(220, 70, 70));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(0, 36));
    }

    private void styleGhost(JButton b) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setForeground(new Color(45, 126, 255));
        b.setBackground(Color.WHITE);
        b.setBorder(BorderFactory.createLineBorder(new Color(45, 126, 255), 1, true));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(0, 36));
    }

    private void styleAccent(JButton b) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setForeground(Color.WHITE);
        b.setBackground(new Color(46, 170, 125));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(0, 36));
    }

    static class RoundedPanel extends JPanel {
        private final int radius;
        public RoundedPanel(int radius) { this.radius = radius; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnLamMoi() { return btnLamMoi; }
    public JButton getBtnNhapFile() { return btnNhapFile; }

    public JTable getTblSach() { return tblSach; }
    public DefaultTableModel getModel() { return model; }

    public String getMaSach() { return txtMaSach.getText().trim(); }
    public String getTenSach() { return txtTenSach.getText().trim(); }
    public String getTacGia() { return txtTacGia.getText().trim(); }
    public String getTheLoai() { return txtTheLoai.getText().trim(); }
    public String getNXB() { return txtNXB.getText().trim(); }
    public String getNamXB() { return txtNamXB.getText().trim(); }
    public String getSoLuong() { return txtSoLuong.getText().trim(); }
    
    
    public void setMaSach(String ma) {
        txtMaSach.setText(ma);
    }


    public void clearForm() {
        txtMaSach.setText("");
        txtTenSach.setText("");
        txtTacGia.setText("");
        txtTheLoai.setText("");
        txtNXB.setText("");
        txtNamXB.setText("");
        txtSoLuong.setText("");
    }
    
    public void setForm(String ma, String ten, String tg, String tl, String nxb, String nam, String sl) {
        txtMaSach.setText(ma);
        txtTenSach.setText(ten);
        txtTacGia.setText(tg);
        txtTheLoai.setText(tl);
        txtNXB.setText(nxb);
        txtNamXB.setText(nam);
        txtSoLuong.setText(sl);
    }

    
    public JTextField getTxtSearch() {
    return txtSearch;
    }

    public JButton getBtnSearch() {
        return btnSearch;
    }

}
