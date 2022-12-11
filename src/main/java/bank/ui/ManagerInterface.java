package bank.ui;

import bank.component.AccountManagerComponent;
import bank.util.ScreenUtils;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerInterface {



    JFrame managerFrame = new JFrame("xx欢迎你");

    final int WIDTH = 1000;
    final int HEIGHT = 600;

    public void init(){
        managerFrame.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2,(ScreenUtils.getScreenHeight()-HEIGHT)/2,WIDTH,HEIGHT);
        managerFrame.setResizable(false);

        //设置菜单栏
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("设置");
        JMenuItem m1 = new JMenuItem("切换账号");
        JMenuItem m2 = new JMenuItem("退出程序");
        m1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginInterface().init();
                managerFrame.dispose();
            }
        });
        m2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(m1);
        menu.add(m2);
        menuBar.add(menu);
        managerFrame.setJMenuBar(menuBar);
        managerFrame.setVisible(true);
        managerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //设置分割面板
        JSplitPane jSplitPane = new JSplitPane();
        jSplitPane.setContinuousLayout(true);
        jSplitPane.setDividerLocation(150);
        jSplitPane.setDividerSize(7);

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("系统管理");
//        DefaultMutableTreeNode userManageNode = new DefaultMutableTreeNode("用户管理");
        DefaultMutableTreeNode bookManageNode = new DefaultMutableTreeNode("账户管理");


//        rootNode.add(userManageNode);
        rootNode.add(bookManageNode);

        JTree jTree = new JTree(rootNode);
        jSplitPane.setLeftComponent(jTree);
        jTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                System.out.println(e.getNewLeadSelectionPath().getLastPathComponent());
                Object lastPathComponent = e.getNewLeadSelectionPath().getLastPathComponent();
                if (bookManageNode.equals(lastPathComponent)){
//                    System.out.println("图书管理----");
                    jSplitPane.setRightComponent(new AccountManagerComponent(managerFrame));
                    jSplitPane.setDividerLocation(150);

                }
            }
        });
        Color color = new Color(203,220,217);
        jTree.setBackground(color);
//        jSplitPane.
        jTree.setSelectionRow(2);
        jSplitPane.setRightComponent(new AccountManagerComponent(managerFrame));
        managerFrame.add(jSplitPane);
        managerFrame.setVisible(true);

    }


    public static void main(String[] args) {
        new ManagerInterface().init();
    }
}
