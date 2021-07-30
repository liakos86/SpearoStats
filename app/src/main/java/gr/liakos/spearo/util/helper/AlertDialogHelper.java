package gr.liakos.spearo.util.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;


public class AlertDialogHelper {

    public static void alertDialogWithPositive(Activity act, String positiveString, DialogInterface.OnClickListener posListener, String message){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(act);

        if (posListener == null){
            posListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            };
        }

        dialogBuilder.setPositiveButton(positiveString, posListener);
        dialogBuilder.setMessage(message);
        dialogBuilder.create().show();
    }

}
