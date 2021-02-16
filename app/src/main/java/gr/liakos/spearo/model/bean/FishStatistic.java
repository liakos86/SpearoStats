package gr.liakos.spearo.model.bean;

import gr.liakos.spearo.enums.Season;

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

//	public Map<Integer, Integer> getCatchesPerHourDay() {
//		return catchesPerHourDay;
//	}

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
	
//	int hour0;
//    int hour1;
//    int hour2;
//    int hour3;
//    int hour4;
//    int hour5;
//    int hour6;
//    int hour7;
//    int hour8;
//    int hour9;
//    int hour10;
//    int hour11;
//    int hour12;
//    int hour13;
//    int hour14;
//    int hour15;
//    int hour16;
//    int hour17;
//    int hour18;
//    int hour19;
//    int hour20;
//    int hour21;
//    int hour22;
//    int hour23;
//	public int getHour0() {
//		if (hour0 == 0){
//			return getCatchesPerHourDay().get(0);
//		}
//		
//		return hour0;
//	}
//	public void setHour0(int hour0) {
//		this.hour0 = hour0;
//	}
//	public int getHour1() {
//		if (hour1 == 0){
//			return getCatchesPerHourDay().get(1);
//		}
//		return hour1;
//	}
//	public void setHour1(int hour1) {
//		this.hour1 = hour1;
//	}
//	public int getHour2() {
//		if (hour2 == 0){
//			return getCatchesPerHourDay().get(2);
//		}
//		return hour2;
//	}
//	public void setHour2(int hour2) {
//		this.hour2 = hour2;
//	}
//	public int getHour3() {
//		if (hour3 == 0){
//			return getCatchesPerHourDay().get(3);
//		}
//		return hour3;
//	}
//	public void setHour3(int hour3) {
//		this.hour3 = hour3;
//	}
//	public int getHour4() {
//		if (hour4 == 0){
//			return getCatchesPerHourDay().get(4);
//		}
//		return hour4;
//	}
//	public void setHour4(int hour4) {
//		this.hour4 = hour4;
//	}
//	public int getHour5() {
//		if (hour5 == 0){
//			return getCatchesPerHourDay().get(5);
//		}
//		return hour5;
//	}
//	public void setHour5(int hour5) {
//		this.hour5 = hour5;
//	}
//	public int getHour6() {
//		if (hour6 == 0){
//			return getCatchesPerHourDay().get(6);
//		}
//		return hour6;
//	}
//	public void setHour6(int hour6) {
//		this.hour6 = hour6;
//	}
//	public int getHour7() {
//		if (hour7 == 0){
//			return getCatchesPerHourDay().get(7);
//		}
//		return hour7;
//	}
//	public void setHour7(int hour7) {
//		this.hour7 = hour7;
//	}
//	public int getHour8() {
//		if (hour8 == 0){
//			return getCatchesPerHourDay().get(8);
//		}
//		return hour8;
//	}
//	public void setHour8(int hour8) {
//		this.hour8 = hour8;
//	}
//	public int getHour9() {
//		if (hour9 == 0){
//			return getCatchesPerHourDay().get(9);
//		}
//		return hour9;
//	}
//	public void setHour9(int hour9) {
//		this.hour9 = hour9;
//	}
//	public int getHour10() {
//		if (hour10 == 0){
//			return getCatchesPerHourDay().get(10);
//		}
//		return hour10;
//	}
//	public void setHour10(int hour10) {
//		this.hour10 = hour10;
//	}
//	public int getHour11() {
//		if (hour11 == 0){
//			return getCatchesPerHourDay().get(11);
//		}
//		return hour11;
//	}
//	public void setHour11(int hour11) {
//		this.hour11 = hour11;
//	}
//	public int getHour12() {
//		if (hour0 == 12){
//			return getCatchesPerHourDay().get(12);
//		}
//		return hour12;
//	}
//	public void setHour12(int hour12) {
//		this.hour12 = hour12;
//	}
//	public int getHour13() {
//		if (hour0 == 13){
//			return getCatchesPerHourDay().get(13);
//		}
//		return hour13;
//	}
//	public void setHour13(int hour13) {
//		this.hour13 = hour13;
//	}
//	public int getHour14() {
//		if (hour14 == 0){
//			return getCatchesPerHourDay().get(14);
//		}
//		return hour14;
//	}
//	public void setHour14(int hour14) {
//		this.hour14 = hour14;
//	}
//	public int getHour15() {
//		if (hour15 == 0){
//			return getCatchesPerHourDay().get(15);
//		}
//		return hour15;
//	}
//	public void setHour15(int hour15) {
//		this.hour15 = hour15;
//	}
//	public int getHour16() {
//		if (hour16 == 0){
//			return getCatchesPerHourDay().get(16);
//		}
//		return hour16;
//	}
//	public void setHour16(int hour16) {
//		this.hour16 = hour16;
//	}
//	public int getHour17() {
//		if (hour17 == 0){
//			return getCatchesPerHourDay().get(17);
//		}
//		return hour17;
//	}
//	public void setHour17(int hour17) {
//		this.hour17 = hour17;
//	}
//	public int getHour18() {
//		if (hour18 == 0){
//			return getCatchesPerHourDay().get(18);
//		}
//		return hour18;
//	}
//	public void setHour18(int hour18) {
//		this.hour18 = hour18;
//	}
//	public int getHour19() {
//		if (hour19 == 0){
//			return getCatchesPerHourDay().get(19);
//		}
//		return hour19;
//	}
//	public void setHour19(int hour19) {
//		this.hour19 = hour19;
//	}
//	public int getHour20() {
//		if (hour20 == 0){
//			return getCatchesPerHourDay().get(20);
//		}
//		return hour20;
//	}
//	public void setHour20(int hour20) {
//		this.hour20 = hour20;
//	}
//	public int getHour21() {
//		if (hour21 == 0){
//			return getCatchesPerHourDay().get(21);
//		}
//		return hour21;
//	}
//	public void setHour21(int hour21) {
//		this.hour21 = hour21;
//	}
//	public int getHour22() {
//		if (hour22 == 0){
//			return getCatchesPerHourDay().get(22);
//		}
//		return hour22;
//	}
//	public void setHour22(int hour22) {
//		this.hour22 = hour22;
//	}
//	public int getHour23() {
//		if (hour0 == 23){
//			return getCatchesPerHourDay().get(23);
//		}
//		return hour23;
//	}
//	public void setHour23(int hour23) {
//		this.hour23 = hour23;
//	}

//	public void initHoursMap() {
//		if (catchesPerHourDay != null){
//			return;
//		}
//		
//		catchesPerHourDay = new HashMap<Integer, Integer>();
//		for (int i=0; i<24; i++) {
//			catchesPerHourDay.put(i, 0);
//		}
//		
//	}
	
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


	
	
}
