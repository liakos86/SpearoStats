package gr.liakos.spearo.model.viewholder;

import android.widget.ImageView;
import android.widget.TextView;

public class FishViewHolder {
	
	private ImageView fishIcon;
	
	private TextView commonName;
	
	private TextView fishFamily;

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
		
}
