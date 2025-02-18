package base.document;

import base.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import static report.ExtentManager.captureImage;

public class ImageUtil {
    private static final Logger logger = LogManager.getLogger(ImageUtil.class);

    private ImageUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean compareAndHighlight(final BufferedImage img1, final BufferedImage img2,
                                              String fileName, boolean highlight, int colorCode) {

        // Get pixels and dimensions of the images
        final int w = img1.getWidth();
        final int h = img1.getHeight();
        final int[] p1 = img1.getRGB(0, 0, w, h, null, 0, w);
        final int[] p2 = img2.getRGB(0, 0, w, h, null, 0, w);

        // Compare the images
        if (!(java.util.Arrays.equals(p1, p2))) {
            logger.warn("Image compared - does not match");
            if (highlight) {
                for (int i = 0; i < p1.length; i++) {
                    if (p1[i] != p2[i]) {
                        p1[i] = colorCode;
                    }
                }
                final BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                out.setRGB(0, 0, w, h, p1, 0, w);
                BaseTest.createStep("Image compared - does not match", false, true, out);
            }
            return false;
        }
        return true;
    }

    public static void saveImage(BufferedImage image, String file) {
        try {
            File outputfile = new File(file);
            ImageIO.write(image, "png", outputfile);
        } catch (Exception e) {
            logger.error("Error while saving image: {}", e.getMessage());
        }
    }
}
