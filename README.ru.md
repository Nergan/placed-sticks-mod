# Placed Sticks

**[English](README.md)** · **[Русский](README.ru.md)**

![Placed Sticks](logo.png)

Мод для **Minecraft 1.21.1** на NeoForge и Fabric: палки и стебли бамбука ставятся как тонкие прутья. NeoForge использует [Kotlin for Forge](https://modrinth.com/mod/kotlin-for-forge). Fabric использует [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin). Ставится один загрузчик, не оба.

Названия блоков есть на английском и русском.

## Что делает мод

- **Палки.** ПКМ по блоку, держа палку. Прут занимает пустую клетку вдоль нажатой грани. Верх и низ ставят его стоймя, бок кладёт плашмя.
- **Три в одном блоке.** Нажмите по пруту другой гранью, чтобы добавить второй или третий. В блоке по одному пруту на каждую ось. При сломе возвращаются все палки, которые внутри были.
- **Бамбук.** Те же прутья, чуть толще, с ванильной текстурой стебля. Обычный клик по земле, где бамбук растёт, по-прежнему сажает ванильный бамбук. Присядьте, чтобы поставить декоративный стебель. Там, где бамбук не растёт, стебель ставится без приседания.
- **Используемые блоки.** Сундуки, двери и другие блоки, с которыми можно взаимодействовать, по-прежнему открываются, если не приседать. Присядьте, чтобы поставить прут на них.

## Загрузки

Jar лежат на [GitHub Releases](https://github.com/Nergan/placed-sticks-mod/releases/latest) и на [Modrinth](https://modrinth.com/project/placed-sticks). Пуш в `main` обновляет файлы релиза текущей версии. На Modrinth попадает только jar этого мода.

Скачайте один набор и положите эти файлы в папку `mods`.

### NeoForge

| Файл | Обязателен | Что это |
| --- | --- | --- |
| `placedsticks-neoforge-1.21.1-1.0.0.jar` | Да | этот мод |
| `kotlinforforge-5.8.0-all.jar` | Да | [Kotlin for Forge](https://modrinth.com/mod/kotlin-for-forge) (LGPL-2.1) |

### Fabric

| Файл | Обязателен | Что это |
| --- | --- | --- |
| `placedsticks-fabric-1.21.1-1.0.0.jar` | Да | этот мод |
| `fabric-api-0.116.17+1.21.1.jar` | Да | [Fabric API](https://modrinth.com/mod/fabric-api). Строка «fabric 0.100.3» означает этот jar, а не Fabric Loader |
| `fabric-language-kotlin-1.13.2+kotlin.2.1.20.jar` | Да | [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) |

`*-sources.jar` в `mods` класть не нужно.

## Требования

| Компонент | Версия |
| --- | --- |
| Minecraft | 1.21.1 |
| NeoForge | 21.1.209 (подойдёт любой 21.1.x) |
| Kotlin for Forge | 5.8.0, сборка **NeoForge** |
| Fabric Loader | 0.16.10 или новее |
| Fabric API | 0.116.17+1.21.1 |
| Fabric Language Kotlin | 1.13.2+kotlin.2.1.20 |
| Java | 21 |

## Установка

NeoForge: установите NeoForge 1.21.1 и положите в `mods` файлы `placedsticks-neoforge-1.21.1-1.0.0.jar` и `kotlinforforge-5.8.0-all.jar`.

Fabric: установите Fabric Loader 0.16.10 или новее и положите в `mods` файлы `placedsticks-fabric-1.21.1-1.0.0.jar`, `fabric-api-0.116.17+1.21.1.jar` и `fabric-language-kotlin-1.13.2+kotlin.2.1.20.jar`. Loader 0.19.x подходит.

Мод нужен и на клиенте, и на сервере.

## Лицензия

Код под [MPL-2.0](LICENSE). Модель стебля бамбука ссылается на ванильную текстуру `bamboo_stalk`, которая уже есть в игре; в этот jar она не входит.
