import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.encoder.SymbolShapeHint;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class BarCodeUtils {

    /**
     * 根据给定的信息生产条形码
     * @param msg 条形码内容
     * @param width 宽
     * @param height 高
     * @param out 输出流
     */
    public static void createQrCodeImg(String msg, int width, int height, OutputStream out) {
        String format = "png";
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0);
        hints.put(EncodeHintType.DATA_MATRIX_SHAPE, SymbolShapeHint.FORCE_SQUARE);
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(msg, BarcodeFormat.CODE_128, width, height, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, format, out);
        } catch (Exception e) {
            throw new RuntimeException("条码图片生成失败",e);
        }
    }
}

添加依赖：
<dependency>
   <groupId>com.google.zxing</groupId>
   <artifactId>core</artifactId>
   <version>3.2.0</version>
</dependency>
<dependency>
   <groupId>com.google.zxing</groupId>
   <artifactId>javase</artifactId>
   <version>3.2.0</version>
</dependency>

