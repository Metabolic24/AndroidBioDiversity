package com.m2dl.biodiversity.biodiversity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class LoginDialog extends DialogFragment {


    // Use this instance of the interface to deliver action events
    private NoticeDialogListener mListener;
    private String loginChosen;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        loginChosen = null;
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View content = inflater.inflate(R.layout.login_dialog, null);
        final EditText txtLogin = (EditText) content.findViewById(R.id.txtLogin);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        setStyle(STYLE_NORMAL, getTheme());
        setCancelable(false);
        builder.setView(content)
                // Add action buttons
                .setPositiveButton(getString(R.string.action_login), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        loginChosen = txtLogin.getText().toString();
                        if (!loginChosen.isEmpty()) {
                            mListener.onDialogPositiveClick(LoginDialog.this);
                        }
                    }
                })
                .setNegativeButton(getString(R.string.action_quit), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(LoginDialog.this);
                    }
                });
        return builder.create();
    }

    public String getLoginChosen() {
        return loginChosen;
    }

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);

        public void onDialogNegativeClick(DialogFragment dialog);
    }
}
