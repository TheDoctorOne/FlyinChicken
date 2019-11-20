package game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

public class EggMachine implements ActionListener, KeyListener{
	
	public static final int WIDTH=420 , HEIGHT=600;
	public static final int TARGET_SIZE = 25;
	private int target_size_point=0;
	public int game=0;
	public boolean PAUSE = false;
	private JFrame frame;
	private Chicken chicken;
	private GamePanel panel;
	private Audio audio;
	
	private ArrayList<Rectangle> targets = new ArrayList<Rectangle>();
	private ArrayList<Rectangle> targetsToRemove = new ArrayList<Rectangle>();
	private ArrayList<Rectangle> eggsLeft = new ArrayList<Rectangle>();
	private ArrayList<Rectangle> eggsRight = new ArrayList<Rectangle>();
	private ArrayList<Image> imgs = new ArrayList<Image>();
	private boolean needToWrite=false;
	private Timer timer = new Timer(10,this);
	private int time=0;
	private Random rand = new Random();
	//IN - GAME SCORES / VARIABLES
	private int level=1;
	private boolean EndlessMode=false;
	private int shoots=24;
	private int life=15;
	private int score=0;
	
	private int highscore=0;
	   
	
	public int get_Highscore() {
		
		return highscore;
	}
	
    private void getHighscore(){ //Read high score from dat file
    	int hs=0;
    	FileInputStream fis= null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream("data.dat");
            
            if(fis.read() == '1') med_Available=true;
            if(fis.read() == '1') hard_Available=true;
            
            while(fis.read() != -1){
                hs++;
            }
            System.out.println(hs);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found.");
            try {
				fos = new FileOutputStream("data.dat");
				fos.write('0');
				fos.write('0');
				System.out.println("File created.");
			} catch (IOException e) {
				System.out.println("File couldn't created.");
				e.printStackTrace();
			}
        } catch (IOException ex) {

        }
        finally{
            try {
                if(fis != null)
                	fis.close();
                if(fos != null)
                	fos.close();
            } catch (IOException ex) {

            }
        }
        highscore = hs;
    }
	
    public void addLife () { 
		life++; audio.gainloot();}
	public void delLifeByLevel () { 
		audio.loselife();
		life = life - level; }
	public void addShoot () { 
		shoots +=2; audio.gainloot();}
	public int getLife() {
		return life;
	}
	public int getShoot () { 
		return shoots;}
	public void delShoot () { 
		if( game == 1 ) shoots--;}
	public int getScore () {
		return score;
	}
	public void lifeToShoot() {
		if ( life > 1 && shoots == 0) {
			life--;
			shoots +=4;
			audio.loselife();
		}
	}
	public void calculateLife() {
		if(!targets.isEmpty())
		for(Rectangle r : targets) {
			if(r.y < 0 - 25) {
				life--;
				audio.loselife();
				targetsToRemove.add(r);
			}
		}
		targets.removeAll(targetsToRemove);
		if (life <= 0) {
			audio.dead();
			game=2;
		}
	}
	public int getTime() { 
		return time;}
	
	private void writeHighscore() { //HighScore to dat file
		if(game != 1 && score >= highscore && needToWrite) {
			needToWrite = false;
			
			FileOutputStream fos = null;
			int random;
			try {
				fos = new FileOutputStream("data.dat");
				System.out.println("write");
				
				if(med_Available) fos.write('1');
				else			  fos.write('0');
				
				if(hard_Available)fos.write('1');
				else			  fos.write('0');
				
				for(int i=0; i<highscore ;i++) {
					random = rand.nextInt(50)+65;
					fos.write(random);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
			
				try {
					if(fos != null)
						fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		}
			
		}
		
	}
	//MAIN
	private void go() {
		frame = new JFrame("FLYING CHICKEN");
		chicken = new Chicken(WIDTH,HEIGHT,eggsLeft,eggsRight,imgs,this);
		panel = new GamePanel(chicken, this , targets, eggsLeft, eggsRight,imgs);
		audio = new Audio(this);
		getHighscore(); //GetHighScore or create the file needed.
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.add(panel);
		frame.setVisible(true);
		frame.addKeyListener(this);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		timer.start();
		updateGame();
		
	}
	
	public static void main(String[] args) {
		new EggMachine().go();
	}
	
	// TARGET LOCALIZATION
	public void randomTargets() {
		int getRand = rand.nextInt(100);
		Rectangle temp;
		target_size_point = rand.nextInt(9);
		if(time % (100-level*5) == 0) {
			if(getRand % 2 == 0) { //Left Side Targets
				temp = new Rectangle(0, 520 , TARGET_SIZE-target_size_point , TARGET_SIZE-target_size_point);
				targets.add(temp);
				
				
			}else if(getRand % 2 == 1) { //Right Side Targets
				temp = new Rectangle(390+target_size_point, 520 , TARGET_SIZE-target_size_point , TARGET_SIZE-target_size_point);
				targets.add(temp);
				
				
			}
		}
	}
		
	//TARGET MOVEMENT
	public void targetUpdate() {
		for(Rectangle r : targets) {
			r.y -=(3+level);
		}
	}
	
	
	//LEVEL UP
	public void levelUp () {
		level++;
	}
	public int getLevel() {
		return level;
	} 
	
	// CHECK IF HIT
	public void ifHit () {
		int i_t=0;
		ArrayList<Rectangle> toRemove = new ArrayList<Rectangle>();
		for(i_t=0; i_t < targets.size() ;i_t++) {
			Rectangle t = targets.get(i_t);
			for(Rectangle e : eggsLeft) {
				if(t.intersects(e)) {
					toRemove.add(t);
					score = (score+1)+((TARGET_SIZE-t.width)/3)+level;
					System.out.println("target size : " + TARGET_SIZE +" Width : " + t.width + "\n");
					shoots +=2;
				}
			}
			for(Rectangle e : eggsRight) {
				if(t.intersects(e)) {
					toRemove.add(t);
					score = (score+1)+((TARGET_SIZE-t.width)/3)+level;
					System.out.println("target size : " + TARGET_SIZE +" Width : " + t.width + "\n");
					shoots +=2;
					
					
					break;
				}
			}
			targets.removeAll(toRemove);
		}
		
		if(highscore <= score && score != 0 && game == 1) {highscore = score; needToWrite=true;}
	}
	
	public void restartGame() {
		PAUSE=false;
		time=0;
		level=1;
		score=0;
		EndlessMode=false;
		game=1;
		shoots=16;
		life=15;
		chicken.setLoc(WIDTH/2 - chicken.getSize()/2, HEIGHT/6);
		targets.clear();
		eggsLeft.clear();
		eggsRight.clear();
		imgs.clear();
		panel.img_y=0;
		needToWrite = false;
	}
	
//		EVENT LISTENERS
	
	//KEY BOOLEANS
	private boolean right=false;
	private boolean left=false;
	private boolean keyZ=false;
	private boolean keyX=false;
	private boolean shootOnceX=true;
	private boolean shootOnceZ=true;
	
	@Override
	public void keyPressed(KeyEvent e) {
		int i = e.getKeyCode();
		
		// START GAME COMMANDS
		
		
		//MAIN MENU SELECTS
		if( i == KeyEvent.VK_ENTER || i == KeyEvent.VK_SPACE) {
				if(game == 0) {	
					if(GamePanel.PLAY == false) {
						if(GamePanel.SELECT == 0) GamePanel.PLAY = true;
						else if(GamePanel.SELECT == 1) JOptionPane.showMessageDialog(null, "Pause game with 'P'\nToggle Music with 'M'\nToggle Sound Effects with 'S'\nMove left and right with arrow keys\nShoot left with 'Z'\nShoot right with 'X'\nAvoid cats they will decrease your health"
								+ "\nCollect hearths to gain healths\nCollect Egg Packs and Shoot Targets for more eggs","INSTRUCTION",1);
						else if(GamePanel.SELECT == 2) {int k = JOptionPane.showConfirmDialog(null, "ARE YOU SURE ABOUT THIS?", "EXIT", 1);
							if(k == 0) System.exit(0);
							if(k == 1) JOptionPane.showMessageDialog(null, "Then come back when you sure.","BE SURE!",0);
						}
					}
					else if(GamePanel.PLAY == true) {
						if(GamePanel.SELECT == 0) { //EASY
							timer.start();
							restartGame();
						}
						if(GamePanel.SELECT == 1) { //MEDIUM
							if(!med_Available) JOptionPane.showMessageDialog(null, "You must reach level 3", "REACH LEVEL 3!", 0);
							else {
							timer.start();
							restartGame();
							level=3;}
						}
						if(GamePanel.SELECT == 2) { //HARD
							if(!hard_Available) JOptionPane.showMessageDialog(null, "You must reach level 5", "REACH LEVEL 5!", 0);
							else {
							timer.start();
							restartGame();
							level=5;}
						}
						if(GamePanel.SELECT == 3) {
							GamePanel.PLAY = false;
							GamePanel.SELECT = 0;
						}
						
						
					}
				}
				if (game == 2) {
					GamePanel.PLAY = false;
					GamePanel.SELECT = 0;
					game = 0;
					//timer.restart();
					//restartGame();
				}
				
		}
		
		//KEYS
		switch (e.getKeyCode()){
			case KeyEvent.VK_RIGHT:
				right=true;
				break;
			case KeyEvent.VK_LEFT:
				left=true;
				break;
			case KeyEvent.VK_Z:
				if(!PAUSE)
				audio.shoot();
				shootOnceZ=true;
				keyZ=true;
				break;
			case KeyEvent.VK_X:
				if(!PAUSE)
				audio.shoot();
				shootOnceX=true;
				keyX=true;
				break;
			case KeyEvent.VK_DOWN:
				GamePanel.SELECT++;
				audio.select();
				if(GamePanel.SELECT == 3 && GamePanel.PLAY == false) GamePanel.SELECT=0;
				if(GamePanel.SELECT == 4 && GamePanel.PLAY == true) GamePanel.SELECT=0;
				break;
			case KeyEvent.VK_UP:
				GamePanel.SELECT--;
				audio.select();
				if(GamePanel.SELECT == -1 && GamePanel.PLAY == false) GamePanel.SELECT=2;
				if(GamePanel.SELECT == -1 && GamePanel.PLAY == true) GamePanel.SELECT=3;
				break;
			case KeyEvent.VK_M:
				if(Audio.MUSIC)
					Audio.MUSIC = false;
				else if(!Audio.MUSIC)
					Audio.MUSIC = true;
				break;
			case KeyEvent.VK_S:
				if(Audio.EFFECT)
					Audio.EFFECT=false;
				else if(!Audio.EFFECT)
					Audio.EFFECT = true;
				break;
			case KeyEvent.VK_P:
				if(PAUSE)
					PAUSE = false;
				else if(!PAUSE && game == 1)
					PAUSE = true;
				break;
		}
		}
		
	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()){
		case KeyEvent.VK_RIGHT:
			right=false;
			break;
		case KeyEvent.VK_LEFT:
			left=false;
			break;
		case KeyEvent.VK_Z:
			keyZ=false;
			break;
		case KeyEvent.VK_X:
			keyX=false;
			break;
		
		
	}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	//Level Access
	private boolean med_Available = false;
	private boolean hard_Available= false;
	
	private void updateGame() {
		Thread gameUpdates = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					if(!PAUSE) {
						
						panel.loots();
						
						//movement
						if(keyZ && shootOnceZ) {
							if(shoots > 0 || game != 1) chicken.shotEggLeft();  shootOnceZ=false;}
						if(keyX && shootOnceX) {
							if(shoots > 0 || game != 1) chicken.shotEggRight(); shootOnceX=false;}
						if(right)
							chicken.moveRIGHT();
						if(left)
							chicken.moveLEFT();
						
						//ShootED eggs
						chicken.eggUpdateLeft();
						chicken.eggUpdateRight();
						
						//Random Target Updates
						if(game == 1) {
						randomTargets();
						targetUpdate();
						time++;
						calculateLife();}
						ifHit();
						lifeToShoot();
						if(needToWrite) {
							//needToWrite=false;
							writeHighscore();
						}
						//level up
						if(!EndlessMode)
						if(time % 800 == 0 && game == 1 && level < 5) {
							levelUp();
							audio.levelup();
							if(level == 3) med_Available = true;
							
						}else if (level == 5) {
							hard_Available = true;
							EndlessMode =true;
						}
						}
						try {
							Thread.sleep(15);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			}
		});
		gameUpdates.start();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		panel.repaint();
	}

}
