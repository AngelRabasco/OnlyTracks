module org.AngelRabasco.OnlyTracks {
	requires transitive javafx.graphics;
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires spring.security.crypto;

	opens org.AngelRabasco.OnlyTracks to javafx.fxml;
	opens org.AngelRabasco.OnlyTracks.Model to javafx.base;

	exports org.AngelRabasco.OnlyTracks;
}
