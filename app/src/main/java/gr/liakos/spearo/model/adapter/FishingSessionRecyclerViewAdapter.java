package gr.liakos.spearo.model.adapter;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cj.videoprogressview.LightProgressView;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import gr.liakos.spearo.ActSpearoStatsMain;
import gr.liakos.spearo.R;
import gr.liakos.spearo.SpearoApplication;
import gr.liakos.spearo.enums.MoonPhase;
import gr.liakos.spearo.enums.Wind;
import gr.liakos.spearo.enums.WindVolume;
import gr.liakos.spearo.fragment.FrgFishingSessions;
import gr.liakos.spearo.model.Database;
import gr.liakos.spearo.model.object.FishingSession;
import gr.liakos.spearo.util.Constants;
import gr.liakos.spearo.util.DateUtils;
import gr.liakos.spearo.util.MoonPhaseUtil;
import gr.liakos.spearo.util.SpearoUtils;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;


public class FishingSessionRecyclerViewAdapter extends RecyclerView.Adapter {

    List<FishingSession> mValues;
    Context mContext;
    FrgFishingSessions fragment;

    public FishingSessionRecyclerViewAdapter(Context context, List<FishingSession> values, FrgFishingSessions frg) {
        mValues = values;
        mContext = context;
        this.fragment = frg;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView fishingSessionIcon;
        TextView sessionCatchesNumText;
        TextView sessionDateText;
        TextView sessionWindText;
        LightProgressView moonPercentageView;
        Button shareButton;
        Float moonPercentage = null;
        MoonPhase moonPhase = null;
        Bitmap sessionImage = null;
        boolean validUriPath;
        FishingSession item;

        public ViewHolder(View v) {
            super(v);

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            sessionCatchesNumText =  v.findViewById(R.id.sessionCatchesNumText);
            sessionDateText = v.findViewById(R.id.sessionDateText);
            sessionWindText = v.findViewById(R.id.sessionWindText);
            fishingSessionIcon = v.findViewById(R.id.fishingSessionRowIcon);
            moonPercentageView = v.findViewById(R.id.moon_percentage);
            shareButton = v.findViewById(R.id.shareButton);
        }

        public void setData(FishingSession item) {
            this.item = item;

            sessionCatchesNumText.setText(String.valueOf(item.getFishCatches().size()));
            String dateString = DateUtils.dateFromMillis(item.getFishingDate());
            sessionDateText.setText(dateString);
            Resources res = mContext.getResources();
            setWindTextAndImg(item, res);
            setSessionImg(item, res);
            setMoonPhaseTextAndImg(item);
            setShareButton();
            showFbShowcase();
        }


        private void setShareButton() {
            if (sessionImage == null && !validUriPath){
                shareButton.setVisibility(View.GONE);
                shareButton.setOnClickListener(null);
            }else {
                shareButton.setVisibility(View.VISIBLE);
                shareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doShare();
                    }
                });
            }
        }

        private void showFbShowcase() {

            if (item.getSessionImageUriPath() == null){
                return;
            }

            new MaterialShowcaseView.Builder(fragment.getActivity())
                    .setTarget(shareButton)
                    .setDismissText(android.R.string.ok)
                    .setContentText(fragment.getResources().getString(R.string.showcase_fb))
                    .setDelay(100)
                    .singleUse(Constants.SHOWCASE_FB)
                    .show();

        }

        /**
         * A deleted image can still be cached in Glide.
         * The validUriPath will be true, but the attempt to parse the uri path will throw an exception.
         */
        void doShare() {
            Bitmap bitmap =  null;
            if (validUriPath){
                try {
                    ContentResolver cr = fragment.getActivity().getBaseContext().getContentResolver();
                    InputStream inputStream = cr.openInputStream(Uri.parse(item.getSessionImageUriPath()));
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inScaled = false;
                    bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                } catch (Exception e) {
                    bitmap = null;
                }
            }else{
                bitmap = sessionImage;
            }

            share(bitmap);
        }

        void share(Bitmap bitmap) {
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();

            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .setShareHashtag(new ShareHashtag.Builder().setHashtag("#SpearoStats").build())
                    .setPageId(Constants.FACEBOOK_APP_ID)
                    .build();

            ((ActSpearoStatsMain) fragment.getActivity()).tryToShare(content);
        }

        void setWindTextAndImg(FishingSession fishingSession,
                               Resources res) {

            Wind sessionWind = fishingSession.getSessionWind();
            WindVolume sessionWindVolume = fishingSession.getSessionWindVolume();
            boolean noWind = Wind.NOT_KNOWN.equals(sessionWind);
            boolean noVolume = WindVolume.NOT_KNOWN.equals(sessionWindVolume);
            if (noWind && noVolume){
                sessionWindText.setVisibility(View.INVISIBLE);
            }else{
                sessionWindText.setVisibility(View.VISIBLE);
            }

            String windText = noWind ? Constants.EMPTY : res.getString(sessionWind.getShortDesc());
            String volText = noVolume ? Constants.EMPTY : fishingSession.getSessionWindVolume().getPosition() + Constants.BFT;
            String finalWindText = !noWind && !noVolume ? windText + Constants.SLASH + volText : windText + volText;
            sessionWindText.setText(finalWindText);
            sessionWindText.setCompoundDrawablesWithIntrinsicBounds(0, 0, fishingSession.getSessionWind().getDrawable(), 0);
        }

        void setMoonPhaseTextAndImg(FishingSession fishingSession) {
                Calendar sessionCal = DateUtils.getCalendarWithTime(fishingSession.getFishingDate());
                sessionCal.set(Calendar.HOUR_OF_DAY, 12);
                MoonPhaseUtil util = new MoonPhaseUtil(sessionCal);
                double percentage = util.getPhase();
                moonPercentage = (float) (percentage/100);
                moonPercentage = moonPercentage < 0 ? -moonPercentage : moonPercentage;
                moonPercentageView.setProgress(moonPercentage);
                moonPhase = MoonPhase.ofPosition(util.getPhaseIndex());
        }

        /**
         * If there is no image set, set icon to the first fish.
         * If there is a bytearray or uri path for image try to set it.
         * If user has deleted the image, catch the exception and set the first fish icon.
         *
         * Glide uses cache, so a deleted image will not disappear instantly.
         *
         * @param fishingSession
         * @param res
         */
        void setSessionImg(FishingSession fishingSession, Resources res) {
            sessionImage = null;
            validUriPath = false;

            if (fishingSession.getSessionImage() == null && fishingSession.getSessionImageUriPath()==null){//no image bytes or uri
                setFirstFishIcon(fishingSession, res);
            }else{
                String uriPath = fishingSession.getSessionImageUriPath();
                if (uriPath != null){
                    try {
                        Glide.with(mContext).asBitmap().load(uriPath).into(fishingSessionIcon);
                        validUriPath = true;
                    }catch(Exception e){
                        validUriPath = false;
                        setFirstFishIcon(fishingSession, res);
                    }
                }else {// session image not null
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inScaled = false;
                    byte[] array = Base64.decode(fishingSession.getSessionImage(), Base64.NO_WRAP);
                    Bitmap bmp = BitmapFactory.decodeByteArray(array, 0, array.length, options);
                    fishingSessionIcon.setImageBitmap(bmp);
                    sessionImage = bmp;
                }
            }
        }

        private void setFirstFishIcon(FishingSession fishingSession, Resources res) {
            Drawable drawableFromFish = null;
            if (fishingSession.getFishCatches().isEmpty()){
                drawableFromFish = ResourcesCompat.getDrawable(res, R.drawable.jelly_grid, null);
            }else{
                drawableFromFish = new SpearoUtils(mContext).getGridDrawableFromFish(fishingSession.getFishCatches().get(0).getFish());
            }

            Glide.with(mContext).load(drawableFromFish).into(fishingSessionIcon);

        }

        @Override
        public void onClick(View view) {
            if (item.getFishCatches().isEmpty()){
                return;
            }

            showAlertDialogFor(item);
        }

        @Override
        public boolean onLongClick(View v) {
            confirmDeleteAlertDialog(item);
            return false;
        }

        void confirmDeleteAlertDialog(final FishingSession fishingSession) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(fragment.getActivity());
            Resources res = fragment.getActivity().getResources();
            dialogBuilder.setPositiveButton(res.getString(R.string.yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ((SpearoApplication) fragment.getActivity().getApplication()).deleteSession(fishingSession);
                }
            })
            .setNegativeButton(res.getString(R.string.no), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }

            })
            .setMessage(res.getString(R.string.delete_session_entry_alert));//spearoUtils.getStringResourceByName( "cancel_session_entry_alert"));

            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

        }



        void showAlertDialogFor(FishingSession fishingSession) {
            FragmentActivity activity = fragment.getActivity();
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
            View dialogView = activity.getLayoutInflater().inflate(R.layout.custom_fish_session_alert_dialog, null);
            Rect displayRectangle = new Rect();
            Window window = activity.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

            dialogView.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
            dialogView.setMinimumHeight((int)(displayRectangle.height() * 0.6f));

            TextView textViewYear = dialogView.findViewById(R.id.textSessionYear);
            Calendar cal = DateUtils.getCalendarWithTime(fishingSession.getFishingDate());
            textViewYear.setText(String.valueOf(cal.get(Calendar.YEAR)));
            TextView textViewDayAndMonth = dialogView.findViewById(R.id.textSessionDayAndMonth);
            textViewDayAndMonth.setText(DateUtils.getDayAndMonth(cal, mContext));

            LightProgressView moonPercentageViewAlert = dialogView.findViewById(R.id.moon_percentage_alert);
            TextView textViewMoon = dialogView.findViewById(R.id.textSessionMoon);
            Resources resources = mContext.getResources();
            if (moonPercentage != null) {
                moonPercentageViewAlert.setProgress(moonPercentage);
                textViewMoon.setText(resources.getText(moonPhase.getAlertText()));
            }

            TextView textViewWind = (TextView) dialogView.findViewById(R.id.textSessionWind);
            if (Wind.NOT_KNOWN.equals(fishingSession.getSessionWind())
            && WindVolume.NOT_KNOWN.equals(fishingSession.getSessionWindVolume())){
                textViewWind.setVisibility(View.INVISIBLE);
            }else{
                textViewWind.setVisibility(View.VISIBLE);
                textViewWind.setCompoundDrawablesWithIntrinsicBounds(0, 0, fishingSession.getSessionWind().getDrawable(), 0);

                boolean noWind = Wind.NOT_KNOWN.equals(fishingSession.getSessionWind());
                boolean noVol = WindVolume.NOT_KNOWN.equals(fishingSession.getSessionWindVolume());
                String windText = noWind ? Constants.EMPTY : resources.getString(fishingSession.getSessionWind().getShortDesc());
                String volText = noVol ? Constants.EMPTY : fishingSession.getSessionWindVolume().getPosition() + Constants.BFT;
                String finalWindText = !noWind && !noVol ? windText + Constants.SLASH + volText : windText + volText;
                textViewWind.setText(finalWindText);
            }

            ListView catchesListView = dialogView.findViewById(R.id.listViewSessionCatchesComp);
            FishingSessionCatchesAdapter catchesAdapter = new FishingSessionCatchesAdapter(activity, R.layout.fish_catch_row_with_delete, fishingSession.getFishCatches());
            catchesListView.setAdapter(catchesAdapter);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setNegativeButton(
                    activity.getResources().getString(R.string.close),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    public FishingSessionRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.gridview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).setData(mValues.get(position));
    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(FishingSession item);
    }
}
