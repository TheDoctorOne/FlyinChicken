package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Chicken {
	
	private int locX, locY;
	private int WIDHT;
	private int size;
	private int egg_size=10;
	private Rectangle chickenRectangle; 
	private ArrayList<Rectangle> eggsLeft;
	private ArrayList<Rectangle> eggsRight;
	private EggMachine em;
	private Image img_chic;
	private boolean read_succes = false;
	

	public Chicken (int WIDHT, int HEIGHT, ArrayList<Rectangle> eggsLeft, ArrayList<Rectangle> eggsRight,ArrayList<Image> imgs,EggMachine em) {
		this.em = em;
		this.eggsRight= eggsRight;
		this.eggsLeft = eggsLeft;
		this.WIDHT = WIDHT;
		size=36;
		locX = WIDHT/2 - size/2;
		locY = HEIGHT/6;
		chickenRectangle = new Rectangle(locX,locY,size,size);
		
		try {
			img_chic = ImageIO.read(new File("img//chicken.png"));
			read_succes=true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Chicken read failed.");
			read_succes=false;
		}
		
	}
	
	public void moveLEFT() {
		if( locX > WIDHT/12*3 - 10)
			locX -=3;
	}
	
	public void moveRIGHT() {
		if( locX < WIDHT/12*9 - size + 10)
			locX +=3;
	}
	
	public void chickenUpdate(Graphics g) {
		
		if(read_succes)
			g.drawImage(img_chic, locX, locY, size+32, size+32, null);
		else
			g.fillRect(locX+16, locY+6, size, size+19);
		
		chickenRectangle = new Rectangle(locX+16,locY+6,size,size+19);
	}
	
	public void shotEggLeft() {
		Rectangle egg = new Rectangle(locX+size-10,locY+size/2+10,egg_size,egg_size);
		eggsLeft.add(egg);
		em.delShoot();
	}
	public void shotEggRight() {
		Rectangle egg = new Rectangle(locX+size-10 , locY+size/2+10 ,egg_size,egg_size);
		eggsRight.add(egg);
		em.delShoot();
	}
	public void eggUpdateLeft() {
		for(Rectangle egg : eggsLeft) {
			egg.x -=6;
			if(em.game == 1)
			egg.y -=1;
		}
	}
	public void eggUpdateRight() {
		for(Rectangle egg : eggsRight) {
			egg.x +=6;
			if(em.game == 1)
			egg.y -=1;
		}
	}
	public int getX () {
		return locX;
	}
	public int getY () {
		return locY;
	}
	public int getSize() {
		return size;
	}
	public void setLoc(int x, int y) {
		locX = x;
		locY = y;
	}
	
	public Rectangle getRectChicken() {
		return chickenRectangle;
	}
}
