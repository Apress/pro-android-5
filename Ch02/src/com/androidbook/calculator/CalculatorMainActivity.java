package com.androidbook.calculator;

import com.androidbook.calculator.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * Activity name: CalculatorMainActivity
 * Layout file: calculator_layout.xml
 * Layout shortcut prefix for ids: cl_
 * Menu file: none
 * Purpose and Logic
 * ******************
 * 1. Demonstrate business logic for a simple calculator
 * 2. Load the calculator_layout.xml as layout
 * 3. Setup button callbacks
 * 4. Respond to button clicks
 * 5. Read values from edit text controls
 * 6. Perform operation and update result edit control
 */
public class CalculatorMainActivity extends Activity 
implements OnClickListener
{
	private EditText number1EditText;
	private EditText number2EditText;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_layout);
        gatherControls();
        setupButtons();
    }
    private void gatherControls()   {
    	number1EditText = (EditText)this.findViewById(R.id.editText1);
    	number2EditText = (EditText)this.findViewById(R.id.editText2);
    	number2EditText.requestFocus();
    }
    private void setupButtons()    {
    	Button b = (Button)this.findViewById(R.id.plusButton);
    	b.setOnClickListener(this);

    	b = (Button)this.findViewById(R.id.minusButton);
    	b.setOnClickListener(this);
    	
    	b = (Button)this.findViewById(R.id.multiplyButton);
    	b.setOnClickListener(this);
    	
    	b = (Button)this.findViewById(R.id.divideButton);
    	b.setOnClickListener(this);
    }
	@Override
	public void onClick(View v) 	{
		String sNum1 = number1EditText.getText().toString();
		String sNum2 = number2EditText.getText().toString();
		double num1 = getDouble(sNum1);
		double num2 = getDouble(sNum2);
		Button b = (Button)v;
		
		double value = 0; 
		if (b.getId() == R.id.plusButton)
		{
			value = plus(num1, num2);
		}
		else if (b.getId() == R.id.minusButton)
		{
			value = minus(num1, num2);
		}
		else if (b.getId() == R.id.multiplyButton)
		{
			value = multiply(num1, num2);
		}
		else if (b.getId() == R.id.divideButton)
		{
			value = divide(num1, num2);
		}
		number1EditText.setText(Double.toString(value));
	}
	
	private double plus(double n1, double n2)	{
		return n1 + n2;
	}
	private double minus(double n1, double n2)	{
		return n1 - n2;
	}
	private double multiply(double n1, double n2)	{
		return n1 * n2;
	}
	private double divide(double n1, double n2)	{
		if (n2 == 0)	{
			return 0;
		}
		return n1 / n2;
	}
	private double getDouble(String s)	{
		if (validString(s))		{
			return Double.parseDouble(s);
		}
		return 0;
	}
    private boolean invalidString(String s)    {
    	return !validString(s);
    }
    private boolean validString(String s)    {
    	if (s == null) 	{
    		return false;
    	}
    	if (s.trim().equalsIgnoreCase(""))	{
    		return false;
    	}
    	return true;
    }
}