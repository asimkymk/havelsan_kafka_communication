# Modüller Arası Haberleşme - Apache Kafka

Java kullanarak Apache Kafka ile özel mesaj tipleriyle subscriber ve publisher yazma ve ilgili haberleşmeyi sağlama.



## Hakkında

*   Daha önce LinkedIn tarafından Java kullanılarak geliştirilen Kafka daha sonra Apache ile açık kaynak bir projeye dönüştürülmüştür. Aktif olarak büyük verilere sahip birçok şirket tarafından kullanılmaktadır. 

*   Verilerin sistem tarafından hızlı bir şekilde toparlanıp hatasız şekilde transfer edilip işlenmesini sağlayan akış şemasını sağlar. Dağıtık (distrubuted) bir veri akış (streaming) platformudur. Hataya dayanıklı, yatay olarak ölçeklenebilen, esnek bir mimariye sahiptir. Son derece yüksek performans ile bir sistemden diğer sisteme 10 ms’den az bir gecikme(latency) ile neredeyse gerçek zamanlı olarak veri transferini mümkün kılmaktadır. Mesajlaşma sistemi (messaging system) olabilir, etkinlik takibi(activity tracking) için, uygulama loglarını toplamak için, sağladığı API ile stream processing amacıyla kullanılabilir.

## Gereksinimler

| Platform                                  |
|-------------------------------------------|
| **JAVA 8+** | 
  
## Kurulum

*  Java Kurulumu
    
    ```sh
    sudo apt install default-jre
    sudo apt install default-jdk
    ```

* Apache Kafka Kullanımı
        Öncelikle Apache Kafka'nın en son Binary sürümünü indiriyoruz. [İndir](https://kafka.apache.org/downloads) 
        İndirilen klasörde yeni bir terminal açıp aşağıdaki gibi çıkartma işlemi yapıyoruz.
    ```sh
    tar -xzf kafka_2.13-3.2.1.tgz
    cd kafka_2.13-3.2.1
    ```

    * Tüm servisleri başlatmak için aşağıdaki komutla devam ediyoruz.
        ```sh
        bin/zookeeper-server-start.sh config/zookeeper.properties
        ```
    * Yeni terminal açıp bir komut daha çalıştırıyoruz.
        ```sh
        bin/kafka-server-start.sh config/server.properties
        ```
    
    Herhangi bir hata alınmadığı kontrol edilerek tüm servislerin başladığı doğrulanabilir.



## Paket Oluşturma
* Seçtiğiniz herhangi bir IDE üzerinden bir Java Maven projesi oluşturun. Oluşturulan projede öncelikle proje dosyamız içerisinde resources/schema dizinini oluşturun.

    ```sh
    mkdir -p resources/schema
    ```
* İhtiyaca uygun olacak şekilde gerekli objelerin json türünde tiplerini tanımlayın.
    * Status.json
    ```json
    {
        "type": "object",
        "javaType": "message.types.Status",
        "properties": {
            "diskSize": {
            "type": "long"
            },
            "freeDiskPartition": {
            "type": "long"
            },
            "usableDiskPartition": {
            "type": "long"
            },
            "systemLiveStatus": {
            "type": "boolean[]"
            },
            "consoleRecordStatus": {
            "type": "boolean[]"
            },
            "displayRecordStatus": {
            "type": "boolean[]"
            }
        }
    }
    ```
    * Action.json
    ```json
    {
        "type": "object",
        "javaType": "message.types.Action",
        "properties": {
            "sender_id": {
            "type": "string"
            },
            "action": {
            "type": "string"
            },
            "action_name": {
            "type": "string"
            },
            "action_properties": {
            "type": "string"
            },
            "action_date": {
            "type": "Long"
            },
            "empty_area": {
            "type": "Object"
            }
        }
    }
    ```
* Proje gereksinimlerinin belirtildiği pom.xml dosyasını aşağıdaki gibi düzenleyin.
    
     ```xml
    ...
        <properties>
            <java.version>1.8</java.version>
            <kafka.version>2.6.0</kafka.version>
            <jersey.version>2.27</jersey.version>
            <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        </properties>
        <dependencies>

        <!-- Apache Kafka Streams-->
        <dependency>
            <groupId>org.apache.kafka</groupId>
            <artifactId>kafka-streams</artifactId>
            <version>${kafka.version}</version>
            <exclusions>
                <exclusion>
                    <groupId>org.rocksdb</groupId>
                    <artifactId>rocksdbjni</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <!-- Apache Log4J2 binding for SLF4J -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j-impl</artifactId>
            <version>2.11.0</version>
        </dependency>
            <dependency>
            <groupId>org.apache.kafka</groupId>
            <artifactId>kafka-clients</artifactId>
            <version>${kafka.version}</version>
        </dependency>
        <dependency>
            <groupId>commons-lang</groupId>
            <artifactId>commons-lang</artifactId>
            <version>2.6</version>
        </dependency>
            <!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind -->
            <dependency>
                <groupId>com.fasterxml.jackson.core</groupId>
                <artifactId>jackson-databind</artifactId>
                <version>2.11.2</version>
            </dependency>
            <!-- https://mvnrepository.com/artifact/org.jsonschema2pojo/jsonschema2pojo-maven-plugin -->
            <dependency>
                <groupId>org.jsonschema2pojo</groupId>
                <artifactId>jsonschema2pojo-maven-plugin</artifactId>
                <version>0.5.1</version>
            </dependency>

        <!-- https://mvnrepository.com/artifact/org.rocksdb/rocksdbjni -->
        <dependency>
            <groupId>org.rocksdb</groupId>
            <artifactId>rocksdbjni</artifactId>
            <version>6.11.4</version>
        </dependency>
        <!--spark java framework for embedded  -->
        <dependency>
            <groupId>com.sparkjava</groupId>
            <artifactId>spark-core</artifactId>
            <version>2.5</version>
        </dependency>
        <!-- javax.ws.rs  -->
        <dependency>
            <groupId>javax.ws.rs</groupId>
            <artifactId>javax.ws.rs-api</artifactId>
            <version>2.1</version>
        </dependency>
        <!--Jeersey dependencies -->
        <dependency>
            <groupId>org.glassfish.jersey.containers</groupId>
            <artifactId>jersey-container-servlet</artifactId>
            <version>${jersey.version}</version>
        </dependency>
        <dependency>
            <groupId>org.glassfish.jersey.inject</groupId>
            <artifactId>jersey-hk2</artifactId>
            <version>${jersey.version}</version>
        </dependency> 
        </dependencies>
        <build>
            <plugins>
                <!-- Maven Compiler Plugin-->
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>3.8.0</version>
                    <configuration>
                        <source>${java.version}</source>
                        <target>${java.version}</target>
                    </configuration>
                </plugin>
                <!-- Json Schema to POJO plugin-->
                <plugin>
                    <groupId>org.jsonschema2pojo</groupId>
                    <artifactId>jsonschema2pojo-maven-plugin</artifactId>
                    <version>0.5.1</version>
                    <executions>
                        <execution>
                            <goals>
                                <goal>generate</goal>
                            </goals>
                            <configuration>
                                <sourceDirectory>${project.basedir}/src/main/resources/schema/</sourceDirectory>
                                <outputDirectory>${project.basedir}/src/main/java/</outputDirectory>
                                <includeAdditionalProperties>false</includeAdditionalProperties>
                                <includeHashcodeAndEquals>false</includeHashcodeAndEquals>
                                <outputEncoding>${project.build.sourceEncoding}</outputEncoding>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
        ...
    ```
    Projeyi build ederek gerekli paketlerin yüklendiği kontrol edilmeli. Herhangi bir hata alınmazsa proje dosyası içerisinde messages/types dizininde Status.java ve Action.java model dosyalarının otomatik olarak oluşturulduğu görülebilir. 

    > Burada her iki objeye de java.util kütüphanesinden Serializable sınıfını implement edin. Böylece bir sonraki aşamalardakaki mesaj gönderimi sırasında herhangi bir hatanın önüne geçmiş oluyoruz.

* Serializer ve Deserializer Oluşturulması
    Proje dosyalarının içerisinde serialization paketi oluşturun ve ObjectSerializer.java, ObjectDeserializer.java dosyalarını oluşturun. Mesaj gönderimi sırasında gönderilecek mesajın tipi özel olarak bu şekilde ayarlanabilir.
    > Her iki dosyanın içeriği de kod içerisinde incelenebilir.

* Action sınıfındaki tarih iletimi için converter yazılması
Tarih formatı long tipinde saatdakikagünayYıl şeklinde yapılacak yani 16.08.2022 01:29 için 12916082022 şeklinde bir mesaj oluşturulacak. Yine aynı mesaj deserialize işleminden sonra 12916082022 olarak aldığı bilgiyi doğru tarih formatına çevrilmeli.

    * model/DateConverter.java
        ```java
        public class DateConverter {

            public static long dateToLong() {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HHmmddMMyyyy");
                LocalDateTime now = LocalDateTime.now();
                return Long.parseLong(dtf.format(now));
            }

            public static String longToStringDate(Object longDate) {
                String converted_date = String.valueOf(longDate);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                LocalDateTime custom_date;
                String temp = "";
                for (int i = 0; i < 12 - converted_date.length(); i++) {
                    temp += "0";
                }
                converted_date = temp + converted_date;

                custom_date = LocalDateTime.of(Integer.parseInt(converted_date.substring(8, 12)),
                        Integer.parseInt(converted_date.substring(6, 8)),
                        Integer.parseInt(converted_date.substring(4, 6)),
                        Integer.parseInt(converted_date.substring(0, 2)),
                        Integer.parseInt(converted_date.substring(2, 4)));

                return dtf.format(custom_date);
            }

        }
        ```
* connection/IAppConfigs.java dosyasında ilgili statik değerleri tutabilirsiniz. Buradaki BOOTSTAP_SERVER urlsinin kafka tarafından verilen url olduğu kontrol edilmeli.
    ```java
    public interface IAppConfigs {
        String BOOTSTAP_SERVER="localhost:9092";
        String APPLICATION_ID_CONFIG="havelsan_config";
        String STATUS_TOPIC="status";
        String ACTION_TOPIC="action";
        String SENDER_ID="client_1";
    }
    ```

* Producer yazılması
Producer yazılırken herhangi bir deserialize işlemi yapılmaz. Sadece serialize işlemi yapılır. Hem producer hem consumer için her mesaj tipine uyumlu key ve value değerleri bulunur. Bu key ve value değerleri uygun topicin partitionda yer alır. Burada bir sözlük yapısı bulunur. Hem key hem value değerinin tipinin ne olduğu özel olarak belirtilmelidir. Bizim projemiz için <key, value> = <String, [Action ya da Status]> olacak.

* Consumer yazılması
Burada ise producer tersine sadece deserialize işlemi yapılır. Mesajın producer tarafından publish edilirken <key,value> değerleri neyse consumer tarafındaki değer tipleri de aynı olmalıdır.

