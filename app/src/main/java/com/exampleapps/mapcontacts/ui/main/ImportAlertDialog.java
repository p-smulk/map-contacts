package com.exampleapps.mapcontacts.ui.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.exampleapps.mapcontacts.R;

public class ImportAlertDialog extends DialogFragment {

    public static final String TAG = "Im portAlertDialog";

    private MainActivity mActivity;

    public static ImportAlertDialog newInstance(Bundle bundle){
    //    Bundle args = new Bundle();
        ImportAlertDialog dialog = new ImportAlertDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity){
            MainActivity mainActivity = (MainActivity) context;
            this.mActivity = mainActivity;
            mainActivity.onFragmentAttached();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int resId = -101;

        if (getArguments() != null) {
            resId = getArguments().getInt("import_alert_db_status");
        }

        int finalResId = resId;
        return new AlertDialog.Builder(getActivity())
                .setTitle("Importing contacts")
                .setMessage(resId)
                .setPositiveButton("OK", ((dialog, which) -> {
                    mActivity.importAlertDialogResponse(AlertDialogResult.OK);
                    this.dismiss();
                }))
                .setNegativeButton("Cancel", ((dialog, which) -> {
                    if (finalResId == R.string.import_alert_empty_db) {
                        mActivity.importAlertDialogResponse(AlertDialogResult.CANCEL_EMPTY_DB);
                    } else {
                        mActivity.importAlertDialogResponse(AlertDialogResult.CANCEL_HAS_DB);
                    }
                    this.dismiss();
                })).create();
    }

    enum AlertDialogResult {
        OK,
        CANCEL_EMPTY_DB,
        CANCEL_HAS_DB
    }

    @Override
    public void onDestroyView() {
        mActivity = null;
        super.onDestroyView();
    }
}
