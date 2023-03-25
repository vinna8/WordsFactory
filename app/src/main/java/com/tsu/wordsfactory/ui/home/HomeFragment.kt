package com.tsu.wordsfactory.ui.home

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.tsu.wordsfactory.*
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
    private lateinit var meaningItemsAdapter: MeaningItemsAdapter
    private lateinit var builder: AlertDialog.Builder

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val mediaPlayer = MediaPlayer()

    var word = ""
    var phonetic = ""
    var audioURL = ""
    var partOfSpeech = ""
    var definitions = arrayListOf<Definitions>()

    /*var definition = ""
    var example = ""*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val database = Room.databaseBuilder(activity!!.applicationContext,
            WordDB::class.java, "words_database").build()
        builder = AlertDialog.Builder(activity!!)

        binding.buttonSearch.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.dictionaryapi.dev/api/v2/entries/en/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(DictionaryAPI::class.java)
            val myResponse: MutableLiveData<Word> = MutableLiveData()

            lifecycleScope.launch(Dispatchers.IO) {
                val enteredWord = binding.editTextTextSearch.text.toString()
                /*val result = service.getWord(enteredWord)*/

                /*println(result.toString())
                println(result[0].toString())*/

                if (!checkNetwork()) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val words = database.wordDao().findWord(enteredWord)
                        println(words)

                        if (words !== null) {
                        /*println(words)*/
                            word = words.word
                            phonetic = words.phonetic
                            audioURL = words.audio
                            partOfSpeech = words.partOfSpeech

                            activity?.runOnUiThread{updateLayout()}
                        } else {
                            withContext(Dispatchers.Main) {
                                builder.setTitle("Error").
                                    setMessage("There is no internet connection and the word hasn't been added to the dictionary before").
                                    setCancelable(true).
                                    setPositiveButton("Ok") { dialog, id ->
                                        Toast.makeText(activity, "Error", Toast.LENGTH_SHORT)
                                    }.show()
                            }
                        }
                    }
                } else {
                    val result = service.getWord(enteredWord)

                    withContext(Dispatchers.Main) {
                        myResponse.value = result[0]
                    }
                }
            }
            myResponse.observe(this, Observer{
                /*binding.textWord.text = it.word
                binding.textTranscription.text = it.phonetic
                binding.textPartOfSpeech.text = it.meanings[0].partOfSpeech

                binding.titleTextPartOfSpeech.setText(R.string.titleTextPartOfSpeech)
                binding.textMeanings.setText(R.string.meaning)*/
                word = it.word
                phonetic = it.phonetic
                audioURL = it.phonetics[0].audio
                partOfSpeech = it.meanings[0].partOfSpeech
                definitions = it.meanings[0].definitions

                updateLayout()

                /*if (audioURL.isNotEmpty()) {
                    binding.imageSound.setImageResource(R.drawable.ic_sound)
                } else {
                    binding.imageSound.setImageResource(0)
                }

                binding.recyclerViewMeanings.layoutManager = LinearLayoutManager(context)
                binding.recyclerViewMeanings.adapter = MeaningItemsAdapter(definitions)*/
            })
        }

        binding.imageSound.setOnClickListener {
            if (!checkNetwork()) {
                builder.setTitle("Error").
                    setMessage("No internet connection").
                    setCancelable(true).
                    setPositiveButton("Ok") { dialog, id ->
                        Toast.makeText(activity, "Error", Toast.LENGTH_SHORT)
                    }.show()
            } else {
                playAudio()
            }
        }

        binding.buttonAddToDictionary.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                /*if (database.wordDao().findWord(word).word !== null) {
                    withContext(Dispatchers.Main) {
                        builder.setTitle("Alert").
                            setMessage("The word has already been added to the dictionary").
                            setCancelable(true).
                            setPositiveButton("Ok") { dialog, id ->
                                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT)
                            }.show()
                        }
                } else {*/
                    database.wordDao()
                        .insertWord(WordEntity(word, phonetic, audioURL, partOfSpeech))
                /*}*/
            }
        }

        return root
    }

    private fun updateLayout () {
        binding.textWord.text = word
        binding.textTranscription.text = phonetic
        binding.textPartOfSpeech.text = partOfSpeech

        binding.titleTextPartOfSpeech.setText(R.string.titleTextPartOfSpeech)
        binding.textMeanings.setText(R.string.meaning)

        if (audioURL.isNotEmpty()) {
            binding.imageSound.setImageResource(R.drawable.ic_sound)
        } else {
            binding.imageSound.setImageResource(0)
        }

        binding.recyclerViewMeanings.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewMeanings.adapter = MeaningItemsAdapter(definitions)
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

    private fun checkNetwork(): Boolean {
        val connectivityManager =
            activity!!.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}