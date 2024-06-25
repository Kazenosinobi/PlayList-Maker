package com.practicum.playlistmaker.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.api.iTunesApi
import com.practicum.playlistmaker.models.TracksResponse
import com.practicum.playlistmaker.recycler.TrackAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private var searchTextValue = SEARCH_TEXT_VALUE
    private var editTextSearch: EditText? = null
    private var trackAdapter: TrackAdapter? = null
    private var searchBackButton: ImageView? = null
    private var imageViewSearchClear: ImageView? = null
    private var rwTrack: RecyclerView? = null
    private var llErrors: LinearLayout? = null
    private var llNotInternet: LinearLayout? = null
    private var reconnectButton: Button? = null
    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    private val iTunesService = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(iTunesApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        trackAdapter = TrackAdapter()
        initViews()
        searchBackButton?.setOnClickListener {
            finish()
        }

        imageViewSearchClear?.setOnClickListener {
            editTextSearch?.setText("")
            hideKeyBoard()
            trackAdapter?.tracks = emptyList()
            trackAdapter?.notifyDataSetChanged()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                imageViewSearchClear?.isVisible = s.isNullOrEmpty().not()
            }

            override fun afterTextChanged(s: Editable?) {
                searchTextValue = s?.toString() ?: ""
            }

        }

        editTextSearch?.addTextChangedListener(simpleTextWatcher)

        editTextSearch?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }

        reconnectButton?.setOnClickListener { search() }
        rwTrack?.adapter = trackAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT_KEY, searchTextValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchTextValue = savedInstanceState.getString(SEARCH_TEXT_KEY, SEARCH_TEXT_VALUE)
        editTextSearch?.setText(searchTextValue)
    }

    private fun initViews() {
        searchBackButton = findViewById<ImageView>(R.id.searchBackButton)
        editTextSearch = findViewById<EditText>(R.id.editTextSearch)
        imageViewSearchClear = findViewById<ImageView>(R.id.imageViewSearchClear)
        rwTrack = findViewById<RecyclerView>(R.id.rwTrack)
        llErrors = findViewById<LinearLayout>(R.id.llErrors)
        llNotInternet = findViewById<LinearLayout>(R.id.llNotInternet)
        reconnectButton = findViewById(R.id.reconnectButton)
    }

    private fun search() {
        iTunesService.searchTracks(editTextSearch?.text.toString())
            .enqueue(object : Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    val result = response.body()?.results
                    if (result != null) {
                        when (response.code()) {
                            200 -> {
                                if (result.isNotEmpty()) {
                                    trackAdapter?.tracks = result
                                    trackAdapter?.notifyDataSetChanged()
                                    rwTrack?.visibility = View.VISIBLE
                                    llErrors?.visibility = View.GONE
                                    llNotInternet?.visibility = View.GONE
                                } else {
                                    showEmpty()
                                }
                            }

                            else -> showError()
                        }
                    } else {
                        showEmpty()
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    showError()
                }

            })
    }

    private fun showEmpty() {
        rwTrack?.visibility = View.GONE
        llErrors?.visibility = View.VISIBLE
    }

    private fun showError() {
        rwTrack?.visibility = View.GONE
        llNotInternet?.visibility = View.VISIBLE
    }

    private fun hideKeyBoard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }


    companion object {
        const val SEARCH_TEXT_VALUE = ""
        const val SEARCH_TEXT_KEY = "SEARCH_TEXT_KEY"
    }
}