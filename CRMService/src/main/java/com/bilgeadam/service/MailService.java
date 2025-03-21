package com.bilgeadam.service;

import com.bilgeadam.entity.enums.TicketStatus;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
	
	private void offerSendEmail(String to, String subject, String body) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom("destek@enterprise.com");
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true); // HTML olarak gönder
			mailSender.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException("⚠️ E-posta gönderilemedi", e);
		}
	}
	
	/** 📌 Müşteriye teklif e-postası gönder */
	public void sendOfferEmail(Long offerId, String email, String title) {
		String subject = "📌 Teklifiniz: " + title;
		String acceptUrl = "http://localhost:8083/v1/dev/offer/accept-offer/" + offerId;
		String rejectUrl = "http://localhost:8083/v1/dev/offer/reject-offer/" + offerId;
		
		String message = """
                <h3>Merhaba,</h3>
                <p>Size yeni bir teklif sunuldu: <b>%s</b></p>
                <p>Bu teklifi kabul etmek veya reddetmek için aşağıdaki butonları kullanabilirsiniz:</p>
                <a href="%s" style="display:inline-block;padding:10px 20px;color:white;background-color:green;text-decoration:none;">✅ Kabul Et</a>
                <a href="%s" style="display:inline-block;padding:10px 20px;color:white;background-color:red;text-decoration:none;">❌ Reddet</a>
                """.formatted(title, acceptUrl, rejectUrl);
		
		offerSendEmail(email, subject, message);
	}
	
	public void sendOfferReactivationEmail(Long offerId, String email, String title) {
		String subject = "📌 Teklifiniz Yeniden Aktif Edildi: " + title;
		String acceptUrl = "http://localhost:8083/v1/dev/offer/accept-offer/" + offerId;
		String rejectUrl = "http://localhost:8083/v1/dev/offer/reject-offer/" + offerId;
		
		String message = """
            <h3>Merhaba,</h3>
            <p>Teklifiniz tekrar aktif hale getirildi: <b>%s</b></p>
            <p>Bu teklifi kabul etmek veya reddetmek için aşağıdaki butonları kullanabilirsiniz:</p>
            <a href="%s" style="display:inline-block;padding:10px 20px;color:white;background-color:green;text-decoration:none;">✅ Kabul Et</a>
            <a href="%s" style="display:inline-block;padding:10px 20px;color:white;background-color:red;text-decoration:none;">❌ Reddet</a>
            """.formatted(title, acceptUrl, rejectUrl);
		
		offerSendEmail(email, subject, message);
	}
	
	/** 📌 Teklif kabul edildiğinde müşteriye bilgilendirme e-postası gönder */
	public void sendOfferAcceptedEmail(String customerEmail, String offerTitle) {
		String subject = "✅ Teklifiniz Kabul Edildi!";
		String message = """
                Merhaba,
                
                🎉 **Tebrikler!** Aşağıdaki teklifinizi kabul ettiniz:
                
                📌 **Teklif Başlığı:** %s
                
                Teklifiniz onaylandı ve işlemler başlatıldı.
                
                📩 Eğer ek bilgi almak isterseniz, bizimle iletişime geçebilirsiniz.
                
                **Enterprise Destek Ekibi**
                """.formatted(offerTitle);
		
		sendEmail(customerEmail, subject, message);
	}
	
	/** 📌 Teklif reddedildiğinde müşteriye bilgilendirme e-postası gönder */
	public void sendOfferRejectedEmail(String customerEmail, String offerTitle) {
		String subject = "❌ Teklifiniz Reddedildi!";
		String message = """
                Merhaba,
                
                ❌ Aşağıdaki teklifi reddettiniz:
                
                📌 **Teklif Başlığı:** %s
                
                Eğer fikrinizi değiştirirseniz, bizimle tekrar iletişime geçebilirsiniz.
                
                **Enterprise Destek Ekibi**
                """.formatted(offerTitle);
		
		sendEmail(customerEmail, subject, message);
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
		String feedbackLink = "http://localhost:8083/feedback.html?ticketId=" + ticketNumber;
		
		return """
        Merhaba,

        📌 **Ticket Konusu:** %s
        🆔 **Ticket Numarası:** %s
        
        Destek talebiniz **%s** olarak işaretlenmiştir.
        
        Eğer sorununuz tamamen çözülmediyse veya ek bir yardıma ihtiyacınız varsa, lütfen bu e-postaya yanıt vererek bizimle iletişime geçin.

        ✨ **Hizmetimizi değerlendirmek ister misiniz?**
        [Geri Bildirim Formu](%s)
        
        **İyi günler dileriz!**
        **Enterprise Destek Ekibi**
        """.formatted(subject, ticketNumber, status, feedbackLink);
	}
	
	public String sendFeedbackRequestEmail(String toEmail, Long ticketId) {
		String feedbackLink = "http://localhost:8083/feedback.html?ticketId=" + ticketId;
		
		return """
        Merhaba,

        Destek talebinizle ilgili geri bildirimde bulunmak ister misiniz?

        📌 **Ticket ID:** %s

        Görüşleriniz bizim için değerli! Lütfen aşağıdaki linkten geri bildirim formunu doldurun:
        
        📝 **[Geri Bildirim Formu](%s)**

        **Teşekkürler!**
        **Enterprise Destek Ekibi**
        """.formatted(ticketId, feedbackLink);
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