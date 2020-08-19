package ch1.lissajous;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by crownus on 29/10/2016.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        lissajous(System.out);
    }

    public static void lissajous(OutputStream output) throws Exception {
        final int cycles = 5;
        final double res = 0.001;
        final int size = 100;
        final int nframes = 64;
        final int delay = 100;
        double phase = 0.0;
        SecureRandom random = new SecureRandom();
        double freq = random.nextFloat() * 3.0;
        List<GifFrame> frames = new ArrayList<>();
        for (int i = 0; i < nframes; i++) {
            BufferedImage image = new BufferedImage(2 * size + 1, 2 * size + 1, BufferedImage.TYPE_BYTE_INDEXED);
            Graphics g = image.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, image.getWidth(), image.getHeight());
            WritableRaster raster = image.getRaster();
            ColorModel model = image.getColorModel();
            Color black = Color.BLACK;
            int argb = black.getRGB();
            Object colorData = model.getDataElements(argb, null);
            for (double t = 0.0; t < cycles * 2 * Math.PI; t += res) {
                double x = Math.sin(t);
                double y = Math.sin(t * freq + phase);
                raster.setDataElements(size + (int) (x * size + 0.5), size + (int) (y * size + 0.5), colorData);
            }
            phase += 0.1;
            GifFrame frame = new GifFrame(image, delay);
            frames.add(frame);
        }
        GifUtil.saveAnimatedGIF(output, frames, nframes);
        output.close();
    }
}
