package net.dongliu.byproxy.setting;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Liu Dong
 */
public class Settings {

    public static final int rootCertValidityDays = 3650;

    public static final int certValidityDays = 10;
    public static final char[] rootKeyStorePassword = "123456".toCharArray();
    public static final char[] keyStorePassword = "123456".toCharArray();

    private static final Path parentPath = Paths.get(System.getProperty("user.home"), ".ByProxy");

    public static Path getParentPath() {
        if (!Files.exists(parentPath)) {
            try {
                Files.createDirectory(parentPath);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return parentPath;

    }
}
