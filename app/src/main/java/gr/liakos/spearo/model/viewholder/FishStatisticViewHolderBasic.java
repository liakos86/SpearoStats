package gr.liakos.spearo.model.viewholder;

import android.widget.ImageView;
import android.widget.TextView;


public class FishStatisticViewHolderBasic {
	
	private ImageView fishIcon;
	
	private TextView commonName;
	
	private TextView fishFamily;
	
	private TextView recordWeight;
	
	private TextView worldRecordWeight;
	
	private TextView avgWeight;
	
	private TextView avgDepth;
	
	private TextView totalCatches;
	
	private TextView mostCommonSummerHour;
	
	private TextView mostCommonWinterHour;

	public ImageView getFishIcon() {
		return fishIcon;
	}

	public void setFishIcon(ImageView fishIcon) {
		this.fishIcon = fishIcon;
	}

	public TextView getCommonName() {
		return commonName;
	}

	public void setCommonName(TextView commonName) {
		this.commonName = commonName;
	}
	
	public TextView getFishFamily() {
		return fishFamily;
	}

	public void setFishFamily(TextView fishFamily) {
		this.fishFamily = fishFamily;
	}

	public TextView getRecordWeight() {
		return recordWeight;
	}

	public void setRecordWeight(TextView recordWeight) {
		this.recordWeight = recordWeight;
	}

	public TextView getAvgWeight() {
		return avgWeight;
	}

	public void setAvgWeight(TextView avgWeight) {
		this.avgWeight = avgWeight;
	}

	public TextView getAvgDepth() {
		return avgDepth;
	}

	public void setAvgDepth(TextView avgDepth) {
		this.avgDepth = avgDepth;
	}

	public TextView getTotalCatches() {
		return totalCatches;
	}

	public void setTotalCatches(TextView totalCatches) {
		this.totalCatches = totalCatches;
	}

	public TextView getMostCommonSummerHour() {
		return mostCommonSummerHour;
	}

	public void setMostCommonSummerHour(TextView mostCommonSummerHour) {
		this.mostCommonSummerHour = mostCommonSummerHour;
	}
	
	public TextView getMostCommonWinterHour() {
		return mostCommonWinterHour;
	}

	public void setMostCommonWinterHour(TextView mostCommonWinterHour) {
		this.mostCommonWinterHour = mostCommonWinterHour;
	}

	public TextView getWorldRecordWeight() {
		return worldRecordWeight;
	}

	public void setWorldRecordWeight(TextView worldRecordWeight) {
		this.worldRecordWeight = worldRecordWeight;
	}
}
