package ffc.app.photo.asymmetric;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.felipecsl.asymmetricgridview.AsymmetricItem;

public class ItemPosition implements AsymmetricItem {

    private int columnSpan;
    private int rowSpan;

    public ItemPosition() {
        this(1, 1, 0);
    }

    public ItemPosition(int columnSpan, int rowSpan, int position) {
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
    }

    public ItemPosition(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int getColumnSpan() {
        return columnSpan;
    }

    @Override
    public int getRowSpan() {
        return rowSpan;
    }

    public void setColumnSpan(int columnSpan) {
        this.columnSpan = columnSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    @Override
    public String toString() {
        return String.format("%sx%s", rowSpan, columnSpan);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {
        columnSpan = in.readInt();
        rowSpan = in.readInt();

    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(columnSpan);
        dest.writeInt(rowSpan);
    }

    /* Parcelable interface implementation */
    public static final Parcelable.Creator<ItemPosition> CREATOR = new Parcelable.Creator<ItemPosition>() {

        @Override
        public ItemPosition createFromParcel(@NonNull Parcel in) {
            return new ItemPosition(in);
        }

        @Override
        @NonNull
        public ItemPosition[] newArray(int size) {
            return new ItemPosition[size];
        }
    };
}
