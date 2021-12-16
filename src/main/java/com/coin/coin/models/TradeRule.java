package com.coin.coin.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeRule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    //create
    private int coinValueOnCreate;
    private String coinName;
    //if
    private String coinIndicator;
    private String coinCondition;
    private int ifPercentage;
    //then
    private int thenPercentage;
    private String marketAction;
}
