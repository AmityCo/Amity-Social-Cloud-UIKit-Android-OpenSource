package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityItemPostFooterBindingImpl extends AmityItemPostFooterBinding  {

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
    private final android.widget.TextView mboundView9;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityItemPostFooterBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View[] root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 14, sIncludes, sViewsWithIds));
    }
    private AmityItemPostFooterBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View[] root, Object[] bindings) {
        super(bindingComponent, root[0], 0
            , (android.widget.TextView) bindings[6]
            , (com.google.android.material.checkbox.MaterialCheckBox) bindings[5]
            , (android.widget.TextView) bindings[7]
            , (android.widget.LinearLayout) bindings[4]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[0]
            , (androidx.recyclerview.widget.RecyclerView) bindings[11]
            , (android.view.View) bindings[3]
            , (android.view.View) bindings[10]
            , (android.view.View) bindings[13]
            , (android.widget.LinearLayout) bindings[8]
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[1]
            , (android.widget.LinearLayout) bindings[12]
            );
        this.cbComment.setTag(null);
        this.cbLike.setTag(null);
        this.cbShare.setTag(null);
        this.mboundView9 = (android.widget.TextView) bindings[9];
        this.mboundView9.setTag(null);
        this.postActionLayout.setTag(null);
        this.reactionStatusLayout.setTag(null);
        this.rvCommentFooter.setTag(null);
        this.separator.setTag(null);
        this.separator2.setTag(null);
        this.separatorViewAllComments.setTag(null);
        this.tvJoinCommunityMessage.setTag(null);
        this.tvNumberOfComments.setTag(null);
        this.tvNumberOfLikes.setTag(null);
        this.viewAllCommentContainer.setTag(null);
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
        if (BR.showViewAllComment == variableId) {
            setShowViewAllComment((java.lang.Boolean) variable);
        }
        else if (BR.readOnly == variableId) {
            setReadOnly((java.lang.Boolean) variable);
        }
        else if (BR.showShareButton == variableId) {
            setShowShareButton((java.lang.Boolean) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setShowViewAllComment(@Nullable java.lang.Boolean ShowViewAllComment) {
        this.mShowViewAllComment = ShowViewAllComment;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.showViewAllComment);
        super.requestRebind();
    }
    public void setReadOnly(@Nullable java.lang.Boolean ReadOnly) {
        this.mReadOnly = ReadOnly;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.readOnly);
        super.requestRebind();
    }
    public void setShowShareButton(@Nullable java.lang.Boolean ShowShareButton) {
        this.mShowShareButton = ShowShareButton;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.showShareButton);
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
        boolean androidxDatabindingViewDataBindingSafeUnboxShowShareButton = false;
        java.lang.Boolean showViewAllComment = mShowViewAllComment;
        int readOnlyViewVISIBLEViewGONE = 0;
        int showShareButtonViewVISIBLEViewGONE = 0;
        boolean androidxDatabindingViewDataBindingSafeUnboxReadOnly = false;
        java.lang.Boolean readOnly = mReadOnly;
        java.lang.Boolean showShareButton = mShowShareButton;
        int showViewAllCommentViewVISIBLEViewGONE = 0;
        boolean androidxDatabindingViewDataBindingSafeUnboxShowViewAllComment = false;
        int readOnlyViewGONEViewVISIBLE = 0;

        if ((dirtyFlags & 0x9L) != 0) {



                // read androidx.databinding.ViewDataBinding.safeUnbox(showViewAllComment)
                androidxDatabindingViewDataBindingSafeUnboxShowViewAllComment = androidx.databinding.ViewDataBinding.safeUnbox(showViewAllComment);
            if((dirtyFlags & 0x9L) != 0) {
                if(androidxDatabindingViewDataBindingSafeUnboxShowViewAllComment) {
                        dirtyFlags |= 0x200L;
                }
                else {
                        dirtyFlags |= 0x100L;
                }
            }


                // read androidx.databinding.ViewDataBinding.safeUnbox(showViewAllComment) ? View.VISIBLE : View.GONE
                showViewAllCommentViewVISIBLEViewGONE = ((androidxDatabindingViewDataBindingSafeUnboxShowViewAllComment) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        if ((dirtyFlags & 0xaL) != 0) {



                // read androidx.databinding.ViewDataBinding.safeUnbox(readOnly)
                androidxDatabindingViewDataBindingSafeUnboxReadOnly = androidx.databinding.ViewDataBinding.safeUnbox(readOnly);
            if((dirtyFlags & 0xaL) != 0) {
                if(androidxDatabindingViewDataBindingSafeUnboxReadOnly) {
                        dirtyFlags |= 0x20L;
                        dirtyFlags |= 0x800L;
                }
                else {
                        dirtyFlags |= 0x10L;
                        dirtyFlags |= 0x400L;
                }
            }


                // read androidx.databinding.ViewDataBinding.safeUnbox(readOnly) ? View.VISIBLE : View.GONE
                readOnlyViewVISIBLEViewGONE = ((androidxDatabindingViewDataBindingSafeUnboxReadOnly) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                // read androidx.databinding.ViewDataBinding.safeUnbox(readOnly) ? View.GONE : View.VISIBLE
                readOnlyViewGONEViewVISIBLE = ((androidxDatabindingViewDataBindingSafeUnboxReadOnly) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
        }
        if ((dirtyFlags & 0xcL) != 0) {



                // read androidx.databinding.ViewDataBinding.safeUnbox(showShareButton)
                androidxDatabindingViewDataBindingSafeUnboxShowShareButton = androidx.databinding.ViewDataBinding.safeUnbox(showShareButton);
            if((dirtyFlags & 0xcL) != 0) {
                if(androidxDatabindingViewDataBindingSafeUnboxShowShareButton) {
                        dirtyFlags |= 0x80L;
                }
                else {
                        dirtyFlags |= 0x40L;
                }
            }


                // read androidx.databinding.ViewDataBinding.safeUnbox(showShareButton) ? View.VISIBLE : View.GONE
                showShareButtonViewVISIBLEViewGONE = ((androidxDatabindingViewDataBindingSafeUnboxShowShareButton) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x8L) != 0) {
            // api target 1

            this.cbComment.setTextColor(getColorFromResource(cbComment, R.color.amityColorBase));
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.cbComment, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setCheckboxSelectorColor(this.cbLike, getColorFromResource(cbLike, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2, getColorFromResource(cbLike, R.color.amityColorPrimary), (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            this.cbShare.setTextColor(getColorFromResource(cbShare, R.color.amityColorBase));
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.cbShare, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.mboundView9, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvNumberOfComments, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvNumberOfLikes, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
        if ((dirtyFlags & 0xcL) != 0) {
            // api target 1

            this.cbShare.setVisibility(showShareButtonViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0xaL) != 0) {
            // api target 1

            this.postActionLayout.setVisibility(readOnlyViewGONEViewVISIBLE);
            this.tvJoinCommunityMessage.setVisibility(readOnlyViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x9L) != 0) {
            // api target 1

            this.separatorViewAllComments.setVisibility(showViewAllCommentViewVISIBLEViewGONE);
            this.viewAllCommentContainer.setVisibility(showViewAllCommentViewVISIBLEViewGONE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): showViewAllComment
        flag 1 (0x2L): readOnly
        flag 2 (0x3L): showShareButton
        flag 3 (0x4L): null
        flag 4 (0x5L): androidx.databinding.ViewDataBinding.safeUnbox(readOnly) ? View.VISIBLE : View.GONE
        flag 5 (0x6L): androidx.databinding.ViewDataBinding.safeUnbox(readOnly) ? View.VISIBLE : View.GONE
        flag 6 (0x7L): androidx.databinding.ViewDataBinding.safeUnbox(showShareButton) ? View.VISIBLE : View.GONE
        flag 7 (0x8L): androidx.databinding.ViewDataBinding.safeUnbox(showShareButton) ? View.VISIBLE : View.GONE
        flag 8 (0x9L): androidx.databinding.ViewDataBinding.safeUnbox(showViewAllComment) ? View.VISIBLE : View.GONE
        flag 9 (0xaL): androidx.databinding.ViewDataBinding.safeUnbox(showViewAllComment) ? View.VISIBLE : View.GONE
        flag 10 (0xbL): androidx.databinding.ViewDataBinding.safeUnbox(readOnly) ? View.GONE : View.VISIBLE
        flag 11 (0xcL): androidx.databinding.ViewDataBinding.safeUnbox(readOnly) ? View.GONE : View.VISIBLE
    flag mapping end*/
    //end
}