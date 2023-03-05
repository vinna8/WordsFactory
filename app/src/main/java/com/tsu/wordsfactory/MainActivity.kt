package com.tsu.wordsfactory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    private lateinit var onboardingItemsAdapter: OnboardingItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setOnboardingItems()
    }

    private fun setOnboardingItems() {
        onboardingItemsAdapter = OnboardingItemsAdapter(
            listOf(
                OnboardingItem(
                    onboardingImage = R.drawable.intro1,
                    title = "Learn anytime \n and anywhere",
                    textButton = "Next"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.intro2,
                    title = "Find a course \n for you",
                    textButton = "Next"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.intro3,
                    title = "Improve your skills",
                    textButton = "Let’s Start"
                ),
            )
        )
        val onboardingViewPager = findViewById<ViewPager2>(R.id.onboardingViewPager)
        onboardingViewPager.adapter = onboardingItemsAdapter
    }
}