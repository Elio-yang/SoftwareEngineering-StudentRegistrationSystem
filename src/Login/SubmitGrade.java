package Login;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import javax.swing.*;
import javax.xml.crypto.Data;

import java.util.*;
import javax.swing.*;


public class SubmitGrade extends JFrame {
    private Socket socket;
    private DataInputStream dis;
    String grade;

    private DataOutputStream dos;
    private Vector<String> idd=new Vector<String>();
    public SubmitGrade(Socket socket,String user_id,String pw)  {
        super("成绩登记");
        this.socket=socket;
        try {
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }



        JPanel con=new JPanel();
        setBounds(150, 150, 1150, 500);//
        //setLayout(null);
        con.setLayout(null);
        //con.setPreferredSize(new Dimension(1000,1000));
        JScrollPane sp1 = new JScrollPane(con);
        //sp1.setBounds(400,200,100,100);
        //sp1.setLayout(null);
        //this.getContentPane().add(sp1);
        this.add(sp1);



        JButton b2=new JButton("返回");
        con.add(b2);
        b2.setBounds(1015, 50, 60, 25);
        JLabel jl1=new JLabel("course");
        JLabel jl3=new JLabel("sid");
        JLabel jl4=new JLabel("name");
        JLabel jl5=new JLabel("grade");
        //JLabel jl9=new JLabel("院系");
        jl1.setBounds(135, 90, 60, 25);
        jl3.setBounds(235, 90, 60, 25);
        jl4.setBounds(365, 90, 60, 25);
        jl5.setBounds(465, 90, 60, 25);
        //jl9.setBounds(815, 90, 60, 25);
        con.add(jl3);
        con.add(jl4);
        con.add(jl5);
//        String sql="select * from course_selection where pid=1";
        String data="";
        try {
            dos.writeUTF("2o");
            dos.flush();

        } catch (IOException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        String da;
        int i=0;//i是当前信息的序号，用来实现删除修改的定位
        int j=0;//j是为了得到传回来的数据，因为是用#分开的数据
        String n="";
        try{
            n= dis.readUTF();
        }catch (IOException e2){
            e2.printStackTrace();
        }


        int t=0;
        while(t<Integer.valueOf(n)){
            final int k=i;
            // 通过字段检索
            try {
                String course  = dis.readUTF();
                String sid =dis.readUTF();
                String name = dis.readUTF();
                grade= dis.readUTF();
                JLabel jl10=new JLabel(i+"");
                JLabel l1=new JLabel(course);
                JLabel l2=new JLabel(sid);
                JLabel l3=new JLabel(name);
                final JLabel[] l4 = {new JLabel(grade)};


            ;

            //JLabel l7=new JLabel(depart);
            con.add(jl1);
            con.add(jl10);
            con.add(l1);
            con.add(l2);
            con.add(l3);
            con.add(l4[0]);

            //con.add(l7);
            jl10.setBounds(35, 120+60*i, 60, 25);//控制位置随信息的条数增加变化
            l1.setBounds(135, 120+60*i, 60, 25);
            l2.setBounds(235, 120+60*i, 60, 25);
            l3.setBounds(365, 120+60*i, 60, 25);
            l4[0].setBounds(465, 120+60*i, 60, 25);

            //l7.setBounds(815, 120+70*i, 60, 25);
            JComboBox jc=new JComboBox();//选择查找标准的下拉框
            jc.setEditable(false);//不可编辑
            jc.setEnabled(true);
            String[] arr ={"--分数评级--"};
            ComboBoxModel cbm = new DefaultComboBoxModel(arr);//下拉框的显示不可选
            jc.setModel(cbm);
            jc.addItem("A");//添加选项
            jc.addItem("B");
            jc.addItem("C");
            jc.addItem("D");
            jc.addItem("E");
            jc.addItem("F");

            con.add(jc);
            jc.setBounds(500, 120+70*i, 110, 24);
            JButton jb=new JButton("确认成绩");//查找按钮
            con.add(jb);
                int finalI = i;
                jb.addActionListener(new ActionListener() {
                //给查找按钮添加事件监听

                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO Auto-generated method stub

                    if(jc.getSelectedIndex()!=0)
                    {

                        int temp = jc.getSelectedIndex();
                        if(temp==1){
                            grade="A";
                        }else if(temp==2){
                            grade="B";
                        }else if(temp==3){
                            grade="C";
                        }else if(temp==4){
                            grade="D";
                        }else if(temp==5){
                            grade="E";
                        }else if(temp==6){
                            grade="F";
                        }
                    }
                    con.remove(l4[0]);
                    l4[0] =new JLabel(grade);

                    con.add(l4[0]);
                    l4[0].setBounds(465, 120+70* finalI, 60, 25);
                    try {
                        dos.writeUTF("2u");
                        dos.writeUTF(sid);
                        dos.writeUTF(course);
                        dos.writeUTF(grade);
                        dos.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            });

            jb.setBounds(660, 120+70*i, 100, 24);

            t++;

            i++;

            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
        }
        con.setPreferredSize(new Dimension(1100,150+70*i));

        b2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                System.out.println("返回教授");
                Prof_GUI rg=new Prof_GUI(user_id,pw,socket);

                dispose();
            }
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

}
