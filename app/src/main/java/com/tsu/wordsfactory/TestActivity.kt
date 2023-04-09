package com.tsu.wordsfactory

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.room.Room
import com.tsu.wordsfactory.databinding.ActivityTestBinding
import kotlinx.coroutines.*

class TestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestBinding

    private var wordsAndMeaning = arrayListOf<Pair<String, String>>()
    private var words = arrayListOf<String>()
    private var wrongWords = arrayListOf<Pair<String, String>>()

    private var size = 0
    private var rightAnswers = 0
    private var position = 0
    private lateinit var database: WordDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Room.databaseBuilder(this.applicationContext,
            WordDB::class.java, "words_database").fallbackToDestructiveMigration().build()

        binding.testOption1.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                it.setBackgroundResource(R.drawable.selected_answer_background)
                delay(700)
                checkAnswer(binding.testOption1.text.toString())
            }
        }

        binding.testOption2.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                it.setBackgroundResource(R.drawable.selected_answer_background)
                delay(700)
                checkAnswer(binding.testOption2.text.toString())
            }
        }

        binding.testOption3.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                it.setBackgroundResource(R.drawable.selected_answer_background)
                delay(700)
                checkAnswer(binding.testOption3.text.toString())
            }
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }

        testStart()
    }

    private fun testStart() {
        position = 0
        rightAnswers = 0
        wrongWords.clear()

        lifecycleScope.launch(Dispatchers.IO) {
            val wordsForTest = ArrayList(database.wordDao().getWords())

            wordsForTest.forEach {
                val word = it.word
                words.add(word)
            }

            words.forEach{
                val meanings = database.meaningDao().findMeanings(it)
                wordsAndMeaning.add(Pair(meanings[0].word, meanings[(meanings.indices).random()].definition))
            }
            println(wordsAndMeaning)

            size = database.wordDao().getCount()

            if (size > 2) {
                val allWords = ArrayList(database.wordDao().getAllWords())
                println(allWords)

                for (i in 0 until size) {
                    println(wordsAndMeaning[i].first)
                    println(allWords.indexOf(wordsAndMeaning[i].first))

                    /*allWords.drop(allWords.indexOf(wordsAndMeaning[i].first))*/
                    /*println(allWords)*/
                    wrongWords.add(Pair(allWords[(0 until size).random()], allWords[(0 until size).random()]))
                    /*println(wrongWords)*/
                }

                //  Неправильные ответы должны быть взяты из сохраненных слов (но они не должны быть равны правильному)
            // чтоб одинаковых не было и чтоб не равны
            }

            drawAnswerTest()
        }
    }

    private fun drawAnswerTest(){
        /*timerStart()*/

        if (position < size) {
            binding.positionText.text = "${position + 1} of $size"
            binding.questionText.text = wordsAndMeaning[position].second

            var answerVariants = listOf("cooking", "smiling", wordsAndMeaning[position].first)
            if (size > 2) {
                answerVariants = listOf(
                    wordsAndMeaning[position].first,
                    wrongWords[position].first,
                    wrongWords[position].second
                )
            }

            var index = (0 ..2).random()
            binding.testOption1.text = "A. ${answerVariants[index]}"
            answerVariants = answerVariants.filterIndexed { i, _ -> i != index}
            index = (0..1).random()
            binding.testOption2.text = "B. ${answerVariants[index]}"
            answerVariants = answerVariants.filterIndexed { i, _ -> i != index}
            binding.testOption3.text = "C. ${answerVariants[0]}"
        }
        else {
            val intent = Intent(this@TestActivity, TrainingFinishActivity::class.java)
            intent.putExtra("correct", rightAnswers.toString())
            intent.putExtra("incorrect", (size - rightAnswers).toString())
            startActivity(intent)
            finish()
        }
    }

    private fun checkAnswer(word : String) {
        binding.testOption1.setBackgroundResource(R.drawable.input_background)
        binding.testOption2.setBackgroundResource(R.drawable.input_background)
        binding.testOption3.setBackgroundResource(R.drawable.input_background)

        val temp = position
        if (word.removeRange(0, 3).lowercase() == wordsAndMeaning[position].first.lowercase()){
            rightAnswers++
        }
        position++
        drawAnswerTest()
    }

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    private fun startCoroutineTimer(delayMillis: Long = 0, repeatMillis: Long = 0, action: () -> Unit) = scope.launch(
        Dispatchers.IO) {
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
        binding.progressBarLinear.max = 8
        binding.progressBarLinear.min = 0

        var second = 0

        val timer: Job = startCoroutineTimer(0, 1000) {
            lifecycleScope.launch(Dispatchers.Main) {
                binding.progressBarLinear.progress = second
                /*val animator = ObjectAnimator.ofInt(binding.progressBarLinear, "progress", second)
                animator.duration = 1000
                animator.start()*/
            }
            second++
        }

        lifecycleScope.launch(Dispatchers.Main) {
            delay(8000)
            timer.cancelAndJoin()
            checkAnswer("   ")
        }
    }
}