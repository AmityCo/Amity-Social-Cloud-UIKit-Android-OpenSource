package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityItemCreatePostFileBindingImpl extends AmityItemCreatePostFileBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.progress, 3);
        sViewsWithIds.put(R.id.ivFileIcon, 4);
        sViewsWithIds.put(R.id.tvFileName, 5);
        sViewsWithIds.put(R.id.overlay, 6);
        sViewsWithIds.put(R.id.layoutPreparingFile, 7);
        sViewsWithIds.put(R.id.ivRemove, 8);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityItemCreatePostFileBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 9, sIncludes, sViewsWithIds));
    }
    private AmityItemCreatePostFileBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[0]
            , (com.google.android.material.imageview.ShapeableImageView) bindings[2]
            , (android.widget.ImageView) bindings[4]
            , (android.widget.ImageView) bindings[8]
            , (android.widget.LinearLayout) bindings[7]
            , (android.view.View) bindings[6]
            , (android.widget.ProgressBar) bindings[3]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[1]
            );
        this.containerView.setTag(null);
        this.ivError.setTag(null);
        this.tvFileSize.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x1L;
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
            return variableSet;
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
        // batch finished
        if ((dirtyFlags & 0x1L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setBackgroundAlpha(this.ivError, ivError.getResources().getInteger(R.integer.alpha_fifty));
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvFileSize, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}