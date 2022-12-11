package bank.component;

import bank.data.DataService;
import bank.listener.AccountActionListener;
import bank.pojo.AccountVO;
import bank.util.JdbcUtil;
import bank.util.ResultConvertToVector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;
import java.util.*;

public class AccountManagerComponent extends Box {

    final int WIDTH = 850;
    final int HEIGHT = 600;

    private JFrame jf;
    private JTable table;
    private Vector<String> titles;
    private Vector<Vector> tableData;
    private DefaultTableModel tableModel;
    private AccountVO accountVO;
    private DataService dataService;

    public AccountManagerComponent(JFrame jf){
        super(BoxLayout.Y_AXIS);
        this.dataService = new DataService();

        this.jf = jf;
        JPanel jPanel = new JPanel();
        Color color = new Color(203,220,217);
        jPanel.setBackground(color);
        jPanel.setMaximumSize(new Dimension(WIDTH,80));
        jPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton addBtn = new JButton("添加");
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddAccountDialog(jf,"添加账户",true,new AccountActionListener(){
                    @Override
                    public void done(Object params) {
                        getTableData(null);
                    }
                },accountVO);
            }
        });
        JButton updateBtn = new JButton("修改");
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int account_no = (int)tableModel.getValueAt(table.getSelectedRow(), 0);
//                System.out.println(table.getSelectedRow());
                List<AccountVO> bookList = dataService.getTableData(account_no);
                if (bookList != null && bookList.size()>0){
                    AccountVO book = bookList.get(0);
                    new AddAccountDialog(jf, "修改账户", true, new AccountActionListener() {
                        @Override
                        public void done(Object params) {
                           getTableData(null);
                        }
                    },book);
                }
//                System.out.println(table.getSelectionModel());
            }
        });
        JButton deleteBtn = new JButton("删除");
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int account_no = (int)tableModel.getValueAt(table.getSelectedRow(), 0);
                int confirmResult = JOptionPane.showConfirmDialog(jf, "确定删除该记录吗？");
                if (confirmResult == JOptionPane.YES_OPTION){
                    String sql = "delete from tb_account where account_no=?";
                    JdbcUtil jdbcUtil = new JdbcUtil();
                    Boolean executeSql = jdbcUtil.executeSql(sql, Arrays.asList(account_no));
                    if (executeSql){
                        JOptionPane.showMessageDialog(jf,"删除成功");
                        getTableData(null);
                    }else {
                        JOptionPane.showMessageDialog(jf,"删除失败");
                    }
                }else {

                }
            }
        });
        jPanel.add(addBtn);
        jPanel.add(updateBtn);
        jPanel.add(deleteBtn);

        this.add(jPanel);


        String[] ts = {"账号","姓名","密码","余额","用户状态"};
        titles = new Vector<>();
        for (String item : ts){
            titles.add(item);
        }
        tableData = new Vector<>();
        tableModel = new DefaultTableModel(tableData,titles);
        table = new JTable(tableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane jScrollPane = new JScrollPane(table);
        this.add(jScrollPane);
        getTableData(null);

    }

    public void getTableData(Integer account_no) {
        tableData.clear();
        Vector<Vector> vectors = ResultConvertToVector.hashMapListToVector(new DataService().getTableData(account_no));
        tableData.addAll(vectors);
        tableModel.fireTableDataChanged();
//        return tableData;
    }
}
