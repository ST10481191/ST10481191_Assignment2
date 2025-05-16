package com.example.history

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class QuestionsActivity : AppCompatActivity() {

    private var score = 0
    private var questionIndex = 0
    private lateinit var countDownTimer: CountDownTimer

    private val timePerQuestionMillis = 30_000L

    private val questions = listOf(
        Triple("Who discovered America in 1492?",
            listOf("Christopher Columbus", "Amerigo Vespucci", "Leif Erikson", "James Cook"),
            "Christopher Columbus"),
        Triple("Which wall divided East and West Berlin during the Cold War?",
            listOf("Berlin Wall", "Great Wall", "Iron Curtain", "Wall of Europe"),
            "Berlin Wall"),
        Triple("What was the name of the ship that sank in 1912 after hitting an iceberg?",
            listOf("Titanic", "Olympic", "Britannic", "Endeavour"),
            "Titanic"),
        Triple("Who was the leader of Nazi Germany during World War II?",
            listOf("Adolf Hitler", "Joseph Stalin", "Benito Mussolini", "Winston Churchill"),
            "Adolf Hitler"),
        Triple("What was the main cause of the American Civil War?",
            listOf("Slavery", "Taxation", "Land ownership", "Religious freedom"),
            "Slavery")
    )

    private lateinit var questionText: TextView
    private lateinit var optionA: Button
    private lateinit var optionB: Button
    private lateinit var optionC: Button
    private lateinit var optionD: Button
    private lateinit var nextButton: Button
    private lateinit var timerText: TextView

    private var selectedAnswer: String? = null
    private var answered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        // Link UI elements
        questionText = findViewById(R.id.questionText)
        optionA = findViewById(R.id.optionA)
        optionB = findViewById(R.id.optionB)
        optionC = findViewById(R.id.optionC)
        optionD = findViewById(R.id.optionD)
        nextButton = findViewById(R.id.nextButton)
        timerText = findViewById(R.id.timerText)

        // Handle option selection
        listOf(optionA, optionB, optionC, optionD).forEach { button ->
            button.setOnClickListener {
                if (!answered) {
                    selectAnswer(button)
                }
            }
        }

        nextButton.setOnClickListener {
            if (answered) {
                countDownTimer.cancel()
                goToNextQuestion()
            }
        }

        loadQuestion()
    }

    private fun loadQuestion() {
        // Reset visuals
        listOf(optionA, optionB, optionC, optionD).forEach {
            it.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))
            it.isEnabled = true
        }

        nextButton.isEnabled = false
        selectedAnswer = null
        answered = false

        // Load question and answers
        val (qText, options, _) = questions[questionIndex]
        questionText.text = "Question ${questionIndex + 1}: $qText"
        optionA.text = options[0]
        optionB.text = options[1]
        optionC.text = options[2]
        optionD.text = options[3]

        startTimer()
    }

    private fun selectAnswer(button: Button) {
        selectedAnswer = button.text.toString()
        answered = true

        // Disable all buttons
        listOf(optionA, optionB, optionC, optionD).forEach {
            it.isEnabled = false
        }

        // Check correctness
        val correctAnswer = questions[questionIndex].third
        if (selectedAnswer == correctAnswer) {
            score++
            button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
        } else {
            button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
            // Highlight correct answer
            listOf(optionA, optionB, optionC, optionD).find {
                it.text == correctAnswer
            }?.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
        }

        nextButton.isEnabled = true
        countDownTimer.cancel()
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timePerQuestionMillis, 1_000) {
            override fun onTick(ms: Long) {
                timerText.text = "Time left: ${ms / 1000} seconds"
            }

            override fun onFinish() {
                timerText.text = "Time's up!"
                // Simulate no answer
                answered = true
                listOf(optionA, optionB, optionC, optionD).forEach {
                    it.isEnabled = false
                }
                // Highlight correct answer
                val correctAnswer = questions[questionIndex].third
                listOf(optionA, optionB, optionC, optionD).find {
                    it.text == correctAnswer
                }?.setBackgroundColor(ContextCompat.getColor(this@QuestionsActivity, android.R.color.holo_green_light))

                nextButton.isEnabled = true
            }
        }.start()
    }

    private fun goToNextQuestion() {
        questionIndex++
        if (questionIndex < questions.size) {
            loadQuestion()
        } else {
            val intent = Intent(this, ScoreActivity::class.java).apply {
                putExtra("FINAL_SCORE", score)
                putExtra("TOTAL_QUESTIONS", questions.size)
            }
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::countDownTimer.isInitialized) countDownTimer.cancel()
    }
}
