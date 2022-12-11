package bank.ui;

import bank.data.DataService;
import bank.pojo.AccountVO;
import bank.util.JdbcUtil;
import bank.util.ScreenUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginInterface {

    JFrame mainFrame = new JFrame("网银系统");

    final int WIDTH=500;
    final int HEIGHT=300;
    // 初始化窗口
    public void init(){
        mainFrame.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2,(ScreenUtils.getScreenHeight()-HEIGHT)/2,WIDTH,HEIGHT);
        mainFrame.setResizable(false);

        JPanel jPanel = new JPanel();
        jPanel.setBounds(mainFrame.getBounds());
        mainFrame.add(jPanel);

        Box vBox = Box.createVerticalBox();
        Box uBox = Box.createHorizontalBox();
        JLabel userNameLable = new JLabel("用户名:");
//        userNameLable.setText();
        userNameLable.setSize(50,30);
        uBox.add(userNameLable);
        uBox.add(Box.createHorizontalStrut(20));
        JTextField uTextField = new JTextField(15);
        uBox.add(uTextField);

        Box pBox = Box.createHorizontalBox();
        JLabel passwordLable = new JLabel();
        passwordLable.setText("密    码:");
        passwordLable.setSize(50,30);
        pBox.add(passwordLable);
        pBox.add(Box.createHorizontalStrut(20));
        JPasswordField pTextField = new JPasswordField(15);
        pBox.add(pTextField);

        vBox.add(Box.createVerticalStrut(40));
        vBox.add(uBox);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(pBox);

        Box userTypeBox = Box.createHorizontalBox();
        JLabel genderLabel = new JLabel("用户类型:");
        genderLabel.setSize(50,30);
        userTypeBox.add(genderLabel);

        JRadioButton mUser = new JRadioButton("管理员",false);
        JRadioButton pUser = new JRadioButton("普通用户",true);
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(mUser);
        genderGroup.add(pUser);
        userTypeBox.add(mUser);
        userTypeBox.add(pUser);

        Box btnBox = Box.createHorizontalBox();

        JButton loginBtn = new JButton();
        loginBtn.setText("登录");
        loginBtn.setSize(50,30);
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JdbcUtil jdbcUtil = new JdbcUtil();
                String userName = uTextField.getText().trim();
                String password = new String(pTextField.getPassword());
                if (StringUtils.isBlank(userName)){
                    JOptionPane.showMessageDialog(mainFrame,"请输入用户名");
                    uTextField.requestFocusInWindow();
                    return;
                }
                String sql = "select * from tb_user where user_name=? and password=?";
                String userType = genderGroup.isSelected(mUser.getModel())?mUser.getText().trim():pUser.getText().trim();
                if ("普通用户".equals(userType)){
                    sql="select * from tb_account where account_no=? and account_password=?";
                }
                Map<String, List<?>> result = new HashMap<>();
                List<String> queryParams = new ArrayList();
                queryParams.add(userName);
                queryParams.add(password);
                List<HashMap> queryResult = jdbcUtil.query(sql, queryParams, result, HashMap.class);
                if (queryResult==null || queryResult.size()<=0){
                    JOptionPane.showMessageDialog(mainFrame,"账号或密码错误");
                    return;
                }else {
                    mainFrame.dispose();
                    if ("普通用户".equals(userType)){
//                        HashMap resultMap = queryResult.get(0);
                        Integer account_no = Integer.valueOf(userName);
                        List<AccountVO> tableData = new DataService().getTableData(account_no);
                        if (tableData!=null && tableData.size()>0){
                            new AccountInterface(tableData.get(0)).init();

                        }

                    }else {
                        new ManagerInterface().init();
                    }
                }
            }
        });
//        jPanel.add(jButton);
        btnBox.add(loginBtn);

        JButton registerBtn = new JButton();
        registerBtn.setText("注册");
        registerBtn.setSize(50,30);
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterInterface().init();
                mainFrame.dispose();
            }
        });
        btnBox.add(Box.createHorizontalStrut(100));
        btnBox.add(registerBtn);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(userTypeBox);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(btnBox);
        jPanel.add(vBox);


        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new LoginInterface().init();
    }

}
