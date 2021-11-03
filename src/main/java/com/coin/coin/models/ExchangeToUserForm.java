package com.coin.coin.models;

import lombok.Data;

@Data
public
class ExchangeToUserForm {
    private String username;
    private String name;
    private String apiKey;
    private String secretKey;
}
