package com.dave;
import com.dave.Person;

interface IStockQuoteService
{
        String getQuote(in String ticker, in Person requester);
}
