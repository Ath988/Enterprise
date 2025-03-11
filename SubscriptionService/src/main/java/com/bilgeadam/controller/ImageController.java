package com.bilgeadam.controller;

import com.bilgeadam.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bilgeadam.constant.RestApis.IMAGE;

@RestController
@RequiredArgsConstructor
@RequestMapping(IMAGE)
@CrossOrigin("*")
public class ImageController {
	private final ImageService imageService;
}