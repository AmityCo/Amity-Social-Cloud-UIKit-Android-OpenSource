package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityFragmentUserProfilePageBindingImpl extends AmityFragmentUserProfilePageBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.appBar, 1);
        sViewsWithIds.put(R.id.collapsingToolbar, 2);
        sViewsWithIds.put(R.id.userProfileHeader, 3);
        sViewsWithIds.put(R.id.tabLayout, 4);
        sViewsWithIds.put(R.id.fabCreatePost, 5);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityFragmentUserProfilePageBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 6, sIncludes, sViewsWithIds));
    }
    private AmityFragmentUserProfilePageBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.google.android.material.appbar.AppBarLayout) bindings[1]
            , (com.google.android.material.appbar.CollapsingToolbarLayout) bindings[2]
            , (com.google.android.material.floatingactionbutton.FloatingActionButton) bindings[5]
            , (androidx.swiperefreshlayout.widget.SwipeRefreshLayout) bindings[0]
            , (com.ekoapp.ekosdk.uikit.components.EkoTabLayout) bindings[4]
            , (com.ekoapp.ekosdk.uikit.community.views.profile.EkoUserProfileHeaderView) bindings[3]
            );
        this.refreshLayout.setTag(null);
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