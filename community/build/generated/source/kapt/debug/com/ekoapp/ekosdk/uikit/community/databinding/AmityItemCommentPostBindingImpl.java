package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityItemCommentPostBindingImpl extends AmityItemCommentPostBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.ekoNewsFeedComment, 3);
        sViewsWithIds.put(R.id.rvReply, 4);
        sViewsWithIds.put(R.id.textview_view_more_replies, 5);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    @NonNull
    private final android.widget.ProgressBar mboundView2;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityItemCommentPostBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 6, sIncludes, sViewsWithIds));
    }
    private AmityItemCommentPostBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.ekoapp.ekosdk.uikit.community.views.newsfeed.EkoPostCommentView) bindings[3]
            , (androidx.recyclerview.widget.RecyclerView) bindings[4]
            , (android.widget.TextView) bindings[5]
            , (android.widget.LinearLayout) bindings[1]
            );
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView2 = (android.widget.ProgressBar) bindings[2];
        this.mboundView2.setTag(null);
        this.viewMoreRepliesContainer.setTag(null);
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
        if (BR.showProgressBar == variableId) {
            setShowProgressBar((java.lang.Boolean) variable);
        }
        else if (BR.showViewMoreRepliesButton == variableId) {
            setShowViewMoreRepliesButton((java.lang.Boolean) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setShowProgressBar(@Nullable java.lang.Boolean ShowProgressBar) {
        this.mShowProgressBar = ShowProgressBar;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.showProgressBar);
        super.requestRebind();
    }
    public void setShowViewMoreRepliesButton(@Nullable java.lang.Boolean ShowViewMoreRepliesButton) {
        this.mShowViewMoreRepliesButton = ShowViewMoreRepliesButton;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.showViewMoreRepliesButton);
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
        java.lang.Boolean showProgressBar = mShowProgressBar;
        boolean androidxDatabindingViewDataBindingSafeUnboxShowProgressBar = false;
        int showViewMoreRepliesButtonViewVISIBLEViewGONE = 0;
        java.lang.Boolean showViewMoreRepliesButton = mShowViewMoreRepliesButton;
        int showProgressBarViewVISIBLEViewGONE = 0;
        boolean androidxDatabindingViewDataBindingSafeUnboxShowViewMoreRepliesButton = false;

        if ((dirtyFlags & 0x5L) != 0) {



                // read androidx.databinding.ViewDataBinding.safeUnbox(showProgressBar)
                androidxDatabindingViewDataBindingSafeUnboxShowProgressBar = androidx.databinding.ViewDataBinding.safeUnbox(showProgressBar);
            if((dirtyFlags & 0x5L) != 0) {
                if(androidxDatabindingViewDataBindingSafeUnboxShowProgressBar) {
                        dirtyFlags |= 0x40L;
                }
                else {
                        dirtyFlags |= 0x20L;
                }
            }


                // read androidx.databinding.ViewDataBinding.safeUnbox(showProgressBar) ? View.VISIBLE : View.GONE
                showProgressBarViewVISIBLEViewGONE = ((androidxDatabindingViewDataBindingSafeUnboxShowProgressBar) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        if ((dirtyFlags & 0x6L) != 0) {



                // read androidx.databinding.ViewDataBinding.safeUnbox(showViewMoreRepliesButton)
                androidxDatabindingViewDataBindingSafeUnboxShowViewMoreRepliesButton = androidx.databinding.ViewDataBinding.safeUnbox(showViewMoreRepliesButton);
            if((dirtyFlags & 0x6L) != 0) {
                if(androidxDatabindingViewDataBindingSafeUnboxShowViewMoreRepliesButton) {
                        dirtyFlags |= 0x10L;
                }
                else {
                        dirtyFlags |= 0x8L;
                }
            }


                // read androidx.databinding.ViewDataBinding.safeUnbox(showViewMoreRepliesButton) ? View.VISIBLE : View.GONE
                showViewMoreRepliesButtonViewVISIBLEViewGONE = ((androidxDatabindingViewDataBindingSafeUnboxShowViewMoreRepliesButton) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x5L) != 0) {
            // api target 1

            this.mboundView2.setVisibility(showProgressBarViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x6L) != 0) {
            // api target 1

            this.viewMoreRepliesContainer.setVisibility(showViewMoreRepliesButtonViewVISIBLEViewGONE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): showProgressBar
        flag 1 (0x2L): showViewMoreRepliesButton
        flag 2 (0x3L): null
        flag 3 (0x4L): androidx.databinding.ViewDataBinding.safeUnbox(showViewMoreRepliesButton) ? View.VISIBLE : View.GONE
        flag 4 (0x5L): androidx.databinding.ViewDataBinding.safeUnbox(showViewMoreRepliesButton) ? View.VISIBLE : View.GONE
        flag 5 (0x6L): androidx.databinding.ViewDataBinding.safeUnbox(showProgressBar) ? View.VISIBLE : View.GONE
        flag 6 (0x7L): androidx.databinding.ViewDataBinding.safeUnbox(showProgressBar) ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}