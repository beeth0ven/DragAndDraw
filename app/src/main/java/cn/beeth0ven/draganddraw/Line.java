package cn.beeth0ven.draganddraw;

/**
 * Created by Air on 2017/2/18.
 */

public class Line {
    public float startX, startY, endX, endY;

    public float getAngle() {
        return (float) Math.atan2(endY - startY, endX - startX);
    }
}
