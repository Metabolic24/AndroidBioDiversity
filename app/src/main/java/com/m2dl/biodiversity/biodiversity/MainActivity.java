package com.m2dl.biodiversity.biodiversity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends ActionBarActivity implements View.OnTouchListener {

    private static final int CAPTURE_IMAGE = 5654;
    private static final int REQUEST_SEND_MAIL = 100;

    private Uri imageUri;
    private CustomImageView iv;
    private RectF current = null;
    private String comment = "";

    private float srcX, srcY, destX, destY = -1f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Handler mHand = new Handler();
        final MainActivity var = this;
        mHand.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(var, SenderActivity.class));
            }
        }, 5000);*/

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
        Toast.makeText(this, imageUri.toString(), Toast.LENGTH_LONG).show();

        switch (requestCode) {
            //Si l'activité était une prise de photo
            case CAPTURE_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageUri;
                    getContentResolver().notifyChange(selectedImage, null);
                    ContentResolver cr = getContentResolver();
                    Bitmap bitmap;
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
            showComment();
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
                        iv.removeRectangle(current);
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
                    iv.removeRectangle(current);
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

                iv.addRectangle(current, true);
                iv.invalidate();

                srcX = -1f;
                srcY = -1f;
                current = null;

                break;
        }

        return true;
    }

    public void showComment() {
        setContentView(R.layout.showcomment);

        final EditText editText = (EditText)findViewById(R.id.editText);

        if (!comment.isEmpty()) {
            editText.setText(comment, TextView.BufferType.EDITABLE);
        }

        Button valider = (Button)findViewById(R.id.button);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = editText.getText().toString();
                setContentView(R.layout.activity_main);
            }
        });

        Button annuler = (Button)findViewById(R.id.button);
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
            }
        });

    }
}
