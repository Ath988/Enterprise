package com.bilgeadam.enterprise.dto.rtc;

public record WebRtcIceCandidate(
		//Peer-to-peer (P2P) bağlantıda, iki tarafın ağ üzerinde birbirine bağlanmasını sağlayacak olası
		// network bilgilerini paylaşan mesaj türüdür.public
		String senderUserId,
		String targetUserId,
		String candidate //ICE (Interactive Connectivity Establishment) adayı;
		// ağdaki IP adresleri, portlar ve protokol bilgilerini içerir.
		
		//Bu adaylar, NAT traversing ve firewall'ların arkasında bulunan istemciler arasında uygun ağ yollarının
		// keşfedilmesi için kullanılır. Her iki taraf da adayları ekleyerek doğrudan bağlantı kurmaya çalışır.
) {
}