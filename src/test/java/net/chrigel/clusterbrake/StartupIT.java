package net.chrigel.clusterbrake;

import java.io.File;
import java.io.IOException;
import net.chrigel.clusterbrake.workflow.manualauto.DirTypes;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;

/**
 *
 */
public class StartupIT {

    private static final File TEST_BASE_DIR = TestUtility.getTestDir();

    private final File testSource = new File(TestUtility.getTestResourcesDir(), "testsample.mp4");

    @Before
    public void setup() {
        TestUtility.initDirs();
        TEST_BASE_DIR.mkdir();
    }

    @After
    public void after() throws IOException {
        FileUtils.deleteDirectory(TEST_BASE_DIR);
    }

    @Test
    public void testManaualTranscode() throws Exception {

        DirTypes.INPUT_AUTO.getBase().mkdirs();
        DirTypes.INPUT_MANUAL.getBase().mkdirs();
        DirTypes.TEMPLATE.getBase().mkdirs();
     //   DirTypes.OUTPUT_MANUAL.getBase().mkdirs();
     //   DirTypes.OUTPUT_AUTO.getBase().mkdirs();
        FileUtils.copyFile(testSource, new File(DirTypes.INPUT_MANUAL.getBase(), "test/testsample.mp4"));
        FileUtils.copyFile(
                new File(TestUtility.getTestResourcesDir(), "test.conf"),
                new File(DirTypes.TEMPLATE.getBase(), "test.conf"));

        Startup.main(new String[]{"target/clusterbrake.properties"});

        Thread.sleep(1000000000);
    }

}
