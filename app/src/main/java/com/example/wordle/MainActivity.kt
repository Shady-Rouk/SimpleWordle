package com.example.wordle

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private var guessCount = 0

    /**
     * Parameters / Fields:
     *   wordToGuess : String - the target word the user is trying to guess
     *   guess : String - what the user entered as their guess
     *
     * Returns a String of 'O', '+', and 'X', where:
     *   'O' represents the right letter in the right place
     *   '+' represents the right letter in the wrong place
     *   'X' represents a letter not in the target word
     */
    private fun checkGuess(guess: String, wordToGuess: String) : String {
        var result = ""
        for (i in 0..3) {
            if (guess[i] == wordToGuess[i]) {
                result += "O"
            }
            else if (guess[i] in wordToGuess) {
                result += "+"
            }
            else {
                result += "X"
            }
        }
        return result
    }

    private fun closeKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val wordSource = FourLetterWordList
        val guessButton = findViewById<Button>(R.id.guessButton)
        val resetButton = findViewById<Button>(R.id.resetButton)

        // textViews
        val userGuess1 = findViewById<TextView>(R.id.guess1)
        val userGuess1Eval = findViewById<TextView>(R.id.guess1Eval)
        val userGuess2 = findViewById<TextView>(R.id.guess2)
        val userGuess2Eval = findViewById<TextView>(R.id.guess2Eval)
        val userGuess3 = findViewById<TextView>(R.id.guess3)
        val userGuess3Eval = findViewById<TextView>(R.id.guess3Eval)
        val correctWordView = findViewById<TextView>(R.id.correctWord)
        val userGuess = findViewById<EditText>(R.id.userGuess)

        var correctWord = wordSource.getRandomFourLetterWord()
        guessButton.setOnClickListener {
            val userGuessText = userGuess.text
            userGuess.setText("")     // clearing the text field
            closeKeyboard(userGuess)  // hiding the keyboard
            val userGuessEval = checkGuess(userGuessText.toString().uppercase(), correctWord)

            guessCount++

            if (guessCount == 1) {
                userGuess1.text = userGuessText.toString().uppercase()
                userGuess1Eval.text = userGuessEval
            } else if (guessCount == 2) {
                userGuess2.text = userGuessText.toString().uppercase()
                userGuess2Eval.text = userGuessEval
            } else if (guessCount == 3) {
                userGuess3.text = userGuessText.toString().uppercase()
                userGuess3Eval.text = userGuessEval
            }

            if (userGuessText.toString().uppercase() == correctWord) {  // user guessed correctly
                // show toast that user won
                val toast = Toast.makeText(this, getString(R.string.winMessage), Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 10)
                toast.show()
                // show correct answer
                correctWordView.text = correctWord
                correctWordView.visibility = View.VISIBLE
                // disable submit button and text input field
                guessButton.isEnabled = false
                userGuess.isEnabled = false
                resetButton.visibility = View.VISIBLE
            } else if (guessCount == 3){  // user did not guess correctly on last try
                // show correct answer
                correctWordView.text = correctWord
                correctWordView.visibility = View.VISIBLE
                // disable submit button and text input field
                guessButton.isEnabled = false
                userGuess.isEnabled = false
                resetButton.visibility = View.VISIBLE
            }
        }

        resetButton.setOnClickListener {
            resetButton.visibility = View.INVISIBLE
            guessCount = 0
            correctWord = wordSource.getRandomFourLetterWord()

            userGuess1.text = ""
            userGuess1Eval.text = ""
            userGuess2.text = ""
            userGuess2Eval.text = ""
            userGuess3.text = ""
            userGuess3Eval.text = ""

            guessButton.isEnabled = true
            userGuess.isEnabled = true
            correctWordView.visibility = View.INVISIBLE
        }

    }
}