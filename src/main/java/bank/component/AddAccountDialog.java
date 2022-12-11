package bank.component;


import bank.listener.AccountActionListener;
import bank.pojo.AccountVO;
import bank.util.JdbcUtil;
import bank.util.ScreenUtils;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


public class AddAccountDialog extends JDialog {

    final int WIDTH = 500;
    final int HEIGHT = 300;

    JDialog self = null;
    AccountActionListener listener = null;
    AccountVO accountVO = null;

    public AddAccountDialog(JFrame jf , String title, boolean isModel, AccountActionListener listener, AccountVO book){
        super(jf,title,isModel);
        this.listener = listener;
        this.accountVO = book;
        this.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2,(ScreenUtils.getScreenHeight()-HEIGHT)/2,WIDTH,HEIGHT);
        Box accountNoBox = Box.createHorizontalBox();
        JLabel accountNoLabel = new JLabel("账         号:");
        JTextField accountNoField = new JTextField(15);
        accountNoBox.add(accountNoLabel);
        accountNoBox.add(Box.createHorizontalStrut(20));
        accountNoBox.add(accountNoField);
        accountNoField.setEnabled(false);

        Box accountNameBox = Box.createHorizontalBox();
        JLabel accountNameLabel = new JLabel("账户名称:");
        JTextField accountNameField = new JTextField(15);
        accountNameField.setSize(50,30);
        accountNameBox.add(accountNameLabel);
        accountNameBox.add(Box.createHorizontalStrut(20));
        accountNameBox.add(accountNameField);

        Box accountPasswordBox = Box.createHorizontalBox();
        JLabel accountPasswordLabel = new JLabel("账户密码:");
        JTextField accountPasswordField = new JTextField(15);
        accountPasswordField.setSize(50,30);
        accountPasswordBox.add(accountPasswordLabel);
        accountPasswordBox.add(Box.createHorizontalStrut(20));
        accountPasswordBox.add(accountPasswordField);

        Box accountMoneyBox = Box.createHorizontalBox();
        JLabel accountMoneyLabel = new JLabel("账户余额:");
        JTextField accountMoneyField = new JTextField(15);
        accountMoneyField.setSize(50,30);
        accountMoneyBox.add(accountMoneyLabel);
        accountMoneyBox.add(Box.createHorizontalStrut(20));
        accountMoneyBox.add(accountMoneyField);


        Box statusBox = Box.createHorizontalBox();
        JLabel statusLabel = new JLabel("用户类型:");
        statusLabel.setSize(50,30);
        statusBox.add(statusLabel);

        JRadioButton mUser = new JRadioButton("正常",true);
        JRadioButton pUser = new JRadioButton("冻结",false);
        ButtonGroup genderGroup = new ButtonGroup();
        if (accountVO == null){
            statusLabel.setEnabled(false);
            mUser.setEnabled(false);
            pUser.setEnabled(false);
        }
        genderGroup.add(mUser);
        genderGroup.add(pUser);
        statusBox.add(mUser);
        statusBox.add(pUser);


        if (accountVO != null){
            accountNoField.setText(accountVO.getAccount_no()+"");
            accountNameField.setText(accountVO.getAccount_name());
            accountPasswordField.setText(accountVO.getAccount_password());
            accountMoneyField.setText(accountVO.getAccount_money()+"");
            if ("正常".equals(accountVO.getAccount_status())){
                mUser.setSelected(true);
            }else {
                pUser.setSelected(true);
            }

        }

        JButton addBtn = new JButton("确定");
        JButton cancelBtn = new JButton("取消");

        Box btnBox = Box.createHorizontalBox();
        btnBox.add(addBtn);
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JdbcUtil jdbcUtil = new JdbcUtil();
                String sql = "INSERT INTO `tb_account` ( account_name,account_password,account_money,account_status) " +
                        "VALUES (?,?,?,?); ";
                String statusType = genderGroup.isSelected(mUser.getModel())?mUser.getText().trim():pUser.getText().trim();

                boolean isNumber = accountMoneyField.getText().matches("^([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])$");
                if (!isNumber){
                    JOptionPane.showMessageDialog(jf,"余额输入异常");
                    return;
                }
                List paramList = Arrays.asList(accountNameField.getText(), accountPasswordField.getText()
                        , new BigDecimal(accountMoneyField.getText()), statusType);
                String sub_title = "添加";
                if (accountVO != null){
                    sql="update tb_account set account_name=?,account_password=?,account_money=?,account_status=? where account_no=?";
                    paramList = Arrays.asList(accountNameField.getText(), accountPasswordField.getText()
                            , new BigDecimal(accountMoneyField.getText()), statusType,accountVO.getAccount_no());
                    sub_title = "更新";
                }
                boolean updateFlag = false;
                String title=sub_title;
                if (accountVO == null){
                    long account_no = jdbcUtil.insertExecuteSql(sql,paramList);
                    updateFlag = account_no>0;
                    title=title+(updateFlag?"成功":"失败")+",账号:"+account_no;
                }else {
                    updateFlag = jdbcUtil.executeSql(sql, paramList);
                    title=title+(updateFlag?"成功":"失败");
                }
                if (updateFlag){
                    listener.done(null);
                    JOptionPane.showMessageDialog(jf,title);
                    dispose();
                    return;
                }else {
                    JOptionPane.showMessageDialog(jf,title);
                    dispose();
                    return;
                }
            }
        });
        btnBox.add(Box.createHorizontalStrut(40));
        btnBox.add(cancelBtn);
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        Box vBox = Box.createVerticalBox();
        vBox.add(Box.createVerticalStrut(25));
        if (accountVO != null){
            vBox.add(accountNoBox);
            vBox.add(Box.createVerticalStrut(15));
        }
        vBox.add(accountNameBox);
        vBox.add(Box.createVerticalStrut(15));
        vBox.add(accountPasswordBox);
        vBox.add(Box.createVerticalStrut(15));
        vBox.add(accountMoneyBox);
        vBox.add(Box.createVerticalStrut(15));
        vBox.add(statusBox);
        vBox.add(Box.createVerticalStrut(15));
        vBox.add(btnBox);
        vBox.add(Box.createVerticalStrut(40));

        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalStrut(20));
        this.add(hBox);

        this.setVisible(true);

    }
}
