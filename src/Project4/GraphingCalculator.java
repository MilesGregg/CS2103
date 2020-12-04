import javafx.application.Application;
import javafx.scene.chart.*;
import java.util.*;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GraphingCalculator extends Application {
	public static void main (String[] args) {
		launch(args);
	}

	protected static final int WINDOW_WIDTH = 600, WINDOW_HEIGHT = 500;
	protected static final String EXAMPLE_EXPRESSION = "2*x+5*x*x";
	protected final ExpressionParser expressionParser = new SimpleExpressionParser();

	@Override
	public void start (Stage primaryStage) {
		primaryStage.setTitle("Graph Calculator");

		final Pane queryPane = new HBox();
		final Label label = new Label("y=");
		final TextField textField = new TextField(EXAMPLE_EXPRESSION);
		final Button button = new Button("Graph");
		queryPane.getChildren().add(label);
		queryPane.getChildren().add(textField);

		final Pane graphPane = new Pane();
		final LineChart<Number, Number> chart = new LineChart<Number, Number>(new NumberAxis(-10, +10, 5), new NumberAxis(-10, +10, 5));
		chart.setLegendVisible(false);
		chart.setCreateSymbols(false);
		graphPane.getChildren().add(chart);
		button.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle (MouseEvent e) {
				try {
					final Expression expression = expressionParser.parse(textField.getText());
					final XYChart.Series series = new XYChart.Series();
					for (double x = -10; x <= +10; x += 0.01) {
						final double y = expression.evaluate(x);
						series.getData().add(new XYChart.Data(x, y));
					}
					chart.getData().clear();
					chart.getData().addAll(series);
				} catch (ExpressionParseException epe) {
					textField.setStyle("-fx-text-fill: red");
				}
			}
		});
		queryPane.getChildren().add(button);

		textField.setOnKeyPressed(e -> textField.setStyle("-fx-text-fill: black"));
		
		final BorderPane root = new BorderPane();
		root.setTop(queryPane);
		root.setCenter(graphPane);

		final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
