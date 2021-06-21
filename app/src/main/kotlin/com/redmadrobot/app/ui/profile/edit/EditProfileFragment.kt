package com.redmadrobot.app.ui.profile.edit

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.redmadrobot.app.R
import com.redmadrobot.app.databinding.FragmentEditProfileBinding
import com.redmadrobot.app.di.DI
import com.redmadrobot.app.ui.base.fragment.BaseFragment
import com.redmadrobot.app.ui.base.viewmodel.SetSelectedDateEvent
import com.redmadrobot.app.ui.base.viewmodel.ShowDatePikerEvent
import com.redmadrobot.app.utils.extension.toDate
import com.redmadrobot.domain.model.UserProfile
import com.redmadrobot.extensions.lifecycle.Event
import com.redmadrobot.extensions.lifecycle.observe
import com.redmadrobot.extensions.viewbinding.viewBinding
import javax.inject.Inject

class EditProfileFragment :
    BaseFragment(R.layout.fragment_edit_profile),
    DialogInterface.OnDismissListener,
    MaterialPickerOnPositiveButtonClickListener<Long> {

    private companion object {
        private const val DATE_PIKER = "date_piker"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<EditProfileViewModel> { viewModelFactory }
    private val binding by viewBinding<FragmentEditProfileBinding>()

    override val anchorViewId: Int
        get() = R.id.edit_profile_button_save

    override fun onEvent(event: Event) {
        when (event) {
            is ShowDatePikerEvent -> {
                with(datePiker()) {
                    addOnPositiveButtonClickListener(this@EditProfileFragment)
                    addOnDismissListener(this@EditProfileFragment)
                    show(this@EditProfileFragment.childFragmentManager, DATE_PIKER)
                }
            }
            is SetSelectedDateEvent -> {
                binding.editProfileEditTextBirthDate.setText(event.date)
            }
            else -> {
                super.onEvent(event)
            }
        }
    }

    override fun inject() {
        DI.appComponent.inject(this)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        viewModel.onDatePickerDismiss()
    }

    override fun onPositiveButtonClick(selection: Long) {
        viewModel.onDatePikerPositiveClicked(selection.toDate())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.eventsQueue, ::onEvent)
        observe(viewModel.isSaveButtonEnabled, ::renderSaveButton)
        observe(viewModel.isLoading, ::showProgressLoader)
        observe(viewModel.currentUserData, ::setCurrentProfileData)

        binding.editProfileToolbar.setNavigationOnClickListener {
            viewModel.onToolbarBackClicked()
        }

        binding.editProfileEditTextNickname.doAfterTextChanged { nickname ->
            viewModel.onNicknameTextChanged(nickname = nickname.toString())
        }

        binding.signUpSecondEditTextName.doAfterTextChanged { firstName ->
            viewModel.onFirstNameTextChanged(name = firstName.toString())
        }

        binding.editProfileEditTextSurname.doAfterTextChanged { secondName ->
            viewModel.onSecondNameTextChanged(surname = secondName.toString())
        }

        binding.editProfileEditTextBirthDate.doAfterTextChanged { date ->
            viewModel.onDateTextChanged(date = date.toString())
        }

        binding.editProfileTextLayoutBirthDate.setEndIconOnClickListener {
            viewModel.onCalendarIconClicked()
        }

        binding.editProfileButtonSave.setOnClickListener {
            viewModel.onSaveClicked(
                nickname = binding.editProfileEditTextNickname.text.toString(),
                firstName = binding.signUpSecondEditTextName.text.toString(),
                secondName = binding.editProfileEditTextSurname.text.toString(),
                birthDate = binding.editProfileEditTextBirthDate.text.toString()
            )
        }
    }

    private fun datePiker(date: Long = MaterialDatePicker.todayInUtcMilliseconds()): MaterialDatePicker<Long> {
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.before(System.currentTimeMillis()))
                .build()
        return MaterialDatePicker.Builder.datePicker()
            .setSelection(date)
            .setCalendarConstraints(constraintsBuilder)
            .build()
    }

    private fun renderSaveButton(isButtonEnabled: Boolean) {
        binding.editProfileButtonSave.isEnabled = isButtonEnabled
    }

    private fun setCurrentProfileData(profile: UserProfile) {
        binding.editProfileEditTextNickname.setText(profile.nickname)
        binding.signUpSecondEditTextName.setText(profile.firstName)
        binding.editProfileEditTextSurname.setText(profile.lastName)
        binding.editProfileEditTextBirthDate.setText(profile.birthDay)
    }
}
