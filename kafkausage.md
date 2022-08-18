# Kafka Paket Yüklemesi


  
## Kurulum

*  Kafka Paketinin Yüklenmesi

    ```sh
    git clone https://github.com/asimkymk/havelsan_kafka_communication.git
    ```

* Ortamın Hazırlanması
    * Zookeper çalıştırılması
        ```sh
        bin/zookeeper-server-start.sh config/zookeeper.properties
        ```
    * Yeni terminalde servislerin başlatılması
        ```sh
        bin/kafka-server-start.sh config/server.properties
        ```
## Kullanım
* Tüm işlemlere başlamadan önce klonlanan proje dosyasının bir IDE üzerinde açılması kolaylık sunacaktır. Netbeans üzerinden devam edildiği düşünülürse açılan proje dosyasına sağ tıklayıp Clean & Build seçeneği seçilir ve paketlerin temiz kurulumu gerçekleştirilir.

* Devamında messages/types paketi içindeki otomatik oluşturulan Status.java ve Action.java modellerine Serializable implements edilir.
    ```java
    import java.io.Serializable;

    class Action implements Serializable
    ```

    ```java
    import java.io.Serializable;

    class Status implements Serializable
    ```


## Test
pubsub paketi içerisindeki dosyalardan Consumer modelleri kapatılana kadar açık kalan ve sürekli mesaj bekleyen modellerdir. Producer modelleri ise açılıp mesajı publish edip kapanırlar. Ide üzerinde istenen consumer ve istenen publisher çalıştırılıp test edilebilir. UI üzerinde test etmek için StatusConsumer önce çalıştırılmalıdır. Devamında pubsub paketi içerisindeki guiLauncher çalıştırılarak UI açılır. Açılan ekrandaki butona tıklandığında StatusConsumer terminali izlendiğinde verilerin geldiği gözlemlenebilir.
