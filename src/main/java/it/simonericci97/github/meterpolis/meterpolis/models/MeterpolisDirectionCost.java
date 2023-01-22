package it.simonericci97.github.meterpolis.meterpolis.models;

import com.google.maps.model.Fare;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * represent the cost for a direction
 */

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@Accessors(chain = true)
public class MeterpolisDirectionCost {

    /**
     * represents the price
     */
    private BigDecimal value;

    /**
     * represents the currency in which the price is calculated. Default EUR
     */
    private Currency currency;

    @Override
    public String toString() {
        return  this.value + " " + this.currency;
    }

    public static MeterpolisDirectionCost free() {
        return new MeterpolisDirectionCost(BigDecimal.ZERO, Currency.getInstance("EUR"));
    }
}
