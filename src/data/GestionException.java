package data;

import java.sql.SQLException;

public class GestionException extends SQLException{
	public GestionException(String err) {
		super(err);
	}
}
