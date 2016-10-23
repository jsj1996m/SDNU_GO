package cn.edu.sdnu.go.data;

/**
 * Created by jsj1996m on 2016/9/1.
 */
public class SettingData {

    private boolean is2d;
    private int defaultPointColor;
    private boolean isAlwaysShowName;


    public SettingData(boolean is2d,int defaultPointColor,boolean isAlwaysShowName){

        this.is2d = is2d;
        this.defaultPointColor = defaultPointColor;
        this.isAlwaysShowName = isAlwaysShowName;
    }

    public boolean is2d() {
        return is2d;
    }

    public void setIs2d(boolean is2d) {
        this.is2d = is2d;
    }

    public int getDefaultPointColor() {
        return defaultPointColor;
    }

    public void setDefaultPointColor(int defaultPointColor) {
        this.defaultPointColor = defaultPointColor;
    }

    public boolean isAlwaysShowName() {
        return isAlwaysShowName;
    }

    public void setAlwaysShowName(boolean alwaysShowName) {
        isAlwaysShowName = alwaysShowName;
    }
}
