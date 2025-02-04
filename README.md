<h1>Сервис коротких ссылок</h1>

Консольная программа для создания и управления короткими ссылками.

<h2>Параметры запуска</h2>

Все параметры являются необязательными. Если программа запущена без параметров, то будут созданы все необходимые файлы в корневом каталоге запуска и сгенерирован новый пользователь.
Задаваемые параметры:

* -id \<UUID\> \- UUID пользователя, если пользователь был ранее создан в системе, то подтянется его профиль, если же такого пользователя нету, то он будет создан и добавлен;
* -url \<путь к файлу\> \- путь к файлу со списком длинным ссылок
* -users \<путь к файлу\> \- путь к файлу со списком зарегестрированных UUID пользователей
* -db \<путь к файлу\> \- путь к файлу с профилями пользователей
* -cfg \<путь к файлу\> \- путь к файлу с глобальной конфигурацией программы
* -short \<путь к файлу\> \- путь к файлу со списком сгенерированных коротких ссылок
* -help - вывод краткой справки

<h2>Организация и хранение данных</h2>

Для сохранения информации программа используются 5 файлов.

* Файл Конфигурации. Задаётся параметром cfg. По-умолчанию имеет имя sets.cfg. Имеет формат JSON. Содержит: префикс для коротких ссылок (по-умолчанию http://localhost); время жизни коротких ссылок в днях (по-умолчанию 3 дня)
* Файл Длинных ссылок. Задаётся параметром url. По-умолчанию имеет имя url.list. Содержит список всех внесённых когда либо длинных ссылок. Имеет формат JSON. Каждый элемент состоит из пары <ссылка в текстовом виде>:<таймстэмп даты создания>
* Файл Коротких ссылок. Задаётся параметром short. По-умолчанию имеет имя short.list. Содержит список созданных коротких ссылок. Имеет формат JSON. Каждый элемент состоит из пары <суфикс короткой ссылки>:<таймстэмп даты создания>
* Файл Пользователей. Задаётся параметром users. По-умолчанию имеет имя users.list. Содержит список созданных пользователей. Имеет формат JSON. Каждый элемент состоит из пары <UUID в тестовом виде>:<таймстэмп даты создания>
* Файл Профилей пользователей. Задаётся параметром db. По-умолчанию имеет имя db.list. Содержит список профилей пользователей. Имеет формат JSON. Каждый элемент состоит из пары <Хэш UUID пользователя>: [{<Хэш длинной ссылки>:{<Хэш короткой ссылки>:<количество оставшихся переходов>}}, ...]

<h2>Получение хэшей</h2>
Расчёт всех хешей обеспечивается классом StringWithDate методом hashCode. По сути это арифметическая сумма всех символов и значения таймстемпа.

<h2>Удаление  элементов</h2>
Если у пользователя не остаётся ни одной короткой ссылки, то он удаляется из списка Профилей пользователей. Если время жизни короткой ссылки исчерпано или исчерпан лимит переходов по короткой ссылке, то она удаляется из файла коротких ссылок.

<h2>Взаимодействие с пользователем</h2>

При запуске в консоле отображается общая информация полученная из подгруженных файлов: префикс для коротких ссылок и время жизни всех коротких ссылок, общее кол-во пользователей, коротких и длинных ссылок. После чего отображатеся UUID пользователя для которого данная сессия активна. И предлагется выбрать из следующего набора действий:

* Показать список URL - выводит всю информацию по каждой ссылке, которая зарагестрированна в профиле пользователя.
* Добаввить новый URL - позволяет пользователю добавить новый длинный URL и установить лимит переходов по этой ссылке. Будет сгенерирована короткая ссылка.
* Перейти по ссылке - позволяет перейти по короткой ссылке из профиля пользователя. Передаёт длинную ссылку в браузер. На этом этапе делается проверка на количество попыток и дату ссылки. Если одно из условий не удовлетворяет, то переход осуществлён не будет и ссылка будет удалена, а пользователь получит уведомление.
* Выйти - завершение работы программы

<h2>Сборка проекта</h2>
Проект собирается с помощью Maven. Для разработки использовался Eclipse IDE. Используются следующие зависимости:

* jcommander - для работы с параметрами командной строки;
* jackson - для работы с JSON.

<h2>Тестирование проекта</h2>

Для тестирования можно несколько раз запустить программу без параметров. Каждый раз при запуске будет создан новый UUID. При каждом запуске можно подабавлять по несколько ссылок. После этого требуется открыть файл user.list, который будет автоматически создан с папке с проектом. Содержимое этого файла может выглядеть примерно так:
<pre>
  {"hOr0jb2KUsHukc1":1737099048717,"U7WLPdVFYG5lrno":1737099200722}
</pre>
Из этого файла выбрать любой uuid и запустить программу с аргументом -id <UUID>. (Примеры: если UUID hOr0jb2KUsHukc1, то строка с аргументом будет выглядеть "-id hOr0jb2KUsHukc1" ). 

После запуска с определённым UUID можно проверить список ссылок, и попробовать по переходить по ним.
