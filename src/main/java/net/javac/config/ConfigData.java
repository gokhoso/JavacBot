package net.javac.config;

import java.util.List;

@SuppressWarnings("unused")
public class ConfigData {
    public Bot bot;
    public Service service;
    public Command command;
    public Guild guild;
    public Systems systems;

    public static class Bot {
        public List<String> owners;
        public String prefix;
    }

    public static class Service {
        public int buffer_size;
        public int service_pool;
    }

    public static class Command {
        public String on_cooldown;
        public Text text;
        public HelpCommand help_command;
        public static class HelpCommand {
            public String title;
            public String description;
            public int bot_thumbnail;
            public String color;
            public String footer;
        }
        public static class Text {
            public int min_length;
            public int max_length;
            public int cooldown_pool;
        }
    }

    public static class Guild {
        public String guild_id;
        public String member_count_channel_name;
        public int channels_enabled;
        public Channels channels;
        public Roles roles;
        public static class Channels {
            public String general;
            public String log;
            public String member_count;
        }

        public static class Roles {
            public String member;
        }
    }

    public static class Systems {
        public Bump bump;
        public WelcomeMessage welcomeMessage;

        public static class Bump {
            public on_bump on_bump;
            public reminder_bump reminder_bump;

            public static class on_bump {
                public String title;
                public String description;
                public String color;
                public int guild_thumbnail;
                public String image;
                public String footer;
            }
            public static class reminder_bump {
                public String title;
                public String description;
                public String color;
                public int guild_thumbnail;
                public String image;
                public String footer;
            }
        }

        public static class WelcomeMessage {
            public String title;
            public String description;
            public String color;
            public Fields fields;
            public String footer;
            public String banner;

            public static class Fields {
                public int enabled;
                public List<String> entries;
                public int suggested_channels;
                public String suggested_channels_title;
                public List<String> suggested_channel_list;
            }
        }

    }
}
