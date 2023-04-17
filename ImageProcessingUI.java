package java1;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ImageProcessingUI extends JFrame implements ActionListener {

	// UI components
	private JLabel imageLabel;
	private JButton loadButton;
	private JButton grayscaleButton;
	private JButton invertButton;
	private JButton sepiaButton;
	private JTextArea consoleTextArea;

	// Input image file
	private File inputFile;

	public ImageProcessingUI() {
		super("Image Processing Application");

		// Create UI components
		imageLabel = new JLabel();
		imageLabel.setPreferredSize(new Dimension(400, 400));
		imageLabel.setBorder(BorderFactory.createTitledBorder("Original Image"));

		loadButton = new JButton("Load Image");
		loadButton.addActionListener(this);

		grayscaleButton = new JButton("Grayscale");
		grayscaleButton.addActionListener(this);

		invertButton = new JButton("Invert");
		invertButton.addActionListener(this);

		sepiaButton = new JButton("Sepia");
		sepiaButton.addActionListener(this);

		consoleTextArea = new JTextArea(5, 20);
		consoleTextArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(consoleTextArea);

		// Create UI layout
		JPanel imagePanel = new JPanel(new FlowLayout());
		imagePanel.add(imageLabel);

		JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
		buttonPanel.add(loadButton);
		buttonPanel.add(grayscaleButton);
		buttonPanel.add(invertButton);
		buttonPanel.add(sepiaButton);

		JPanel consolePanel = new JPanel(new BorderLayout());
		consolePanel.setBorder(BorderFactory.createTitledBorder("Console"));
		consolePanel.add(scrollPane);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(imagePanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.WEST);
		mainPanel.add(consolePanel, BorderLayout.SOUTH);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Set the main UI panel
		setContentPane(mainPanel);

		// Set the frame properties
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 500);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loadButton) {
			// Open a file chooser dialog to select an image file
			JFileChooser fileChooser = new JFileChooser();
			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				inputFile = fileChooser.getSelectedFile();
				try {
					// Load the selected image file
					BufferedImage img = ImageIO.read(inputFile);
					Image scaledImg = img.getScaledInstance(400, 400, Image.SCALE_SMOOTH);
					imageLabel.setIcon(new ImageIcon(scaledImg));
					consoleTextArea.append("Image loaded: " + inputFile.getName() + "\n");
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(this, "Failed to load image file", "Error",
							JOptionPane.ERROR_MESSAGE);
					consoleTextArea.append("Failed to load image file: " + inputFile.getName() + "\n");
				}
			}
		} else if (inputFile == null) {
			// If no image is loaded, display an error message
			JOptionPane.showMessageDialog(this, "No image selected", "Error", JOptionPane.ERROR_MESSAGE);
			consoleTextArea.append("No image selected\n");
			return;
		}
		// Perform the selected image processing operation
		BufferedImage processedImg = null;
		if (e.getSource() == grayscaleButton) {
			processedImg = applyGrayscaleFilter(inputFile);
		} else if (e.getSource() == invertButton) {
			processedImg = applyInvertFilter(inputFile);
		} else if (e.getSource() == sepiaButton) {
			processedImg = applySepiaFilter(inputFile);
		}

		// Update the displayed image with the processed image
		if (processedImg != null) {
			Image scaledImg = processedImg.getScaledInstance(400, 400, Image.SCALE_SMOOTH);
			imageLabel.setIcon(new ImageIcon(scaledImg));
			consoleTextArea.append("Image processed: " + e.getActionCommand() + "\n");
		} else {
			consoleTextArea.append("Image processing failed: " + e.getActionCommand() + "\n");
		}
	}

	/**
	 * Applies a grayscale filter to the input image.
	 *
	 * @param file the input image file
	 * @return the processed image
	 */
	private BufferedImage applyGrayscaleFilter(File file) {
		BufferedImage img = null;
		try {
			// Read the input image file
			img = ImageIO.read(file);

			// Process the image pixels
			for (int i = 0; i < img.getWidth(); i++) {
				for (int j = 0; j < img.getHeight(); j++) {
					// Get the RGB values for the current pixel
					int rgb = img.getRGB(i, j);
					int r = (rgb >> 16) & 0xFF;
					int g = (rgb >> 8) & 0xFF;
					int b = rgb & 0xFF;

					// Calculate the grayscale value
					int grayscale = (int) (0.21 * r + 0.72 * g + 0.07 * b);

					// Set the new grayscale RGB value for the pixel
					int grayRgb = (grayscale << 16) | (grayscale << 8) | grayscale;
					img.setRGB(i, j, grayRgb);
				}
			}
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "Failed to process image", "Error", JOptionPane.ERROR_MESSAGE);
			consoleTextArea.append("Failed to process image: " + file.getName() + "\n");
		}
		return img;
	}

/**
 * Applies an invert filter to the input image.
 *
 * @param file the input image file
 * @return the processed image
 */
private BufferedImage applyInvertFilter(File file) {
    BufferedImage img = null;
    try {
        // Read the input image file
        img = ImageIO.read(file);

        // Process the image pixels
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                // Get the RGB values for the current pixel
                int rgb = img.getRGB(i, j);
                int r = 255 - (rgb >> 16) & 0xFF;
                int g = 255 - (rgb >> 8) & 0xFF;
                int b = 255 - rgb & 0xFF;

                // Set the new inverted RGB value for the pixel
                int invertedRgb = (r << 16) | (g << 8) | b;
                img.setRGB(i, j, invertedRgb);
            }
            }
            } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to process image", "Error", JOptionPane.ERROR_MESSAGE);
            consoleTextArea.append("Failed to process image: " + file.getName() + "\n");
            }
            return img;
            }

	/**

            Applies a sepia filter to the input image.

            @param file the input image file

            @return the processed image
            */
            private BufferedImage applySepiaFilter(File file) {
            BufferedImage img = null;
            try {
            // Read the input image file
            img = ImageIO.read(file);
            // Process the image pixels
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    // Get the RGB values for the current pixel
                    int rgb = img.getRGB(i, j);
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = rgb & 0xFF;

                    // Calculate the sepia values for each color channel
                    int sepiaR = (int) (0.393 * r + 0.769 * g + 0.189 * b);
                    int sepiaG = (int) (0.349 * r + 0.686 * g + 0.168 * b);
                    int sepiaB = (int) (0.272 * r + 0.534 * g + 0.131 * b);

                    // Cap the values at 255
                    sepiaR = Math.min(255, sepiaR);
                    sepiaG = Math.min(255, sepiaG);
                    sepiaB = Math.min(255, sepiaB);

                    // Set the new sepia RGB value for the pixel
                    int sepiaRgb = (sepiaR << 16) | (sepiaG << 8) | sepiaB;
                    img.setRGB(i, j, sepiaRgb);
                }
            }
