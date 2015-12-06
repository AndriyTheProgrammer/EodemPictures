package laststar.ua.eodempictures;

/**
 * Created by andrew on 06.12.15.
 */
public class EodemResult {
    private String bmp1Signature;
    private String bmp2Signature;
    private int similarity;



    public String getBmp1Signature() {
        return bmp1Signature;
    }

    public String getBmp2Signature() {
        return bmp2Signature;
    }

    public int getSimilarity() {
        return similarity;
    }

    public void setBmp1Signature(String bmp1Signature) {
        this.bmp1Signature = bmp1Signature;
    }

    public void setBmp2Signature(String bmp2Signature) {
        this.bmp2Signature = bmp2Signature;
    }

    public void setSimilarity(int similarity) {
        this.similarity = similarity;
    }
}
