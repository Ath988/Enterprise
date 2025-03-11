package com.bilgeadam.utility;

import com.bilgeadam.entity.ServiceEntity;
import com.bilgeadam.repository.ServiceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GenerateServiceEntityInitializer implements CommandLineRunner {
	
	private final ServiceEntityRepository serviceEntityRepository;
	
	@Override
	public void run(String... args) {
		if (serviceEntityRepository.count() == 0) {  // Eğer veritabanında veri yoksa mock data ekle
			List<ServiceEntity> serviceCards = createMockServiceEntities();
			serviceEntityRepository.saveAll(serviceCards);
		}
	}
	
	/**
	 * Mock ServiceEntity verilerini oluşturan metot
	 */
	private List<ServiceEntity> createMockServiceEntities() {
		return List.of(
				ServiceEntity.builder()
				             .title("Kullanıcı ve Destek Yönetimi")
				             .descriptions(List.of(
						             "Müşteri ilişkilerinizi kolayca yönetin.",
						             "Kişi ve firma bilgilerini merkezi bir sistemde tutun.",
						             "Satış süreçlerini izleyin ve yönetim araçlarıyla optimize edin.",
						             "E-posta entegrasyonu ile hızlı iletişim kurun.",
						             "Müşteri taleplerini takip edin ve raporlarla analiz edin."
				             ))
				             .build(),
				ServiceEntity.builder()
				             .title("Envanter Yönetimi")
				             .descriptions(List.of(
						             "Stok seviyelerini anlık olarak izleyin.",
						             "Ürünleri envanterinize ekleyin ve yönetin.",
						             "Envanter analizleri ile stok ihtiyaçlarını önceden tahmin edin.",
						             "Sipariş ve tedarik süreçlerini kolayca yönetin.",
						             "Verimli depo yönetimi ile maliyetleri azaltın."
				             ))
				             .build(),
				ServiceEntity.builder()
				             .title("Takvim Ve Planlama")
				             .descriptions(List.of(
						             "Ekip takvimlerini ortak bir platformda yönetin.",
						             "Toplantı ve etkinlikleri kolayca planlayın.",
						             "Zaman yönetimini optimize edin ve hatırlatıcılarla unutmayın.",
						             "Ekipler arası koordinasyonu artırın.",
						             "İş takvimleri ve kişisel takvim entegrasyonları."
				             ))
				             .build(),
				ServiceEntity.builder()
				             .title("Finans Ve Muhasebe Yönetimi")
				             .descriptions(List.of(
						             "Gelir ve giderlerinizi etkili bir şekilde takip edin.",
						             "Faturalarınızı düzenli olarak oluşturun ve yönetin.",
						             "Bütçeleme ve raporlama araçlarıyla finansal kararlar alın.",
						             "Vergi süreçlerini dijitalleştirin ve kolayca yönetin.",
						             "Finansal verilerinizi güvenli bir ortamda saklayın ve paylaşın."
				             ))
				             .build(),
				ServiceEntity.builder()
				             .title("Organizasyon Yönetimi")
				             .descriptions(List.of(
						             "Organizasyon şeması ile yapıyı düzenleyin ve yönetin.",
						             "Departmanlar arası verimli iletişim sağlayın.",
						             "Çalışan bilgilerini ve performansını takip edin.",
						             "İş gücü yönetimi ve kaynak planlaması yapın.",
						             "Ekip içi işbirliğini artırarak verimliliği yükseltin."
				             ))
				             .build(),
				ServiceEntity.builder()
				             .title("Proje Yönetimi")
				             .descriptions(List.of(
						             "Projeleri başlatın, izleyin ve zamanında tamamlayın.",
						             "Görevleri ve süreleri belirleyerek etkili bir planlama yapın.",
						             "Ekiplerle proje detayları hakkında anlık bilgi paylaşın.",
						             "Proje ilerlemelerini takip edin ve raporlarla sunun.",
						             "Kaynakları verimli kullanarak bütçeyi kontrol altında tutun."
				             ))
				             .build()
		);
	}
}