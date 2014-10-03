package in.folder.jdbi.util;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.StatementLocator;

import java.io.InputStream;
import java.util.Scanner;

public class QueryLocator implements StatementLocator {

    public String locate(String name) throws Exception {
        InputStream resource = QueryLocator.class.getResourceAsStream("/queries/" + name + ".sql");
        if (resource == null) {
            throw new RuntimeException("Unable to find any query for '" + name + "'");
        }

        return getQueryFromJar(resource);
    }

    private String getQueryFromJar(InputStream resource) {
        Scanner scanner = new Scanner(resource);
        String query = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return query;
    }

    @Override
    public String locate(String name, StatementContext ctx) throws Exception {
        return locate(name);
    }
}
