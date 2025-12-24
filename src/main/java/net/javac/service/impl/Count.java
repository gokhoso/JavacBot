package net.javac.service.impl;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.javac.config.ConfigLoader;
import net.javac.config.ModelConfig;
import net.javac.service.IService;
import net.javac.utils.GuildUtils;
import net.javac.utils.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Count implements IService {
    private final Logger logger = LoggerFactory.getLogger(Count.class);
    private final ModelConfig data = ConfigLoader.getData();
    private final Guild guild;
    private final VoiceChannel channel;
    private final int initialDelay;
    private final int period;
    private final TimeUnit timeType;
    private ScheduledFuture<?> serviceLoop;

    public Count(Guild guild) {
        this.guild = guild;
        channel = guild.getVoiceChannelById(ConfigLoader.getData().channels.count);
        initialDelay = 1;
        period = 15;
        timeType = TimeUnit.MINUTES;
    }

    @Override
    public Runnable service() {
        if (channel == null) {
            logger.error("Voice channel is null! failed to set member count");
            return null;
        }
        return () -> {
            final String channelName = TextUtils.setCountVariable(data.guild.member_count_channel_name, guild.getId());
            final int count = GuildUtils.getCount(guild.getId());
            logger.info("Updating member count to {}", count);
            channel.getManager().setName(channelName).queue();
        };
    }

    @Override
    public void run(ScheduledExecutorService executor) {
        logger.info("[Services] Member count service is started.");
        serviceLoop = executor.scheduleAtFixedRate(service(), initialDelay, period, timeType);
    }

    @Override
    public void stop(boolean mayInterruptIfRunning) {
        this.serviceLoop.cancel(mayInterruptIfRunning);
        serviceLoop = null;
    }

    @Override
    public boolean isServiceLoopStarted() {
        return serviceLoop != null;
    }

    @Override
    public int getInitialDelay() {
        return initialDelay;
    }

    @Override
    public int getPeriod() {
        return period;
    }

    @Override
    public TimeUnit getTimeType() {
        return timeType;
    }
}
