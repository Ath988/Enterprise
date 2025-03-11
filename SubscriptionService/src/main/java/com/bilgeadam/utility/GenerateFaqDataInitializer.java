package com.bilgeadam.utility;

import com.bilgeadam.entity.Faq;
import com.bilgeadam.entity.AnswerEntity;
import com.bilgeadam.repository.FaqRepository;
import com.bilgeadam.repository.AnswerEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GenerateFaqDataInitializer implements CommandLineRunner {
	
	private final FaqRepository faqRepository;
	private final AnswerEntityRepository answerEntityRepository;
	
	@Override
	public void run(String... args) {
		if (faqRepository.count() == 0 && answerEntityRepository.count() == 0) {
			List<Faq> faqList = createMockFaqs();
			faqRepository.saveAll(faqList);
			
			List<AnswerEntity> answerList = createMockAnswers(faqList);
			answerEntityRepository.saveAll(answerList);
		}
	}
	
	/**
	 * Mock Faq verilerini oluşturan metot
	 */
	private List<Faq> createMockFaqs() {
		return List.of(
				Faq.builder()
				   .question("Hangi servisleri sunuyorsunuz?")
				   .build(),
				Faq.builder()
				   .question("Servislerin ücretleri nedir?")
				   .build(),
				Faq.builder()
				   .question("Enterprise paketinin avantajları nelerdir?")
				   .build()
		);
	}
	
	/**
	 * Mock AnswerEntity verilerini oluşturan metot
	 * @param faqList Kaydedilmiş Faq listesi
	 */
	private List<AnswerEntity> createMockAnswers(List<Faq> faqList) {
		return List.of(
				AnswerEntity.builder()
				            .faqId(faqList.get(0).getId())
				            .answer("CRM, Envanter Yönetimi, Finans ve daha fazlası.")
				            .build(),
				AnswerEntity.builder()
				            .faqId(faqList.get(1).getId())
				            .answer("Farklı paketlere göre ücretlendirme yapılmaktadır.")
				            .build(),
				AnswerEntity.builder()
				            .faqId(faqList.get(2).getId())
				            .answer("Gelişmiş raporlama, API entegrasyonları, özel destek ve daha fazlası.")
				            .build()
		);
	}
}