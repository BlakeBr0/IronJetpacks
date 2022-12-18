# Iron Jetpacks [![](http://cf.way2muchnoise.eu/full_284497_downloads.svg)](https://minecraft.curseforge.com/projects/iron-jetpacks)
Adds fully customizable FE powered jetpacks!

[Version 2.x+ Docs](https://blakesmods.com/docs/ironjetpacks)

[Version 1.x Docs](https://github.com/BlakeBr0/IronJetpacks/wiki)

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
