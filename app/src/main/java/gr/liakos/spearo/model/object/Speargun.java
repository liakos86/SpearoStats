package gr.liakos.spearo.model.object;

import android.content.ContentValues;
import android.content.res.Resources;

import java.util.List;

import gr.liakos.spearo.R;
import gr.liakos.spearo.enums.SpearGunBrand;
import gr.liakos.spearo.enums.SpeargunType;
import gr.liakos.spearo.model.ContentDescriptor;
import gr.liakos.spearo.model.Database;

public class Speargun implements Comparable<Speargun>{

    Integer gunId;

    SpearGunBrand brand;

    String model;

    String nickName;

    Integer length;

    SpeargunType type;

    List<FishCatch> caughtFish;

    public Speargun(int gunId, SpearGunBrand brand, String model, SpeargunType type, int length, String nickName) {
        this.gunId = gunId;
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.length = length;
        this.nickName = nickName;
    }

    public Speargun() {
        this.gunId = Database.INVALID_ID;
    }

    public static Speargun dummy(int size, Resources resources) {
        Speargun dummy = new Speargun();
        if (size == 0){
            dummy.setModel(resources.getString(R.string.no_guns_spinner_text));
        }else{
            dummy.setModel(resources.getString(R.string.existing_guns_spinner_text));
        }

        return dummy;
    }

    public Integer getGunId() {
        return gunId;
    }

    public void setGunId(Integer gunId) {
        this.gunId = gunId;
    }

    public SpearGunBrand getBrand() {
        return brand;
    }

    public void setBrand(SpearGunBrand brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public SpeargunType getType() {
        return type;
    }

    public void setType(SpeargunType type) {
        this.type = type;
    }

    public List<FishCatch> getCaughtFish() {
        return caughtFish;
    }

    public void setCaughtFish(List<FishCatch> caughtFish) {
        this.caughtFish = caughtFish;
    }

    public static ContentValues asContentValues(Speargun item) {
        if (item == null)
            return null;
        synchronized (item) {
            ContentValues toRet = new ContentValues();
            toRet.put(ContentDescriptor.Speargun.Cols.GUN_ID, item.gunId);
            toRet.put(ContentDescriptor.Speargun.Cols.BRAND, item.brand.getId());
            toRet.put(ContentDescriptor.Speargun.Cols.MODEL, item.model);
            toRet.put(ContentDescriptor.Speargun.Cols.TYPE, item.type.getId());
            toRet.put(ContentDescriptor.Speargun.Cols.LENGTH, item.length);
            toRet.put(ContentDescriptor.Speargun.Cols.NICKNAME, item.nickName);

            return toRet;
        }
    }

    @Override
    public int compareTo(Speargun s) {
        if (this.getCaughtFish() == null || this.getCaughtFish().isEmpty()){
            return 1;
        }

        if (s.getCaughtFish() == null || s.getCaughtFish().isEmpty()){
            return -1;
        }

        return  s.getCaughtFish().size() - this.getCaughtFish().size();
    }
}
