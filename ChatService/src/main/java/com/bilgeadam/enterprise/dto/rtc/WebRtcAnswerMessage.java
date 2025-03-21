package com.bilgeadam.enterprise.dto.rtc;

public record WebRtcAnswerMessage(
		//WEBRTCANSWERMESSAGE : Gelen offer mesajına cevap veren mesajdır.
		// Çağrıyı alan taraf, kendisine gelen SDP teklifini aldıktan sonra kendi SDP bilgisini oluşturur.
		String senderUserId,
		String targetUserId,
		String sdp
) {
}