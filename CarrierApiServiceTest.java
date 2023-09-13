package com.example.carrier.POC.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.carrier.POC.pojo.label.VoidRequest.ShipmentCancellationRequest;
import com.example.carrier.POC.pojo.label.VoidResponse.ShipmentCancellationResponse;
import com.example.carrier.POC.service.CarrierApiService;

@SpringBootTest
public class CarrierApiServiceTest {

    @Autowired
    private CarrierApiService carrierApiService;

    @MockBean
    private RestTemplate restTemplate;
    
    private static final String FEDEX_API_URL = "https://apis-sandbox.fedex.com/ship/v1/shipments/cancel";

    @BeforeEach
    public void setUp() {
        // Reset any Mockito interactions before each test
        reset(restTemplate);
    }

    @Test
    public void testCancelShipment() {
    	
        // Mocked request and response objects
        ShipmentCancellationRequest request = new ShipmentCancellationRequest();
        request.setTrackingNumber("12345");
        // Set up a mocked response entity
        ShipmentCancellationResponse expectedResponse = new ShipmentCancellationResponse();
        ResponseEntity<ShipmentCancellationResponse> mockResponseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        // Mock the restTemplate.exchange method to return the mocked response entity
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(ShipmentCancellationResponse.class)))
                .thenReturn(mockResponseEntity);

        // Call the service method
        String authToken = "yourAuthToken";
        ShipmentCancellationResponse actualResponse = carrierApiService.cancelShipment(request, authToken);

        // Verify that the restTemplate.exchange method was called with the correct parameters
        verify(restTemplate, times(1)).exchange(
                eq(FEDEX_API_URL),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(ShipmentCancellationResponse.class)
        );

        // Verify that the actual response matches the expected response
        assertEquals(expectedResponse, actualResponse);
    }
}