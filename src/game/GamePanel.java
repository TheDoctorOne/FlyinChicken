package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	
	private Chicken c;
	private EggMachine em;
	private ArrayList<Rectangle> targets;
	private ArrayList<Rectangle> eggsLeft;
	private ArrayList<Rectangle> eggsRight;
	private ArrayList<Image> imgs;
	private ArrayList<Image> imgs_eggPack = new ArrayList<Image>();
	private ArrayList<Image> imgs_cat = new ArrayList<Image>();
	private Random rand = new Random();
	private ArrayList<Dimension> imgXY = new ArrayList<Dimension>();
	private ArrayList<Dimension> imgXY_eggPack = new ArrayList<Dimension>();
	private ArrayList<Dimension> imgsXY_cat = new ArrayList<Dimension>();
	private Image img_heart;
	private Image img_eggPack;
	private Image img;
	private Image img_cat;
	public int img_y=0;
	
	public static int SELECT=0;
	public static boolean PLAY=false;
	
	
	public GamePanel(Chicken c, EggMachine em, ArrayList<Rectangle> targets, ArrayList<Rectangle> eggsLeft, ArrayList<Rectangle> eggsRight,ArrayList<Image> imgs) {
		this.c = c;
		this.em = em;
		this.targets = targets;
		this.eggsLeft = eggsLeft;
		this.eggsRight = eggsRight;
		this.imgs = imgs;
		c.getRectChicken();
		try {
			img_heart = ImageIO.read(new File ("img//heart.png"));
			img_eggPack = ImageIO.read(new File("img//eggpack.png"));
			img = ImageIO.read(new File("img//background.jpg"));
			img_cat = ImageIO.read(new File("img//cat.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, EggMachine.WIDTH , EggMachine.HEIGHT); // BACKGROUND
		g.drawImage(img, 0, img_y, null); //BACKGROUND IMAGE
		g.drawImage(img, 0, img.getHeight(null)+img_y, null);
		if(img_y + img.getHeight(null) < 2) img_y=0;
		
		g.setColor(Color.WHITE);
		c.chickenUpdate(g);
		
		// Drawing Eggs
		g.setColor(Color.YELLOW);
		ArrayList<Rectangle> allEggs = new ArrayList<Rectangle>();
		for(Rectangle egg : eggsLeft) {
			allEggs.add(egg);
		}
		for(Rectangle egg : eggsRight) {
			allEggs.add(egg);
		}
		for(Rectangle egg : allEggs) {
			g.fillOval(egg.x, egg.y, egg.width+3, egg.height-1);
		}
		g.setColor(Color.ORANGE);
		//Drawing Targets
		for(Rectangle target : targets) {
			if(em.game == 1)
			g.fillRect(target.x, target.y, target.width, target.height);
			
			
		}
		
		//Loots
		drawLoots(g);
		
		//PAUSE
		if(em.PAUSE) {
			g.setColor(Color.RED);
			g.drawString("PAUSED", EggMachine.WIDTH/2-50, EggMachine.HEIGHT/3-40);
			g.drawString("PRESS P FOR CONTINUE", EggMachine.WIDTH/2-80, EggMachine.HEIGHT/3+20);
		}
		
		g.setColor(Color.BLACK); /* BOTTOM: SCORE & LIFE LEFT */
		g.fillRect(0, 520, 420, 60);
		g.setColor(Color.cyan);
		
		g.drawString("High Score : " + em.get_Highscore(), 10, 20);
		g.drawLine(0, 520, 420, 520);
		g.drawString("Life : " + em.getLife(), 20, 550);
		g.drawString("Score :" + em.getScore(), 330, 550);
		g.drawString("Egg Remain", 173, 544);
		g.drawString(em.getShoot() + " " , 195, 559);
		if(em.game == 1) {
			g.drawString("Level : " + em.getLevel(), 330, 20);
			bgUpdate(g);
		}
		g.setColor(Color.BLACK);
		if(em.game == 2) {
			imgXY.clear();
			imgXY_eggPack.clear();
			imgs_eggPack.clear();
			imgs_cat.clear();
			imgsXY_cat.clear();
			g.drawString("Your Score was : " + em.getScore(), EggMachine.WIDTH/2-50, EggMachine.HEIGHT/3-40);
			
			g.drawString("PRESS SPACE TO BACK TO MAIN MENU!", EggMachine.WIDTH/2-80, EggMachine.HEIGHT/3+20);
			if(em.getScore() < 12) {
				g.drawString("YOU SUCK AS HELL", EggMachine.WIDTH/2-55, EggMachine.HEIGHT/3-10);
				}
				else if (em.getScore() < 26) {
				g.drawString("YOU SUCK", EggMachine.WIDTH/2-50, EggMachine.HEIGHT/3-10);
				}else if(em.getScore() < 75) {
					g.drawString("FINE ", EggMachine.WIDTH/2-100, EggMachine.HEIGHT/3-10);
				} else if(em.getScore() > 499) {
					g.drawString("YOU MADE IT! NICE HACK", EggMachine.WIDTH/2-70, EggMachine.HEIGHT/3-10);
				} else {
					g.drawString("YOU'RE LEARNING. THE GOAL IS 500!", EggMachine.WIDTH/2-100, EggMachine.HEIGHT/3-10);
				}
			}
		
		if(em.game == 0) {	//MAIN MENU
			imgXY.clear();
			imgXY_eggPack.clear();
			imgs_eggPack.clear();
			imgs_cat.clear();
			imgsXY_cat.clear();
			g.setColor(Color.BLUE);
			g.drawString("WELCOME TO THE FLYING CHICKEN!", EggMachine.WIDTH/2-80, EggMachine.HEIGHT/3-30);
			g.setColor(Color.BLACK);
			if(!PLAY) {
				if(SELECT!=0)
					g.drawString("PLAY", EggMachine.WIDTH/2, EggMachine.HEIGHT/3);
				else {
					g.setColor(Color.CYAN);
					g.drawString(">PLAY", EggMachine.WIDTH/2-8, EggMachine.HEIGHT/3);
					g.setColor(Color.BLACK);}
				if(SELECT!=1)
					g.drawString("INSTRUCTIONS", EggMachine.WIDTH/2-30, EggMachine.HEIGHT/3+30);
				else {
					g.setColor(Color.CYAN);
					g.drawString(">INSTRUCTIONS", EggMachine.WIDTH/2-30, EggMachine.HEIGHT/3+30);
					g.setColor(Color.BLACK);}
				g.setColor(Color.ORANGE);
				if(SELECT!=2)
					g.drawString("EXIT", EggMachine.WIDTH/2, EggMachine.HEIGHT/3+60);
				else {
					g.setColor(Color.RED);
					g.drawString(">EXIT", EggMachine.WIDTH/2-8, EggMachine.HEIGHT/3+60);
					g.setColor(Color.BLACK);}
				g.setColor(Color.BLACK);
			}
			else {
				if(SELECT!=0)
					g.drawString("EASY", EggMachine.WIDTH/2, EggMachine.HEIGHT/3);
				else {
					g.setColor(Color.CYAN);
					g.drawString(">EASY", EggMachine.WIDTH/2-8, EggMachine.HEIGHT/3);
					g.setColor(Color.BLACK);}
				if(SELECT!=1)
					g.drawString("MEDIUM", EggMachine.WIDTH/2-8, EggMachine.HEIGHT/3+30);
				else {
					g.setColor(Color.CYAN);
					g.drawString(">MEDIUM", EggMachine.WIDTH/2-14, EggMachine.HEIGHT/3+30);
					g.setColor(Color.BLACK);}
				if(SELECT!=2)
					g.drawString("HARD", EggMachine.WIDTH/2, EggMachine.HEIGHT/3+60);
				else {
					g.setColor(Color.CYAN);
					g.drawString(">HARD", EggMachine.WIDTH/2-8, EggMachine.HEIGHT/3+60);
					g.setColor(Color.BLACK);}
				g.setColor(Color.BLUE);
				if(SELECT!=3)
					g.drawString("BACK", EggMachine.WIDTH/2, EggMachine.HEIGHT/3+90);
				else {
					g.setColor(Color.CYAN);
					g.drawString(">BACK", EggMachine.WIDTH/2-8, EggMachine.HEIGHT/3+90);
					g.setColor(Color.BLACK);
				}
				g.setColor(Color.BLACK);
			}
			
			repaint();
		}
	} 
	public void drawLoots (Graphics g) {
		int i=0, k = imgs.size();
		if(em.game == 1) {
			
			for(i=0 ; i<k ; i++) {// HEALTH
				if(!em.PAUSE) imgXY.get(i).height--;
				g.drawImage(imgs.get(i), imgXY.get(i).width , imgXY.get(i).height, null);
				
				if( c.getRectChicken().intersects(new Rectangle(imgXY.get(i).width , imgXY.get(i).height, 32,32) ) ) {
					em.addLife();
					imgs.remove(i);
					imgXY.remove(i);
					i = 0;
					k = imgs.size();
				}
			}
			
			k= imgs_eggPack.size();
			for(i=0 ; i<k ; i++) { // EGG PACK
				if(!em.PAUSE) imgXY_eggPack.get(i).height--;
				g.drawImage(imgs_eggPack.get(i), imgXY_eggPack.get(i).width , imgXY_eggPack.get(i).height, null);
				
				if( c.getRectChicken().intersects(new Rectangle(imgXY_eggPack.get(i).width , imgXY_eggPack.get(i).height, 32,32) ) ) {
					em.addShoot();
					imgs_eggPack.remove(i);
					imgXY_eggPack.remove(i);
					i = 0;
					k = imgs_eggPack.size();
				}
			}
			
			k= imgs_cat.size();
			
			for(i=0 ; i<k ; i++) {// CAT
				if(!em.PAUSE) imgsXY_cat.get(i).height--;
				g.drawImage(imgs_cat.get(i), imgsXY_cat.get(i).width , imgsXY_cat.get(i).height, null);
				
				if( c.getRectChicken().intersects(new Rectangle(imgsXY_cat.get(i).width , imgsXY_cat.get(i).height, 32,32) ) ) {
					em.delLifeByLevel();
					imgs_cat.remove(i);
					imgsXY_cat.remove(i);
					i = 0;
					k = imgs_cat.size();
				}
			}
			
			
			
			}/* END IF */
			
			
		}
	
	public void loots() {
		int i = rand.nextInt(100);
		if(em.game == 1 && em.getTime() % 150 == 0) {
		if( i % 10 == 0) {
			i = rand.nextInt(100);
			if(i % 3 == 0) { //heart
				imgs.add(img_heart);
				imgXY.add(new Dimension(rand.nextInt(195)+95 , 500));
			}else if(i % 3 == 1) { //eggPack
				imgs_eggPack.add(img_eggPack);
				imgXY_eggPack.add(new Dimension(rand.nextInt(195)+95 , 500));
			}
		}}
		if(em.game == 1 && i % 5 == 0 && em.getTime() % 50 == 0) {
			imgs_cat.add(img_cat);
			imgsXY_cat.add(new Dimension(rand.nextInt(195)+95 , 510+(i % 20)));
		}
	}
	
	public void bgUpdate(Graphics g) {
		if(!em.PAUSE)
		img_y -=1;
		if(em.game == 2 || em.game == 0) {
			img_y = 0;
		} 
		
	}
	
}
