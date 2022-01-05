package com.ekoapp.ekosdk.uikit.chat.databinding;
import com.ekoapp.ekosdk.uikit.chat.R;
import com.ekoapp.ekosdk.uikit.chat.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityItemImageMsgReceiverBindingImpl extends AmityItemImageMsgReceiverBinding implements com.ekoapp.ekosdk.uikit.chat.generated.callback.OnLongClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new androidx.databinding.ViewDataBinding.IncludedLayouts(9);
        sIncludes.setIncludes(0, 
            new String[] {"amity_view_date", "amity_view_msg_deleted"},
            new int[] {7, 8},
            new int[] {com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_date,
                com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_msg_deleted});
        sViewsWithIds = null;
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    @Nullable
    private final com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgDeletedBinding mboundView01;
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView1;
    @NonNull
    private final android.widget.TextView mboundView5;
    // variables
    @Nullable
    private final android.view.View.OnLongClickListener mCallback19;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityItemImageMsgReceiverBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 9, sIncludes, sViewsWithIds));
    }
    private AmityItemImageMsgReceiverBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 10
            , (com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewDateBinding) bindings[7]
            , (com.google.android.material.imageview.ShapeableImageView) bindings[2]
            , (com.google.android.material.imageview.ShapeableImageView) bindings[4]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[6]
            );
        this.ivAvatar.setTag(null);
        this.ivImageIncoming.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView01 = (com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgDeletedBinding) bindings[8];
        setContainedBinding(this.mboundView01);
        this.mboundView1 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[1];
        this.mboundView1.setTag(null);
        this.mboundView5 = (android.widget.TextView) bindings[5];
        this.mboundView5.setTag(null);
        this.tvName.setTag(null);
        this.tvTime.setTag(null);
        setRootTag(root);
        // listeners
        mCallback19 = new com.ekoapp.ekosdk.uikit.chat.generated.callback.OnLongClickListener(this, 1);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x800L;
        }
        dateHeader.invalidateAll();
        mboundView01.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (dateHeader.hasPendingBindings()) {
            return true;
        }
        if (mboundView01.hasPendingBindings()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.vmImageMessage == variableId) {
            setVmImageMessage((com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoImageMsgViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setVmImageMessage(@Nullable com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoImageMsgViewModel VmImageMessage) {
        this.mVmImageMessage = VmImageMessage;
        synchronized(this) {
            mDirtyFlags |= 0x400L;
        }
        notifyPropertyChanged(BR.vmImageMessage);
        super.requestRebind();
    }

    @Override
    public void setLifecycleOwner(@Nullable androidx.lifecycle.LifecycleOwner lifecycleOwner) {
        super.setLifecycleOwner(lifecycleOwner);
        dateHeader.setLifecycleOwner(lifecycleOwner);
        mboundView01.setLifecycleOwner(lifecycleOwner);
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeVmImageMessageDateFillColor((androidx.databinding.ObservableField<java.lang.Integer>) object, fieldId);
            case 1 :
                return onChangeVmImageMessageIsDeleted((androidx.databinding.ObservableBoolean) object, fieldId);
            case 2 :
                return onChangeVmImageMessageIsSenderVisible((androidx.databinding.ObservableBoolean) object, fieldId);
            case 3 :
                return onChangeVmImageMessageMsgDate((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 4 :
                return onChangeVmImageMessageSender((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 5 :
                return onChangeDateHeader((com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewDateBinding) object, fieldId);
            case 6 :
                return onChangeVmImageMessageInSelectionMode((androidx.databinding.ObservableBoolean) object, fieldId);
            case 7 :
                return onChangeVmImageMessageImageUrl((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 8 :
                return onChangeVmImageMessageMsgTime((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 9 :
                return onChangeVmImageMessageIsDateVisible((androidx.databinding.ObservableBoolean) object, fieldId);
        }
        return false;
    }
    private boolean onChangeVmImageMessageDateFillColor(androidx.databinding.ObservableField<java.lang.Integer> VmImageMessageDateFillColor, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmImageMessageIsDeleted(androidx.databinding.ObservableBoolean VmImageMessageIsDeleted, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmImageMessageIsSenderVisible(androidx.databinding.ObservableBoolean VmImageMessageIsSenderVisible, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x4L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmImageMessageMsgDate(androidx.databinding.ObservableField<java.lang.String> VmImageMessageMsgDate, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x8L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmImageMessageSender(androidx.databinding.ObservableField<java.lang.String> VmImageMessageSender, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x10L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeDateHeader(com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewDateBinding DateHeader, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x20L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmImageMessageInSelectionMode(androidx.databinding.ObservableBoolean VmImageMessageInSelectionMode, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x40L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmImageMessageImageUrl(androidx.databinding.ObservableField<java.lang.String> VmImageMessageImageUrl, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x80L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmImageMessageMsgTime(androidx.databinding.ObservableField<java.lang.String> VmImageMessageMsgTime, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x100L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmImageMessageIsDateVisible(androidx.databinding.ObservableBoolean VmImageMessageIsDateVisible, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x200L;
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
        androidx.databinding.ObservableField<java.lang.Integer> vmImageMessageDateFillColor = null;
        androidx.databinding.ObservableBoolean vmImageMessageIsDeleted = null;
        int vmImageMessageIsDateVisibleViewVISIBLEViewGONE = 0;
        com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoImageMsgViewModel vmImageMessage = mVmImageMessage;
        androidx.databinding.ObservableBoolean vmImageMessageIsSenderVisible = null;
        int vmImageMessageIsDeletedViewGONEViewVISIBLE = 0;
        int vmImageMessageInSelectionModeViewVISIBLEViewGONE = 0;
        java.lang.String vmImageMessageMsgDateGet = null;
        java.lang.String vmImageMessageSenderGet = null;
        androidx.databinding.ObservableField<java.lang.String> vmImageMessageMsgDate = null;
        java.lang.Integer vmImageMessageDateFillColorGet = null;
        boolean vmImageMessageIsDateVisibleGet = false;
        androidx.databinding.ObservableField<java.lang.String> vmImageMessageSender = null;
        java.lang.String vmImageMessageMsgTimeGet = null;
        int vmImageMessageIsSenderVisibleViewVISIBLEViewGONE = 0;
        androidx.databinding.ObservableBoolean vmImageMessageInSelectionMode = null;
        int androidxDatabindingViewDataBindingSafeUnboxVmImageMessageDateFillColorGet = 0;
        boolean vmImageMessageInSelectionModeGet = false;
        androidx.databinding.ObservableField<java.lang.String> vmImageMessageImageUrl = null;
        androidx.databinding.ObservableField<java.lang.String> vmImageMessageMsgTime = null;
        java.lang.String vmImageMessageImageUrlGet = null;
        boolean vmImageMessageIsSenderVisibleGet = false;
        androidx.databinding.ObservableBoolean vmImageMessageIsDateVisible = null;
        boolean vmImageMessageIsDeletedGet = false;

        if ((dirtyFlags & 0xfdfL) != 0) {


            if ((dirtyFlags & 0xc01L) != 0) {

                    if (vmImageMessage != null) {
                        // read vmImageMessage.dateFillColor
                        vmImageMessageDateFillColor = vmImageMessage.getDateFillColor();
                    }
                    updateRegistration(0, vmImageMessageDateFillColor);


                    if (vmImageMessageDateFillColor != null) {
                        // read vmImageMessage.dateFillColor.get()
                        vmImageMessageDateFillColorGet = vmImageMessageDateFillColor.get();
                    }


                    // read androidx.databinding.ViewDataBinding.safeUnbox(vmImageMessage.dateFillColor.get())
                    androidxDatabindingViewDataBindingSafeUnboxVmImageMessageDateFillColorGet = androidx.databinding.ViewDataBinding.safeUnbox(vmImageMessageDateFillColorGet);
            }
            if ((dirtyFlags & 0xc02L) != 0) {

                    if (vmImageMessage != null) {
                        // read vmImageMessage.isDeleted()
                        vmImageMessageIsDeleted = vmImageMessage.isDeleted();
                    }
                    updateRegistration(1, vmImageMessageIsDeleted);


                    if (vmImageMessageIsDeleted != null) {
                        // read vmImageMessage.isDeleted().get()
                        vmImageMessageIsDeletedGet = vmImageMessageIsDeleted.get();
                    }
                if((dirtyFlags & 0xc02L) != 0) {
                    if(vmImageMessageIsDeletedGet) {
                            dirtyFlags |= 0x8000L;
                    }
                    else {
                            dirtyFlags |= 0x4000L;
                    }
                }


                    // read vmImageMessage.isDeleted().get() ? View.GONE : View.VISIBLE
                    vmImageMessageIsDeletedViewGONEViewVISIBLE = ((vmImageMessageIsDeletedGet) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
            }
            if ((dirtyFlags & 0xc04L) != 0) {

                    if (vmImageMessage != null) {
                        // read vmImageMessage.isSenderVisible()
                        vmImageMessageIsSenderVisible = vmImageMessage.isSenderVisible();
                    }
                    updateRegistration(2, vmImageMessageIsSenderVisible);


                    if (vmImageMessageIsSenderVisible != null) {
                        // read vmImageMessage.isSenderVisible().get()
                        vmImageMessageIsSenderVisibleGet = vmImageMessageIsSenderVisible.get();
                    }
                if((dirtyFlags & 0xc04L) != 0) {
                    if(vmImageMessageIsSenderVisibleGet) {
                            dirtyFlags |= 0x80000L;
                    }
                    else {
                            dirtyFlags |= 0x40000L;
                    }
                }


                    // read vmImageMessage.isSenderVisible().get() ? View.VISIBLE : View.GONE
                    vmImageMessageIsSenderVisibleViewVISIBLEViewGONE = ((vmImageMessageIsSenderVisibleGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
            if ((dirtyFlags & 0xc08L) != 0) {

                    if (vmImageMessage != null) {
                        // read vmImageMessage.msgDate
                        vmImageMessageMsgDate = vmImageMessage.getMsgDate();
                    }
                    updateRegistration(3, vmImageMessageMsgDate);


                    if (vmImageMessageMsgDate != null) {
                        // read vmImageMessage.msgDate.get()
                        vmImageMessageMsgDateGet = vmImageMessageMsgDate.get();
                    }
            }
            if ((dirtyFlags & 0xc10L) != 0) {

                    if (vmImageMessage != null) {
                        // read vmImageMessage.sender
                        vmImageMessageSender = vmImageMessage.getSender();
                    }
                    updateRegistration(4, vmImageMessageSender);


                    if (vmImageMessageSender != null) {
                        // read vmImageMessage.sender.get()
                        vmImageMessageSenderGet = vmImageMessageSender.get();
                    }
            }
            if ((dirtyFlags & 0xc40L) != 0) {

                    if (vmImageMessage != null) {
                        // read vmImageMessage.inSelectionMode
                        vmImageMessageInSelectionMode = vmImageMessage.getInSelectionMode();
                    }
                    updateRegistration(6, vmImageMessageInSelectionMode);


                    if (vmImageMessageInSelectionMode != null) {
                        // read vmImageMessage.inSelectionMode.get()
                        vmImageMessageInSelectionModeGet = vmImageMessageInSelectionMode.get();
                    }
                if((dirtyFlags & 0xc40L) != 0) {
                    if(vmImageMessageInSelectionModeGet) {
                            dirtyFlags |= 0x20000L;
                    }
                    else {
                            dirtyFlags |= 0x10000L;
                    }
                }


                    // read vmImageMessage.inSelectionMode.get() ? View.VISIBLE : View.GONE
                    vmImageMessageInSelectionModeViewVISIBLEViewGONE = ((vmImageMessageInSelectionModeGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
            if ((dirtyFlags & 0xc80L) != 0) {

                    if (vmImageMessage != null) {
                        // read vmImageMessage.imageUrl
                        vmImageMessageImageUrl = vmImageMessage.getImageUrl();
                    }
                    updateRegistration(7, vmImageMessageImageUrl);


                    if (vmImageMessageImageUrl != null) {
                        // read vmImageMessage.imageUrl.get()
                        vmImageMessageImageUrlGet = vmImageMessageImageUrl.get();
                    }
            }
            if ((dirtyFlags & 0xd00L) != 0) {

                    if (vmImageMessage != null) {
                        // read vmImageMessage.msgTime
                        vmImageMessageMsgTime = vmImageMessage.getMsgTime();
                    }
                    updateRegistration(8, vmImageMessageMsgTime);


                    if (vmImageMessageMsgTime != null) {
                        // read vmImageMessage.msgTime.get()
                        vmImageMessageMsgTimeGet = vmImageMessageMsgTime.get();
                    }
            }
            if ((dirtyFlags & 0xe00L) != 0) {

                    if (vmImageMessage != null) {
                        // read vmImageMessage.isDateVisible()
                        vmImageMessageIsDateVisible = vmImageMessage.isDateVisible();
                    }
                    updateRegistration(9, vmImageMessageIsDateVisible);


                    if (vmImageMessageIsDateVisible != null) {
                        // read vmImageMessage.isDateVisible().get()
                        vmImageMessageIsDateVisibleGet = vmImageMessageIsDateVisible.get();
                    }
                if((dirtyFlags & 0xe00L) != 0) {
                    if(vmImageMessageIsDateVisibleGet) {
                            dirtyFlags |= 0x2000L;
                    }
                    else {
                            dirtyFlags |= 0x1000L;
                    }
                }


                    // read vmImageMessage.isDateVisible().get() ? View.VISIBLE : View.GONE
                    vmImageMessageIsDateVisibleViewVISIBLEViewGONE = ((vmImageMessageIsDateVisibleGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
        }
        // batch finished
        if ((dirtyFlags & 0xe00L) != 0) {
            // api target 1

            this.dateHeader.getRoot().setVisibility(vmImageMessageIsDateVisibleViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0xc08L) != 0) {
            // api target 1

            this.dateHeader.setDate(vmImageMessageMsgDateGet);
        }
        if ((dirtyFlags & 0xc01L) != 0) {
            // api target 1

            this.dateHeader.setDateFillColor(androidxDatabindingViewDataBindingSafeUnboxVmImageMessageDateFillColorGet);
        }
        if ((dirtyFlags & 0xc04L) != 0) {
            // api target 1

            this.ivAvatar.setVisibility(vmImageMessageIsSenderVisibleViewVISIBLEViewGONE);
            this.tvName.setVisibility(vmImageMessageIsSenderVisibleViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x800L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.ivAvatar, getColorFromResource(ivAvatar, R.color.amityColorPrimary), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE3);
            this.ivImageIncoming.setOnLongClickListener(mCallback19);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setRoundedCorner(this.mboundView5, true, (java.lang.Float)null, (java.lang.Float)null, mboundView5.getResources().getDimension(R.dimen.amity_zero), (java.lang.Float)null, (java.lang.Integer)null, (java.lang.Integer)null, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvName, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvTime, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
        if ((dirtyFlags & 0xc80L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setImageUrl(this.ivImageIncoming, vmImageMessageImageUrlGet, androidx.appcompat.content.res.AppCompatResources.getDrawable(ivImageIncoming.getContext(), R.drawable.amity_ic_image_failed));
        }
        if ((dirtyFlags & 0xc00L) != 0) {
            // api target 1

            this.mboundView01.setViewModel(vmImageMessage);
        }
        if ((dirtyFlags & 0xc02L) != 0) {
            // api target 1

            this.mboundView1.setVisibility(vmImageMessageIsDeletedViewGONEViewVISIBLE);
        }
        if ((dirtyFlags & 0xc40L) != 0) {
            // api target 1

            this.mboundView5.setVisibility(vmImageMessageInSelectionModeViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0xc10L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvName, vmImageMessageSenderGet);
        }
        if ((dirtyFlags & 0xd00L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvTime, vmImageMessageMsgTimeGet);
        }
        executeBindingsOn(dateHeader);
        executeBindingsOn(mboundView01);
    }
    // Listener Stub Implementations
    // callback impls
    public final boolean _internalCallbackOnLongClick(int sourceId , android.view.View callbackArg_0) {
        // localize variables for thread safety
        // vmImageMessage != null
        boolean vmImageMessageJavaLangObjectNull = false;
        // vmImageMessage
        com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoImageMsgViewModel vmImageMessage = mVmImageMessage;
        // vmImageMessage.onLongPress()
        boolean vmImageMessageOnLongPress = false;



        vmImageMessageJavaLangObjectNull = (vmImageMessage) != (null);
        if (vmImageMessageJavaLangObjectNull) {


            vmImageMessageOnLongPress = vmImageMessage.onLongPress();
        }
        return vmImageMessageOnLongPress;
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): vmImageMessage.dateFillColor
        flag 1 (0x2L): vmImageMessage.isDeleted()
        flag 2 (0x3L): vmImageMessage.isSenderVisible()
        flag 3 (0x4L): vmImageMessage.msgDate
        flag 4 (0x5L): vmImageMessage.sender
        flag 5 (0x6L): dateHeader
        flag 6 (0x7L): vmImageMessage.inSelectionMode
        flag 7 (0x8L): vmImageMessage.imageUrl
        flag 8 (0x9L): vmImageMessage.msgTime
        flag 9 (0xaL): vmImageMessage.isDateVisible()
        flag 10 (0xbL): vmImageMessage
        flag 11 (0xcL): null
        flag 12 (0xdL): vmImageMessage.isDateVisible().get() ? View.VISIBLE : View.GONE
        flag 13 (0xeL): vmImageMessage.isDateVisible().get() ? View.VISIBLE : View.GONE
        flag 14 (0xfL): vmImageMessage.isDeleted().get() ? View.GONE : View.VISIBLE
        flag 15 (0x10L): vmImageMessage.isDeleted().get() ? View.GONE : View.VISIBLE
        flag 16 (0x11L): vmImageMessage.inSelectionMode.get() ? View.VISIBLE : View.GONE
        flag 17 (0x12L): vmImageMessage.inSelectionMode.get() ? View.VISIBLE : View.GONE
        flag 18 (0x13L): vmImageMessage.isSenderVisible().get() ? View.VISIBLE : View.GONE
        flag 19 (0x14L): vmImageMessage.isSenderVisible().get() ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}