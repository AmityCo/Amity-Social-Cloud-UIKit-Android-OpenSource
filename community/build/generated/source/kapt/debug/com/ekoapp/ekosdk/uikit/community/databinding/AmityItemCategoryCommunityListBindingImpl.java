package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityItemCategoryCommunityListBindingImpl extends AmityItemCategoryCommunityListBinding  {

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
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityItemCategoryCommunityListBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 3, sIncludes, sViewsWithIds));
    }
    private AmityItemCategoryCommunityListBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.google.android.material.imageview.ShapeableImageView) bindings[1]
            , (android.widget.TextView) bindings[2]
            );
        this.communityAvatar.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.tvCommunityName.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
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
        if (BR.ekoCommunity == variableId) {
            setEkoCommunity((com.ekoapp.ekosdk.community.EkoCommunity) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setEkoCommunity(@Nullable com.ekoapp.ekosdk.community.EkoCommunity EkoCommunity) {
        this.mEkoCommunity = EkoCommunity;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.ekoCommunity);
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
        android.graphics.drawable.Drawable ekoCommunityPublicJavaLangObjectNullTvCommunityNameAndroidDrawableAmityIcLock2 = null;
        com.ekoapp.ekosdk.community.EkoCommunity ekoCommunity = mEkoCommunity;
        android.graphics.drawable.Drawable ekoCommunityOfficialTvCommunityNameAndroidDrawableAmityIcVerifiedJavaLangObjectNull = null;
        boolean ekoCommunityPublic = false;
        boolean ekoCommunityOfficial = false;

        if ((dirtyFlags & 0x3L) != 0) {



                if (ekoCommunity != null) {
                    // read ekoCommunity.public
                    ekoCommunityPublic = ekoCommunity.isPublic();
                    // read ekoCommunity.official
                    ekoCommunityOfficial = ekoCommunity.isOfficial();
                }
            if((dirtyFlags & 0x3L) != 0) {
                if(ekoCommunityPublic) {
                        dirtyFlags |= 0x8L;
                }
                else {
                        dirtyFlags |= 0x4L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(ekoCommunityOfficial) {
                        dirtyFlags |= 0x20L;
                }
                else {
                        dirtyFlags |= 0x10L;
                }
            }


                // read ekoCommunity.public ? null : @android:drawable/amity_ic_lock2
                ekoCommunityPublicJavaLangObjectNullTvCommunityNameAndroidDrawableAmityIcLock2 = ((ekoCommunityPublic) ? (null) : (androidx.appcompat.content.res.AppCompatResources.getDrawable(tvCommunityName.getContext(), R.drawable.amity_ic_lock2)));
                // read ekoCommunity.official ? @android:drawable/amity_ic_verified : null
                ekoCommunityOfficialTvCommunityNameAndroidDrawableAmityIcVerifiedJavaLangObjectNull = ((ekoCommunityOfficial) ? (androidx.appcompat.content.res.AppCompatResources.getDrawable(tvCommunityName.getContext(), R.drawable.amity_ic_verified)) : (null));
        }
        // batch finished
        if ((dirtyFlags & 0x2L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setRoundedImageView(this.communityAvatar, getColorFromResource(communityAvatar, R.color.amityColorPrimary), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE3);
        }
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setDrawableStart(this.tvCommunityName, ekoCommunityPublicJavaLangObjectNullTvCommunityNameAndroidDrawableAmityIcLock2);
            androidx.databinding.adapters.TextViewBindingAdapter.setDrawableEnd(this.tvCommunityName, ekoCommunityOfficialTvCommunityNameAndroidDrawableAmityIcVerifiedJavaLangObjectNull);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): ekoCommunity
        flag 1 (0x2L): null
        flag 2 (0x3L): ekoCommunity.public ? null : @android:drawable/amity_ic_lock2
        flag 3 (0x4L): ekoCommunity.public ? null : @android:drawable/amity_ic_lock2
        flag 4 (0x5L): ekoCommunity.official ? @android:drawable/amity_ic_verified : null
        flag 5 (0x6L): ekoCommunity.official ? @android:drawable/amity_ic_verified : null
    flag mapping end*/
    //end
}