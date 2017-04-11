package com.elfec.cobranza.view.services;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.elfec.cobranza.R;
import com.pnikosis.materialishprogress.ProgressWheel;

import static com.elfec.cobranza.R.layout.progress_dialog_material;

/**
 * Dialogo de progreso indefinido
 */
public class ProgressDialogService implements DialogInterface {

    private AlertDialog mDialog;
    private AlertDialog.Builder mDialogBuilder;
    private View mRootView;
    private ProgressWheel mProgressBar;
    private TextView mTxtMessage;

    private boolean mIsIndeterminate = true;
    private int mMax = -1;
    private int mCount = -1;

    public ProgressDialogService(Context context) {
        mRootView = LayoutInflater.from(context).inflate(
                progress_dialog_material, null, false);
        mDialogBuilder = new AlertDialog.Builder(context).setView(mRootView);
        mTxtMessage = (TextView) mRootView.findViewById(R.id.txt_progress_message);
        mProgressBar = (ProgressWheel) mRootView.findViewById(R.id.progress_wheel);
    }

    public ProgressDialogService setMessage(CharSequence message) {
        mTxtMessage.setText(message);
        return this;
    }

    public ProgressDialogService setMessage(@StringRes int messageId) {
        mTxtMessage.setText(messageId);
        return this;
    }

    public ProgressDialogService setTitle(CharSequence title) {
        mDialogBuilder.setTitle(title);
        return this;
    }

    public ProgressDialogService setTitle(@StringRes int titleId) {
        mDialogBuilder.setTitle(titleId);
        return this;
    }

    public ProgressDialogService setCancelable(boolean cancelable) {
        mDialogBuilder.setCancelable(cancelable);
        return this;
    }

    public void setIcon(@DrawableRes int icon) {
        mDialogBuilder.setIcon(icon);
    }


    public void setIndeterminate(boolean indeterminate) {
        if (mIsIndeterminate == indeterminate) return;
        mIsIndeterminate = indeterminate;
        if (indeterminate) {
            mProgressBar.spin();
            mMax = -1;
            mCount = -1;
        } else {
            mMax = 100;
            mCount = 0;
            mProgressBar.setInstantProgress(mCount);
        }
    }

    /**
     * Sets the total data which would be 100%
     *
     * @param totalData total data
     */
    public void setMax(int totalData) {
        mMax = totalData;
    }

    /**
     * Sets the current data count
     *
     * @param dataCount data count
     */
    public void setProgress(int dataCount) {
        mProgressBar.setProgress((float) dataCount / (float) mMax);
    }

    public ProgressDialogService setNegativeButton(@StringRes int buttonLabel, OnClickListener
            listener) {
        mDialogBuilder.setNegativeButton(buttonLabel, listener);
        return this;
    }

    public ProgressDialogService setCanceledOnTouchOutside(boolean cancel) {
        if (mDialog == null)
            mDialog = mDialogBuilder.create();
        mDialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public void show() {
        mDialog.show();
    }

    @Override
    public void cancel() {
        if (mDialog == null)
            mDialog = mDialogBuilder.create();
        mDialog.cancel();
    }

    @Override
    public void dismiss() {
        if (mDialog == null)
            mDialog = mDialogBuilder.create();
        mDialog.dismiss();
    }
}
