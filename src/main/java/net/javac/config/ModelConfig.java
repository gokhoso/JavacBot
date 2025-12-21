package net.javac.config;

@SuppressWarnings("unused")
public class ModelConfig {
    public Bot bot;
    public Config config;
    public Channels channels;
    public Roles roles;

    public static class Bot {
        public String prefix;
        public String guild_id;
    }

    public static class Config {
        public int buffer_size;
        public int service_pool;
    }

    public static class Channels {
        public String general;
        public String log;
        public String count;
    }

    public static class Roles {
        public String member;
    }
}
