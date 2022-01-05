package com.ekoapp.ekosdk.uikit.chat.databinding;
import com.ekoapp.ekosdk.uikit.chat.R;
import com.ekoapp.ekosdk.uikit.chat.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityItemImageMsgSenderBindingImpl extends AmityItemImageMsgSenderBinding implements com.ekoapp.ekosdk.uikit.chat.generated.callback.OnLongClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new androidx.databinding.ViewDataBinding.IncludedLayouts(9);
        sIncludes.setIncludes(0, 
            new String[] {"amity_view_date", "amity_view_msg_deleted"},
            new int[] {6, 8},
            new int[] {com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_date,
                com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_msg_deleted});
        sIncludes.setIncludes(1, 
            new String[] {"amity_view_msg_error"},
            new int[] {7},
            new int[] {com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_msg_error});
        sViewsWithIds = null;
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    @Nullable
    private final com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgDeletedBinding mboundView01;
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView1;
    @Nullable
    private final com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgErrorBinding mboundView11;
    @NonNull
    private final android.widget.TextView mboundView4;
    // variables
    @Nullable
    private final android.view.View.OnLongClickListener mCallback18;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityItemImageMsgSenderBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 9, sIncludes, sViewsWithIds));
    }
    private AmityItemImageMsgSenderBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 11
            , (com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewDateBinding) bindings[6]
            , (com.google.android.material.imageview.ShapeableImageView) bindings[3]
            , (com.google.android.material.progressindicator.CircularProgressIndicator) bindings[5]
            , (android.widget.TextView) bindings[2]
            );
        this.ivMsgOutgoing.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView01 = (com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgDeletedBinding) bindings[8];
        setContainedBinding(this.mboundView01);
        this.mboundView1 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[1];
        this.mboundView1.setTag(null);
        this.mboundView11 = (com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgErrorBinding) bindings[7];
        setContainedBinding(this.mboundView11);
        this.mboundView4 = (android.widget.TextView) bindings[4];
        this.mboundView4.setTag(null);
        this.progressBar.setTag(null);
        this.tvTimeOutgoing.setTag(null);
        setRootTag(root);
        // listeners
        mCallback18 = new com.ekoapp.ekosdk.uikit.chat.generated.callback.OnLongClickListener(this, 1);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x1000L;
        }
        dateHeader.invalidateAll();
        mboundView11.invalidateAll();
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
        if (mboundView11.hasPendingBindings()) {
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
            mDirtyFlags |= 0x800L;
        }
        notifyPropertyChanged(BR.vmImageMessage);
        super.requestRebind();
    }

    @Override
    public void setLifecycleOwner(@Nullable androidx.lifecycle.LifecycleOwner lifecycleOwner) {
        super.setLifecycleOwner(lifecycleOwner);
        dateHeader.setLifecycleOwner(lifecycleOwner);
        mboundView11.setLifecycleOwner(lifecycleOwner);
        mboundView01.setLifecycleOwner(lifecycleOwner);
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeVmImageMessageIsDeleted((androidx.databinding.ObservableBoolean) object, fieldId);
            case 1 :
                return onChangeVmImageMessageMsgDate((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 2 :
                return onChangeVmImageMessageInSelectionMode((androidx.databinding.ObservableBoolean) object, fieldId);
            case 3 :
                return onChangeVmImageMessageUploadProgress((androidx.databinding.ObservableField<java.lang.Integer>) object, fieldId);
            case 4 :
                return onChangeVmImageMessageMsgTime((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 5 :
                return onChangeVmImageMessageImageUrl((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 6 :
                return onChangeVmImageMessageUploading((androidx.databinding.ObservableBoolean) object, fieldId);
            case 7 :
                return onChangeVmImageMessageDateFillColor((androidx.databinding.ObservableField<java.lang.Integer>) object, fieldId);
            case 8 :
                return onChangeDateHeader((com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewDateBinding) object, fieldId);
            case 9 :
                return onChangeVmImageMessageIsDateVisible((androidx.databinding.ObservableBoolean) object, fieldId);
            case 10 :
                return onChangeVmImageMessageIsFailed((androidx.databinding.ObservableBoolean) object, fieldId);
        }
        return false;
    }
    private boolean onChangeVmImageMessageIsDeleted(androidx.databinding.ObservableBoolean VmImageMessageIsDeleted, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmImageMessageMsgDate(androidx.databinding.ObservableField<java.lang.String> VmImageMessageMsgDate, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmImageMessageInSelectionMode(androidx.databinding.ObservableBoolean VmImageMessageInSelectionMode, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x4L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmImageMessageUploadProgress(androidx.databinding.ObservableField<java.lang.Integer> VmImageMessageUploadProgress, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x8L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmImageMessageMsgTime(androidx.databinding.ObservableField<java.lang.String> VmImageMessageMsgTime, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x10L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmImageMessageImageUrl(androidx.databinding.ObservableField<java.lang.String> VmImageMessageImageUrl, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x20L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmImageMessageUploading(androidx.databinding.ObservableBoolean VmImageMessageUploading, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x40L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmImageMessageDateFillColor(androidx.databinding.ObservableField<java.lang.Integer> VmImageMessageDateFillColor, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x80L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeDateHeader(com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewDateBinding DateHeader, int fieldId) {
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
    private boolean onChangeVmImageMessageIsFailed(androidx.databinding.ObservableBoolean VmImageMessageIsFailed, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x400L;
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
        androidx.databinding.ObservableBoolean vmImageMessageIsDeleted = null;
        boolean vmImageMessageIsFailed = false;
        java.lang.Integer vmImageMessageUploadProgressGet = null;
        int vmImageMessageIsDeletedViewGONEViewVISIBLE = 0;
        androidx.databinding.ObservableField<java.lang.String> vmImageMessageMsgDate = null;
        int androidxDatabindingViewDataBindingSafeUnboxVmImageMessageUploadProgressGet = 0;
        boolean vmImageMessageIsDateVisibleGet = false;
        androidx.databinding.ObservableBoolean vmImageMessageInSelectionMode = null;
        androidx.databinding.ObservableField<java.lang.Integer> vmImageMessageUploadProgress = null;
        boolean vmImageMessageInSelectionModeGet = false;
        androidx.databinding.ObservableField<java.lang.String> vmImageMessageMsgTime = null;
        androidx.databinding.ObservableField<java.lang.String> vmImageMessageImageUrl = null;
        java.lang.String vmImageMessageImageUrlGet = null;
        boolean vmImageMessageIsFailedGet = false;
        androidx.databinding.ObservableBoolean vmImageMessageUploading = null;
        androidx.databinding.ObservableField<java.lang.Integer> vmImageMessageDateFillColor = null;
        int vmImageMessageInSelectionModeBooleanTrueVmImageMessageUploadingViewVISIBLEViewGONE = 0;
        int vmImageMessageIsDateVisibleViewVISIBLEViewGONE = 0;
        int vmImageMessageUploadingVmImageMessageIsFailedViewVISIBLEViewGONE = 0;
        com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoImageMsgViewModel vmImageMessage = mVmImageMessage;
        java.lang.String vmImageMessageMsgDateGet = null;
        boolean vmImageMessageUploadingGet = false;
        java.lang.Integer vmImageMessageDateFillColorGet = null;
        boolean vmImageMessageUploadingVmImageMessageIsFailed = false;
        int vmImageMessageIsFailedViewVISIBLEViewGONE = 0;
        java.lang.String vmImageMessageMsgTimeGet = null;
        boolean vmImageMessageInSelectionModeBooleanTrueVmImageMessageUploading = false;
        int androidxDatabindingViewDataBindingSafeUnboxVmImageMessageDateFillColorGet = 0;
        androidx.databinding.ObservableBoolean vmImageMessageIsDateVisible = null;
        androidx.databinding.ObservableBoolean VmImageMessageIsFailed1 = null;
        boolean vmImageMessageIsDeletedGet = false;

        if ((dirtyFlags & 0x1effL) != 0) {


            if ((dirtyFlags & 0x1801L) != 0) {

                    if (vmImageMessage != null) {
                        // read vmImageMessage.isDeleted()
                        vmImageMessageIsDeleted = vmImageMessage.isDeleted();
                    }
                    updateRegistration(0, vmImageMessageIsDeleted);


                    if (vmImageMessageIsDeleted != null) {
                        // read vmImageMessage.isDeleted().get()
                        vmImageMessageIsDeletedGet = vmImageMessageIsDeleted.get();
                    }
                if((dirtyFlags & 0x1801L) != 0) {
                    if(vmImageMessageIsDeletedGet) {
                            dirtyFlags |= 0x4000L;
                    }
                    else {
                            dirtyFlags |= 0x2000L;
                    }
                }


                    // read vmImageMessage.isDeleted().get() ? View.GONE : View.VISIBLE
                    vmImageMessageIsDeletedViewGONEViewVISIBLE = ((vmImageMessageIsDeletedGet) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
            }
            if ((dirtyFlags & 0x1802L) != 0) {

                    if (vmImageMessage != null) {
                        // read vmImageMessage.msgDate
                        vmImageMessageMsgDate = vmImageMessage.getMsgDate();
                    }
                    updateRegistration(1, vmImageMessageMsgDate);


                    if (vmImageMessageMsgDate != null) {
                        // read vmImageMessage.msgDate.get()
                        vmImageMessageMsgDateGet = vmImageMessageMsgDate.get();
                    }
            }
            if ((dirtyFlags & 0x1844L) != 0) {

                    if (vmImageMessage != null) {
                        // read vmImageMessage.inSelectionMode
                        vmImageMessageInSelectionMode = vmImageMessage.getInSelectionMode();
                    }
                    updateRegistration(2, vmImageMessageInSelectionMode);


                    if (vmImageMessageInSelectionMode != null) {
                        // read vmImageMessage.inSelectionMode.get()
                        vmImageMessageInSelectionModeGet = vmImageMessageInSelectionMode.get();
                    }
                if((dirtyFlags & 0x1844L) != 0) {
                    if(vmImageMessageInSelectionModeGet) {
                            dirtyFlags |= 0x1000000L;
                    }
                    else {
                            dirtyFlags |= 0x800000L;
                    }
                }
            }
            if ((dirtyFlags & 0x1808L) != 0) {

                    if (vmImageMessage != null) {
                        // read vmImageMessage.uploadProgress
                        vmImageMessageUploadProgress = vmImageMessage.getUploadProgress();
                    }
                    updateRegistration(3, vmImageMessageUploadProgress);


                    if (vmImageMessageUploadProgress != null) {
                        // read vmImageMessage.uploadProgress.get()
                        vmImageMessageUploadProgressGet = vmImageMessageUploadProgress.get();
                    }


                    // read androidx.databinding.ViewDataBinding.safeUnbox(vmImageMessage.uploadProgress.get())
                    androidxDatabindingViewDataBindingSafeUnboxVmImageMessageUploadProgressGet = androidx.databinding.ViewDataBinding.safeUnbox(vmImageMessageUploadProgressGet);
            }
            if ((dirtyFlags & 0x1810L) != 0) {

                    if (vmImageMessage != null) {
                        // read vmImageMessage.msgTime
                        vmImageMessageMsgTime = vmImageMessage.getMsgTime();
                    }
                    updateRegistration(4, vmImageMessageMsgTime);


                    if (vmImageMessageMsgTime != null) {
                        // read vmImageMessage.msgTime.get()
                        vmImageMessageMsgTimeGet = vmImageMessageMsgTime.get();
                    }
            }
            if ((dirtyFlags & 0x1820L) != 0) {

                    if (vmImageMessage != null) {
                        // read vmImageMessage.imageUrl
                        vmImageMessageImageUrl = vmImageMessage.getImageUrl();
                    }
                    updateRegistration(5, vmImageMessageImageUrl);


                    if (vmImageMessageImageUrl != null) {
                        // read vmImageMessage.imageUrl.get()
                        vmImageMessageImageUrlGet = vmImageMessageImageUrl.get();
                    }
            }
            if ((dirtyFlags & 0x1c40L) != 0) {

                    if (vmImageMessage != null) {
                        // read vmImageMessage.uploading
                        vmImageMessageUploading = vmImageMessage.getUploading();
                        // read vmImageMessage.isFailed()
                        VmImageMessageIsFailed1 = vmImageMessage.isFailed();
                    }
                    updateRegistration(6, vmImageMessageUploading);
                    updateRegistration(10, VmImageMessageIsFailed1);


                    if (vmImageMessageUploading != null) {
                        // read vmImageMessage.uploading.get()
                        vmImageMessageUploadingGet = vmImageMessageUploading.get();
                    }
                    if (VmImageMessageIsFailed1 != null) {
                        // read vmImageMessage.isFailed().get()
                        vmImageMessageIsFailedGet = VmImageMessageIsFailed1.get();
                    }
                if((dirtyFlags & 0x1c00L) != 0) {
                    if(vmImageMessageIsFailedGet) {
                            dirtyFlags |= 0x400000L;
                    }
                    else {
                            dirtyFlags |= 0x200000L;
                    }
                }


                    // read !vmImageMessage.isFailed().get()
                    vmImageMessageIsFailed = !vmImageMessageIsFailedGet;


                    // read (vmImageMessage.uploading.get()) & (!vmImageMessage.isFailed().get())
                    vmImageMessageUploadingVmImageMessageIsFailed = (vmImageMessageUploadingGet) & (vmImageMessageIsFailed);
                if((dirtyFlags & 0x1c40L) != 0) {
                    if(vmImageMessageUploadingVmImageMessageIsFailed) {
                            dirtyFlags |= 0x100000L;
                    }
                    else {
                            dirtyFlags |= 0x80000L;
                    }
                }


                    // read (vmImageMessage.uploading.get()) & (!vmImageMessage.isFailed().get()) ? View.VISIBLE : View.GONE
                    vmImageMessageUploadingVmImageMessageIsFailedViewVISIBLEViewGONE = ((vmImageMessageUploadingVmImageMessageIsFailed) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                if ((dirtyFlags & 0x1c00L) != 0) {

                        // read vmImageMessage.isFailed().get() ? View.VISIBLE : View.GONE
                        vmImageMessageIsFailedViewVISIBLEViewGONE = ((vmImageMessageIsFailedGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                }
            }
            if ((dirtyFlags & 0x1880L) != 0) {

                    if (vmImageMessage != null) {
                        // read vmImageMessage.dateFillColor
                        vmImageMessageDateFillColor = vmImageMessage.getDateFillColor();
                    }
                    updateRegistration(7, vmImageMessageDateFillColor);


                    if (vmImageMessageDateFillColor != null) {
                        // read vmImageMessage.dateFillColor.get()
                        vmImageMessageDateFillColorGet = vmImageMessageDateFillColor.get();
                    }


                    // read androidx.databinding.ViewDataBinding.safeUnbox(vmImageMessage.dateFillColor.get())
                    androidxDatabindingViewDataBindingSafeUnboxVmImageMessageDateFillColorGet = androidx.databinding.ViewDataBinding.safeUnbox(vmImageMessageDateFillColorGet);
            }
            if ((dirtyFlags & 0x1a00L) != 0) {

                    if (vmImageMessage != null) {
                        // read vmImageMessage.isDateVisible()
                        vmImageMessageIsDateVisible = vmImageMessage.isDateVisible();
                    }
                    updateRegistration(9, vmImageMessageIsDateVisible);


                    if (vmImageMessageIsDateVisible != null) {
                        // read vmImageMessage.isDateVisible().get()
                        vmImageMessageIsDateVisibleGet = vmImageMessageIsDateVisible.get();
                    }
                if((dirtyFlags & 0x1a00L) != 0) {
                    if(vmImageMessageIsDateVisibleGet) {
                            dirtyFlags |= 0x40000L;
                    }
                    else {
                            dirtyFlags |= 0x20000L;
                    }
                }


                    // read vmImageMessage.isDateVisible().get() ? View.VISIBLE : View.GONE
                    vmImageMessageIsDateVisibleViewVISIBLEViewGONE = ((vmImageMessageIsDateVisibleGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
        }
        // batch finished

        if ((dirtyFlags & 0x800000L) != 0) {

                if (vmImageMessage != null) {
                    // read vmImageMessage.uploading
                    vmImageMessageUploading = vmImageMessage.getUploading();
                }
                updateRegistration(6, vmImageMessageUploading);


                if (vmImageMessageUploading != null) {
                    // read vmImageMessage.uploading.get()
                    vmImageMessageUploadingGet = vmImageMessageUploading.get();
                }
        }

        if ((dirtyFlags & 0x1844L) != 0) {

                // read vmImageMessage.inSelectionMode.get() ? true : vmImageMessage.uploading.get()
                vmImageMessageInSelectionModeBooleanTrueVmImageMessageUploading = ((vmImageMessageInSelectionModeGet) ? (true) : (vmImageMessageUploadingGet));
            if((dirtyFlags & 0x1844L) != 0) {
                if(vmImageMessageInSelectionModeBooleanTrueVmImageMessageUploading) {
                        dirtyFlags |= 0x10000L;
                }
                else {
                        dirtyFlags |= 0x8000L;
                }
            }


                // read vmImageMessage.inSelectionMode.get() ? true : vmImageMessage.uploading.get() ? View.VISIBLE : View.GONE
                vmImageMessageInSelectionModeBooleanTrueVmImageMessageUploadingViewVISIBLEViewGONE = ((vmImageMessageInSelectionModeBooleanTrueVmImageMessageUploading) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x1a00L) != 0) {
            // api target 1

            this.dateHeader.getRoot().setVisibility(vmImageMessageIsDateVisibleViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x1802L) != 0) {
            // api target 1

            this.dateHeader.setDate(vmImageMessageMsgDateGet);
        }
        if ((dirtyFlags & 0x1880L) != 0) {
            // api target 1

            this.dateHeader.setDateFillColor(androidxDatabindingViewDataBindingSafeUnboxVmImageMessageDateFillColorGet);
        }
        if ((dirtyFlags & 0x1000L) != 0) {
            // api target 1

            this.ivMsgOutgoing.setOnLongClickListener(mCallback18);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setRoundedCorner(this.mboundView4, true, (java.lang.Float)null, (java.lang.Float)null, mboundView4.getResources().getDimension(R.dimen.amity_zero), (java.lang.Float)null, (java.lang.Integer)null, (java.lang.Integer)null, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvTimeOutgoing, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
        if ((dirtyFlags & 0x1820L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setImageUrl(this.ivMsgOutgoing, vmImageMessageImageUrlGet, androidx.appcompat.content.res.AppCompatResources.getDrawable(ivMsgOutgoing.getContext(), R.drawable.amity_ic_image_failed));
        }
        if ((dirtyFlags & 0x1800L) != 0) {
            // api target 1

            this.mboundView01.setViewModel(vmImageMessage);
            this.mboundView11.setViewModel(vmImageMessage);
        }
        if ((dirtyFlags & 0x1801L) != 0) {
            // api target 1

            this.mboundView1.setVisibility(vmImageMessageIsDeletedViewGONEViewVISIBLE);
        }
        if ((dirtyFlags & 0x1c00L) != 0) {
            // api target 1

            this.mboundView11.getRoot().setVisibility(vmImageMessageIsFailedViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x1844L) != 0) {
            // api target 1

            this.mboundView4.setVisibility(vmImageMessageInSelectionModeBooleanTrueVmImageMessageUploadingViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x1808L) != 0) {
            // api target 1

            this.progressBar.setProgress(androidxDatabindingViewDataBindingSafeUnboxVmImageMessageUploadProgressGet);
        }
        if ((dirtyFlags & 0x1c40L) != 0) {
            // api target 1

            this.progressBar.setVisibility(vmImageMessageUploadingVmImageMessageIsFailedViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x1810L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvTimeOutgoing, vmImageMessageMsgTimeGet);
        }
        executeBindingsOn(dateHeader);
        executeBindingsOn(mboundView11);
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
        flag 0 (0x1L): vmImageMessage.isDeleted()
        flag 1 (0x2L): vmImageMessage.msgDate
        flag 2 (0x3L): vmImageMessage.inSelectionMode
        flag 3 (0x4L): vmImageMessage.uploadProgress
        flag 4 (0x5L): vmImageMessage.msgTime
        flag 5 (0x6L): vmImageMessage.imageUrl
        flag 6 (0x7L): vmImageMessage.uploading
        flag 7 (0x8L): vmImageMessage.dateFillColor
        flag 8 (0x9L): dateHeader
        flag 9 (0xaL): vmImageMessage.isDateVisible()
        flag 10 (0xbL): vmImageMessage.isFailed()
        flag 11 (0xcL): vmImageMessage
        flag 12 (0xdL): null
        flag 13 (0xeL): vmImageMessage.isDeleted().get() ? View.GONE : View.VISIBLE
        flag 14 (0xfL): vmImageMessage.isDeleted().get() ? View.GONE : View.VISIBLE
        flag 15 (0x10L): vmImageMessage.inSelectionMode.get() ? true : vmImageMessage.uploading.get() ? View.VISIBLE : View.GONE
        flag 16 (0x11L): vmImageMessage.inSelectionMode.get() ? true : vmImageMessage.uploading.get() ? View.VISIBLE : View.GONE
        flag 17 (0x12L): vmImageMessage.isDateVisible().get() ? View.VISIBLE : View.GONE
        flag 18 (0x13L): vmImageMessage.isDateVisible().get() ? View.VISIBLE : View.GONE
        flag 19 (0x14L): (vmImageMessage.uploading.get()) & (!vmImageMessage.isFailed().get()) ? View.VISIBLE : View.GONE
        flag 20 (0x15L): (vmImageMessage.uploading.get()) & (!vmImageMessage.isFailed().get()) ? View.VISIBLE : View.GONE
        flag 21 (0x16L): vmImageMessage.isFailed().get() ? View.VISIBLE : View.GONE
        flag 22 (0x17L): vmImageMessage.isFailed().get() ? View.VISIBLE : View.GONE
        flag 23 (0x18L): vmImageMessage.inSelectionMode.get() ? true : vmImageMessage.uploading.get()
        flag 24 (0x19L): vmImageMessage.inSelectionMode.get() ? true : vmImageMessage.uploading.get()
    flag mapping end*/
    //end
}