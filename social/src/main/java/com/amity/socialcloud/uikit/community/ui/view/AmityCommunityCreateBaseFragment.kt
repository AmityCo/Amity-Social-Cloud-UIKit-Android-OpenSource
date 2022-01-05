package com.amity.socialcloud.uikit.community.ui.view

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amity.socialcloud.sdk.core.error.AmityException
import com.amity.socialcloud.sdk.core.file.AmityUploadResult
import com.amity.socialcloud.uikit.common.common.showSnackBar
import com.amity.socialcloud.uikit.common.contract.AmityPickImageContract
import com.amity.socialcloud.uikit.common.utils.AmityAlertDialogUtil
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.data.AmitySelectCategoryItem
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentCreateCommunityBinding
import com.amity.socialcloud.uikit.community.detailpage.AmityCommunityPageActivity
import com.amity.socialcloud.uikit.community.explore.activity.AmityCategoryPickerActivity
import com.amity.socialcloud.uikit.community.home.activity.AmityCommunityHomePageActivity
import com.amity.socialcloud.uikit.community.ui.viewModel.AmityCreateCommunityViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.trello.rxlifecycle3.components.support.RxFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.amity_fragment_create_community.*
import kotlinx.android.synthetic.main.amity_item_settings_nav_content.*
import timber.log.Timber

abstract class AmityCommunityCreateBaseFragment : RxFragment() {

    private val TAG = AmityCommunityCreateBaseFragment::class.java.canonicalName
    var disposable = CompositeDisposable()
    private var imageUri: Uri? = null
    lateinit var viewModel: AmityCreateCommunityViewModel
    internal lateinit var mBinding: AmityFragmentCreateCommunityBinding

    private val pickImage = registerForActivityResult(AmityPickImageContract()) { data ->
        if(data != null) {
            imageUri = data
            viewModel.initialStateChanged.set(true)
            Glide.with(this)
                .load(data)
                .centerCrop()
                .placeholder(R.drawable.amity_ic_default_community_avatar)
                .into(ccAvatar)
        }
    }

    private val pickImagePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                pickImage.launch(getString(com.amity.socialcloud.uikit.common.R.string.amity_choose_image))
            } else {
                this.rootView.showSnackBar("Permission denied", Snackbar.LENGTH_SHORT)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.amity_fragment_create_community, container, false
        )
        mBinding.setLifecycleOwner(this)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity() as AppCompatActivity).get(AmityCreateCommunityViewModel::class.java)
        mBinding.viewModel = viewModel

        mBinding.category.setOnClickListener {
            launchCategorySelection(viewModel.category.get()!!)
        }

        mBinding.categoryArrow.setOnClickListener {
            launchCategorySelection(viewModel.category.get()!!)
        }
        addInitialStateChangeListener()
        setUpBackPress()
        setAvatar()
        uploadImageAndCreateCommunity()
    }

    private fun uploadImageAndCreateCommunity() {
        mBinding.btnCreateCommunity.setOnClickListener {
            viewModel.createIdList()
            uploadImage(false)
        }
        mBinding.btnEditCommunity.setOnClickListener {
            uploadImage(true)
        }
    }

    private fun addInitialStateChangeListener() {
        viewModel.setPropertyChangeCallback()
    }

    fun getBindingVariable(): AmityFragmentCreateCommunityBinding = mBinding

    private fun pickImage() {
        pickImagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private fun launchCategorySelection(preSelectedCategoryAmity: AmitySelectCategoryItem) {
        selectCategoryContract.launch(preSelectedCategoryAmity)
    }

    open fun renderAvatar() {
        Glide.with(requireContext())
            .load(R.drawable.amity_ic_default_community_avatar)
            .centerCrop()
            .into(mBinding.ccAvatar)
    }

    private fun setAvatar() {

        renderAvatar()

        mBinding.lAvatar.setOnClickListener {
            pickImage()
        }

        ccName.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3

                if (event?.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (ccName.right - ccName.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                        viewModel.communityName.set("")
                    }
                }
                return false
            }
        })

        etDescription.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3

                if (event?.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (etDescription.right - etDescription.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                        viewModel.description.set("")
                    }
                }
                return false
            }
        })
    }

    private fun createCommunity() {
        disposable.add(viewModel.createCommunity()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                viewModel.communityId.set(it.getCommunityId())
                viewModel.savedCommunityId = it.getCommunityId()
                val detailIntent = AmityCommunityPageActivity
                    .newIntent(requireContext(), it, true)
                startActivity(detailIntent)
                requireActivity().finish()
            }
            .doOnError {
                Timber.e(TAG, "createCommunity: ${it.localizedMessage}")
            }.subscribe()
        )
    }

    fun onLeftIconClick() {
        if (viewModel.initialStateChanged.get() == true) {
            showDialog()
        } else {
            requireActivity().finish()
        }
    }

    private fun setUpBackPress() {
        activity?.onBackPressedDispatcher?.addCallback(
            requireActivity(),
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.initialStateChanged.get() == true) {
                        showDialog()
                    } else {
                        requireActivity().finish()
                    }
                }
            })
    }

    private fun showDialog() {
        AmityAlertDialogUtil.showDialog(requireContext(),
            getString(R.string.amity_cc_leave),
            getString(R.string.amity_cc_dialog_msg),
            getString(R.string.amity_leave),
            getString(R.string.amity_cancel),
            DialogInterface.OnClickListener { dialog, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    requireActivity().finish()
                } else {
                    dialog.cancel()
                }
            })
    }

    fun uploadImage(isEditCommunity: Boolean) {
        if (isEditCommunity) {
            viewModel.initialStateChanged.set(false)
        } else {
            mBinding.btnCreateCommunity.isEnabled = false
        }

        if (imageUri != null) {
            disposable.add(viewModel.uploadProfilePicture(imageUri!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { uploadStatus ->
                    when (uploadStatus) {
                        is AmityUploadResult.COMPLETE -> {
                            viewModel.amityImage = uploadStatus.getFile()
                            if (isEditCommunity) {
                                editCommunity()
                            } else {
                                createCommunity()
                            }
                        }
                        is AmityUploadResult.ERROR, AmityUploadResult.CANCELLED -> {
                            btnCreateCommunity.isEnabled = true
                            mBinding.btnCreateCommunity.isEnabled = true
                            view?.showSnackBar(getString(R.string.amity_image_upload_error), Snackbar.LENGTH_SHORT)
                        }
                        else -> {
                        }
                    }
                }.doOnError {
                    Timber.e(TAG, "uploadImageAndCreateCommunity: ${it.localizedMessage}")
                    mBinding.btnCreateCommunity.isEnabled = true
                    viewModel.initialStateChanged.set(true)
                }.subscribe()
            )
        } else {
            if (isEditCommunity) {
                editCommunity()
            } else {
                createCommunity()
            }
        }
    }

    private fun editCommunity() {
        viewModel.editCommunity().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                requireActivity().finish()
            }.doOnError { exception ->
                if (exception is AmityException) {
                    if (exception.code == AmityConstants.NO_PERMISSION_ERROR_CODE) {
                        AmityAlertDialogUtil.showNoPermissionDialog(requireContext(),
                            DialogInterface.OnClickListener { dialog, _ ->
                                dialog?.dismiss()
                                checkUserRole()
                            })
                    }
                }

            }.subscribe()
    }

    private fun checkUserRole() {
        disposable.add(viewModel.getCommunityDetail().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .firstOrError()
            .doOnSuccess {
                if (it.isJoined()) {
                    requireActivity().finish()
                } else {
                    val intent =
                        Intent(requireContext(), AmityCommunityHomePageActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }.doOnError {
                Timber.e(TAG, "checkUserRole: ${it.localizedMessage}")
            }.subscribe()
        )
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    private var selectCategoryContract = registerForActivityResult(
        AmityCategoryPickerActivity.AmityCategorySelectionActivityContract()
    ) {
        it?.let {
            viewModel.setCategory(it)
        }
    }
}