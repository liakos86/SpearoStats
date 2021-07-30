package gr.liakos.spearo.model.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;

public class FishStatisticViewHolderFull extends  FishStatisticViewHolderBasic{

	private LinearLayout globalLayout;

	private TextView lineChartMonthsTitle;

	private TextView lineChartSummerTitle;

	private TextView lineChartWinterTitle;

	private View lineChartMonths;

	private View lineChartWinter;

	private View lineChartSummer;

	private CarouselView carouselView;

	private RelativeLayout noPremiumLayout;

	public RelativeLayout getNoPremiumLayout() {
		return noPremiumLayout;
	}

	public void setNoPremiumLayout(RelativeLayout noPremiumLayout) {
		this.noPremiumLayout = noPremiumLayout;
	}

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

	public TextView getLineChartSummerTitle() {
		return lineChartSummerTitle;
	}

	public void setLineChartSummerTitle(TextView lineChartSummerTitle) {
		this.lineChartSummerTitle = lineChartSummerTitle;
	}

	public TextView getLineChartWinterTitle() {
		return lineChartWinterTitle;
	}

	public void setLineChartWinterTitle(TextView lineChartWinterTitle) {
		this.lineChartWinterTitle = lineChartWinterTitle;
	}

	public LinearLayout getGlobalLayout() {
		return globalLayout;
	}

	public void setGlobalLayout(LinearLayout globalLayout) {
		this.globalLayout = globalLayout;
	}
}
