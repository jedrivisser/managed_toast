/*
 * Copyright (C) 2015 j3dev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class ManagedToast {
    private static Set<Toast> sToasts = new LinkedHashSet<>();

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
        sToasts.add(mToast);
        mToast.show();
    }

    /**
     * Cancels currently showing ManagedToast and remove all ManagedToasts from the queue
     */
    public static void cancelAll() {
        Iterator<Toast> i = sToasts.iterator();
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
        Iterator<Toast> i = sToasts.iterator();
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
