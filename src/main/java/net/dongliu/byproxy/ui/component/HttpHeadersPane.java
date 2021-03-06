package net.dongliu.byproxy.ui.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import net.dongliu.byproxy.struct.Headers;

import java.io.IOException;

import static java.util.stream.Collectors.joining;

/**
 * Show one single http request or response
 *
 * @author Liu Dong
 */
public class HttpHeadersPane extends SplitPane {
    @FXML
    private TextArea cookieText;
    @FXML
    private TextArea headerText;

    private ObjectProperty<Headers> httpHeader = new SimpleObjectProperty<>();

    public HttpHeadersPane() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/http_header.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
    }

    @FXML
    private void initialize() {
        httpHeader.addListener((o, old, message) -> {
            if (message == null) {
                headerText.clear();
                cookieText.clear();
                return;
            }
            headerText.setText(String.join("\n", httpHeader.get().toRawLines()));
            String s = httpHeader.get().getCookieValues().stream()
                    .map(c -> c.getName() + "=" + c.getValue())
                    .collect(joining("\n"));
            cookieText.setText(s);
        });
    }

    public void setHttpHeader(Headers headers) {
        this.httpHeader.set(headers);
    }
}
