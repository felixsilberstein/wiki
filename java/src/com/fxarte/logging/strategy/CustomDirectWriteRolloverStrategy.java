package com.fxarte.logging.strategy;

import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.appender.rolling.DirectWriteRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.appender.rolling.RolloverDescription;
import org.apache.logging.log4j.core.appender.rolling.RolloverDescriptionImpl;
import org.apache.logging.log4j.core.appender.rolling.action.Action;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.util.Integers;

import java.io.File;
import java.util.zip.Deflater;

/**
 * Extends DirectWriteRolloverStrategy to allow for the implementation of custom Actions
 * Here a simple copy to /tmp/ with rename is included
 * @references:
 * https://github.com/apache/logging-log4j2/blob/log4j-2.11.1/log4j-core/src/main/java/org/apache/logging/log4j/core/appender/rolling/action/FileRenameAction.java
 * https://stackoverflow.com/questions/29745938/log4j2-2-1-custom-plugin-not-detected-by-packages-attribute
 * https://stackoverflow.com/questions/30030875/how-to-write-custom-web-service-appender-using-log4j2
 * https://logging.apache.org/log4j/2.x/manual/extending.html#Plugin_Builders
 * https://stackoverflow.com/questions/32378201/in-log4j2-how-to-configure-renameemptyfiles-to-be-false-for-the-rollingfile-app
 */
@Plugin(name = "CustomDirectWriteRolloverStrategy", category = Core.CATEGORY_NAME, printObject = true)
public class CustomDirectWriteRolloverStrategy extends DirectWriteRolloverStrategy {
    private static final int DEFAULT_MAX_FILES = 7;

    protected CustomDirectWriteRolloverStrategy(int maxFiles, int compressionLevel, StrSubstitutor strSubstitutor, Action[] customActions, boolean stopCustomActionsOnError, String tempCompressedFilePatternString) {
        super(maxFiles, compressionLevel, strSubstitutor, customActions, stopCustomActionsOnError, tempCompressedFilePatternString);
    }


    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder() {
            @Override
            public DirectWriteRolloverStrategy build() {
                String maxFiles = getMaxFiles();
                String compressionLevelStr = getCompressionLevelStr();
                Configuration config = getConfig();
                Action[] customActions = getCustomActions();
                boolean stopCustomActionsOnError = isStopCustomActionsOnError();
                String tempCompressedFilePattern = getTempCompressedFilePattern();

                int maxIndex = Integer.MAX_VALUE;
                if (maxFiles != null) {
                    maxIndex = Integer.parseInt(maxFiles);
                    if (maxIndex < 0) {
                        maxIndex = Integer.MAX_VALUE;
                    } else if (maxIndex < 2) {
                        LOGGER.error("Maximum files too small. Limited to " + DEFAULT_MAX_FILES);
                        maxIndex = DEFAULT_MAX_FILES;
                    }
                }
                final int compressionLevel = Integers.parseInt(compressionLevelStr, Deflater.DEFAULT_COMPRESSION);
                return new CustomDirectWriteRolloverStrategy(maxIndex, compressionLevel, config.getStrSubstitutor(),
                        customActions, stopCustomActionsOnError, tempCompressedFilePattern);
            }
        };
    }

    @Override
    public RolloverDescription rollover(final RollingFileManager manager) throws SecurityException {
        RolloverDescription d = super.rollover(manager);
        final File source = new File(d.getActiveFileName());
        return new RolloverDescriptionImpl(d.getActiveFileName(), false, null, new SampleCopyFileCustomAction(source));
    }

}
