package com.ekoapp.ekosdk.uikit.chat.databinding;
import com.ekoapp.ekosdk.uikit.chat.R;
import com.ekoapp.ekosdk.uikit.chat.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityViewDateBindingImpl extends AmityViewDateBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    @NonNull
    private final android.widget.TextView mboundView1;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityViewDateBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 2, sIncludes, sViewsWithIds));
    }
    private AmityViewDateBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            );
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.widget.TextView) bindings[1];
        this.mboundView1.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.date == variableId) {
            setDate((java.lang.String) variable);
        }
        else if (BR.dateFillColor == variableId) {
            setDateFillColor((int) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setDate(@Nullable java.lang.String Date) {
        this.mDate = Date;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.date);
        super.requestRebind();
    }
    public void setDateFillColor(int DateFillColor) {
        this.mDateFillColor = DateFillColor;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.dateFillColor);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        java.lang.String date = mDate;
        int dateFillColor = mDateFillColor;

        if ((dirtyFlags & 0x5L) != 0) {
        }
        if ((dirtyFlags & 0x6L) != 0) {
        }
        // batch finished
        if ((dirtyFlags & 0x5L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView1, date);
        }
        if ((dirtyFlags & 0x6L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setRoundedCorner(this.mboundView1, true, (java.lang.Float)null, (java.lang.Float)null, (java.lang.Float)null, (java.lang.Float)null, dateFillColor, (java.lang.Integer)null, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): date
        flag 1 (0x2L): dateFillColor
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}