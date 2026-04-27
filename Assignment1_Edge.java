import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Assignment1_Edge {
    public static void main(String[] args) {
        try {
            File inputFile = new File("aa.jpg");
            BufferedImage img = ImageIO.read(inputFile);
            int width = img.getWidth();
            int height = img.getHeight();
            
            BufferedImage edgeImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            int[][] gray = new int[width][height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int p = img.getRGB(x, y);
                    int r = (p >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    int b = p & 0xff;
                    gray[x][y] = (r + g + b) / 3;
                }
            }

            // 3. 終極升級：同時使用 Sobel X 與 Sobel Y
            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    // 計算 X 方向梯度 (偵測垂直線條)
                    int ix = (gray[x+1][y-1] - gray[x-1][y-1]) +
                             (2 * (gray[x+1][y] - gray[x-1][y])) +
                             (gray[x+1][y+1] - gray[x-1][y+1]);
                    
                    // 計算 Y 方向梯度 (偵測水平線條)
                    int iy = (gray[x-1][y-1] + 2 * gray[x][y-1] + gray[x+1][y-1]) -
                             (gray[x-1][y+1] + 2 * gray[x][y+1] + gray[x+1][y+1]);
                    
                    // 結合雙向梯度計算總大小 (Magnitude)
                    int magnitude = (int) Math.sqrt(ix * ix + iy * iy);
                    
                    // 限制範圍在 0-255 (黑底白線)
                    if (magnitude > 255) magnitude = 255;
                    if (magnitude < 0) magnitude = 0;
                    
                    int newPixel = (magnitude << 16) | (magnitude << 8) | magnitude;
                    edgeImg.setRGB(x, y, newPixel);
                }
            }

            File outputFile = new File("edge_result.jpg");
            ImageIO.write(edgeImg, "jpg", outputFile);
            System.out.println("=== Assignment 1: Edge Detection (Full Sobel) ===");
            System.out.println("影像處理完成！極致清晰的黑底白線邊緣圖已儲存為 edge_result.jpg");

        } catch (Exception e) {
            System.out.println("發生錯誤，請確認 aa.jpg 是否在同一個資料夾內。");
            e.printStackTrace();
        }
    }
}