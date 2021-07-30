package gr.liakos.spearo.billing;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gr.liakos.spearo.R;
import gr.liakos.spearo.def.AsyncListener;
import gr.liakos.spearo.util.BillingSecurity;

import static com.android.billingclient.api.BillingClient.SkuType.INAPP;
import static gr.liakos.spearo.util.Constants.SKU_PREMIUM_STATS;

public class BillingHelper implements PurchasesUpdatedListener {

    private Activity activity;

    private BillingClient billingClient;

    private AsyncListener asyncListener;

    Purchase purchase;

    public BillingHelper(Activity activity){
        this.activity = activity;
        billingClient = BillingClient.newBuilder(activity)
                .enablePendingPurchases().setListener(this).build();
    }

    public void queryInventory(AsyncListener listener){

        asyncListener = listener;
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {


                if(billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK){
                    Purchase.PurchasesResult queryPurchase = billingClient.queryPurchases(INAPP);
                    List<Purchase> queryPurchases = queryPurchase.getPurchasesList();
                    if(queryPurchases!=null && queryPurchases.size()>0){
                        handlePurchases(queryPurchases);
                    }
                    //if purchase list is empty that means item is not purchased
                    //Or purchase is refunded or canceled
                    else{
                        listener.onPurchaseStatsAttemptFinished(false);
                        // savePurchaseValueToPref(false);
                    }
                }else{
                    listener.onPurchaseStatsAttemptFinished(false);
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                //Toast.makeText(activity, "DISCONNECTED", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void purchase() {
        //check if service is already connected
        if (billingClient.isReady()) {
            initiatePurchase();
        }
        //else reconnect service
        else{
            billingClient = BillingClient.newBuilder(activity).enablePendingPurchases().setListener(this).build();
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(BillingResult billingResult) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase();
                    } else {

                        if (null != asyncListener){
                           asyncListener.onPurchaseStatsAttemptFinished(false);
                        }

                       // Toast.makeText(activity.getApplicationContext(),"Error "+billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onBillingServiceDisconnected() {
                }
            });
        }
    }

    private void initiatePurchase() {
        List<String> skuList = new ArrayList<>();
        skuList.add(SKU_PREMIUM_STATS);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(INAPP);
        billingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(@NonNull BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            if (skuDetailsList != null && skuDetailsList.size() > 0) {
                                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                        .setSkuDetails(skuDetailsList.get(0))
                                        .build();
                                billingClient.launchBillingFlow(activity, flowParams);
                            }
                            else{

                                if (null != asyncListener){
                                    asyncListener.onPurchaseStatsAttemptFinished(false);
                                }
                                //try to add item/product id "consumable" inside managed product in google play console
                                //Toast.makeText(activity.getApplicationContext(),"Purchase Item not Found",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //Toast.makeText(activity.getApplicationContext(),
                              //      " Error "+billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();

                            if (null != asyncListener){
                                asyncListener.onPurchaseStatsAttemptFinished(false);
                            }

                        }
                    }
                });
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        //if item newly purchased
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases);
        }
        //if item already purchased then check and reflect changes
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            Purchase.PurchasesResult queryAlreadyPurchasesResult = billingClient.queryPurchases(INAPP);
            List<Purchase> alreadyPurchases = queryAlreadyPurchasesResult.getPurchasesList();
            if(alreadyPurchases!=null){
                handlePurchases(alreadyPurchases);
            }
        }
        //if purchase cancelled
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            if (null != asyncListener){
                asyncListener.onPurchaseStatsAttemptFinished(false);
            }
            //Toast.makeText(activity.getApplicationContext(),"Purchase Canceled",Toast.LENGTH_SHORT).show();
        }
        // Handle any other error msgs
        else {
            if (null != asyncListener){
                asyncListener.onPurchaseStatsAttemptFinished(false);
            }
            //Toast.makeText(activity.getApplicationContext(),"Error "+billingResult.getDebugMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    void handlePurchases(List<Purchase>  purchases) {
        for(Purchase purchase:purchases) {
            //if item is purchased
            if (SKU_PREMIUM_STATS.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED)
            {

                this.purchase = purchase;

                if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
                    // Invalid purchase
                    // show error to user
                    asyncListener.onPurchaseStatsAttemptFinished(false);
                    //Toast.makeText(activity.getApplicationContext(), "Error : Invalid Purchase", Toast.LENGTH_SHORT).show();
                    return;
                }
                // else purchase is valid
                //if item is purchased and not acknowledged
                if (!purchase.isAcknowledged()) {
                    AcknowledgePurchaseParams acknowledgePurchaseParams =
                            AcknowledgePurchaseParams.newBuilder()
                                    .setPurchaseToken(purchase.getPurchaseToken())
                                    .build();
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams, ackPurchase);
                }
                //else item is purchased and also acknowledged
                else {
                    asyncListener.onPurchaseStatsAttemptFinished(true);
                }
            }
            //if purchase is pending
            else if( SKU_PREMIUM_STATS.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.PENDING)
            {
                asyncListener.onPurchaseStatsAttemptFinished(false);
                Toast.makeText(activity.getApplicationContext(),
                        "Purchase is Pending. Please complete Transaction", Toast.LENGTH_SHORT).show();
            }
            //if purchase is unknown
            else if(SKU_PREMIUM_STATS.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.UNSPECIFIED_STATE)
            {
                asyncListener.onPurchaseStatsAttemptFinished(false);
                Toast.makeText(activity.getApplicationContext(), "Purchase Status Unknown", Toast.LENGTH_SHORT).show();
            }
        }
    }

    AcknowledgePurchaseResponseListener ackPurchase = new AcknowledgePurchaseResponseListener() {
        @Override
        public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
            if(billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK){
                //Toast.makeText(activity, "ACK", Toast.LENGTH_SHORT).show();
                asyncListener.onPurchaseStatsAttemptFinished(true);
            }else{
                asyncListener.onPurchaseStatsAttemptFinished(false);
            }
        }
    };

//    ConsumeResponseListener consumeListener = new ConsumeResponseListener() {
//        @Override
//        public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
//            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//             asyncListener.onPurchaseStatsAttemptFinished(false);
//                Toast.makeText(activity.getApplicationContext(), "Item Consumed", Toast.LENGTH_SHORT).show();
//            }else{
//                asyncListener.onPurchaseStatsAttemptFinished(false);
//            }
//        }
//    };

    //initiate purchase on consume button click
//    public void consume(View view) {
//
//        if (purchase!=null){
//            ConsumeParams consumeParams = ConsumeParams.newBuilder()
//                    .setPurchaseToken(purchase.getPurchaseToken())
//                    .build();
//
//            billingClient.consumeAsync(consumeParams, consumeListener);
//        }
//
//    }

    /**
     * Verifies that the purchase was signed correctly for this developer's public key.
     * <p>Note: It's strongly recommended to perform such check on your backend since hackers can
     * replace this method with "constant true" if they decompile/rebuild your app.
     * </p>
     */
    private boolean verifyValidSignature(String signedData, String signature) {
        try {
            // To get key go to Developer Console > Select your app > Development Tools > Services & APIs.
            String base64Key = activity.getString(R.string.monetization_id);
            return BillingSecurity.verifyPurchase(base64Key, signedData, signature);
        } catch (IOException e) {
            return false;
        }
    }

    public void endConnection() {
        if(billingClient!=null){
            billingClient.endConnection();
        }
    }
}
