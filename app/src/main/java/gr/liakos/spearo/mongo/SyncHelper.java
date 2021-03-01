package gr.liakos.spearo.mongo;

import android.os.Build;

import gr.liakos.spearo.R;
import gr.liakos.spearo.SpearoApplication;
import gr.liakos.spearo.model.bean.FishStatistic;
import gr.liakos.spearo.model.object.Fish;
import gr.liakos.spearo.model.object.User;
import gr.liakos.spearo.util.Constants;
import gr.liakos.spearo.util.DateUtils;
import gr.liakos.spearo.util.FieldsForMongo;
import gr.liakos.spearo.util.MongoConstants;
import gr.liakos.spearo.util.MongoOperators;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class SyncHelper {

//    private static final String TAG = Thread.currentThread().getStackTrace()[2].getClassName()
//            .substring(0, 23);
 //   Database database;
    String conn;
    MongoClient mongoClient;

    public SyncHelper(SpearoApplication application) {
        conn = application.getResources().getString(R.string.m_url_1);
    }
    
    
	public User createAtlasMongoUser(User user){
         MongoCollection<Document> users = getMongoCollection(MongoConstants.COLLECTION_USERS);
         Document userDoc = new Document(FieldsForMongo.username, user.getUsername());
         users.insertOne(userDoc);
         ObjectId id = userDoc.getObjectId("_id");
         user.set_id(id);
         mongoClient.close();
         return user;
    }
    
    public void insertMongoFishRequest(String fishName){
        MongoCollection<Document> requests = getMongoCollection(MongoConstants.COLLECTION_REQUESTS);
        Document userDoc = new Document(FieldsForMongo.fishname, fishName);
        requests.insertOne(userDoc);
        mongoClient.close();
   }
 
	public List<FishStatistic> getAtlasCommunityStats(){
         MongoCollection<Document> species = getMongoCollection(MongoConstants.COLLECTION_FISH);
         FindIterable<Document> find = species.find();
 		List<FishStatistic> atlasStats = new ArrayList<FishStatistic>();
 		for (Document document : find) {
 			String json = document.toJson();
 			FishStatistic stat = (FishStatistic) new Gson().fromJson(json, new TypeToken<FishStatistic>() {}.getType());
 			stat.initSeasonHoursMap();
 			atlasStats.add(stat);
 		}
 		
 		mongoClient.close();
 		return atlasStats;
    }

	public void uploadAtlasStats(Map<Fish, FishStatistic> toBeUploaded){

    	if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
			return;
		}


         MongoCollection<Document> species = getMongoCollection(MongoConstants.COLLECTION_FISH);

         for (Map.Entry<Fish, FishStatistic> mapEntry : toBeUploaded.entrySet()){
        	 Document searchQuery = new Document(FieldsForMongo.fishId, mapEntry.getKey().getFishId());
        	 FishStatistic fishStatistic = mapEntry.getValue();
        	
        	 Map<String, Double> documentsToIncrease = new HashMap<String, Double>();
        	 
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.totalCatches, fishStatistic.getTotalCatches());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.weightedCatches, fishStatistic.getWeightedCatches());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.totalDepth, fishStatistic.getTotalDepth());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.depthedCatches, fishStatistic.getDepthedCatches());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.totalWeight, fishStatistic.getTotalWeight());
        	 
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer0, fishStatistic.getHourSummer0());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer1, fishStatistic.getHourSummer1());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer2, fishStatistic.getHourSummer2());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer3, fishStatistic.getHourSummer3());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer4, fishStatistic.getHourSummer4());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer5, fishStatistic.getHourSummer5());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer6, fishStatistic.getHourSummer6());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer7, fishStatistic.getHourSummer7());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer8, fishStatistic.getHourSummer8());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer9, fishStatistic.getHourSummer9());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer10, fishStatistic.getHourSummer10());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer11, fishStatistic.getHourSummer11());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer12, fishStatistic.getHourSummer12());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer13, fishStatistic.getHourSummer13());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer14, fishStatistic.getHourSummer14());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer15, fishStatistic.getHourSummer15());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer16, fishStatistic.getHourSummer16());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer17, fishStatistic.getHourSummer17());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer18, fishStatistic.getHourSummer18());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer19, fishStatistic.getHourSummer19());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer20, fishStatistic.getHourSummer20());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer21, fishStatistic.getHourSummer21());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer22, fishStatistic.getHourSummer22());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourSummer23, fishStatistic.getHourSummer23());
        	 
        	 
        	 
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter0, fishStatistic.getHourWinter0());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter1, fishStatistic.getHourWinter1());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter2, fishStatistic.getHourWinter2());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter3, fishStatistic.getHourWinter3());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter4, fishStatistic.getHourWinter4());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter5, fishStatistic.getHourWinter5());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter6, fishStatistic.getHourWinter6());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter7, fishStatistic.getHourWinter7());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter8, fishStatistic.getHourWinter8());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter9, fishStatistic.getHourWinter9());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter10, fishStatistic.getHourWinter10());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter11, fishStatistic.getHourWinter11());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter12, fishStatistic.getHourWinter12());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter13, fishStatistic.getHourWinter13());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter14, fishStatistic.getHourWinter14());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter15, fishStatistic.getHourWinter15());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter16, fishStatistic.getHourWinter16());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter17, fishStatistic.getHourWinter17());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter18, fishStatistic.getHourWinter18());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter19, fishStatistic.getHourWinter19());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter20, fishStatistic.getHourWinter20());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter21, fishStatistic.getHourWinter21());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter22, fishStatistic.getHourWinter22());
        	 addIfNotZero(documentsToIncrease, FieldsForMongo.hourWinter23, fishStatistic.getHourWinter23());
        	 
        	 
        	 
        	 if (documentsToIncrease.isEmpty()){
        		 return;
        	 }
        	 
        	 Document fieldsToIncrease = null;
        	 for (Map.Entry<String, Double> entry :  documentsToIncrease.entrySet()){
        		 if (fieldsToIncrease == null){
        			 fieldsToIncrease = new Document(entry.getKey(), entry.getValue());
        			 continue;
        		 }
        		 fieldsToIncrease.append(entry.getKey(), entry.getValue());
        	 }
        	 
        	 Document increaseAllFieldsQuery = new Document(MongoOperators.INCREASE, fieldsToIncrease);
        	 species.findOneAndUpdate(searchQuery, increaseAllFieldsQuery);
         }
         mongoClient.close();
    }

	void addIfNotZero(Map<String, Double> documentsToIncrease, String fieldName, double increment) {
		if (increment > 0 ){
			documentsToIncrease.put(fieldName, increment);
		}
	}
	
	public void sendCrashReport(CrashReportData report) {
		MongoCollection<Document> users = getMongoCollection(MongoConstants.COLLECTION_CRASH);
		Document userDoc = new Document(FieldsForMongo.app_version, report.get(ReportField.APP_VERSION_CODE));
		
		DateFormat formatter = new SimpleDateFormat(Constants.DATETIME_FORMAT, Locale.US);
        Calendar calendar = DateUtils.getCalendarWithTime(new Date().getTime());
        String dateString = formatter.format(calendar.getTime());
		
        userDoc.append(FieldsForMongo.app_version_name, report.get(ReportField.APP_VERSION_NAME));
        userDoc.append(FieldsForMongo.android_version, report.get(ReportField.ANDROID_VERSION)); 
        userDoc.append(FieldsForMongo.phone_model, report.get(ReportField.PHONE_MODEL));
        userDoc.append(FieldsForMongo.timestamp, dateString);
        userDoc.append(FieldsForMongo.stack_trace, report.get(ReportField.STACK_TRACE));
        users.insertOne(userDoc);
        mongoClient.close();
	}

   // @SuppressWarnings("resource")
	MongoCollection<Document> getMongoCollection(String collectionName){
    	MongoClientURI uri = new MongoClientURI(conn + "<dbname>?ssl=true&replicaSet=spearo-shard-0&authSource=admin&retryWrites=true&w=majority");
       //  		"mongodb://liakos86:art78tha3m@spearo-shard-00-00.tgnig.gcp.mongodb.net:27017,spearo-shard-00-01.tgnig.gcp.mongodb.net:27017,spearo-shard-00-02.tgnig.gcp.mongodb.net:27017/<dbname>?ssl=true&replicaSet=spearo-shard-0&authSource=admin&retryWrites=true&w=majority");
		mongoClient = new MongoClient(uri);
    	MongoDatabase database = mongoClient.getDatabase(MongoConstants.SPEARO_DB);
        return database.getCollection(collectionName);
    }


}
