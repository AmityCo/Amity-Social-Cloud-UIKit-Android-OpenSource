package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityItemPostHeaderBindingImpl extends AmityItemPostHeaderBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.layoutPostedBy, 7);
        sViewsWithIds.put(R.id.userName, 8);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityItemPostHeaderBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 9, sIncludes, sViewsWithIds));
    }
    private AmityItemPostHeaderBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.google.android.material.imageview.ShapeableImageView) bindings[1]
            , (android.widget.ImageButton) bindings[6]
            , (com.ekoapp.ekosdk.uikit.common.views.text.EkoTextView) bindings[2]
            , (android.widget.TextView) bindings[4]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[0]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[7]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[3]
            , (com.ekoapp.ekosdk.uikit.common.views.text.EkoTextView) bindings[8]
            );
        this.avatarView.setTag(null);
        this.btnFeedAction.setTag(null);
        this.communityName.setTag(null);
        this.feedPostTime.setTag(null);
        this.layoutNewsFeedHeader.setTag(null);
        this.tvEdited.setTag(null);
        this.tvPostBy.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x20L;
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
        if (BR.showFeedAction == variableId) {
            setShowFeedAction((java.lang.Boolean) variable);
        }
        else if (BR.readOnly == variableId) {
            setReadOnly((java.lang.Boolean) variable);
        }
        else if (BR.avatarUrl == variableId) {
            setAvatarUrl((java.lang.String) variable);
        }
        else if (BR.isCommunity == variableId) {
            setIsCommunity((java.lang.Boolean) variable);
        }
        else if (BR.isModerator == variableId) {
            setIsModerator((java.lang.Boolean) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setShowFeedAction(@Nullable java.lang.Boolean ShowFeedAction) {
        this.mShowFeedAction = ShowFeedAction;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.showFeedAction);
        super.requestRebind();
    }
    public void setReadOnly(@Nullable java.lang.Boolean ReadOnly) {
        this.mReadOnly = ReadOnly;
    }
    public void setAvatarUrl(@Nullable java.lang.String AvatarUrl) {
        this.mAvatarUrl = AvatarUrl;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.avatarUrl);
        super.requestRebind();
    }
    public void setIsCommunity(@Nullable java.lang.Boolean IsCommunity) {
        this.mIsCommunity = IsCommunity;
        synchronized(this) {
            mDirtyFlags |= 0x8L;
        }
        notifyPropertyChanged(BR.isCommunity);
        super.requestRebind();
    }
    public void setIsModerator(@Nullable java.lang.Boolean IsModerator) {
        this.mIsModerator = IsModerator;
        synchronized(this) {
            mDirtyFlags |= 0x10L;
        }
        notifyPropertyChanged(BR.isModerator);
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
        java.lang.Boolean showFeedAction = mShowFeedAction;
        boolean androidxDatabindingViewDataBindingSafeUnboxIsCommunity = false;
        int showFeedActionViewVISIBLEViewGONE = 0;
        boolean androidxDatabindingViewDataBindingSafeUnboxIsModerator = false;
        java.lang.String avatarUrl = mAvatarUrl;
        java.lang.Boolean isCommunity = mIsCommunity;
        boolean androidxDatabindingViewDataBindingSafeUnboxShowFeedAction = false;
        int isCommunityViewVISIBLEViewGONE = 0;
        java.lang.Boolean isModerator = mIsModerator;
        int isModeratorViewVISIBLEViewGONE = 0;

        if ((dirtyFlags & 0x21L) != 0) {



                // read androidx.databinding.ViewDataBinding.safeUnbox(showFeedAction)
                androidxDatabindingViewDataBindingSafeUnboxShowFeedAction = androidx.databinding.ViewDataBinding.safeUnbox(showFeedAction);
            if((dirtyFlags & 0x21L) != 0) {
                if(androidxDatabindingViewDataBindingSafeUnboxShowFeedAction) {
                        dirtyFlags |= 0x80L;
                }
                else {
                        dirtyFlags |= 0x40L;
                }
            }


                // read androidx.databinding.ViewDataBinding.safeUnbox(showFeedAction) ? View.VISIBLE : View.GONE
                showFeedActionViewVISIBLEViewGONE = ((androidxDatabindingViewDataBindingSafeUnboxShowFeedAction) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        if ((dirtyFlags & 0x24L) != 0) {
        }
        if ((dirtyFlags & 0x28L) != 0) {



                // read androidx.databinding.ViewDataBinding.safeUnbox(isCommunity)
                androidxDatabindingViewDataBindingSafeUnboxIsCommunity = androidx.databinding.ViewDataBinding.safeUnbox(isCommunity);
            if((dirtyFlags & 0x28L) != 0) {
                if(androidxDatabindingViewDataBindingSafeUnboxIsCommunity) {
                        dirtyFlags |= 0x200L;
                }
                else {
                        dirtyFlags |= 0x100L;
                }
            }


                // read androidx.databinding.ViewDataBinding.safeUnbox(isCommunity) ? View.VISIBLE : View.GONE
                isCommunityViewVISIBLEViewGONE = ((androidxDatabindingViewDataBindingSafeUnboxIsCommunity) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        if ((dirtyFlags & 0x30L) != 0) {



                // read androidx.databinding.ViewDataBinding.safeUnbox(isModerator)
                androidxDatabindingViewDataBindingSafeUnboxIsModerator = androidx.databinding.ViewDataBinding.safeUnbox(isModerator);
            if((dirtyFlags & 0x30L) != 0) {
                if(androidxDatabindingViewDataBindingSafeUnboxIsModerator) {
                        dirtyFlags |= 0x800L;
                }
                else {
                        dirtyFlags |= 0x400L;
                }
            }


                // read androidx.databinding.ViewDataBinding.safeUnbox(isModerator) ? View.VISIBLE : View.GONE
                isModeratorViewVISIBLEViewGONE = ((androidxDatabindingViewDataBindingSafeUnboxIsModerator) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x24L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setImageUrl(this.avatarView, avatarUrl, androidx.appcompat.content.res.AppCompatResources.getDrawable(avatarView.getContext(), R.drawable.amity_ic_default_profile_large));
        }
        if ((dirtyFlags & 0x20L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.avatarView, getColorFromResource(avatarView, R.color.amityColorPrimary), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE3);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.feedPostTime, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvEdited, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setDrawableTint(this.tvPostBy, getColorFromResource(tvPostBy, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvPostBy, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
        if ((dirtyFlags & 0x21L) != 0) {
            // api target 1

            this.btnFeedAction.setVisibility(showFeedActionViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x28L) != 0) {
            // api target 1

            this.communityName.setVisibility(isCommunityViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x30L) != 0) {
            // api target 1

            this.tvPostBy.setVisibility(isModeratorViewVISIBLEViewGONE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): showFeedAction
        flag 1 (0x2L): readOnly
        flag 2 (0x3L): avatarUrl
        flag 3 (0x4L): isCommunity
        flag 4 (0x5L): isModerator
        flag 5 (0x6L): null
        flag 6 (0x7L): androidx.databinding.ViewDataBinding.safeUnbox(showFeedAction) ? View.VISIBLE : View.GONE
        flag 7 (0x8L): androidx.databinding.ViewDataBinding.safeUnbox(showFeedAction) ? View.VISIBLE : View.GONE
        flag 8 (0x9L): androidx.databinding.ViewDataBinding.safeUnbox(isCommunity) ? View.VISIBLE : View.GONE
        flag 9 (0xaL): androidx.databinding.ViewDataBinding.safeUnbox(isCommunity) ? View.VISIBLE : View.GONE
        flag 10 (0xbL): androidx.databinding.ViewDataBinding.safeUnbox(isModerator) ? View.VISIBLE : View.GONE
        flag 11 (0xcL): androidx.databinding.ViewDataBinding.safeUnbox(isModerator) ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}