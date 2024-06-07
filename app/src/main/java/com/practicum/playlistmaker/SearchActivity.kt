package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

    private var searchTextValue = SEARCH_TEXT_VALUE
    private lateinit var editTextSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchBackButton = findViewById<ImageView>(R.id.searchBackButton)
        editTextSearch = findViewById<EditText>(R.id.editTextSearch)
        val imageViewSearchClear = findViewById<ImageView>(R.id.imageViewSearchClear)

        searchBackButton.setOnClickListener {
            finish()
        }

        imageViewSearchClear.setOnClickListener {
            editTextSearch.setText("")
            hideKeyBoard()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                imageViewSearchClear.isVisible = s.isNullOrEmpty().not()
            }

            override fun afterTextChanged(s: Editable?) {
                searchTextValue = s?.toString() ?: ""
            }

        }

        editTextSearch.addTextChangedListener(simpleTextWatcher)

        val trackAdapter = TrackAdapter(
            tracks = listOf(
                Track(
                    getString(R.string.track_name1),
                    getString(R.string.artist_name1),
                    getString(R.string.track_time1),
                    ARTWORK_URL_1
                ),
                Track(
                    getString(R.string.track_name2),
                    getString(R.string.artist_name2),
                    getString(R.string.track_time2),
                    ARTWORK_URL_2
                ),
                Track(
                    getString(R.string.track_name3),
                    getString(R.string.artist_name3),
                    getString(R.string.track_time3),
                    ARTWORK_URL_3
                ),
                Track(
                    getString(R.string.track_name4),
                    getString(R.string.artist_name4),
                    getString(R.string.track_time4),
                    ARTWORK_URL_4
                ),
                Track(
                    getString(R.string.track_name5),
                    getString(R.string.artist_name5),
                    getString(R.string.track_time5),
                    ARTWORK_URL_5
                ),
            )
        )

        val rwTrack = findViewById<RecyclerView>(R.id.rwTrack)
        rwTrack.adapter = trackAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT_KEY, searchTextValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchTextValue = savedInstanceState.getString(SEARCH_TEXT_KEY, SEARCH_TEXT_VALUE)
        editTextSearch.setText(searchTextValue)
    }

    private fun hideKeyBoard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }

    companion object {
        const val SEARCH_TEXT_VALUE = ""
        const val SEARCH_TEXT_KEY = "SEARCH_TEXT_KEY"
        const val ARTWORK_URL_1 =
            "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
        const val ARTWORK_URL_2 =
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
        const val ARTWORK_URL_3 =
            "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
        const val ARTWORK_URL_4 =
            "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
        const val ARTWORK_URL_5 =
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
    }
}