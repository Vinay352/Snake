package snake;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Game {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		JFrame jframe=new JFrame("Snake");
		jframe.setContentPane(new GamePanel());
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setResizable(false);
		jframe.setPreferredSize(new Dimension(GamePanel.WIDTH,GamePanel.HEIGHT));
		jframe.pack();
		jframe.setVisible(true);
		jframe.setLocationRelativeTo(null);
		
	}

}
