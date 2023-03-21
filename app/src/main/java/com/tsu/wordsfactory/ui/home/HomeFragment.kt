package com.tsu.wordsfactory.ui.home

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tsu.wordsfactory.DictionaryAPI
import com.tsu.wordsfactory.R
import com.tsu.wordsfactory.Word
import com.tsu.wordsfactory.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.IOException

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var audioURL = ""
    private val mediaPlayer = MediaPlayer()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonSearch.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.dictionaryapi.dev/api/v2/entries/en/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(DictionaryAPI::class.java)
            val myResponse: MutableLiveData<Word> = MutableLiveData()

            lifecycleScope.launch(Dispatchers.IO) {
                val enteredWord = binding.editTextTextSearch.text.toString()
                val result = service.getWord(enteredWord)

                println(result.toString())
                println(result[0].toString())

                withContext(Dispatchers.Main) {
                    myResponse.value = result[0]
                }
            }
            myResponse.observe(this, Observer{
                binding.textWord.text = it.word
                binding.textTranscription.text = it.phonetic
                binding.textPartOfSpeech.text = it.meanings[0].partOfSpeech

                audioURL = it.phonetics[0].audio

                if (audioURL.isNotEmpty()) {
                    binding.imageSound.setImageResource(R.drawable.ic_sound)
                } else {
                    binding.imageSound.setBackgroundResource(R.color.white)
                }
            })
        }

        binding.imageSound.setOnClickListener {
            playAudio()
        }

        return root
    }

    private fun playAudio() {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(audioURL)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}