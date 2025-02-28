package com.bilgeadam.enterprise.dto.rtc;

public record WebRtcOfferMessage(
		//WEBRTCOFFERMESSAGE : Bir RTC bağlantısında, görüşmeyi başlatmak isteyen tarafın (örneğin, çağrıyı başlatan
		// kullanıcı) ilk adım olarak oluşturduğu mesajdır.
		String senderUserId,
		String targetUserId,
		String sdp, //Session Description Protocol
		//SDP, medya oturumunun özelliklerini (kullanılacak codec'ler, medya akış bilgileri, bağlantı detayları vs.) içerir.
		String callType
) {
}