package backend;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

public class PhotoScaler {

	public static byte[] resizeByteArray(int height, int width, byte[] photo) {
		try {
			
			ByteArrayInputStream in = new ByteArrayInputStream(photo);			
			BufferedImage img = ImageIO.read(in);
	
			Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0,0,0), null);
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			ImageIO.write(imageBuff, "jpg", buffer);
			
			byte[] newBuffer = buffer.toByteArray();
			
			return newBuffer;
			
		}catch(IOException e) {
			
			System.out.println(e);
		}
	
		return photo;

	}
}
