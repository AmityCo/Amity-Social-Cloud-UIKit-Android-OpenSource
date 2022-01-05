package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityItemCommunityCategoryBindingImpl extends AmityItemCommunityCategoryBinding implements com.ekoapp.ekosdk.uikit.community.generated.callback.OnClickListener.Listener {

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
    @NonNull
    private final android.widget.TextView mboundView2;
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback12;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityItemCommunityCategoryBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 3, sIncludes, sViewsWithIds));
    }
    private AmityItemCommunityCategoryBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.google.android.material.imageview.ShapeableImageView) bindings[1]
            );
        this.ivAvatar.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView2 = (android.widget.TextView) bindings[2];
        this.mboundView2.setTag(null);
        setRootTag(root);
        // listeners
        mCallback12 = new com.ekoapp.ekosdk.uikit.community.generated.callback.OnClickListener(this, 1);
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
        if (BR.avatarUrl == variableId) {
            setAvatarUrl((java.lang.String) variable);
        }
        else if (BR.communityCategory == variableId) {
            setCommunityCategory((com.ekoapp.ekosdk.community.category.EkoCommunityCategory) variable);
        }
        else if (BR.listener == variableId) {
            setListener((com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setAvatarUrl(@Nullable java.lang.String AvatarUrl) {
        this.mAvatarUrl = AvatarUrl;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.avatarUrl);
        super.requestRebind();
    }
    public void setCommunityCategory(@Nullable com.ekoapp.ekosdk.community.category.EkoCommunityCategory CommunityCategory) {
        this.mCommunityCategory = CommunityCategory;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.communityCategory);
        super.requestRebind();
    }
    public void setListener(@Nullable com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener Listener) {
        this.mListener = Listener;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.listener);
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
        java.lang.String avatarUrl = mAvatarUrl;
        com.ekoapp.ekosdk.community.category.EkoCommunityCategory communityCategory = mCommunityCategory;
        com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener listener = mListener;
        java.lang.String communityCategoryName = null;

        if ((dirtyFlags & 0x9L) != 0) {
        }
        if ((dirtyFlags & 0xaL) != 0) {



                if (communityCategory != null) {
                    // read communityCategory.name
                    communityCategoryName = communityCategory.getName();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x9L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setImageUrl(this.ivAvatar, avatarUrl, androidx.appcompat.content.res.AppCompatResources.getDrawable(ivAvatar.getContext(), R.drawable.amity_ic_default_category_avatar));
        }
        if ((dirtyFlags & 0x8L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.ivAvatar, getColorFromResource(ivAvatar, R.color.amityColorPrimary), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE3);
            this.mboundView0.setOnClickListener(mCallback12);
        }
        if ((dirtyFlags & 0xaL) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView2, communityCategoryName);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        // localize variables for thread safety
        // listener != null
        boolean listenerJavaLangObjectNull = false;
        // listener
        com.ekoapp.ekosdk.uikit.community.explore.listener.IEkoCategoryItemClickListener listener = mListener;
        // communityCategory
        com.ekoapp.ekosdk.community.category.EkoCommunityCategory communityCategory = mCommunityCategory;



        listenerJavaLangObjectNull = (listener) != (null);
        if (listenerJavaLangObjectNull) {



            listener.onCategorySelected(communityCategory);
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): avatarUrl
        flag 1 (0x2L): communityCategory
        flag 2 (0x3L): listener
        flag 3 (0x4L): null
    flag mapping end*/
    //end
}