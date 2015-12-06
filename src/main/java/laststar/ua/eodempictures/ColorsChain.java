package laststar.ua.eodempictures;

/**
 * Created by andrew on 05.12.15.
 */
public class ColorsChain {

    int[] colorsChain;
    int averageColor;
    int i = 0;

    public ColorsChain(int chainSize){
        colorsChain = new int[chainSize];
    }

    public void addColor(int color){
        colorsChain[i] = color;
        i++;
    }

    public int[] getColorsChain() {
        return colorsChain;
    }

    public int getAverageColor() {
        return averageColor;
    }

    public void setAverageColor(int averageColor) {
        this.averageColor = averageColor;
    }
}
