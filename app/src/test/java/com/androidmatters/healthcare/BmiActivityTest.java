package com.androidmatters.healthcare;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
public class BmiActivityTest {

    private BmiActivity bmiActivity;

    @Before
    public  void setUp(){
        bmiActivity = new BmiActivity();
    }

    /**IT19966922*/
    @Test
    public void bmi_isCorrect_1() {
        float result = bmiActivity.calculateBmi(6.0f,50.0f);
        assertEquals(1.388888, result);
    }

    /**IT19966618*/
    @Test
    public void bmi_isCorrect_2() {
        float result = bmiActivity.calculateBmi(4.5f,40.0f);
        assertEquals(1.9753086, result);
    }

    /**IT19962580*/
    @Test
    public void bmi_isCorrect_3() {
        float result = bmiActivity.calculateBmi(6.5f,65.5f);
        assertEquals(1.550295, result);
    }

    /**IT19974774*/
    @Test
    public void bmi_isCorrect_4() {
        float result = bmiActivity.calculateBmi(4.0f,35.6f);
        assertEquals(2.225000, result);
    }

}
