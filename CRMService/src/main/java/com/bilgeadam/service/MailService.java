package com.bilgeadam.service;

import com.bilgeadam.entity.enums.TicketStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
	
	private final JavaMailSender mailSender;
	
	/** 📌 Genel e-posta gönderme metodu */
	public void sendEmail(String toEmail, String subject, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("destek@enterprise.com");
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(content);
		mailSender.send(message);
	}
	
	public void sendFeedbackReceivedEmail(String toEmail) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("destek@enterprise.com"); // **Destek ekibi e-posta adresi**
		message.setTo(toEmail); // **Müşterinin e-posta adresi**
		message.setSubject("Geri Bildiriminiz Alındı");
		message.setText("Merhaba, geri bildiriminiz için teşekkür eder. İyi günler dileriz!");
		mailSender.send(message);
	}
	
	public void sendFeedbackToSupport(String userEmail, String subject, String messageContent) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo("destek@enterprise.com"); // **Destek ekibi e-posta adresi**
		message.setFrom(userEmail); // **Müşterinin e-posta adresi**
		message.setSubject(subject);
		message.setText(messageContent);
		mailSender.send(message);
	}
	
	/** 📌 Müşteriye Ticket Oluşturuldu Bilgilendirmesi Gönder */
	public String sendTicketCreationEmail(String ticketNumber, String subject) {
		return """
                    Merhaba,

                    Göndermiş olduğunuz mesajı aldık ve destek kaydınızı oluşturduk.
                    
                    📌 **Ticket Konusu:** %s
                    🆔 **Ticket Numarası:** %s

                    İlgili birimlerimize iletildi ve sorununuz en kısa sürede çözüme ulaştırılacaktır.
                    Gelişmeleri sizinle paylaşacağız.

                    Destek ekibimizle iletişimde kalmak için bu e-postaya yanıt verebilirsiniz.

                    **İyi günler dileriz!**
                    **Enterprise Destek Ekibi**
                    """.formatted(subject, ticketNumber);
	}
	
	/** 📌 Ticket çözüldüğünde/kapatıldığında müşteriye bilgilendirme gönder */
	public String sendTicketResolvedEmail(String toEmail, String ticketNumber, String subject, TicketStatus status) {
		return """
                    Merhaba,

                    📌 **Ticket Konusu:** %s
                    🆔 **Ticket Numarası:** %s
                    
                    Destek talebiniz **%s** olarak işaretlenmiştir.
                    
                    Eğer sorununuz tamamen çözülmediyse veya ek bir yardıma ihtiyacınız varsa, lütfen bu e-postaya yanıt vererek bizimle iletişime geçin.
                    
                    **İyi günler dileriz!**
                    **Enterprise Destek Ekibi**
                    """.formatted(subject, ticketNumber, status.getDescription());
//			sendEmail(toEmail, "🎉 Destek Kaydınız " + (status == TicketStatus.RESOLVED ? "Çözüldü" : "Kapatıldı") + " - #" + ticketNumber, messageContent);
	}
	
	/** 📌 Destek ekibine müşterinin mesajını içeren e-posta gönderir */
	public void sendSupportTeamNotificationEmail(String senderEmail, String recipientEmail, String subject,
	                                             String messageContent) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(senderEmail);
			message.setTo(recipientEmail);
			message.setSubject("📩 Yeni Destek Talebi - " + subject);
			message.setText("""
                    Merhaba Destek Ekibi,

                    Yeni bir müşteri mesajı aldınız.

                    **📌 Gönderen:** %s
                    **📌 Konu:** %s

                    **📌 Mesaj İçeriği:**
                    %s

                    Lütfen bu mesajı inceleyerek uygun bir **Ticket oluşturun**.

                    **İyi çalışmalar!**
                    **%s Destek Ekibi**
                    """.formatted(senderEmail, subject, messageContent, "Enterprise"));
			
			mailSender.send(message);
		} catch (Exception e) {
			System.err.println("⚠️ [Uyarı] Destek ekibine e-posta gönderilemedi: " + e.getMessage());
		}
	}
	
	/** 📌 Müşteriye mesajın alındığını bildiren e-posta gönderir */
	public String  sendTicketMessageReceivedEmail(String customerEmail, String subject, String customerMessage) {
		String autoReplyMessage = """
                Merhaba,

                Mesajınızı aldık ve en kısa sürede sizinle iletişime geçeceğiz.

                **📌 Gönderdiğiniz Mesaj:**
                %s

                Eğer ek bilgi vermek isterseniz bu e-postaya yanıt verebilirsiniz.

                **İyi günler dileriz!**
                **Enterprise Destek Ekibi**
                """.formatted(customerMessage);
		
		sendEmail(customerEmail, "📩 Destek Talebiniz Alındı - " + subject, autoReplyMessage);
		return autoReplyMessage; // TicketMessage tablosuna kaydetmek için geri dönüyoruz
	}
}