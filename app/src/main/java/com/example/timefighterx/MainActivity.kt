package com.example.timefighterx

import android.annotation.SuppressLint
import android.nfc.Tag
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
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    internal var score = 0
    internal var gamestarted = false
    internal lateinit var countDownTimer: CountDownTimer
    internal var initialCd: Long = 60000
    internal var cdIndervall: Long = 1000

    internal lateinit var textView1: TextView
    internal lateinit var textView2: TextView
    internal lateinit var btnTapzz : Button
    internal val TAG = MainActivity::class.java.simpleName
    internal var timeLeftOnTimer : Long = 60000

    companion object {
        private val SCORE_KEY = "SCORE_KEY"
        private val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "Oncreate called score is $score")

        textView1 = findViewById(R.id.tv1)
        textView2 = findViewById(R.id.tv2)
        btnTapzz = findViewById(R.id.btnTap)
        textView1.text = getString(R.string.your_score, score.toString())

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState .getLong(TIME_LEFT_KEY)
            restorGame()

        } else {
            resetGame()
        }

        btnTapzz.setOnClickListener {
            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
            it.startAnimation(bounceAnimation)

            incrementScore()
        }
    }

    fun restorGame() {
        textView1.text = getString(R.string.your_score, score.toString())
        val restoredTime = timeLeftOnTimer / 1000
        textView2.text = getString(R.string.your_time, restoredTime.toString())

        countDownTimer = object: CountDownTimer(timeLeftOnTimer, cdIndervall) {
            override fun onTick(p0: Long) {
                timeLeftOnTimer = p0
                var timeleft = p0 /1000
                textView2.text = getString(R.string.your_time, timeleft.toString())
            }

            override fun onFinish() {
                endGame()
            }
        }
        countDownTimer.start()
        gamestarted = true
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
        countDownTimer.cancel()
        Log.d(TAG, "Saving score: $score & Time left $timeLeftOnTimer")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_about) {
            showinfo()
        }
        return true
    }

    private fun showinfo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Info")
        builder.setMessage("Hello why are you reading this ?")
        builder.create().show()
    }

    fun resetGame() {
        score = 0
        textView1.text = getString(R.string.your_score, score.toString())

        countDownTimer = object : CountDownTimer(initialCd, cdIndervall) {
            override fun onFinish() {
                endGame()
            }

            override fun onTick(p0: Long) {
                timeLeftOnTimer = p0
                val timeLeft = p0 / 1000
                textView2.text = getString(R.string.your_time, timeLeft.toString())
            }
        }
        gamestarted = false
    }

     fun startGame() {
        countDownTimer.start()
        gamestarted = true
    }

    fun incrementScore() {

        if (!gamestarted) {
            startGame()
        }
        score += 1

        textView1.text = getString(R.string.your_score, score.toString())

        val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.score)
        textView1.startAnimation(blinkAnimation )
    }

     fun endGame() {
        Toast.makeText(this, getString(R.string.game_over, score.toString()) , Toast.LENGTH_SHORT).show()
        resetGame()
    }
}
