package com.acidumirae.fortunecookie

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.acidumirae.fortunecookie.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private var fortunes: List<String> = emptyList()
    private var currentFortuneIndex = 0
    private var isCookieBroken = false
    
    companion object {
        // Default fortune URL - can be changed to any URL that returns text fortunes
        private const val FORTUNE_URL = "https://raw.githubusercontent.com/bmc/fortunes/master/fortunes"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViews()
        loadFortunes()
    }
    
    private fun setupViews() {
        // Set initial cookie image
        binding.fortuneCookieImage.setImageResource(R.drawable.fortune_cookie_whole)
        
        // Cookie click listener
        binding.fortuneCookieImage.setOnClickListener {
            if (!isCookieBroken && fortunes.isNotEmpty()) {
                breakCookie()
            }
        }
        
        // Reset button click listener
        binding.resetButton.setOnClickListener {
            resetCookie()
        }
    }
    
    private fun loadFortunes() {
        // Show loading indicator
        binding.loadingProgress.visibility = View.VISIBLE
        binding.loadingText.visibility = View.VISIBLE
        binding.fortuneCookieImage.visibility = View.GONE
        binding.instructionText.visibility = View.GONE
        
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val loadedFortunes = fetchFortunesFromUrl(FORTUNE_URL)
                
                withContext(Dispatchers.Main) {
                    if (loadedFortunes.isNotEmpty()) {
                        fortunes = loadedFortunes.shuffled()
                        currentFortuneIndex = 0
                        
                        // Hide loading, show cookie
                        binding.loadingProgress.visibility = View.GONE
                        binding.loadingText.visibility = View.GONE
                        binding.fortuneCookieImage.visibility = View.VISIBLE
                        binding.instructionText.visibility = View.VISIBLE
                        
                        Toast.makeText(
                            this@MainActivity,
                            "Loaded ${fortunes.size} fortunes!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        showError()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showError()
                }
            }
        }
    }
    
    private fun fetchFortunesFromUrl(urlString: String): List<String> {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        
        try {
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val content = BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                    reader.readText()
                }
                
                // Parse fortunes - assuming they're separated by % character
                return content.split("%")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() && it.length > 10 }
                    .take(100) // Limit to 100 fortunes
            }
        } finally {
            connection.disconnect()
        }
        
        return emptyList()
    }
    
    private fun showError() {
        binding.loadingProgress.visibility = View.GONE
        binding.loadingText.visibility = View.GONE
        binding.fortuneCookieImage.visibility = View.VISIBLE
        binding.instructionText.visibility = View.VISIBLE
        
        Toast.makeText(
            this,
            R.string.error_loading,
            Toast.LENGTH_LONG
        ).show()
        
        // Use sample fortunes as fallback
        fortunes = listOf(
            "A beautiful, smart, and loving person will be coming into your life.",
            "Your ability to juggle many tasks will take you far.",
            "A golden egg of opportunity falls into your lap this month.",
            "You will be successful in your work.",
            "Good news will come to you by mail.",
            "The fortune you seek is in another cookie.",
            "You will have a pleasant surprise.",
            "Adventure can be real happiness.",
            "All your hard work will soon pay off.",
            "Be brave enough to live creatively."
        )
        currentFortuneIndex = 0
    }
    
    private fun breakCookie() {
        isCookieBroken = true
        
        // Start break animation
        val breakAnim = AnimationUtils.loadAnimation(this, R.anim.cookie_break)
        binding.fortuneCookieImage.startAnimation(breakAnim)
        
        // After animation, change to broken cookie and show fortune
        breakAnim.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}
            
            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                // Change to broken cookie image
                binding.fortuneCookieImage.setImageResource(R.drawable.fortune_cookie_broken)
                binding.fortuneCookieImage.alpha = 1f
                
                // Show the fortune text with animation
                val currentFortune = fortunes[currentFortuneIndex]
                binding.fortuneText.text = currentFortune
                
                val fortuneAnim = AnimationUtils.loadAnimation(
                    this@MainActivity,
                    R.anim.fortune_appear
                )
                binding.fortuneText.startAnimation(fortuneAnim)
                binding.fortuneText.alpha = 1f
                
                // Hide instruction, show reset button
                binding.instructionText.visibility = View.GONE
                binding.resetButton.visibility = View.VISIBLE
            }
            
            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
        })
    }
    
    private fun resetCookie() {
        isCookieBroken = false
        
        // Move to next fortune for next time
        currentFortuneIndex = (currentFortuneIndex + 1) % fortunes.size
        
        // Hide fortune text and reset button
        binding.fortuneText.alpha = 0f
        binding.fortuneText.text = ""
        binding.resetButton.visibility = View.GONE
        
        // Show instruction text
        binding.instructionText.visibility = View.VISIBLE
        
        // Change back to whole cookie with animation
        binding.fortuneCookieImage.setImageResource(R.drawable.fortune_cookie_whole)
        val appearAnim = AnimationUtils.loadAnimation(this, R.anim.cookie_appear)
        binding.fortuneCookieImage.startAnimation(appearAnim)
    }
}
