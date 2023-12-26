package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class TextGraphicsConverterImpl implements TextGraphicsConverter {
    private int width;
    private int height;
    private double maxRatio;
    private TextColorSchema schema;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        double ratio;
        if (img.getHeight() > img.getWidth()) {
            ratio = img.getHeight() * 1.0 / img.getWidth();
        } else {
            ratio = img.getWidth() * 1.0 / img.getHeight();
        }
        if (this.maxRatio != 0.0 && this.maxRatio < ratio) {
            throw new BadImageSizeException(ratio, this.maxRatio);
        }
        changeWidthHeight(img);
        Image scaledImage = img.getScaledInstance(this.width, this.height, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(this.width, this.height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();
        if (this.schema == null) {
            this.schema = new TextColorSchemaImpl();
        }
        char[][] charsImg = new char[bwRaster.getHeight()][bwRaster.getWidth()];
        for (int h = 0; h < bwRaster.getHeight(); h++) {
            for (int w = 0; w < bwRaster.getWidth(); w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                charsImg[h][w] = c;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < charsImg.length; i++) {
            for (int j = 0; j < charsImg[i].length; j++) {
                stringBuilder.append(charsImg[i][j]).append(charsImg[i][j]);
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.width = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.height = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }

    private void changeWidthHeight(BufferedImage img) {
        int newWidth;
        int newHeight;
        if (this.width == 0 || this.width > img.getWidth()) {
            newWidth = img.getWidth();
        } else {
            newWidth = this.width;
        }
        if (this.height == 0 || this.height > img.getHeight()) {
            newHeight = img.getHeight();
        } else {
            newHeight = this.height;
        }
        if (img.getWidth() > newWidth || img.getHeight() > newHeight) {
            double ratio = Math.max(img.getWidth() * 1.0 / newWidth, img.getHeight() * 1.0 / newHeight);
            this.width = (int) Math.round(img.getWidth() / ratio);
            this.height = (int) Math.round(img.getHeight() / ratio);
        } else {
            this.width = newWidth;
            this.height = newHeight;
        }
    }
}
