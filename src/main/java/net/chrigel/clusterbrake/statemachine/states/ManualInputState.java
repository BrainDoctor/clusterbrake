package net.chrigel.clusterbrake.statemachine.states;

import com.google.inject.Inject;
import net.chrigel.clusterbrake.settings.InputSettings;
import net.chrigel.clusterbrake.statemachine.StateContext;

/**
 *
 */
class ManualInputState
        extends AbstractState {

    private final InputSettings inputSettings;

    @Inject
    ManualInputState(StateContext context, InputSettings inputSettings) {
        super(context);
        this.inputSettings = inputSettings;
    }

    @Override
    protected void enterState() {
       
    }

    @Override
    protected void exitState() {

    }

}
