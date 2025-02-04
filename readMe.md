# Yeni Servis kuracaklar Icin Not

### Her yeni servis oluşturulduğunda tekrar bir `build.gradle` dosyası yazmamak için mevcut ana proje için Gradle ayarları düzenlenmiştir.

### Mevcut bağımlılıklar dışında yeni bir bağımlılık ekleme ihtiyacı olmadığı sürece, yeni oluşturulan servisin `build.gradle` dosyası boş kalabilir.

### Eğer sadece eklediğiniz yeni servise özel bir bağımlılık eklenmesi gerekiyorsa, bu bağımlılığı o servisin `build.gradle` dosyasına ekleyebilirsiniz. Ancak, tüm servislere eklenmesi gereken bir bağımlılıksa, doğrudan ana yapı altındaki merkezi Gradle dosyasına eklenmelidir.


`dependencies.gradle` icerisine:

```
degiskenIsmi : 'bagimlilikLinki'
```
seklinde ekleme yaptiktan sonra `build.gradle` icerisindeki _dependencies_ icerisine :

```
implementation libs.degiskenAdi
```
seklinde ekleme yapilabilir!