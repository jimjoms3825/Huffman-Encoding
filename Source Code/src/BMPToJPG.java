/*
 *  COMP 435
 * 	Assignment 2
 * 	James Bombardier
 * 	3444839
 * 	April 25th, 2022
 * 
 * 	Program		: BMPToJPG
 * 
 * 	Description	: This class contains the main data to run the program for converting a bmp
 * 		image to a JPG image with custom quantization tables.
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.plugins.jpeg.JPEGQTable;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.w3c.dom.NodeList;

public class BMPToJPG {

	public JFrame frame;

	public BufferedImage image1;
	public BufferedImage image2;

	public JScrollPane scrollPane1;
	public JScrollPane scrollPane2;

	public ZoomableImagePane zoomPane1;
	public ZoomableImagePane zoomPane2;

	public JPanel quantizationPanel;

	public QuantizationPanel luminancePanel;
	public QuantizationPanel chrominancePanel;

	public static void main(String args[]) {
		new BMPToJPG();
	}

	/*
	 * Constructor method which initializes the program.
	 */
	public BMPToJPG() {
		// Frame initialization
		frame = new JFrame();
		frame.setSize(new Dimension(1400, 800));
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.setTitle("BMP To JPG Program");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Quantization panel creation.
		quantizationPanel = new JPanel(new GridLayout(4, 1));
		quantizationPanel.add(new JLabel("Luminance", SwingConstants.CENTER));
		luminancePanel = new QuantizationPanel();
		quantizationPanel.add(luminancePanel);
		quantizationPanel.add(new JLabel("Chrominance", SwingConstants.CENTER));
		chrominancePanel = new QuantizationPanel();
		quantizationPanel.add(chrominancePanel);
		chrominancePanel.setDefaultChrominance();
		luminancePanel.setDefaultLuminance();

		// Preparing the images.
		try {
			image1 = ImageIO.read(BMPToJPG.class.getResource("flower.bmp"));
			image2 = convertToJPG(image1);
		} catch (IOException e) {
			System.out.println("Could not load images.");
			System.exit(0);
			e.printStackTrace();
		}

		//Creating the photo storage panels.
		zoomPane1 = new ZoomableImagePane(image1);
		zoomPane2 = new ZoomableImagePane(image2);
		scrollPane1 = new JScrollPane(zoomPane1);
		scrollPane2 = new JScrollPane(zoomPane2);

		// General panel layout
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(0, 3));
		mainPanel.add(scrollPane1, BorderLayout.WEST);
		mainPanel.add(scrollPane2, BorderLayout.CENTER);
		mainPanel.add(quantizationPanel, BorderLayout.EAST);

		// Menu addition
		JMenuBar menu = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem close = new JMenuItem("Close");

		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					outputPhoto();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		file.add(save);
		file.add(close);
		menu.add(file);

		//Button initialization.
		JButton zoomIn1 = new JButton("Zoom In");
		JButton zoomOut1 = new JButton("Zoom Out");
		JButton resetZoom1 = new JButton("Actual Size");
		JButton zoomIn2 = new JButton("Zoom In");
		JButton zoomOut2 = new JButton("Zoom Out");
		JButton resetZoom2 = new JButton("Actual Size");
		JButton defaultButton = new JButton("Default");
		JButton constant = new JButton("Constant");
		JButton dcOnly = new JButton("DC Only");

		//Button listeners
		zoomIn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoomPane1.ZoomIn();
			}
		});

		zoomOut1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoomPane1.ZoomOut();
			}
		});

		resetZoom1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoomPane1.ResetZoom();
			}
		});

		zoomIn2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoomPane2.ZoomIn();
			}
		});

		zoomOut2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoomPane2.ZoomOut();
			}
		});

		resetZoom2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoomPane2.ResetZoom();
			}
		});

		defaultButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chrominancePanel.setDefaultChrominance();
				luminancePanel.setDefaultLuminance();
			}
		});

		constant.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chrominancePanel.setConstantValue(200);
				luminancePanel.setConstantValue(200);
			}
		});

		dcOnly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chrominancePanel.setDCOnly();
				luminancePanel.setDCOnly();
			}
		});

		//Add buttons to panel.
		JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
		JPanel buttonSubPanel1 = new JPanel();
		JPanel buttonSubPanel2 = new JPanel();
		JPanel buttonSubPanel3 = new JPanel();

		buttonSubPanel1.add(zoomIn1);
		buttonSubPanel1.add(zoomOut1);
		buttonSubPanel1.add(resetZoom1);

		buttonSubPanel2.add(zoomIn2);
		buttonSubPanel2.add(zoomOut2);
		buttonSubPanel2.add(resetZoom2);

		buttonSubPanel3.add(defaultButton);
		buttonSubPanel3.add(constant);
		buttonSubPanel3.add(dcOnly);

		buttonPanel.add(buttonSubPanel1);
		buttonPanel.add(buttonSubPanel2);
		buttonPanel.add(buttonSubPanel3);

		//Add panels to frame.
		frame.add(menu, BorderLayout.NORTH);
		frame.add(mainPanel, BorderLayout.CENTER);
		frame.add(buttonPanel, BorderLayout.SOUTH);

		frame.setVisible(true);
	}

	/*
	 * Converts the passed image to a jpg image and returns it.
	 */
	private BufferedImage convertToJPG(BufferedImage oldImage) {

		File output = new File("ConvertedImage.jpg");
		try {
			ImageIO.write(oldImage, "jpg", output);
			return ImageIO.read(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	/*
	 * 	This method is a modified algorithm which was originally written by Harold K.
	 * on a stack overflow thread.
	 * (https://stackoverflow.com/questions/25037227/custom-quantization-tables-for-jpeg-compression-in-java)
	 * Some of the comments from his algorithm have remained in place, but much of the code
	 * has been changed or ammended to fit the current problem.
	 * 
	 * 	This solves the largest limitation of the ImageIO librarys quantization solution, in that
	 * this algorithm creates the new output file from scratch, allowing for custom metadata
	 * to be written directly to the "fresh" file. 
	 */
	public void outputPhoto() throws IOException {

		IIOMetadata metadata = null;
		ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("JPEG").next();

		//Custom quantization tables.
		JPEGQTable[] quantizationTables = { luminancePanel.getQTable(), chrominancePanel.getQTable() };
		//Standard huffman tables.
		JPEGHuffmanTable[] huffmanDcTables = { JPEGHuffmanTable.StdDCLuminance, JPEGHuffmanTable.StdDCChrominance };
		JPEGHuffmanTable[] huffmanAcTables = { JPEGHuffmanTable.StdACLuminance, JPEGHuffmanTable.StdACChrominance };
		if (quantizationTables != null) {
			// Obtain default image metadata data, in native JPEG format
			metadata = imageWriter.getDefaultImageMetadata(ImageTypeSpecifier.createFromRenderedImage(image1),
					null);
			IIOMetadataNode nativeMeta = (IIOMetadataNode) metadata.getAsTree("javax_imageio_jpeg_image_1.0");

			// Update dqt to values from mQMatrix
			NodeList dqtables = nativeMeta.getElementsByTagName("dqtable");
			for (int i = 0; i < dqtables.getLength(); i++) {
				IIOMetadataNode dqt = (IIOMetadataNode) dqtables.item(i);
				int dqtId = Integer.parseInt(dqt.getAttribute("qtableId"));
				dqt.setUserObject(quantizationTables[dqtId]);
			}

			// For some reason, we need dht explicitly defined, when using
			// MODE_COPY_FROM_METADATA...
			NodeList dhtables = nativeMeta.getElementsByTagName("dhtable");

			// Update dht
			for (int i = 0; i < dhtables.getLength(); i++) {
				IIOMetadataNode dht = (IIOMetadataNode) dhtables.item(i);
				int dhtClass = Integer.parseInt(dht.getAttribute("class")); // 0: DC, 1: AC
				int dhtId = Integer.parseInt(dht.getAttribute("htableId"));

				dht.setUserObject(dhtClass == 0 ? huffmanDcTables[dhtId] : huffmanAcTables[dhtId]);
			}
			// Merge updated tree back (important!)
				metadata.mergeTree("javax_imageio_jpeg_image_1.0", nativeMeta);
		}

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageOutputStream imageOutputStream;
		imageOutputStream = ImageIO.createImageOutputStream(outputStream);
		imageWriter.setOutput(imageOutputStream);

		// See
		// http://docs.oracle.com/javase/6/docs/api/javax/imageio/metadata/doc-files/jpeg_metadata.html#tables
		JPEGImageWriteParam params = new JPEGImageWriteParam(null);
		params.setCompressionMode(
				metadata == null ? JPEGImageWriteParam.MODE_DEFAULT : JPEGImageWriteParam.MODE_COPY_FROM_METADATA); // Unless

		imageWriter.write(null, new IIOImage(image1, null, metadata), params);
		imageOutputStream.close();
		try (FileOutputStream fileOutputStream = new FileOutputStream(System.getProperty("user.dir") + "Flower.jpg")) {
			fileOutputStream.write(outputStream.toByteArray());
		}
	}
}
