package com.ekoapp.ekosdk.uikit.community.databinding;
import com.ekoapp.ekosdk.uikit.community.R;
import com.ekoapp.ekosdk.uikit.community.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityFragmentEditUserProfileBindingImpl extends AmityFragmentEditUserProfileBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.avatarView, 8);
        sViewsWithIds.put(R.id.tvAboutLabel, 9);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers
    private androidx.databinding.InverseBindingListener etAboutandroidTextAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of vm.about.getValue()
            //         is vm.about.setValue((java.lang.String) callbackArg_0)
            java.lang.String callbackArg_0 = androidx.databinding.adapters.TextViewBindingAdapter.getTextString(etAbout);
            // localize variables for thread safety
            // vm != null
            boolean vmJavaLangObjectNull = false;
            // vm
            com.ekoapp.ekosdk.uikit.community.profile.viewmodel.EkoEditUserProfileViewModel vm = mVm;
            // vm.about
            androidx.lifecycle.MutableLiveData<java.lang.String> vmAbout = null;
            // vm.about != null
            boolean vmAboutJavaLangObjectNull = false;
            // vm.about.getValue()
            java.lang.String vmAboutGetValue = null;



            vmJavaLangObjectNull = (vm) != (null);
            if (vmJavaLangObjectNull) {


                vmAbout = vm.getAbout();

                vmAboutJavaLangObjectNull = (vmAbout) != (null);
                if (vmAboutJavaLangObjectNull) {




                    vmAbout.setValue(((java.lang.String) (callbackArg_0)));
                }
            }
        }
    };
    private androidx.databinding.InverseBindingListener etDisplayNameandroidTextAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of vm.displayName.getValue()
            //         is vm.displayName.setValue((java.lang.String) callbackArg_0)
            java.lang.String callbackArg_0 = androidx.databinding.adapters.TextViewBindingAdapter.getTextString(etDisplayName);
            // localize variables for thread safety
            // vm != null
            boolean vmJavaLangObjectNull = false;
            // vm
            com.ekoapp.ekosdk.uikit.community.profile.viewmodel.EkoEditUserProfileViewModel vm = mVm;
            // vm.displayName.getValue()
            java.lang.String vmDisplayNameGetValue = null;
            // vm.displayName
            androidx.lifecycle.MutableLiveData<java.lang.String> vmDisplayName = null;
            // vm.displayName != null
            boolean vmDisplayNameJavaLangObjectNull = false;



            vmJavaLangObjectNull = (vm) != (null);
            if (vmJavaLangObjectNull) {


                vmDisplayName = vm.getDisplayName();

                vmDisplayNameJavaLangObjectNull = (vmDisplayName) != (null);
                if (vmDisplayNameJavaLangObjectNull) {




                    vmDisplayName.setValue(((java.lang.String) (callbackArg_0)));
                }
            }
        }
    };

    public AmityFragmentEditUserProfileBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 10, sIncludes, sViewsWithIds));
    }
    private AmityFragmentEditUserProfileBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 2
            , (android.widget.FrameLayout) bindings[8]
            , (com.google.android.material.textfield.TextInputEditText) bindings[7]
            , (com.google.android.material.textfield.TextInputEditText) bindings[5]
            , (com.google.android.material.imageview.ShapeableImageView) bindings[1]
            , (com.google.android.material.imageview.ShapeableImageView) bindings[2]
            , (android.widget.TextView) bindings[6]
            , (android.widget.TextView) bindings[9]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[4]
            );
        this.etAbout.setTag(null);
        this.etDisplayName.setTag(null);
        this.ivAvatar.setTag(null);
        this.ivCamera.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.tvAboutCount.setTag(null);
        this.tvDisplayNameLabel.setTag(null);
        this.tvUserNameCount.setTag(null);
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
        if (BR.vm == variableId) {
            setVm((com.ekoapp.ekosdk.uikit.community.profile.viewmodel.EkoEditUserProfileViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setVm(@Nullable com.ekoapp.ekosdk.uikit.community.profile.viewmodel.EkoEditUserProfileViewModel Vm) {
        this.mVm = Vm;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.vm);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeVmAbout((androidx.lifecycle.MutableLiveData<java.lang.String>) object, fieldId);
            case 1 :
                return onChangeVmDisplayName((androidx.lifecycle.MutableLiveData<java.lang.String>) object, fieldId);
        }
        return false;
    }
    private boolean onChangeVmAbout(androidx.lifecycle.MutableLiveData<java.lang.String> VmAbout, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmDisplayName(androidx.lifecycle.MutableLiveData<java.lang.String> VmDisplayName, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
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
        androidx.lifecycle.MutableLiveData<java.lang.String> vmAbout = null;
        java.lang.String vmAboutTrimLengthJavaLangString = null;
        com.ekoapp.ekosdk.uikit.community.profile.viewmodel.EkoEditUserProfileViewModel vm = mVm;
        int vmAboutMaxTextLength = 0;
        java.lang.String vmDisplayNameGetValue = null;
        int vmAboutTrimLength = 0;
        java.lang.String vmAboutTrimLengthJavaLangStringVmAboutMaxTextLength = null;
        int vmDisplayNameTrimLength = 0;
        int vmUserNameMaxTextLength = 0;
        androidx.lifecycle.MutableLiveData<java.lang.String> vmDisplayName = null;
        java.lang.String vmDisplayNameTrim = null;
        java.lang.String vmAboutGetValue = null;
        java.lang.String vmDisplayNameTrimLengthJavaLangStringVmUserNameMaxTextLength = null;
        java.lang.String vmAboutTrim = null;
        java.lang.String vmDisplayNameTrimLengthJavaLangString = null;

        if ((dirtyFlags & 0xfL) != 0) {


            if ((dirtyFlags & 0xdL) != 0) {

                    if (vm != null) {
                        // read vm.about
                        vmAbout = vm.getAbout();
                        // read vm.aboutMaxTextLength
                        vmAboutMaxTextLength = vm.getAboutMaxTextLength();
                    }
                    updateLiveDataRegistration(0, vmAbout);


                    if (vmAbout != null) {
                        // read vm.about.getValue()
                        vmAboutGetValue = vmAbout.getValue();
                    }


                    if (vmAboutGetValue != null) {
                        // read vm.about.getValue().trim()
                        vmAboutTrim = vmAboutGetValue.trim();
                    }


                    if (vmAboutTrim != null) {
                        // read vm.about.getValue().trim().length()
                        vmAboutTrimLength = vmAboutTrim.length();
                    }


                    // read (vm.about.getValue().trim().length()) + ("/")
                    vmAboutTrimLengthJavaLangString = (vmAboutTrimLength) + ("/");


                    // read ((vm.about.getValue().trim().length()) + ("/")) + (vm.aboutMaxTextLength)
                    vmAboutTrimLengthJavaLangStringVmAboutMaxTextLength = (vmAboutTrimLengthJavaLangString) + (vmAboutMaxTextLength);
            }
            if ((dirtyFlags & 0xeL) != 0) {

                    if (vm != null) {
                        // read vm.userNameMaxTextLength
                        vmUserNameMaxTextLength = vm.getUserNameMaxTextLength();
                        // read vm.displayName
                        vmDisplayName = vm.getDisplayName();
                    }
                    updateLiveDataRegistration(1, vmDisplayName);


                    if (vmDisplayName != null) {
                        // read vm.displayName.getValue()
                        vmDisplayNameGetValue = vmDisplayName.getValue();
                    }


                    if (vmDisplayNameGetValue != null) {
                        // read vm.displayName.getValue().trim()
                        vmDisplayNameTrim = vmDisplayNameGetValue.trim();
                    }


                    if (vmDisplayNameTrim != null) {
                        // read vm.displayName.getValue().trim().length()
                        vmDisplayNameTrimLength = vmDisplayNameTrim.length();
                    }


                    // read (vm.displayName.getValue().trim().length()) + ("/")
                    vmDisplayNameTrimLengthJavaLangString = (vmDisplayNameTrimLength) + ("/");


                    // read ((vm.displayName.getValue().trim().length()) + ("/")) + (vm.userNameMaxTextLength)
                    vmDisplayNameTrimLengthJavaLangStringVmUserNameMaxTextLength = (vmDisplayNameTrimLengthJavaLangString) + (vmUserNameMaxTextLength);
            }
        }
        // batch finished
        if ((dirtyFlags & 0xdL) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.etAbout, vmAboutGetValue);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvAboutCount, vmAboutTrimLengthJavaLangStringVmAboutMaxTextLength);
        }
        if ((dirtyFlags & 0x8L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setTextWatcher(this.etAbout, (androidx.databinding.adapters.TextViewBindingAdapter.BeforeTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.OnTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged)null, etAboutandroidTextAttrChanged);
            androidx.databinding.adapters.TextViewBindingAdapter.setTextWatcher(this.etDisplayName, (androidx.databinding.adapters.TextViewBindingAdapter.BeforeTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.OnTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged)null, etDisplayNameandroidTextAttrChanged);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.ivAvatar, getColorFromResource(ivAvatar, R.color.amityColorPrimary), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE3);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.ivCamera, getColorFromResource(ivCamera, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE4);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setRequiredInLabel(this.tvDisplayNameLabel, true);
        }
        if ((dirtyFlags & 0xeL) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.etDisplayName, vmDisplayNameGetValue);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvUserNameCount, vmDisplayNameTrimLengthJavaLangStringVmUserNameMaxTextLength);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): vm.about
        flag 1 (0x2L): vm.displayName
        flag 2 (0x3L): vm
        flag 3 (0x4L): null
    flag mapping end*/
    //end
}