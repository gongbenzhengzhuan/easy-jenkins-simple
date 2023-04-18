package com.easy.frame;

import com.easy.EasyJenkinsApplication;
import com.easy.bean.DataStructure;
import com.easy.service.DataStructureService;
import com.easy.util.DESUtil;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * @author 武天
 * @date 2022/11/28 13:57
 */

@Slf4j
public class MainFrame extends JFrame {
    int i = 1;//记录步骤数
    int total = 2;//总步骤数
    int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    int height = Toolkit.getDefaultToolkit().getScreenSize().height;
    JPanel up_panel = new JPanel();
    JPanel down_panel = new JPanel();
    JPanel up_left_panel = new JPanel();
    JPanel up_right_panel = new JPanel();
    JPanel up_left_list_panel = new JPanel();

    JButton last_button = new JButton("上一步");
    JButton next_button = new JButton("下一步");
    JButton cancle_button = new JButton("取消");
    JButton finish_button = new JButton("安装并启动");
    JTextField descFile = new JTextField();//安装文件路径

    JTextField mavenDescFile = new JTextField();//maven文件路径

    JTextField portInput = new JTextField();

    String[] args;

    public static void main(String[] args) {
        new MainFrame(args);
    }

    public MainFrame(String[] args) {
        this.args = args;
        last_button.setVisible(false);
        finish_button.setVisible(false);
        this.setTitle("easy-jenkins");
        ImageIcon icon = new ImageIcon("src\\main\\resources\\static\\images\\logo\\easy-jenkins-logo.png");  //xxx代表图片存放路径，2.png图片名称及格式
        this.setIconImage(icon.getImage());
        this.setSize(840, 660);
        this.setResizable(false);
        this.setLocation(width / 2 - 176, height / 2 - 170);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon icon11 = new ImageIcon("");
        JLabel label = new JLabel(icon11);
        label.setBounds(0, 0, 640, 460);
        //获取窗口的第二层，将label放入
        this.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
        // 居中显示
        this.setLocationRelativeTo(null);
        //获取frame的顶层容器,并设置为透明
        JPanel j = (JPanel) this.getContentPane();
        j.setOpaque(false);

        up_left_panel.setBorder(new TitledBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.CYAN), ""));
        up_right_panel.setBorder(new TitledBorder(BorderFactory.createMatteBorder(0, 1, 1, 0, Color.CYAN), ""));
        up_panel.setOpaque(false);
        up_left_panel.setOpaque(false);
        up_right_panel.setOpaque(false);
        up_left_list_panel.setOpaque(false);
        down_panel.setOpaque(false);

        //对下面的区域作设置
        down_panel.add(last_button);
        down_panel.add(next_button);
        down_panel.add(cancle_button);
        finish_button.addActionListener(new DeterMine(portInput));
        //调整为安装按钮绑定事件
        down_panel.add(finish_button);

        JLabel imageLable = new JLabel(new ImageIcon(""));
        imageLable.setPreferredSize(new Dimension(170, 20));
        up_left_panel.add(imageLable);
        up_left_panel.add(up_left_list_panel);
        BoxLayout layout = new BoxLayout(up_left_list_panel, BoxLayout.Y_AXIS);
        up_left_list_panel.setLayout(layout);
        up_left_list_panel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        up_left_list_panel.add(createNewLable("简介", "", SwingConstants.LEFT));
        up_left_list_panel.add(createNewLable("安装", "", SwingConstants.LEFT));

        up_right_panel.add(getPanelByID(1));


        this.setLayout(new BorderLayout());
        this.add(up_panel, BorderLayout.CENTER);
        this.add(down_panel, BorderLayout.SOUTH);
        up_panel.setLayout(new BorderLayout(0, 0));
        up_left_panel.setPreferredSize(new Dimension(180, 250));
        ;
        up_panel.add(up_left_panel, BorderLayout.WEST);
        up_panel.add(up_right_panel, BorderLayout.CENTER);

        //设置按钮点击事件

        next_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                up_right_panel.getComponent(i - 1).setVisible(false);
                int compCount = up_right_panel.getComponentCount();
                if (compCount > i) {
                    up_right_panel.getComponent(i).setVisible(true);
                    JLabel tempLable2 = (JLabel) up_left_list_panel.getComponent(i - 1);
                    tempLable2.setIcon(new ImageIcon(""));
                    JLabel tempLable3 = (JLabel) up_left_list_panel.getComponent(i);
                    tempLable3.setIcon(new ImageIcon(""));
                    ++i;
                } else {
                    up_right_panel.add(getPanelByID(++i));
                    JLabel tempLable = (JLabel) up_left_list_panel.getComponent(i - 2);
                    tempLable.setIcon(new ImageIcon(""));
                    JLabel tempLable4 = (JLabel) up_left_list_panel.getComponent(i - 1);
                    tempLable4.setIcon(new ImageIcon(""));
                }
                if (i > 1) {
                    last_button.setVisible(true);
                }
                if (i == total) {
                    next_button.setVisible(false);
                    cancle_button.setVisible(false);
                    finish_button.setVisible(true);
                }
            }
        });

        last_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (i >= 2) {
                    finish_button.setVisible(false);
                    next_button.setVisible(true);
                    up_right_panel.getComponent(i - 1).setVisible(false);
                    up_right_panel.getComponent(i - 2).setVisible(true);
                    if (i == 2) {
                        last_button.setVisible(false);
                    }
                    JLabel tempLable = (JLabel) up_left_list_panel.getComponent(i - 2);
                    tempLable.setIcon(new ImageIcon(""));
                    JLabel tempLable2 = (JLabel) up_left_list_panel.getComponent(i - 1);
                    tempLable2.setIcon(new ImageIcon(""));
                    i--;
                }
            }
        });

        finish_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println("完成按钮启动事件");
//                String[] args = new String[]{};
//                EasyJenkinsApplication.main(args);
//                JOptionPane.showMessageDialog(
//                        null, "安装成功,请点击右上角退出进行下一步操作", "成功",JOptionPane.QUESTION_MESSAGE);

//                System.exit(0);
                System.out.println("你好");
            }
        });

        cancle_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        this.setVisible(true);
    }

    public JLabel createNewLable(String text, String url, int horizontalAlignment) {
        JLabel tempLable = new JLabel(text, new ImageIcon(url), horizontalAlignment);
        tempLable.setFont(new Font("宋体", Font.PLAIN, 25));
        tempLable.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        tempLable.setPreferredSize(new Dimension(170, 40));
        return tempLable;
    }

    public JPanel getPanelByID(int index) {
        JPanel tempPanel = new JPanel();
        tempPanel.setOpaque(false);
        switch (index) {
            case 1:
                JLabel label = new JLabel("<html>本向导将指引您完成easy-jenkins的安装。<br><br>" +
                        "<p align=center>简介</p><br>" +
                        "easy-jenkins是一款对vue和jar的部署工具，操作简单，实行一键部署，内部结构采用流水线形式架构，每次部署，时时提供部署过程，部署记录，界面友好简洁，使用方便，符合用户常规操作，easy-jenkins面向分支形式，无需登录，默认分支为jenkins，每个分支可以配置多个数据源，切换不同分支可以管理不同数据源，easy-jenkins采用本地存储的结构无需配置数据库，简单易上手"
                );
                label.setFont(new Font("宋体", Font.PLAIN, 25));
                label.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
                label.setPreferredSize(new Dimension(420, 490));
                tempPanel.add(label);
                break;
            case 2:

                BoxLayout layout_case7 = new BoxLayout(tempPanel, BoxLayout.Y_AXIS);

                JPanel tempPanel_case2_2 = new JPanel();
                tempPanel_case2_2.setOpaque(false);

                JPanel tempPanel_case3_3 = new JPanel();
                tempPanel_case3_3.setOpaque(false);

                JPanel tempPanel_case4_4 = new JPanel();
                tempPanel_case4_4.setOpaque(false);

                JLabel dataTxtlabel = new JLabel("安装路径：");
                dataTxtlabel.setFont(new Font("宋体", Font.PLAIN, 18));
                dataTxtlabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 0, 0));
                descFile.setPreferredSize(new Dimension(200, 28));
                JButton browseBtn = new JButton("浏览（R）");
                browseBtn.setBounds(340, 20, 40, 25);
                browseBtn.addActionListener(e -> {
                    JFileChooser jfc = new JFileChooser();// 文件选择器
                    jfc.setFileSelectionMode(1);// 设定只能选择到文件夹
                    int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
                    if (state == 1) {
                        return;
                    } else {
                        File f = jfc.getSelectedFile();// f为选择到的目录
                        descFile.setText(f.getAbsolutePath());
                    }

                });


                JLabel mavenTxtlabel = new JLabel("maven路径：");
                mavenTxtlabel.setFont(new Font("宋体", Font.PLAIN, 18));
                mavenTxtlabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 0, 0));
                mavenDescFile.setPreferredSize(new Dimension(200, 28));
                JButton browseMavenBtn = new JButton("浏览（R）");
                browseMavenBtn.setBounds(340, 20, 40, 25);
                browseMavenBtn.addActionListener(e -> {
                    JFileChooser jfc = new JFileChooser();// 文件选择器
                    jfc.setFileSelectionMode(1);// 设定只能选择到文件夹
                    int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
                    if (state == 1) {
                        return;
                    } else {
                        File f = jfc.getSelectedFile();// f为选择到的目录
                        mavenDescFile.setText(f.getAbsolutePath());
                    }

                });


                tempPanel_case2_2.add(dataTxtlabel);
                tempPanel_case2_2.add(descFile);
                tempPanel_case2_2.add(browseBtn);
                tempPanel_case2_2.setPreferredSize(new Dimension(520, 200));
                tempPanel.add(tempPanel_case2_2);

                tempPanel.setLayout(layout_case7);

                tempPanel_case3_3.add(mavenTxtlabel);
                tempPanel_case3_3.add(mavenDescFile);
                tempPanel_case3_3.add(browseMavenBtn);
                tempPanel_case3_3.setPreferredSize(new Dimension(520, 200));
                tempPanel.add(tempPanel_case3_3);

                tempPanel.setLayout(layout_case7);

                tempPanel_case4_4.add(createNewPanel01("端口：", portInput));
                tempPanel.add(tempPanel_case4_4);


                break;
        }

        return tempPanel;
    }

    /**
     * 文件选择框
     *
     * @param title 备注
     * @return
     */
    public JPanel createNewPanel01(String title, JTextField txtInput) {
        JPanel tempPanel = new JPanel();
        tempPanel.setOpaque(false);
        JLabel label1 = new JLabel(title);
        label1.setPreferredSize(new Dimension(80, 28));
        label1.setFont(new Font("宋体", Font.PLAIN, 18));
        label1.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 0));
//        JTextField text1 = new JTextField();//
        txtInput.setPreferredSize(new Dimension(250, 28));
        //监听输入框键盘输入事件  控制端口号只能输入纯数字
        txtInput.addKeyListener(new PortInoutToNum());
        ;
        tempPanel.add(label1);
        tempPanel.add(txtInput);
        return tempPanel;
    }

    /**
     * 端口号键盘输入监听类
     */
    class PortInoutToNum extends KeyAdapter {

        public void keyTyped(KeyEvent e) {
            String key = "0123456789" + (char) 8;
            if (key.indexOf(e.getKeyChar()) < 0) {
                e.consume();//如果不是数字则取消
            }
        }
    }

    /**
     * 按钮点击监听
     */
    class DeterMine implements ActionListener {
        private JTextField portInput;

        public DeterMine(JTextField portInput) {
            this.portInput = portInput;
        }

        /**
         * 重写监听点击事件
         *
         * @param e xing行动的事件
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            log.info("按钮的点击事件");
            boolean validate = true;
            if (descFile.getText().isEmpty()) {
                JOptionPane.showMessageDialog(
                        null, "数据文件路径不可为空", "必填提示", JOptionPane.ERROR_MESSAGE);
                validate = false;
            }
            if (mavenDescFile.getText().isEmpty()) {
                JOptionPane.showMessageDialog(
                        null, "maven路径不可为空", "必填提示", JOptionPane.ERROR_MESSAGE);
                validate = false;
            }
            if (portInput.getText().isEmpty()) {
                JOptionPane.showMessageDialog(
                        null, "端口号不可为空", "必填提示", JOptionPane.ERROR_MESSAGE);
                validate = false;
            }
            if (validate) {
                DataStructure dataStructure = new DataStructure();
                dataStructure
                        .setDataPath(descFile.getText())
                        .setMavenPath(mavenDescFile.getText())
                        .setEasyJenkinsPort(Integer.parseInt(portInput.getText()))
                        .setFileId(DESUtil.encryption(String.valueOf(System.currentTimeMillis())));
                System.out.println(dataStructure);
                DataStructureService.initJSON(dataStructure.getDataPath(), dataStructure.getMavenPath(), dataStructure.getEasyJenkinsPort());
                JOptionPane.showMessageDialog(null, "安装成功,请妥善保存数据文件,避免数据丢失,下次启动直接点击Easy-jenkins.exe启动", "提示信息", JOptionPane.QUESTION_MESSAGE);
                //关闭窗口
                // System.exit(0);
                dispose();
                EasyJenkinsApplication.main(args);
            }


        }
    }

}


