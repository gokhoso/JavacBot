package net.javac;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.javac.buffer.impl.GuildMessageBuffer;
import net.javac.command.CommandManager;
import net.javac.command.general.Ping;
import net.javac.command.utility.SendEmbed;
import net.javac.config.ConfigLoader;
import net.javac.config.ConfigData;
import net.javac.log.LogManager;
import net.javac.log.impl.DeletedMessageLogger;
import net.javac.log.impl.UpdatedMessageLogger;
import net.javac.service.ServiceManager;
import net.javac.service.impl.Count;
import net.javac.utils.GuildUtils;

import java.util.Arrays;

public class Javac {
    static final Dotenv dotenv = Dotenv.configure().load();
    static ShardManager shardManager;
    final ConfigData data = ConfigLoader.getData();
    final LogManager logManager = new LogManager();
    final ServiceManager serviceManager = new ServiceManager(data.service.service_pool);
    final CommandManager commandManager = new CommandManager(data.command.text.cooldown_pool);


    static void main() {
        final var token = dotenv.get("TOKEN");
        if (token == null) throw new RuntimeException("TOKEN not found!");
        try {
            new Javac().init(token);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused")
    public static Dotenv getDotenv() {
        return dotenv;
    }

    public static ShardManager getShardManager() {
        return shardManager;
    }

    void build(String token, GatewayIntent... intents) {
        shardManager = DefaultShardManagerBuilder.createDefault(token).enableIntents(Arrays.asList(intents)).setMemberCachePolicy(MemberCachePolicy.ALL).build();
    }

    JavacChecker checker() {
        final int entriesSize = data.systems.welcomeMessage.fields.entries.size();
        final int bufferSize = data.service.buffer_size;
        final int servicePool = data.service.service_pool;
        final int min_command_length = data.command.text.min_length;
        final int max_command_length = data.command.text.max_length;
        final int cooldown_pool = data.command.text.cooldown_pool;
        final var guild = data.guild;

        var checker = new JavacChecker()
                .addMinSizeCheck(bufferSize, 50, "Buffer Size")
                .addMaxSizeCheck(bufferSize, 1000, "Buffer Size")
                .addMinSizeCheck(servicePool, 0, "Service Pool")
                .addMaxSizeCheck(servicePool, 10, "Service Pool")
                .addMinSizeCheck(min_command_length, 2, "Minimum Command Length")
                .addMaxSizeCheck(min_command_length, 5, "Minimum Command Length")
                .addMaxSizeCheck(max_command_length, 15, "Maximum Command Length")
                .addMinSizeCheck(max_command_length, 7, "Maximum Command Length")
                .addMaxSizeCheck(cooldown_pool, 10, "Cooldown Pool")
                .addMinSizeCheck(cooldown_pool, 1, "Cooldown Pool")
                .addPlaceHolderCheck(data.bot.prefix)
                .addPlaceHolderCheck(guild.guild_id)
                .addPlaceHolderCheck(guild.channels.general)
                .addPlaceHolderCheck(guild.channels.member_count)
                .addPlaceHolderCheck(guild.channels.log)
                .addPlaceHolderCheck(guild.roles.member)
                .addEqualCheck(entriesSize % 2, 0, "Entries size must be pair")
                .addMaxSizeCheck(entriesSize, 10, "Entries size");

        if (data.systems.welcomeMessage.fields.suggested_channel_list.size() == 2) {
            checker.addPlaceHolderCheck(data.systems.welcomeMessage.fields.suggested_channel_list.getFirst(), "Placeholder value used for first Suggested Channel entry.")
            .addPlaceHolderCheck(data.systems.welcomeMessage.fields.suggested_channel_list.get(1), "Placeholder value used for second Suggested Channel entry.");
        }

        return checker;
    }

    void init(String token) throws InterruptedException {
        // Check for Config errors
        checker().check();

        // Build ShardManager with Intents and Listener
        build(token, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT);

        // Wait until login is complete
        shardManager.getShards().getFirst().getSelfUser().getJDA().awaitReady();

        // Set buffer
        var guildMessageBuffer = new GuildMessageBuffer(data.service.buffer_size);

        // Register Logs
        logManager.registerLog(new DeletedMessageLogger(guildMessageBuffer));
        logManager.registerLog(new UpdatedMessageLogger(guildMessageBuffer));

        // Set up and start the services
        serviceManager.addService("count", new Count(GuildUtils.getGuild(data.guild.guild_id)));
        serviceManager.start("count");

        // Set up commands
        commandManager.getRegistry().addCommand("ping", new Ping());
        commandManager.getRegistry().addCommand("sendEmbed", new SendEmbed());

        // Add Listener
        shardManager.addEventListener(new Listener(commandManager, logManager, guildMessageBuffer));
    }
}
