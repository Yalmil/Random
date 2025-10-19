import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import io.github.cdimascio.dotenv.Dotenv;

public class Main extends ListenerAdapter {
    
    public static void main(String[] args) {
        // Load environment variables from .env file
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("DISCORD_TOKEN");
        
        if (token == null || token.isEmpty()) {
            System.err.println("Error: DISCORD_TOKEN not found in .env file");
            return;
        }
        
        try {
            // Build the bot
            JDA jda = JDABuilder.createDefault(token)
                    .enableIntents(
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.MESSAGE_CONTENT,
                            GatewayIntent.GUILD_MEMBERS
                    )
                    .addEventListeners(new Main())
                    .setActivity(Activity.playing("!help for commands"))
                    .build();
            
            jda.awaitReady();
            System.out.println("Bot is ready!");
            
        } catch (Exception e) {
            System.err.println("Error starting bot: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // Ignore bot messages
        if (event.getAuthor().isBot()) return;
        
        String message = event.getMessage().getContentRaw();
        
        // Command: !ping
        if (message.equalsIgnoreCase("!ping")) {
            event.getChannel().sendMessage("🏓 Pong! Latency: " + 
                    event.getJDA().getGatewayPing() + "ms").queue();
        }
        
        // Command: !hello
        if (message.equalsIgnoreCase("!hello")) {
            event.getChannel().sendMessage("👋 Hello " + 
                    event.getAuthor().getAsMention() + "!").queue();
        }
        
        // Command: !help
        if (message.equalsIgnoreCase("!help")) {
            String helpMessage = """
                    **Available Commands:**
                    `!ping` - Check bot latency
                    `!hello` - Get a greeting
                    `!help` - Show this help message
                    """;
            event.getChannel().sendMessage(helpMessage).queue();
        }
    }
}
