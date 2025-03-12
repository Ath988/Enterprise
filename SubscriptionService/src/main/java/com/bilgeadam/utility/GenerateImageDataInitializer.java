package com.bilgeadam.utility;



import com.bilgeadam.entity.Image;
import com.bilgeadam.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GenerateImageDataInitializer implements CommandLineRunner {
	
	private final ImageRepository imageRepository;
	
	@Override
	public void run(String... args) {
		if (imageRepository.count() == 0) {
			List<Image> images = List.of(
					Image.builder().fileUrl("https://picsum.photos/1200/800?random=1").build(),
					Image.builder().fileUrl("https://picsum.photos/1200/800?random=2").build(),
					Image.builder().fileUrl("https://picsum.photos/1200/800?random=3").build(),
					Image.builder().fileUrl("https://picsum.photos/1200/800?random=4").build(),
					Image.builder().fileUrl("https://picsum.photos/1200/800?random=5").build(),
					Image.builder().fileUrl("https://picsum.photos/1200/800?random=6").build()
			);
			imageRepository.saveAll(images);
		}
	}
}