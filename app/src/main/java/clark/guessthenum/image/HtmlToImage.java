package clark.guessthenum.image;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JEditorPane;

public class HtmlToImage {
	public static void convert(String html) throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();

		// http://www.java2s.com/Tutorials/Java/Graphics_How_to/Image/Render_HTML_and_save_to_Image.htm
		BufferedImage image = GraphicsEnvironment
			.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice()
			.getDefaultConfiguration()
			.createCompatibleImage(200, 100);

		Graphics g = image.createGraphics();

		JEditorPane jpane = new JEditorPane("text/html", html);
		jpane.setSize(new Dimension(200, 100));
		jpane.print(g);

		ImageIO.write(image, "png", bytes);
		FileOutputStream out = new FileOutputStream("./image.png");
		out.write(bytes.toByteArray());

		out.close();
		bytes.close();
	}
}
