module org.AngelRabasco.OnlyTracks {
	requires transitive javafx.graphics;
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;

	opens org.AngelRabasco.OnlyTracks to javafx.fxml;

	exports org.AngelRabasco.OnlyTracks;
}
