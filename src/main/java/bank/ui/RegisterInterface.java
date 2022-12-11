package bank.ui;

import bank.util.JdbcUtil;
import bank.util.ScreenUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
//import java.awt.*;

public class RegisterInterface {

    JFrame registerFrame = new JFrame("注册");
    final int WIDTH=500;
    final int HEIGHT=300;

    public void init(){
        registerFrame.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2,(ScreenUtils.getScreenHeight()-HEIGHT)/2,WIDTH,HEIGHT);
        registerFrame.setResizable(false);

        JPanel jPanel = new JPanel();
        jPanel.setBounds(registerFrame.getBounds());
//        jPanel.setBackground(Color.BLUE);
        registerFrame.add(jPanel);

        Box uBox = Box.createHorizontalBox();
        JLabel userNameLabel = new JLabel("用户名:");
        userNameLabel.setSize(50,30);
        uBox.add(userNameLabel);
        uBox.add(Box.createHorizontalStrut(20));
        JTextField uTextField = new JTextField(15);
        uBox.add(uTextField);

        Box pBox = Box.createHorizontalBox();
        JLabel userPasswordLabel = new JLabel("密    码:");
        userPasswordLabel.setSize(50,30);
        pBox.add(userPasswordLabel);
        pBox.add(Box.createHorizontalStrut(20));
        JPasswordField pTextField = new JPasswordField(15);

        pBox.add(pTextField);

//        Box phoneBox = Box.createHorizontalBox();
//        JLabel userPhoneLabel = new JLabel("手机号:");
//        userPhoneLabel.setSize(50,30);
//        phoneBox.add(userPhoneLabel);
//        phoneBox.add(Box.createHorizontalStrut(20));
//        JTextField phoneTextField = new JTextField(15);
//        phoneBox.add(phoneTextField);

//        Box genderBox = Box.createHorizontalBox();
//        JLabel genderLabel = new JLabel("性    别:");
//        genderLabel.setSize(50,30);
//        genderBox.add(genderLabel);
//
//        JRadioButton manGender = new JRadioButton("男",true);
//        JRadioButton womanGender = new JRadioButton("女",false);
//        ButtonGroup genderGroup = new ButtonGroup();
//        genderGroup.add(manGender);
//        genderGroup.add(womanGender);
//        genderBox.add(manGender);
//        genderBox.add(womanGender);


        Box btnBox = Box.createHorizontalBox();
        JButton registerBtn = new JButton("注册");
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = uTextField.getText().trim();
                String password = new String(pTextField.getPassword());
//                String phone = phoneTextField.getText().trim();
//                String gender = genderGroup.isSelected(manGender.getModel())?manGender.getText().trim():womanGender.getText().trim();
                if (StringUtils.isBlank(userName)){
//                    JMessageBox.show(registerFrame,"提示","用户名不能为空",200,100);
                    JOptionPane.showMessageDialog(registerFrame,"用户名不能为空");

                    uTextField.requestFocusInWindow();
                    return;
                }
                if (StringUtils.isBlank(password)){
//                    JMessageBox.show(registerFrame,"提示","密码不能为空",200,100);
                    JOptionPane.showMessageDialog(registerFrame,"密码不能为空");

                    uTextField.requestFocusInWindow();
                    return;
                }
                JdbcUtil jdbcUtil = new JdbcUtil();

                String querySql = "select 1 from tb_user where user_name=?";
                List<String> queryParams = new ArrayList();
                queryParams.add(userName);
                Map<String, List<?>> result = new HashMap<>();
                List<String> listResult = jdbcUtil.query(querySql,queryParams,result,String.class);
                if (listResult!=null && listResult.size()>0){
//                    JMessageBox.show(registerFrame,"提示","用户名重复",200,100);
                    JOptionPane.showMessageDialog(registerFrame,"用户名重复");

                    uTextField.requestFocusInWindow();
                    return;
                }
                String sql = "insert into tb_user(user_id,user_name,password)values(?,?,?)";
                List params = new ArrayList();
                params.add(UUID.randomUUID().toString());
                params.add(userName);
                params.add(password);
                Boolean insertFlag = jdbcUtil.executeSql(sql,params);
                if(insertFlag){
                    JOptionPane.showMessageDialog(registerFrame,"注册成功");
                    uTextField.requestFocusInWindow();
                    return;
                }else {
                    JOptionPane.showMessageDialog(registerFrame,"注册失败");;
                    uTextField.requestFocusInWindow();
                    return;
                }

            }
        });
        registerBtn.setSize(50,30);
        JButton backLoginBtn = new JButton("返回登录界面");
        backLoginBtn.setSize(60,30);
        btnBox.add(registerBtn);
        btnBox.add(Box.createHorizontalStrut(40));
        btnBox.add(backLoginBtn);
        backLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginInterface().init();
                registerFrame.dispose();
            }
        });




        Box vBox = Box.createVerticalBox();
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(uBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(pBox);
//        vBox.add(Box.createVerticalStrut(20));
//        vBox.add(phoneBox);
        vBox.add(Box.createVerticalStrut(20));
//        vBox.add(genderBox);
//        vBox.add(Box.createVerticalStrut(20));
        vBox.add(btnBox);
        jPanel.add(vBox);


        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerFrame.setVisible(true);
    }

    public static void main(String[] args) {
        RegisterInterface registerInterface = new RegisterInterface();
        registerInterface.init();
    }

}
