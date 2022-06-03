package gr.liakos.spearo.def;

import gr.liakos.spearo.enums.FishingSessionsState;

public interface SpearoDataChangeListener {

    void notifyChanges(FishingSessionsState state);

}
