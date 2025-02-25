package com.bilgeadam.utility;

import com.bilgeadam.entity.PricingPlan;
import com.bilgeadam.entity.enums.SubscriptionPlan;
import com.bilgeadam.repository.PricingPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GenerateSubscriptionPlanInitializer implements CommandLineRunner {
	private final PricingPlanRepository pricingPlanRepository;
	
	@Override
	public void run(String... args) throws Exception {
		if (pricingPlanRepository.count() == 0) {
			List<PricingPlan> plans = List.of(
					PricingPlan.builder()
					                .title("Basic Plan")
					                 .price(39.99)
					           .features(List.of("7/24 Müşteri Desteği", "Yerinde Eğitim ve Atölyeler", "Tam Kapsamlı İşe Alım", "Özelleştirilmiş Yan Haklar Programları"))
					                .build(),
					PricingPlan.builder()
					                .title("Enterprise Plan")
					                .price(79.99)
					           .features(List.of("7/24 Müşteri Desteği", "Yerinde Eğitim ve Atölyeler", "Tam Kapsamlı İşe Alım", "Özelleştirilmiş Yan Haklar Programları", "Hukuki Destek", "Liderlik Eğitim Programları"))
					                .build(),
					PricingPlan.builder()
					                .title("Pro Plan")
					                .price(59.99)
					           .features(List.of("7/24 Müşteri Desteği", "Yerinde Eğitim ve Atölyeler", "Tam Kapsamlı İşe Alım", "Özelleştirilmiş Yan Haklar Programları", "Hukuki Destek"))
					                .build()
			);
			pricingPlanRepository.saveAll(plans);
		}
	}
}