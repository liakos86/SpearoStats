package gr.liakos.spearo.util;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import gr.liakos.spearo.model.object.Fish;

public class StringUtils {

    static String stringContainedIn(List<String> accepted, String source){
        for (String acceptedValue : accepted) {
            if (acceptedValue.contains(source)){
                return acceptedValue;
            }
        }
        return null;
    }

    public static String removeDiacriticalMarks(String string) {
        return Normalizer.normalize(string, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public static boolean stringContainedInFishNames(List<String> allFish, String fishName) {
        return stringContainedIn(allFish, fishName) != null;
    }

    public static String fishNameContaining(List<Fish> allFish, String fishName) {
        List<String> allFishNames = new ArrayList<>();
        for (Fish fish : allFish
             ) {
            allFishNames.add(fish.getLatinName().toLowerCase(Locale.ENGLISH));
            allFishNames.add(removeDiacriticalMarks(fish.getCommonName().toLowerCase(Locale.getDefault())));
            allFishNames.add(removeDiacriticalMarks(fish.getSecondaryCommonNameForSearch().toLowerCase(new Locale("el"))));
        }

        String fishNameFixed = removeDiacriticalMarks(fishName.toLowerCase(Locale.getDefault()));
        return stringContainedIn(allFishNames, fishNameFixed);
    }

}
