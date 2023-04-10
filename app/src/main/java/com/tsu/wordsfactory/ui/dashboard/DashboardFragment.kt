package com.tsu.wordsfactory.ui.dashboard

import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.room.Room
import com.tsu.wordsfactory.R
import com.tsu.wordsfactory.WordDB
import com.tsu.wordsfactory.databinding.FragmentDashboardBinding
import kotlinx.coroutines.*

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var builder: AlertDialog.Builder

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var size = 0
    var text = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val database = Room.databaseBuilder(activity!!.applicationContext,
            WordDB::class.java, "words_database").fallbackToDestructiveMigration().build()
        builder = AlertDialog.Builder(activity!!)

        lifecycleScope.launch(Dispatchers.IO) {
            size = database.wordDao().getCount()

            lifecycleScope.launch(Dispatchers.Main) {
                if (size != 0) {
                    text = "There are $size words \n in your Dictionary. \n\n Start the Training?"
                    val spannableString = SpannableString(text)
                    val foregroundColorSpanCyan = ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.primary))
                    spannableString.setSpan(foregroundColorSpanCyan, 10, 10 + size.toString().length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                    binding.countWordText.text = spannableString
                } else {
                    text = "Add words to the dictionary"
                    binding.countWordText.text = text
                }

                binding.buttonStartTraining.setOnClickListener {
                    if (size != 0) {
                        binding.buttonStartTraining.visibility = View.INVISIBLE
                        binding.progressBarCircle.visibility = View.VISIBLE
                        binding.progressText.visibility = View.VISIBLE
                        timerStart()

                    } else {
                        builder.setTitle("Error").
                        setMessage("Add words to the dictionary").
                        setCancelable(true).
                        setPositiveButton("Ok") { dialog, id ->
                            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT)
                        }.show()
                    }
                }

            }
        }
        return root
    }

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)
    private fun startCoroutineTimer(delayMillis: Long = 0, repeatMillis: Long = 0, action: () -> Unit) =
        scope.launch(Dispatchers.IO) {
            delay(delayMillis)
            if (repeatMillis > 0) {
                while (isActive) {
                    action()
                    delay(repeatMillis)
                }
            } else {
                action()
            }
        }

    private fun timerStart() {
        var second = 6

        val timer: Job = startCoroutineTimer(0, 1050) {
            second--
            lifecycleScope.launch(Dispatchers.Main) {
                binding.progressText.text = second.toString()

                when (second) {
                    5 -> {
                        binding.progressText.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.primary
                            )
                        )
                    }
                    4 -> {
                        binding.progressText.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.blue
                            )
                        )
                        binding.progressBarCircle.setIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.blue
                            )
                        )
                    }
                    3 -> {
                        binding.progressText.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                        binding.progressBarCircle.setIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            )
                        )
                    }
                    2 -> {
                        binding.progressText.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.yellow
                            )
                        )
                        binding.progressBarCircle.setIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.yellow
                            )
                        )
                    }
                    1 -> {
                        binding.progressText.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.red
                            )
                        )
                        binding.progressBarCircle.setIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.red
                            )
                        )
                    }
                    else -> {
                        binding.progressText.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.primary
                            )
                        )
                        binding.progressBarCircle.setIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.primary
                            )
                        )
                        binding.progressText.setText(R.string.go)
                    }
                }

                val animator = ObjectAnimator.ofInt(binding.progressBarCircle, "progress", 0, 100)
                animator.duration = 1000
                animator.start()
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            delay(6300)
            timer.cancelAndJoin()

            val navController = Navigation.findNavController(binding.root)
            navController.navigate(R.id.action_navigation_dashboard_to_testActivity)

            binding.progressBarCircle.visibility = View.INVISIBLE
            binding.progressText.visibility = View.INVISIBLE
            binding.buttonStartTraining.visibility = View.VISIBLE
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}