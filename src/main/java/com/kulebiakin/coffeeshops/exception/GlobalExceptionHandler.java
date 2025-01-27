package com.kulebiakin.coffeeshops.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneralException(Exception ex) {
        log.error("Unhandled Exception occurred: ", ex);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", "Что-то пошло не так. Попробуйте позже.");
        modelAndView.addObject("error", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Unhandled exception Resource not found: ", ex);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.addObject("error", "Ресурс не найден.");
        return modelAndView;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Unhandled exception Illegal argument: ", ex);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", "Некорректный запрос.");
        modelAndView.addObject("error", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ModelAndView handleNoSuchElementException(NoSuchElementException ex) {
        log.error("Unhandled exception No such element: ", ex);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", "Элемент не найден.");
        modelAndView.addObject("error", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(IOException.class)
    public ModelAndView handleIOException(IOException ex) {
        log.error("Unhandled exception IO exception: ", ex);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", "Произошла ошибка ввода-вывода.");
        modelAndView.addObject("error", ex.getMessage());
        return modelAndView;
    }
}
