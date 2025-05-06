### 📥 Шаги запуска

1. **Клонируйте репозиторий:**

```bash
git clone https://github.com/EgorUgchv/cardManagement.git
cd cardManagement
```

2. **Создание базы данных**

Необходимо создать postgresql базу данных с названием `card_management` или возможно впоследствии создать базу данных через команды `docker`.

3. **Запустите контейнеры:**

```bash
docker compose up
```

4. **Создание базы данных через `docker cli` (опционально)**

```shell
docker compose exec cardmanagement_db psql -U postgres -c "CREATE DATABASE card_management;"
```

### ✅ Всё готово



**Swagger**

Swagger UI находится по адресу `http://localhost:8080/swagger-ui/index.html#/`. Токен для роли администратора находится в `logs` контейнера `cardmanagement` в следующем виде `Admin toke: токен`.


*Развертывание протестировано на OS `Linux Mint`*
