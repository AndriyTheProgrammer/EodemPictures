package laststar.ua.eodempictures;

import android.graphics.Bitmap;

/**
 * Created by andrew on 04.12.15.
 */
public class Eodem {

    private Bitmap bmp1, bmp2;
    private String signature1, signature2;

    private Eodem(Bitmap bmp1){
        this.bmp1 = bmp1;
    }

    private Eodem(String signature1){
        this.signature1 = signature1;
    }

    public static Eodem compare(String signature){
        if (signature == null || signature.length() == 0) throw new IllegalArgumentException("Signature of compared image cannot be empty");
        return new Eodem(signature);
    }

    public static Eodem compare(Bitmap bitmap){
        if (bitmap == null) throw new IllegalArgumentException("Compared image cannot be null");
        return new Eodem(bitmap);
    }

    public Eodem with(String signature){
        if (signature == null || signature.length() == 0) throw new IllegalArgumentException("Signature of compared image cannot be empty");
        signature2 = signature;
        return this;
    }

    public Eodem with(Bitmap bitmap){
        if (bitmap == null) throw new IllegalArgumentException("Compared image cannot be null");
        bmp2 = bitmap;
        return this;
    }

//    public void getResult(CompareListener compareListener){
//
//    }

    public EodemResult getResult(){
        if (signature1 == null) signature1 = SignatureCreator.getSimilarityForBitmap(bmp1).getSignature();

        if (signature2 == null) signature2 = SignatureCreator.getSimilarityForBitmap(bmp2).getSignature();


        EodemResult result = new EodemResult();
        result.setBmp1Signature(signature1);
        result.setBmp2Signature(signature2);
        result.setSimilarity(SignatureCreator.getLevenshteinDistance(signature1, signature2));
        recycleResources();
        return result;
    }

    private void recycleResources() {
        signature1 = null;
        signature2 = null;
        bmp1 = null;
        bmp2 = null;
    }

}
