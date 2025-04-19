<h1>Задание 1</h1>
В данном задании был развернут кластер Kafka в облаке, протестирована отпарвка и получение сообщений из топика


Получение сертификата для подключения к Кафке в облаке
![img.png](img.png)

Добавление сертификата к truststore.jks
keytool -importcert -alias YandexCA -file /usr/local/share/ca-certificates/Yandex/YandexInternalRootCA.crt -keystore kafka.truststore.jks -storepass password -noprompt

Настройка кластера в облаке
![img_1.png](img_1.png)

Успешная отправка сообщений через Producer в топик test
![img_2.png](img_2.png)

Успешное чтение сообщений через Consumer в топике test
![img_3.png](img_3.png)

Топик в облаке 
![img_4.png](img_4.png)

Пользователи 
![img_5.png](img_5.png)

Добавил и успешно развернул Schema Registry

![img_13.png](img_13.png)

Запуск через Producer - ProducerWithSR, Consumer - ConsumerWithSR
![img_12.png](img_12.png)

Отправка сообщения 
![img_15.png](img_15.png)

Прием сообщения
![img_14.png](img_14.png)


<h1>Задание 2</h1>
Сервиса nifi в облаке я не нашел, потому развернул его локлаьно.

Строим ETL в NiFi
![img_6.png](img_6.png)

В папку nifi_data перекладываем файл из папки temp_data
Nifi его подхватывает 
![img_7.png](img_7.png)
![img_8.png](img_8.png)

Данные успешно отправлены в топик кафки
![img_9.png](img_9.png)

Настройки Producer Nifi
![img_10.png](img_10.png)
![img_11.png](img_11.png)

