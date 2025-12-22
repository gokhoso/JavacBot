package net.javac.config;

import java.util.List;

@SuppressWarnings("unused")
public class ModelConfig {
    public Bot bot;
    public Config config;
    public WelcomeMessage welcomeMessage;
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

    public static class WelcomeMessage {
        public String title;
        public String description;
        public Fields fields;

        public static class Fields {
            public int enabled;
            public int number_of_fields;
            public List<String> entries;
            public int suggested_channels;
            public String suggested_channels_title;
            public List<String> suggested_channel_list;
        }
        public String footer;
        public String banner;
    }


    public static class Channels {
        public String general;
        public String log;
        public String count;
        public String about;
        public String rules;
    }

    public static class Roles {
        public String member;
    }
}
