import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class PartialDerivativeDetector {
    public static void main(String[] args) {
        try {
            File input = new File("unnamed.jpg");
            BufferedImage image = ImageIO.read(input);
            int width = image.getWidth();
            int height = image.getHeight();

            BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

            // 轉為灰階強度矩陣
            double[][] gray = new double[width][height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color c = new Color(image.getRGB(x, y));
                    gray[x][y] = (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue());
                }
            }

            // 計算偏微分 df/dx
            for (int y = 0; y < height; y++) {
                for (int x = 1; x < width - 1; x++) {
                    // 公式: f(x+1, y) - f(x-1, y)
                    // 對應投影片中展示的 [-1 0 1] 核心或差分概念
                    double dfdx = (gray[x + 1][y] - gray[x - 1][y]);

                    // 關鍵：將微分值偏移 128
                    // 沒變化 = 128 (灰)
                    // 正變化 = >128 (亮)
                    // 負變化 = <128 (暗)
                    int pixelValue = (int) (dfdx + 128);

                    // 確保數值在 0-255 範圍內
                    pixelValue = Math.min(255, Math.max(0, pixelValue));

                    output.setRGB(x, y, new Color(pixelValue, pixelValue, pixelValue).getRGB());
                }
            }

            ImageIO.write(output, "png", new File("partial_derivative_x.png"));
            System.out.println("成功產生偏微分圖檔: partial_derivative_x.png");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}