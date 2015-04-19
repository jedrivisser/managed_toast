package com.j3dev.managedtoast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;
import java.util.Iterator;

public class ManagedToast {
    private static HashSet<Toast> mToasts = new HashSet<>();

    private Toast mToast;

    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {}

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public ManagedToast(Context context) {
        mToast = new Toast(context);
    }

    /**
     * Make a standard toast that just contains a text view.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link Toast#LENGTH_SHORT} or
     *                 {@link Toast#LENGTH_LONG}
     *
     */
    @SuppressLint("ShowToast")
    public static ManagedToast makeText(@NonNull Context context, @NonNull CharSequence text, @Duration int duration) {
        ManagedToast toast = new ManagedToast(context);
        toast.mToast = Toast.makeText(context, text, duration);
        return toast;
    }

    /**
     * Make a standard toast that just contains a text view with the text from a resource.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param resId    The resource id of the string resource to use.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link Toast#LENGTH_SHORT} or
     *                 {@link Toast#LENGTH_LONG}
     *
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    @SuppressLint("ShowToast")
    public static ManagedToast makeText(@NonNull  Context context, @StringRes int resId, @Duration int duration)
            throws Resources.NotFoundException {
        ManagedToast toast = new ManagedToast(context);
        toast.mToast = Toast.makeText(context, context.getResources().getText(resId), duration);
        return toast;
    }

    /**
     * Set the view to show.
     */
    public void setView(View view) {
        mToast.setView(view);
    }

    public void show() {
        mToasts.add(mToast);
        mToast.show();
    }

    /**
     * Cancels currently showing ManagedToast and remove all ManagedToasts from the queue
     */
    public static void cancelAll() {
        Iterator<Toast> i = mToasts.iterator();
        while (i.hasNext()) {
            Toast toast = i.next();
            if (toast != null) toast.cancel();
            i.remove();
        }
    }

    /**
     * Cancel the currently showing ManagedToast
     * @return true if a toast was canceled, false if there was nothing to cancel
     */
    public static boolean cancelCurrent() {
        Iterator<Toast> i = mToasts.iterator();
        while (i.hasNext()) {
            Toast toast = i.next();
            boolean shown = false;
            if (toast != null) {
                shown = toast.getView().isShown();
                toast.cancel();
            }
            i.remove();
            if (shown) return true;
        }
        return false;
    }
}
