package no.auke.drone.dao;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by huyduong on 4/24/2015.
 */
public class QueryBuilder {
    
	public final String EQUAL = "=";
    public final String MORE_THAN = ">";
    public final String MORE_THAN_EQUAL = ">=";
    public final String LESS_THAN = "<";
    public final String LESS_THAN_EQUAL = "=<";
    public final String STAR = "*";


    private StringBuilder sb;

    private String prepareWhereClause(Properties properties) {
        return prepareWhereClause(properties, EQUAL);
    }

    private String prepareWhereClause(Properties properties, String operator) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ");
        String leftQuotation = "";
        String rightQuotation = "";
        if(StringUtils.isNotEmpty(operator) && operator.equalsIgnoreCase(EQUAL)) {
            leftQuotation = " '";
            rightQuotation = "' ";
        }

        if(properties != null && properties.size() > 0) {
            List<String> strings = new ArrayList<>();
            for(Object o : properties.keySet()) {
                strings.add(o.toString() + " " + operator + " " + leftQuotation + properties.get(o).toString() +  rightQuotation);
            }
            sb.append(StringUtils.join(strings.toArray()," AND "));
        }
        return sb.toString();
    }


    public QueryBuilder() {
        sb = new StringBuilder();
    }

    public QueryBuilder buildSelect(String select, String entity) {
        sb.append("SELECT ");
        sb.append(select);
        sb.append(" FROM ");
        sb.append(entity);
        return this;
    }

    public QueryBuilder buildSelect(String entity) {
        return buildSelect(STAR,entity);
    }

    public QueryBuilder buildWhere() {
        sb.append(" WHERE ");
        return this;
    }

    public QueryBuilder buildEqualClause(Properties properties) {
        sb.append(prepareWhereClause(properties));
        return this;
    }

    public QueryBuilder buildMoreThanClause(Properties properties) {
        sb.append(prepareWhereClause(properties,MORE_THAN));
        return this;
    }

    public QueryBuilder buildParam(String param) {
        sb.append(" " + param + " ");
        return this;
    }


    public QueryBuilder buildInnerQuery(String query) {
        sb.append("(" + query + ")");
        return this;
    }

    public QueryBuilder buildMoreThan() {
        sb.append(" " + MORE_THAN_EQUAL + " ");
        return this;
    }

    public QueryBuilder buildMoreThanEqualClause(Properties properties) {
        sb.append(prepareWhereClause(properties,MORE_THAN_EQUAL));
        return this;
    }

    public QueryBuilder buildLessThanClause(Properties properties) {
        sb.append(prepareWhereClause(properties,LESS_THAN));
        return this;
    }

    public QueryBuilder buildLessThanEqualClause(Properties properties) {
        sb.append(prepareWhereClause(properties,LESS_THAN_EQUAL));
        return this;
    }

    public String build() {
        return sb.toString();
    }
}
