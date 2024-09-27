package com.obi.service;

import com.obi.dto.CoinDto;
import com.obi.response.ApiResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ChatBotSericeImpl implements ChatbotService{

    private double convertToDouble(Object value){
        if(value instanceof Integer){
            return ((Integer) value).doubleValue();
        } else if(value instanceof Double){
            return (Double) value;
        } else if(value instanceof Long){
            return ((Long) value).doubleValue();
        }

        else throw new IllegalArgumentException("unsupported type" + value.getClass().getName());
    }

    public CoinDto makeApiRequest (String currencyName) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/ethereum";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<Map> responseEntity = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> responseBody = responseEntity.getBody();

        if(responseBody != null){
            Map<String, Object> image = (Map<String, Object>) responseBody.get("image");
            Map<String, Object> marketData = (Map<String, Object>) responseBody.get("market_data");
            CoinDto coinDto = new CoinDto();
            coinDto.setId((String) responseBody.get("id"));
            coinDto.setName((String) responseBody.get("name"));
            coinDto.setSymbol((String) responseBody.get("symbol"));
            coinDto.setImage((String)image.get("large"));

//            market data
            coinDto.setCurrentPrice(convertToDouble(((Map<String, Object>) marketData.get("current_price")).
                    get("usd")));
            coinDto.setMarketCap(convertToDouble(((Map<String, Object>) marketData.get("market_cap"))
                    .get("usd")));
            coinDto.setMarketCapRank(convertToDouble(((int) marketData.get("market_cap_rank"))));
            coinDto.setTotalVolume(convertToDouble(((Map<String, Object>) marketData.get("total_volume")).
                    get("usd")));
            coinDto.setHigh24h(convertToDouble(((Map<String, Object>) marketData.get("high_24h")).get("usd")));
            coinDto.setLow24h(convertToDouble(((Map<String, Object>) marketData.get("low_24h")).get("usd")));
            coinDto.setPriceChange24h(convertToDouble((marketData.get("price_change_24h"))));
            coinDto.setPriceChangePercentage24h(convertToDouble((
                     marketData.get("price_change_percentage_24h"))));
            coinDto.setMarketCapChange24h(convertToDouble((
                     marketData.get("market_cap_change_24h"))));
            coinDto.setMarketCapChangePercentage24h(convertToDouble((
                     marketData.get("market_cap_change_percentage_24h"))));
            coinDto.setCirculatingSupply(convertToDouble(( marketData.get("circulating_supply"))));
            coinDto.setTotalSupply(convertToDouble((marketData.get("total_supply"))));

            return coinDto;
        }
        throw new Exception("coin not found");
    }
    @Override
    public ApiResponse getCoinDetails(String prompt) throws Exception {
        CoinDto coinDto = makeApiRequest(prompt);
        System.out.println("coinDto = " + coinDto);
        return null;
    }

    @Override
    public String simpleChat(String prompt) {
        String GEMINI_API_URL = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        String requestBody = new JSONObj
        return "";
    }
}
