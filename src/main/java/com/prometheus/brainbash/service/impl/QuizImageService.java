package com.prometheus.brainbash.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.prometheus.brainbash.service.IQuizImageService;

@Service
public class QuizImageService implements IQuizImageService {
	private final String quizImagesFolder;
	
	public QuizImageService(@Value("${quiz.images.folder}") String quizImagesFolder) {
		this.quizImagesFolder = quizImagesFolder;
	}

	@Override
	public List<String> findAll() {
		try (Stream<Path> paths = Files.walk(Paths.get(quizImagesFolder))) {
			return paths.filter(Files::isRegularFile)
						.map(Path::getFileName)
						.map(Path::toString)
						.filter(name -> name.matches(".*\\.(jpg|jpeg|png|gif|bmp|webp)$"))
						.toList();
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
}
