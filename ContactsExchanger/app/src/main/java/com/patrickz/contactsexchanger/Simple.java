package com.patrickz.contactsexchanger;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.EnumMap;
import java.util.Map;

public class Simple
{
    private final static String LOGTAG = MainActivity.LOGPATTERN + "Simple";

    public static Bitmap encodeAsBitmap(String text, int width, int height) throws Exception
    {
        Log.d(LOGTAG, "QRCode gen. started.");

        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

//        Log.d("PatrickPups", "createBitmap start.");
//
//        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//
//        Log.d("PatrickPups", "createBitmap done.");
//        Log.d("PatrickPups", "forloop start.");
//
//        for (int x = 0; x < width; x++)
//        {
//            for (int y = 0; y < height; y++)
//            {
//                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
//            }
//        }
//
//        Log.d("PatrickPups", "forloop done.");
//
//        return bmp;

        final int widthm  = matrix.getWidth();
        final int heightm = matrix.getHeight();

        int[] pixels = new int[widthm * heightm];

        for (int y = 0; y < heightm; y++)
        {
            int offset = y * widthm;

            for (int x = 0; x < widthm; x++)
            {
                pixels[offset + x] = matrix.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(widthm, heightm, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, widthm, 0, 0, widthm, heightm);

        Log.d(LOGTAG, "QRCode gen. done.");

        return bitmap;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float pixels)
    {
        // http://stackoverflow.com/questions/2459916/how-to-make-an-imageview-with-rounded-corners

        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap output = Bitmap.createBitmap(
            width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect rect  = new Rect(0, 0, width, height);
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, pixels, pixels, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static GradientDrawable roundedCorners(int radius, String color, int strockSize, String strockColor)
    {
        GradientDrawable gdrawable = new GradientDrawable();
        gdrawable.setCornerRadius(radius);
        gdrawable.setColor(Color.parseColor(color));
        gdrawable.setStroke(strockSize, Color.parseColor(strockColor));

        return gdrawable;
    }

    public static GradientDrawable roundedCorners(int radius, String color)
    {
        GradientDrawable gdrawable = new GradientDrawable();
        gdrawable.setCornerRadius(radius);
        gdrawable.setColor(Color.parseColor(color));

        return gdrawable;
    }
}
