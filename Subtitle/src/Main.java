import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Main {

	private static String fileName = "자막.txt";
	
	private static FileReader fr = null;
	private static BufferedReader br = null;
	
	private static ArrayList<String> list = new ArrayList<String>();
	private static int header = 0;
	
	private static Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

	static JTextField tfPrev = new JTextField();
	static JTextField tfPrev2 = new JTextField();
	static JTextField tfCur = new JTextField();
	static JTextField tfCur2 = new JTextField();
	static JTextField tfNext = new JTextField();
	static JTextField tfNext2 = new JTextField();
	
	
	public static void main(String[] args) {
		if(!initFile()) { //파일이 없어서 init에 실패한 경우
			pause();
			return;
		}
		readFile();
		
		//창 init
		JFrame frame = new JFrame("자막 복사기");
		frame.setSize(500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(0, 2));

		tfPrev.setText("이전: ");
		tfPrev.setEditable(false);
		tfPrev2.setText("");
		tfPrev2.setEditable(false);

		tfCur.setText("현재: ");
		tfCur.setEditable(false);
		tfCur2.setText("");
		tfCur2.setEditable(false);

		tfNext.setText("현재: ");
		tfNext.setEditable(false);
		tfNext2.setText("");
		tfNext2.setEditable(false);

		frame.add(tfPrev);
		frame.add(tfPrev2);
		frame.add(tfCur);
		frame.add(tfCur2);
		frame.add(tfNext);
		frame.add(tfNext2);
		
		JButton b1 = new JButton("이전");
		JButton b2 = new JButton("다음");
		
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == b1) {
					back();
					show();
				} else if(e.getSource() == b2) {
					next();
					show();
				}
			}
		};

		b1.addActionListener(listener);
		b2.addActionListener(listener);
		frame.add(b1);
		frame.add(b2);
		
		frame.setVisible(true);

		show();
		
		while(true);
	}
	
	public static void show() {
		if(header > 0) {
			tfPrev2.setText(list.get(header - 1));
		} else {
			tfPrev2.setText("X");
		}
		
		tfCur2.setText(list.get(header));
		
		if(header < list.size() - 1) {
			tfNext2.setText(list.get(header + 1));
		} else {
			tfNext2.setText("X");
		}
		
		copy(list.get(header));
	}
	
	static void back() {
		if(header > 0) {
			header--;
		}
	}
	
	static void next() {
		if(header < list.size() - 1) {
			header++;
		}
	}
	
	static void copy(String str) {
		if(clipboard != null && str != null) {
			StringSelection contents = new StringSelection(str);
			clipboard.setContents(contents, null);
		}
	}
	
	static char readyInput() throws IOException {
		return (char) System.in.read();
	}
	
	private static void pause() {
		// TODO Auto-generated method stub
		try {
			System.out.println("아무 키나 눌러주세요.");
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static boolean initFile() {
		if(fr == null) {
			try {
				fr = new FileReader(fileName);
				br = new BufferedReader(fr);
			} catch(FileNotFoundException e) {
				System.out.println("파일을 찾을 수 없습니다. 자막 파일의 이름을 자막.txt로 한 후 파일과 같은 폴더에 놓아주세요.");
				return false;
			}catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		return true;
	}
	
	//읽을 거 없으면 "" return
	private static void readFile() {
		if(br != null) {
			String s = null;
			try {
				while((s = br.readLine()) != null) {
					list.add(s);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("file이 null입니다");
		}
	}
}
