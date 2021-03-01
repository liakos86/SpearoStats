package gr.liakos.spearo.enums;

public enum FishFamily {
	
	Unknown(0, "unknown"),
	
	Serranidae(1, "serranidae"),
	
	Carangidae(2, "carangidae"),
	
	Lophiidae(3, "lophiidae"),
	
	Sparidae(4, "sparidae"),
	
	Sphyraenidae(5, "sphyraenidae"),
	
	Pomatomidae(6, "pomatomidae"),
	
	Coryphaenidae(7, "coryphaenidae"),
	
	Polyprionidae(8, "polyprionidae"),
	
	Lobotidae(9, "lobotidae"),
	
	Moronidae(10, "moronidae"),
	
	Centrolophidae(11, "centrolophidae"),
	
	Pentacerotidae(12, "pentacerotidae"),
	
	Scombridae(13, "scombridae"),
	
	Lethrinidae(14, "lethrinidae"),
	
	Cottidae(15, "cottidae"),
	
	Kyphosidae(16, "kyphosidae"),
	
	Rachycentridae(17, "rachycentridae"),
	
	Gadidae(18, "gadidae"),
	
	Congridae(19, "congridae"),
	
	Sciaenidae(20, "sciaenidae"),
	
	Paralichthyidae(21, "paralichthyidae"),
	
	Pleuronectidae(22, "pleuronectidae"),	
	
	Mullidae(23, "mullidae"),
	
	Scorpaenidae(24, "scorpaenidae"),
	
	Balistidae(25, "balistidae"),

	Trachinidae(26, "trachinidae"),
	
	Mugilidae(27, "mugilidae"),
	
	Muraenidae(28, "muraenidae"),
	
	Uranoscopidae(29, "uranoscopidae"),
	
	Istiophoridae(30, "istiophoridae"),
	
	Scaridae(31, "scaridae"),
	
	Zeidae(32, "zeidae"),
	
	Labridae(33, "labridae"),
	
	Phycidae(34, "phycidae"),

	Lutjanidae(35, "lutjanidae"),

	Octopodidae(36, "octopodidae");
	
	int position;
	
	String name;
	
	private FishFamily(int position, String name){
		this.position = position;
		this.name = name;
	}

	public int getPosition() {
		return position;
	}

	public String getName() {
		return name;
	}

	public static FishFamily fromPosition(int position) {
		for (FishFamily ff : FishFamily.values()) {
			if (ff.getPosition() == position){
				return ff;
			}
		}
		return Unknown;
	}
	
	
	

}
