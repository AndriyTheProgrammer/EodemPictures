package laststar.ua.eodempictures;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.BitSet;

/**
 * Created by andrew on 04.12.15.
 */
public class SignatureCreator {

    private final static int RESULT_BITMAP_SIDE_SIZE = 8;

    public static Similarity getSimilarityForBitmap(Bitmap bitmap){
        Similarity similarity = new Similarity();
        long startTime = System.currentTimeMillis();
        Bitmap bitmap1 = toGrayScale(minimizeBitmap(bitmap));
        bitmap.recycle();
        bitmap = null;
        ColorsChain colorsChain = calculateAverageColor(bitmap1);

        similarity.setSignature(getHash(createBitsChain(colorsChain)));

        Log.d("Testing", "Calculations finished, time: " + (System.currentTimeMillis() - startTime));


        return similarity;
    }

    private static Bitmap minimizeBitmap(Bitmap bitmap){
        return Bitmap.createScaledBitmap(bitmap, RESULT_BITMAP_SIDE_SIZE, RESULT_BITMAP_SIDE_SIZE, false);
    }

    private static Bitmap toGrayScale(Bitmap src){
        ColorMatrix bwMatrix = new ColorMatrix();
        bwMatrix.setSaturation(0);
        final ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(bwMatrix);
        Bitmap rBitmap = src.copy(Bitmap.Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);
        Canvas myCanvas = new Canvas(rBitmap);
        myCanvas.drawBitmap(rBitmap, 0, 0, paint);

        src.recycle();
        src = null;

        return rBitmap;
    }

    private static ColorsChain calculateAverageColor(Bitmap bitmap){
        ColorsChain colorsChain = new ColorsChain(RESULT_BITMAP_SIDE_SIZE * RESULT_BITMAP_SIDE_SIZE);
        int averageColor = 0;
        int pixelCount = 0;

        for (int y = 0; y < bitmap.getHeight(); y++)
        {
            for (int x = 0; x < bitmap.getWidth(); x++)
            {
                int c = bitmap.getPixel(x, y);
                colorsChain.addColor(Color.red(c));
                averageColor += colorsChain.getColorsChain()[pixelCount];
                pixelCount++;
            }
        }
        // calculate average of bitmap r,g,b values
        averageColor = averageColor / pixelCount;
        colorsChain.setAverageColor(averageColor);
        return colorsChain;
    }

    private static boolean[] createBitsChain(ColorsChain colorsChain){
        boolean[] bitsChain = new boolean[RESULT_BITMAP_SIDE_SIZE * RESULT_BITMAP_SIDE_SIZE];
        int i = 0;
        for (int color : colorsChain.getColorsChain()){
            bitsChain[i] = color >= colorsChain.getAverageColor();
            i++;
        }

        return bitsChain;
    }

    private static String getHash(boolean[] bits){
        String binary = "";
        byte[] bytes = new byte[8];
        for (boolean bit : bits){
            binary = binary + (bit?"1":"0");

        }
        byte[] bval = null;
        bval = new BigInteger(binary, 2).toByteArray();

        String hash = toHex(bval);


        return hash;

    }

    public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }

    public static int getLevenshteinDistance(String s, String t) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }


        int n = s.length(); // length of s
        int m = t.length(); // length of t

        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        }

        if (n > m) {
            // swap the input strings to consume less memory
            String tmp = s;
            s = t;
            t = tmp;
            n = m;
            m = t.length();
        }

        int p[] = new int[n+1]; //'previous' cost array, horizontally
        int d[] = new int[n+1]; // cost array, horizontally
        int _d[]; //placeholder to assist in swapping p and d

        // indexes into strings s and t
        int i; // iterates through s
        int j; // iterates through t

        char t_j; // jth character of t

        int cost; // cost

        for (i = 0; i<=n; i++) {
            p[i] = i;
        }

        for (j = 1; j<=m; j++) {
            t_j = t.charAt(j-1);
            d[0] = j;

            for (i=1; i<=n; i++) {
                cost = s.charAt(i-1)==t_j ? 0 : 1;
                // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
                d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1),  p[i-1]+cost);
            }

            // copy current distance counts to 'previous row' distance counts
            _d = p;
            p = d;
            d = _d;
        }

        // our last action in the above loop was to switch d and p, so p now
        // actually has the most recent cost counts
        return p[n];
    }



}
