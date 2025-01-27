package com.kulebiakin.coffeeshops.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CustomErrorControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private Model model;

    @InjectMocks
    private CustomErrorController customErrorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleErrorWith404Status() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(404);
        when(request.getAttribute(RequestDispatcher.ERROR_MESSAGE)).thenReturn("Not Found");

        String viewName = customErrorController.handleError(request, model);

        assertEquals("error-404", viewName);
    }

    @Test
    void handleErrorWith500Status() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(500);
        when(request.getAttribute(RequestDispatcher.ERROR_MESSAGE)).thenReturn("Internal Server Error");

        String viewName = customErrorController.handleError(request, model);

        assertEquals("error-500", viewName);
    }

    @Test
    void handleErrorWithUnknownStatus() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(400);
        when(request.getAttribute(RequestDispatcher.ERROR_MESSAGE)).thenReturn("Bad Request");

        String viewName = customErrorController.handleError(request, model);

        assertEquals("error", viewName);
    }

    @Test
    void handleErrorWithNullStatus() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(null);

        String viewName = customErrorController.handleError(request, model);

        assertEquals("error", viewName);
    }
}
