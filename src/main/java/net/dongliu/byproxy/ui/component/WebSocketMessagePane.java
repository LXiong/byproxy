package net.dongliu.byproxy.ui.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import net.dongliu.byproxy.struct.WebSocketMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Liu Dong
 */
public class WebSocketMessagePane extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketMessagePane.class);
    @FXML
    private HttpBodyPane httpBodyPane;
    @FXML
    private Text description;

    private ObjectProperty<WebSocketMessage> message = new SimpleObjectProperty<>();

    public WebSocketMessagePane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/web_socket_message.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    @FXML
    void initialize() {
        message.addListener((ob, o, n) -> {
            if (n == null) {
                description.setText("");
                this.setCenter(new Text(""));
                return;
            }
            description.setText("WebSocket " + (n.isRequest() ? "Request" : "Response"));
            if (n.getType() == 2) {
                this.setCenter(new Text("Binary message"));
                return;
            }

            httpBodyPane.setBody(n.getBody());
        });
    }

    public WebSocketMessage getMessage() {
        return message.get();
    }

    public ObjectProperty<WebSocketMessage> messageProperty() {
        return message;
    }

    public void setMessage(WebSocketMessage message) {
        this.message.set(message);
    }
}
