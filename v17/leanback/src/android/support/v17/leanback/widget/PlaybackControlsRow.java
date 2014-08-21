/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.widget;

import android.support.v17.leanback.R;
import android.util.TypedValue;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * A row of playback controls to be displayed by a {@link PlaybackControlsRowPresenter}.
 *
 * This row consists of some optional item detail, a series of primary actions,
 * and an optional series of secondary actions.
 *
 * Controls are specified via an {@link ObjectAdapter} containing one or more
 * {@link Action}s.
 *
 * Adapters should have their {@link PresenterSelector} set to an instance of
 * {@link ControlButtonPresenterSelector}.
 *
 */
public class PlaybackControlsRow extends Row {

    /**
     * Base class for an action comprised of a series of icons.
     */
    public static abstract class MultiAction extends Action {
        private int mIndex;
        private Drawable[] mDrawables;

        /**
         * Constructor
         * @param id The id of the Action.
         */
        public MultiAction(int id) {
            super(id);
        }

        /**
         * Sets the array of drawables.  The size of the array defines the range
         * of valid indices for this action.
         */
        public void setDrawables(Drawable[] drawables) {
            mDrawables = drawables;
            setIndex(0);
        }

        /**
         * Returns the number of drawables.
         */
        public int getNumberOfDrawables() {
            return mDrawables.length;
        }

        /**
         * Returns the drawable at the given index.
         */
        public Drawable getDrawable(int index) {
            return mDrawables[index];
        }

        /**
         * Increments the index, wrapping to zero once the end is reached.
         */
        public void nextIndex() {
            setIndex(mIndex < mDrawables.length - 1 ? mIndex + 1 : 0);
        }

        /**
         * Sets the current index.
         */
        public void setIndex(int index) {
            mIndex = index;
            setIcon(mDrawables[mIndex]);
        }

        /**
         * Gets the current index.
         */
        public int getIndex() {
            return mIndex;
        }
    }

    /**
     * An action displaying icons for play and pause.
     */
    public static class PlayPauseAction extends MultiAction {
        /**
         * Action index for the play icon.
         */
        public static int PLAY = 0;

        /**
         * Action index for the pause icon.
         */
        public static int PAUSE = 1;

        /**
         * Constructor
         * @param context Context used for loading resources.
         */
        public PlayPauseAction(Context context) {
            super(R.id.lb_control_play_pause);
            Drawable[] drawables = new Drawable[2];
            drawables[PLAY] = getStyledDrawable(context,
                    R.styleable.lbPlaybackControlsActionIcons_play);
            drawables[PAUSE] = getStyledDrawable(context,
                    R.styleable.lbPlaybackControlsActionIcons_pause);
            setDrawables(drawables);
        }

        @Override
        public String toString() {
            return "PlayPauseAction";
        }
    }

    /**
     * An action displaying an icon for fast forward.
     */
    public static class FastForwardAction extends Action {
        /**
         * Constructor
         * @param context Context used for loading resources.
         */
        public FastForwardAction(Context context) {
            super(R.id.lb_control_fast_forward);
            setIcon(getStyledDrawable(context,
                    R.styleable.lbPlaybackControlsActionIcons_fast_forward));
        }

        @Override
        public String toString() {
            return "FastForwardAction";
        }
    }

    /**
     * An action displaying an icon for rewind.
     */
    public static class RewindAction extends Action {
        /**
         * Constructor
         * @param context Context used for loading resources.
         */
        public RewindAction(Context context) {
            super(R.id.lb_control_fast_rewind);
            setIcon(getStyledDrawable(context,
                    R.styleable.lbPlaybackControlsActionIcons_rewind));
        }

        @Override
        public String toString() {
            return "RewindAction";
        }
    }

    /**
     * An action displaying an icon for skip next.
     */
    public static class SkipNextAction extends Action {
        /**
         * Constructor
         * @param context Context used for loading resources.
         */
        public SkipNextAction(Context context) {
            super(R.id.lb_control_skip_next);
            setIcon(getStyledDrawable(context,
                    R.styleable.lbPlaybackControlsActionIcons_skip_next));
        }

        @Override
        public String toString() {
            return "SkipNextAction";
        }
    }

    /**
     * An action displaying an icon for skip previous.
     */
    public static class SkipPreviousAction extends Action {
        /**
         * Constructor
         * @param context Context used for loading resources.
         */
        public SkipPreviousAction(Context context) {
            super(R.id.lb_control_skip_previous);
            setIcon(getStyledDrawable(context,
                    R.styleable.lbPlaybackControlsActionIcons_skip_previous));
        }

        @Override
        public String toString() {
            return "SkipPreviousAction";
        }
    }

    /**
     * An action displaying an icon for "more actions".
     */
    public static class MoreActions extends Action {
        /**
         * Constructor
         * @param context Context used for loading resources.
         */
        public MoreActions(Context context) {
            super(R.id.lb_control_more_actions);
            setIcon(context.getResources().getDrawable(R.drawable.lb_ic_more));
        }

        @Override
        public String toString() {
            return "MoreActions";
        }
    }

    /**
     * A base class for displaying a thumbs action.
     */
    public static abstract class ThumbsAction extends MultiAction {
        /**
         * Action index for the solid thumb icon.
         */
        public static int SOLID = 0;

        /**
         * Action index for the outline thumb icon.
         */
        public static int OUTLINE = 1;

        /**
         * Constructor
         * @param context Context used for loading resources.
         */
        public ThumbsAction(int id, Context context, int solidIconIndex, int outlineIconIndex) {
            super(id);
            Drawable[] drawables = new Drawable[2];
            drawables[SOLID] = getStyledDrawable(context, solidIconIndex);
            drawables[OUTLINE] = getStyledDrawable(context, outlineIconIndex);
            setDrawables(drawables);
        }
    }

    /**
     * An action displaying an icon for thumbs up.
     */
    public static class ThumbsUpAction extends ThumbsAction {
        public ThumbsUpAction(Context context) {
            super(R.id.lb_control_thumbs_up, context,
                    R.styleable.lbPlaybackControlsActionIcons_thumb_up,
                    R.styleable.lbPlaybackControlsActionIcons_thumb_up_outline);
        }

        @Override
        public String toString() {
            return "ThumbsUpAction";
        }
    }

    /**
     * An action displaying an icon for thumbs down.
     */
    public static class ThumbsDownAction extends ThumbsAction {
        public ThumbsDownAction(Context context) {
            super(R.id.lb_control_thumbs_down, context,
                    R.styleable.lbPlaybackControlsActionIcons_thumb_down,
                    R.styleable.lbPlaybackControlsActionIcons_thumb_down_outline);
        }

        @Override
        public String toString() {
            return "ThumbsDownAction";
        }
    }

    /**
     * An action for displaying three repeat states: none, one, or all.
     */
    public static class RepeatAction extends MultiAction {
        /**
         * Action index for the repeat-none icon.
         */
        public static int NONE = 0;

        /**
         * Action index for the repeat-all icon.
         */
        public static int ALL = 1;

        /**
         * Action index for the repeat-one icon.
         */
        public static int ONE = 2;

        /**
         * Constructor
         * @param context Context used for loading resources.
         */
        public RepeatAction(Context context) {
            this(context, getColorFromTheme(context,
                    R.attr.playbackControlsIconHighlightColor));
        }

        /**
         * Constructor
         * @param context Context used for loading resources
         * @param highlightColor Color to display the repeat-all and repeat0one icons.
         */
        public RepeatAction(Context context, int highlightColor) {
            this(context, highlightColor, highlightColor);
        }

        /**
         * Constructor
         * @param context Context used for loading resources
         * @param repeatAllColor Color to display the repeat-all icon.
         * @param repeatOneColor Color to display the repeat-one icon.
         */
        public RepeatAction(Context context, int repeatAllColor, int repeatOneColor) {
            super(R.id.lb_control_repeat);
            Drawable[] drawables = new Drawable[3];
            BitmapDrawable repeatDrawable = (BitmapDrawable) getStyledDrawable(context,
                    R.styleable.lbPlaybackControlsActionIcons_repeat);
            BitmapDrawable repeatOneDrawable = (BitmapDrawable) getStyledDrawable(context,
                    R.styleable.lbPlaybackControlsActionIcons_repeat_one);
            drawables[NONE] = repeatDrawable;
            drawables[ALL] = new BitmapDrawable(context.getResources(),
                    createBitmap(repeatDrawable.getBitmap(), repeatAllColor));
            drawables[ONE] = new BitmapDrawable(context.getResources(),
                    createBitmap(repeatOneDrawable.getBitmap(), repeatOneColor));
            setDrawables(drawables);
        }

        @Override
        public String toString() {
            return "RepeatAction";
        }
    }

    /**
     * An action for displaying a shuffle icon.
     */
    public static class ShuffleAction extends MultiAction {
        public static int OFF = 0;
        public static int ON = 1;

        /**
         * Constructor
         * @param context Context used for loading resources.
         */
        public ShuffleAction(Context context) {
            this(context, getColorFromTheme(context,
                    R.attr.playbackControlsIconHighlightColor));
        }

        /**
         * Constructor
         * @param context Context used for loading resources.
         * @param highlightColor Color for the highlighted icon state.
         */
        public ShuffleAction(Context context, int highlightColor) {
            super(R.id.lb_control_shuffle);
            BitmapDrawable uncoloredDrawable = (BitmapDrawable) getStyledDrawable(context,
                    R.styleable.lbPlaybackControlsActionIcons_shuffle);
            Drawable[] drawables = new Drawable[2];
            drawables[OFF] = uncoloredDrawable;
            drawables[ON] = new BitmapDrawable(context.getResources(),
                    createBitmap(uncoloredDrawable.getBitmap(), highlightColor));
            setDrawables(drawables);
        }

        @Override
        public String toString() {
            return "ShuffleAction";
        }
    }

    /**
     * An action for displaying a HQ (High Quality) icon.
     */
    public static class HighQualityAction extends MultiAction {
        public static int OFF = 0;
        public static int ON = 1;

        /**
         * Constructor
         * @param context Context used for loading resources.
         */
        public HighQualityAction(Context context) {
            this(context, getColorFromTheme(context,
                    R.attr.playbackControlsIconHighlightColor));
        }

        /**
         * Constructor
         * @param context Context used for loading resources.
         * @param highlightColor Color for the highlighted icon state.
         */
        public HighQualityAction(Context context, int highlightColor) {
            super(R.id.lb_control_high_quality);
            BitmapDrawable uncoloredDrawable = (BitmapDrawable) getStyledDrawable(context,
                    R.styleable.lbPlaybackControlsActionIcons_high_quality);
            Drawable[] drawables = new Drawable[2];
            drawables[OFF] = uncoloredDrawable;
            drawables[ON] = new BitmapDrawable(context.getResources(),
                    createBitmap(uncoloredDrawable.getBitmap(), highlightColor));
            setDrawables(drawables);
        }

        @Override
        public String toString() {
            return "HighQualityAction";
        }
    }

    /**
     * An action for displaying a CC (Closed Captioning) icon.
     */
    public static class ClosedCaptioningAction extends MultiAction {
        public static int OFF = 0;
        public static int ON = 1;

        /**
         * Constructor
         * @param context Context used for loading resources.
         */
        public ClosedCaptioningAction(Context context) {
            this(context, getColorFromTheme(context,
                    R.attr.playbackControlsIconHighlightColor));
        }

        /**
         * Constructor
         * @param context Context used for loading resources.
         * @param highlightColor Color for the highlighted icon state.
         */
        public ClosedCaptioningAction(Context context, int highlightColor) {
            super(R.id.lb_control_high_quality);
            BitmapDrawable uncoloredDrawable = (BitmapDrawable) getStyledDrawable(context,
                    R.styleable.lbPlaybackControlsActionIcons_closed_captioning);
            Drawable[] drawables = new Drawable[2];
            drawables[OFF] = uncoloredDrawable;
            drawables[ON] = new BitmapDrawable(context.getResources(),
                    createBitmap(uncoloredDrawable.getBitmap(), highlightColor));
            setDrawables(drawables);
        }

        @Override
        public String toString() {
            return "ClosedCaptioningAction";
        }
    }

    private static Bitmap createBitmap(Bitmap bitmap, int color) {
        Bitmap dst = bitmap.copy(bitmap.getConfig(), true);
        Canvas canvas = new Canvas(dst);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return dst;
    }

    private static int getColorFromTheme(Context context, int attributeResId) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(attributeResId, outValue, true);
        return outValue.data;
    }

    private static Drawable getStyledDrawable(Context context, int index) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(
                R.attr.playbackControlsActionIcons, outValue, false);
        TypedArray array = context.getTheme().obtainStyledAttributes(outValue.data,
                R.styleable.lbPlaybackControlsActionIcons);
        Drawable drawable = array.getDrawable(index);
        array.recycle();
        return drawable;
    }

    private Object mItem;
    private Drawable mImageDrawable;
    private ObjectAdapter mPrimaryActionsAdapter;
    private ObjectAdapter mSecondaryActionsAdapter;
    private int mTotalTimeMs;
    private int mCurrentTimeMs;
    private int mBufferedProgressMs;
    private OnPlaybackStateChangedListener mListener;

    /**
     * Constructor for a PlaybackControlsRow that displays some details from
     * the given item.
     *
     * @param item The main item for the row.
     */
    public PlaybackControlsRow(Object item) {
        mItem = item;
    }

    /**
     * Constructor for a PlaybackControlsRow that has no item details.
     */
    public PlaybackControlsRow() {
    }

    /**
     * Gets the main item for the details page.
     */
    public final Object getItem() {
        return mItem;
    }

    /**
     * Sets a {link @Drawable} image for this row.
     *
     * @param drawable The drawable to set.
     */
    public final void setImageDrawable(Drawable drawable) {
        mImageDrawable = drawable;
    }

    /**
     * Sets a {@link Bitmap} for this row.
     *
     * @param context The context to retrieve display metrics from.
     * @param bm The bitmap to set.
     */
    public final void setImageBitmap(Context context, Bitmap bm) {
        mImageDrawable = new BitmapDrawable(context.getResources(), bm);
    }

    /**
     * Gets the image {@link Drawable} of this row.
     *
     * @return The overview's image drawable, or null if no drawable has been
     *         assigned.
     */
    public final Drawable getImageDrawable() {
        return mImageDrawable;
    }

    /**
     * Sets the primary actions {@link ObjectAdapter}.
     */
    public final void setPrimaryActionsAdapter(ObjectAdapter adapter) {
        mPrimaryActionsAdapter = adapter;
    }

    /**
     * Sets the secondary actions {@link ObjectAdapter}.
     */
    public final void setSecondaryActionsAdapter(ObjectAdapter adapter) {
        mSecondaryActionsAdapter = adapter;
    }

    /**
     * Returns the primary actions {@link ObjectAdapter}.
     */
    public final ObjectAdapter getPrimaryActionsAdapter() {
        return mPrimaryActionsAdapter;
    }

    /**
     * Returns the secondary actions {@link ObjectAdapter}.
     */
    public final ObjectAdapter getSecondaryActionsAdapter() {
        return mSecondaryActionsAdapter;
    }

    /**
     * Sets the total time in milliseconds for the playback controls row.
     */
    public void setTotalTime(int ms) {
        mTotalTimeMs = ms;
    }

    /**
     * Returns the total time in milliseconds for the playback controls row.
     */
    public int getTotalTime() {
        return mTotalTimeMs;
    }

    /**
     * Sets the current time in milliseconds for the playback controls row.
     */
    public void setCurrentTime(int ms) {
        if (mCurrentTimeMs != ms) {
            mCurrentTimeMs = ms;
            currentTimeChanged();
        }
    }

    /**
     * Returns the current time in milliseconds for the playback controls row.
     */
    public int getCurrentTime() {
        return mCurrentTimeMs;
    }

    /**
     * Sets the buffered progress for the playback controls row.
     */
    public void setBufferedProgress(int ms) {
        if (mBufferedProgressMs != ms) {
            mBufferedProgressMs = ms;
            bufferedProgressChanged();
        }
    }

    /**
     * Returns the buffered progress for the playback controls row.
     */
    public int getBufferedProgress() {
        return mBufferedProgressMs;
    }

    interface OnPlaybackStateChangedListener {
        public void onCurrentTimeChanged(int currentTimeMs);
        public void onBufferedProgressChanged(int bufferedProgressMs);
    }

    /**
     * Sets a listener to be called when the playback state changes.
     */
    public void setOnPlaybackStateChangedListener(OnPlaybackStateChangedListener listener) {
        mListener = listener;
    }

    /**
     * Returns the playback state listener.
     */
    public OnPlaybackStateChangedListener getOnPlaybackStateChangedListener() {
        return mListener;
    }

    private void currentTimeChanged() {
        if (mListener != null) {
            mListener.onCurrentTimeChanged(mCurrentTimeMs);
        }
    }

    private void bufferedProgressChanged() {
        if (mListener != null) {
            mListener.onBufferedProgressChanged(mBufferedProgressMs);
        }
    }
}
