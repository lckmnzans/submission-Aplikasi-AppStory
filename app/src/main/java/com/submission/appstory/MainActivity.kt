package com.submission.appstory

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.appstory.api.ApiConfig
import com.submission.appstory.databinding.ActivityMainBinding
import com.submission.appstory.response.ListStoryItem
import com.submission.appstory.response.StoriesResponse
import com.submission.appstory.stories.Story
import com.submission.appstory.stories.StoryAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG = "MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvStories.layoutManager = LinearLayoutManager(this)
        val token = getSharedPreferences("LoginSession", Context.MODE_PRIVATE).getString("token", "")
        getStories(token.toString())

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_action, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_setting -> {
                getSharedPreferences("LoginSession", Context.MODE_PRIVATE).edit().clear().apply()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return true
    }
    private fun getStories(token: String) {
        binding.loadMain.visibility = View.VISIBLE
        val call = ApiConfig.getApiService(token).getAllStories()
        call.enqueue(object: Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                if (response.isSuccessful) {
                    binding.loadMain.visibility = View.GONE
                    val responseBody = response.body()!!
                    setUserStories(responseBody.listStory)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                binding.loadMain.visibility = View.GONE
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setUserStories(stories: List<ListStoryItem>) {
        val list = ArrayList<Story>()
        for (story in stories) {
            val storyData = Story(story.id.toString(), story.name.toString(), story.photoUrl.toString(), story.description.toString())
            list.add(storyData)
        }

        val listStory = StoryAdapter(list)
        binding.rvStories.adapter = listStory

        listStory.setOnItemClickCallback(object: StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Story) {
                val storyDetail = Story(data.id, data.userName, data.avtUrl, data.desc)
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STORY, storyDetail)
                startActivity(intent)
            }
        })
    }
}