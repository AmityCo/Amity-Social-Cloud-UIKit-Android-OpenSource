package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityItemCommunitySelectionListBindingImpl extends AmityItemCommunitySelectionListBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.communityNameContainer, 3);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityItemCommunitySelectionListBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 4, sIncludes, sViewsWithIds));
    }
    private AmityItemCommunitySelectionListBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.google.android.material.imageview.ShapeableImageView) bindings[1]
            , (android.widget.FrameLayout) bindings[3]
            , (android.widget.TextView) bindings[2]
            );
        this.avCommunityProfile.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.tvCommunityName.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
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
        if (BR.community == variableId) {
            setCommunity((com.ekoapp.ekosdk.community.EkoCommunity) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setCommunity(@Nullable com.ekoapp.ekosdk.community.EkoCommunity Community) {
        this.mCommunity = Community;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.community);
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
        java.lang.String communityGetDisplayName = null;
        com.ekoapp.ekosdk.community.EkoCommunity community = mCommunity;

        if ((dirtyFlags & 0x3L) != 0) {



                if (community != null) {
                    // read community.getDisplayName()
                    communityGetDisplayName = community.getDisplayName();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x2L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.avCommunityProfile, getColorFromResource(avCommunityProfile, R.color.amityColorPrimary), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE3);
        }
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvCommunityName, communityGetDisplayName);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): community
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}