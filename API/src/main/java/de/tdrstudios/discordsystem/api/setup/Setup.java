package de.tdrstudios.discordsystem.api.setup;

import de.tdrstudios.discordsystem.utils.JsonDocument;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public abstract class Setup {
    public abstract void onComplete(JsonDocument document);
    public abstract void onCancel();
    public void initSetup() {}
    @Getter
    private boolean privateChannel;
    @Getter
    private long channelId;
    private JsonDocument data = new JsonDocument();

    private List<SetupStep> steps = new ArrayList<>();
    private int currentStepIndex = -1;
    private JDA jda;

    public SetupStep getCurrentStep() {
        try {
            return steps.get(currentStepIndex);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public TextChannel getTextChannel(Guild guild) {
        if (privateChannel) throw new UnsupportedOperationException();
        return guild.getTextChannelById(channelId);
    }

    public PrivateChannel getPrivateChannel(JDA jda) {
        if (!privateChannel) throw new UnsupportedOperationException();
        return jda.getPrivateChannelById(channelId);
    }

    public Setup() {
        initSetup();
    }

    public void start(MessageChannel channel) {
        privateChannel = channel instanceof PrivateChannel;
        channelId = channel.getIdLong();
        jda = channel.getJDA();
        nextStep();
    }

    public void nextStep() {
        currentStepIndex++;
        SetupStep currentStep = getCurrentStep();
        if (currentStep == null) {
            onComplete(data);
            return;
        }
        currentStep.execute(this, isPrivateChannel() ? getPrivateChannel(jda) : jda.getTextChannelById(channelId));
    }

    public void cancel() {
        for (SetupStep step : steps) {
            step.cancel();
        }
        onCancel();
    }

    public void completeStep(JsonDocument data) {
        this.data.addDocument(getCurrentStep().getIdentifier(), data);
        nextStep();
    }
    public void completeStep(Number data) {
        this.data.add(getCurrentStep().getIdentifier(), data);
        nextStep();
    }

    public void completeStep(Boolean data) {
        this.data.add(getCurrentStep().getIdentifier(), data);
        nextStep();
    }

    public void completeStep(Character data) {
        this.data.add(getCurrentStep().getIdentifier(), data);
        nextStep();
    }

    public void completeStep(String data) {
        this.data.add(getCurrentStep().getIdentifier(), data);
        nextStep();
    }

    public void addStep(SetupStep step) {
        steps.add(step);
    }
}
