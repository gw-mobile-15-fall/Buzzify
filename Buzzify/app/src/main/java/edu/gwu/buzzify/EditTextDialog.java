package edu.gwu.buzzify;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Displays a simple dialog comprising of an EditText. Used to order drinks.
 */
public class EditTextDialog extends DialogFragment {
    /**
     * Interface to receive button press callbacks
     */
    public interface EditTextDialogListener {
        void onPositiveClicked(String input);
        void onCancelClicked();
    }

    private EditTextDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Create the main EditText
        final EditText et = new EditText(getActivity());
        et.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        et.setPadding(10, 20, 10, 0);

        builder.setTitle(getString(R.string.order_a_drink));
        builder.setView(et);
        builder.setPositiveButton(R.string.order, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Deliver the inputted text to the listener
                if(mListener != null)
                    mListener.onPositiveClicked(et.getText().toString());
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mListener != null)
                    mListener.onCancelClicked();
                getDialog().cancel();
            }
        });

        return builder.create();
    }

    public void onAttach(Context context){
        super.onAttach(context);

        if(context instanceof EditTextDialogListener)
            mListener = (EditTextDialogListener)context;
    }
}
