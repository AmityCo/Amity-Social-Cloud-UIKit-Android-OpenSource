package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityFragmentPostCreateBindingImpl extends AmityFragmentPostCreateBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.clContent, 1);
        sViewsWithIds.put(R.id.etPost, 2);
        sViewsWithIds.put(R.id.rvAttachment, 3);
        sViewsWithIds.put(R.id.bottomBar, 4);
        sViewsWithIds.put(R.id.layoutPostAsCommunity, 5);
        sViewsWithIds.put(R.id.switchPostAsCommunity, 6);
        sViewsWithIds.put(R.id.composeBar, 7);
        sViewsWithIds.put(R.id.separator, 8);
        sViewsWithIds.put(R.id.btnTakePhoto, 9);
        sViewsWithIds.put(R.id.btnUploadPhotos, 10);
        sViewsWithIds.put(R.id.btnUploadAttachment, 11);
        sViewsWithIds.put(R.id.pbLoading, 12);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityFragmentPostCreateBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 13, sIncludes, sViewsWithIds));
    }
    private AmityFragmentPostCreateBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[4]
            , (com.google.android.material.button.MaterialButton) bindings[9]
            , (com.google.android.material.button.MaterialButton) bindings[11]
            , (com.google.android.material.button.MaterialButton) bindings[10]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[1]
            , (android.widget.RelativeLayout) bindings[7]
            , (com.ekoapp.ekosdk.uikit.community.views.createpost.EkoPostComposeView) bindings[2]
            , (android.widget.LinearLayout) bindings[5]
            , (android.widget.ProgressBar) bindings[12]
            , (androidx.recyclerview.widget.RecyclerView) bindings[3]
            , (android.view.View) bindings[8]
            , (com.google.android.material.switchmaterial.SwitchMaterial) bindings[6]
            );
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
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