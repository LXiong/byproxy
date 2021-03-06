package net.dongliu.byproxy.ui.task;

import javafx.concurrent.Task;
import net.dongliu.byproxy.Context;
import net.dongliu.byproxy.setting.KeyStoreSetting;
import net.dongliu.byproxy.setting.ProxySetting;
import net.dongliu.byproxy.setting.ServerSetting;
import net.dongliu.byproxy.setting.Settings;
import net.dongliu.byproxy.ssl.RootKeyStoreGenerator;
import net.dongliu.byproxy.ui.UIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * When start bazehe, run this task
 *
 * @author Liu Dong
 */
public class InitContextTask extends Task<Void> {
    private static final Logger logger = LoggerFactory.getLogger(InitContextTask.class);
    private Context context;

    public InitContextTask(Context context) {
        this.context = context;
    }

    @Override
    public Void call() throws Exception {
        // load serverSetting
        updateMessage("Loading serverSetting file...");
        Path configPath = ServerSetting.configPath();
        updateProgress(1, 10);
        ServerSetting serverSetting;
        KeyStoreSetting keyStoreSetting;
        ProxySetting proxySetting;
        if (Files.exists(configPath)) {
            try (InputStream in = Files.newInputStream(configPath);
                 BufferedInputStream bin = new BufferedInputStream(in);
                 ObjectInputStream oin = new ObjectInputStream(bin)) {
                serverSetting = (ServerSetting) oin.readObject();
                keyStoreSetting = (KeyStoreSetting) oin.readObject();
                proxySetting = (ProxySetting) oin.readObject();
            }
        } else {
            serverSetting = ServerSetting.getDefault();
            keyStoreSetting = KeyStoreSetting.getDefault();
            proxySetting = ProxySetting.getDefault();
        }
        updateProgress(3, 10);

        updateMessage("Loading key store file...");
        Path keyStorePath = Paths.get(keyStoreSetting.usedKeyStore());
        char[] keyStorePassword = keyStoreSetting.usedPassword().toCharArray();
        if (!Files.exists(keyStorePath)) {
            if (!keyStoreSetting.isUseCustom()) {
                logger.info("Generate new key store file");
                updateMessage("Generating new key store...");
                // generate one new key store
                RootKeyStoreGenerator generator = RootKeyStoreGenerator.getInstance();
                byte[] keyStoreData = generator.generate(keyStorePassword, Settings.rootCertValidityDays);
                Files.write(keyStorePath, keyStoreData);
            } else {
                UIUtils.showMessageDialog("KeyStore file not found");
                //TODO: How to deal with this?
            }

        }
        context.setServerSetting(serverSetting);
        context.setKeyStoreSetting(keyStoreSetting);
        updateProgress(8, 10);
        context.setProxySetting(proxySetting);
        updateProgress(10, 10);
        return null;
    }
}
