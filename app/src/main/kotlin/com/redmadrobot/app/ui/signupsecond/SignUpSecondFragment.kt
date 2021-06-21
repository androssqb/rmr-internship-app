package com.redmadrobot.app.ui.signupsecond

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.redmadrobot.app.R
import com.redmadrobot.app.databinding.FragmentSignUpSecondBinding
import com.redmadrobot.app.di.DI
import com.redmadrobot.app.ui.base.fragment.BaseFragment
import com.redmadrobot.app.ui.base.viewmodel.SetSelectedDateEvent
import com.redmadrobot.app.ui.base.viewmodel.ShowDatePikerEvent
import com.redmadrobot.app.utils.extension.assistedViewModel
import com.redmadrobot.app.utils.extension.toDate
import com.redmadrobot.extensions.lifecycle.Event
import com.redmadrobot.extensions.lifecycle.observe
import com.redmadrobot.extensions.viewbinding.viewBinding
import javax.inject.Inject

class SignUpSecondFragment :
    BaseFragment(R.layout.fragment_sign_up_second),
    DialogInterface.OnDismissListener,
    MaterialPickerOnPositiveButtonClickListener<Long> {

    private companion object {
        private const val DATE_PIKER = "date_piker"
    }

    @Inject
    lateinit var factory: SignUpSecondViewModel.Factory

    private val viewModel by assistedViewModel { factory.create(email = args.email, password = args.password) }
    private val binding by viewBinding<FragmentSignUpSecondBinding>()
    private val args by navArgs<SignUpSecondFragmentArgs>()

    override val anchorViewId: Int
        get() = R.id.sign_up_second_button_check_in

    override fun onEvent(event: Event) {
        when (event) {
            is ShowDatePikerEvent -> {
                with(datePiker()) {
                    addOnPositiveButtonClickListener(this@SignUpSecondFragment)
                    addOnDismissListener(this@SignUpSecondFragment)
                    show(this@SignUpSecondFragment.childFragmentManager, DATE_PIKER)
                }
            }
            is SetSelectedDateEvent -> {
                binding.signUpSecondEditTextBirthDate.setText(event.date)
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
        observe(viewModel.checkInButtonEnabled, ::renderCheckIn)
        observe(viewModel.isLoading, ::showProgressLoader)

        binding.signUpSecondToolbar.setNavigationOnClickListener {
            viewModel.onToolbarBackClicked()
        }

        binding.signUpSecondEditTextNickname.doAfterTextChanged { nickname ->
            viewModel.onNicknameTextChanged(nickname = nickname.toString())
        }

        binding.signUpSecondEditTextName.doAfterTextChanged { firstName ->
            viewModel.onFirstNameTextChanged(name = firstName.toString())
        }

        binding.signUpSecondEditTextSurname.doAfterTextChanged { secondName ->
            viewModel.onSecondNameTextChanged(surname = secondName.toString())
        }

        binding.signUpSecondEditTextBirthDate.doAfterTextChanged { date ->
            viewModel.onDateTextChanged(date = date.toString())
        }

        binding.signUpSecondButtonCheckIn.setOnClickListener {
            viewModel.onCheckInClicked(
                nickname = binding.signUpSecondEditTextNickname.text.toString(),
                firstName = binding.signUpSecondEditTextName.text.toString(),
                secondName = binding.signUpSecondEditTextSurname.text.toString(),
                birthDate = binding.signUpSecondEditTextBirthDate.text.toString()
            )
        }

        binding.signUpSecondTextLayoutBirthDate.setEndIconOnClickListener {
            viewModel.onCalendarIconClicked()
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

    private fun renderCheckIn(isButtonEnabled: Boolean) {
        binding.signUpSecondButtonCheckIn.isEnabled = isButtonEnabled
    }
}
