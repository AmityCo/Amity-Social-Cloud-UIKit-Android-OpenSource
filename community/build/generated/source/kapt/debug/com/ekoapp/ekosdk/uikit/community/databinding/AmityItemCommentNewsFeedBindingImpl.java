package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityItemCommentNewsFeedBindingImpl extends AmityItemCommentNewsFeedBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.tvUserName, 9);
        sViewsWithIds.put(R.id.btnCommentAction, 10);
        sViewsWithIds.put(R.id.tvViewReplies, 11);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityItemCommentNewsFeedBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 12, sIncludes, sViewsWithIds));
    }
    private AmityItemCommentNewsFeedBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (androidx.constraintlayout.widget.Group) bindings[7]
            , (android.widget.ImageButton) bindings[10]
            , (com.google.android.material.checkbox.MaterialCheckBox) bindings[5]
            , (com.google.android.material.imageview.ShapeableImageView) bindings[1]
            , (android.widget.TextView) bindings[6]
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[3]
            , (com.ekoapp.ekosdk.uikit.common.views.text.EkoExpandableTextView) bindings[4]
            , (android.widget.TextView) bindings[9]
            , (android.widget.TextView) bindings[11]
            , (android.widget.LinearLayout) bindings[8]
            );
        this.actionGroup.setTag(null);
        this.cbLike.setTag(null);
        this.ivAvatar.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.reply.setTag(null);
        this.tvCommentTime.setTag(null);
        this.tvEdited.setTag(null);
        this.tvPostComment.setTag(null);
        this.viewRepliesContainer.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x40L;
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
        if (BR.edited == variableId) {
            setEdited((java.lang.Boolean) variable);
        }
        else if (BR.isReplyComment == variableId) {
            setIsReplyComment((java.lang.Boolean) variable);
        }
        else if (BR.addBottomSpace == variableId) {
            setAddBottomSpace((java.lang.Boolean) variable);
        }
        else if (BR.showViewRepliesButton == variableId) {
            setShowViewRepliesButton((java.lang.Boolean) variable);
        }
        else if (BR.readOnly == variableId) {
            setReadOnly((java.lang.Boolean) variable);
        }
        else if (BR.avatarUrl == variableId) {
            setAvatarUrl((java.lang.String) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setEdited(@Nullable java.lang.Boolean Edited) {
        this.mEdited = Edited;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.edited);
        super.requestRebind();
    }
    public void setIsReplyComment(@Nullable java.lang.Boolean IsReplyComment) {
        this.mIsReplyComment = IsReplyComment;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.isReplyComment);
        super.requestRebind();
    }
    public void setAddBottomSpace(@Nullable java.lang.Boolean AddBottomSpace) {
        this.mAddBottomSpace = AddBottomSpace;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.addBottomSpace);
        super.requestRebind();
    }
    public void setShowViewRepliesButton(@Nullable java.lang.Boolean ShowViewRepliesButton) {
        this.mShowViewRepliesButton = ShowViewRepliesButton;
        synchronized(this) {
            mDirtyFlags |= 0x8L;
        }
        notifyPropertyChanged(BR.showViewRepliesButton);
        super.requestRebind();
    }
    public void setReadOnly(@Nullable java.lang.Boolean ReadOnly) {
        this.mReadOnly = ReadOnly;
        synchronized(this) {
            mDirtyFlags |= 0x10L;
        }
        notifyPropertyChanged(BR.readOnly);
        super.requestRebind();
    }
    public void setAvatarUrl(@Nullable java.lang.String AvatarUrl) {
        this.mAvatarUrl = AvatarUrl;
        synchronized(this) {
            mDirtyFlags |= 0x20L;
        }
        notifyPropertyChanged(BR.avatarUrl);
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
        boolean androidxDatabindingViewDataBindingSafeUnboxShowViewRepliesButton = false;
        boolean androidxDatabindingViewDataBindingSafeUnboxAddBottomSpace = false;
        java.lang.Boolean edited = mEdited;
        boolean isReplyCommentBooleanTrueReadOnly = false;
        java.lang.Boolean isReplyComment = mIsReplyComment;
        java.lang.Boolean addBottomSpace = mAddBottomSpace;
        int isReplyCommentBooleanTrueReadOnlyViewGONEViewVISIBLE = 0;
        java.lang.Boolean showViewRepliesButton = mShowViewRepliesButton;
        int readOnlyViewGONEViewVISIBLE = 0;
        float addBottomSpaceTvPostCommentAndroidDimenAmityPaddingXsInt0 = 0f;
        boolean androidxDatabindingViewDataBindingSafeUnboxIsReplyComment = false;
        boolean androidxDatabindingViewDataBindingSafeUnboxEdited = false;
        boolean androidxDatabindingViewDataBindingSafeUnboxReadOnly = false;
        int showViewRepliesButtonViewVISIBLEViewGONE = 0;
        java.lang.Boolean readOnly = mReadOnly;
        java.lang.String avatarUrl = mAvatarUrl;
        int editedViewVISIBLEViewGONE = 0;
        boolean readOnlyBooleanFalseBooleanTrue = false;

        if ((dirtyFlags & 0x41L) != 0) {



                // read androidx.databinding.ViewDataBinding.safeUnbox(edited)
                androidxDatabindingViewDataBindingSafeUnboxEdited = androidx.databinding.ViewDataBinding.safeUnbox(edited);
            if((dirtyFlags & 0x41L) != 0) {
                if(androidxDatabindingViewDataBindingSafeUnboxEdited) {
                        dirtyFlags |= 0x40000L;
                }
                else {
                        dirtyFlags |= 0x20000L;
                }
            }


                // read androidx.databinding.ViewDataBinding.safeUnbox(edited) ? View.VISIBLE : View.GONE
                editedViewVISIBLEViewGONE = ((androidxDatabindingViewDataBindingSafeUnboxEdited) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        if ((dirtyFlags & 0x52L) != 0) {



                // read androidx.databinding.ViewDataBinding.safeUnbox(isReplyComment)
                androidxDatabindingViewDataBindingSafeUnboxIsReplyComment = androidx.databinding.ViewDataBinding.safeUnbox(isReplyComment);
            if((dirtyFlags & 0x52L) != 0) {
                if(androidxDatabindingViewDataBindingSafeUnboxIsReplyComment) {
                        dirtyFlags |= 0x100L;
                }
                else {
                        dirtyFlags |= 0x80L;
                }
            }
        }
        if ((dirtyFlags & 0x44L) != 0) {



                // read androidx.databinding.ViewDataBinding.safeUnbox(addBottomSpace)
                androidxDatabindingViewDataBindingSafeUnboxAddBottomSpace = androidx.databinding.ViewDataBinding.safeUnbox(addBottomSpace);
            if((dirtyFlags & 0x44L) != 0) {
                if(androidxDatabindingViewDataBindingSafeUnboxAddBottomSpace) {
                        dirtyFlags |= 0x4000L;
                }
                else {
                        dirtyFlags |= 0x2000L;
                }
            }


                // read androidx.databinding.ViewDataBinding.safeUnbox(addBottomSpace) ? @android:dimen/amity_padding_xs : 0
                addBottomSpaceTvPostCommentAndroidDimenAmityPaddingXsInt0 = ((androidxDatabindingViewDataBindingSafeUnboxAddBottomSpace) ? (tvPostComment.getResources().getDimension(R.dimen.amity_padding_xs)) : (0));
        }
        if ((dirtyFlags & 0x48L) != 0) {



                // read androidx.databinding.ViewDataBinding.safeUnbox(showViewRepliesButton)
                androidxDatabindingViewDataBindingSafeUnboxShowViewRepliesButton = androidx.databinding.ViewDataBinding.safeUnbox(showViewRepliesButton);
            if((dirtyFlags & 0x48L) != 0) {
                if(androidxDatabindingViewDataBindingSafeUnboxShowViewRepliesButton) {
                        dirtyFlags |= 0x10000L;
                }
                else {
                        dirtyFlags |= 0x8000L;
                }
            }


                // read androidx.databinding.ViewDataBinding.safeUnbox(showViewRepliesButton) ? View.VISIBLE : View.GONE
                showViewRepliesButtonViewVISIBLEViewGONE = ((androidxDatabindingViewDataBindingSafeUnboxShowViewRepliesButton) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        if ((dirtyFlags & 0x50L) != 0) {



                // read androidx.databinding.ViewDataBinding.safeUnbox(readOnly)
                androidxDatabindingViewDataBindingSafeUnboxReadOnly = androidx.databinding.ViewDataBinding.safeUnbox(readOnly);
            if((dirtyFlags & 0x50L) != 0) {
                if(androidxDatabindingViewDataBindingSafeUnboxReadOnly) {
                        dirtyFlags |= 0x1000L;
                        dirtyFlags |= 0x100000L;
                }
                else {
                        dirtyFlags |= 0x800L;
                        dirtyFlags |= 0x80000L;
                }
            }


                // read androidx.databinding.ViewDataBinding.safeUnbox(readOnly) ? View.GONE : View.VISIBLE
                readOnlyViewGONEViewVISIBLE = ((androidxDatabindingViewDataBindingSafeUnboxReadOnly) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
                // read androidx.databinding.ViewDataBinding.safeUnbox(readOnly) ? false : true
                readOnlyBooleanFalseBooleanTrue = ((androidxDatabindingViewDataBindingSafeUnboxReadOnly) ? (false) : (true));
        }
        if ((dirtyFlags & 0x60L) != 0) {
        }
        // batch finished

        if ((dirtyFlags & 0x80L) != 0) {



                // read androidx.databinding.ViewDataBinding.safeUnbox(readOnly)
                androidxDatabindingViewDataBindingSafeUnboxReadOnly = androidx.databinding.ViewDataBinding.safeUnbox(readOnly);
            if((dirtyFlags & 0x50L) != 0) {
                if(androidxDatabindingViewDataBindingSafeUnboxReadOnly) {
                        dirtyFlags |= 0x1000L;
                        dirtyFlags |= 0x100000L;
                }
                else {
                        dirtyFlags |= 0x800L;
                        dirtyFlags |= 0x80000L;
                }
            }
        }

        if ((dirtyFlags & 0x52L) != 0) {

                // read androidx.databinding.ViewDataBinding.safeUnbox(isReplyComment) ? true : androidx.databinding.ViewDataBinding.safeUnbox(readOnly)
                isReplyCommentBooleanTrueReadOnly = ((androidxDatabindingViewDataBindingSafeUnboxIsReplyComment) ? (true) : (androidxDatabindingViewDataBindingSafeUnboxReadOnly));
            if((dirtyFlags & 0x52L) != 0) {
                if(isReplyCommentBooleanTrueReadOnly) {
                        dirtyFlags |= 0x400L;
                }
                else {
                        dirtyFlags |= 0x200L;
                }
            }


                // read androidx.databinding.ViewDataBinding.safeUnbox(isReplyComment) ? true : androidx.databinding.ViewDataBinding.safeUnbox(readOnly) ? View.GONE : View.VISIBLE
                isReplyCommentBooleanTrueReadOnlyViewGONEViewVISIBLE = ((isReplyCommentBooleanTrueReadOnly) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
        }
        // batch finished
        if ((dirtyFlags & 0x50L) != 0) {
            // api target 1

            this.actionGroup.setVisibility(readOnlyViewGONEViewVISIBLE);
            this.cbLike.setEnabled(readOnlyBooleanFalseBooleanTrue);
        }
        if ((dirtyFlags & 0x40L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setCheckboxSelectorColor(this.cbLike, getColorFromResource(cbLike, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2, getColorFromResource(cbLike, R.color.amityColorPrimary), (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.ivAvatar, getColorFromResource(ivAvatar, R.color.amityColorPrimary), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE3);
            this.reply.setTextColor(getColorFromResource(reply, R.color.upstraColorBase));
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.reply, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvCommentTime, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvEdited, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
        if ((dirtyFlags & 0x60L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setImageUrl(this.ivAvatar, avatarUrl, androidx.appcompat.content.res.AppCompatResources.getDrawable(ivAvatar.getContext(), R.drawable.amity_ic_default_profile1));
        }
        if ((dirtyFlags & 0x52L) != 0) {
            // api target 1

            this.reply.setVisibility(isReplyCommentBooleanTrueReadOnlyViewGONEViewVISIBLE);
        }
        if ((dirtyFlags & 0x41L) != 0) {
            // api target 1

            this.tvEdited.setVisibility(editedViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x44L) != 0) {
            // api target 1

            androidx.databinding.adapters.ViewBindingAdapter.setPaddingBottom(this.tvPostComment, addBottomSpaceTvPostCommentAndroidDimenAmityPaddingXsInt0);
        }
        if ((dirtyFlags & 0x48L) != 0) {
            // api target 1

            this.viewRepliesContainer.setVisibility(showViewRepliesButtonViewVISIBLEViewGONE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): edited
        flag 1 (0x2L): isReplyComment
        flag 2 (0x3L): addBottomSpace
        flag 3 (0x4L): showViewRepliesButton
        flag 4 (0x5L): readOnly
        flag 5 (0x6L): avatarUrl
        flag 6 (0x7L): null
        flag 7 (0x8L): androidx.databinding.ViewDataBinding.safeUnbox(isReplyComment) ? true : androidx.databinding.ViewDataBinding.safeUnbox(readOnly)
        flag 8 (0x9L): androidx.databinding.ViewDataBinding.safeUnbox(isReplyComment) ? true : androidx.databinding.ViewDataBinding.safeUnbox(readOnly)
        flag 9 (0xaL): androidx.databinding.ViewDataBinding.safeUnbox(isReplyComment) ? true : androidx.databinding.ViewDataBinding.safeUnbox(readOnly) ? View.GONE : View.VISIBLE
        flag 10 (0xbL): androidx.databinding.ViewDataBinding.safeUnbox(isReplyComment) ? true : androidx.databinding.ViewDataBinding.safeUnbox(readOnly) ? View.GONE : View.VISIBLE
        flag 11 (0xcL): androidx.databinding.ViewDataBinding.safeUnbox(readOnly) ? View.GONE : View.VISIBLE
        flag 12 (0xdL): androidx.databinding.ViewDataBinding.safeUnbox(readOnly) ? View.GONE : View.VISIBLE
        flag 13 (0xeL): androidx.databinding.ViewDataBinding.safeUnbox(addBottomSpace) ? @android:dimen/amity_padding_xs : 0
        flag 14 (0xfL): androidx.databinding.ViewDataBinding.safeUnbox(addBottomSpace) ? @android:dimen/amity_padding_xs : 0
        flag 15 (0x10L): androidx.databinding.ViewDataBinding.safeUnbox(showViewRepliesButton) ? View.VISIBLE : View.GONE
        flag 16 (0x11L): androidx.databinding.ViewDataBinding.safeUnbox(showViewRepliesButton) ? View.VISIBLE : View.GONE
        flag 17 (0x12L): androidx.databinding.ViewDataBinding.safeUnbox(edited) ? View.VISIBLE : View.GONE
        flag 18 (0x13L): androidx.databinding.ViewDataBinding.safeUnbox(edited) ? View.VISIBLE : View.GONE
        flag 19 (0x14L): androidx.databinding.ViewDataBinding.safeUnbox(readOnly) ? false : true
        flag 20 (0x15L): androidx.databinding.ViewDataBinding.safeUnbox(readOnly) ? false : true
    flag mapping end*/
    //end
}