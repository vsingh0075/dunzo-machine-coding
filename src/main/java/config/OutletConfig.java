package config;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *  Class to parse Outlet portion in the JSON input.
 */
public class OutletConfig {
    @JsonProperty("count_n")
    private Integer countN;

    public Integer getCountN() {
        return countN;
    }

    public void setCountN(Integer countN) {
        this.countN = countN;
    }
}
