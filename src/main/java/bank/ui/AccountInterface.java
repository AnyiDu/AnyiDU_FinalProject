package bank.ui;

import bank.component.AccountComponet;
import bank.pojo.AccountVO;
import bank.util.ScreenUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountInterface {


    JFrame accountFrame = new JFrame("账户管理");

    final int WIDTH=500;
    final int HEIGHT=300;
    private AccountVO accountVO;

    public AccountInterface(AccountVO accountVO){
        this.accountVO = accountVO;
    }

    // 初始化窗口
    public void init(){
        accountFrame.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2,(ScreenUtils.getScreenHeight()-HEIGHT)/2,WIDTH,HEIGHT);
        accountFrame.setResizable(false);

        Box hBox = Box.createHorizontalBox();

        Box vBox = Box.createVerticalBox();
        hBox.add(Box.createHorizontalStrut(80));
        JButton inputMoney = new JButton("存款");
        inputMoney.setSize(50,30);
        inputMoney.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AccountComponet(accountVO,false,false).init();
            }
        });


        hBox.add(inputMoney);
        hBox.add(Box.createHorizontalStrut(86));
        JButton outputMoney = new JButton("取款");
        outputMoney.setSize(50,30);
        outputMoney.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AccountComponet(accountVO,false,true).init();
            }
        });


        hBox.add(outputMoney);

        hBox.add(Box.createHorizontalStrut(87));
        JButton translateMoney = new JButton("转账");
        translateMoney.setSize(50,30);
        translateMoney.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AccountComponet(accountVO,true,false).init();
            }
        });

        hBox.add(translateMoney);

        accountFrame.add(hBox);



        accountFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        accountFrame.setVisible(true);
    }


    public static void main(String[] args) {
        new AccountInterface(null).init();
    }

}
