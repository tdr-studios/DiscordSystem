package de.tdrstudios.discordsystem.core.api.implementation;

import com.google.inject.Singleton;
import de.tdrstudios.discordsystem.api.Discord;
import de.tdrstudios.discordsystem.api.DiscordService;
import de.tdrstudios.discordsystem.api.event.EventHandler;
import de.tdrstudios.discordsystem.api.event.EventService;
import de.tdrstudios.discordsystem.api.event.events.discord.ListenerAdapter;
import de.tdrstudios.discordsystem.api.modules.ModuleAction;
import de.tdrstudios.discordsystem.api.modules.ModuleService;
import de.tdrstudios.discordsystem.api.services.CreateService;
import de.tdrstudios.discordsystem.utils.JsonConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@Singleton
@CreateService(1)
public class CoreDiscordService implements DiscordService {

    private ShardManager shardManager;

    @Override
    public ShardManager getShardManager() {
        return shardManager;
    }

    @Override
    public void start() {
        if (shardManager != null) return;
        JsonConfig config = Discord.getConfig();
        try {
            shardManager = DefaultShardManagerBuilder.createDefault(config.getString("bot.token")).build();
            shardManager.addEventListener(Discord.getInstance(ListenerAdapter.class));
            for (JDA shard : shardManager.getShards()) {
                shard.awaitReady();
            }
        } catch (LoginException | CompletionException e) {
            System.err.println("Failed to start the DiscordAPI! "+e.getCause().getMessage());
            Discord.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        shardManager.setStatus(OnlineStatus.valueOf(config.getString("bot.activity.status").toUpperCase()));
        shardManager.setActivity(Activity.of(Activity.ActivityType.valueOf(config.getString("bot.activity.action").toUpperCase()), config.getString("bot.activity.activity")));
    }

    @Override
    public void stop() {
        if (shardManager == null) return;
        shardManager.setStatus(OnlineStatus.OFFLINE);
        try {
            TimeUnit.MILLISECONDS.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        shardManager.shutdown();
    }

    @Override
    public void initialize() throws Exception {
        start();
        Discord.getInstance(EventService.class).scanForEvents(this.getClass());
        Discord.getInstance(ModuleService.class).callAction(ModuleAction.DISCORD_READY);
    }

    @EventHandler
    public void onJdaReady(ReadyEvent event) {
        JDA.ShardInfo shardInfo = event.getJDA().getShardInfo();
        System.out.println("Shard "+shardInfo.getShardId()+"/"+shardInfo.getShardTotal()+" is now ready");
    }
}
