package net.chrigel.clusterbrake.statemachine.states;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.util.Callback;
import net.chrigel.clusterbrake.statemachine.State;
import net.chrigel.clusterbrake.statemachine.StateContext;
import net.chrigel.clusterbrake.statemachine.Trigger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Provides a base class for states.
 */
public abstract class AbstractState
        implements State {

    private final AtomicBoolean isActive;
    private final StateContext context;
    protected final Logger logger;
    private final Map<Class<? extends Trigger>, StateTriggerPair> conditionalMap;

    public AbstractState(StateContext context) {
        this.logger = LogManager.getLogger(getClass());
        this.isActive = new AtomicBoolean();
        this.context = context;
        this.conditionalMap = new HashMap<>();
    }

    /**
     * Binds another state to a trigger. If the trigger activates, then the passed state will be transitioned to.
     *
     * @param <T> a subtype of Trigger.
     * @param state the state.
     * @param trigger the trigger class.
     * @throws IllegalStateException if trigger is already bound.
     */
    public final <T extends Trigger> void bindNextStateToTrigger(State state, Class<T> trigger) {
        AbstractState.this.bindNextStateToTrigger(state, trigger, null);
    }

    /**
     * Binds another state to a trigger. If the trigger activates, then the passed state will be transitioned to.
     *
     * @param <T> a subtype of Trigger.
     * @param state the state.
     * @param trigger the trigger class.
     * @param callback a callback after triggering a state. If null, the callback will be disabled. It will be called
     * before exiting state and entering next state.
     * @throws IllegalStateException if trigger is already bound.
     */
    public final <T extends Trigger> void bindNextStateToTrigger(
            State state, Class<T> trigger, Callback<T, Void> callback) {
        synchronized (conditionalMap) {
            if (conditionalMap.containsKey(trigger)) {
                throw new IllegalStateException("trigger already bound.");
            } else {
                conditionalMap.put(trigger, new StateTriggerPair(state, callback));
            }
        }
    }

    @Override
    public final void enter() {
        isActive.set(true);
        enterState();
        logger.debug("Entered {}.", this);
    }

    @Override
    public final void exit() {
        exitState();
        isActive.set(false);
        logger.debug("Exited {}.", this);
    }

    /**
     * This method is meant to be overriden and is basically identical to {@link State#exit()}, except that after this
     * method, a log statement will be made and the state will be marked inactive (no need to log something like "exited
     * state x").
     */
    protected abstract void enterState();

    /**
     * This method is meant to be overriden and is basically identical to {@link State#enter() }, except that after this
     * method, a log statement will be made and the state will be marked active (no need to log something like "entered
     * state x").
     */
    protected abstract void exitState();

    /**
     * When calling this method, the state will transition to the next state, based on the passed trigger. For the state
     * transition to happen, a state must be bound first. If the trigger is not recognized, nothing will happen and the
     * state remains in its current state.
     *
     * @param trigger any trigger action, for which a listener has been bound.
     * @throws NullPointerException if parameter is null.
     */
    protected final void fireStateTrigger(Trigger trigger) {
        if (conditionalMap.containsKey(trigger.getClass())) {
            StateTriggerPair pair = conditionalMap.get(trigger.getClass());
            if (pair.getCallback() != null) {
                pair.getCallback().call(trigger);
            }
            changeStateTo(pair.getState());
        } else {
            logger.error("Conditional wanted to change state, but there is no subsequent state defined for {}.",
                    trigger.getClass());
        }
    }

    private void changeStateTo(State state) {
        if (isActive.get()) {
            context.setState(state);
        } else {
            logger.warn("Tried changing state while being inactive!");
        }
    }

    protected StateContext getContext() {
        return context;
    }

    @Override
    public final boolean isActive() {
        return isActive.get();
    }

}
