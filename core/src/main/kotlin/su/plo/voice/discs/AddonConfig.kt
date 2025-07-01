package su.plo.voice.discs

import su.plo.config.Config
import su.plo.config.ConfigField
import su.plo.config.provider.ConfigurationProvider
import su.plo.config.provider.toml.TomlConfiguration
import su.plo.slib.api.server.McServerLib
import su.plo.voice.api.server.PlasmoVoiceServer
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.URI

@Config
class AddonConfig {
    @ConfigField
    var sourceLineWeight = 10

    @ConfigField(
        comment = """
            The default volume. Volume is configured on the client side
            and can be adjusted via the mod settings.
        """
    )
    var defaultSourceLineVolume = 0.5

    @ConfigField(
        comment = """
            Add enchantment glint to custom discs.
        """
    )
    var addGlintToCustomDiscs = false

    enum class LoreMethod {
        DISABLE,
        REPLACE,
        APPEND
    }
    @ConfigField(
        comment = """
            The method for creating/removing a lore on burning/erasing the discs:
            
            DISABLE — Disables any lore manipulations on burn/erase.
            REPLACE — Replaces the whole lore with a string containing the song name on burn, and removes the lore completely on erase.
            APPEND — Adds a new line to the end of the lore on burn, and removes the last line on erase.
            
            Default is REPLACE.
        """
    )
    val burnLoreMethod = LoreMethod.REPLACE

    @ConfigField(
        comment = """
            Uses mono sources to play tracks.
            Quality is worse, but they will now have panning.
            
            Conversion happens on client side, so there is no server overhead.
        """
    )
    val monoSources: Boolean = false

    @ConfigField(
        comment = """
            HTTP/HTTPS proxy. Valid formats:
            http://user:password@ip:port
            https://user:password@ip:port
        """,
        nullComment = "http_proxy = \"http://user:password@ip:port\""
    )
    val httpProxy: String? = null

    @Config class GoatHornConfig {

        @ConfigField(
            comment = """
                Enable goat horns.
                Allows burning audio onto the goat horn using /disc burn.
                When a custom goat horn is used, the audio will be loaded and attached to the player.
                Use /disc cancel to stop the goat horn audio from playing.
            """
        )
        val enabled: Boolean = false

        @ConfigField(
            comment = """
                Visualizes the distance to the player who used a custom goat horn.
                Visualization will be seen only by players with "Visualize Voice Distance" enabled.
            """
        )
        val visualizeDistance: Boolean = true

        @ConfigField(
            comment = """
                Goat horn distance.
            """
        )
        val distance: Short = 15

        @ConfigField(
            comment = """
                Goat horn maximum audio duration in seconds.
                Set to 0 to disable duration limit.
            """
        )
        val maxDurationSeconds: Int = 200
    }

    @ConfigField
    val goatHorn: GoatHornConfig = GoatHornConfig()

    @Config class DistanceConfig {

        @ConfigField(
            comment = """
                Visualizes the distance to the player who inserted a custom disc into the jukebox.
                Visualization will be seen only by players with "Visualize Voice Distance" enabled.
            """
        )
        var visualizeDistance: Boolean = true

        @ConfigField(
            comment = """
                Distance if 'enable_beacon_like_distance_amplification' is set
                to false.
            """
        )
        var jukeboxDistance: Short = 65

        @ConfigField(
            path = "enable_beacon_like_distance_amplification",
            comment = """
                With this option enabled you can build a beacon-like pyramid
                under a jukebox to change the distance of the sound.
            """
        )
        var enableBeaconLikeDistance = false

        @ConfigField(
            path = "beacon_like_distances",
            comment = """
                The first element is the distance without any pyramid layers.
                You can add as much layers as you want. Even more or less
                than the vanilla beacon, but at least one layer is required.  
            """
        )
        var beaconLikeDistanceList: List<Short> = listOf(12, 24, 32, 48, 64)
    }

    @ConfigField
    val distance = DistanceConfig()

    @Config
    class HttpSourceConfig {
        @ConfigField(
            comment = """
                Only allow links from trusted sources. You can disable this if
                the server IP is public and leaking it is not a problem.
            """
        )
        val whitelistEnabled = true
        @ConfigField
        val whitelist = listOf(
            "dropbox.com",
            "dropboxusercontent.com"
        )
    }

    @ConfigField
    val httpSource = HttpSourceConfig()

    @Config
    class YouTubeSourceConfig {
        @ConfigField(
            comment = """
                If you see a error like "Sign in to confirm you're not a bot",
                you can try using YouTube oauth2 authorization.

                On the first start with authorization enabled,
                you will see "OAUTH INTEGRATION" in your console.
                Follow the instructions in this prompt.
                If you do everything right, you will see "Token retrieved successfully" in your console.
                You only need to do this once;
                the token will be stored in "pv-addon-discs/.youtube-token" after successful authorization.
            """
        )
        val useOauth2: Boolean = false

        @ConfigField(
            comment = """
                https://github.com/lavalink-devs/youtube-source?tab=readme-ov-file#using-a-potoken
               
                You don't need to specify a poToken if using oauth2, and vice versa.
                If poToken is specified, oauth2 will be disabled.
            """,
            nullComment = """
                [youtube_source.po_token]
                token = "paste your po_token here"
                visitor_data = "paste your visitor_data here"
            """
        )
        val poToken: PoToken? = null

        @ConfigField(
            comment = """
                You can check available clients here: https://github.com/lavalink-devs/youtube-source?tab=readme-ov-file#available-clients
            """,
            nullComment = """
                clients = ["MUSIC", "ANDROID_VR", "WEB", "WEBEMBEDDED", "TVHTML5EMBEDDED"]
            """
        )
        val clients: List<String>? = null

        @Config
        class PoToken {
            @ConfigField
            val token: String = ""

            @ConfigField
            val visitorData: String = ""
        }
    }

    @ConfigField
    val youtubeSource = YouTubeSourceConfig()

    @ConfigField
    val localSource = LocalSourceConfig()

    @Config
    class LocalSourceConfig {
        @ConfigField(
            comment = """
                Enable local source. Local source allows to load audio files from specified "path" folder.
                
                Example format:
                file://test.mp3
                local://test.mp3
            """
        )
        val enabled = false

        @ConfigField(comment = """
            Path used as a root to resolve audio files.
            If path is empty, "local" folder inside addon's plugin folder will be used ("plugins/pv-addon-discs/local").
        """)
        val path = ""
    }

    @Config
    class BurnableTag {
        @ConfigField(
            comment = """
                With this option you can only burn discs that have a special NBT
                tag. You can use this to add a custom way of getting burnable
                discs, like buying for in-game currency, or crafting.
            """
        )
        var requireBurnableTag = false
        @ConfigField(
            comment = """
                Enable a recipe for burnable discs. It's a shapeless craft.
                By default you need a disc + 4 diamonds to get a burnable disc.
                You can configure recipe item and cost. 
            """
        )
        var enableDefaultRecipe = false
        @ConfigField
        var defaultRecipeItem = "minecraft:diamond"
        @ConfigField
        var defaultRecipeCost = 4
        @ConfigField(
            comment = """
                A lore that will be added to a burnable disc crafted with the
                default recipe.
            """
        )
        var defaultRecipeLore = "Burnable"
    }

    @ConfigField
    val burnableTag = BurnableTag()

    companion object {
        fun loadConfig(server: PlasmoVoiceServer): AddonConfig {

            val addonFolder = getAddonFolder(server.minecraftServer)

            server.languages.register(
                URI.create("https://github.com/plasmoapp/plasmo-voice-crowdin/archive/refs/heads/addons.zip").toURL(),
                "server/discs.toml",
                { resourcePath: String -> getLanguageResource(resourcePath)
                    ?: throw Exception("Can't load language resource") },
                File(addonFolder, "languages")
            )

            val configFile = File(addonFolder, "discs.toml")

            return toml.load<AddonConfig>(AddonConfig::class.java, configFile, false)
                .also { toml.save(AddonConfig::class.java, it, configFile) }
        }

        @Throws(IOException::class)
        private fun getLanguageResource(resourcePath: String): InputStream? {
            return javaClass.classLoader.getResourceAsStream(String.format("discs/%s", resourcePath))
        }

        private val toml = ConfigurationProvider.getProvider<ConfigurationProvider>(
            TomlConfiguration::class.java
        )

        private fun getAddonFolder(minecraftServer: McServerLib): File =
            File(minecraftServer.configsFolder, "pv-addon-discs")
    }
}
