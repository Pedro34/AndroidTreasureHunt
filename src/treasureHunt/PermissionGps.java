package treasureHunt;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Activité permettant d'informer l'utilisateur que l'application a
 * besoin de l'activation du GPS si celui-ci n'est pas activé.
 * 
 * @author Burc Pierre, Duplouy Olivier
 *
 */
public class PermissionGps extends Activity {

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        createGpsDisabledAlert();
    }
    
    private void createGpsDisabledAlert() {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder
            .setMessage("Le jeu a besoin d'utiliser le GPS.")
            .setCancelable(false)
            .setPositiveButton("Activer le GPS ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        PermissionGps.this.showGpsOptions();
                    }
                }
            );
        localBuilder.create().show();
    }

    private void showGpsOptions() {
        startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
        finish();
    }

}
