package com.example.calculator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import org.mariuszgromada.math.mxparser.*;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.EditText;

 public class MainActivity extends AppCompatActivity {

    private EditText display; // create an instance of EditText class - 'display'

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.input); // assign EditText 'input' to 'display' - can now work with the activity xml EditText 'input' within the Java file
        display.setShowSoftInputOnFocus(false); // prevent default keyboard from popping up


        display.setOnClickListener(new View.OnClickListener() { // detect when 'display' is clicked on
            @Override
            public void onClick(View v) {
                if (getString(R.string.display).equals(display.getText().toString())) { // check if the string stored in 'display' is the prompt text in 'input'
                  display.setText(""); // EditText 'display' (aka 'input') is cleared [when clicked on]
                }
           }
        });
    }

    // updates 'display'
    private void updateText (String strToAdd) { // 'strToAdd' - will be added to 'display'
          String oldStr = display.getText().toString(); // grab text from 'display' and store in 'oldStr'
          int cursorPos = display.getSelectionStart(); // get cursor position
        // split 'oldStr' into 'leftStr' and 'rightStr'
          String leftStr = oldStr.substring(0, cursorPos); // 'leftStr' - from start of 'oldStr' up until cursor position
          String rightStr = oldStr.substring(cursorPos); // 'rightStr' - from cursor position until end of 'oldStr'
          if (getString(R.string.display).equals(display.getText().toString())) { // check if strings.xml resource 'display' is the same as the instance 'display' - if original prompt is still there
              display.setText(strToAdd); // add strToAdd to 'display' (no oldStr to break up - original prompt still in 'display')
              display.setSelection(cursorPos + 1); // keep cursor to the right of the last input value
          } else { // update display to include strToAdd according to where cursor position is
              display.setText(String.format("%s%s%s", leftStr, strToAdd, rightStr));
              display.setSelection(cursorPos + 1); // keep cursor to the right of the last input value
          }
    }

    // methods to make buttons actionable
    //  updateText ( strToAdd: ""); - when press button, display is updated to include button's attribute wherever cursor is placed
    public void zeroBTN (View view) {
         updateText ("O");
    }

    public void oneBTN (View view) {
         updateText ("1");
    }

    public void twoBTN (View view) {
       updateText ("2");
    }

    public void threeBTN (View view) {
       updateText ("3");
    }

    public void fourBTN (View view) {
       updateText ("4");
    }

    public void fiveBTN (View view) {
       updateText ("5");
    }

    public void sixBTN (View view) {
       updateText ("6");
    }

    public void sevenBTN (View view) {
       updateText ("7");
    }

    public void eightBTN (View view) {
       updateText ("8");
    }

    public void nineBTN (View view) {
       updateText ("9");
    }

    public void multiplyBTN (View view) {
       updateText ("✕");
    }

    public void divideBTN (View view) {
       updateText ("÷");
    }

    public void subtractBTN (View view) {
       updateText ("-");
    }

    public void addBTN (View view) {
       updateText ("+");
    }

    public void clearBTN (View view) {
        display.setText(""); // clear the display text
    }

    public void parBTN (View view) {
        int cursorPos = display.getSelectionStart(); // get cursor position
        int openPar = 0;
        int closePar = 0;
        int textLen = display.getText().length(); // get length of text in 'display'

        for (int i = 0; i < cursorPos; i++) { // run the loop while i is less than the cursor position since we want to see how many open and close parentheses there are to the left of the cursor
            if (display.getText().toString().substring(i, i + 1).equals("(")) { // checks if text in position 'i' in 'display' has an open parentheses
                openPar++; // increments count of open parentheses currently in 'display'
            }

            if (display.getText().toString().substring(i, i + 1).equals(")")) { // checks if text in position 'i' in 'display' has a close parentheses
                closePar++; // increments count of close parentheses currently in 'display'
            }
        }

        if (openPar == closePar || display.getText().toString().substring(textLen - 1, textLen).equals("(")) { // if there is an equal amount of open and close parentheses or if the last character in 'display' is an open parentheses (because don't want to close parentheses with nothing inside)
            updateText("("); // add open parentheses
        } else if (closePar < openPar && !(display.getText().toString().substring(textLen - 1, textLen).equals("("))) { // want to close parentheses only if both the number of close parentheses is less than the number of open parentheses and if the last character in 'display' is NOT an oepn parentheses
            updateText(")"); // add close parentheses
        }

        display.setSelection(cursorPos + 1); // move cursor over to the right since a character - close parentheses - was added
    }

    public void expBTN (View view) {
       updateText ("^");
    }

    public void plusMinusBTN (View view) {
       updateText ("-");
    }

    public void decimalBTN (View view) {
       updateText (".");
    }

    public void equalBTN (View view) {
        String userExp = display.getText().toString();

        userExp = userExp.replaceAll("+", "/"); // in order for mxParser to be able to read, evaluate the user's expression
        userExp = userExp.replaceAll("✕", "*"); // in order for mxParser to be able to read, evaluate the user's expression

        Expression exp = new Expression (userExp); // create 'exp' in Expression class that is the value of the user's expression, makes expression readable by mxParser

        String result = String.valueOf(exp.calculate()); // create a string 'result' of the value of the result of the user's mathematical expression

        display.setText(result); // displays calculated result
        display.setSelection(result.length()); // updates cursor's position to the end of the result
    }

    public void backspaceBTN (View view) {
        int cursorPos = display.getSelectionStart(); // grab current cursor position
        int textLen = display.getText().length(); // store length of 'display' in textLen - will use to ensure that user cannot backspace when 'display' is empty to avoid an index error

        if (cursorPos != 0 && textLen != 0) { // ensures that the backspace button will not work if the cursor position and text length are both greater than zero to avoid index error
            SpannableStringBuilder selection = (SpannableStringBuilder) display.getText(); // SpannableStringBuilder allows one to replace different characters within the string
            selection.replace(cursorPos - 1, cursorPos, ""); // deletes character by replacing it with an empty string
            display.setText(selection); // updates 'display' to the text without the deleted character (the character that was replaced with an empty string in 'selection')
            display.setSelection(cursorPos - 1); // update cursor position to one to the left since we deleted a character (so -1 and not +1)
        }
    }
}