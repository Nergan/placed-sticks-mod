# Placed Sticks

**[English](README.md)** · **[Русский](README.ru.md)**

![Placed Sticks](logo.png)

A **Minecraft 1.21.1** mod for NeoForge and Fabric: place sticks and bamboo stalks as thin rods. NeoForge uses [Kotlin for Forge](https://modrinth.com/mod/kotlin-for-forge). Fabric uses [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin). Install one loader, not both.

Block names are available in English and Russian.

## What it does

- **Sticks.** Right-click a block with a stick. The rod occupies the empty cell along the face you clicked. Top and bottom stand it upright; a side lays it flat.
- **Three per block.** Click the rod on another face to add a second or third. One block holds one rod along each axis. Breaking it returns every stick that was inside.
- **Bamboo.** The same rods, a little thicker, using the vanilla bamboo stalk. A normal click on ground where bamboo can grow still plants vanilla bamboo. Sneak to place a decorative stalk there. Anywhere bamboo cannot grow, the stalk is placed without sneaking.
- **Used blocks.** Chests, doors, and other blocks you can use still open when you are not sneaking. Sneak to place a rod on them.

## Downloads

Jars live on [GitHub Releases](https://github.com/Nergan/placed-sticks-mod/releases/latest) and on [Modrinth](https://modrinth.com/project/placed-sticks). A push to `main` updates the files on the current version’s release. Modrinth receives only this mod’s jar.

Download one set and put those files in the `mods` folder.

### NeoForge

| File | Required | What it is |
| --- | --- | --- |
| `placedsticks-neoforge-1.21.1-1.0.0.jar` | Yes | this mod |
| `kotlinforforge-5.8.0-all.jar` | Yes | [Kotlin for Forge](https://modrinth.com/mod/kotlin-for-forge) (LGPL-2.1) |

### Fabric

| File | Required | What it is |
| --- | --- | --- |
| `placedsticks-fabric-1.21.1-1.0.0.jar` | Yes | this mod |
| `fabric-api-0.116.17+1.21.1.jar` | Yes | [Fabric API](https://modrinth.com/mod/fabric-api). A message that says "fabric 0.100.3" means this jar, not Fabric Loader |
| `fabric-language-kotlin-1.13.2+kotlin.2.1.20.jar` | Yes | [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) |

Do not install `*-sources.jar`.

## Requirements

| Component | Version |
| --- | --- |
| Minecraft | 1.21.1 |
| NeoForge | 21.1.209 (any 21.1.x should work) |
| Kotlin for Forge | 5.8.0, **NeoForge** build |
| Fabric Loader | 0.16.10 or newer |
| Fabric API | 0.116.17+1.21.1 |
| Fabric Language Kotlin | 1.13.2+kotlin.2.1.20 |
| Java | 21 |

## Installation

NeoForge: install NeoForge 1.21.1 and put `placedsticks-neoforge-1.21.1-1.0.0.jar` and `kotlinforforge-5.8.0-all.jar` in `mods`.

Fabric: install Fabric Loader 0.16.10 or newer and put `placedsticks-fabric-1.21.1-1.0.0.jar`, `fabric-api-0.116.17+1.21.1.jar`, and `fabric-language-kotlin-1.13.2+kotlin.2.1.20.jar` in `mods`. Loader 0.19.x is fine.

The mod is required on both client and server.

## License

The code is [MPL-2.0](LICENSE). The bamboo stalk model uses the vanilla `bamboo_stalk` texture already in the game; that texture is not shipped in this jar.
