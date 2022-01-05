package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityViewAddedMemberWithCountBindingImpl extends AmityViewAddedMemberWithCountBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new androidx.databinding.ViewDataBinding.IncludedLayouts(5);
        sIncludes.setIncludes(0, 
            new String[] {"amity_item_added_member"},
            new int[] {2},
            new int[] {com.ekoapp.ekosdk.uikit.community.R.layout.amity_item_added_member});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.layout_icon, 3);
        sViewsWithIds.put(R.id.tvCount, 4);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityViewAddedMemberWithCountBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 5, sIncludes, sViewsWithIds));
    }
    private AmityViewAddedMemberWithCountBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 1
            , (android.widget.ImageView) bindings[1]
            , (com.ekoapp.ekosdk.uikit.community.databinding.AmityItemAddedMemberBinding) bindings[2]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[3]
            , (android.widget.TextView) bindings[4]
            );
        this.ivAdd.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        layoutAddedMember.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (layoutAddedMember.hasPendingBindings()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
            return variableSet;
    }

    @Override
    public void setLifecycleOwner(@Nullable androidx.lifecycle.LifecycleOwner lifecycleOwner) {
        super.setLifecycleOwner(lifecycleOwner);
        layoutAddedMember.setLifecycleOwner(lifecycleOwner);
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeLayoutAddedMember((com.ekoapp.ekosdk.uikit.community.databinding.AmityItemAddedMemberBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeLayoutAddedMember(com.ekoapp.ekosdk.uikit.community.databinding.AmityItemAddedMemberBinding LayoutAddedMember, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
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
        if ((dirtyFlags & 0x2L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setRoundedImageView(this.ivAdd, getColorFromResource(ivAdd, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
        }
        executeBindingsOn(layoutAddedMember);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): layoutAddedMember
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}