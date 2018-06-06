/*
Brooke Porter
xsadrithx@yahoo.com
5/20/18

Final_Porter_Brooke

CS 17.11, Section 6991: Sean Kirkpatrick

This class randomly picks a row from quotes.sqlite and gets the
string to give to the Quote class, to be used in the game.
*/

package edu.srjc.porter.brooke.wpm.Data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.DriverManager.getConnection;

class QuoteData
{
    private static final String database = "jdbc:sqlite::resource:quotes.sqlite";

    String getQuote(Integer num)
    {
        Connection connection = null;
        Statement statement = null;

        try
        {
            connection = getConnection(database);
            statement = connection.createStatement();
            statement.setQueryTimeout(30);

        }
        catch (SQLException ex)
        {
            System.err.println(String.format("Fatal Error: Cannot connect to %s database, %s", database, ex.getMessage()));
            System.exit(0);
        }

        ResultSet result = null;
        String query = "SELECT text FROM Quotes WHERE ID LIKE " + num;

        try
        {
            result = statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            System.err.println(String.format("Query failed: %s", query));
        }

        if (result != null)
        {
            try
            {
                String text = result.getString("text");
                result.close();
                return text;
            }
            catch (SQLException ex)
            {
                System.err.println("Problem converting result to string");
            }
        }
        return "couldn't get quote";
    }
}
