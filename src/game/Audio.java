package game;

import java.io.File;
import java.awt.Toolkit;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

public class Audio {
	
	public static boolean MUSIC = true;
	public static boolean EFFECT = true;
	
	private boolean NOT_FOUND = false;
	
	private Clip shoot;
	private Clip select;
	private Clip dead;
	private Clip levelup;
	
	
	private EggMachine em;
	Audio(final EggMachine em){
		this.em = em;
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				while(true) {
					System.out.println("here " + em.game);
					if(MUSIC) {
						
				//in game audio
					
					if(em.game == 1) {
						System.out.println("hereee");
						try {
							AudioInputStream ais = AudioSystem.getAudioInputStream(new File("music/ingame.wav"));
							Clip clip;
							clip = AudioSystem.getClip();
							clip.open(ais);
							clip.start();
							int len = clip.getFrameLength();
							while(true) {
								if(clip.getFramePosition() > len-10000) {
									if(em.game == 1) {
										System.out.println("looped");
										clip.stop();
										clip.setFramePosition(0);
										clip.start();
									}
									else
									break;
								}
								if(em.game != 1 || !MUSIC) break;		
								
							}
							clip.stop();
							clip.close();
							ais.close();
							
						} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
							JOptionPane.showMessageDialog(null, "Audio files not found.", "WHO TOOK THEM?", 0);
							NOT_FOUND = true;
						}
						
						
					}
				//main menu audio
					if(em.game == 0 || em.game == 2) {
						System.out.println("hereee");
						try {
							AudioInputStream ais = AudioSystem.getAudioInputStream(new File("music/menu.wav"));
							Clip clip;
							clip = AudioSystem.getClip();
							clip.open(ais);
							clip.start();
							while(clip.getFramePosition() <= clip.getFrameLength()-10) {
								//System.out.println("Main Menu Audio");
								if((em.game != 0 && em.game != 2) || !MUSIC) break;
								if(clip.getFramePosition() == clip.getFrameLength()-80) break;
							}
							clip.stop();
							clip.close();
							ais.close();
							
						} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null, "Audio files not found.", "WHO TOOK THEM?", 0);
							NOT_FOUND = true;
							while(true);
						}
						
						
						
					}
					}
					}
			}
		} ).start();
		
		if(!NOT_FOUND)
			setClips();
		
	}

	private void setClips() {
		
		
		AudioInputStream ais;
		if(!NOT_FOUND)
		try {
			ais = AudioSystem.getAudioInputStream(new File("music/shoot.wav"));		//Shoot
			shoot = AudioSystem.getClip();
			shoot.open(ais);
			
			ais = AudioSystem.getAudioInputStream(new File("music/select.wav"));	//Select
			select = AudioSystem.getClip();
			select.open(ais);
			
			ais = AudioSystem.getAudioInputStream(new File("music/dead.wav"));		//Dead
			dead = AudioSystem.getClip();
			dead.open(ais);
			
			ais = AudioSystem.getAudioInputStream(new File("music/levelup.wav"));	//Level up
			levelup = AudioSystem.getClip();
			levelup.open(ais);
			
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			// TODO Auto-generated catch block
			System.out.println("AUDIO FILES GONE.");
		}
		
		
		
	}
	
	public void shoot() {
		if(!NOT_FOUND)
		new Thread(new Runnable() {
			
			@Override
			public void run() {
					
						if(EFFECT) {
							shoot.setFramePosition(0);
							shoot.start();
					
						}
				}
		}).start();
		
		
	}
	
	public void select() {
		if(!NOT_FOUND)
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(EFFECT && em.game == 0) {
					
					try {
						AudioInputStream ais = AudioSystem.getAudioInputStream(new File("music/select.wav"));	//Select
						select = AudioSystem.getClip();
						select.open(ais);
					} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					select.start();
			
				}
				
			}
		}).start();
		
	}
	
	public void dead() {
		if(!NOT_FOUND)
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(EFFECT) {
					dead.setFramePosition(0);
					dead.start();
			
				}
				
			}
		}).start();
	}
	
	public void levelup() {
		if(!NOT_FOUND)
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(EFFECT) {
					levelup.setFramePosition(0);
					levelup.start();
			
				}
				
			}
		}).start();
	}
	
	public void loselife() {
		if(!NOT_FOUND)
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(EFFECT)
				try {
					AudioInputStream ais = AudioSystem.getAudioInputStream(new File("music/loselife.wav"));
					Clip clip;
					clip = AudioSystem.getClip();
					clip.open(ais);
					clip.start();
					while(clip.getFramePosition() != clip.getFrameLength()) {
						
					}
					clip.stop();
					clip.close();
					ais.close();
					
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
		
	}
	
	public void gainloot() {
		if(!NOT_FOUND)
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(EFFECT)
				try {
					AudioInputStream ais = AudioSystem.getAudioInputStream(new File("music/gainloot.wav"));
					Clip clip;
					clip = AudioSystem.getClip();
					clip.open(ais);
					clip.start();
					while(clip.getFramePosition() != clip.getFrameLength()) {
						
					}
					clip.stop();
					clip.close();
					ais.close();
					
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
		
		
	}
	
}
