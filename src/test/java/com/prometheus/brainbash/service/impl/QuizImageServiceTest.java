package com.prometheus.brainbash.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QuizImageServiceTest {

    private static final String TEST_FOLDER = "/mocked/path";

    @InjectMocks
    private QuizImageService quizImageService;

    @BeforeEach
    void setUp() {
        quizImageService = new QuizImageService(TEST_FOLDER);
    }

    @Test
    void testFindAll_ShouldReturnImageFiles() {
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            Stream<Path> mockPaths = Stream.of(
                Path.of("image1.jpg"),
                Path.of("document.pdf"),
                Path.of("photo.png"),
                Path.of("script.js")
            );

            mockedFiles.when(() -> Files.walk(Path.of(TEST_FOLDER))).thenReturn(mockPaths);
            mockedFiles.when(() -> Files.isRegularFile(any(Path.class))).thenReturn(true);

            List<String> result = quizImageService.findAll();

            assertEquals(List.of("image1.jpg", "photo.png"), result);
        }
    }

    @Test
    void testFindAll_ShouldReturnEmptyListOnIOException() {
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.walk(Path.of(TEST_FOLDER))).thenThrow(new IOException("Test Exception"));

            List<String> result = quizImageService.findAll();

            assertEquals(List.of(), result);
        }
    }
}
