package com.sanjie.zy.widget.indicatior;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by SanJie on 2017/5/16.
 */

public class PositionSavedState extends View.BaseSavedState {

    private int selectedIndex, selectingIndex, lastSelectedIndex;

    public PositionSavedState(Parcelable superState) {
        super(superState);
    }

    public PositionSavedState(Parcel source) {
        super(source);
        this.selectedIndex = source.readInt();
        this.selectingIndex = source.readInt();
        this.lastSelectedIndex = source.readInt();
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public int getSelectingIndex() {
        return selectingIndex;
    }

    public void setSelectingIndex(int selectingIndex) {
        this.selectingIndex = selectingIndex;
    }

    public int getLastSelectedIndex() {
        return lastSelectedIndex;
    }

    public void setLastSelectedIndex(int lastSelectedIndex) {
        this.lastSelectedIndex = lastSelectedIndex;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(this.selectedIndex);
        out.writeInt(this.selectingIndex);
        out.writeInt(this.lastSelectedIndex);
    }

    public static final Parcelable.Creator<PositionSavedState> CREATOR = new Parcelable.Creator<PositionSavedState>() {
        @Override
        public PositionSavedState createFromParcel(Parcel source) {
            return new PositionSavedState(source);
        }

        @Override
        public PositionSavedState[] newArray(int size) {
            return new PositionSavedState[size];
        }
    };
}
