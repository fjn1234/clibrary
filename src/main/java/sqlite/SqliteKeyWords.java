package sqlite;


public class SqliteKeyWords {
    private SqliteKeyWords() {
    }

    public static final String INTEGER = " INTEGER ";
    public static final String REAL = " REAL ";
    public static final String NONE = " NONE ";
    public static final String FLOAT = " FLOAT ";
    public static final String BOOLEAN = " BOOLEAN ";
    public static final String TIMESTAMP = " TIMESTAMP ";
    public static final String DATE = " DATE ";
    public static final String TIME = " TIME ";
    public static final String TEXT = " TEXT ";
    public static final String NUMERIC = " NUMERIC ";
    public static final String VARCHAR = " VARCHAR(%s) ";
    public static final String DECIMAL = " decimal(%s,%s) ";
    public static final String NVARCHAR = " NVARCHAR(%s) ";
    public static final String CHAR = " CHAR(%S) ";
    public static final String DEFAULT = " DEFAULT ";
    public static final String AUTOINCREMENT = " AUTOINCREMENT ";
    public static final String PRIMARY_KEY = " PRIMARY KEY ";
    public static final int LONG_LENGTH = 19;
    public static final int TIME_LENGTH = 13;

    public static String getChar(int length) {
        int len = length;
        if (len < 1)
            len = 1;
        return String.format(CHAR, len + "");
    }

    public static String getVarchar(int length) {
        int len = length;
        if (len < 1)
            len = 1;
        return String.format(VARCHAR, len + "");
    }

    public static String getNvarchar(int length) {
        int len = length;
        if (len < 1)
            len = 1;
        return String.format(NVARCHAR, len + "");
    }

    public static String getDecimal(int length, int point) {
        int len = length;
        int p = point;
        if (len < 1)
            len = 1;
        if (p < 0)
            p = 0;
        return String.format(DECIMAL, len + "", p + "");
    }

    public static String getFixedColumnType(String type, int length, int point) {
        switch (type) {
            case VARCHAR:
                return getVarchar(length);
            case NVARCHAR:
                return getNvarchar(length);
            case CHAR:
                return getChar(length);
            case DECIMAL:
                return getDecimal(length, point);
            default:
                return "";
        }
    }
}
