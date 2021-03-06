package com.m2dl.biodiversity.biodiversity;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activité Principale de l'Application
 */
public class MainActivity extends ActionBarActivity implements CustomImageView.KeyLauncher, LoginDialog.LoginDialogListener {

    private static final int CAPTURE_IMAGE = 5654;
    private static final int KEY_SELECTION = 303;

    private CustomImageView iv;
    private SharedPreferences settings;
    private LoginDialog loginDialog;

    private UserInformation userInfo;

    /**
     * Constructeur
     */
    public MainActivity() {
        super();
        userInfo = new UserInformation();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences("PREFS_BIODIVERSITY", 0);

        showLoginDialog(false);
    }

    /* Effectue un traitement après la fin d'une activité : */

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //Photo
            case CAPTURE_IMAGE:
                if (resultCode == RESULT_OK) {
                    /*Uri selectedImage = imageUri;
                    getContentResolver().notifyChange(selectedImage, null);*/
                    ContentResolver cr = getContentResolver();
                    try {
                        userInfo.setImage(android.provider.MediaStore.Images.Media
                                .getBitmap(cr, data.getData()));
                        iv.setImageBitmap(userInfo.getImage());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }

                    LocationManager lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
                    if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        userInfo.setLocation(lm.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                    } else {
                        userInfo.setLocation(null);
                    }
                } else {
                    System.exit(RESULT_CANCELED);
                }
                break;
            //Clé de détermination
            case KEY_SELECTION:
                iv.finalize(resultCode == RESULT_OK);
                if (resultCode == RESULT_OK) {
                    showComment(true);
                }
                break;
        }

    }


    /************/
    /****MENU****/
    /**
     * ********
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.editer_commentaire:
                showComment(false);
                return true;
            case R.id.change_login:
                showLoginDialog(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*************/
    /****LOGIN****/
    /*************/

    /**
     * Show the login dialog
     *
     * @param fromMenu called from the Menu
     */
    public void showLoginDialog(boolean fromMenu) {
        // Create an instance of the dialog fragment and show it
        userInfo.setLogin(settings.getString("login", null));

        //Si le login n'est pas déjà spécifié
        //Ou que l'appel a été déclenché par le menu
        if (userInfo.getLogin() == null || fromMenu) {
            loginDialog = new LoginDialog();
            loginDialog.setFromMenu(fromMenu);
            loginDialog.show(getFragmentManager(), "LoginDialog");
        }
        //Sinon on déclenche directement l'activité de prise de photo
        else {
            startPhoto();
        }
    }

    @Override
    public void onConnectClick(DialogFragment dialog) {
        userInfo.setLogin(loginDialog.getLoginChosen());

        SharedPreferences.Editor editor = settings.edit();
        editor.putString("login", userInfo.getLogin());
        editor.commit();

        if (!loginDialog.isFromMenu()) {
            startPhoto();
        }
    }

    @Override
    public void onCancelClick(DialogFragment dialog) {
        if (!loginDialog.isFromMenu()) {
            System.exit(RESULT_CANCELED);
        }
    }


    /*************/
    /****PHOTO****/
    /**
     * *********
     */

    public void startPhoto() {
        iv = (CustomImageView) findViewById(R.id.imageView);
        iv.setLauncher(this);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE);
    }


    /*************/
    /*****CLE*****/
    /**
     * *********
     */

    @Override
    public void launch() {
        Intent nextIntent = new Intent(this, KeySelectionActivity.class);
        nextIntent.putExtra("USER_INFORMATION", userInfo);
        startActivityForResult(nextIntent, KEY_SELECTION);
    }


    /*************/
    /*COMMENTAIRE*/
    /*************/

    /**
     * Show the comment dialog
     *
     * @param isKeySet true if the key is already set, false otherwise
     */
    public void showComment(final boolean isKeySet) {

        //Initialisation des titres des boutons selon le cas
        String negButtonTitle = isKeySet ?
                getString(R.string.action_pass) :
                getString(R.string.action_cancel);

        String posButtonTitle = isKeySet ?
                getString(R.string.action_next) :
                getString(R.string.action_ok);

        //Création de la Dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Commentaire");
        alert.setMessage("Editer le commentaire");

        // Création d'une zone d'édition de texte
        final EditText input = new EditText(this);

        if (!userInfo.getComment().isEmpty()) {
            input.setText(userInfo.getComment(), TextView.BufferType.EDITABLE);
        }

        alert.setView(input);

        if (isKeySet) {
            alert.setCancelable(false);
        }

        //Préparation de l'intent pour le lancement de la prochaine activité
        final Intent nextIntent = new Intent(this, SenderActivity.class);
        nextIntent.putExtra("USER_INFORMATION", userInfo);


        //Définition du comportement des boutons de la Dialog
        alert.setPositiveButton(posButtonTitle, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                userInfo.setComment(input.getText().toString());

                if (isKeySet) {
                    userInfo.saveImageToDir(getCacheDir());
                    startActivity(nextIntent);
                }
            }
        });

        alert.setNegativeButton(negButtonTitle, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (isKeySet) {
                    userInfo.saveImageToDir(getCacheDir());
                    startActivity(nextIntent);
                }
            }
        });

        alert.show();
    }
}
