package net.javac;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.javac.buffer.impl.GuildMessageBuffer;
import net.javac.command.CommandManager;
import net.javac.command.general.Ping;
import net.javac.config.ConfigLoader;
import net.javac.config.ModelConfig;
import net.javac.log.LogManager;
import net.javac.log.impl.DeletedMessageLogger;
import net.javac.log.impl.UpdatedMessageLogger;
import net.javac.service.ServiceManager;
import net.javac.service.impl.Count;
import net.javac.utils.GuildUtils;

import java.util.Arrays;

public class Javac {
    static ShardManager shardManager;
    static final Dotenv dotenv = Dotenv.configure().load();
    final ModelConfig data = ConfigLoader.getData();
    final LogManager logManager = new LogManager();
    final ServiceManager serviceManager = new ServiceManager(data.config.service_pool);
    final CommandManager commandManager = new CommandManager(data.commands.text_commands.cooldown_pool);

    static void main() {
        final var token = dotenv.get("TOKEN");
        if (token == null) throw new RuntimeException("TOKEN not found!");
        try {
            new Javac().init(token);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    void build(String token, GatewayIntent... intents) {
        shardManager = DefaultShardManagerBuilder.createDefault(token).enableIntents(Arrays.asList(intents)).setMemberCachePolicy(MemberCachePolicy.ALL).build();
    }

    void init(String token) throws InterruptedException {
        // Build ShardManager with Intents and Listener
        build(token, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT);

        // Wait until login is complete
        shardManager.getShards().getFirst().getSelfUser().getJDA().awaitReady();

        // Set buffer
        var guildMessageBuffer = new GuildMessageBuffer(data.config.buffer_size);

        // Register Logs
        logManager.registerLog(new DeletedMessageLogger(guildMessageBuffer));
        logManager.registerLog(new UpdatedMessageLogger(guildMessageBuffer));

        // Set up and start the services
        serviceManager.addService("count", new Count(GuildUtils.getGuild(data.guild.guild_id)));
        serviceManager.start("count");

        // Set up commands
        commandManager.getRegistry().addCommand("ping", new Ping());

        // Add Listener
        shardManager.addEventListener(new Listener(commandManager, logManager, guildMessageBuffer));
    }

    @SuppressWarnings("unused")
    public static Dotenv getDotenv() {
        return dotenv;
    }

    public static ShardManager getShardManager() {
        return shardManager;
    }
}
