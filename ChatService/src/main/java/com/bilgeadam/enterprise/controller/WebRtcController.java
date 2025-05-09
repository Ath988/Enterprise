package com.bilgeadam.enterprise.controller;

import com.bilgeadam.enterprise.dto.rtc.WebRtcAnswerMessage;
import com.bilgeadam.enterprise.dto.rtc.WebRtcIceCandidate;
import com.bilgeadam.enterprise.dto.rtc.WebRtcOfferMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebRtcController {
	
	private final SimpMessagingTemplate messagingTemplate;
	
	public WebRtcController(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}
	
	@MessageMapping("/webrtc/offer")
	public void handleOffer(@Payload WebRtcOfferMessage offerMessage) {

		System.out.println("offer metoda girdi????");
		System.out.println("Offer message: " + offerMessage);
		messagingTemplate.convertAndSend("/topic/webrtc/offer/" + offerMessage.targetUserId(), offerMessage);
	}
	

	@MessageMapping("/webrtc/answer")
	public void handleAnswer(@Payload WebRtcAnswerMessage answerMessage) {
		System.out.println("answer metoda girdi????");
		messagingTemplate.convertAndSend("/topic/webrtc/answer/" + answerMessage.targetUserId(), answerMessage);
	}
	

	@MessageMapping("/webrtc/ice")
	public void handleIceCandidate(@Payload WebRtcIceCandidate iceCandidate) {
		System.out.println("ice metoda girdi????");
		messagingTemplate.convertAndSend("/topic/webrtc/ice/" + iceCandidate.targetUserId(), iceCandidate);
	}
}