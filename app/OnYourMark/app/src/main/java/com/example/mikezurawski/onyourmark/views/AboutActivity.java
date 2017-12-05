package com.example.mikezurawski.onyourmark.views;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mikezurawski.onyourmark.R;


public class AboutActivity extends AppCompatActivity {

    LinearLayout parentView;

    String[] QUESTIONS = {
            "What is 'On Your Mark'?",
            "Who made 'On Your Mark'?"
    };

    String[] ANSWERS = {
            "A revolutionary application that uses the power of MACHINE LEARNING and OPTICAL CHARACTER RECOGNITION to make keeping track of your budget a breeze.",
            "Sohit Pal, Dave Machado, & Mike Zurawski.\nWe are all students at the University of Massachusetts Lowell, making this app for Mobile App Dev 1."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        new HamburgerMenuHandler(this, R.id.toolbar_about, "About").init_subpage(false);

        parentView = (LinearLayout) findViewById(R.id.about_page_layout);

        displayText();
    }

    private TextView createNewQuestionTextView(final String question) {
        TextView questionText = new TextView(this);
        questionText.setText(question);
        questionText.setTypeface(null, Typeface.BOLD);
        questionText.setPadding(0,0,0,5);

        return questionText;
    }

    private TextView createNewAnswerTextView(final String answer) {
        TextView answerText = new TextView(this);
        answerText.setText(answer);
        answerText.setPadding(0,0,0,20);

        return answerText;
    }

    private void displayText() {
        // Just to be clear, I'm not proud of this
        for (int i = 0; i < QUESTIONS.length; i++) {
            parentView.addView(createNewQuestionTextView(QUESTIONS[i]));
            parentView.addView(createNewAnswerTextView(ANSWERS[i]));
        }
    }
}
