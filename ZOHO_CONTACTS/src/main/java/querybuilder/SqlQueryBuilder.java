package querybuilder;




import java.util.ArrayList;
import java.util.List;

public class  SqlQueryBuilder {
    private StringBuilder query;
    private List<Object> parameters; 

    public  SqlQueryBuilder() {
        query = new StringBuilder();
        parameters = new ArrayList<>();
    }

    public  SqlQueryBuilder select(String... columns) {
        query.append("SELECT ");
        query.append(String.join(", ", columns));
        query.append(" ");
        return this;
    }

    public  SqlQueryBuilder from(String table) {
        query.append("FROM ").append(table).append(" ");
        return this;
    }

    public  SqlQueryBuilder where(String condition, Object... params) {
        query.append("WHERE ").append(condition).append(" ");
        addParameters(params);
        return this;
    }

    public  SqlQueryBuilder and(String condition, Object... params) {
        query.append("AND ").append(condition).append(" ");
        addParameters(params);
        return this;
    }

    public  SqlQueryBuilder or(String condition, Object... params) {
        query.append("OR ").append(condition).append(" ");
        addParameters(params);
        return this;
    }

    public String build() {
        return query.toString().trim() + ";"; 
    }

    public List<Object> getParameters() {
        return parameters;
    }

    private void addParameters(Object... params) {
        for (Object param : params) {
            parameters.add(param);
        }
    }
}

