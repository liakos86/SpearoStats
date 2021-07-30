package gr.liakos.spearo.model.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

public class FishStatisticViewHolder extends  FishStatisticViewHolderBasic{

	private ViewListener carouselListener;

	private View lineChartMonths;

	private TextView lineChartMonthsTitle;

	private View lineChartWinter;

	private View lineChartSummer;

	private CarouselView carouselView;

	public CarouselView getCarouselView() {
		return carouselView;
	}

	public void setCarouselView(CarouselView carouselView) {
		this.carouselView = carouselView;
	}

	public View getLineChartMonths() {
		return lineChartMonths;
	}

	public void setLineChartMonths(View lineChartMonths) {
		this.lineChartMonths = lineChartMonths;
	}

	public View getLineChartWinter() {
		return lineChartWinter;
	}

	public void setLineChartWinter(View lineChartWinter) {
		this.lineChartWinter = lineChartWinter;
	}

	public View getLineChartSummer() {
		return lineChartSummer;
	}

	public void setLineChartSummer(View lineChartSummer) {
		this.lineChartSummer = lineChartSummer;
	}

	public TextView getLineChartMonthsTitle() {
		return lineChartMonthsTitle;
	}

	public void setLineChartMonthsTitle(TextView lineChartMonthsTitle) {
		this.lineChartMonthsTitle = lineChartMonthsTitle;
	}

	public ViewListener getCarouselListener() {
		return carouselListener;
	}

	public void setCarouselListener(ViewListener carouselListener) {
		this.carouselListener = carouselListener;
	}
}
