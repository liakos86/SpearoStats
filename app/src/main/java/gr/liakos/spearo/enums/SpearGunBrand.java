package gr.liakos.spearo.enums;

import gr.liakos.spearo.R;

public enum SpearGunBrand {

    NO_GUN(0, R.drawable.speargun_50, R.string.select_gun_spinner_text),

    PATHOS(1, R.drawable.pathos, R.string.pathos),

    BLEUTEC(2, R.drawable.bleutec100, R.string.bleu),

    MARES(3, R.drawable.mares100, R.string.mares),

    OMER(4, R.drawable.omer100, R.string.omer),

    APNEA(5, R.drawable.apnea100, R.string.apnea),

    BALCO(6, R.drawable.balco100, R.string.balco),

    DEMKA(7, R.drawable.demka100, R.string.demka),

    MEANDROS(8, R.drawable.meandros100, R.string.meandros),

    MVD(9, R.drawable.mvd100, R.string.mvd),

    TIGULLIO(10, R.drawable.tigullio100, R.string.tigullio),

    XIFIAS(11, R.drawable.xifias100, R.string.xifias),

    ARCUS(12, R.drawable.arcus100, R.string.arcus),

    BUCANERO(13, R.drawable.bucanero100, R.string.bucanero),

    PICASSO(14, R.drawable.picasso100, R.string.picasso),

    EPSEALON(15, R.drawable.epsealon100, R.string.epsealon),

    X_DIVE(16, R.drawable.xdive100, R.string.xdive),

    ROB_ALLEN(17, R.drawable.roballen100, R.string.roballen),

    RIFFE(18, R.drawable.riffe100, R.string.riffe),

    CRESSI(19, R.drawable.cressi100, R.string.cressi),

    BEAUCHAT(20, R.drawable.beauchat100, R.string.beauchat),

    SALVIMAR(21, R.drawable.salvimar100, R.string.salvimar),

    SEAC(22, R.drawable.seac100, R.string.seac),

    C4(23, R.drawable.c4100, R.string.c4),

    BLUEGREEN(24, R.drawable.bluegreen100, R.string.bluegreen),

    AZURE(25, R.drawable.azure100, R.string.azure),

    DN_SUB(26, R.drawable.speargun_50, R.string.dnsub),

    LANARA(27, R.drawable.lanara100, R.string.lanara),

    TEAKSEA(28, R.drawable.teaksea100, R.string.teaksea),

    MYTHICON(29, R.drawable.mythicon100, R.string.mythicon),

    KOAH(30, R.drawable.koah100, R.string.koah),

    ROISUB(31, R.drawable.roisub100, R.string.roisub),

    JBL(32, R.drawable.jbl100, R.string.jbl),

    ALEMANNI(33, R.drawable.alemanni100, R.string.alemanni),

    WONG(34, R.drawable.speargun_50, R.string.wong),

    MAKO(35, R.drawable.mako100, R.string.mako),

    AB_BILLER(36, R.drawable.ab_biller100, R.string.abbiller),

    HAMMERHEAD(37, R.drawable.hammerhead100, R.string.hammerhead),

    MASTRO(38, R.drawable.speargun_50, R.string.mastro),

    TIXI(39, R.drawable.speargun_50, R.string.tixi),

    CAMPUS(40, R.drawable.speargun_50, R.string.campus),

    CETMA(41, R.drawable.speargun_50, R.string.cetma),

    SPETTON(42, R.drawable.speargun_50, R.string.spetton),

    SEATEC(43, R.drawable.speargun_50, R.string.seatec),

    TORELLI(44, R.drawable.speargun_50, R.string.torelli),

    VELCO(45, R.drawable.speargun_50, R.string.velco),

    CUSTOM(46, R.drawable.speargun_50, R.string.custom_gun);

    private final Integer id;
    private final Integer drawableId;
    private final Integer brandName;

    public static SpearGunBrand ofPosition(Integer position){
        for (SpearGunBrand brand : SpearGunBrand.values()){
            if (position.equals(brand.getId())){
                return brand;
            }
        }

        return null;
    }

    SpearGunBrand(Integer id, Integer drawableId, Integer brandName){
        this.id = id;
        this.drawableId = drawableId;
        this.brandName = brandName;
    }



    public Integer getDrawableId() {
        return drawableId;
    }

    public Integer getId() {
        return id;
    }

    public Integer getBrandName() {
        return brandName;
    }
}
