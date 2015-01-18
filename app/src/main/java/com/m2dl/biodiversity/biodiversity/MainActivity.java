package com.m2dl.biodiversity.biodiversity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends ActionBarActivity implements View.OnTouchListener, LoginDialog.NoticeDialogListener {

    private static final int CAPTURE_IMAGE = 5654;
    private static final int REQUEST_SEND_MAIL = 100;
    private static final int KEY_SELECTION = 303;


    private Uri imageUri;
    private CustomImageView iv;
    private RectF current = null;
    private String comment = "";
    private Bitmap bitmap = null;
    private String login = "";

    private SharedPreferences settings;

    private LoginDialog loginDialog;

    private float srcX, srcY, destX, destY = -1f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences("PREFS_BIODIVERSITY",0);

        showLoginDialog();
    }

    public void showLoginDialog() {
        // Create an instance of the dialog fragment and show it
        login = settings.getString("login", null);
        if(login == null) {
            loginDialog = new LoginDialog();
            loginDialog.show(getFragmentManager(), "LoginDialog");
        }
        else {
            startPhoto();
        }
    }

    public void startPhoto() {
        iv = (CustomImageView) findViewById(R.id.imageView);
        iv.setOnTouchListener(this);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAPTURE_IMAGE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //Si l'activité était une prise de photo
            case CAPTURE_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageUri;
                    getContentResolver().notifyChange(selectedImage, null);
                    ContentResolver cr = getContentResolver();
                    try {
                        bitmap = android.provider.MediaStore.Images.Media
                                .getBitmap(cr, selectedImage);

                        iv.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }
                }
                break;
            case KEY_SELECTION:
                if (resultCode == RESULT_OK) {
                    iv.addRectangle(current, true);
                    iv.invalidate();
                    showComment(true);
                }
                current = null;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.editer_commentaire) {
            showComment(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
                srcX = -1f;
                srcY = -1f;
                destX = -1f;
                destY = -1f;
                current = null;
                break;
            case MotionEvent.ACTION_DOWN:
                srcX = event.getX();
                srcY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (srcX != -1f && srcY != -1f) {
                    if (current != null) {
                        iv.removeRectangle();
                    }

                    destX = event.getX();
                    destY = event.getY();
                    current = new RectF(srcX, srcY, destX, destY);

                    iv.addRectangle(current, false);
                    iv.invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (current != null) {
                    iv.removeRectangle();
                }

                if (destX != -1f && destY != -1f) {
                    if (srcX == destX) {
                        srcX = srcX - 5;
                        destX = destX + 5;
                    }

                    if (srcY == destY) {
                        srcY = srcY - 5;
                        destY = destY + 5;
                    }
                    current = new RectF(srcX, srcY, destX, destY);
                    destX = -1f;
                    destY = -1f;
                } else {
                    current = new RectF(srcX - 5, srcY - 5, srcX + 5, srcY + 5);
                }

                srcX = -1f;
                srcY = -1f;

                Intent nextIntent = new Intent(this, KeySelectionActivity.class);
                startActivityForResult(nextIntent, KEY_SELECTION);
                break;
        }

        return true;
    }

    public void showComment(final boolean isKeySet) {
        String negButtonTitle = isKeySet ?
                getString(R.string.action_pass) :
                getString(R.string.action_cancel);

        String posButtonTitle = isKeySet ?
                getString(R.string.action_next) :
                getString(R.string.action_ok);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Commentaire");
        alert.setMessage("Editer le commentaire");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        if (!comment.isEmpty()) {
            input.setText(comment, TextView.BufferType.EDITABLE);
        }

        alert.setView(input);

        final Intent nextIntent = new Intent(this, SenderActivity.class);

        alert.setPositiveButton(posButtonTitle, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                comment = input.getText().toString();

                if (isKeySet) {
                    startActivity(nextIntent);
                }
            }
        });

        alert.setNegativeButton(negButtonTitle, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (isKeySet) {
                    startActivity(nextIntent);
                }
            }
        });

        alert.show();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        login = loginDialog.getLoginChosen();
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("login", login);
        editor.commit();

        startPhoto();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        System.exit(RESULT_OK);
    }
}
