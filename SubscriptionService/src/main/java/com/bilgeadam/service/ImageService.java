package com.bilgeadam.service;

import com.bilgeadam.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {
	private final ImageRepository imageRepository;
}