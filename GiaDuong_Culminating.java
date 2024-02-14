// ICS4U: Gia Duong - Culminating "JoD Rex Game"
// Final Submission (Fri Jun 13, 2023)
// Input: Prompt for user name, password, emali(optional)
// Processing: Calculuating position of palyer to avoid detect collision
// Output: Display user's current score, highest score, and all users' scores as top 10 ranking from highest to lowest

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.net.URL;
import javax.sound.sampled.*;
import java.util.List;

public class GiaDuong_Culminating{
	public static void main(String[] args) {
		new RegisterFrame();
	}
}

class RegisterFrame extends JFrame implements ActionListener {
	public static final int screenWidth = 1280;    // 1280
	public static final int screenHeight = 768;   // 768

	static JButton signInButton, signUpButton, quitButton, onOffSoundButton, InfoButton, RankButton;
  static JTextField userNameBox, passwordBox, emailBox;

	static String Username;		// will be use to store current user name

	private int lineNum;
	private Font pixelFont;
	private Clip clip;
	private boolean isMuted = false;
	private boolean swapped;

	static List<Integer> bestScoreList = new ArrayList<>();
	static int[] bestScoreArr;
	static List<String> usrNameList = new ArrayList<>();
	static String[] usrNameArr;
	
	public RegisterFrame() {
    super("The JoD Rex Game - Register Frame - By GiaD");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(screenWidth, screenHeight);

    JPanel panel = new JPanel();
    panel.setLayout(null);

		try {
			pixelFont = Font.createFont(Font.TRUETYPE_FONT, new File("Font/PixeloidSans.ttf")).deriveFont(16f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Font/PixeloidSans.ttf")));
		} catch (IOException | FontFormatException e) {}

    JLabel BGLabel = new JLabel();
    ImageIcon BGIcon = new ImageIcon("Image/TitleScreen.png");
    BGLabel.setIcon(BGIcon);
    BGLabel.setBounds(0, 0, BGIcon.getIconWidth(), BGIcon.getIconHeight());

		JLabel TitleLabel = new JLabel();
    ImageIcon LabelIcon = new ImageIcon("Image/Title_Text_Resized.png");
    TitleLabel.setIcon(LabelIcon);
    TitleLabel.setBounds(getWidth()/2-330, -70, LabelIcon.getIconWidth(), LabelIcon.getIconHeight());

    userNameBox = new JTextField(20);
    userNameBox.setBounds(getWidth() / 2 - 110, 250, 200, 30);

		passwordBox = new JTextField(20);
    passwordBox.setBounds(getWidth() / 2 - 110, 340, 200, 30);

		emailBox = new JTextField(20);
    emailBox.setBounds(getWidth() / 2 - 110, 430, 200, 30);

		JLabel userNameLabel = new JLabel("*Enter UserName:");
    userNameLabel.setBounds(getWidth() / 2 - 110, 220, 200, 30);
    userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userNameLabel.setFont(pixelFont);
		userNameLabel.setForeground(Color.BLACK);

		JLabel passwordLabel = new JLabel("*Enter Password:");
    passwordLabel.setBounds(getWidth() / 2 - 110, 310, 200, 30);
    passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		passwordLabel.setFont(pixelFont);
		passwordLabel.setForeground(Color.BLACK);

		JLabel emailLabel = new JLabel("Enter Email (optional):");
    emailLabel.setBounds(getWidth() / 2 - 110, 400, 200, 30);
    emailLabel.setHorizontalAlignment(SwingConstants.CENTER);
		emailLabel.setFont(pixelFont);
		emailLabel.setForeground(Color.BLACK);

    ImageIcon iconSign1 = new ImageIcon("Image/Button_SignIn.png");
		signInButton = new JButton(iconSign1);
		signInButton.setBounds(getWidth()/2-110-192-150, 550, 194, 97);
		signInButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign2 = new ImageIcon("Image/Button_SignUp.png");
		signUpButton = new JButton(iconSign2);
		signUpButton.setBounds(getWidth()/2-110, 550, 192, 97);
		signUpButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign3 = new ImageIcon("Image/Button_Quit.png");
		quitButton = new JButton(iconSign3);
		quitButton.setBounds(getWidth()/2-110+192+150, 550, 194, 97);
		quitButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign4 = new ImageIcon("Image/OnOffSound_Button_resized.png");
		onOffSoundButton = new JButton(iconSign4);
		onOffSoundButton.setBounds(50, 50, 60, 60);
		onOffSoundButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign5 = new ImageIcon("Image/Info_Button_resized.png");
		InfoButton = new JButton(iconSign5);
		InfoButton.setBounds(50, 140, 60, 60);
		InfoButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign6 = new ImageIcon("Image/RankButton_resized.png");
		RankButton = new JButton(iconSign6);
		RankButton.setBounds(50, 230, 60, 60);
		RankButton.addActionListener((ActionListener) this); 

		try {		// get sound
      URL url = this.getClass().getClassLoader().getResource("Sound/AdhesiveWombat.wav");
      AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
      clip = AudioSystem.getClip(); // Assign the Clip instance to the variable
      clip.open(audioIn);
      clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
    } catch (UnsupportedAudioFileException err) {
      err.printStackTrace();
    } catch (IOException err) {
      err.printStackTrace();
    } catch (LineUnavailableException err) {
      err.printStackTrace();
    }

		try (BufferedReader myReader = new BufferedReader(new FileReader("UserData/ScoreBoard.txt"))) {		// get username from reading from file, and put in array
			String lineText;
			while ((lineText = myReader.readLine()) != null) {
				if (!lineText.trim().isEmpty()) {
					if (lineText.matches(".*[a-zA-Z]+.*")) {
						String myLineText = lineText.trim();
						usrNameList.add(myLineText);
					}
				}
			}
		} catch (IOException e) {
      e.printStackTrace();
    }

		usrNameArr = new String[usrNameList.size()];
		for (int index=0; index<usrNameList.size(); index++) {
			usrNameArr[index] = usrNameList.get(index);
		}

		try (BufferedReader myReader = new BufferedReader(new FileReader("UserData/ScoreBoard.txt"))) {			// get overall highest scores from reading from a file, and put in array
			String lineText;
			while ((lineText = myReader.readLine()) != null) {		
				if (!lineText.trim().isEmpty()) {		
					try {
						int number = Integer.parseInt(lineText.trim());
						bestScoreList.add(number);
					} catch (NumberFormatException e) {}
				}
			}
		} catch (IOException e) {
       e.printStackTrace();
    }

		bestScoreArr = new int[bestScoreList.size()];
		for (int index=0; index<bestScoreList.size(); index++) {
			bestScoreArr[index] = bestScoreList.get(index);
		}

		for (int index=0; index < bestScoreArr.length-1; index++) {
			swapped = false;
			for (int num=0; num < bestScoreArr.length-index-1; num++) {
				if (bestScoreArr[num] < bestScoreArr[num+1]) {
					int temp = bestScoreArr[num];
					String tempStr = usrNameArr[num];
					bestScoreArr[num] = bestScoreArr[num+1];
					usrNameArr[num] = usrNameArr[num+1];
					bestScoreArr[num+1] = temp;
					usrNameArr[num+1] = tempStr;
					swapped = true;
				}
			}
			if (swapped == false) {
				break;
			}
		}

    panel.add(signInButton);
		panel.add(signUpButton);
		panel.add(quitButton);
		panel.add(onOffSoundButton);
		panel.add(InfoButton);
		panel.add(RankButton);
    panel.add(userNameBox);
		panel.add(passwordBox);
		panel.add(emailBox);
		panel.add(userNameLabel);
		panel.add(passwordLabel);
		panel.add(emailLabel);
		panel.add(TitleLabel);
		panel.add(BGLabel);
    add(panel);

    setLocationRelativeTo(null);
    setVisible(true);
    }

	void readFile() {
		try {
			FileReader myReader = new FileReader("UserData/UserData.txt");
			System.out.println("file exists!");
		} catch (FileNotFoundException ex) {
			try {
				FileWriter myWriter = new FileWriter("UserData/UserData.txt");
				System.out.println("File created");
			} catch (IOException ex1) {
				Logger.getLogger(RegisterFrame.class.getName()).log(Level.SEVERE, null, ex1);
			}
		}	
	}
    
	void addData(String userN,String passW,String email){
		try {
			RandomAccessFile myFile = new RandomAccessFile("UserData/UserData.txt", "rw");
			for(int index=0; index<lineNum; index++){
				myFile.readLine();
			}
			if(lineNum>0){
				myFile.writeBytes("\r\n");
				myFile.writeBytes("\r\n");
			}
				myFile.writeBytes("Username:"+userN+ "\r\n");
				myFile.writeBytes("Password:"+passW+ "\r\n");
				myFile.writeBytes("Email:"+email);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(RegisterFrame.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(RegisterFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
    
  boolean logic(String userN,String passW){
    try {
      RandomAccessFile myFile = new RandomAccessFile("UserData/UserData.txt", "rw");
      for(int index=0; index<lineNum; index+=4){
        String forUser = myFile.readLine().substring(9);
        String forPswd = myFile.readLine().substring(9);
        if(userN.equals(forUser) & passW.equals(forPswd)){
          JOptionPane.showMessageDialog(null, "Successfully sign in!\nAre you ready for the game?");
          return true;
        } else if(index==(lineNum-3)){
          JOptionPane.showMessageDialog(null, "Incorrect Username/Password!\nTry again!");
          return false;
        }
        for(int num=1; num<=2; num++){
          myFile.readLine();
        }
      }
    } catch (FileNotFoundException ex) {
      Logger.getLogger(RegisterFrame.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(RegisterFrame.class.getName()).log(Level.SEVERE, null, ex);
    }
		return false;
  }
    
	void countLines(){
		try {
			lineNum=0;
			RandomAccessFile myFile = new RandomAccessFile("UserData/UserData.txt", "rw");
			for(int index=0; myFile.readLine()!=null; index++){
				lineNum++;
			}
			System.out.println("number of lines: " + lineNum);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(RegisterFrame.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(RegisterFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == signInButton) {
			readFile();
			countLines();
			Username = userNameBox.getText();
			boolean passwordMatches = logic(Username, passwordBox.getText());
			if (passwordMatches) {
				clip.stop();
				new IntroFrame();
				setVisible(false);
			} else {
				userNameBox.setText("");
				passwordBox.setText("");
				emailBox.setText("");
			}
		} else if (e.getSource() == signUpButton) {
			readFile();
			countLines();
			addData(userNameBox.getText(), passwordBox.getText(), emailBox.getText());
			JOptionPane.showMessageDialog(null,"New Data Registered.\nSign in now.");
			userNameBox.setText("");
			passwordBox.setText("");
		} else if (e.getSource() == quitButton) {
			clip.stop();
			setVisible(false);
		} else if (e.getSource() == onOffSoundButton) {
			if (isMuted) {		// unmute
				clip.start();
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				isMuted = false;
			} else {			// mute
				clip.stop();
				isMuted = true;
			}
		} else if (e.getSource() == InfoButton) {
			clip.stop();
			new aboutMeFrame_RegisterF();
			setVisible(false);
		} else if (e.getSource() == RankButton) {
			clip.stop();
			new EverybodyScore_RegisterF();
			setVisible(false);
		}
	}
}

class IntroFrame extends JFrame implements ActionListener {
	static JButton startButton, ruleButton, SignOutButton, onOffSoundButton, InfoButton, RankButton, HomeButton;

	private Clip clip;
	private boolean isMuted = false;

	public IntroFrame() {
		super("The JoD Rex Game - Intro Frame - By GiaD");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(RegisterFrame.screenWidth, RegisterFrame.screenHeight);

		JPanel panel = new JPanel();
		panel.setLayout(null);

		JLabel BGLabel = new JLabel();
    ImageIcon BGIcon = new ImageIcon("Image/Intro_Screen.png");
    BGLabel.setIcon(BGIcon);
    BGLabel.setBounds(0, 0, BGIcon.getIconWidth(), BGIcon.getIconHeight());

		JLabel TitleLabel = new JLabel();
    ImageIcon LabelIcon = new ImageIcon("Image/Title_Text_Resized.png");
    TitleLabel.setIcon(LabelIcon);
    TitleLabel.setBounds(getWidth()/2-330, -70, LabelIcon.getIconWidth(), LabelIcon.getIconHeight());

		JLabel memeLabel = new JLabel();
		ImageIcon MemeIcon = new ImageIcon("Image/Fynn_IntroFrame_resized.png");
		memeLabel.setIcon(MemeIcon);
		memeLabel.setBounds(180, 200, MemeIcon.getIconWidth(), MemeIcon.getIconHeight());

    ImageIcon iconSign1 = new ImageIcon("Image/Button_Start.png");
		startButton = new JButton(iconSign1);
		startButton.setBounds(getWidth()/2-110-192-150, 550, 194, 97);
		startButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign2 = new ImageIcon("Image/Button_Rule.png");
		ruleButton = new JButton(iconSign2);
		ruleButton.setBounds(getWidth()/2-110, 550, 192, 97);
		ruleButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign3 = new ImageIcon("Image/Button_SignOut.png");
		SignOutButton = new JButton(iconSign3);
		SignOutButton.setBounds(getWidth()/2-110+192+150, 550, 194, 97);
		SignOutButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign4 = new ImageIcon("Image/OnOffSound_Button_resized.png");
		onOffSoundButton = new JButton(iconSign4);
		onOffSoundButton.setBounds(50, 50, 60, 60);
		onOffSoundButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign5 = new ImageIcon("Image/Info_Button_resized.png");
		InfoButton = new JButton(iconSign5);
		InfoButton.setBounds(50, 140, 60, 60);
		InfoButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign6 = new ImageIcon("Image/RankButton_resized.png");
		RankButton = new JButton(iconSign6);
		RankButton.setBounds(50, 230, 60, 60);
		RankButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign7 = new ImageIcon("Image/HomeButton_resized.png");
		HomeButton = new JButton(iconSign7);
		HomeButton.setBounds(50, 320, 60, 60);
		HomeButton.addActionListener((ActionListener) this); 

		try {
			URL url = this.getClass().getClassLoader().getResource("Sound/Kevin_MacLeod_Itty_Bitty.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip(); // Assign the Clip instance to the variable
			clip.open(audioIn);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(5.0f);
      clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException err) {
				err.printStackTrace();
		} catch (IOException err) {
				err.printStackTrace();
		} catch (LineUnavailableException err) {
				err.printStackTrace();
		}

    panel.add(startButton);
		panel.add(ruleButton);
		panel.add(SignOutButton);
		panel.add(onOffSoundButton);
		panel.add(InfoButton);
		panel.add(RankButton);
		panel.add(HomeButton);
		panel.add(memeLabel);
		panel.add(TitleLabel);
		panel.add(BGLabel);
		add(panel);

		setLocationRelativeTo(null);
		setVisible(true);
  }

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startButton) {
			clip.stop();
			MainGameFrame obj1 = new MainGameFrame();
			obj1.startGame();
			setVisible(false);
		} else if (e.getSource() == ruleButton) {
			new ruleFrame();
			clip.stop();
			setVisible(false);
		} else if (e.getSource() == SignOutButton) {
			clip.stop();
			new RegisterFrame();
			setVisible(false);
		} else if (e.getSource() == onOffSoundButton) {
			if (isMuted) {		// unmute
				clip.start();
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				isMuted = false;
			} else {			// mute
				clip.stop();
				isMuted = true;
			}
		} else if (e.getSource() == InfoButton) {
			clip.stop();
			new aboutMeFrame();
			setVisible(false);
		} else if (e.getSource() == HomeButton) {
			clip.stop();
			new IntroFrame();
			setVisible(false);
		} else if (e.getSource() == RankButton) {
			clip.stop();
			new EverybodyScore();
			setVisible(false);
		}
	}
}

class ruleFrame extends JFrame implements ActionListener {
	static JButton goBackButton, SignOutButton, onOffSoundButton, InfoButton, RankButton, HomeButton;
	Font pixelFont, pixelFontBold;
	JLabel textLabel1, textLabel2, textLabel3, titleLabel, creditLabel;
	Color ruleTextColor = new Color(249, 123, 34);
	private Clip clip;
	private boolean isMuted = false;

	public ruleFrame() {
		super("The JoD Rex Game - Rule Frame - By GiaD");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(RegisterFrame.screenWidth, RegisterFrame.screenHeight);

		JPanel panel = new JPanel();
		panel.setLayout(null);

		try {
			pixelFont = Font.createFont(Font.TRUETYPE_FONT, new File("Font/PixeloidSans.ttf")).deriveFont(20f);
			pixelFontBold = Font.createFont(Font.TRUETYPE_FONT, new File("Font/PixeloidSansBold.ttf")).deriveFont(30f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Font/PixeloidSans.ttf")));
		} catch (IOException | FontFormatException e) {}

		titleLabel = new JLabel("Instruction");
		titleLabel.setBounds(250, -130, getWidth(), getHeight());
		titleLabel.setFont(pixelFontBold);
		titleLabel.setForeground(ruleTextColor);

		textLabel1 = new JLabel("Press \"Space\" to start the game and jump your character.");
		textLabel1.setBounds(250, -70, getWidth(), getHeight());
		textLabel1.setFont(pixelFont);
		textLabel1.setForeground(ruleTextColor);

		textLabel2 = new JLabel("Keep tapping \"Space\" to jump over the obstacles that come along.");
		textLabel2.setBounds(250, -20, getWidth(), getHeight());
		textLabel2.setFont(pixelFont);
		textLabel2.setForeground(ruleTextColor);

		textLabel3 = new JLabel("The longer your character survive, the more points you start getting.");
		textLabel3.setBounds(250, 30, getWidth(), getHeight());
		textLabel3.setFont(pixelFont);
		textLabel3.setForeground(ruleTextColor);

		creditLabel = new JLabel("[Made by: @giajolied]");
		creditLabel.setBounds(860, -180, getWidth(), getHeight());
		creditLabel.setFont(pixelFont);
		creditLabel.setForeground(Color.black);

		JLabel BGLabel = new JLabel();
    ImageIcon BGIcon = new ImageIcon("Image/Register_rule_resized.png");
    BGLabel.setIcon(BGIcon);
    BGLabel.setBounds(0, 0, BGIcon.getIconWidth(), BGIcon.getIconHeight());

		JLabel TitleLabel = new JLabel();
    ImageIcon LabelIcon = new ImageIcon("Image/Title_Text_Resized.png");
    TitleLabel.setIcon(LabelIcon);
    TitleLabel.setBounds(getWidth()/2-330, -70, LabelIcon.getIconWidth(), LabelIcon.getIconHeight());

		ImageIcon iconSign1 = new ImageIcon("Image/Button_GoBack.png");
		goBackButton = new JButton(iconSign1);
		goBackButton.setBounds(180, 541, 194, 97);
		goBackButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign2 = new ImageIcon("Image/Button_SignOut.png");
		SignOutButton = new JButton(iconSign2);
		SignOutButton.setBounds(905, 541, 194, 97);
		SignOutButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign3 = new ImageIcon("Image/OnOffSound_Button_resized.png");
		onOffSoundButton = new JButton(iconSign3);
		onOffSoundButton.setBounds(50, 50, 60, 60);
		onOffSoundButton.addActionListener((ActionListener) this);

		ImageIcon iconSign4 = new ImageIcon("Image/Info_Button_resized.png");
		InfoButton = new JButton(iconSign4);
		InfoButton.setBounds(50, 140, 60, 60);
		InfoButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign6 = new ImageIcon("Image/RankButton_resized.png");
		RankButton = new JButton(iconSign6);
		RankButton.setBounds(50, 230, 60, 60);
		RankButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign7 = new ImageIcon("Image/HomeButton_resized.png");
		HomeButton = new JButton(iconSign7);
		HomeButton.setBounds(50, 320, 60, 60);
		HomeButton.addActionListener((ActionListener) this); 

		try {
			URL url = this.getClass().getClassLoader().getResource("Sound/Kevin_MacLeod_Light_Sting.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip(); // Assign the Clip instance to the variable
			clip.open(audioIn);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(5.0f);
			clip.start();
		} catch (UnsupportedAudioFileException err) {
			err.printStackTrace();
		} catch (IOException err) {
			err.printStackTrace();
		} catch (LineUnavailableException err) {
			err.printStackTrace();
		}

		panel.add(goBackButton);
		panel.add(SignOutButton);
		panel.add(onOffSoundButton);
		panel.add(InfoButton);
		panel.add(RankButton);
		panel.add(HomeButton);
		panel.add(titleLabel);
		panel.add(textLabel1);
		panel.add(textLabel2);
		panel.add(textLabel3);
		panel.add(creditLabel);
		panel.add(TitleLabel);
		panel.add(BGLabel);
		add(panel);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == goBackButton) {
			clip.stop();
			new IntroFrame();
			setVisible(false);
		} else if (e.getSource() == SignOutButton) {
			clip.stop();
			new RegisterFrame();
			setVisible(false);
		} else if (e.getSource() == onOffSoundButton) {
			if (isMuted) {		// unmute
				clip.start();
				isMuted = false;
			} else {			// mute
				clip.stop();
				isMuted = true;
			}
		} else if (e.getSource() == InfoButton) {
			clip.stop();
			new aboutMeFrame();
			setVisible(false);
		} else if (e.getSource() == RankButton) {
			clip.stop();
			new EverybodyScore();
			setVisible(false);
		} else if (e.getSource() == HomeButton) {
			clip.stop();
			new IntroFrame();
			setVisible(false);
		}
	}
}

class aboutMeFrame extends JFrame implements ActionListener {
	static JButton goBackButton, quitButton, onOffSoundButton, InfoButton, RankButton, HomeButton;
	Font pixelFont, pixelFontBold;
	JLabel textLabel1, textLabel2, textLabel3, textLabel4, textLabel5, titleLabel, creditLabel;
	Color ruleTextColor = new Color(249, 123, 34);
	private Clip clip;
	private boolean isMuted = false;

	public aboutMeFrame() {
		super("The JoD Rex Game - About Me Frame - By Gia D");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(RegisterFrame.screenWidth, RegisterFrame.screenHeight);

		JPanel panel = new JPanel();
		panel.setLayout(null);

		try {
			pixelFont = Font.createFont(Font.TRUETYPE_FONT, new File("Font/PixeloidSans.ttf")).deriveFont(20f);
			pixelFontBold = Font.createFont(Font.TRUETYPE_FONT, new File("Font/PixeloidSansBold.ttf")).deriveFont(30f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Font/PixeloidSans.ttf")));
		} catch (IOException | FontFormatException e) {}

		titleLabel = new JLabel("About Me");
		titleLabel.setBounds(200, -130, getWidth(), getHeight());
		titleLabel.setFont(pixelFontBold);
		titleLabel.setForeground(ruleTextColor);

		textLabel1 = new JLabel("     This is Java Game made by me, Gia Duong - Gr11 student from ECI, called \"The");
		textLabel1.setBounds(200, -70, getWidth(), getHeight());
		textLabel1.setFont(pixelFont);
		textLabel1.setForeground(ruleTextColor);

		textLabel2 = new JLabel("Jod Rex Game\". The original idea is an offline Dinosaur Game developed by Google.");
		textLabel2.setBounds(200, -20, getWidth(), getHeight());
		textLabel2.setFont(pixelFont);
		textLabel2.setForeground(ruleTextColor);

		textLabel3 = new JLabel("From the original game, I made some modifies and added some other features.");
		textLabel3.setBounds(200, 30, getWidth(), getHeight());
		textLabel3.setFont(pixelFont);
		textLabel3.setForeground(ruleTextColor);

		textLabel4 = new JLabel("     Now, Let's enjoy the Game, shall we?!");
		textLabel4.setBounds(200, 80, getWidth(), getHeight());
		textLabel4.setFont(pixelFont);
		textLabel4.setForeground(ruleTextColor);

		textLabel5 = new JLabel("     Contact me if you have any questions: dghao5906@gmail.com");
		textLabel5.setBounds(200, 130, getWidth(), getHeight());
		textLabel5.setFont(pixelFont);
		textLabel5.setForeground(ruleTextColor);

		creditLabel = new JLabel("[Made by: @giajolied]");
		creditLabel.setBounds(860, -180, getWidth(), getHeight());
		creditLabel.setFont(pixelFont);
		creditLabel.setForeground(Color.black);

		JLabel BGLabel = new JLabel();
    ImageIcon BGIcon = new ImageIcon("Image/Register_rule_resized.png");
    BGLabel.setIcon(BGIcon);
    BGLabel.setBounds(0, 0, BGIcon.getIconWidth(), BGIcon.getIconHeight());

		JLabel TitleLabel = new JLabel();
    ImageIcon LabelIcon = new ImageIcon("Image/Title_Text_Resized.png");
    TitleLabel.setIcon(LabelIcon);
    TitleLabel.setBounds(getWidth()/2-330, -70, LabelIcon.getIconWidth(), LabelIcon.getIconHeight());

		ImageIcon iconSign1 = new ImageIcon("Image/Button_GoBack.png");
		goBackButton = new JButton(iconSign1);
		goBackButton.setBounds(180, 541, 194, 97);
		goBackButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign2 = new ImageIcon("Image/Button_Quit.png");
		quitButton = new JButton(iconSign2);
		quitButton.setBounds(905, 541, 194, 97);
		quitButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign3 = new ImageIcon("Image/OnOffSound_Button_resized.png");
		onOffSoundButton = new JButton(iconSign3);
		onOffSoundButton.setBounds(50, 50, 60, 60);
		onOffSoundButton.addActionListener((ActionListener) this);

		ImageIcon iconSign4 = new ImageIcon("Image/Info_Button_resized.png");
		InfoButton = new JButton(iconSign4);
		InfoButton.setBounds(50, 140, 60, 60);
		InfoButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign6 = new ImageIcon("Image/RankButton_resized.png");
		RankButton = new JButton(iconSign6);
		RankButton.setBounds(50, 230, 60, 60);
		RankButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign7 = new ImageIcon("Image/HomeButton_resized.png");
		HomeButton = new JButton(iconSign7);
		HomeButton.setBounds(50, 320, 60, 60);
		HomeButton.addActionListener((ActionListener) this); 

		try {
			URL url = this.getClass().getClassLoader().getResource("Sound/Kevin_MacLeod_Light_Sting.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip(); // Assign the Clip instance to the variable
			clip.open(audioIn);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(5.0f);
			clip.start();
		} catch (UnsupportedAudioFileException err) {
			err.printStackTrace();
		} catch (IOException err) {
			err.printStackTrace();
		} catch (LineUnavailableException err) {
			err.printStackTrace();
		}

		panel.add(goBackButton);
		panel.add(quitButton);
		panel.add(onOffSoundButton);
		panel.add(InfoButton);
		panel.add(RankButton);
		panel.add(HomeButton);
		panel.add(titleLabel);
		panel.add(textLabel1);
		panel.add(textLabel2);
		panel.add(textLabel3);
		panel.add(textLabel4);
		panel.add(textLabel5);
		panel.add(creditLabel);
		panel.add(TitleLabel);
		panel.add(TitleLabel);
		panel.add(BGLabel);
		add(panel);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == goBackButton) {
			clip.stop();
			new IntroFrame();
			setVisible(false);
		} else if (e.getSource() == quitButton) {
			clip.stop();
			setVisible(false);
		} else if (e.getSource() == onOffSoundButton) {
			if (isMuted) {		// unmute
				clip.start();
				isMuted = false;
			} else {			// mute
				clip.stop();
				isMuted = true;
			}
		} else if (e.getSource() == RankButton) {
			clip.stop();
			new EverybodyScore();
			setVisible(false);
		} else if (e.getSource() == HomeButton) {
			clip.stop();
			new IntroFrame();
			setVisible(false);
		} else if (e.getSource() == InfoButton) {
			clip.stop();
			new aboutMeFrame();
			setVisible(false);
		}
	}
}

class aboutMeFrame_RegisterF extends JFrame implements ActionListener {
	static JButton goBackButton, quitButton, onOffSoundButton, InfoButton, RankButton;
	Font pixelFont, pixelFontBold;
	JLabel textLabel1, textLabel2, textLabel3, textLabel4, textLabel5, titleLabel, creditLabel;
	Color ruleTextColor = new Color(249, 123, 34);
	private Clip clip;
	private boolean isMuted = false;

	public aboutMeFrame_RegisterF() {
		super("The JoD Rex Game - About Me Frame - By Gia D");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(RegisterFrame.screenWidth, RegisterFrame.screenHeight);

		JPanel panel = new JPanel();
		panel.setLayout(null);

		try {
			pixelFont = Font.createFont(Font.TRUETYPE_FONT, new File("Font/PixeloidSans.ttf")).deriveFont(20f);
			pixelFontBold = Font.createFont(Font.TRUETYPE_FONT, new File("Font/PixeloidSansBold.ttf")).deriveFont(30f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Font/PixeloidSans.ttf")));
		} catch (IOException | FontFormatException e) {}

		titleLabel = new JLabel("About Me");
		titleLabel.setBounds(200, -130, getWidth(), getHeight());
		titleLabel.setFont(pixelFontBold);
		titleLabel.setForeground(ruleTextColor);

		textLabel1 = new JLabel("     This is Java Game made by me, Gia Duong - Gr11 student from ECI, called \"The");
		textLabel1.setBounds(200, -70, getWidth(), getHeight());
		textLabel1.setFont(pixelFont);
		textLabel1.setForeground(ruleTextColor);

		textLabel2 = new JLabel("Jod Rex Game\". The original idea is an offline Dinosaur Game developed by Google.");
		textLabel2.setBounds(200, -20, getWidth(), getHeight());
		textLabel2.setFont(pixelFont);
		textLabel2.setForeground(ruleTextColor);

		textLabel3 = new JLabel("From the original game, I made some modifies and added some other features.");
		textLabel3.setBounds(200, 30, getWidth(), getHeight());
		textLabel3.setFont(pixelFont);
		textLabel3.setForeground(ruleTextColor);

		textLabel4 = new JLabel("     Now, Let's enjoy the Game, shall we?!");
		textLabel4.setBounds(200, 80, getWidth(), getHeight());
		textLabel4.setFont(pixelFont);
		textLabel4.setForeground(ruleTextColor);

		textLabel5 = new JLabel("     Contact me if you have any questions: dghao5906@gmail.com");
		textLabel5.setBounds(200, 130, getWidth(), getHeight());
		textLabel5.setFont(pixelFont);
		textLabel5.setForeground(ruleTextColor);

		creditLabel = new JLabel("[Made by: @giajolied]");
		creditLabel.setBounds(860, -180, getWidth(), getHeight());
		creditLabel.setFont(pixelFont);
		creditLabel.setForeground(Color.black);

		JLabel BGLabel = new JLabel();
    ImageIcon BGIcon = new ImageIcon("Image/Register_rule_resized.png");
    BGLabel.setIcon(BGIcon);
    BGLabel.setBounds(0, 0, BGIcon.getIconWidth(), BGIcon.getIconHeight());

		JLabel TitleLabel = new JLabel();
    ImageIcon LabelIcon = new ImageIcon("Image/Title_Text_Resized.png");
    TitleLabel.setIcon(LabelIcon);
    TitleLabel.setBounds(getWidth()/2-330, -70, LabelIcon.getIconWidth(), LabelIcon.getIconHeight());

		ImageIcon iconSign1 = new ImageIcon("Image/Button_GoBack.png");
		goBackButton = new JButton(iconSign1);
		goBackButton.setBounds(180, 541, 194, 97);
		goBackButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign2 = new ImageIcon("Image/Button_Quit.png");
		quitButton = new JButton(iconSign2);
		quitButton.setBounds(905, 541, 194, 97);
		quitButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign3 = new ImageIcon("Image/OnOffSound_Button_resized.png");
		onOffSoundButton = new JButton(iconSign3);
		onOffSoundButton.setBounds(50, 50, 60, 60);
		onOffSoundButton.addActionListener((ActionListener) this);

		ImageIcon iconSign4 = new ImageIcon("Image/Info_Button_resized.png");
		InfoButton = new JButton(iconSign4);
		InfoButton.setBounds(50, 140, 60, 60);
		InfoButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign6 = new ImageIcon("Image/RankButton_resized.png");
		RankButton = new JButton(iconSign6);
		RankButton.setBounds(50, 230, 60, 60);
		RankButton.addActionListener((ActionListener) this);  

		try {
			URL url = this.getClass().getClassLoader().getResource("Sound/Kevin_MacLeod_Light_Sting.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip(); // Assign the Clip instance to the variable
			clip.open(audioIn);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(5.0f);
			clip.start();
		} catch (UnsupportedAudioFileException err) {
			err.printStackTrace();
		} catch (IOException err) {
			err.printStackTrace();
		} catch (LineUnavailableException err) {
			err.printStackTrace();
		}

		panel.add(goBackButton);
		panel.add(quitButton);
		panel.add(onOffSoundButton);
		panel.add(InfoButton);
		panel.add(RankButton);
		panel.add(titleLabel);
		panel.add(textLabel1);
		panel.add(textLabel2);
		panel.add(textLabel3);
		panel.add(textLabel4);
		panel.add(textLabel5);
		panel.add(creditLabel);
		panel.add(TitleLabel);
		panel.add(TitleLabel);
		panel.add(BGLabel);
		add(panel);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == goBackButton) {
			clip.stop();
			new RegisterFrame();
			setVisible(false);
		} else if (e.getSource() == quitButton) {
			clip.stop();
			setVisible(false);
		} else if (e.getSource() == onOffSoundButton) {
			if (isMuted) {		// unmute
				clip.start();
				isMuted = false;
			} else {			// mute
				clip.stop();
				isMuted = true;
			}
		} else if (e.getSource() == RankButton) {
			clip.stop();
			new EverybodyScore_RegisterF();
			setVisible(false);
		} else if (e.getSource() == InfoButton) {
			clip.stop();
			new aboutMeFrame_RegisterF();
			setVisible(false);
		}
	}
}

class ScoreBoardFrame extends JFrame implements ActionListener {
	static JButton RePlayButton, signOutButton, onOffSoundButton, InfoButton, RankButton, HomeButton;

	Font pixelFont, pixelFontBold;
	JLabel textLabel1, textLabel2, textLabel3, textLabel4, textLabel5, 
	textLabel6, textLabel7, textLabel8, titleLabel1, titleLabel2, cheerLabel;

	Color ruleTextColor = new Color(249, 123, 34);

	private Clip clip;
	private boolean isMuted = false;

	private MainCharacter mainCharacter;

	public ScoreBoardFrame(String usrName, int Score) {
		super("The JoD Rex Game - Score Board Frame - By GiaD");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(RegisterFrame.screenWidth, RegisterFrame.screenHeight);

		JPanel panel = new JPanel();
		panel.setLayout(null);

		try {
			pixelFont = Font.createFont(Font.TRUETYPE_FONT, new File("Font/PixeloidSans.ttf")).deriveFont(20f);
			pixelFontBold = Font.createFont(Font.TRUETYPE_FONT, new File("Font/PixeloidSansBold.ttf")).deriveFont(30f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Font/PixeloidSans.ttf")));
		} catch (IOException | FontFormatException e) {}

		titleLabel1 = new JLabel("Your Result");
		titleLabel1.setBounds(250, -130, getWidth(), getHeight());
		titleLabel1.setFont(pixelFontBold);
		titleLabel1.setForeground(ruleTextColor);

		textLabel1 = new JLabel("UserName: " + usrName);
		textLabel1.setBounds(250, -70, getWidth(), getHeight());
		textLabel1.setFont(pixelFont);
		textLabel1.setForeground(ruleTextColor);

		textLabel2 = new JLabel("Current Score: " + Score);
		textLabel2.setBounds(250, -20, getWidth(), getHeight());
		textLabel2.setFont(pixelFont);
		textLabel2.setForeground(ruleTextColor);

		if (MainGamePanel.overAllHighestScore != 0) {
			textLabel3 = new JLabel("Highest Score: " + String.valueOf(MainGamePanel.overAllHighestScore));
			textLabel3.setBounds(250, 30, getWidth(), getHeight());
			textLabel3.setFont(pixelFont);
			textLabel3.setForeground(ruleTextColor);
		} else {
			textLabel3 = new JLabel("Highest Score: " + Score);
			textLabel3.setBounds(250, 30, getWidth(), getHeight());
			textLabel3.setFont(pixelFont);
			textLabel3.setForeground(ruleTextColor);
		}

		titleLabel2 = new JLabel("Top Players");
		titleLabel2.setBounds(650, -130, getWidth(), getHeight());
		titleLabel2.setFont(pixelFontBold);
		titleLabel2.setForeground(ruleTextColor);

		textLabel4 = new JLabel("*No1. " + RegisterFrame.usrNameArr[0] + ": " + RegisterFrame.bestScoreArr[0]);
		textLabel4.setBounds(650, -70, getWidth(), getHeight());
		textLabel4.setFont(pixelFont);
		textLabel4.setForeground(ruleTextColor);

		textLabel5 = new JLabel("*No2. " + RegisterFrame.usrNameArr[1] + ": " + RegisterFrame.bestScoreArr[1]);
		textLabel5.setBounds(650, -20, getWidth(), getHeight());
		textLabel5.setFont(pixelFont);
		textLabel5.setForeground(ruleTextColor);

		textLabel6 = new JLabel("*No3. " + RegisterFrame.usrNameArr[2] + ": " + RegisterFrame.bestScoreArr[2]);
		textLabel6.setBounds(650, 30, getWidth(), getHeight());
		textLabel6.setFont(pixelFont);
		textLabel6.setForeground(ruleTextColor);

		textLabel7 = new JLabel(" No4. " + RegisterFrame.usrNameArr[3] + ": " + RegisterFrame.bestScoreArr[3]);
		textLabel7.setBounds(650, 80, getWidth(), getHeight());
		textLabel7.setFont(pixelFont);
		textLabel7.setForeground(ruleTextColor);

		textLabel8 = new JLabel(" No5. " + RegisterFrame.usrNameArr[4] + ": " + RegisterFrame.bestScoreArr[4]);
		textLabel8.setBounds(650, 130, getWidth(), getHeight());
		textLabel8.setFont(pixelFont);
		textLabel8.setForeground(ruleTextColor);

		ImageIcon iconSign6 = new ImageIcon("Image/RankButton_resized.png");
		RankButton = new JButton(iconSign6);
		RankButton.setBounds(50, 230, 60, 60);
		RankButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign7 = new ImageIcon("Image/HomeButton_resized.png");
		HomeButton = new JButton(iconSign7);
		HomeButton.setBounds(50, 320, 60, 60);
		HomeButton.addActionListener((ActionListener) this); 

		JLabel BGLabel = new JLabel();
    ImageIcon BGIcon = new ImageIcon("Image/Register_rule_resized.png");
    BGLabel.setIcon(BGIcon);
    BGLabel.setBounds(0, 0, BGIcon.getIconWidth(), BGIcon.getIconHeight());

		JLabel TitleLabel = new JLabel();
    ImageIcon LabelIcon = new ImageIcon("Image/Title_Text_Resized.png");
    TitleLabel.setIcon(LabelIcon);
    TitleLabel.setBounds(getWidth()/2-330, -70, LabelIcon.getIconWidth(), LabelIcon.getIconHeight());

		ImageIcon iconSign1 = new ImageIcon("Image/Button_RePlay.png");
		RePlayButton = new JButton(iconSign1);
		RePlayButton.setBounds(180, 541, 194, 97);
		RePlayButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign2 = new ImageIcon("Image/Button_SignOut.png");
		signOutButton = new JButton(iconSign2);
		signOutButton.setBounds(905, 541, 194, 97);
		signOutButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign3 = new ImageIcon("Image/OnOffSound_Button_resized.png");
		onOffSoundButton = new JButton(iconSign3);
		onOffSoundButton.setBounds(50, 50, 60, 60);
		onOffSoundButton.addActionListener((ActionListener) this);

		ImageIcon iconSign4 = new ImageIcon("Image/Info_Button_resized.png");
		InfoButton = new JButton(iconSign4);
		InfoButton.setBounds(50, 140, 60, 60);
		InfoButton.addActionListener((ActionListener) this);

		try {
			URL url = this.getClass().getClassLoader().getResource("Sound/Kevin_MacLeod_Light_Sting.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip(); // Assign the Clip instance to the variable
			clip.open(audioIn);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(5.0f);
			clip.start();
		} catch (UnsupportedAudioFileException err) {
			err.printStackTrace();
		} catch (IOException err) {
			err.printStackTrace();
		} catch (LineUnavailableException err) {
			err.printStackTrace();
		}

		panel.add(RePlayButton);
		panel.add(signOutButton);
		panel.add(onOffSoundButton);
		panel.add(InfoButton);
		panel.add(RankButton);
		panel.add(HomeButton);
		panel.add(titleLabel1);
		panel.add(titleLabel2);
		panel.add(textLabel1);
		panel.add(textLabel2);
		panel.add(textLabel3);
		panel.add(textLabel4);
		panel.add(textLabel5);
		panel.add(textLabel6);
		panel.add(textLabel7);
		panel.add(textLabel8);
		panel.add(TitleLabel);
		panel.add(BGLabel);

		add(panel);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == RePlayButton) {
			clip.stop();
			MainGameFrame obj1 = new MainGameFrame();
			obj1.startGame();
			setVisible(false);
		} else if (e.getSource() == signOutButton) {
			clip.stop();
			new RegisterFrame();
			setVisible(false);
		} else if (e.getSource() == onOffSoundButton) {
			if (isMuted) {		// unmute
				clip.start();
				isMuted = false;
			} else {			// mute
				clip.stop();
				isMuted = true;
			}
		} else if (e.getSource() == InfoButton) {
			clip.stop();
			new aboutMeFrame();
			setVisible(false);
		} else if (e.getSource() == RankButton) {
			clip.stop();
			new EverybodyScore();
			setVisible(false);
		} else if (e.getSource() == HomeButton) {
			clip.stop();
			new IntroFrame();
			setVisible(false);
		}
	}
}

class EverybodyScore extends JFrame implements ActionListener {
	static JButton goBackButton, signOutButton, onOffSoundButton, InfoButton, RankButton, HomeButton;

	Font pixelFont, pixelFontBold;
	JLabel textLabel1, textLabel2, textLabel3, textLabel4, textLabel5, 
	textLabel6, textLabel7, textLabel8, textLabel9, textLabel10, titleLabel1, titleLabel2, creditLabel;

	Color ruleTextColor = new Color(249, 123, 34);

	private Clip clip;
	private boolean isMuted = false;

	private MainCharacter mainCharacter;

	public EverybodyScore() {
		super("The JoD Rex Game - Score Board Frame - By GiaD");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(RegisterFrame.screenWidth, RegisterFrame.screenHeight);

		JPanel panel = new JPanel();
		panel.setLayout(null);

		try {
			pixelFont = Font.createFont(Font.TRUETYPE_FONT, new File("Font/PixeloidSans.ttf")).deriveFont(20f);
			pixelFontBold = Font.createFont(Font.TRUETYPE_FONT, new File("Font/PixeloidSansBold.ttf")).deriveFont(30f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Font/PixeloidSans.ttf")));
		} catch (IOException | FontFormatException e) {}

		titleLabel1 = new JLabel("Top Players");
		titleLabel1.setBounds(350, -150, getWidth(), getHeight());
		titleLabel1.setFont(pixelFontBold);
		titleLabel1.setForeground(ruleTextColor);

		textLabel1 = new JLabel("*No1. " + RegisterFrame.usrNameArr[0] + ": " + RegisterFrame.bestScoreArr[0]);
		textLabel1.setBounds(350, -90, getWidth(), getHeight());
		textLabel1.setFont(pixelFont);
		textLabel1.setForeground(ruleTextColor);

		textLabel2 = new JLabel("*No2. " + RegisterFrame.usrNameArr[1] + ": " + RegisterFrame.bestScoreArr[1]);
		textLabel2.setBounds(350, -40, getWidth(), getHeight());
		textLabel2.setFont(pixelFont);
		textLabel2.setForeground(ruleTextColor);

		textLabel3 = new JLabel("*No3. " + RegisterFrame.usrNameArr[2] + ": " + RegisterFrame.bestScoreArr[2]);
		textLabel3.setBounds(350, 10, getWidth(), getHeight());
		textLabel3.setFont(pixelFont);
		textLabel3.setForeground(ruleTextColor);

		textLabel4 = new JLabel(" No4. " + RegisterFrame.usrNameArr[3] + ": " + RegisterFrame.bestScoreArr[3]);
		textLabel4.setBounds(350, 60, getWidth(), getHeight());
		textLabel4.setFont(pixelFont);
		textLabel4.setForeground(ruleTextColor);

		textLabel5 = new JLabel(" No5. " + RegisterFrame.usrNameArr[4] + ": " + RegisterFrame.bestScoreArr[4]);
		textLabel5.setBounds(350, 110, getWidth(), getHeight());
		textLabel5.setFont(pixelFont);
		textLabel5.setForeground(ruleTextColor);

		titleLabel2 = new JLabel("Runners Up");
		titleLabel2.setBounds(750, -150, getWidth(), getHeight());
		titleLabel2.setFont(pixelFontBold);
		titleLabel2.setForeground(ruleTextColor);

		textLabel6 = new JLabel("No6. " + RegisterFrame.usrNameArr[5] + ": " + RegisterFrame.bestScoreArr[5]);
		textLabel6.setBounds(750, -90, getWidth(), getHeight());
		textLabel6.setFont(pixelFont);
		textLabel6.setForeground(ruleTextColor);

		textLabel7 = new JLabel("No7. " + RegisterFrame.usrNameArr[6] + ": " + RegisterFrame.bestScoreArr[6]);
		textLabel7.setBounds(750, -40, getWidth(), getHeight());
		textLabel7.setFont(pixelFont);
		textLabel7.setForeground(ruleTextColor);

		textLabel8 = new JLabel("No8. " + RegisterFrame.usrNameArr[7] + ": " + RegisterFrame.bestScoreArr[7]);
		textLabel8.setBounds(750, 10, getWidth(), getHeight());
		textLabel8.setFont(pixelFont);
		textLabel8.setForeground(ruleTextColor);

		textLabel9 = new JLabel("No9. " + RegisterFrame.usrNameArr[8] + ": " + RegisterFrame.bestScoreArr[8]);
		textLabel9.setBounds(750, 60, getWidth(), getHeight());
		textLabel9.setFont(pixelFont);
		textLabel9.setForeground(ruleTextColor);

		textLabel10 = new JLabel("No10. " + RegisterFrame.usrNameArr[9] + ": " + RegisterFrame.bestScoreArr[9]);
		textLabel10.setBounds(750, 110, getWidth(), getHeight());
		textLabel10.setFont(pixelFont);
		textLabel10.setForeground(ruleTextColor);

		ImageIcon iconSign6 = new ImageIcon("Image/RankButton_resized.png");
		RankButton = new JButton(iconSign6);
		RankButton.setBounds(50, 230, 60, 60);
		RankButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign7 = new ImageIcon("Image/HomeButton_resized.png");
		HomeButton = new JButton(iconSign7);
		HomeButton.setBounds(50, 320, 60, 60);
		HomeButton.addActionListener((ActionListener) this); 

		JLabel BGLabel = new JLabel();
    ImageIcon BGIcon = new ImageIcon("Image/Register_rule_resized.png");
    BGLabel.setIcon(BGIcon);
    BGLabel.setBounds(0, 0, BGIcon.getIconWidth(), BGIcon.getIconHeight());

		JLabel TitleLabel = new JLabel();
    ImageIcon LabelIcon = new ImageIcon("Image/Title_Text_Resized.png");
    TitleLabel.setIcon(LabelIcon);
    TitleLabel.setBounds(getWidth()/2-330, -70, LabelIcon.getIconWidth(), LabelIcon.getIconHeight());

		ImageIcon iconSign1 = new ImageIcon("Image/Button_GoBack.png");
		goBackButton = new JButton(iconSign1);
		goBackButton.setBounds(180, 541, 194, 97);
		goBackButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign2 = new ImageIcon("Image/Button_SignOut.png");
		signOutButton = new JButton(iconSign2);
		signOutButton.setBounds(905, 541, 194, 97);
		signOutButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign3 = new ImageIcon("Image/OnOffSound_Button_resized.png");
		onOffSoundButton = new JButton(iconSign3);
		onOffSoundButton.setBounds(50, 50, 60, 60);
		onOffSoundButton.addActionListener((ActionListener) this);

		ImageIcon iconSign4 = new ImageIcon("Image/Info_Button_resized.png");
		InfoButton = new JButton(iconSign4);
		InfoButton.setBounds(50, 140, 60, 60);
		InfoButton.addActionListener((ActionListener) this);

		try {
			URL url = this.getClass().getClassLoader().getResource("Sound/Kevin_MacLeod_Light_Sting.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip(); // Assign the Clip instance to the variable
			clip.open(audioIn);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(5.0f);
			clip.start();
		} catch (UnsupportedAudioFileException err) {
			err.printStackTrace();
		} catch (IOException err) {
			err.printStackTrace();
		} catch (LineUnavailableException err) {
			err.printStackTrace();
		}

		panel.add(goBackButton);
		panel.add(signOutButton);
		panel.add(onOffSoundButton);
		panel.add(InfoButton);
		panel.add(RankButton);
		panel.add(HomeButton);
		panel.add(titleLabel1);
		panel.add(titleLabel2);
		panel.add(textLabel1);
		panel.add(textLabel2);
		panel.add(textLabel3);
		panel.add(textLabel4);
		panel.add(textLabel5);
		panel.add(textLabel6);
		panel.add(textLabel7);
		panel.add(textLabel8);
		panel.add(textLabel9);
		panel.add(textLabel10);
		panel.add(TitleLabel);
		panel.add(BGLabel);

		add(panel);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == goBackButton) {
			clip.stop();
			new IntroFrame();
			setVisible(false);
		} else if (e.getSource() == signOutButton) {
			clip.stop();
			new RegisterFrame();
			setVisible(false);
		} else if (e.getSource() == onOffSoundButton) {
			if (isMuted) {		// unmute
				clip.start();
				isMuted = false;
			} else {			// mute
				clip.stop();
				isMuted = true;
			}
		} else if (e.getSource() == InfoButton) {
			clip.stop();
			new aboutMeFrame();
			setVisible(false);
		} else if (e.getSource() == RankButton) {
			clip.stop();
			new EverybodyScore();
			setVisible(false);
		} else if (e.getSource() == HomeButton) {
			clip.stop();
			new IntroFrame();
			setVisible(false);
		}
	}
}

class EverybodyScore_RegisterF extends JFrame implements ActionListener {
	static JButton goBackButton, quitButton, onOffSoundButton, InfoButton, RankButton, HomeButton;

	Font pixelFont, pixelFontBold;
	JLabel textLabel1, textLabel2, textLabel3, textLabel4, textLabel5, 
	textLabel6, textLabel7, textLabel8, textLabel9, textLabel10, titleLabel1, titleLabel2, creditLabel;

	Color ruleTextColor = new Color(249, 123, 34);

	private Clip clip;
	private boolean isMuted = false;

	private MainCharacter mainCharacter;

	public EverybodyScore_RegisterF() {
		super("The JoD Rex Game - Score Board Frame - By GiaD");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(RegisterFrame.screenWidth, RegisterFrame.screenHeight);

		JPanel panel = new JPanel();
		panel.setLayout(null);

		try {
			pixelFont = Font.createFont(Font.TRUETYPE_FONT, new File("Font/PixeloidSans.ttf")).deriveFont(20f);
			pixelFontBold = Font.createFont(Font.TRUETYPE_FONT, new File("Font/PixeloidSansBold.ttf")).deriveFont(30f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Font/PixeloidSans.ttf")));
		} catch (IOException | FontFormatException e) {}

		titleLabel1 = new JLabel("Top Players");
		titleLabel1.setBounds(350, -150, getWidth(), getHeight());
		titleLabel1.setFont(pixelFontBold);
		titleLabel1.setForeground(ruleTextColor);

		textLabel1 = new JLabel("*No1. " + RegisterFrame.usrNameArr[0] + ": " + RegisterFrame.bestScoreArr[0]);
		textLabel1.setBounds(350, -90, getWidth(), getHeight());
		textLabel1.setFont(pixelFont);
		textLabel1.setForeground(ruleTextColor);

		textLabel2 = new JLabel("*No2. " + RegisterFrame.usrNameArr[1] + ": " + RegisterFrame.bestScoreArr[1]);
		textLabel2.setBounds(350, -40, getWidth(), getHeight());
		textLabel2.setFont(pixelFont);
		textLabel2.setForeground(ruleTextColor);

		textLabel3 = new JLabel("*No3. " + RegisterFrame.usrNameArr[2] + ": " + RegisterFrame.bestScoreArr[2]);
		textLabel3.setBounds(350, 10, getWidth(), getHeight());
		textLabel3.setFont(pixelFont);
		textLabel3.setForeground(ruleTextColor);

		textLabel4 = new JLabel(" No4. " + RegisterFrame.usrNameArr[3] + ": " + RegisterFrame.bestScoreArr[3]);
		textLabel4.setBounds(350, 60, getWidth(), getHeight());
		textLabel4.setFont(pixelFont);
		textLabel4.setForeground(ruleTextColor);

		textLabel5 = new JLabel(" No5. " + RegisterFrame.usrNameArr[4] + ": " + RegisterFrame.bestScoreArr[4]);
		textLabel5.setBounds(350, 110, getWidth(), getHeight());
		textLabel5.setFont(pixelFont);
		textLabel5.setForeground(ruleTextColor);

		titleLabel2 = new JLabel("Runners Up");
		titleLabel2.setBounds(750, -150, getWidth(), getHeight());
		titleLabel2.setFont(pixelFontBold);
		titleLabel2.setForeground(ruleTextColor);

		textLabel6 = new JLabel("No6. " + RegisterFrame.usrNameArr[5] + ": " + RegisterFrame.bestScoreArr[5]);
		textLabel6.setBounds(750, -90, getWidth(), getHeight());
		textLabel6.setFont(pixelFont);
		textLabel6.setForeground(ruleTextColor);

		textLabel7 = new JLabel("No7. " + RegisterFrame.usrNameArr[6] + ": " + RegisterFrame.bestScoreArr[6]);
		textLabel7.setBounds(750, -40, getWidth(), getHeight());
		textLabel7.setFont(pixelFont);
		textLabel7.setForeground(ruleTextColor);

		textLabel8 = new JLabel("No8. " + RegisterFrame.usrNameArr[7] + ": " + RegisterFrame.bestScoreArr[7]);
		textLabel8.setBounds(750, 10, getWidth(), getHeight());
		textLabel8.setFont(pixelFont);
		textLabel8.setForeground(ruleTextColor);

		textLabel9 = new JLabel("No9. " + RegisterFrame.usrNameArr[8] + ": " + RegisterFrame.bestScoreArr[8]);
		textLabel9.setBounds(750, 60, getWidth(), getHeight());
		textLabel9.setFont(pixelFont);
		textLabel9.setForeground(ruleTextColor);

		textLabel10 = new JLabel("No10. " + RegisterFrame.usrNameArr[9] + ": " + RegisterFrame.bestScoreArr[9]);
		textLabel10.setBounds(750, 110, getWidth(), getHeight());
		textLabel10.setFont(pixelFont);
		textLabel10.setForeground(ruleTextColor);

		ImageIcon iconSign6 = new ImageIcon("Image/RankButton_resized.png");
		RankButton = new JButton(iconSign6);
		RankButton.setBounds(50, 230, 60, 60);
		RankButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign7 = new ImageIcon("Image/HomeButton_resized.png");
		HomeButton = new JButton(iconSign7);
		HomeButton.setBounds(50, 320, 60, 60);
		HomeButton.addActionListener((ActionListener) this); 

		JLabel BGLabel = new JLabel();
    ImageIcon BGIcon = new ImageIcon("Image/Register_rule_resized.png");
    BGLabel.setIcon(BGIcon);
    BGLabel.setBounds(0, 0, BGIcon.getIconWidth(), BGIcon.getIconHeight());

		JLabel TitleLabel = new JLabel();
    ImageIcon LabelIcon = new ImageIcon("Image/Title_Text_Resized.png");
    TitleLabel.setIcon(LabelIcon);
    TitleLabel.setBounds(getWidth()/2-330, -70, LabelIcon.getIconWidth(), LabelIcon.getIconHeight());

		
		ImageIcon iconSign1 = new ImageIcon("Image/Button_GoBack.png");
		goBackButton = new JButton(iconSign1);
		goBackButton.setBounds(180, 541, 194, 97);
		goBackButton.addActionListener((ActionListener) this); 

		
		ImageIcon iconSign2 = new ImageIcon("Image/Button_Quit.png");
		quitButton = new JButton(iconSign2);
		quitButton.setBounds(905, 541, 194, 97);
		quitButton.addActionListener((ActionListener) this); 

		ImageIcon iconSign3 = new ImageIcon("Image/OnOffSound_Button_resized.png");
		onOffSoundButton = new JButton(iconSign3);
		onOffSoundButton.setBounds(50, 50, 60, 60);
		onOffSoundButton.addActionListener((ActionListener) this);

		ImageIcon iconSign4 = new ImageIcon("Image/Info_Button_resized.png");
		InfoButton = new JButton(iconSign4);
		InfoButton.setBounds(50, 140, 60, 60);
		InfoButton.addActionListener((ActionListener) this);

		try {
			URL url = this.getClass().getClassLoader().getResource("Sound/Kevin_MacLeod_Light_Sting.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip(); // Assign the Clip instance to the variable
			clip.open(audioIn);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(5.0f);
			clip.start();
		} catch (UnsupportedAudioFileException err) {
			err.printStackTrace();
		} catch (IOException err) {
			err.printStackTrace();
		} catch (LineUnavailableException err) {
			err.printStackTrace();
		}

		panel.add(goBackButton);
		panel.add(quitButton);
		panel.add(onOffSoundButton);
		panel.add(InfoButton);
		panel.add(RankButton);
		panel.add(RankButton);
		panel.add(titleLabel1);
		panel.add(titleLabel2);
		panel.add(textLabel1);
		panel.add(textLabel2);
		panel.add(textLabel3);
		panel.add(textLabel4);
		panel.add(textLabel5);
		panel.add(textLabel6);
		panel.add(textLabel7);
		panel.add(textLabel8);
		panel.add(textLabel9);
		panel.add(textLabel10);
		panel.add(TitleLabel);
		panel.add(BGLabel);

		add(panel);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == goBackButton) {
			clip.stop();
			new RegisterFrame();
			setVisible(false);
		} else if (e.getSource() == quitButton) {
			clip.stop();
			setVisible(false);
		} else if (e.getSource() == onOffSoundButton) {
			if (isMuted) {		// unmute
				clip.start();
				isMuted = false;
			} else {			// mute
				clip.stop();
				isMuted = true;
			}
		} else if (e.getSource() == InfoButton) {
			clip.stop();
			new aboutMeFrame_RegisterF();
			setVisible(false);
		} else if (e.getSource() == RankButton) {
			clip.stop();
			new EverybodyScore_RegisterF();
			setVisible(false);
		}
	}
}

class MainGameFrame extends JFrame{
	public static final int screenWidth = 1280;    // 1280
	public static final int screenHeight = 768;   // 768
	private MainGamePanel gameScreen;
	
	public MainGameFrame() {
		super("GiaD - The JoD Rex Game - Main Game Frame - By GiaD");
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		pack();
		setLocation(0, 0);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo((Component)null);
		gameScreen = new MainGamePanel();
		addKeyListener(gameScreen);
		add(gameScreen);
	}
	
	public void startGame() {
		setVisible(true);
		gameScreen.startGame();
	}
	
	public static void main(String args[]) {
		(new MainGameFrame()).startGame();
	}
}

class MainGamePanel extends JPanel implements Runnable, KeyListener {
	public static MainGamePanel gamePanel;

	private static final int START_GAME_STATE = 0;
	private static final int GAME_PLAYING_STATE = 1;
	private static final int GAME_OVER_STATE = 2;
	private int gameState = START_GAME_STATE;
	
	private Land1 land1;
	private Land2 land2;
	private Land3 land3;
	private Land4 land4;
	private Land5 land5;
	private MainCharacter mainCharacter;
	private EnemiesManager enemiesManager;
	private Thread thread;

	private boolean isKeyPressed;
	Image GameOverImg,GameOverTextImg;
	public int tileSize;

	public static ArrayList<Integer> sortedScoreList = new ArrayList<>();
	public static ArrayList<String> userList = new ArrayList<>();
	public static ArrayList<String> highestScoreList = new ArrayList<>();
	public static boolean swapped;
	private int line;
	Clip clip;

	static int highestScore;
	static int overAllHighestScore;

	public MainGamePanel() {
		mainCharacter = new MainCharacter();
		land1 = new Land1(MainGameFrame.screenWidth, mainCharacter);
		land2 = new Land2(MainGameFrame.screenWidth, mainCharacter);
		land3 = new Land3(MainGameFrame.screenWidth, mainCharacter);
		land4 = new Land4(MainGameFrame.screenWidth, mainCharacter);
		land5 = new Land5(MainGameFrame.screenWidth, mainCharacter);
		mainCharacter.setSpeedX(4);
		enemiesManager = new EnemiesManager(mainCharacter);

		ImageIcon iconSign1 = new ImageIcon("Image/GameOver_resized.png");
		GameOverImg = iconSign1.getImage();

		ImageIcon iconSign2 = new ImageIcon("Image/GameOver_Continue_resized.png");
		GameOverTextImg = iconSign2.getImage();		

		try {
			URL url = this.getClass().getClassLoader().getResource("Sound/Jeremy_Blake_Powerup.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip(); // Assign the Clip instance to the variable
			clip.open(audioIn);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(5.0f);
			
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException err) {
			err.printStackTrace();
		} catch (IOException err) {
			err.printStackTrace();
		} catch (LineUnavailableException err) {
			err.printStackTrace();
		}
	}

	public void startGame() {		// using multiple threads, program can perform multiple tasks concurrently
		thread = new Thread(this);
		thread.start();
	}

	public void gameUpdate() {
		if (gameState == START_GAME_STATE) {
			mainCharacter.update();
		}
		if (gameState == GAME_PLAYING_STATE) {
			land1.update();
			land2.update();
			land3.update();
			land4.update();
			land5.update();
			mainCharacter.update();
			enemiesManager.update();
			//clip.start();
			if (enemiesManager.isCollision()) {
				//mainCharacter.playDeadSound();

				sortedScoreList.add(mainCharacter.score);		// add scores to List
				int[] highestScoreArr = new int[sortedScoreList.size()];		// convert list to array
				for (int index=0; index<sortedScoreList.size(); index++) {
					highestScoreArr[index] = sortedScoreList.get(index);
				}

				for (int index=0; index<(highestScoreArr.length-1); index++) {		// bubble sort
					swapped = false;
					for (int num=0; num<(highestScoreArr.length-index-1); num++) {
						if (highestScoreArr[num] > highestScoreArr[num+1]) {
							int temp = highestScoreArr[num];
							highestScoreArr[num] = highestScoreArr[num+1];
							highestScoreArr[num+1] = temp;
							swapped = true;
						}
					}
					if (swapped == false) {
						break;
					}
				}

				// highest individual score
				highestScore = highestScoreArr[highestScoreArr.length-1];
				
				try {		// check if file is created and is saft
					FileReader myReader = new FileReader("UserData/ScoreBoard.txt");
					System.out.println("file exists!");
				} catch (FileNotFoundException ex) {
					try {
						FileWriter myWrite = new FileWriter("UserData/ScoreBoard.txt");
						System.out.println("File created");
					} catch (IOException ex1) {
						Logger.getLogger(RegisterFrame.class.getName()).log(Level.SEVERE, null, ex1);
					}
				}

				StringBuilder fileContent = new StringBuilder();
				Boolean userExists = false;
				try (BufferedReader myReader = new BufferedReader(new FileReader("UserData/ScoreBoard.txt"))) {
					String lineText;
					while ((lineText = myReader.readLine()) != null) {		// compare user name to check their score
						fileContent.append(lineText).append("\n");			// if user name is already exists, compare the current highest score
						if (lineText.equals(RegisterFrame.Username)) {			// vs the existed in file highest score
							userExists = true;
							String existedScoreStr = myReader.readLine();
							int existedScoreInt = Integer.parseInt(existedScoreStr);
							if (highestScore > existedScoreInt) {		
								overAllHighestScore = highestScore;						
								fileContent.append(highestScore).append("\n");
							} else {
								overAllHighestScore = existedScoreInt;
								fileContent.append(existedScoreInt).append("\n");
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (!userExists) {		// add new user if user not existed
					fileContent.append(RegisterFrame.Username).append("\n");
					fileContent.append(highestScore).append("\n\n");
				}

				try (BufferedWriter myWriter = new BufferedWriter(new FileWriter("UserData/ScoreBoard.txt"))) {	//write back to file
					myWriter.write(fileContent.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				gameState = GAME_OVER_STATE;
				mainCharacter.dead(true);
				clip.stop();
			}
		}
	}

	public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(Color.decode("#f7f7f7"));
    g.fillRect(0, 0, getWidth(), getHeight());

    Graphics2D g2 = (Graphics2D) g;

    switch (gameState) {
      case START_GAME_STATE:

				ImageIcon iconBG = new ImageIcon("Image/beginning_game_state.png");
				Image BG = iconBG.getImage(); 
				g.drawImage(BG, 0, -50, this);

				ImageIcon iconTitle = new ImageIcon("Image/ready.png");
				Image Title = iconTitle.getImage();
				g.drawImage(Title, getWidth()/2-195, 100, this);

				ImageIcon iconText = new ImageIcon("Image/start.png");
				Image Text = iconText.getImage();
				g.drawImage(Text, getWidth()/2-325, 300, this);

				mainCharacter.draw(g2);

        break;
			case GAME_PLAYING_STATE:
			case GAME_OVER_STATE:
				land1.draw(g);
				land2.draw(g);
				land3.draw(g);
				land4.draw(g);
				land5.draw(g);
				enemiesManager.draw(g);
				mainCharacter.draw(g);
				g.setColor(Color.BLACK);
				g.drawString("Name: " + RegisterFrame.Username, 500, 20);
				g.drawString("Score: " + mainCharacter.score, 500, 50);
				if (gameState == GAME_OVER_STATE) {
					g.drawString("Name: " + RegisterFrame.Username, 500, 20);
					g.drawString("Score: " + mainCharacter.score, 500, 50);
					clip.stop();
					g.drawImage(GameOverImg, getWidth()/2-195, 100, null);
					g.drawImage(GameOverTextImg, getWidth()/2-325, 300, null);
        }
        break;
    	}
    g2.dispose(); // Dispose g2 here, after using it
	}

	//@Override
	public void run() {		// manager System nano time (computer run time) for animaiton between frames
		int fps = 100;
		long msPerFrame = 1000 * 1000000 / fps;
		long lastTime = 0;
		long elapsed;
		int msSleep;
		int nanoSleep;
		long endProcessGame;
		//long lag = 0;

		while (true) {
			gameUpdate();
			repaint();
			endProcessGame = System.nanoTime();
			elapsed = (lastTime + msPerFrame - System.nanoTime());
			msSleep = (int) (elapsed / 1000000);
			nanoSleep = (int) (elapsed % 1000000);
			if (msSleep <= 0) {
				lastTime = System.nanoTime();
				continue;
			}
			try {
				Thread.sleep(msSleep, nanoSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lastTime = System.nanoTime();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (gameState) {
			case START_GAME_STATE:
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					gameState = GAME_PLAYING_STATE;
				}
				break;
			case GAME_PLAYING_STATE:
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					mainCharacter.jump();
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					mainCharacter.down(true);
				}
				break;
			case GAME_OVER_STATE:
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					//mainCharacter.score = 0;
					//gameState = GAME_PLAYING_STATE;
					//resetGame();
					//clip.stop();
					new ScoreBoardFrame(RegisterFrame.Username, mainCharacter.score);
					setVisible(false);
				} 
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		isKeyPressed = false;
		if (gameState == GAME_PLAYING_STATE) {
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				mainCharacter.down(false);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	private void resetGame() {
		enemiesManager.reset();
		mainCharacter.dead(false);
		mainCharacter.reset();
	}
}

// -------- ANIMATION CLASS --------
// Animating Frames: Create an animation by adding frames and automatically cycling through them based on the specified time interval.
class Animation {
	private BufferedImage[] array;
	private long deltaTime;			// Time (in milliseconds) between frames
	private int currentFrame = 0;	// Index of the current frame in the array
	private long previousTime;		// Previous time at which the frame was updated

	public Animation(int deltaTime) {
		this.deltaTime = deltaTime;
		array = new BufferedImage[0];
		previousTime = 0;
	}

	public void updateFrame() {
		if (System.currentTimeMillis() - previousTime >= deltaTime) {		// Check elapsed time since the previous frame update is greater than or equal to deltaTime
			//System.out.println(System.currentTimeMillis() - previousTime);
			currentFrame++;
			//System.out.println(currentFrame);
			if (currentFrame >= array.length) {		// If currentFrame is beyond the length of array
				currentFrame = 0;										// it wraps back to 0
			}
			previousTime = System.currentTimeMillis();		// Updates the previousTime to the current time
		}
	}

	public void addFrame(BufferedImage image) {
		BufferedImage[] newArray = new BufferedImage[array.length + 1];
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[array.length] = image;
		array = newArray;
	}

	public BufferedImage getFrame() {
		return array[currentFrame];		// Returns the current frame (BufferedImage) based on currentFrame index
	}
}

// -------- GETIMGTOOL CLASS --------
// Shortcut to get images for animation by path directory all at one
class getImageTool {
	public static BufferedImage getImage(String path) {
		BufferedImage BG = null;
		try {
		    BG = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return BG;
	}
}

// -------- ENEMY CLASS --------
// In charge of containing enemiesArr
class Enemy {
	public void update() {}
	public void draw(Graphics g) {}
	public Rectangle getBound() {
		return null;
	}
	public boolean isOutOfScreen() {
		return false;
	}
}

// -------- MAINCHARACTER CLASS --------
// represents main character, handling its movement, animations, collisions, and scoring
class MainCharacter {
	public static final int LAND_POSY = 440;		// 80
	public static final float GRAVITY = 0.4f;
	
	private static final int NORMAL_RUN = 0;
	private static final int JUMPING = 1;
	private static final int DOWN_RUN = 2;
	private static final int DEATH = 3;
	private int state = NORMAL_RUN;
	
	private float posY;
	private float posX;
	private float speedX;
	private float speedY;
	private Rectangle rectBound;
	
	public int score = 0;
	
	private Animation normalRunAnim;
	private BufferedImage jumping;
	private Animation downRunAnim;
	private BufferedImage deathImage;

	MainGamePanel gamePanelObj;
	
	public MainCharacter() {
		posX = 50;
		posY = LAND_POSY;		
		rectBound = new Rectangle();
		normalRunAnim = new Animation(90);
		normalRunAnim.addFrame(getImageTool.getImage("Image/Fynn_resized_run_1.png"));		// Add frame to obj
		normalRunAnim.addFrame(getImageTool.getImage("Image/Fynn_resized_run_2.png"));		
		normalRunAnim.addFrame(getImageTool.getImage("Image/Fynn_resized_run_3.png"));		
		normalRunAnim.addFrame(getImageTool.getImage("Image/Fynn_resized_run_4.png"));		
		normalRunAnim.addFrame(getImageTool.getImage("Image/Fynn_resized_run_5.png"));
		jumping = getImageTool.getImage("Image/Fynn_resized_jump.png");					// Load an BG to jumpImg variable
		downRunAnim = new Animation(90);
		downRunAnim.addFrame(getImageTool.getImage("Image/Fynn_resized_headDown.png"));
		deathImage = getImageTool.getImage("Image/Fynn_resized_dead.png");
	}

	public float getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;		// sets the value of the speedX variable to the provided value above
	}
	
	public void draw(Graphics g) {
		switch(state) {
			case NORMAL_RUN:				
				g.drawImage(normalRunAnim.getFrame(), (int) posX, (int) posY, null);
				break;
			case JUMPING:
				g.drawImage(jumping, (int) posX, (int) posY, null);
				break;
			case DOWN_RUN:				
				g.drawImage(downRunAnim.getFrame(), (int) posX, (int) (posY + 20), null);
				break;
			case DEATH:
				g.drawImage(deathImage, (int) posX, (int) posY, null);
				break;
		}
	}
	
	public void update() {		// updates the state and position of the character
		normalRunAnim.updateFrame();
		downRunAnim.updateFrame();
		if(posY >= LAND_POSY) {		// checks if the character is on the ground
			posY = LAND_POSY;		// sets the position posY to the ground position
			if(state != DOWN_RUN) {		// If character not in the crouching state 
				state = NORMAL_RUN;		// sets the state to normal run
			}
		} else {
			speedY += GRAVITY;		// applies gravity to the vertical speed (speedY)
			posY += speedY;			// updates the position accordingly
		}
	}
	
	public void jump() {
		if(posY >= LAND_POSY) {
			/*if(jumpSound != null) {
				jumpSound.play();
			}*/
			speedY = -12f;		// sets the vertical speed (speedY) to a negative value
			posY += speedY;		// updates position
			state = JUMPING;
		}
	}
	
	public void down(boolean isDown) {
		if(state == JUMPING) {
			return;		// returns without making any changes
		}
		if(isDown) {
			state = DOWN_RUN;
		} else {
			state = NORMAL_RUN;
		}
	}
	
	public Rectangle getBound() {		// returns rectangle object representing bounding rectangle of the character
		rectBound = new Rectangle();
		if(state == DOWN_RUN) {
			rectBound.x = (int) posX + 5;
			rectBound.y = (int) posY + 20;
			rectBound.width = downRunAnim.getFrame().getWidth() - 40;		// smaller bounding area for the crouching state
			rectBound.height = downRunAnim.getFrame().getHeight();			// making the collision detection more precise
		} else {
			rectBound.x = (int) posX + 5;
			rectBound.y = (int) posY;
			rectBound.width = normalRunAnim.getFrame().getWidth() - 40;
			rectBound.height = normalRunAnim.getFrame().getHeight();
		}
		return rectBound;
	}
	
	public void dead(boolean isDeath) {
		if(isDeath) {
			state = DEATH;
		} else {
			state = NORMAL_RUN;
		}
	}
	
	public void reset() {		// resets the character's position (posY) to the ground when dead
		posY = LAND_POSY;
	}
	
	/*public void playDeadSound() {
		deadSound.play();
	}*/
	
	public void upScore() {
		score += 20;
	}
}

// -------- ENEMY_MANAGER CLASS --------
// Manage a collection of enemy objects including creation, updating, drawing, collision detection, and resetting
class EnemiesManager {
	private BufferedImage enemy1, enemy2, enemy3, enemy4, enemy5, enemy6, enemy7, enemy8;
	private Random rand;

	private Enemy[] enemiesArr;
	private MainCharacter mainCharacter;
	private int maxEnemies = 1;

	public EnemiesManager(MainCharacter mainCharacter) {
		rand = new Random();
		enemy1 = getImageTool.getImage("Image/Enemy_resized_Armored.png");
		enemy2 = getImageTool.getImage("Image/Enemy_resized_ChessPuff.png");
		enemy3 = getImageTool.getImage("Image/Enemy_resized_Grab.png");
		enemy4 = getImageTool.getImage("Image/Enemy_resized_Mochi.png");
		enemy5 = getImageTool.getImage("Image/Enemy_resized_Octi.png");
		enemy6 = getImageTool.getImage("Image/Enemy_resized_Pumpkin.png");
		enemy7 = getImageTool.getImage("Image/Enemy_resized_Tank.png");
		enemy8 = getImageTool.getImage("Image/Enemy_resized_Totem.png");
		enemiesArr = new Enemy[maxEnemies];
		this.mainCharacter = mainCharacter;
		enemiesArr[0] = createEnemy();
	}

	public void update() {
		for (int index = 0; index < enemiesArr.length; index++) {
			enemiesArr[index].update();
		}
		Enemy enemy = enemiesArr[0];
		if (enemy.isOutOfScreen()) {
			mainCharacter.upScore();
			reset();
		}
	}

	public void draw(Graphics g) {
		for (int index = 0; index < enemiesArr.length; index++) {
			enemiesArr[index].draw(g);
		}
	}

	private Enemy createEnemy() {	// creating a new enemy object based on a random selection
		int type = rand.nextInt(7);
		if (type == 0) {
			return new Monsters(mainCharacter, 1250, enemy1.getWidth() - 20, enemy1.getHeight() - 40, enemy1);
		} else if (type==1) {
			return new Monsters(mainCharacter, 1250, enemy2.getWidth() - 20, enemy2.getHeight() - 40, enemy2);
		} else if (type==2) {
			return new Monsters(mainCharacter, 1250, enemy3.getWidth() - 20, enemy3.getHeight() - 40, enemy3);
		} else if (type==3) {
			return new Monsters(mainCharacter, 1250, enemy4.getWidth() - 20, enemy4.getHeight() - 40, enemy4);
		} else if (type==4) {
			return new Monsters(mainCharacter, 1250, enemy5.getWidth() - 20, enemy5.getHeight() - 40, enemy5);
		} else if (type==5) {
			return new Monsters(mainCharacter, 1250, enemy6.getWidth() - 20, enemy6.getHeight() - 40, enemy6);
		} else if (type==6) {
			return new Monsters(mainCharacter, 1250, enemy7.getWidth() - 20, enemy7.getHeight() - 40, enemy7);
		} else {
			return new Monsters(mainCharacter, 1250, enemy8.getWidth() - 20, enemy8.getHeight() - 40, enemy8);
		}
	}

	public boolean isCollision() {		// checks collision between the main character and any of the enemy objects
		for (int index = 0; index < enemiesArr.length; index++) {
			if (mainCharacter.getBound().intersects(enemiesArr[index].getBound())) {
				return true;
			}
		}
		return false;
	}

	public void reset() {
		enemiesArr[0] = createEnemy();
	}
}

// -------- MONSTERS CLASS --------
class Monsters extends Enemy {
	public static final int Y_LAND = 560;	

	private int posX;
	private int width;
	private int height;

	private BufferedImage image;
	private MainCharacter mainCharacter;

	private Rectangle rectBound;

	public Monsters(MainCharacter mainCharacter, int posX, int width, int height, BufferedImage image) {
		this.posX = posX;
		this.width = width;
		this.height = height;
		this.image = image;
		this.mainCharacter = mainCharacter;
		rectBound = new Rectangle();
	}

	public void update() {
		posX -= 4.2;		// mainCharacter.getSpeedX()
	}

	public void draw(Graphics g) {
		g.drawImage(image, posX, Y_LAND - image.getHeight(), null);
		g.setColor(Color.red);
	}

	public Rectangle getBound() {
		rectBound = new Rectangle();
		rectBound.x = (int) (posX + (image.getWidth() - width)/50);
		rectBound.y =(int) (Y_LAND - image.getHeight() + (image.getHeight() - height)/0.8);
		rectBound.width = width;
		rectBound.height = height;
		return rectBound;
	}

	@Override
	public boolean isOutOfScreen() {
		if(posX < -image.getWidth()) {
			return true;
		}
		return false;
	}
}

// -------- LAND CLASS --------
// Containing main land1 layers, in charge of copy land1 BG and create new ones when out of screen
class Land1 {
	public static final int LAND_POSY = -80;

	private ImageLand[] landArr;
	private BufferedImage land1;
	private BufferedImage land2; 
	private MainCharacter mainCharacter;

	public Land1(int width, MainCharacter mainCharacter) {
		this.mainCharacter = mainCharacter;
		land1 = getImageTool.getImage("Image/Hills_Layer_01_resized.png");
		land2 = getImageTool.getImage("Image/Hills_Layer_01_resized.png");
		int numberOfImageLand = width / land1.getWidth() + 2;        // adds 2 to ensure there are enough images to cover the screen
		landArr = new ImageLand[numberOfImageLand];

		for (int index = 0; index < numberOfImageLand; index++) {
			ImageLand imageLand = new ImageLand();        // creates a new instance of nested imgLandPos class, assigns it to imgLand variable
			imageLand.posX = index * land1.getWidth();        // calculated x-coordinate based on current index index and width of the land1 image
			imageLand.image = land2;        							// always set imageLand.image to land2
			landArr[index] = imageLand;
		}
	}

	public void update() {
		ImageLand firstElement = landArr[0];
		firstElement.posX -= mainCharacter.getSpeedX() / 10;		// Change in speed of BG Land, smaller value, speed faster
		float previousPosX = firstElement.posX;

		for (int index = 1; index < landArr.length; index++) {
			ImageLand element = landArr[index];
			element.posX = previousPosX + land1.getWidth();
			previousPosX = element.posX;
		}

		if (firstElement.posX < -land1.getWidth()) {
			System.arraycopy(landArr, 1, landArr, 0, landArr.length - 1);
			firstElement.posX = previousPosX + land1.getWidth();
			landArr[landArr.length - 1] = firstElement;
		}
	}

	public void draw(Graphics g) {
		for (int index = 0; index < landArr.length; index++) {
			g.drawImage(landArr[index].image, (int) landArr[index].posX, LAND_POSY, null);
		}
	}

	private class ImageLand {    // store position and image info of land1 objects in landArr array
		float posX;
		BufferedImage image;
	}
}

class Land2 {
	public static final int LAND_POSY = 80;

	private ImageLand[] landArr;
	private BufferedImage land1;
	private BufferedImage land2; 
	private MainCharacter mainCharacter;

	public Land2(int width, MainCharacter mainCharacter) {
		this.mainCharacter = mainCharacter;
		land1 = getImageTool.getImage("Image/Hills_Layer_02_resized.png");
		land2 = getImageTool.getImage("Image/Hills_Layer_02_resized.png");
		int numberOfImageLand = width / land1.getWidth() + 2;        // adds 2 to ensure there are enough images to cover the screen
		landArr = new ImageLand[numberOfImageLand];
		for (int index = 0; index < numberOfImageLand; index++) {
			ImageLand imageLand = new ImageLand();        // creates a new instance of nested imgLandPos class, assigns it to imgLand variable
			imageLand.posX = index * land1.getWidth();        // calculated x-coordinate based on current index index and width of the land1 image
			imageLand.image = land2;        // always set imageLand.image to land2
			landArr[index] = imageLand;
		}
	}
	public void update() {
		ImageLand firstElement = landArr[0];
		firstElement.posX -= mainCharacter.getSpeedX()/8;
		float previousPosX = firstElement.posX;
		for (int index = 1; index < landArr.length; index++) {
			ImageLand element = landArr[index];
			element.posX = previousPosX + land1.getWidth();
			previousPosX = element.posX;
		}
		if (firstElement.posX < -land1.getWidth()) {
			System.arraycopy(landArr, 1, landArr, 0, landArr.length - 1);
			firstElement.posX = previousPosX + land1.getWidth();
			landArr[landArr.length - 1] = firstElement;
		}
	}
    public void draw(Graphics g) {
			for (int index = 0; index < landArr.length; index++) {
				g.drawImage(landArr[index].image, (int) landArr[index].posX, LAND_POSY, null);
			}
    }
    private class ImageLand {    // store position and image info of land1 objects in landArr array
			float posX;
			BufferedImage image;
    }
}

class Land3 {
	public static final int LAND_POSY = 80;

	private ImageLand[] landArr;
	private BufferedImage land1;
	private BufferedImage land2; 
	private MainCharacter mainCharacter;

	public Land3(int width, MainCharacter mainCharacter) {
		this.mainCharacter = mainCharacter;
		land1 = getImageTool.getImage("Image/Hills_Layer_03_resized.png");
		land2 = getImageTool.getImage("Image/Hills_Layer_03_resized.png");
		int numberOfImageLand = width / land1.getWidth() + 2;        // adds 2 to ensure there are enough images to cover the screen
		landArr = new ImageLand[numberOfImageLand];
		for (int index = 0; index < numberOfImageLand; index++) {
			ImageLand imageLand = new ImageLand();        // creates a new instance of nested imgLandPos class, assigns it to imgLand variable
			imageLand.posX = index * land1.getWidth();        // calculated x-coordinate based on current index index and width of the land1 image
			imageLand.image = land2;        // always set imageLand.image to land2
			landArr[index] = imageLand;
		}
	}
	public void update() {
		ImageLand firstElement = landArr[0];
		firstElement.posX -= mainCharacter.getSpeedX()/6;
		float previousPosX = firstElement.posX;
		for (int index = 1; index < landArr.length; index++) {
			ImageLand element = landArr[index];
			element.posX = previousPosX + land1.getWidth();
			previousPosX = element.posX;
		}
		if (firstElement.posX < -land1.getWidth()) {
			System.arraycopy(landArr, 1, landArr, 0, landArr.length - 1);
			firstElement.posX = previousPosX + land1.getWidth();
			landArr[landArr.length - 1] = firstElement;
		}
	}
	public void draw(Graphics g) {
		for (int index = 0; index < landArr.length; index++) {
			g.drawImage(landArr[index].image, (int) landArr[index].posX, LAND_POSY, null);
		}
	}
	private class ImageLand {    // store position and image info of land1 objects in landArr array
		float posX;
		BufferedImage image;
	}
}

class Land4 {
	public static final int LAND_POSY = 80;

	private ImageLand[] landArr;
	private BufferedImage land1;
	private BufferedImage land2; 
	private MainCharacter mainCharacter;

	public Land4(int width, MainCharacter mainCharacter) {
		this.mainCharacter = mainCharacter;
		land1 = getImageTool.getImage("Image/Hills_Layer_04_resized.png");
		land2 = getImageTool.getImage("Image/Hills_Layer_04_resized.png");
		int numberOfImageLand = width / land1.getWidth() + 2;        // adds 2 to ensure there are enough images to cover the screen
		landArr = new ImageLand[numberOfImageLand];
		for (int index = 0; index < numberOfImageLand; index++) {
			ImageLand imageLand = new ImageLand();        // creates a new instance of nested imgLandPos class, assigns it to imgLand variable
			imageLand.posX = index * land1.getWidth();        // calculated x-coordinate based on current index index and width of the land1 image
			imageLand.image = land2;        // always set imageLand.image to land2
			landArr[index] = imageLand;
		}
	}
	public void update() {
		ImageLand firstElement = landArr[0];
		firstElement.posX -= mainCharacter.getSpeedX()/4;
		float previousPosX = firstElement.posX;
		for (int index = 1; index < landArr.length; index++) {
			ImageLand element = landArr[index];
			element.posX = previousPosX + land1.getWidth();
			previousPosX = element.posX;
		}
		if (firstElement.posX < -land1.getWidth()) {
			System.arraycopy(landArr, 1, landArr, 0, landArr.length - 1);
			firstElement.posX = previousPosX + land1.getWidth();
			landArr[landArr.length - 1] = firstElement;
		}
	}
	public void draw(Graphics g) {
		for (int index = 0; index < landArr.length; index++) {
			g.drawImage(landArr[index].image, (int) landArr[index].posX, LAND_POSY, null);
		}
	}
	private class ImageLand {    // store position and image info of land1 objects in landArr array
		float posX;
		BufferedImage image;
	}
}

class Land5 {
	public static final int LAND_POSY = 502;

	private ImageLand[] landArr;
	private BufferedImage land1;
	private BufferedImage land2; 
	private MainCharacter mainCharacter;

	public Land5(int width, MainCharacter mainCharacter) {
		this.mainCharacter = mainCharacter;
		land1 = getImageTool.getImage("Image/Hills_Layer_05aa_resized.png");
		land2 = getImageTool.getImage("Image/Hills_Layer_05aa_resized.png");
		int numberOfImageLand = width / land1.getWidth() + 2;        // adds 2 to ensure there are enough images to cover the screen
		landArr = new ImageLand[numberOfImageLand];
		for (int index = 0; index < numberOfImageLand; index++) {
			ImageLand imageLand = new ImageLand();        // creates a new instance of nested imgLandPos class, assigns it to imgLand variable
			imageLand.posX = index * land1.getWidth();        // calculated x-coordinate based on current index index and width of the land1 image
			imageLand.image = land2;        // always set imageLand.image to land2
			landArr[index] = imageLand;
		}
	}
	public void update() {
		ImageLand firstElement = landArr[0];
		firstElement.posX -= mainCharacter.getSpeedX()/0.8;
		float previousPosX = firstElement.posX;
		for (int index = 1; index < landArr.length; index++) {
			ImageLand element = landArr[index];
			element.posX = previousPosX + land1.getWidth();
			previousPosX = element.posX;
		}
		if (firstElement.posX < -land1.getWidth()) {
			System.arraycopy(landArr, 1, landArr, 0, landArr.length - 1);
			firstElement.posX = previousPosX + land1.getWidth();
			landArr[landArr.length - 1] = firstElement;
		}
	}
	public void draw(Graphics g) {
		for (int index = 0; index < landArr.length; index++) {
			g.drawImage(landArr[index].image, (int) landArr[index].posX, LAND_POSY, null);
		}
	}
	private class ImageLand {    // store position and image info of land1 objects in landArr array
		float posX;
		BufferedImage image;
	}
}