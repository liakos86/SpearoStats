package gr.liakos.spearo.enums;

public enum Concern {
	
	LEAST_CONCERN(1, "least_concern"),
	
	CONSERVATION_DEPENDENT(2, "conservation_dependent"),
	
	NEAR_THREATENED(3, "near_threatened"),
	
	VULNERABLE(4, "vulnerable"),
	
	ENDANGERED(5, "endangered"),
	
	CRITICALLY_ENDANGERED(6, "critically_endangered"),
	
	EXTINCT_WILD(7, "extinct_wild"),
	
	EXTINCT(8, "extinct");

	private int weight;
	private String desc;

	Concern(int weight, String desc){
		this.weight = weight;
		this.desc = desc;
	}

	public static Concern fromWeight(int weight){
		for (Concern c : Concern.values()){
			if (c.weight == weight){
				return c;
			}
		}

		return Concern.LEAST_CONCERN;
	}

	public int getWeight() {
		return weight;
	}

	public String getDesc() {
		return desc;
	}

}
