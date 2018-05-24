/*
Brooke Porter
xsadrithx@yahoo.com
4/28/18

Final_Porter_Brooke

CS 17.11, Section 6991: Sean Kirkpatrick

This class takes a string from QuoteData, and converts it
into an ArrayList of characters for Input to compare against
and Controller to display in a label.
*/

package edu.srjc.porter.brooke.wpm.Data;

import java.util.ArrayList;
import java.util.Random;

public class Quote
{
    public ArrayList<Character> charList()
    {
        QuoteData quoteData = new QuoteData();
        Random random = new Random();

        int num = random.nextInt(75) + 1;
        String quote = quoteData.getQuote(num);

        ArrayList<Character> charList = new ArrayList<>();

        String line = "";

        line = quote.trim();
        int size = line.length();

        for (int i = 0; i < size; i++)
        {
            try
            {
                charList.add(line.charAt(i));
            }
            catch (Exception ex)
            {
                System.out.printf("Error: %s", ex.getMessage());
            }
        }

        return charList;
    }
}