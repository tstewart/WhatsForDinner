package io.github.tstewart.whatsfordinner;

import android.content.Context;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import io.github.tstewart.CalorieLookup.APIRequest;
import io.github.tstewart.CalorieLookup.edamam.EdamamConnection;
import io.github.tstewart.CalorieLookup.request.FoodRequest;
import io.github.tstewart.whatsfordinner.async.RequestAsync;

@RunWith(AndroidJUnit4.class)
public class RequestTest {
    @Test
    public void validRequestSuccessful() {
        //Arrange
        FoodRequest foodRequest = new FoodRequest("apple");
        APIRequest apiRequest = new APIRequest(foodRequest);
        EdamamConnection edamamConnection = new EdamamConnection("da5ccccd", "830de1530183470f64e9ef0f6352421e");

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        //Act / Assert
        new RequestAsync(appContext, Assert::assertNotNull);
    }

    @Test
    public void invalidRequestDoesNotCrash() {
        //Arrange
        FoodRequest foodRequest = new FoodRequest("apple");
        APIRequest apiRequest = new APIRequest(foodRequest);
        EdamamConnection edamamConnection = new EdamamConnection("invalid", "invalid");

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        //Act / Assert
        new RequestAsync(appContext, Assert::assertNotNull);
    }
}
