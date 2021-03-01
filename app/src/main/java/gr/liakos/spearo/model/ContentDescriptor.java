
package gr.liakos.spearo.model;

import gr.liakos.spearo.util.Constants;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * based on http://www.nofluffjuststuff.com/blog/vladimir_vivien/2011/11/
 * a_pattern_for_creating_custom_android_content_providers <br/>
 * workaround (wa1): We don't use {@link #applyBatch(java.util.ArrayList)}for
 * number matching (see
 * http://code.google.com/p/android/issues/detail?mId=27031). We use * and take
 * care of it in code.<br/>
 *
 * @author kliakopoulos
 */
public class ContentDescriptor {

    public static final String AUTHORITY = "gr.liakos.spearo.contentprovider";
    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
    public static final UriMatcher URI_MATCHER = buildUriMatcher();

    public static final String PARAM_FULL = "full";
    public static final String PARAM_SEARCH = "search";
    public static final String PARAM_COUNT = "count";


    // argument passed via query params, start with this string
    // in other words: query params starting with this string are arguments
    public static final String ARG_PREFIX = "arg_";
    // helper format strings
    //private static final String sFormatArg = ARG_PREFIX + "%s";
    // helper format strings for table creation
    private static final String sFrmIdAutoinc = " %s INTEGER PRIMARY KEY AUTOINCREMENT ";
    //private static final String sFrmId = " %s INTEGER PRIMARY KEY ";
    private static final String sFrmInt = " %s INTEGER ";
    //private static final String sFrmBlob = " %s BLOB ";
    private static final String sFrmIntDefault0 = " %s INTEGER DEFAULT 0 ";
    //private static final String sFrmIntNotNullDefault0 = " %s INTEGER NOT NULL DEFAULT 0 ";
    private static final String sFrmIntNotNull = " %s INTEGER NOT NULL ";
    private static final String sFrmText = " %s TEXT ";
    private static final String sFrmLong = " %s LONG ";
    private static final String sFrmTextNotNull = " %s TEXT NOT NULL ";
    private static final String sFrmDouble = " %s DOUBLE ";
    //private static final String sFrmDoubleDefault0 = " %s DOUBLE DEFAULT 0 ";
    private static final String sFrmDoubleNotNull = " %s DOUBLE NOT NULL ";
    //private static final String sFrmDoubleNotNullDefault0 = " %s DOUBLE NOT NULL DEFAULT 0 ";
    private static final String sFrmPrimaryKey = " UNIQUE (%s) ON CONFLICT REPLACE ";

    private ContentDescriptor() {
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = AUTHORITY;

        Fish.addToUriMatcher(authority, matcher);
        FishCatch.addToUriMatcher(authority, matcher);
        FishingSession.addToUriMatcher(authority, matcher);
        FishAverageStatistic.addToUriMatcher(authority, matcher);
        return matcher;
    }

    public static class FishCatch {
        public static final String TABLE_NAME = "FISHCATCH";
        // content://xxxxx/running
        public static final String PATH = "fishcatch";
        public static final int PATH_TOKEN = 10;
        // content://xxxxx/running/20
        public static final String PATH_FOR_ID = "fishcatch/#";
        // see wa1 content://xxxxx/running/21
        public static final String PATH_FOR_ID_WA = "fishcatch/*";
        public static final int PATH_FOR_ID_TOKEN = 11;
        // content://xxxxx/simcounterdetailresponses/startletters
        public static final String PATH_START_LETTERS = "fishcatch/startletters";
        public static final int PATH_START_LETTERS_TOKEN = 12;

        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH).build();

        public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.gr.liakos.spearo.app";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.gr.liakos.spearo.app";

        public static class Cols {
            public static final String FISHCATCHID = BaseColumns._ID; // by convention
            public static final String FISHINGSESSIONID = "fishingsessionid";
            public static final String FISHID = "fishid";
            public static final String WEIGHT = "weight";
            public static final String DEPTH = "depth";
            public static final String CATCH_TIME_MINUTES = "catchTimeInMinutes";
            public static final String CATCH_HOUR = "catchHour";
        }

        protected static UriMatcher addToUriMatcher(String authority, UriMatcher matcher) {
            matcher.addURI(authority, FishCatch.PATH, FishCatch.PATH_TOKEN);
            matcher.addURI(authority, FishCatch.PATH_FOR_ID, FishCatch.PATH_FOR_ID_TOKEN);
            matcher.addURI(authority, FishCatch.PATH_FOR_ID_WA, FishCatch.PATH_FOR_ID_TOKEN);
            matcher.addURI(authority, FishCatch.PATH_START_LETTERS, FishCatch.PATH_START_LETTERS_TOKEN);
            return matcher;
        }

        public static String createTable() {
            return "CREATE TABLE " + FishCatch.TABLE_NAME + " ( "
                    + String.format(sFrmIdAutoinc, Cols.FISHCATCHID) + " , "
                    + String.format(sFrmTextNotNull, Cols.FISHINGSESSIONID) + " , "
                    + String.format(sFrmIntNotNull, Cols.FISHID) + " , "
                    + String.format(sFrmInt, Cols.CATCH_TIME_MINUTES) + " , "
                    + String.format(sFrmInt, Cols.CATCH_HOUR) + " , "
                    + String.format(sFrmDouble, Cols.WEIGHT) + " , "
                    + String.format(sFrmInt, Cols.DEPTH) + " , "

                    + String.format(sFrmPrimaryKey, Cols.FISHCATCHID) + ")";
        }
    }


    public static class FishingSession {
        public static final String TABLE_NAME = "fishingSession";
        // content://xxxxx/running
        public static final String PATH = "fishingSession";
        public static final int PATH_TOKEN = 20;
        // content://xxxxx/running/20
        public static final String PATH_FOR_ID = "fishingSession/#";
        // see wa1 content://xxxxx/running/21
        public static final String PATH_FOR_ID_WA = "fishingSession/*";
        public static final int PATH_FOR_ID_TOKEN = 21;
        // content://xxxxx/simcounterdetailresponses/startletters
        public static final String PATH_START_LETTERS = "fishingSession/startletters";
        public static final int PATH_START_LETTERS_TOKEN = 22;

        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH).build();

        public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.gr.liakos.spearo.app";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.gr.liakos.spearo.app";

        public static class Cols {
            public static final String FISHINGSESSIONID = BaseColumns._ID; // by convention
            public static final String FISHINGDATE = "fishingdate";
            public static final String FISHINGSESSIONLAT = "latitude";
            public static final String FISHINGSESSIONLON = "longitude";
            public static final String UPLOADED = "uploaded";
			public static final String SESSION_IMAGE = "sessionImage";
            public static final String SESSION_IMAGE_URI_PATH = "sessionImageUriPath";
			public static final String SESSION_MOON = "sessionMoon";
			public static final String SESSION_WIND = "sessionWind";
            public static final String SESSION_WIND_VOLUME = "sessionWindVolume";
        }

        protected static UriMatcher addToUriMatcher(String authority, UriMatcher matcher) {
            matcher.addURI(authority, FishingSession.PATH, FishingSession.PATH_TOKEN);
            matcher.addURI(authority, FishingSession.PATH_FOR_ID, FishingSession.PATH_FOR_ID_TOKEN);
            matcher.addURI(authority, FishingSession.PATH_FOR_ID_WA, FishingSession.PATH_FOR_ID_TOKEN);
            matcher.addURI(authority, FishingSession.PATH_START_LETTERS, FishingSession.PATH_START_LETTERS_TOKEN);
            return matcher;
        }

        public static String createTable() {
            return "CREATE TABLE " + FishingSession.TABLE_NAME + " ( "
                    + String.format(sFrmIdAutoinc, Cols.FISHINGSESSIONID) + " , "
                    + String.format(sFrmLong, Cols.FISHINGDATE) + " , "
                    + String.format(sFrmDouble, Cols.FISHINGSESSIONLAT) + " , "
                    + String.format(sFrmDouble, Cols.FISHINGSESSIONLON) + " , "
                    + String.format(sFrmIntDefault0, Cols.UPLOADED) + " , "
                    + String.format(sFrmText, Cols.SESSION_IMAGE) + " , "
                    + String.format(sFrmText, Cols.SESSION_IMAGE_URI_PATH) + " , "
                    + String.format(sFrmIntDefault0, Cols.SESSION_MOON) + " , "
                    + String.format(sFrmIntDefault0, Cols.SESSION_WIND) + " , "
                    + String.format(sFrmIntDefault0, Cols.SESSION_WIND_VOLUME) + " , "
                    + String.format(sFrmPrimaryKey, Cols.FISHINGSESSIONID) + ")";
        }
    }


    public static class Fish {
        public static final String TABLE_NAME = "fish";
        // content://xxxxx/running
        public static final String PATH = "fish";
        public static final int PATH_TOKEN = 30;
        // content://xxxxx/running/20
        public static final String PATH_FOR_ID = "fish/#";
        // see wa1 content://xxxxx/running/21
        public static final String PATH_FOR_ID_WA = "fish/*";
        public static final int PATH_FOR_ID_TOKEN = 31;
        // content://xxxxx/simcounterdetailresponses/startletters
        public static final String PATH_START_LETTERS = "fish/startletters";
        public static final int PATH_START_LETTERS_TOKEN = 32;

        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH).build();

        public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.gr.liakos.spearo.app";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.gr.liakos.spearo.app";

        public static class Cols {
            public static final String FISHID = BaseColumns._ID; // by convention
            public static final String LATINNAME = "latinname";
            public static final String RECORDWEIGHT = "record";
            public static final String FISHFAMILY = "fishFamily";
            public static final String MAXALLOWEDCATCHWEIGHT = "maxAllowedCatchWeight";
            public static final String CONCERN = "concern";

        }

        protected static UriMatcher addToUriMatcher(String authority, UriMatcher matcher) {
            matcher.addURI(authority, Fish.PATH, Fish.PATH_TOKEN);
            matcher.addURI(authority, Fish.PATH_FOR_ID, Fish.PATH_FOR_ID_TOKEN);
            matcher.addURI(authority, Fish.PATH_FOR_ID_WA, Fish.PATH_FOR_ID_TOKEN);
            matcher.addURI(authority, Fish.PATH_START_LETTERS, Fish.PATH_START_LETTERS_TOKEN);
            return matcher;
        }

        public static String createTable() {
            return "CREATE TABLE " + Fish.TABLE_NAME + " ( "
                    + String.format(sFrmIdAutoinc, Cols.FISHID) + " , "
                    + String.format(sFrmTextNotNull, Cols.LATINNAME) + " , "
                    + String.format(sFrmDoubleNotNull, Cols.RECORDWEIGHT) + " , "
                    + String.format(sFrmIntDefault0, Cols.FISHFAMILY) + " , "
                    + String.format(sFrmIntDefault0, Cols.CONCERN) + " , "
                    + String.format(sFrmDouble, Cols.MAXALLOWEDCATCHWEIGHT) + " , "

                    + String.format(sFrmPrimaryKey, Cols.FISHID) + ")";
        }


        public static String insertSpecies() {
            String insert = "INSERT OR IGNORE INTO " + Fish.TABLE_NAME
                    + "( " + ContentDescriptor.Fish.Cols.FISHID + Constants.COMMA_SEP + ContentDescriptor.Fish.Cols.LATINNAME + Constants.COMMA_SEP + ContentDescriptor.Fish.Cols.RECORDWEIGHT
                    + Constants.COMMA_SEP + ContentDescriptor.Fish.Cols.FISHFAMILY + Constants.COMMA_SEP + ContentDescriptor.Fish.Cols.MAXALLOWEDCATCHWEIGHT + Constants.COMMA_SEP
                    + ContentDescriptor.Fish.Cols.CONCERN + ") values "
                    + "(1, 'Seriola dumerili', 75.4, 2, 0, 1),"
                    + "(2, 'Seriola fasciata', 10, 2, 0, 1),"
                    + "(3, 'Lophius piscatorius', 38.3, 3, 0, 1),"
                    + "(4, 'Sphyraena afra', 25.0, 5, 0, 1),"
                    + "(5, 'Sphyraena qenie', 16.4, 5, 0, 1),"
                    + "(6, 'Sphyraena barracuda', 29.4, 5, 0, 1),"
                    + "(7, 'Sphyraena argentea', 5.2, 5, 0, 1),"
                    + "(8, 'Sphyraena jello', 18.5, 5, 0, 1),"
                    + "(9, 'Sphyraena putnamae', 10.5, 5, 0, 1),"
                    + "(10,'Sphyraena viridensis', 11.6, 5, 0, 1),"
                    + "(11,'Sphyraena sphyraena', 3.6, 5, 0, 1),"
                    + "(12,'Stereolepis gigas', 247.2, 8, 0, 6),"
                    + "(13,'Paralabrax clathratus', 5.6, 1, 0, 1),"
                    + "(14,'Dermatolepis dermatolepis', 10.6, 1, 0, 1),"
                    + "(15,'Mullus', -1, 23, 2.0, 1),"
                    + "(16, 'Morone saxatilis', 5.6, 10, 0, 1),"
                    + "(17, 'Seriolella brama', 6.7, 11, 0, 1),"
                    + "(18, 'Pomatomus saltatrix', 11.6, 6, 0, 4),"
                    + "(19, 'Paristiopterus labiosus', 5.4, 12, 0, 1),"
                    + "(20, 'Sarda sarda', 10.1, 13, 0, 1),"
                    + "(21, 'Sarda chiliensis', 8.2, 13, 0, 1),"
                    + "(22, 'Scorpaena scrofa', 3.6, 24, 5.0, 1),"
                    + "(23, 'Monotaxis grandoculis', 6.7, 14, 0, 1),"
                    + "(24, 'Argyrops spinifer', 4.7, 4, 0, 1),"
                    + "(25, 'Balistes capriscus', -1, 25, 10.0, 1),"
                    + "(26, 'Pterois', -1, 24, 1.5, 1),"
                    + "(27, 'Weever', -1, 26, 3.0, 1),"
                    + "(28, 'Scorpaenichthys marmoratus', 11.3, 15, 0, 1),"
                    + "(29, 'Epinephelus analogus', 23.6, 1, 0, 1),"
                    + "(30, 'Mugil cephalus', -1, 27, 10.0, 1),"
                    + "(31, 'Muraena helena', -1, 28, 6.0, 1),"
                    + "(32, 'Uranoscopus scaber', -1, 29, 4.0, 1),"
                    + "(33, 'Lichia stella', -1, 2, 3, 1),"
                    + "(34, 'Kyphosus sectatrix', 9.9, 16, 0, 1),"
                    + "(35, 'Rachycentron canadum', 66.2, 17, 0, 1),"
                    + "(36, 'Gadus morhua', 9.8, 18, 0, 4),"
                    + "(37, 'Gadus macrocephaleus', 11.5, 18, 0, 1),"
                    + "(38, 'Conger conger', 4.9, 19, 0, 1),"
                    + "(39, 'Plectropomus laevis', 24.9, 1, 0, 1),"
                    + "(40, 'Plectropomus leopardus', 7.2, 1, 0, 1),"
                    + "(41, 'Plectropomus punctatus', 17.3, 1, 0, 1),"
                    + "(42, 'Plectropomus pessuliferus', 13.1, 1, 0, 1),"
                    + "(43, 'Plectropomus maculatus', 11.8, 1, 0, 1),"
                    + "(44, 'Cynoscion stolzmanni', 10.5, 20, 0, 1),"
                    + "(45, 'Dentex dentex', 12.7, 4, 0, 4),"
                    + "(46, 'Dentex gibbosus', 9.9, 4, 0, 1),"
                    + "(47, 'Coryphaena hippurus', 31.8, 7, 0, 1),"
                    + "(48, 'Pogonias cromis', 45.9, 20, 0, 1),"
                    + "(49, 'Boops boops', -1, 4, 2.0, 1),"
                    + "(50, 'Sciaena umbra', -1, 20, 5.0, 1),"
                    + "(51, 'Lethrinus olivaceus', 9.2, 14, 0, 1),"
                    + "(52, 'Lethrinus erythracanthus', 8.7, 14, 0, 1),"
                    + "(53, 'Lethrinus nebulosus', 5.8, 14, 0, 1),"
                    + "(54, 'Lethrinus xanthochilus', 4.6, 14, 0, 1),"
                    + "(55, 'Paralichthys brasiliensis', 10.1, 21, 0, 1),"
                    + "(56, 'Platichthys stellatus', 6.0, 22, 0, 1),"
                    + "(57, 'Paralichthys dentatus', 6.9, 21, 0, 1),"
                    + "(58, 'Umbrina cirrosa', -1, 20, 12.0 , 4),"
                    + "(59, 'Sparisoma cretense', -1, 31, 4.0, 1),"

                    + "(60, 'Lophius americanus', 11.1, 3, 0, 1),"
                    + "(61, 'Mycteroperca bonaci', 44.2, 1, 0, 1),"
                    + "(62, 'Mycteroperca xenarcha', 55.8, 1, 0, 1),"
                    + "(63, 'Epinephelus fuscoguttatus', 11.6, 1, 0, 4),"
                    + "(64, 'Mycteroperca acutirostris', 10.2, 1, 0, 1),"
                    + "(65, 'Epinephelus marginatus', 35.1, 1, 0, 4),"
                    + "(66, 'Mycteroperca microlepis', 29.2, 1, 0, 4),"
                    + "(67, 'Epinephelus costae', 7.8, 1, 0, 1),"
                    + "(68, 'Mycteroperca jordani', 95.7, 1, 0, 5),"
                    + "(69, 'Mycteroperca rosacea', 13.7, 1, 0, 1),"
                    + "(70, 'Epinephelus malabaricus', 46.5, 1, 0, 1),"
                    + "(71, 'Mycteroperca rubra', 7.0, 1, 0, 1),"
                    + "(72, 'Epinephelus coioides', 19.8, 1, 0, 1),"
                    + "(73, 'Epinephelus morio', 8.7, 1, 0, 4),"
                    + "(74, 'Epinephelus adsensionis', 4.6, 1, 0, 1),"
                    + "(75, 'Mycteroperca prionura', 14.1, 1, 0, 1),"
                    + "(76, 'Mycteroperca phenax', 12.4, 1, 0, 1),"
                    + "(77, 'Epinephelus posteli', 10.6, 1, 0, 1),"
                    + "(78, 'Epinephelus nigritus', 21.3, 1, 0, 1),"
                    + "(79, 'Epinephelus multinotatus', 16.0, 1, 0, 1),"
                    + "(80, 'Mycteroperca venenosa', 14.1, 1, 0, 1),"


                    + "(81, 'Mycteroperca interstitialis', 8.2 , 1, 0, 4),"

                    + "(82, 'Hippoglossus hippoglossus', 63.4, 22, 0, 5),"

                    + "(83, 'Paralichthys californicus', 26.7, 21, 0, 1),"

                    + "(84, 'Hippoglossus stenolepis', 	61.2, 22, 0, 1),"

                    + "(85, 'Seriola rivoliana',	54.4 ,	2, 0, 1),"
                    + "(86, 'Caranx lugubris',	14.0,	2, 0, 1),"
                    + "(87, 'Caranx hippos',		21.7 ,	2, 0, 1),"
                    + "(88, 'Caranx caninus',	11.3 ,	2, 0, 1),"
                    + "(89, 'Seriola peruana',	7.0 , 2, 0, 1),"
                    + "(90, 'Caranx latus',	13.9 ,	2, 0, 1),"
                    + "(91, 'Caranx fischeri',	18.2 ,2, 0, 1),"
                    + "(92, 'Caranx bartholomaei',	12.8 ,2, 0, 1),"
                    + "(93, 'Lichia amia', 30.0, 2, 0, 1),"
                    + "(94, 'Euthynnus alletteratus', 10.7 , 13, 0, 1),"

                    + "(95, 'Scomberomorus regalis',  7.4 , 13, 0, 1),"
                    + "(96, 'Scomberomorus cavalla', 35.3 , 13, 0, 1),"
                    + "(97, 'Scomberomorus commerson', 40.5 , 13, 0, 1),"


                    + "(98, 'Makaira indica', 	374.8, 30, 0, 1),"
                    + "(99, 'Makaira nigricans', 301.2 , 30, 0, 4),"

                    + "(100, 'Makaira mazara',  259.0 , 30, 0, 1),"
                    + "(101, 'Kajikia audax', 164.2 , 30, 0, 1),"
                    + "(102, 'Kajikia albida', 42.5  , 30, 0, 4),"

                    + "(103, 'Argyrosomus regius', 32.8  , 20, 0, 1),"

                    + "(104, 'Pollachius pollachius', 5.5  , 18, 0, 1),"

                    + "(105, 'Pagrus pagrus',  5.4 ,4, 0, 1),"

                    + "(106, 'Caranx crysos',  4.6 , 2, 0, 1),"
                    + "(107, 'Oblada melanura',  -1 , 4, 2.0, 1),"
                    + "(108, 'Dicentrarchus labrax',  9.8 , 10, 0, 1),"

                    + "(109, 'Sparus aurata',  6.9 , 4, 0, 1),"
                    + "(110, 'Diplodus puntazzo',  6.2 , 4, 0, 1),"

                    + "(111, 'Lithognathus mormyrus',  -1, 4, 3.0, 1),"

                    + "(112, 'Diplodus sargus',  2.87 , 4, 5.0, 1),"

                    + "(113, 'Salema porgy', -1, 4, 3.0, 1),"

                    + "(114, 'Epinephelus aeneus', 22.6, 1, 0, 1),"

                    + "(115, 'Spondyliosoma cantharus', -1, 4, 5.0, 1),"

                    + "(116, 'Zeus faber', -1, 32, 6.0, 1),"

                    + "(117, 'Symphodus melops', -1, 33, 4.0, 1),"

                    + "(118, 'Diplodus vulgaris', -1, 4, 1.0, 1),"
                    + "(119, 'Phycis phycis', -1, 34, 8.0, 1),"
                    + "(120, 'Polyprion americanus', -1, 8, 30.0, 1),"
                    + "(121, 'Pagrus auratus', 15.2, 4, 0, 1),"

                    + "(122, 'Lutjanus Dentatus', 24.7, 35, 0, 4),"
                    + "(123, 'Lutjanus agennes', 13.9, 35, 0, 1),"
                    + "(124, 'Lutjanus cyanopterus', 55.5, 35, 0, 4),"
                    + "(125, 'Macolor niger', 4.8, 35, 0, 1),"
                    + "(126, 'Lutjanus colorado', 15.7, 35, 0, 1),"
                    + "(127, 'Lutjanus jocu', 14.6, 35, 0, 1),"
                    + "(128, 'Lutjanus sebae', 18.6, 35, 0, 1),"
                    + "(129, 'Lutjanus goreensis', 8, 35, 0, 1),"
                    + "(130, 'Lutjanus griseus', 8.2, 35, 0, 1),"
                    + "(131, 'Lutjanus griseus cyanopterus hybird', 13.4, 35, 0, 1),"
                    + "(132, 'Lutjanus sanguineus', 13.4, 35, 0, 1),"

                    + "(133, 'Hoplopagrus guentheri', 17.5, 35, 0, 1),"
                    + "(134, 'Macolor macularis', 5.1, 35, 0, 1),"
                    + "(135, 'Lutjanus aratus', 19.5, 35, 0, 1),"
                    + "(136, 'Lutjanus analis', 12.3, 35, 0, 3),"
                    + "(137, 'Lutjanus campechanus', 14.1, 35, 0, 4),"
                    + "(138, 'Lutjanus novemfasciatus', 36.7, 35, 0, 1),"
                    + "(139, 'Lutjanus peru', 12.2, 35, 0, 1),"
                    + "(140, 'Lutjanus goldiei', 6.5, 35, 0, 1),"
                    + "(141, 'Lutjanus bohar', 12.7, 35, 0, 1),"
                    + "(142, 'Lutjanus argentimaculatus', 10, 35, 0, 1),"
                    + "(143, 'Lutjanus rivulatus', 17.3, 35, 0, 1),"
                    + "(144, 'Lutjanus argentiventris', 6.6, 35, 0, 1),"
                    + "(145, 'Octopus vulgaris', 10, 36, 0, 1)";

            return insert;
        }

        public static String insertAdditionalSpecies0() {
            String insert = "INSERT OR IGNORE INTO " + Fish.TABLE_NAME
                    + "( " + ContentDescriptor.Fish.Cols.FISHID + Constants.COMMA_SEP + ContentDescriptor.Fish.Cols.LATINNAME + Constants.COMMA_SEP + ContentDescriptor.Fish.Cols.RECORDWEIGHT
                    + Constants.COMMA_SEP + ContentDescriptor.Fish.Cols.FISHFAMILY + Constants.COMMA_SEP + ContentDescriptor.Fish.Cols.MAXALLOWEDCATCHWEIGHT + Constants.COMMA_SEP
                    + ContentDescriptor.Fish.Cols.CONCERN + ") values "
                    + "(122, 'Lutjanus Dentatus', 24.7, 35, 0, 4),"
                    + "(123, 'Lutjanus agennes', 13.9, 35, 0, 1),"
                    + "(124, 'Lutjanus cyanopterus', 55.5, 35, 0, 4),"
                    + "(125, 'Macolor niger', 4.8, 35, 0, 1),"
                    + "(126, 'Lutjanus colorado', 15.7, 35, 0, 1),"
                    + "(127, 'Lutjanus jocu', 14.6, 35, 0, 1),"
                    + "(128, 'Lutjanus sebae', 18.6, 35, 0, 1),"
                    + "(129, 'Lutjanus goreensis', 8, 35, 0, 1),"
                    + "(130, 'Lutjanus griseus', 8.2, 35, 0, 1),"
                    + "(131, 'Lutjanus griseus cyanopterus hybird', 13.4, 35, 0, 1),"
                    + "(132, 'Lutjanus sanguineus', 13.4, 35, 0, 1),"
                    + "(133, 'Hoplopagrus guentheri', 17.5, 35, 0, 1),"
                    + "(134, 'Macolor macularis', 5.1, 35, 0, 1),"
                    + "(135, 'Lutjanus aratus', 19.5, 35, 0, 1),"
                    + "(136, 'Lutjanus analis', 12.3, 35, 0, 3),"
                    + "(137, 'Lutjanus campechanus', 14.1, 35, 0, 4),"
                    + "(138, 'Lutjanus novemfasciatus', 36.7, 35, 0, 1),"
                    + "(139, 'Lutjanus peru', 12.2, 35, 0, 1),"
                    + "(140, 'Lutjanus goldiei', 6.5, 35, 0, 1),"
                    + "(141, 'Lutjanus bohar', 12.7, 35, 0, 1),"
                    + "(142, 'Lutjanus argentimaculatus', 10, 35, 0, 1),"
                    + "(143, 'Lutjanus rivulatus', 17.3, 35, 0, 1),"
                    + "(144, 'Lutjanus argentiventris', 6.6, 35, 0, 1),"
                    + "(145, 'Octopus vulgaris', 6.6, 35, 0, 1)";
            return insert;
        }

    }
    
    public static class FishAverageStatistic {
        public static final String TABLE_NAME = "FISHAVERAGESTATISTIC";
        // content://xxxxx/running
        public static final String PATH = "fishaveragestatistic";
        public static final int PATH_TOKEN = 40;
        // content://xxxxx/running/20
        public static final String PATH_FOR_ID = "fishaveragestatistic/#";
        // see wa1 content://xxxxx/running/21
        public static final String PATH_FOR_ID_WA = "fishaveragestatistic/*";
        public static final int PATH_FOR_ID_TOKEN = 41;
        // content://xxxxx/simcounterdetailresponses/startletters
        public static final String PATH_START_LETTERS = "fishaveragestatistic/startletters";
        public static final int PATH_START_LETTERS_TOKEN = 42;

        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH).build();

        public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.gr.liakos.spearo.app";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.gr.liakos.spearo.app";

        public static class Cols {
            public static final String FISHAVGID = BaseColumns._ID; // by convention
            public static final String FISHID = "fishid";
            public static final String TOTAL_CATCHES = "totalCatches";
            public static final String AVG_WEIGHT = "avgweight";
            public static final String AVG_DEPTH = "avgdepth";
            public static final String MOST_COMMON_SUMMER_HOUR = "mostCommonSummerHour";
            public static final String MOST_COMMON_WINTER_HOUR = "mostCommonWinterHour";
            public static final String RECORD_WEIGHT = "recordweight";
        }

        protected static UriMatcher addToUriMatcher(String authority, UriMatcher matcher) {
            matcher.addURI(authority, FishAverageStatistic.PATH, FishAverageStatistic.PATH_TOKEN);
            matcher.addURI(authority, FishAverageStatistic.PATH_FOR_ID, FishAverageStatistic.PATH_FOR_ID_TOKEN);
            matcher.addURI(authority, FishAverageStatistic.PATH_FOR_ID_WA, FishAverageStatistic.PATH_FOR_ID_TOKEN);
            matcher.addURI(authority, FishAverageStatistic.PATH_START_LETTERS, FishAverageStatistic.PATH_START_LETTERS_TOKEN);
            return matcher;
        }

        public static String createTable() {
            return "CREATE TABLE " + FishAverageStatistic.TABLE_NAME + " ( "
                    + String.format(sFrmIdAutoinc, Cols.FISHAVGID) + " , "
                    + String.format(sFrmIntNotNull, Cols.FISHID) + " , "
                    + String.format(sFrmInt, Cols.TOTAL_CATCHES) + " , "
                    + String.format(sFrmDouble, Cols.RECORD_WEIGHT) + " , "
                    + String.format(sFrmInt, Cols.MOST_COMMON_SUMMER_HOUR) + " , "
                    + String.format(sFrmInt, Cols.MOST_COMMON_WINTER_HOUR) + " , "
                    + String.format(sFrmDouble, Cols.AVG_DEPTH) + " , "
                    + String.format(sFrmDouble, Cols.AVG_WEIGHT) + " , "

                    + String.format(sFrmPrimaryKey, Cols.FISHAVGID) + ");";
        }
    }

}
