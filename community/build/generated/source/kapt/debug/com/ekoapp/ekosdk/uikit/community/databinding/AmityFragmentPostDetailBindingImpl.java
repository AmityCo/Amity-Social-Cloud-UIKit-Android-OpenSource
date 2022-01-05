package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityFragmentPostDetailBindingImpl extends AmityFragmentPostDetailBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.view_top, 4);
        sViewsWithIds.put(R.id.appBar, 5);
        sViewsWithIds.put(R.id.collapsingToolbar, 6);
        sViewsWithIds.put(R.id.newsFeedHeader, 7);
        sViewsWithIds.put(R.id.rvNewsFeed, 8);
        sViewsWithIds.put(R.id.postItemFooter, 9);
        sViewsWithIds.put(R.id.scroll_empty_view, 10);
        sViewsWithIds.put(R.id.view_bottom, 11);
        sViewsWithIds.put(R.id.imageview_close_reply, 12);
        sViewsWithIds.put(R.id.composeBarDivider, 13);
        sViewsWithIds.put(R.id.commentComposeBar, 14);
    }
    // views
    @NonNull
    private final android.widget.ProgressBar mboundView1;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityFragmentPostDetailBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 15, sIncludes, sViewsWithIds));
    }
    private AmityFragmentPostDetailBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.google.android.material.appbar.AppBarLayout) bindings[5]
            , (com.google.android.material.appbar.CollapsingToolbarLayout) bindings[6]
            , (com.ekoapp.ekosdk.uikit.community.views.EkoCommentComposeBar) bindings[14]
            , (android.view.View) bindings[13]
            , (android.widget.ImageView) bindings[12]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[0]
            , (com.ekoapp.ekosdk.uikit.community.views.newsfeed.EkoPostItemHeader) bindings[7]
            , (com.ekoapp.ekosdk.uikit.community.views.newsfeed.EkoPostItemFooter) bindings[9]
            , (androidx.recyclerview.widget.RecyclerView) bindings[8]
            , (androidx.core.widget.NestedScrollView) bindings[10]
            , (android.widget.TextView) bindings[3]
            , (android.widget.LinearLayout) bindings[11]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[2]
            , (androidx.coordinatorlayout.widget.CoordinatorLayout) bindings[4]
            );
        this.layoutParent.setTag(null);
        this.mboundView1 = (android.widget.ProgressBar) bindings[1];
        this.mboundView1.setTag(null);
        this.textviewReplyTo.setTag(null);
        this.viewReplyTo.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x8L;
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
        if (BR.showReplying == variableId) {
            setShowReplying((java.lang.Boolean) variable);
        }
        else if (BR.showLoadingComment == variableId) {
            setShowLoadingComment((java.lang.Boolean) variable);
        }
        else if (BR.replyingToUser == variableId) {
            setReplyingToUser((java.lang.String) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setShowReplying(@Nullable java.lang.Boolean ShowReplying) {
        this.mShowReplying = ShowReplying;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.showReplying);
        super.requestRebind();
    }
    public void setShowLoadingComment(@Nullable java.lang.Boolean ShowLoadingComment) {
        this.mShowLoadingComment = ShowLoadingComment;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.showLoadingComment);
        super.requestRebind();
    }
    public void setReplyingToUser(@Nullable java.lang.String ReplyingToUser) {
        this.mReplyingToUser = ReplyingToUser;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.replyingToUser);
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
        java.lang.Boolean showReplying = mShowReplying;
        java.lang.String stringFormatTextviewReplyToAndroidStringAmityReplyingToReplyingToUser = null;
        int showReplyingViewVISIBLEViewGONE = 0;
        boolean androidxDatabindingViewDataBindingSafeUnboxShowReplying = false;
        java.lang.Boolean showLoadingComment = mShowLoadingComment;
        java.lang.String replyingToUser = mReplyingToUser;
        boolean androidxDatabindingViewDataBindingSafeUnboxShowLoadingComment = false;
        int showLoadingCommentViewVISIBLEViewGONE = 0;

        if ((dirtyFlags & 0x9L) != 0) {



                // read androidx.databinding.ViewDataBinding.safeUnbox(showReplying)
                androidxDatabindingViewDataBindingSafeUnboxShowReplying = androidx.databinding.ViewDataBinding.safeUnbox(showReplying);
            if((dirtyFlags & 0x9L) != 0) {
                if(androidxDatabindingViewDataBindingSafeUnboxShowReplying) {
                        dirtyFlags |= 0x20L;
                }
                else {
                        dirtyFlags |= 0x10L;
                }
            }


                // read androidx.databinding.ViewDataBinding.safeUnbox(showReplying) ? View.VISIBLE : View.GONE
                showReplyingViewVISIBLEViewGONE = ((androidxDatabindingViewDataBindingSafeUnboxShowReplying) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        if ((dirtyFlags & 0xaL) != 0) {



                // read androidx.databinding.ViewDataBinding.safeUnbox(showLoadingComment)
                androidxDatabindingViewDataBindingSafeUnboxShowLoadingComment = androidx.databinding.ViewDataBinding.safeUnbox(showLoadingComment);
            if((dirtyFlags & 0xaL) != 0) {
                if(androidxDatabindingViewDataBindingSafeUnboxShowLoadingComment) {
                        dirtyFlags |= 0x80L;
                }
                else {
                        dirtyFlags |= 0x40L;
                }
            }


                // read androidx.databinding.ViewDataBinding.safeUnbox(showLoadingComment) ? View.VISIBLE : View.GONE
                showLoadingCommentViewVISIBLEViewGONE = ((androidxDatabindingViewDataBindingSafeUnboxShowLoadingComment) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        if ((dirtyFlags & 0xcL) != 0) {



                // read String.format(@android:string/amity_replying_to, replyingToUser)
                stringFormatTextviewReplyToAndroidStringAmityReplyingToReplyingToUser = java.lang.String.format(textviewReplyTo.getResources().getString(R.string.amity_replying_to), replyingToUser);
        }
        // batch finished
        if ((dirtyFlags & 0xaL) != 0) {
            // api target 1

            this.mboundView1.setVisibility(showLoadingCommentViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0xcL) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textviewReplyTo, stringFormatTextviewReplyToAndroidStringAmityReplyingToReplyingToUser);
        }
        if ((dirtyFlags & 0x9L) != 0) {
            // api target 1

            this.viewReplyTo.setVisibility(showReplyingViewVISIBLEViewGONE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): showReplying
        flag 1 (0x2L): showLoadingComment
        flag 2 (0x3L): replyingToUser
        flag 3 (0x4L): null
        flag 4 (0x5L): androidx.databinding.ViewDataBinding.safeUnbox(showReplying) ? View.VISIBLE : View.GONE
        flag 5 (0x6L): androidx.databinding.ViewDataBinding.safeUnbox(showReplying) ? View.VISIBLE : View.GONE
        flag 6 (0x7L): androidx.databinding.ViewDataBinding.safeUnbox(showLoadingComment) ? View.VISIBLE : View.GONE
        flag 7 (0x8L): androidx.databinding.ViewDataBinding.safeUnbox(showLoadingComment) ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}