/*
 *  COMP 435
 * 	Assignment 2
 * 	James Bombardier
 * 	3444839
 * 	April 25th, 2022
 * 
 * 	Program		: QuantizationPanel
 * 
 * 	Description	: This class extends JPanel to provide a Convenient container for inputting
 * 		and storing JPEG quantization tables.
 */
import java.awt.GridLayout;

import javax.imageio.plugins.jpeg.JPEGQTable;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class QuantizationPanel extends JPanel {

	//The input fields.
	private JTextField[][] boxes;
	//Default chrominance and luminance values as per the text.
	public static final int[][] DEFAULT_LUMINANCE = { { 16, 11, 10, 16, 24, 40, 51, 61 },
			{ 12, 12, 14, 19, 26, 58, 60, 55 }, { 14, 13, 16, 24, 40, 57, 69, 56 }, { 14, 17, 22, 29, 51, 87, 80, 62 },
			{ 18, 22, 37, 56, 68, 109, 103, 77 }, { 24, 35, 55, 64, 81, 104, 113, 92 },
			{ 49, 64, 78, 87, 103, 121, 120, 101 }, { 72, 92, 95, 98, 112, 100, 103, 99 } };
	public static final int[][] DEFAULT_CHROMINANCE = { { 17, 18, 24, 47, 99, 99, 99, 99 },
			{ 18, 21, 26, 66, 99, 99, 99, 99 }, { 24, 26, 56, 99, 99, 99, 99, 99 }, { 47, 66, 99, 99, 99, 99, 99, 99 },
			{ 99, 99, 99, 99, 99, 99, 99, 99 }, { 99, 99, 99, 99, 99, 99, 99, 99 }, { 99, 99, 99, 99, 99, 99, 99, 99 },
			{ 99, 99, 99, 99, 99, 99, 99, 99 } };

	/*
	 * Custom constructor.
	 */
	public QuantizationPanel() {
		boxes = new JTextField[8][8];
		setLayout(new GridLayout(8, 8));
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				boxes[i][j] = new JTextField("0");
				add(boxes[i][j]);
			}
		}

	}

	/*
	 * Getter method.
	 */
	public int getTableEntry(int i, int j) {
		return Integer.parseInt(boxes[i][j].getText());
	}

	/*
	 * Setter method.
	 */
	public void setTableEntry(int i, int j, int newValue) {
		boxes[i][j].setText(String.valueOf(newValue));
	}

	/*
	 * Assigns the default chrominance to this quantization table.
	 */
	public void setDefaultChrominance() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				boxes[i][j].setText(String.valueOf(DEFAULT_CHROMINANCE[i][j]));;
			}
		}
	}

	/*
	 * Assigns the default luminance to this quantization table.
	 */
	public void setDefaultLuminance() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				boxes[i][j].setText(String.valueOf(DEFAULT_LUMINANCE[i][j]));;
			}
		}
	}
	
	/*
	 * Sets all values to _i.
	 */
	public void setConstantValue(int _i) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				boxes[i][j].setText(Integer.toString(_i));;
			}
		}
	}
	
	/*
	 * Sets all values to 256, and the DC value to 1. 0's and values smaller than  will
	 * always throw errors with the JPEG conversion algorithm, so I had to improvise by using 
	 * an extremely low value (256)
	 */
	public void setDCOnly() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				boxes[i][j].setText("256");
			}
		}
		boxes[0][0].setText("1");
	}
	
	/*
	 * Returns the values of boxes[][] as a JPEGQTable.
	 */
	public JPEGQTable getQTable() {
		int tracker = 0;
		int qTable[] = new int[8 * 8];
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				qTable[tracker++] = Integer.parseInt(boxes[i][j].getText());
			}
		}
		return new JPEGQTable(qTable);
	}
	
}
