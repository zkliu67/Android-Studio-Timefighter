package com.example.timefighter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

// The logic for the game screen goes.
class MainActivity : AppCompatActivity() {

    // Lateinit: a type of property that wont have a value when class is instantiate.
    private lateinit var gameScoreTextView: TextView
    private lateinit var timeLeftTextView: TextView
    private lateinit var tapMeButton: Button

    private var gameStarted = false // Indicate whether the game start.
    private var score = 0

    private lateinit var countDownTimer: CountDownTimer
    private var initialCountDown: Long = 5000
    private var countDownInterval: Long = 1000 // The rate the countdown timer decrements.
    private var timeLeft = 60 // the seconds left.

    // onCreate is the entry point to this activity.
    // While onCreate is not the only entry point, but it is most common one.
    // It can work with other methods to make up an Activity life cycle.
    // override with a custom implementation from the base class.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Required to call the base implementation.

        // Takes the layout and puts it on the device screen by passing in the identifier
        // for the layout. R.layout.LAYOUT_FILE_NAME_TO_BE_USED
        setContentView(R.layout.activity_main)

        // connect views to variables
        gameScoreTextView = findViewById(R.id.game_score_text_view)
        timeLeftTextView = findViewById(R.id.time_left_text_view)
        tapMeButton = findViewById(R.id.tap_me_button)

        // Attach a click listener to the button.
        // If the button is clicked, call incrementScore function.
        tapMeButton.setOnClickListener { incrementScore() }
        resetGame() // called every time opening the app.
    }

    private fun incrementScore() {
        if (!gameStarted) {
            startGame()
        }

        // Increment score login
        score ++
        // getString: a built-in method for referencing string from R file
        // by name OR id, and replace the placeholder with a given value.
        val newScore = getString(R.string.your_score, score)
        gameScoreTextView.text = newScore

    }

    private fun resetGame() {
        score = 0 //reset the score value to 0.

        // update the text string.
        val initialScore = getString(R.string.your_score, score)
        gameScoreTextView.text = initialScore

        // update the timer string.
        val initialTimeLeft = getString(R.string.time_left, 5)
        timeLeftTextView.text = initialTimeLeft

        // Create a new countDownTimer and pass the initial value.
        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            // Called every interval passing into the timer,
            // namely, once a second.
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000

                // Update time left string.
                val timeLeftString = getString(R.string.time_left, timeLeft)
                timeLeftTextView.text = timeLeftString
            }

            // Called when the timer counting down to zero.
            override fun onFinish() {
                endGame()
            }
        }

        gameStarted = false
    }

    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.game_over_message, score),
            Toast.LENGTH_LONG).show()
        resetGame()
    }
}