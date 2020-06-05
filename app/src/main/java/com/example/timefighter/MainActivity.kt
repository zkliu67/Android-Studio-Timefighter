package com.example.timefighter

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

// The logic for the game screen goes.
class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    // Lateinit: a type of property that wont have a value when class is instantiate.
    private lateinit var gameScoreTextView: TextView
    private lateinit var timeLeftTextView: TextView
    private lateinit var tapMeButton: Button

    private var gameStarted = false // Indicate whether the game start.
    private var score = 0

    private lateinit var countDownTimer: CountDownTimer
    private var initialCountDown: Long = 10000
    private var countDownInterval: Long = 1000 // The rate the countdown timer decrements.
    private var timeLeft = 10 // the seconds left.

    companion object {
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    // onCreate is the entry point to this activity.
    // While onCreate is not the only entry point, but it is most common one.
    // It can work with other methods to make up an Activity life cycle.
    // override with a custom implementation from the base class.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Required to call the base implementation.

        // Takes the layout and puts it on the device screen by passing in the identifier
        // for the layout. R.layout.LAYOUT_FILE_NAME_TO_BE_USED
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreated called. Score is: $score")

        // connect views to variables
        gameScoreTextView = findViewById(R.id.game_score_text_view)
        timeLeftTextView = findViewById(R.id.time_left_text_view)
        tapMeButton = findViewById(R.id.tap_me_button)

        // Attach a click listener to the button.
        // If the button is clicked, call incrementScore function.
        // Add a callback to enable animation.
        tapMeButton.setOnClickListener { v ->
            val bounceAnimation = AnimationUtils.loadAnimation(this,
                R.anim.bounce)
            v.startAnimation(bounceAnimation)
            incrementScore()
         }

        //resetGame() // called every time opening the app, or changing any configuration.

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getInt(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }
    }

    // Bundle: a hashmap Android uses to pass values across different screens.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()

        Log.d(TAG, "onSaveInstanceState: Saving score: $score & time: $timeLeft")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called.")
    }

    // attempt to create the menu.
    // use menuInflater to programmatically set up the own menu
    // return true that the menu is setup.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it presents.
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    // Calls whenever a user selects a menu item.
    // Check the id if the selected menu is equal to the id of the item you set up earlier.
    // return true that the event is processed.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === R.id.about_item) {
            showInfo()
        }
        return true
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
        val initialTimeLeft = getString(R.string.time_left, 10)
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

    private fun restoreGame() {

        val restoredScore = getString(R.string.your_score, score)
        gameScoreTextView.text = restoredScore

        // update the timer string.
        val restoredTimeLeft = getString(R.string.time_left, timeLeft)
        timeLeftTextView.text = restoredTimeLeft

        // Create a new countDownTimer and pass the initial value.
        countDownTimer = object : CountDownTimer((timeLeft * 1000).toLong(), countDownInterval) {
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

        countDownTimer.start()
        gameStarted = true
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

    private fun showInfo() {
        val dialogTitle = getString(R.string.about_title, BuildConfig.VERSION_NAME)
        val dialogMessage = getString(R.string.about_message)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
    }
}