package com.example.playlistmaker

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private var searchData: String = SEARCH_DEF
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private var lastSearch: String = SEARCH_DEF
    private var searchFieldEmpty: Boolean = true
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private var tracks = ArrayList<Track>()
    private lateinit var sharedPreferences:SharedPreferences
    private lateinit var searchHistory :SearchHistory
    private lateinit var searchHistoryTracks: List<Track>
    private lateinit var trackListAdapter: TrackListAdapter
    private lateinit var trackListRecyclerView: RecyclerView
    private lateinit var errorPlaceholderLayout: LinearLayout
    private lateinit var errorImage: ImageView
    private lateinit var errorText: TextView
    private lateinit var errorRefreshButton: Button
    private lateinit var loadingProgressBar: ProgressBar
    private val searchRunnable = Runnable {
        iTunesSearch(searchData)
    }
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        sharedPreferences = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)
        searchHistoryTracks = searchHistory.read()
        val backButton = findViewById<ImageButton>(R.id.back_btn)
        val searchTextField = findViewById<EditText>(R.id.search_field_edittext)
        loadingProgressBar = findViewById(R.id.search_pb)
        errorPlaceholderLayout = findViewById<LinearLayout>(R.id.error_placeholder_layout)
        errorImage = findViewById<ImageView>(R.id.error_placeholder_image)
        errorText = findViewById<TextView>(R.id.error_text)
        errorRefreshButton = findViewById<Button>(R.id.refresh_button)
        val clearHistoryButton = findViewById<Button>(R.id.clear_history_button)
        val searchHistoryTextView = findViewById<TextView>(R.id.search_history_textview)
        if (savedInstanceState != null) {
            searchData = savedInstanceState.getString(SEARCH_VALUE, SEARCH_DEF)
            lastSearch = savedInstanceState.getString(LAST_SEARCH, searchData)
        }
        searchTextField.setText(searchData)
        val clearButton = findViewById<ImageView>(R.id.clear_button)
        trackListRecyclerView = findViewById<RecyclerView>(R.id.track_list_recyclerview)
        trackListAdapter = TrackListAdapter(tracks, searchHistory)
        trackListRecyclerView.adapter = trackListAdapter
        clearHistoryButton.setOnClickListener {
            searchHistory.clear()
            tracks.clear()
            trackListAdapter.notifyDataSetChanged()
            clearHistoryButton.visibility = View.GONE
            searchHistoryTextView.visibility = View.GONE
        }
        val searchFieldTextWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchHistoryTracks = searchHistory.read()
                if (s.isNullOrEmpty()){
                    handler.removeCallbacksAndMessages(null)
                    if(searchTextField.hasFocus() && searchTextField.text.isEmpty() && searchHistoryTracks.isNotEmpty()){
                        clearHistoryButton.visibility = View.VISIBLE
                        searchHistoryTextView.visibility = View.VISIBLE
                        errorPlaceholderLayout.visibility = View.GONE
                        tracks.clear()
                        tracks.addAll(searchHistoryTracks)
                        trackListAdapter.notifyDataSetChanged()
                        trackListRecyclerView.visibility = View.VISIBLE

                    }
                    else{
                        clearHistoryButton.visibility = View.GONE
                        searchHistoryTextView.visibility = View.GONE
                        trackListRecyclerView.visibility = View.GONE
                    }
                    clearButton.visibility = View.GONE
                }
                else{
                    searchFieldEmpty=false
                    loadingProgressBar.isVisible=false
                    clearHistoryButton.visibility = View.GONE
                    trackListRecyclerView.visibility = View.GONE
                    searchHistoryTextView.visibility = View.GONE
                    clearButton.visibility = View.VISIBLE
                    searchData = s.toString()
                    searchDebounce()
                }
            }
            override fun afterTextChanged(s: Editable?) = Unit
        }
        searchTextField.addTextChangedListener(searchFieldTextWatcher)
        searchTextField.setOnFocusChangeListener { _, hasFocus ->
            searchHistoryTracks = searchHistory.read()
            if(hasFocus && searchTextField.text.isEmpty() && searchHistoryTracks.isNotEmpty()){
                clearHistoryButton.visibility = View.VISIBLE
                searchHistoryTextView.visibility = View.VISIBLE
                tracks.clear()
                tracks.addAll(searchHistoryTracks)
                trackListAdapter.notifyDataSetChanged()
                trackListRecyclerView.visibility = View.VISIBLE
            }
            else{
                clearHistoryButton.visibility = View.GONE
                searchHistoryTextView.visibility = View.GONE
                trackListRecyclerView.visibility = View.GONE
            }
        }
        clearButton.setOnClickListener {

            tracks.clear()
            trackListAdapter.notifyDataSetChanged()
            currentFocus?.let {
                val inputMethodManager = ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
                inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
            }
            searchTextField.setText("")
        }

        searchTextField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handler.removeCallbacks(searchRunnable, SEARCH_RUNNABLE_TOKEN)
                iTunesSearch(searchData)
            }
            false
        }
        errorRefreshButton.setOnClickListener {
            iTunesSearch(lastSearch)
        }

        backButton.setOnClickListener {
            finish()
        }

    }
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable, SEARCH_RUNNABLE_TOKEN)
        handler.postDelayed(searchRunnable ,SEARCH_RUNNABLE_TOKEN, SEARCH_DEBOUNCE_DELAY)
    }
    private fun iTunesSearch(query: String) {
        if (query.isNotEmpty()) {
            loadingProgressBar.isVisible=true
            errorPlaceholderLayout.isVisible=false
            trackListRecyclerView.isVisible=false
            iTunesService.search(query).enqueue(object : Callback<ITunesResponse> {
                override fun onResponse(call: Call<ITunesResponse>,
                                        response: Response<ITunesResponse>
                ) {
                    loadingProgressBar.isVisible=false
                    if (response.isSuccessful) {
                        val results = response.body()?.results
                        if(results!= null){
                            tracks.clear()
                            if (results.isEmpty()) {
                                trackListAdapter.notifyDataSetChanged()
                                trackListRecyclerView.visibility = View.GONE
                                errorPlaceholderLayout.visibility = View.VISIBLE
                                errorImage.setImageResource(R.drawable.no_search_results_placeholder)
                                errorText.text = resources.getText(R.string.no_search_results)
                                errorRefreshButton.visibility = View.GONE
                            } else {
                                tracks.addAll(results)
                                trackListAdapter.notifyDataSetChanged()
                                errorPlaceholderLayout.visibility = View.GONE
                                trackListRecyclerView.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        lastSearch = query
                        trackListRecyclerView.visibility = View.GONE
                        errorRefreshButton.visibility = View.VISIBLE
                        errorPlaceholderLayout.visibility = View.VISIBLE
                        errorImage.setImageResource(R.drawable.no_connection_placeholder)
                        errorText.text = resources.getText(R.string.connection_problem)
                    }
                }
                override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                    lastSearch = query
                    trackListRecyclerView.visibility = View.GONE
                    loadingProgressBar.isVisible=false
                    errorRefreshButton.visibility = View.VISIBLE
                    errorPlaceholderLayout.visibility = View.VISIBLE
                    errorImage.setImageResource(R.drawable.no_connection_placeholder)
                    errorText.text = resources.getText(R.string.connection_problem)
                }

            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE, searchData)
        outState.putString(LAST_SEARCH, lastSearch)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchData = savedInstanceState.getString(SEARCH_VALUE, SEARCH_DEF)
        lastSearch = savedInstanceState.getString(LAST_SEARCH, searchData)
    }
    companion object {
        const val SEARCH_RUNNABLE_TOKEN = 1
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
        const val SEARCH_VALUE = "SEARCH_VALUE"
        const val SEARCH_DEF = ""
        const val LAST_SEARCH = "LAST_SEARCH"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}