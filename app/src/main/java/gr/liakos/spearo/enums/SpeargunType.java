package gr.liakos.spearo.enums;

import gr.liakos.spearo.R;

public enum SpeargunType {

    NO_TYPE(0, R.string.select_type_spinner_text),

    RUBBER_BAND(1, R.string.rubber_band_spinner_text),

    ROLLER(2, R.string.roller_spinner_text),

    INVERT(3, R.string.invert_spinner_text),

    PNEUMATIC(4, R.string.pneumatic_spinner_text);


    private final int id;
    private final int textId;

    SpeargunType(int id, int textId) {
        this.id = id;
        this.textId = textId;
    }

    public static SpeargunType ofPosition(int position){
        for (SpeargunType type : SpeargunType.values()) {
            if (position == type.id){
                return type;
            }
        }

        return null;
    }

    public int getId() {
        return id;
    }

    public int getTextId() {
        return textId;
    }
}
