# Iron Jetpacks 

<p align="left">
    <a href="https://blakesmods.com/iron-jetpacks" alt="Downloads">
        <img src="https://img.shields.io/endpoint?url=https://api.blakesmods.com/v2/badges/ironjetpacks/downloads&style=for-the-badge" />
    </a>
    <a href="https://blakesmods.com/iron-jetpacks" alt="Latest Version">
        <img src="https://img.shields.io/endpoint?url=https://api.blakesmods.com/v2/badges/ironjetpacks/version&style=for-the-badge" />
    </a>
    <a href="https://blakesmods.com/iron-jetpacks" alt="Minecraft Version">
        <img src="https://img.shields.io/endpoint?url=https://api.blakesmods.com/v2/badges/ironjetpacks/mc_version&style=for-the-badge" />
    </a>
    <a href="https://blakesmods.com/docs/ironjetpacks" alt="Docs">
        <img src="https://img.shields.io/static/v1?label=docs&message=view&color=brightgreen&style=for-the-badge" />
    </a>
    <a href="https://blakesmods.com/wiki/ironjetpacks" alt="Wiki">
        <img src="https://img.shields.io/static/v1?label=wiki&message=view&color=brightgreen&style=for-the-badge" />
    </a>
</p>

Adds fully customizable FE powered jetpacks!

## Download

The official release builds can be downloaded from the following websites.

- [Blake's Mods](https://blakesmods.com/iron-jetpacks/download)
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/iron-jetpacks)
- [Modrinth](https://modrinth.com/mod/iron-jetpacks)

## Development

To use this mod in a development environment, you will need to add the following to your `build.gradle`.

```groovy
repositories {
    maven {
        url 'https://maven.blakesmods.com'
    }
}

dependencies {
    implementation fg.deobf('com.blakebr0.cucumber:Cucumber:<minecraft_version>-<mod_version>')
    implementation fg.deobf('com.blakebr0.ironjetpacks:IronJetpacks:<minecraft_version>-<mod_version>')
}
```

## License

[MIT License](./LICENSE)
