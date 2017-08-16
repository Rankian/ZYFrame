package com.sanjie.zy.widget.snack;

import android.app.Activity;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.sanjie.zy.R;

import java.lang.ref.WeakReference;

/**
 * ZYSnackBar helper class. Will attach a temporary layout to the current activity's content, on top of
 * all other views. It should appear under the status bar.
 *
 * Created by LangSanJie on 2017/2/20.
 */

public class ZYSnackBar {

    private static final String TAG = "ZYSnackBar";

    private static WeakReference<Activity> activityWeakReference;

    private Snack snack;

    /**
     * Constructor
     */
    public ZYSnackBar() {
    }

    public static ZYSnackBar create(@NonNull final Activity activity){
        if(activity == null){
            throw new IllegalArgumentException("Activity cannot be null");
        }
        final ZYSnackBar ZYSnackBar = new ZYSnackBar();

        //Clear Current ZYSnackBar, if one is Active
        ZYSnackBar.clearCurrent(activity);

        ZYSnackBar.setActivity(activity);
        ZYSnackBar.setSnack(new Snack(activity));

        return ZYSnackBar;
    }

    /**
     * Cleans up the currently showing snack view, is one is present
     * @param activity
     */
    private static void clearCurrent(@NonNull final Activity activity){
        if(activity == null){
            return;
        }
        try {
            final View snackView = activity.getWindow().getDecorView().findViewById(R.id.flSnackBackground);
            if(snackView == null || snackView.getWindowToken() == null){
                Log.d(TAG, "clearCurrent: " + activity.getString(R.string.msg_no_snack_showing));
            }else{
                snackView.animate().alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        ((ViewGroup)snackView.getParent()).removeView(snackView);
                    }
                }).start();
                Log.d(TAG, activity.getString(R.string.msg_snack_cleared));
            }
        }catch (Exception e){
            Log.e(TAG, "clearCurrent: "+ Log.getStackTraceString(e));
        }
    }

    /**
     * Shows the Alert, after it's built
     *
     * @return An Alert object check can be altered or hidden
     */
    public Snack show() {
        //This will get the Activity Window's DecorView
        if (getActivityWeakReference() != null) {
            getActivityWeakReference().get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Add the new Snack to the View Hierarchy
                    final ViewGroup decorView = getActivityDecorView();
                    if (decorView != null && getSnack().getParent() == null) {
                        decorView.addView(getSnack());
                    }
                }
            });
        }

        return getSnack();
    }

    /**
     * Sets the title of the Alert
     *
     * @param titleId Title String Resource
     * @return This Alerter
     */
    public ZYSnackBar setTitle(@StringRes final int titleId) {
        if (getSnack() != null) {
            getSnack().setTitle(titleId);
        }

        return this;
    }

    /**
     * Set Title of the Alert
     *
     * @param title Title as a String
     * @return This Alerter
     */
    public ZYSnackBar setTitle(final String title) {
        if (getSnack() != null) {
            getSnack().setTitle(title);
        }

        return this;
    }

    /**
     * Sets the Alert Text
     *
     * @param textId Text String Resource
     * @return This Alerter
     */
    public ZYSnackBar setText(@StringRes final int textId) {
        if (getSnack() != null) {
            getSnack().setText(textId);
        }

        return this;
    }

    /**
     * Sets the Alert Text
     *
     * @param text String of Alert Text
     * @return This Alerter
     */
    public ZYSnackBar setText(final String text) {
        if (getSnack() != null) {
            getSnack().setText(text);
        }

        return this;
    }

    /**
     * Set the Alert's Background Colour
     *
     * @param color Colour Resource Id
     * @return This Alerter
     */
    public ZYSnackBar setBackgroundColor(int color) {
        if (getSnack() != null && getActivityWeakReference() != null) {
            getSnack().setSnackBackgroundColor(ContextCompat.getColor(getActivityWeakReference().get(), color));
        }

        return this;
    }

    /**
     * Set the Alert's Icon
     *
     * @param iconId The Drawable's Resource Idw
     * @return This Alerter
     */
    public ZYSnackBar setIcon(@DrawableRes final int iconId) {
        if (getSnack() != null) {
            getSnack().setIcon(iconId);
        }

        return this;
    }

    /**
     * Set the onClickListener for the Alert
     *
     * @param onClickListener The onClickListener for the Alert
     * @return This Alerter
     */
    public ZYSnackBar setOnClickListener(@NonNull final View.OnClickListener onClickListener) {
        if (getSnack() != null) {
            getSnack().setOnClickListener(onClickListener);
        }

        return this;
    }

    /**
     * Set the on screen duration of the alert
     *
     * @param milliseconds The duration in milliseconds
     * @return This Alerter
     */
    public ZYSnackBar setDuration(final long milliseconds) {
        if (getSnack() != null) {
            getSnack().setDuration(milliseconds);
        }
        return this;
    }

    /**
     * Enable or Disable Icon Pulse Animations
     *
     * @param pulse True if the icon should pulse
     * @return This Alerter
     */
    public ZYSnackBar enableIconPulse(final boolean pulse) {
        if (getSnack() != null) {
            getSnack().pulseIcon(pulse);
        }
        return this;
    }

    /**
     * Gets the Alert associated with the Alerter
     *
     * @return The current Alert
     */
    private Snack getSnack() {
        return snack;
    }

    /**
     * Sets the Alert
     *
     * @param alert The Alert to be references and maintained
     */
    private void setSnack(final Snack alert) {
        this.snack = alert;
    }

    @Nullable
    private WeakReference<Activity> getActivityWeakReference() {
        return activityWeakReference;
    }

    /**
     * Get the enclosing Decor View
     *
     * @return The Decor View of the Activity the Alerter was called from
     */
    @Nullable
    private ViewGroup getActivityDecorView() {
        ViewGroup decorView = null;

        if (getActivityWeakReference() != null && getActivityWeakReference().get() != null) {
            decorView = (ViewGroup) getActivityWeakReference().get().getWindow().getDecorView();
        }

        return decorView;
    }

    /**
     * Creates a weak reference to the calling Activity
     *
     * @param activity The calling Activity
     */
    private void setActivity(@NonNull final Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }
}
