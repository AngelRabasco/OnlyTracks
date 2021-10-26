module org.AngelRabasco.OnlyTracks {
	requires transitive javafx.graphics;
	requires javafx.controls;
	requires javafx.fxml;

	opens org.AngelRabasco.OnlyTracks to javafx.fxml;

	exports org.AngelRabasco.OnlyTracks;
}
