package com.example.onlinevotingsystem;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.onlinevotingsystem.util.ReusedMethods;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DecisionMakerCreatesOptionsTest {

    @Test
    public void regexOptionsValidity() {
        // Test values
        String[] optionValues = {"red, purple","Green, yellow and green","low","25, green", "^%$*"};
        // Correct answer
        Boolean[] expectedValue = {true, true, false, true, false};

        // Retrieved answer
        Boolean[] result = new Boolean[optionValues.length];
        for(int i = 0; i < optionValues.length; i++){
            result[i] = ReusedMethods.optionIsValid(optionValues[i]);
        }

        // Check answers match
        assertArrayEquals(expectedValue, result);
    }
}