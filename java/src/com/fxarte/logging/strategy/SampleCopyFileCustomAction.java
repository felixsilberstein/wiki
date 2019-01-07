package package com.fxarte.logging.strategy;

import org.apache.logging.log4j.core.appender.rolling.action.AbstractAction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SampleCopyFileCustomAction extends AbstractAction {

    /**
     * Source.
     */
    private final File source;

    public SampleCopyFileCustomAction(File source) {
        this.source = source;
    }

    /**
     * Rename file.
     *
     * @return true if successfully renamed.
     */
    @Override
    public boolean execute() {
        return execute(source);
    }



    public static boolean execute(final File source) {
        File dest = new File(String.format("/tmp/test__%s", source.getName()));
        LOGGER.info("-- - - - - Copying file {} to {}.", source.getName(), dest.getAbsolutePath());
        try {
            Files.copy(source.toPath(), dest.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
