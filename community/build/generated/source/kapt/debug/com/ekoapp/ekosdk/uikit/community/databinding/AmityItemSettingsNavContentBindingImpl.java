package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityItemSettingsNavContentBindingImpl extends AmityItemSettingsNavContentBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new androidx.databinding.ViewDataBinding.IncludedLayouts(4);
        sIncludes.setIncludes(0, 
            new String[] {"amity_view_main_settings_content"},
            new int[] {1},
            new int[] {com.ekoapp.ekosdk.uikit.community.R.layout.amity_view_main_settings_content});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.tvValue, 2);
        sViewsWithIds.put(R.id.ivNavigation, 3);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityItemSettingsNavContentBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 4, sIncludes, sViewsWithIds));
    }
    private AmityItemSettingsNavContentBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 1
            , (android.widget.ImageView) bindings[3]
            , (com.ekoapp.ekosdk.uikit.community.databinding.AmityViewMainSettingsContentBinding) bindings[1]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[0]
            , (android.widget.TextView) bindings[2]
            );
        this.rootView.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        mainSettingsContent.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (mainSettingsContent.hasPendingBindings()) {
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
        mainSettingsContent.setLifecycleOwner(lifecycleOwner);
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeMainSettingsContent((com.ekoapp.ekosdk.uikit.community.databinding.AmityViewMainSettingsContentBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeMainSettingsContent(com.ekoapp.ekosdk.uikit.community.databinding.AmityViewMainSettingsContentBinding MainSettingsContent, int fieldId) {
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
        executeBindingsOn(mainSettingsContent);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): mainSettingsContent
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}