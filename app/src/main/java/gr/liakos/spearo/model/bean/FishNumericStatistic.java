package gr.liakos.spearo.model.bean;

import gr.liakos.spearo.model.object.Fish;

public class FishNumericStatistic {

    int fishId;

    Fish fish;

    int totalCatches;

    public int getFishId() {
        return fishId;
    }

    public void setFishId(int fishId) {
        this.fishId = fishId;
    }

    public Fish getFish() {
        return fish;
    }

    public void setFish(Fish fish) {
        this.fish = fish;
    }

    public int getTotalCatches() {
        return totalCatches;
    }

    public void setTotalCatches(int totalCatches) {
        this.totalCatches = totalCatches;
    }
}
