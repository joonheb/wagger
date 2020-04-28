package com.example.afinal.dialog;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.afinal.R;

public class CustomDialogFragment extends DialogFragment{
    private static final String TAG = "CustomDialogFragment";
    private static final String ARG_DIALOG_MAIN_MSG = "dialog_main_msg";

    private String mMainMsg;

    private TextView confirm_btn;

    public static CustomDialogFragment newInstance(String mainMsg) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_DIALOG_MAIN_MSG, mainMsg);

        CustomDialogFragment fragment = new CustomDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMainMsg = getArguments().getString(ARG_DIALOG_MAIN_MSG);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.layout_custom_dialog, null);

        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    public TextView getConfirm_btn() {
        return confirm_btn;
    }

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_custom_dialog, container, false);
        ((TextView)view.findViewById(R.id.dialog_confirm_msg)).setText(mMainMsg);
        view.findViewById(R.id.dialog_confirm_btn).setOnClickListener(this);
        Dialog dialog = getDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        return view;
    }
*/
}
