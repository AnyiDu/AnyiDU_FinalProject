package bank.component;

import bank.data.DataService;
import bank.pojo.AccountVO;
import bank.util.JdbcUtil;
import bank.util.ScreenUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountComponet {

    public AccountComponet(AccountVO accountVO,boolean translate,boolean getMoney){
        super();
//        this.jf=jf;
        this.accountVO = accountVO;
        this.translate = translate;
        this.getMoney = getMoney;
    }

//    private JFrame jf;
    private AccountVO accountVO;
    private boolean translate;
    private boolean getMoney;

    JFrame accountFrame = new JFrame("资金管理");

    final int WIDTH=500;
    final int HEIGHT=300;
    // 初始化窗口
    public void init() {
        accountFrame.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
        accountFrame.setResizable(false);

        Box accountNoBox = Box.createHorizontalBox();
        accountNoBox.add(Box.createHorizontalStrut(20));

        JLabel accountNoLabel = new JLabel("账         号:");
        JTextField accountNoField = new JTextField(15);
        accountNoBox.add(accountNoLabel);
        accountNoBox.add(Box.createHorizontalStrut(20));
        accountNoBox.add(accountNoField);
        accountNoField.setEnabled(false);
        accountNoField.setText(accountVO.getAccount_no()+"");
        accountNoBox.add(Box.createHorizontalStrut(45));

        Box accountMoneyBox = Box.createHorizontalBox();
        accountMoneyBox.add(Box.createHorizontalStrut(20));

        JLabel accountMoneyLabel = new JLabel("账户余额:");
        JTextField accountMoneyField = new JTextField(15);
        accountMoneyField.setSize(50,30);
        accountMoneyBox.add(accountMoneyLabel);
        accountMoneyBox.add(Box.createHorizontalStrut(20));
        accountMoneyBox.add(accountMoneyField);
        accountMoneyField.setText(accountVO.getAccount_money()+"");
        accountMoneyField.setEnabled(false);
        JLabel label = new JLabel("元");
        accountMoneyBox.add(Box.createHorizontalStrut(10));
        accountMoneyBox.add(label);
        accountMoneyBox.add(Box.createHorizontalStrut(20));


        Box translateAccountNoBox = Box.createHorizontalBox();
        translateAccountNoBox.add(Box.createHorizontalStrut(20));
        JLabel translateAccountNoLabel = new JLabel("转入账号:");
        JTextField translateAccountNoField = new JTextField(15);
        translateAccountNoBox.add(translateAccountNoLabel);
        translateAccountNoBox.add(Box.createHorizontalStrut(20));
        translateAccountNoBox.add(translateAccountNoField);
//        translateAccountNoField.setEnabled(false);
//        translateAccountNoField.setText(accountVO.getAccount_no()+"");
        translateAccountNoBox.add(Box.createHorizontalStrut(45));




        Box inputMoneyBox = Box.createHorizontalBox();
        inputMoneyBox.add(Box.createHorizontalStrut(20));

        String inputMoneyLabelText = "存入金额:";
        if (this.translate) {
            inputMoneyLabelText = "转入金额:";
        }
        if (this.getMoney){
            inputMoneyLabelText="取出金额";
        }
        JLabel inputMoneyLabel = new JLabel(inputMoneyLabelText);
        JTextField inputMoneyField = new JTextField(15);
        inputMoneyField.setSize(50,30);
        inputMoneyBox.add(inputMoneyLabel);
        inputMoneyBox.add(Box.createHorizontalStrut(20));
        inputMoneyBox.add(inputMoneyField);
        JLabel label1 = new JLabel("元");
        inputMoneyBox.add(Box.createHorizontalStrut(10));
        inputMoneyBox.add(label1);
        inputMoneyBox.add(Box.createHorizontalStrut(20));


        Box btnBox = Box.createHorizontalBox();
        JButton saveBtn = new JButton("确定");
        saveBtn.setSize(50,30);
        btnBox.add(Box.createHorizontalStrut(40));
        btnBox.add(saveBtn);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isNumber = inputMoneyField.getText().matches("^([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])$");
                if (!isNumber){
                    JOptionPane.showMessageDialog(accountFrame,"余额输入异常");
                    return;
                }
                AccountVO tmpAccountVO = new AccountVO();
                AccountVO translateAccountVO = null;
                tmpAccountVO.setAccount_no(accountVO.getAccount_no());
                tmpAccountVO.setBefore_money(accountVO.getAccount_money());
                tmpAccountVO.setChange_money(new BigDecimal(inputMoneyField.getText()));
                if (translate || getMoney){
                    tmpAccountVO.setAfter_money(accountVO.getAccount_money()
                            .subtract(new BigDecimal(inputMoneyField.getText())));
                    tmpAccountVO.setAccount_money(tmpAccountVO.getAfter_money());
                    if (tmpAccountVO.getAfter_money().compareTo(new BigDecimal("0.0"))==-1) {
                        JOptionPane.showMessageDialog(accountFrame,"金额不足");
                        return;
                    }
                    tmpAccountVO.setChange_type(translate?"转出":"取款");
                    if (translate){
                        translateAccountVO = new AccountVO();
                        translateAccountVO.setAccount_no(Integer.valueOf(translateAccountNoField.getText()));
                        List<AccountVO> tableData = new DataService().getTableData(translateAccountVO.getAccount_no());
                        if (tableData != null && tableData.size()<=0){
                            JOptionPane.showMessageDialog(accountFrame,"转入账户不存在");
                            return;
                        }else {
                            AccountVO accountVO1 = tableData.get(0);
                            if (!"正常".equals(accountVO1.getAccount_status())){
                                JOptionPane.showMessageDialog(accountFrame,"转入账户状态异常");
                                return;
                            }
                            translateAccountVO.setBefore_money(accountVO1.getAccount_money());
                            translateAccountVO.setAccount_money(accountVO1.getAccount_money().add(new BigDecimal(inputMoneyField.getText())));
                            translateAccountVO.setAfter_money(translateAccountVO.getAccount_money());
                            translateAccountVO.setChange_type("转入");
                            translateAccountVO.setChange_money(new BigDecimal(inputMoneyField.getText()));
                        }
                    }
                }else {
                    tmpAccountVO.setAfter_money(accountVO.getAccount_money()
                            .add(new BigDecimal(inputMoneyField.getText())));
                    tmpAccountVO.setChange_type("存款");
                    tmpAccountVO.setAccount_money(tmpAccountVO.getAfter_money());
                }

                boolean result = updateData(tmpAccountVO,translateAccountVO);
                if (result){
                    accountMoneyField.setText(tmpAccountVO.getAccount_money()+"");
                    inputMoneyField.setText("");
                    JOptionPane.showMessageDialog(accountFrame,"更新成功");
                    return;
                }else {
                    JOptionPane.showMessageDialog(accountFrame,"更新失败");
                    return;
                }

            }
        });

//        Box cancelBox = Box.createHorizontalBox();
        JButton cancelBtn = new JButton("取消");
        cancelBtn.setSize(50,30);
        btnBox.add(Box.createHorizontalStrut(40));
        btnBox.add(cancelBtn);
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputMoneyField.setText("");
            }
        });

        JButton quitBtn = new JButton("退出");
        quitBtn.setSize(50,30);
        btnBox.add(Box.createHorizontalStrut(40));
        btnBox.add(quitBtn);
        quitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        Box vBox = Box.createVerticalBox();
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(accountNoBox);
        vBox.add(Box.createVerticalStrut(25));
        vBox.add(accountMoneyBox);
        vBox.add(Box.createVerticalStrut(25));
        vBox.add(inputMoneyBox);
        if (this.translate){
            vBox.add(Box.createVerticalStrut(25));
            vBox.add(translateAccountNoBox);
        }
        vBox.add(Box.createVerticalStrut(25));
        vBox.add(btnBox);
        vBox.add(Box.createVerticalStrut(40));

        accountFrame.add(vBox);
        accountFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        accountFrame.setVisible(true);
    }

    public boolean updateData(AccountVO mainAccount,AccountVO translateAccount){
        JdbcUtil jdbcUtil = new JdbcUtil();
        String sql="update tb_account set account_money=? where account_no=?";
        String insertSql = "insert into tb_account_change(account_no,account_change_type,account_money_before" +
                ",account_change_money,account_money_after) values(?,?,?,?,?) ";
        List paramList = Arrays.asList(mainAccount.getAccount_money(),mainAccount.getAccount_no());
        boolean mainInsertFlag = jdbcUtil.executeSql(sql,paramList);
        if (mainInsertFlag){

            paramList = Arrays.asList(mainAccount.getAccount_no(),mainAccount.getChange_type()
                    ,mainAccount.getBefore_money(),mainAccount.getChange_money(),mainAccount.getAfter_money());
            mainInsertFlag = jdbcUtil.executeSql(insertSql,paramList);
        }
        if (translateAccount != null){
//            sql="update tb_account set account_money=? where account_no=?";
            paramList = Arrays.asList(translateAccount.getAccount_money(),translateAccount.getAccount_no());
            mainInsertFlag = jdbcUtil.executeSql(sql,paramList);
            if (mainInsertFlag){
                paramList = Arrays.asList(translateAccount.getAccount_no(),translateAccount.getChange_type(),
                        translateAccount.getBefore_money(),translateAccount.getChange_money(),translateAccount.getAfter_money());
                mainInsertFlag = jdbcUtil.executeSql(insertSql,paramList);
            }
        }
        return mainInsertFlag;
    }

    public static void main(String[] args) {
        new AccountComponet(null,false,false).init();
    }

}
