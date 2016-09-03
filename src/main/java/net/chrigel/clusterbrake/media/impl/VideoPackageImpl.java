package net.chrigel.clusterbrake.media.impl;

import java.io.File;
import net.chrigel.clusterbrake.media.Video;
import net.chrigel.clusterbrake.media.VideoOptionPackage;
import net.chrigel.clusterbrake.media.VideoPackage;

/**
 *
 */
class VideoPackageImpl
        implements VideoPackage {

    private Video video;
    private File outputFile;
    private VideoOptionPackage settings;

    @Override
    public Video getVideo() {
        return video;
    }

    @Override
    public void setVideo(Video video) {
        this.video = video;
    }

    @Override
    public File getOutputFile() {
        return outputFile;
    }

    @Override
    public void setOutputFile(File file) {
        this.outputFile = file;
    }

    @Override
    public VideoOptionPackage getSettings() {
        return settings;
    }

    @Override
    public void setSettings(VideoOptionPackage settings) {
        this.settings = settings;
    }

}