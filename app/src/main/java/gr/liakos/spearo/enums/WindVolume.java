package gr.liakos.spearo.enums;

import java.util.ArrayList;
import java.util.List;

public enum WindVolume {

    NOT_KNOWN(0),

    ONE(1),

    TWO(2),

    THREE(3),

    FOUR(4),

    FIVE(5),

    SIX(6),

    SEVEN(7),

    EIGHT(8);

    private final int position;

    WindVolume(int position){
        this.position = position;
    }

    public static WindVolume ofPosition(int position){
        for (WindVolume windVolume : WindVolume.values()){
            if (windVolume.position == position){
                return windVolume;
            }
        }

        return null;
    }

    public static List<WindVolume> forSpinner(){
        List<WindVolume> forSpinner = new ArrayList<>();
        forSpinner.add(NOT_KNOWN);
        forSpinner.add(ONE);
        forSpinner.add(TWO);
        forSpinner.add(THREE);
        forSpinner.add(FOUR);
        forSpinner.add(FIVE);
        forSpinner.add(SIX);
        forSpinner.add(SEVEN);
        forSpinner.add(EIGHT);
        return forSpinner;
    }

    public int getPosition() {
        return position;
    }

}
