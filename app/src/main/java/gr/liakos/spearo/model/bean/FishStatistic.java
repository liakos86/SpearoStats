package gr.liakos.spearo.model.bean;

import gr.liakos.spearo.enums.Season;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FishStatistic {
	
	int fishId;
	
	int totalCatches;

	/**
	 * I do not want to count the non weighted in averages.
	 * Weight is not mandatory.
	 */
	int weightedCatches;
	
	double totalWeight;
	
	int depthedCatches;
	
	double totalDepth;
	
	double recordWeight;

	Map<Season, Map<Integer, Integer>> seasonCatchesPerHourDay;


	public FishStatistic(){
		initSeasonHoursMap();
	}

	public int getTotalCatches() {
		return totalCatches;
	}

	public void setTotalCatches(int totalCatches) {
		this.totalCatches = totalCatches;
	}

	public int getWeightedCatches() {
		return weightedCatches;
	}

	public void setWeightedCatches(int weightedCatches) {
		this.weightedCatches = weightedCatches;
	}

	public double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public int getDepthedCatches() {
		return depthedCatches;
	}

	public void setDepthedCatches(int depthedCatches) {
		this.depthedCatches = depthedCatches;
	}

	public double getTotalDepth() {
		return totalDepth;
	}

	public void setTotalDepth(double totalDepth) {
		this.totalDepth = totalDepth;
	}

	public double getRecordWeight() {
		return recordWeight;
	}

	public void setRecordWeight(double recordWeight) {
		this.recordWeight = recordWeight;
	}

	public int getFishId() {
		return fishId;
	}

	public void setFishId(int fishId) {
		this.fishId = fishId;
	}
	
	/*************/
	
	public void initSeasonHoursMap() {
		if (seasonCatchesPerHourDay != null){
			return;
		}

		seasonCatchesPerHourDay = new HashMap<Season, Map<Integer,Integer>>();
		HashMap<Integer, Integer> summerHours = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> winterHours = new HashMap<Integer, Integer>();
		for (int i=0; i<24; i++) {
			summerHours.put(i, 0);
			winterHours.put(i, 0);
		}
		seasonCatchesPerHourDay.put(Season.SUMMER, summerHours);
		seasonCatchesPerHourDay.put(Season.WINTER, winterHours);
		
	}
	
	
	
	int hourSummer0;
    int hourSummer1;
    int hourSummer2;
    int hourSummer3;
    int hourSummer4;
    int hourSummer5;
    int hourSummer6;
    int hourSummer7;
    int hourSummer8;
    int hourSummer9;
    int hourSummer10;
    int hourSummer11;
    int hourSummer12;
    int hourSummer13;
    int hourSummer14;
    int hourSummer15;
    int hourSummer16;
    int hourSummer17;
    int hourSummer18;
    int hourSummer19;
    int hourSummer20;
    int hourSummer21;
    int hourSummer22;
    int hourSummer23;
    
    
    int hourWinter0;
    int hourWinter1;
    int hourWinter2;
    int hourWinter3;
    int hourWinter4;
    int hourWinter5;
    int hourWinter6;
    int hourWinter7;
    int hourWinter8;
    int hourWinter9;
    int hourWinter10;
    int hourWinter11;
    int hourWinter12;
    int hourWinter13;
    int hourWinter14;
    int hourWinter15;
    int hourWinter16;
    int hourWinter17;
    int hourWinter18;
    int hourWinter19;
    int hourWinter20;
    int hourWinter21;
    int hourWinter22;
    int hourWinter23;
    
	public int getHourSummer0() {
		if (hourSummer0 == 0){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(0);
		}
		
		return hourSummer0;
	}
	public void setHourSummer0(int hourSummer0) {
		this.hourSummer0 = hourSummer0;
	}
	public int getHourSummer1() {
		if (hourSummer1 == 0){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(1);
		}
		return hourSummer1;
	}
	public void setHourSummer1(int hourSummer1) {
		this.hourSummer1 = hourSummer1;
	}
	public int getHourSummer2() {
		if (hourSummer2 == 0){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(2);
		}
		return hourSummer2;
	}
	public void setHourSummer2(int hourSummer2) {
		this.hourSummer2 = hourSummer2;
	}
	public int getHourSummer3() {
		if (hourSummer3 == 0){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(3);
		}
		return hourSummer3;
	}
	public void setHourSummer3(int hourSummer3) {
		this.hourSummer3 = hourSummer3;
	}
	public int getHourSummer4() {
		if (hourSummer4 == 0){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(4);
		}
		return hourSummer4;
	}
	public void setHourSummer4(int hourSummer4) {
		this.hourSummer4 = hourSummer4;
	}
	public int getHourSummer5() {
		if (hourSummer5 == 0){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(5);
		}
		return hourSummer5;
	}
	public void setHourSummer5(int hourSummer5) {
		this.hourSummer5 = hourSummer5;
	}
	public int getHourSummer6() {
		if (hourSummer6 == 0){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(6);
		}
		return hourSummer6;
	}
	public void setHourSummer6(int hourSummer6) {
		this.hourSummer6 = hourSummer6;
	}
	public int getHourSummer7() {
		if (hourSummer7 == 0){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(7);
		}
		return hourSummer7;
	}
	public void setHourSummer7(int hourSummer7) {
		this.hourSummer7 = hourSummer7;
	}
	public int getHourSummer8() {
		if (hourSummer8 == 0){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(8);
		}
		return hourSummer8;
	}
	public void setHourSummer8(int hourSummer8) {
		this.hourSummer8 = hourSummer8;
	}
	public int getHourSummer9() {
		if (hourSummer9 == 0){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(9);
		}
		return hourSummer9;
	}
	public void setHourSummer9(int hourSummer9) {
		this.hourSummer9 = hourSummer9;
	}
	public int getHourSummer10() {
		if (hourSummer10 == 0){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(10);
		}
		return hourSummer10;
	}
	public void setHourSummer10(int hourSummer10) {
		this.hourSummer10 = hourSummer10;
	}
	public int getHourSummer11() {
		if (hourSummer11 == 0){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(11);
		}
		return hourSummer11;
	}
	public void setHourSummer11(int hourSummer11) {
		this.hourSummer11 = hourSummer11;
	}
	public int getHourSummer12() {
		if (hourSummer0 == 12){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(12);
		}
		return hourSummer12;
	}
	public void setHourSummer12(int hourSummer12) {
		this.hourSummer12 = hourSummer12;
	}
	public int getHourSummer13() {
		if (hourSummer0 == 13){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(13);
		}
		return hourSummer13;
	}
	public void setHourSummer13(int hourSummer13) {
		this.hourSummer13 = hourSummer13;
	}
	public int getHourSummer14() {
		if (hourSummer14 == 0){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(14);
		}
		return hourSummer14;
	}
	public void setHourSummer14(int hourSummer14) {
		this.hourSummer14 = hourSummer14;
	}
	public int getHourSummer15() {
		if (hourSummer15 == 0){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(15);
		}
		return hourSummer15;
	}
	public void setHourSummer15(int hourSummer15) {
		this.hourSummer15 = hourSummer15;
	}
	public int getHourSummer16() {
		if (hourSummer16 == 0){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(16);
		}
		return hourSummer16;
	}
	public void setHourSummer16(int hourSummer16) {
		this.hourSummer16 = hourSummer16;
	}
	public int getHourSummer17() {
		if (hourSummer17 == 0){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(17);
		}
		return hourSummer17;
	}
	public void setHourSummer17(int hourSummer17) {
		this.hourSummer17 = hourSummer17;
	}
	public int getHourSummer18() {
		if (hourSummer18 == 0){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(18);
		}
		return hourSummer18;
	}
	public void setHourSummer18(int hourSummer18) {
		this.hourSummer18 = hourSummer18;
	}
	public int getHourSummer19() {
		if (hourSummer19 == 0){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(19);
		}
		return hourSummer19;
	}
	public void setHourSummer19(int hourSummer19) {
		this.hourSummer19 = hourSummer19;
	}
	public int getHourSummer20() {
		if (hourSummer20 == 0){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(20);
		}
		return hourSummer20;
	}
	public void setHourSummer20(int hourSummer20) {
		this.hourSummer20 = hourSummer20;
	}
	public int getHourSummer21() {
		if (hourSummer21 == 0){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(21);
		}
		return hourSummer21;
	}
	public void setHourSummer21(int hourSummer21) {
		this.hourSummer21 = hourSummer21;
	}
	public int getHourSummer22() {
		if (hourSummer22 == 0){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(22);
		}
		return hourSummer22;
	}
	public void setHourSummer22(int hourSummer22) {
		this.hourSummer22 = hourSummer22;
	}
	public int getHourSummer23() {
		if (hourSummer0 == 23){
			return getSeasonCatchesPerHourDay().get(Season.SUMMER).get(23);
		}
		return hourSummer23;
	}
	public void setHourSummer23(int hourSummer23) {
		this.hourSummer23 = hourSummer23;
	}
	
	
	
	/////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	
	public int getHourWinter0() {
		if (hourWinter0 == 0){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(0);
		}
		
		return hourWinter0;
	}
	public void setHourWinter0(int hourWinter0) {
		this.hourWinter0 = hourWinter0;
	}
	public int getHourWinter1() {
		if (hourWinter1 == 0){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(1);
		}
		return hourWinter1;
	}
	public void setHourWinter1(int hourWinter1) {
		this.hourWinter1 = hourWinter1;
	}
	public int getHourWinter2() {
		if (hourWinter2 == 0){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(2);
		}
		return hourWinter2;
	}
	public void setHourWinter2(int hourWinter2) {
		this.hourWinter2 = hourWinter2;
	}
	public int getHourWinter3() {
		if (hourWinter3 == 0){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(3);
		}
		return hourWinter3;
	}
	public void setHourWinter3(int hourWinter3) {
		this.hourWinter3 = hourWinter3;
	}
	public int getHourWinter4() {
		if (hourWinter4 == 0){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(4);
		}
		return hourWinter4;
	}
	public void setHourWinter4(int hourWinter4) {
		this.hourWinter4 = hourWinter4;
	}
	public int getHourWinter5() {
		if (hourWinter5 == 0){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(5);
		}
		return hourWinter5;
	}
	public void setHourWinter5(int hourWinter5) {
		this.hourWinter5 = hourWinter5;
	}
	public int getHourWinter6() {
		if (hourWinter6 == 0){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(6);
		}
		return hourWinter6;
	}
	public void setHourWinter6(int hourWinter6) {
		this.hourWinter6 = hourWinter6;
	}
	public int getHourWinter7() {
		if (hourWinter7 == 0){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(7);
		}
		return hourWinter7;
	}
	public void setHourWinter7(int hourWinter7) {
		this.hourWinter7 = hourWinter7;
	}
	public int getHourWinter8() {
		if (hourWinter8 == 0){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(8);
		}
		return hourWinter8;
	}
	public void setHourWinter8(int hourWinter8) {
		this.hourWinter8 = hourWinter8;
	}
	public int getHourWinter9() {
		if (hourWinter9 == 0){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(9);
		}
		return hourWinter9;
	}
	public void setHourWinter9(int hourWinter9) {
		this.hourWinter9 = hourWinter9;
	}
	public int getHourWinter10() {
		if (hourWinter10 == 0){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(10);
		}
		return hourWinter10;
	}
	public void setHourWinter10(int hourWinter10) {
		this.hourWinter10 = hourWinter10;
	}
	public int getHourWinter11() {
		if (hourWinter11 == 0){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(11);
		}
		return hourWinter11;
	}
	public void setHourWinter11(int hourWinter11) {
		this.hourWinter11 = hourWinter11;
	}
	public int getHourWinter12() {
		if (hourWinter0 == 12){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(12);
		}
		return hourWinter12;
	}
	public void setHourWinter12(int hourWinter12) {
		this.hourWinter12 = hourWinter12;
	}
	public int getHourWinter13() {
		if (hourWinter0 == 13){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(13);
		}
		return hourWinter13;
	}
	public void setHourWinter13(int hourWinter13) {
		this.hourWinter13 = hourWinter13;
	}
	public int getHourWinter14() {
		if (hourWinter14 == 0){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(14);
		}
		return hourWinter14;
	}
	public void setHourWinter14(int hourWinter14) {
		this.hourWinter14 = hourWinter14;
	}
	public int getHourWinter15() {
		if (hourWinter15 == 0){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(15);
		}
		return hourWinter15;
	}
	public void setHourWinter15(int hourWinter15) {
		this.hourWinter15 = hourWinter15;
	}
	public int getHourWinter16() {
		if (hourWinter16 == 0){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(16);
		}
		return hourWinter16;
	}
	public void setHourWinter16(int hourWinter16) {
		this.hourWinter16 = hourWinter16;
	}
	public int getHourWinter17() {
		if (hourWinter17 == 0){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(17);
		}
		return hourWinter17;
	}
	public void setHourWinter17(int hourWinter17) {
		this.hourWinter17 = hourWinter17;
	}
	public int getHourWinter18() {
		if (hourWinter18 == 0){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(18);
		}
		return hourWinter18;
	}
	public void setHourWinter18(int hourWinter18) {
		this.hourWinter18 = hourWinter18;
	}
	public int getHourWinter19() {
		if (hourWinter19 == 0){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(19);
		}
		return hourWinter19;
	}
	public void setHourWinter19(int hourWinter19) {
		this.hourWinter19 = hourWinter19;
	}
	public int getHourWinter20() {
		if (hourWinter20 == 0){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(20);
		}
		return hourWinter20;
	}
	public void setHourWinter20(int hourWinter20) {
		this.hourWinter20 = hourWinter20;
	}
	public int getHourWinter21() {
		if (hourWinter21 == 0){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(21);
		}
		return hourWinter21;
	}
	public void setHourWinter21(int hourWinter21) {
		this.hourWinter21 = hourWinter21;
	}
	public int getHourWinter22() {
		if (hourWinter22 == 0){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(22);
		}
		return hourWinter22;
	}
	public void setHourWinter22(int hourWinter22) {
		this.hourWinter22 = hourWinter22;
	}
	public int getHourWinter23() {
		if (hourWinter0 == 23){
			return getSeasonCatchesPerHourDay().get(Season.WINTER).get(23);
		}
		return hourWinter23;
	}
	public void setHourWinter23(int hourWinter23) {
		this.hourWinter23 = hourWinter23;
	}
	

	public Map<Season, Map<Integer, Integer>> getSeasonCatchesPerHourDay() {
		return seasonCatchesPerHourDay;
	}

	public Map<Integer, Integer> summerHours(){
		Map<Integer, Integer> summerHours = new HashMap<>();
		summerHours.put(0, hourSummer0);
		summerHours.put(1, hourSummer1);
		summerHours.put(2, hourSummer2);
		summerHours.put(3, hourSummer3);
		summerHours.put(4, hourSummer4);
		summerHours.put(5, hourSummer5);
		summerHours.put(6, hourSummer6);
		summerHours.put(7, hourSummer7);
		summerHours.put(8, hourSummer8);
		summerHours.put(9, hourSummer9);
		summerHours.put(10, hourSummer10);
		summerHours.put(11, hourSummer11);
		summerHours.put(12, hourSummer12);
		summerHours.put(13, hourSummer13);
		summerHours.put(14, hourSummer14);
		summerHours.put(15, hourSummer15);
		summerHours.put(16, hourSummer16);
		summerHours.put(17, hourSummer17);
		summerHours.put(18, hourSummer18);
		summerHours.put(19, hourSummer19);
		summerHours.put(20, hourSummer20);
		summerHours.put(21, hourSummer21);
		summerHours.put(22, hourSummer22);
		summerHours.put(23, hourSummer23);
		return summerHours;
	}

	public Map<Integer, Integer> winterHours(){
		Map<Integer, Integer> winterHours = new HashMap<>();
		winterHours.put(0, hourWinter0);
		winterHours.put(1, hourWinter1);
		winterHours.put(2, hourWinter2);
		winterHours.put(3, hourWinter3);
		winterHours.put(4, hourWinter4);
		winterHours.put(5, hourWinter5);
		winterHours.put(6, hourWinter6);
		winterHours.put(7, hourWinter7);
		winterHours.put(8, hourWinter8);
		winterHours.put(9, hourWinter9);
		winterHours.put(10, hourWinter10);
		winterHours.put(11, hourWinter11);
		winterHours.put(12, hourWinter12);
		winterHours.put(13, hourWinter13);
		winterHours.put(14, hourWinter14);
		winterHours.put(15, hourWinter15);
		winterHours.put(16, hourWinter16);
		winterHours.put(17, hourWinter17);
		winterHours.put(18, hourWinter18);
		winterHours.put(19, hourWinter19);
		winterHours.put(20, hourWinter20);
		winterHours.put(21, hourWinter21);
		winterHours.put(22, hourWinter22);
		winterHours.put(23, hourWinter23);
		return winterHours;
	}
	
	
}
