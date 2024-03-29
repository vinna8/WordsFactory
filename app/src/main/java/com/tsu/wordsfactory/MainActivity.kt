package com.tsu.wordsfactory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tsu.wordsfactory.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var onboardingItemsAdapter: OnboardingItemsAdapter
    private lateinit var indicatorsContainer: LinearLayout
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnboardingItems()
        setupIndicators()
        setCurrentIndicator(0)
    }

    private fun setOnboardingItems() {
        onboardingItemsAdapter = OnboardingItemsAdapter(
            listOf(
                OnboardingItem(
                    onboardingImage = R.drawable.intro1,
                    title = "Learn anytime \n and anywhere",
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.intro2,
                    title = "Find a course \n for you",
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.intro3,
                    title = "\n Improve your skills",
                ),
            )
        )
        val onboardingViewPager = binding.onboardingViewPager
        onboardingViewPager.adapter = onboardingItemsAdapter
        onboardingViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentIndicator(position)
                }
            })
        (onboardingViewPager.getChildAt(0) as RecyclerView).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER
        binding.buttonOndoarding.setOnClickListener{
            if (onboardingViewPager.currentItem + 1 < onboardingItemsAdapter.itemCount) {
                onboardingViewPager.currentItem += 1
            } else {
                navigateToSignUpActivity()
            }
        }
        binding.textSkip.setOnClickListener{
            navigateToSignUpActivity()
        }
    }

    private fun navigateToSignUpActivity() {
        startActivity(Intent(applicationContext, SignUpActivity::class.java))
        finish()
    }

    private fun setupIndicators() {
        indicatorsContainer = binding.linearLayoutPagination
        val indicators = arrayOfNulls<ImageView>(onboardingItemsAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(12,0,12,0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_background
                    )
                )
                it.layoutParams = layoutParams
                indicatorsContainer.addView(it)
            }
        }
    }

    private fun setCurrentIndicator(position: Int) {
        val childCount = indicatorsContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorsContainer.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active_background
                    )
                )
                if (position == childCount - 1) {
                    binding.buttonOndoarding.setText(R.string.start)
                } else {
                    binding.buttonOndoarding.setText(R.string.next)
                }
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_background
                    )
                )
            }
        }
    }
}