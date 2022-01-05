package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityItemCommunityMembershipBindingImpl extends AmityItemCommunityMembershipBinding implements com.ekoapp.ekosdk.uikit.community.generated.callback.OnClickListener.Listener {

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
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback10;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityItemCommunityMembershipBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 4, sIncludes, sViewsWithIds));
    }
    private AmityItemCommunityMembershipBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.google.android.material.imageview.ShapeableImageView) bindings[1]
            , (android.widget.ImageView) bindings[3]
            , (android.widget.TextView) bindings[2]
            );
        this.ivAvatar.setTag(null);
        this.ivMore.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.tvMemberName.setTag(null);
        setRootTag(root);
        // listeners
        mCallback10 = new com.ekoapp.ekosdk.uikit.community.generated.callback.OnClickListener(this, 1);
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
        if (BR.isMyUser == variableId) {
            setIsMyUser((java.lang.Boolean) variable);
        }
        else if (BR.listener == variableId) {
            setListener((com.ekoapp.ekosdk.uikit.community.members.IMemberClickListener) variable);
        }
        else if (BR.communityMemberShip == variableId) {
            setCommunityMemberShip((com.ekoapp.ekosdk.community.membership.EkoCommunityMembership) variable);
        }
        else if (BR.avatarUrl == variableId) {
            setAvatarUrl((java.lang.String) variable);
        }
        else if (BR.isJoined == variableId) {
            setIsJoined((java.lang.Boolean) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setIsMyUser(@Nullable java.lang.Boolean IsMyUser) {
        this.mIsMyUser = IsMyUser;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.isMyUser);
        super.requestRebind();
    }
    public void setListener(@Nullable com.ekoapp.ekosdk.uikit.community.members.IMemberClickListener Listener) {
        this.mListener = Listener;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.listener);
        super.requestRebind();
    }
    public void setCommunityMemberShip(@Nullable com.ekoapp.ekosdk.community.membership.EkoCommunityMembership CommunityMemberShip) {
        this.mCommunityMemberShip = CommunityMemberShip;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.communityMemberShip);
        super.requestRebind();
    }
    public void setAvatarUrl(@Nullable java.lang.String AvatarUrl) {
        this.mAvatarUrl = AvatarUrl;
        synchronized(this) {
            mDirtyFlags |= 0x8L;
        }
        notifyPropertyChanged(BR.avatarUrl);
        super.requestRebind();
    }
    public void setIsJoined(@Nullable java.lang.Boolean IsJoined) {
        this.mIsJoined = IsJoined;
        synchronized(this) {
            mDirtyFlags |= 0x10L;
        }
        notifyPropertyChanged(BR.isJoined);
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
        boolean isJoinedBooleanTrueIsMyUser = false;
        boolean androidxDatabindingViewDataBindingSafeUnboxIsMyUser = false;
        java.lang.Boolean isMyUser = mIsMyUser;
        com.ekoapp.ekosdk.uikit.community.members.IMemberClickListener listener = mListener;
        com.ekoapp.ekosdk.community.membership.EkoCommunityMembership communityMemberShip = mCommunityMemberShip;
        java.lang.String communityMemberShipUserDisplayName = null;
        boolean androidxDatabindingViewDataBindingSafeUnboxIsJoined = false;
        java.lang.String avatarUrl = mAvatarUrl;
        int isJoinedBooleanTrueIsMyUserViewGONEViewVISIBLE = 0;
        java.lang.Boolean isJoined = mIsJoined;
        com.ekoapp.ekosdk.user.EkoUser communityMemberShipUser = null;
        boolean IsJoined1 = false;

        if ((dirtyFlags & 0x24L) != 0) {



                if (communityMemberShip != null) {
                    // read communityMemberShip.user
                    communityMemberShipUser = communityMemberShip.getUser();
                }


                if (communityMemberShipUser != null) {
                    // read communityMemberShip.user.displayName
                    communityMemberShipUserDisplayName = communityMemberShipUser.getDisplayName();
                }
        }
        if ((dirtyFlags & 0x28L) != 0) {
        }
        if ((dirtyFlags & 0x31L) != 0) {



                // read androidx.databinding.ViewDataBinding.safeUnbox(isJoined)
                androidxDatabindingViewDataBindingSafeUnboxIsJoined = androidx.databinding.ViewDataBinding.safeUnbox(isJoined);


                // read !androidx.databinding.ViewDataBinding.safeUnbox(isJoined)
                IsJoined1 = !androidxDatabindingViewDataBindingSafeUnboxIsJoined;
            if((dirtyFlags & 0x31L) != 0) {
                if(IsJoined1) {
                        dirtyFlags |= 0x80L;
                }
                else {
                        dirtyFlags |= 0x40L;
                }
            }
        }
        // batch finished

        if ((dirtyFlags & 0x40L) != 0) {



                // read androidx.databinding.ViewDataBinding.safeUnbox(isMyUser)
                androidxDatabindingViewDataBindingSafeUnboxIsMyUser = androidx.databinding.ViewDataBinding.safeUnbox(isMyUser);
        }

        if ((dirtyFlags & 0x31L) != 0) {

                // read !androidx.databinding.ViewDataBinding.safeUnbox(isJoined) ? true : androidx.databinding.ViewDataBinding.safeUnbox(isMyUser)
                isJoinedBooleanTrueIsMyUser = ((IsJoined1) ? (true) : (androidxDatabindingViewDataBindingSafeUnboxIsMyUser));
            if((dirtyFlags & 0x31L) != 0) {
                if(isJoinedBooleanTrueIsMyUser) {
                        dirtyFlags |= 0x200L;
                }
                else {
                        dirtyFlags |= 0x100L;
                }
            }


                // read !androidx.databinding.ViewDataBinding.safeUnbox(isJoined) ? true : androidx.databinding.ViewDataBinding.safeUnbox(isMyUser) ? View.GONE : View.VISIBLE
                isJoinedBooleanTrueIsMyUserViewGONEViewVISIBLE = ((isJoinedBooleanTrueIsMyUser) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
        }
        // batch finished
        if ((dirtyFlags & 0x28L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setImageUrl(this.ivAvatar, avatarUrl, androidx.appcompat.content.res.AppCompatResources.getDrawable(ivAvatar.getContext(), R.drawable.amity_ic_default_profile_large));
        }
        if ((dirtyFlags & 0x20L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.ivAvatar, getColorFromResource(ivAvatar, R.color.amityColorPrimary), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE3);
            this.mboundView0.setOnClickListener(mCallback10);
        }
        if ((dirtyFlags & 0x31L) != 0) {
            // api target 1

            this.ivMore.setVisibility(isJoinedBooleanTrueIsMyUserViewGONEViewVISIBLE);
        }
        if ((dirtyFlags & 0x24L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setText(this.tvMemberName, communityMemberShipUserDisplayName);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        // localize variables for thread safety
        // listener != null
        boolean listenerJavaLangObjectNull = false;
        // listener
        com.ekoapp.ekosdk.uikit.community.members.IMemberClickListener listener = mListener;
        // communityMemberShip
        com.ekoapp.ekosdk.community.membership.EkoCommunityMembership communityMemberShip = mCommunityMemberShip;



        listenerJavaLangObjectNull = (listener) != (null);
        if (listenerJavaLangObjectNull) {



            listener.onCommunityMembershipSelected(communityMemberShip);
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): isMyUser
        flag 1 (0x2L): listener
        flag 2 (0x3L): communityMemberShip
        flag 3 (0x4L): avatarUrl
        flag 4 (0x5L): isJoined
        flag 5 (0x6L): null
        flag 6 (0x7L): !androidx.databinding.ViewDataBinding.safeUnbox(isJoined) ? true : androidx.databinding.ViewDataBinding.safeUnbox(isMyUser)
        flag 7 (0x8L): !androidx.databinding.ViewDataBinding.safeUnbox(isJoined) ? true : androidx.databinding.ViewDataBinding.safeUnbox(isMyUser)
        flag 8 (0x9L): !androidx.databinding.ViewDataBinding.safeUnbox(isJoined) ? true : androidx.databinding.ViewDataBinding.safeUnbox(isMyUser) ? View.GONE : View.VISIBLE
        flag 9 (0xaL): !androidx.databinding.ViewDataBinding.safeUnbox(isJoined) ? true : androidx.databinding.ViewDataBinding.safeUnbox(isMyUser) ? View.GONE : View.VISIBLE
    flag mapping end*/
    //end
}