/*
 *  COMP 435
 * 	Assignment 2
 * 	James Bombardier
 * 	3444839
 * 	April 25th, 2022
 * 
 * 	Program		: ZoomableImagePane
 * 
 * 	Description	: This class extends JPanel to provide a zoomable image container.
 */

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class ZoomableImagePane extends JPanel {
	private static final long serialVersionUID = 1L;
	//The displayed image.
	private BufferedImage image;

	private double zoomFactor = 1;
	
	/*
	 * Custom constructor.
	 */
	public ZoomableImagePane(BufferedImage _image) {
		image = _image;
		setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		revalidate();
	}
	
	/*
	 * Zooms the panel and it's contents in.
	 */
	public void ZoomIn() {
		if(zoomFactor == 0) {
			zoomFactor = 0.05; // Limit the scaling
			return;
		}
		zoomFactor = zoomFactor * 1.1; //Relative scaling
		setPreferredSize(new Dimension((int)(image.getWidth() * zoomFactor), (int)(image.getHeight() * zoomFactor)));
		revalidate();
		repaint();
	}
	
	/*
	 * Zooms the panel and it's contents out.
	 */
	public void ZoomOut() {
		if(zoomFactor < 0.05) {
			zoomFactor = 0.05;
			return;
		}
		zoomFactor = zoomFactor * 0.9; // Relative scaling
		setPreferredSize(new Dimension((int)(image.getWidth() * zoomFactor), (int)(image.getHeight() * zoomFactor)));
		revalidate();
		repaint();
	}
	
	/*
	 * Resets the zoom on the panel and it's contents.
	 */
	public void ResetZoom() {
		zoomFactor = 1;
		setPreferredSize(new Dimension((int)(image.getWidth() * zoomFactor), (int)(image.getHeight() * zoomFactor)));
		revalidate();
		repaint();
	}
	
	@Override 
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawImage(image, getX(), getY(), getWidth(), getHeight(), null);
        if(this.getParent().isValid()) { this.getParent().repaint(); }
        g2d.dispose();
	}
	
	public void setImage(BufferedImage _image) {
		image = _image;
	}
	
}
