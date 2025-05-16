package com.example.history

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ScoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        // Retrieve score data
        val score = intent.getIntExtra("FINAL_SCORE", 0)
        val total = intent.getIntExtra("TOTAL_QUESTIONS", 0)
        val percent = if (total > 0) (score * 100) / total else 0

        // UI references
        val scoreText = findViewById<TextView>(R.id.scoreText)
        val feedbackText = findViewById<TextView>(R.id.feedbackText)
        val progressBar = findViewById<ProgressBar>(R.id.scoreProgressBar)
        val retryButton = findViewById<Button>(R.id.retryButton)
        val exitButton = findViewById<Button>(R.id.exitButton)

        // Display numeric score
        scoreText.text = "You scored $score out of $total"

        // Update progress bar
        progressBar.max = 100
        progressBar.progress = percent

        // Personalized feedback
        val feedback = when {
            percent >= 80 -> "Excellent! 🎉"
            percent >= 50 -> "Good job! 👍"
            percent >= 30 -> "Not bad, keep learning! 🤓"
            else -> "Try again for a better score! 💪"
        }
        feedbackText.text = feedback

        // Retry quiz
        retryButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Exit button
        exitButton.setOnClickListener {
            finishAffinity()
        }
    }
}



