package net.chrigel.clusterbrake.statemachine.trigger;

import net.chrigel.clusterbrake.settings.Job;

/**
 *
 */
public class TranscodingFinishedTrigger
        extends GenericPayloadTrigger<Job> {

    public TranscodingFinishedTrigger(Job job) {
        super(job);
    }

}
