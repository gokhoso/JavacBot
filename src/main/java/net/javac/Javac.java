package net.javac;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.javac.buffer.BufferManager;
import net.javac.buffer.impl.GuildMessageBuffer;

import java.util.Arrays;

public class Javac {
    static ShardManager shardManager;
    static Dotenv dotenv = Dotenv.configure().load();
    final BufferManager bufferManager = new BufferManager();

    static void main() {
        var token = dotenv.get("TOKEN");
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
        build(token, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT);
        shardManager.getShards().getFirst().getSelfUser().getJDA().awaitReady();

        bufferManager.registerBuffer("guild_message", new GuildMessageBuffer(100));
        GuildMessageBuffer guildMessage = (GuildMessageBuffer) bufferManager.getBuffer("guild_message");
        shardManager.addEventListener(new Listener(guildMessage));
    }
}
