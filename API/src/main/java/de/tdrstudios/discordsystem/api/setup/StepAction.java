package de.tdrstudios.discordsystem.api.setup;

public interface StepAction<T> {
    T getValue();
    Setup getSetup();
    SetupStep getStep();
}
