# Liquibase

**Liquibase** — это открытая независимая от БД библиотека для отслеживания, управления и применения изменений схемы базы данных. Поддерживает подавляющее большинство БД, включая PostgreSQL

- Миграция баз данных — это что-то вроде системы контроля версий для вашей схемы базы данных. Она позволяет разработчикам изменять структуру БД, сообщать другим участникам команды об этих изменениям и самим быть в курсе апдейтов, а также отслеживать историю изменений.
- Все изменения, которые мы вносим, хранятся в отдельных файлах. Часто их называют чейнджлогами.
- В файлах-чейнджлогах изменения представляются в виде чейнджсетов, так называемых точек сохранения. Чейнджсет может хранить одно или несколько изменений базы данных. Каждый чейнджсет уникально идентифицируется.

## Подключаем библиотеку Liquibase
Для работы с библиотекой нам нужно добавить зависимость в файл pom.xml.

```
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
</dependency>
```

## Создаем главный запускаемый файл changelog.xml
В changelog.xml вставляем стандартный пустой шаблон по пути: *src/main/resources/liquibase/changelog.xml*
```
‹?xml version="1.0" encoding="UTF-8"?>
‹databaseChangeLog
       xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.1.xsd">

    ‹!-- Здесь будем писать пути к файлам-чейнджлогам -->
    <include file="structure/create_schema.xml" relativeToChangelogFile="true"/>
    
‹/databaseChangeLog>
```

## Создание таблицы
<details>
  <summary>structure/create_table_genre.xml</summary>

```
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
       xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.1.xsd">

   <changeSet id="create_table_genre" author="team29" remarks="Жанр">
      <!-- Прописываем создание таблицы genre-->
       <createTable tableName="genre">
        <!--Создаем поля -->
           <column autoIncrement="true" name="genre_id" type="bigint" remarks="Идентификатор">
               <constraints primaryKey="true" nullable="false"/>
           </column>
           <column name="genre_name" type="varchar(64)" remarks="Наименование жанра" />
       </createTable>
   </changeSet>
   
</databaseChangeLog>
```
Типы полей: [ссылка](https://docs.liquibase.com/concepts/changelogs/attributes/column.html)
</details>


## Занести данные в таблицу
<details>
  <summary>structure/insert-changeset-genre-table.xml</summary>

```
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
       xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
                    http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.1.xsd">

<!-- Добавим 4 новых жанра -->

   <changeSet id="insert-into-genre" author="team29">
       <insert tableName="genre">
           <column name="genre_name" value="Роман"/>
       </insert>
       <insert tableName="genre">
           <column name="genre_name" value="Поэма"/>
       </insert>
       <insert tableName="genre">
           <column name="genre_name" value="Рассказ"/>
       </insert>
       <insert tableName="genre">
           <column name="genre_name" value="Эпос"/>
       </insert>
   </changeSet>

</databaseChangeLog>
```
</details>

## Связать таблицы по внешнему ключу
<details>
  <summary>Пример</summary>

Таблица **book** ссылается на таблицу **genre**
```
<addForeignKeyConstraint baseColumnNames="genre_id"
                        baseTableName="book"
                        constraintName="fk_author_id"
                        referencedColumnNames="genre_id"
                        referencedTableName="genre"/>
```
Важный момент: сначала создайте таблицу, к которой будете привязывать, а уже потом привязываемую. Иначе Liquibase не поймет, что с чем нужно связывать и выдаст ошибку. В нашем случае мы сначала создаем genre, а уже потом book с его внешним ключом

</details>

## Changelog

Разработчики сохраняют изменения базы данных в текстовых файлах на своих локальных машинах разработки и применяют их к своим локальным базам данных. Файлы изменений могут быть произвольно вложены для лучшего управления. Корнем всех изменений Liquibase является файл databaseChangeLog.

## ChangeSet

Каждый тег changeSet уникально идентифицируется комбинацией тега id, тега «author» и имени пути к файлу списка изменений. Тег id используется только как идентификатор, он не управляет порядком выполнения изменений и даже не должен быть целым числом. Если вы не знаете или не хотите сохранить фактического автора, просто используйте значение заполнитель, например «UNKNOWN». Поскольку Liquibase выполняет databaseChangeLog, он считывает изменения в порядке и для каждого из них проверяет таблицу «databasechangelog», чтобы увидеть, была ли запущена комбинация id / author / filepath. Если он был запущен, changeSet будет пропущен, если не будет истинного тега «runAlways». После того, как все изменения в changeSet будут запущены, Liquibase вставляет новую строку с id / author / filepath вместе с MD5Sum changeSet в «databasechangelog». Liquibase пытается выполнить каждый changeSet в транзакции, которая выполняется в конце, или откат, если есть ошибка. Некоторые базы данных будут автоматически фиксировать утверждения, которые мешают этой настройке транзакции и могут привести к непредвиденному состоянию базы данных. Поэтому обычно лучше всего иметь одно изменение для changeSet, если нет группы изменений, не связанных с автоматической фиксацией, которые вы хотите применить в качестве транзакции, такой как вставка данных.


## Полезные ссылки
- [Ссылка 1](https://struchkov.dev/blog/get-started-liquibase/)
- [Ссылка 2](https://ru.bmstu.wiki/Liquibase)
- [Ссылка 3](https://sendel.ru/posts/liquibase-and-spring-boot/)
- [ChangeSet](https://habr.com/ru/post/251617/)
