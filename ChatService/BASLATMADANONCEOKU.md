-UYGULAMAYI KULLANABİLMEK İÇİN GEREKLİ ADIMLAR--

1-Utility içerisindeki MockDataInitializer sınıfını aç ve @Component anotasyonunu yorumdan çıkar 

2-Program başlatıldığında mock data olarak 5 user oluşturulacak.user1@example.com user1 
// user2@example.com user2 vs.

3-Auth componentine bağlı olunmadığı için öncelikle postman üzerinden giriş yapılması gerekiyor.

4-http://localhost:5174/chat doğrudan bu adrese gidin herhangi bir auth kontrolü şuanlık yok.

4-Postman'den dönen token'i localStorage'a (token,{token}) şeklinde kaydedin. Sayfayı yenileyin!!

5-Token kaydetmeden websocket bağlantısı ve fetch işlemleri gerçekleşmeyeceği için sayfa render olmuyor, bu nedenle
üstteki adımlar önemli.

6-Frontend'de yer alan + butonu ile private ya da group chat'i oluşturabilirsiniz.

7-Sonrasında sol bar'da yer alan chat listesinden istediğiniz chat'i seçip mesajlaşmaya başlayabilirsiniz.

8-Karşılıklı mesajlaşabilmek için başka bir browser'la ya da gizli mod'da bir client daha açıp başka bir user ile giriş
yapıp yine postman'den dönen tokeni diğer browser'ın localStorage'ına token key'i ile kaydedin.