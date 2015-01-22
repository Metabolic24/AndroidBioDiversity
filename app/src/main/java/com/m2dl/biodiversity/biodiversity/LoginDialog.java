package com.m2dl.biodiversity.biodiversity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Provides a dialog for writing the login
 */
public class LoginDialog extends DialogFragment {

    // Use this instance of the interface to deliver action events
    private LoginDialogListener mListener;
    private String loginChosen;
    private boolean fromMenu;

    public LoginDialog() {
        super();
    }

    //Permet de savoir si cette Dialog a été appelée depuis le menu
    public boolean isFromMenu() {
        return fromMenu;
    }

    //Permet de définir si l'origine de l'action est le menu
    public void setFromMenu(boolean fromMenu) {
        this.fromMenu = fromMenu;
    }

    // Override the Fragment.onAttach() method to instantiate the LoginDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        loginChosen = null;
        try { //Verify the activity implements LoginDialogListener
            mListener = (LoginDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Get the layout inflater, pass null as parent view because it's going in the dialog layout
        View content = getActivity().getLayoutInflater().inflate(R.layout.login_dialog, null);

        final EditText txtLogin = (EditText) content.findViewById(R.id.txtLogin);
        // Inflate and set the layout for the dialog
        setStyle(STYLE_NORMAL, getTheme());

        if (!fromMenu) {
            setCancelable(false);
        }


        String quit_Title = fromMenu ? getString(R.string.action_cancel) : getString(R.string.action_quit);

        builder.setView(content)
                // Add action buttons
                .setPositiveButton(getString(R.string.action_login), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        loginChosen = txtLogin.getText().toString();
                        if (!loginChosen.isEmpty()) {
                            mListener.onConnectClick(LoginDialog.this);
                        } else {
                            Toast.makeText(LoginDialog.this.getActivity(), "Login cannot be empty", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(quit_Title, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onCancelClick(LoginDialog.this);
                    }
                });
        return builder.create();
    }

    public String getLoginChosen() {
        return loginChosen;
    }

    /**
     * Interface for listening to the result of this dialog
     */
    public interface LoginDialogListener {
        /**
         * If the connect button was chosen by the user
         *
         * @param dialog the dialog
         */
        public void onConnectClick(DialogFragment dialog);

        /**
         * If the cancel button was chosen by the user
         *
         * @param dialog the dialog
         */
        public void onCancelClick(DialogFragment dialog);
    }
}
