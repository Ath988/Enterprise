package com.bilgeadam.utility;

import com.bilgeadam.entity.FileInfo;
import com.bilgeadam.repository.FileInfoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeneratePhotoInitializer implements CommandLineRunner {
	private final FileInfoRepository fileInfoRepository;
	
	public GeneratePhotoInitializer(FileInfoRepository fileInfoRepository) {
		this.fileInfoRepository = fileInfoRepository;
	}
	
	@Override
	public void run(String... args) throws Exception {
		if (fileInfoRepository.count() == 0) {
			List<FileInfo> photos = List.of(
					FileInfo.builder()
					     .fileName("Sunset Over the Mountains")
					     .url("https://example.com/photos/sunset.jpg")
					     .build(),
					FileInfo.builder()
					     .fileName("City Skyline at Night")
					     .url("https://example.com/photos/city_skyline.jpg")
					     .build(),
					FileInfo.builder()
					     .fileName("Forest Pathway")
					     .url("https://example.com/photos/forest_pathway.jpg")
					     .build(),
					FileInfo.builder()
					     .fileName("Ocean Waves")
					     .url("https://example.com/photos/ocean_waves.jpg")
					     .build(),
					FileInfo.builder()
					     .fileName("Snowy Mountain Peaks")
					     .url("https://example.com/photos/snowy_peaks.jpg")
					     .build()
			);
			fileInfoRepository.saveAll(photos);
		}
	}
}