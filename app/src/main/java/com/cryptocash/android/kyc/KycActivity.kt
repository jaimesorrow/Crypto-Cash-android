package com.cryptocash.android.kyc

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cryptocash.android.R
import com.cryptocash.android.databinding.ActivityKycBinding

class KycActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKycBinding
    private var currentStep = 1
    private val totalSteps = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKycBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.kyc_title)

        updateStep(1)
        binding.btnNext.setOnClickListener { advanceStep() }
        binding.btnBack.setOnClickListener {
            if (currentStep > 1) updateStep(currentStep - 1) else finish()
        }
    }

    private fun advanceStep() {
        when (currentStep) {
            1 -> {
                val name = binding.etFullName.text?.toString().orEmpty().trim()
                val dob = binding.etDob.text?.toString().orEmpty().trim()
                if (name.isEmpty() || dob.isEmpty()) {
                    Toast.makeText(this, getString(R.string.kyc_fill_all_fields), Toast.LENGTH_SHORT).show()
                    return
                }
                updateStep(2)
            }
            2 -> updateStep(3)
            3 -> updateStep(4)
            4 -> {
                Toast.makeText(this, getString(R.string.kyc_submitted), Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun updateStep(step: Int) {
        currentStep = step
        val progress = (step * 100) / totalSteps
        binding.stepIndicator.progress = progress
        binding.stepIndicator.contentDescription = getString(R.string.kyc_step_cd, step, totalSteps)
        binding.tvStepCounter.text = getString(R.string.kyc_step_counter, step, totalSteps)
        binding.tvStepTitle.text = stepTitle(step)
        binding.tvStepDesc.text = stepDesc(step)

        binding.layoutPersonalInfo.visibility = if (step == 1) View.VISIBLE else View.GONE
        binding.layoutDocType.visibility = if (step == 2) View.VISIBLE else View.GONE
        binding.layoutDocUpload.visibility = if (step == 3) View.VISIBLE else View.GONE
        binding.layoutReview.visibility = if (step == 4) View.VISIBLE else View.GONE

        binding.btnBack.visibility = if (step == 1) View.INVISIBLE else View.VISIBLE
        binding.btnNext.text = if (step == totalSteps) getString(R.string.kyc_submit) else getString(R.string.next)
    }

    private fun stepTitle(step: Int) = when (step) {
        1 -> getString(R.string.kyc_step1_title)
        2 -> getString(R.string.kyc_step2_title)
        3 -> getString(R.string.kyc_step3_title)
        4 -> getString(R.string.kyc_step4_title)
        else -> ""
    }

    private fun stepDesc(step: Int) = when (step) {
        1 -> getString(R.string.kyc_step1_desc)
        2 -> getString(R.string.kyc_step2_desc)
        3 -> getString(R.string.kyc_step3_desc)
        4 -> getString(R.string.kyc_step4_desc)
        else -> ""
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
