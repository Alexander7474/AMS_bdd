package data;

public enum fieldType {
	NUMERIC, VARCHAR, FLOAT8, INT4, SERIAL, BIGSERIAL, DATE;
	
    public static fieldType getSqlType(String sqlType) {
        switch (sqlType.toUpperCase()) {
            case "VARCHAR":
            case "BPCHAR":
            case "TEXT":
                return VARCHAR;
            case "INT4":
                return INT4;
            case "FLOAT8":
                return FLOAT8;
            case "SERIAL":
            	return SERIAL;
            case "BIGSERIAL":
            	return BIGSERIAL;
            case "DATE":
            	return DATE;
        }
		return null;
    }
}
