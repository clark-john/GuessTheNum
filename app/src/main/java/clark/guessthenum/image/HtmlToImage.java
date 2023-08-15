package clark.guessthenum.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xhtmlrenderer.swing.Java2DRenderer;

public class HtmlToImage {
	public static void convert(String html) throws Exception {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();

		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

		Document doc = builder.parse(new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8"))));

		Java2DRenderer renderer = new Java2DRenderer(doc, 200, 100);
		BufferedImage image = renderer.getImage();

		ImageIO.write(image, "png", bytes);
		
		FileOutputStream out = new FileOutputStream("./image.png");
		out.write(bytes.toByteArray());

		out.close();
		bytes.close();
	}
}
