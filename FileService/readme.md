MinIO'yu Docker'da ayağa kaldırmak için aşağıdaki komut CMD'ye yapıştırılmalı.

docker run -p 9000:9000 -p 9001:9001 --name minio -e MINIO_ROOT_USER="admin" -e MINIO_ROOT_PASSWORD="BilgeAdam123**" quay.io/minio/minio server /data --console-address ":9001"

Bu komut CMD'ye yapıştırıldıktan sonra http://localhost:9001/ adresinden MinIO arayüzüne giriş yapılabilir.

Username : admin
Password : BilgeAdam123**

Arayüze girdikten sonra Buckets sekmesinden Create Bucket'a tıklayarak ismi enterprise-bucket olan yeni bir bucket oluşturmalısınız.

MinIO'yu kullanabilmek için FileService>src>main>resources altındaki application.yml içerisine şu config'ler eklenmeli : 

minio:
  endpoint: http://127.0.0.1:9000
  access-key: admin
  secret-key: BilgeAdam123**
  bucket-name: enterprise-bucket


build.gradle dosyanızda eğer yoksa şu bağımlılığı eklemelisiniz : 

implementation 'io.minio:minio:8.5.6'