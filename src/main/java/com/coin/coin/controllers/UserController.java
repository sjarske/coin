package com.coin.coin.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Account;
import com.coin.coin.models.Role;
import com.coin.coin.models.User;
import com.coin.coin.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController @RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?> saveRole(@RequestBody RoleToUserForm form){
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/exchange/addtouser")
    public ResponseEntity<?> saveExchange(@RequestBody ExchangeToUserForm form){
        userService.addExchangeToUser(form.getUsername(), form.getName(), form.getApiKey(), form.getSecretKey());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader !=null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);
                String acces_token = JWT.create().withSubject(user.getUsername()).withExpiresAt(new Date(System.currentTimeMillis() +10 * 60 * 1000)).withIssuer(request.getRequestURL().toString()).withClaim("roles",user.getRoles().stream().map(Role::getName).collect(Collectors.toList())).sign(algorithm);
                Map<String,String> tokens = new HashMap<>();
                tokens.put("acces_token",acces_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            }catch (Exception e){
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                //response.sendError(FORBIDDEN.value());
                Map<String,String> error = new HashMap<>();
                error.put("error_message",e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
        }else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    @GetMapping("/user/wallet")
    public List getAccountBalances(){
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("TWdALenejiWRJjzQTP4vDKFPcwsfsdVcbZE4Qs5O3K33BrgiCZuRxnNhnM5tHt5A", "Zpp1zcKffSd3YEsWP9R0B5Hw5nLunB3huivzwNWfAFJT7k4CQ2MyGOcD7MHLMRzT");
        BinanceApiRestClient client = factory.newRestClient();

        Account account = client.getAccount();
        var filtered = account.getBalances().stream().filter(assetBalance -> !Objects.equals(assetBalance.getFree(), "0.00000000")).filter(assetBalance -> !Objects.equals(assetBalance.getFree(), "0.00") ).collect(Collectors.toList());
        return filtered;
    }

    @GetMapping("/user/wallet/test")
    public void getAccountBalancesTest(){
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("aoENyRgtqNkeH5FVFqDqB0QoU4r6OR6XN187tI0KuwE2JZTWBaM4vYYwaU6nuX9p", "Ni7aHTlCMTht0qhxeUakmdnYc5WifkjQxJpBnidYwLapovcXvZ99qQm7gNLbsgaW",true,true);
        BinanceApiRestClient client = factory.newRestClient();

        Account account = client.getAccount();
        System.out.println(account.getBalances());
        System.out.println(account.getAssetBalance("ETH").getFree());
    }
}

@Data
class RoleToUserForm{
    private String username;
    private String roleName;
}

@Data
class ExchangeToUserForm{
    private String username;
    private String name;
    private String apiKey;
    private String secretKey;
}