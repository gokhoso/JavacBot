package net.javac;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.javac.buffer.impl.GuildMessageBuffer;
import net.javac.config.ConfigLoader;
import net.javac.log.LogManager;
import net.javac.log.impl.DeletedMessageLogger;
import net.javac.log.impl.UpdatedMessageLogger;

import java.util.Arrays;

public class Javac {
    static ShardManager shardManager;
    static final Dotenv dotenv = Dotenv.configure().load();
    final LogManager logManager = new LogManager();

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

        var guildMessageBuffer = new GuildMessageBuffer(ConfigLoader.getData().config.buffer_size);

        // Register Logs
        logManager.registerLog(new DeletedMessageLogger(guildMessageBuffer));
        logManager.registerLog(new UpdatedMessageLogger(guildMessageBuffer));

        // Add Listener
        shardManager.addEventListener(new Listener(guildMessageBuffer, logManager));
    }

    @SuppressWarnings("unused")
    public static Dotenv getDotenv() {
        return dotenv;
    }

    public static ShardManager getShardManager() {
        return shardManager;
    }
}
