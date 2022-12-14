/*
 *@author 雷浩洁
 *@version 1.0
 * 登录界面,包括最初的首界面与三种身份（教授学生注册员）登录后的主界面
 * */
package Login;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
public class Login {
	public static void main(String[] args) {
		Main_Login_GUI MLG = new Main_Login_GUI();
	}
}

class Main_Login_GUI extends JFrame implements ActionListener
{	
	//GUI界面相关属性
	public int sys_width;//桌面尺寸
	public int sys_height;
	public int windowsWidth;//窗口尺寸
	public int windowsHeight;
	public String name;//存储用户名与密码
	public String pw;
	public JLabel title;//欢迎标语
	public JComboBox<String> jc;//选择身份
	public JPanel jp;
	public JPanel jp2;
	public JPanel jp3;
	public JPanel con;
	public JTextField nameField;//输入用户名
	public JPasswordField pwField;//输入密码
	public JLabel nameLabel;
	public JLabel pwLabel;
	public JButton jb1;
	public JButton jb2;
	public JButton b2;
	
	//网络编程相关属性
	public Socket socket;
	public DataInputStream dis;
	public DataOutputStream dos;
	
	public Main_Login_GUI() {
		jp = new JPanel();
		jp2 = new JPanel();
		jp3 =  new JPanel();
		//GUI标题
		this.setTitle("CRS");
		JPanel con=new JPanel();


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
	    title = new JLabel("Welcome to CRS!",JLabel.CENTER);
	    this.add(title);
	    
		
	    //输入登录信息
	    jp2.setLayout(new GridLayout(2,2,10,5));
	    nameLabel = new JLabel("ID",JLabel.CENTER);
	    nameField = new JTextField();
	    pwLabel = new JLabel("Password",JLabel.CENTER);
	    pwField = new JPasswordField();
	    jp2.add(nameLabel);
	    jp2.add(nameField);
	    jp2.add(pwLabel);
	    jp2.add(pwField);
	    this.add(jp2);



	    //选择身份
	    jc = new JComboBox<String>();
	    jc.addItem("--- Authentication ---");
	    jc.addItem("Registrar");
	    jc.addItem("Student");
	    jc.addItem("Professor");
	    jp.add(jc);
	    this.add(jp);
	    
	    //确定与取消按钮
	    jb1 = new JButton("Log In");
	    jb2 = new JButton("Cancel");

//		b2=new JButton("返回");

//		b2.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				dispose();
//				Registrar_GUI rg=new Registrar_GUI(socket);
//			}
//		});
//		b2.addActionListener(this);
	    jb1.addActionListener(this);
	    jb2.addActionListener(this);
	    jp3.setLayout(new GridLayout(1,2));
	    jp3.add(jb1);
	    jp3.add(jb2);
//		jp3.add(b2);
	    this.add(jp3);
	    
	    this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		//创建客户端socket，与服务端进行过连接
		try {
			socket = new Socket("127.0.0.1",8888);
			dis = new DataInputStream(
	                new BufferedInputStream(socket.getInputStream()));
	        dos = new DataOutputStream(
	                new BufferedOutputStream(socket.getOutputStream()));
		} catch (UnknownHostException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		
	}
	
	public void actionPerformed(ActionEvent e) {
		/*
		 * 事件监听，主界面只有一种事件：登录
		 */
		String buttonName = e.getActionCommand();
		if(buttonName.equals("Log In")) {
			//保存输入的身份、用户名、密码
			name = new String(nameField.getText());
			pw = new String(pwField.getPassword());
			int temp = jc.getSelectedIndex();
			if(temp==1) {//当前是注册员
				this.dispose();
				Registrar_GUI rGui = new Registrar_GUI(socket);
			}else if (temp==2) {//当前是学生
				if(isCorrect(temp)) {
					this.dispose();
					Stu_GUI stu_GUI = new Stu_GUI(name,pw,socket);
				}else {
					JOptionPane.showMessageDialog(null, "Wrong id or password", "Error",JOptionPane.ERROR_MESSAGE);
				}
			}else if (temp==3) {//当前是教授
				if(isCorrect(temp)) {
					try {//发送2z代码，查询是否有课程被取消，返回有几门课程即可
						dos.writeUTF("2z");
						dos.flush();
						dos.writeUTF(name);//传pid用于检索
						dos.flush();
						String canselcourse=dis.readUTF();//看返回的是否为空决定课程是否被取消
						if(canselcourse.equals("没有取消的课程")==false)//课程被取消
						{
							JOptionPane.showMessageDialog(null,canselcourse , "Prompt",JOptionPane.WARNING_MESSAGE);
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					this.dispose();
					Prof_GUI prof_GUI = new Prof_GUI(name,pw,socket);
				}else {
					JOptionPane.showMessageDialog(null, "Wrong id or password", "Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		else if(buttonName.equals("Cancel")){
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
	}

	public boolean isCorrect(int id) {  
		/*
		 * 首先判断调用该函数的是什么身份：学生或者老师
		 * 将用户名name与密码pw发送给客户端验证密码是否正确
		 */
		String idString=new String();
		String flag="0";//用该变量保存返回的结果，若用户名密码正确，则为1，否则为0
		if(id==2) {
			idString = "10";//1代表学生，0代表执行登录用例
		}else if(id==3){
			idString = "20";//2代表教授，0代表执行登录用例
		}
		
		try {			
			dos.writeUTF(idString);//先发送请求码，告诉服务端执行相应功能
			dos.writeUTF(name);//将学号与密码发给服务器
			dos.writeUTF(pw);
			dos.flush();
			flag = dis.readUTF();
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		if(flag.equals("1")) return true;
		else return false;
	}

}
