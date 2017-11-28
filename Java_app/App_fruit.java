import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.io.BufferedReader;  
import java.io.InputStreamReader;  
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.imageio.ImageIO; 

import java.io.File;  
import java.io.FileOutputStream;  
import java.awt.Color;  
import java.awt.Graphics;  
import java.awt.Image;  
import java.awt.image.BufferedImage;  

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import java.awt.GridLayout;


 public class App_fruit extends JFrame{
    public JFileChooser fileChooser=new JFileChooser("/home");
   
	public void initFrame(){
		
		setTitle("水果识别和腐烂检测程序");
		Toolkit toolkit=getToolkit();
		Dimension screen_size=toolkit.getScreenSize();
		setSize(550,500);
		setLocationRelativeTo(null);
		getContentPane().setBackground(Color.white);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Font font=new  Font("Serief", Font.BOLD, 18);
		Font font1=new  Font("Serief", Font.BOLD, 15);
		//菜单
		MenuBar menubar=new MenuBar();
		setMenuBar(menubar);

		Menu menu1=new Menu("File");
	
		Menu menu2=new Menu("Function");
		Menu menu3=new Menu("Help");
		
		menu1.setFont(font1);
		menu2.setFont(font1);
		menu3.setFont(font1);

		MenuItem item1=new MenuItem("open");
		
		MenuItem item4=new MenuItem("exit");
		MenuItem item5=new MenuItem("kind");
		MenuItem item6=new MenuItem("rot");
		MenuItem item7=new MenuItem("fruitworm");
        MenuItem item8=new MenuItem("about");
		menubar.add(menu1);
		menubar.add(menu2);
		menubar.add(menu3);
		
		menu1.add(item1);
		
		menu1.add(item4);
		menu2.add(item5);
		menu2.add(item6);
		menu2.add(item7);
        menu3.add(item8);
		
		JLabel lab=new JLabel();
		//lab.setBorder(BorderFactory.createLineBorder(Color.red));
		
		add(lab);
        
        //背景图片的路径。（相对路径或者绝对路径。本例图片放于"java项目名"的文件下）  
        String path = "background.jpg";  
        // 背景图片  
        ImageIcon background = new ImageIcon(path);  
        // 把背景图片显示在一个标签里面  
        JLabel label = new JLabel(background);  
        // 把标签的大小位置设置为图片刚好填充整个面板  
        label.setBounds(0, 0, this.getWidth(), this.getHeight());  
        // 把内容窗格转化为JPanel，否则不能用方法setOpaque()来使内容窗格透明  
        JPanel imagePanel = (JPanel) this.getContentPane();  
        imagePanel.setOpaque(false);  
         //把背景图片添加到分层窗格的最底层作为背景  
        this.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));  
		
			
	item4.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0){
                 FontUIResource fontRes = new javax.swing.plaf.FontUIResource(font1);
                setUIFont();
			
                int result = JOptionPane.showConfirmDialog(null,"Are you sure exit?","Exit",
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION )
                {  
                    System.out.println("YES");
                    System.exit(0);
                }
                else if(result == JOptionPane.NO_OPTION )
                {
                    System.out.println("NO");
                }
			
			}
		});
        item8.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0){
               FontUIResource fontRes = new javax.swing.plaf.FontUIResource(font1);
                setUIFont();
			//JOptionPane.showInternalMessageDialog(lab, "You can see more help at my github",
						//"Help", JOptionPane.INFORMATION_MESSAGE);
                        Second hw=new Second(new App_fruit());  //使用匿名对象
            
			
			}
		});

		item6.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0){
			
			try{
			String cmd=String.format("./test %s",fileChooser.getSelectedFile()); 
        	Process ps1 = Runtime.getRuntime().exec(cmd);  
            
          
			}
			catch (Exception e) {  
            e.printStackTrace();  
            }  
            
			}
		});
		
		item1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
			
			
                lab.setVerticalTextPosition(JLabel.BOTTOM);
                lab.setHorizontalTextPosition(JLabel.CENTER); 
				//FileFilter 
				int ret=fileChooser.showOpenDialog(null);
				if(ret==JFileChooser.APPROVE_OPTION){
                    try
                    {
                        Jpgset();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace(); 
                        
                    }
                    System.out.println(fileChooser.getSelectedFile());
                    ImageIcon image=new ImageIcon(fileChooser.getSelectedFile().getAbsolutePath());
					image.setImage(image.getImage().getScaledInstance(320,320,Image.SCALE_DEFAULT));
					lab.setIcon(image);
					 
            
				}
            }
		});
		
		item5.addActionListener(new ActionListener() {
		
			
			public void actionPerformed(ActionEvent arg0){
				 int j=0;
				 int k=0;
				try{
				String str=String.format("./build/examples/cpp_classification/classification.bin examples/classical/deploy.prototxt examples/classical/my_iter_6000.caffemodel examples/classical/mean.binaryproto examples/classical/synset_words.txt %s",
                fileChooser.getSelectedFile()); 
				System.out.println(str);
                    
            Process ps = Runtime.getRuntime().exec(str);  
            ps.waitFor();  
  
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));  
            StringBuffer sr =new StringBuffer();  
            String line;  
            while ((line = br.readLine()) != null) {  
                sr.append(line).append("\n");  
            } 
           
            String result = sr.toString();  
            byte[] bytes=result.getBytes();
            
            for(int i=0;i<result.length();i++)
            {
                if(bytes[i]=='-')
                {
                j++;
                }
                if(j==20)
                {
                    k=i;
                    break;
                }
            }
            
            System.out.println(k);          
             result=result.substring(k-2); 
            result=result.substring(10,19);
             byte[] bytes1=result.getBytes();
             j=0;
             for(int i=0;i<result.length();i++)
            {
                if(bytes1[i]=='#')
                {
                j=i;
                break;
                }
            }
              if(j!=0)
              result=result.substring(0,j);
             
            System.out.println(result);  
				lab.setText(result);
				Font font=new  Font("Serief", Font.BOLD, 18);
			        lab.setFont(font);
			        lab.setVerticalTextPosition(JLabel.BOTTOM);
                    lab.setHorizontalTextPosition(JLabel.CENTER); 
		
            
				}
				 catch (Exception e) {  
            e.printStackTrace();  
            }  
				
			}
		});


		item7.addActionListener(new ActionListener() {
		
			
			public void actionPerformed(ActionEvent arg0){
				 int j=0;
				 int k=0;
				
				try{
				String str=String.format("./build/examples/cpp_classification/classification.bin examples/classical_2/deploy.prototxt examples/classical_2/my_iter_6000.caffemodel examples/classical_2/mean.binaryproto examples/classical_2/synset_words.txt %s",
                fileChooser.getSelectedFile()); 
				
				System.out.println(str);
                    
            Process ps = Runtime.getRuntime().exec(str);  
            ps.waitFor();  
  
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));  
            StringBuffer sr =new StringBuffer();  
            String line;  
            while ((line = br.readLine()) != null) {  
                sr.append(line).append("\n");  
            } 
           
            String result = sr.toString();  
            byte[] bytes=result.getBytes();
            
            for(int i=0;i<result.length();i++)
            {
                if(bytes[i]=='-')
                {
                j++;
                }
                if(j==20)
                {
                    k=i;
                    break;
                }
            }
            
            System.out.println(k);          
             result=result.substring(k-2); 
            result=result.substring(10,19);
             byte[] bytes1=result.getBytes();
             j=0;
             for(int i=0;i<result.length();i++)
            {
                if(bytes1[i]=='#')
                {
                j=i;
                break;
                }
            }
              if(j!=0)
              result=result.substring(0,j);
             
            System.out.println(result);  
				lab.setText(result);
				Font font=new  Font("Serief", Font.BOLD, 18);
			        lab.setFont(font);
			        lab.setVerticalTextPosition(JLabel.BOTTOM);
                    lab.setHorizontalTextPosition(JLabel.CENTER); 
		
            
				}
				 catch (Exception e) {  
            e.printStackTrace();  
            }  
				
			}
		});
		setVisible(true);
	}
    
    public void Jpgset()  throws Exception{
        String str=String.format("%s",fileChooser.getSelectedFile());   
        System.out.println(str);    
       File _file = new File(str); // 读入文件  

       Image src = javax.imageio.ImageIO.read(_file); // 构造Image对象  
       int width = src.getWidth(null); // 得到源图宽  
       int height = src.getHeight(null); // 得到源图长  
        
       //需要长度  
       int newwidth =width ;
       int newheight = width ;
       BufferedImage image = new BufferedImage(newwidth, newheight,  
         BufferedImage.TYPE_INT_RGB);  
       Graphics graphics = image.getGraphics();  
        
              
       graphics.drawImage(src, 0, 0, newwidth, newheight, null); // 绘制缩小后的图  
      // 画边框,在drawImage后面，下面代码给图片加上两个像素的白边     
       graphics.setColor(Color.GREEN);     
       graphics.drawRect(0, 0, newwidth - 1, newheight - 1);  
       graphics.drawRect(1, 1, newwidth - 1, newheight - 1);  
       graphics.drawRect(0, 0, newwidth - 2, newheight - 2);  

        String formatName = str.substring(str.lastIndexOf(".") + 1);
         //FileOutputStream out = new FileOutputStream(dstName);
         //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
         //encoder.encode(dstImage);                            //老版本
         ImageIO.write(image, /*"GIF"*/ formatName /* format desired */ , new File(str) /* target */ );
        
       
    }  
    public static void setUIFont()
    {
	    Font f = new Font("宋体",Font.PLAIN,20);
	    String   names[]={ "Label"
	        }; 
	for (String item : names) {
		 UIManager.put(item+ ".font",f); 
	    }
    }
     /*names[]={ "Label", "CheckBox", "PopupMenu","MenuItem", "CheckBoxMenuItem",
			"JRadioButtonMenuItem","ComboBox", "Button", "Tree", "ScrollPane",
			"TabbedPane", "EditorPane", "TitledBorder", "Menu", "TextArea",
			"OptionPane", "MenuBar", "ToolBar", "ToggleButton", "ToolTip",
			"ProgressBar", "TableHeader", "Panel", "List", "ColorChooser",
			"PasswordField","TextField", "Table", "Label", "Viewport",
			"RadioButtonMenuItem","RadioButton", "DesktopPane", "InternalFrame"
	};*/ 
    public static void main(String[] args)
	{
		App_fruit f=new App_fruit();
		f.initFrame();
		
	}
}
 class Second {
JDialog jDialog1=null; //创建一个空的对话框对象
Second(JFrame jFrame){
/* 初始化jDialog1
* 指定对话框的拥有者为jFrame,标题为"About",当对话框为可视时,其他构件不能
* 接受用户的输入(静态对话框) */
jDialog1=new JDialog(jFrame,"About",true);
JLabel j1=new JLabel("Version:2.0");
JLabel j2=new JLabel("You can find more help in my github XPG1017");
jDialog1.getContentPane().add(j1);
jDialog1.getContentPane().add(j2);
/* 设置对话框的初始大小 */
jDialog1.setSize(450,200);
/* 设置对话框初始显示在屏幕当中的位置 */
jDialog1.setLocation(450,450);
/* 设置对话框为可见 */
jDialog1.setLayout(new GridLayout(3,1));
jDialog1.setVisible(true);
}
}






