/*
 * @author 雷浩洁
 * @version 1.0
 * 注册员客户端代码
 */
package Login;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Registrar_GUI extends JFrame implements ActionListener{
	private int sys_width;
	private int sys_height;
	private int windowsWidth;
	private int windowsHeight;
	
	private JButton jb1;//负责注册员的4个功能：维护学生/教授信息、打开/关闭注册
	private JButton jb2;
	private JButton jb3;
	private JButton jb4;
	private JPanel jp;
	public JButton b2;
	private JLabel title;
	
	//负责与服务器通信的socket，已在构造函数中初始化
	public Socket socket;
	public DataInputStream dis;
	public DataOutputStream dos;
	
	public Registrar_GUI(Socket socket) {
		this.socket = socket;
		try {
			this.dis = new DataInputStream(
			        new BufferedInputStream(socket.getInputStream()));
			this.dos = new DataOutputStream(
	                new BufferedOutputStream(socket.getOutputStream()));
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		this.setTitle("Register management");
		
		//设置窗口大小
		sys_width = Toolkit.getDefaultToolkit().getScreenSize().width;
	    sys_height = Toolkit.getDefaultToolkit().getScreenSize().height;
	    windowsWidth = 350;
	    windowsHeight = 350;
	    this.setSize(windowsWidth,windowsHeight);
	    this.setBounds((sys_width- windowsWidth) / 2,
                (sys_height - windowsHeight) / 2, windowsWidth, windowsHeight);
		
	    //布局
	    this.setLayout(new GridLayout(4,1));
	    
	    //欢迎语
	    title = new JLabel("Welcome , Registrar",JLabel.CENTER);
	    this.add(title);
	    
	    //开启与关闭注册
	    jb1 = new JButton("Open registration");
	    jb2 = new JButton("Close registration");
		b2=new JButton("Back");
	    jb1.addActionListener(this);
	    jb2.addActionListener(this);
		b2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
				Main_Login_GUI rg=new Main_Login_GUI();
			}
		});
	    jp = new JPanel();
	    jp.setLayout(new GridLayout(1,2));
	    jp.add(jb1);
	    jp.add(jb2);
		jp.add(b2);
	    this.add(jp);
	    
	    //维护学生信息
	    jb3 = new JButton("Maintaining Student Information");
	    jb3.addActionListener(this);
	    this.add(jb3);
	    
	    //维护老师信息
	    jb4 = new JButton("Maintaining Professor Information");
	    jb4.addActionListener(this);
	    this.add(jb4);
	    
	    
	    this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==jb1) {  //开启注册按钮

			String res="false";
			try {
				dos.writeUTF("3b");
				dos.flush();
				res=dis.readUTF();
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			if(res.contentEquals("true")) {
				JOptionPane.showMessageDialog(null, "The course selection system has been opened！");
			}else if(res.contentEquals("true")) {
				JOptionPane.showMessageDialog(null, "The course selection system failed to open!", "开启失败 ", JOptionPane.ERROR_MESSAGE);
			}
			
		}else if(e.getSource()==jb2){  //关闭注册按钮
			String res="false";
			try {
				dos.writeUTF("32");
				dos.flush();
				res=dis.readUTF();
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			if(res.contentEquals("true")) {
				JOptionPane.showMessageDialog(null, "The registration system was shut down successfully");
			}else if(res.contentEquals("notEmpty")) {
				JOptionPane.showMessageDialog(null, "There are still people in the course, closing failed!", "关闭失败 ", JOptionPane.ERROR_MESSAGE);
			}
		}else if (e.getSource()==jb3) {  //维护学生信息
			//补充：执行维护学生信息用例
			MaintainStudent mp=new MaintainStudent(socket);
			dispose();
			//或者this.dispose(),不确定哪个是能用的，有一个可以，一个不可以
		}else if(e.getSource()==jb4) {   //维护教授信息
			//补充：执行维护教授信息用例
			MaintainProfessor mp=new MaintainProfessor(socket);
			dispose();
			//同上

		}else if(e.getSource()==b2){

		}
	}
	
}
