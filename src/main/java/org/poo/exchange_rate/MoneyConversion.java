package org.poo.exchange_rate;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.ExchangeInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

@Getter
@Setter
public final class MoneyConversion {
    private ArrayList<ExchangeRate> exchangeRates = new ArrayList<>();
    private HashMap<String, ArrayList<String>> links = new HashMap<>();
    private HashMap<String, Integer> visited = new HashMap<>();

    public MoneyConversion(final ExchangeInput[] rates) {
        for (ExchangeInput rate : rates) {
            ExchangeRate exchange = new ExchangeRate(rate.getFrom(), rate.getTo(), rate.getRate(),
                    rate.getTimestamp());
            exchangeRates.add(exchange);
            completeList(rate.getFrom(), rate.getTo());
            visited.put(rate.getFrom(), 0);
        }
        addComplement();
    }

    /**
     * It adds a new neighbor to a string which means that string is directly connected
     * to this new added value
     * @param key representing the string which receives a new neighbor
     * @param valueToAdd representing the new neighbor to be added
     */

    public void completeList(final String key, final String valueToAdd) {
        if (!links.containsKey(key)) {
            links.put(key, new ArrayList<>());
        }
        links.get(key).add(valueToAdd);
    }

    /**
     * For every `ExchangeRate` element, It creates the opposite transformation (another element),
     * with `to` field as `from` and vice versa. The new rate is also calculated. This concept
     * helps if the bank wants to change two currencies, but the `to` cannot be founded through
     * the array.
     */

    public void addComplement() {
        ArrayList<ExchangeRate> complement = new ArrayList<>();
        for (ExchangeRate rate : exchangeRates) {
            double newRate = 1 / rate.getRate();
            complement.add(new ExchangeRate(rate.getTo(), rate.getFrom(), newRate,
                    rate.getTimestamp()));
            completeList(rate.getTo(), rate.getFrom());
            visited.put(rate.getTo(), 0);
        }
        exchangeRates.addAll(complement);
    }

    /**
     * It performs a `DFS` algorithm to complete the fastest road of currencies between `from` and
     * `to`
     * @param from the start currency
     * @param to the final currency
     * @param amount is passed to the `moneyConvert` method
     * @return the converted amount
     */

    public double convertMoney(final String from, final String to, final double amount) {
        for (String key : links.keySet()) {
            visited.put(key, 0);
        }
        Stack<String> stack = new Stack<>();
        ArrayList<String> roadToConverse = new ArrayList<>();
        stack.push(from);
        visited.put(from, 1);
        roadToConverse.add(from);
        while (!stack.isEmpty()) {
            String top = stack.peek();
            if (top.equals(to)) {
                return moneyConvert(roadToConverse, amount);
            }
            boolean hasNeighbour = false;
            ArrayList<String> list = links.get(top);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals(to)) {
                    roadToConverse.add(list.get(i));
                    return moneyConvert(roadToConverse, amount);
                }
            }
            for (int i = 0; i < list.size(); i++) {
                if (visited.get(list.get(i)) == 0) {
                    hasNeighbour = true;
                    roadToConverse.add(list.get(i));
                    visited.put(list.get(i), 1);
                    stack.push(list.get(i));
                    break;
                }
            }
            if (!hasNeighbour) {
                stack.pop();
                roadToConverse.removeLast();
            }
        }
        return amount;
    }

    /**
     * It creates an array completed with rates (the first two currencies completes the first
     * element and so on). It multiplies these values and obtains the needed rate to can converse
     * @param road representing the array of currencies
     * @param amount representing the amount to be converted
     * @return the converted amount
     */

    public double moneyConvert(final ArrayList<String> road, final double amount) {
        ArrayList<Double> rates = new ArrayList<>();
        for (int i = 0; i < road.size() - 1; i++) {
            int j = i + 1;
            String from = road.get(i);
            String to = road.get(j);
            for (ExchangeRate rate : exchangeRates) {
                if (rate.getFrom().equals(from) && rate.getTo().equals(to)) {
                    rates.add(rate.getRate());
                }
            }
        }
        double newRate = 1;
        for (Double rate : rates) {
            newRate *= rate;
        }
        return newRate * amount;
    }
}
